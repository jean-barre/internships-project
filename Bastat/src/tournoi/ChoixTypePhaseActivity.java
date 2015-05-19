package com.enseirb.pfa.bastats.tournoi;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.enseirb.pfa.bastats.R;
import com.enseirb.pfa.bastats.data.DAO.DBPhaseDAO;
import com.enseirb.pfa.bastats.data.DAO.DBTournoiDAO;
import com.enseirb.pfa.bastats.data.model.Phase;

import java.net.Inet4Address;
import java.util.Calendar;

public class ChoixTypePhaseActivity extends ActionBarActivity {
    private static final int REQUEST_NOUVELLE_PHASE = 99;

    private Button buttonFinale;
    private Button buttonPoule;
    private EditText editNomPḧase;
    private TextView pDisplayDate;
    private Button pPickDate;
    private int pYear;
    private int pMonth;
    private int pDay;
    /** This integer will uniquely define the dialog to be used for displaying date picker.*/
    static final int DATE_DIALOG_ID = 0;

    private Context mCtx;
    private int nbEquipesNext;
    private int tournoiId;
    private int nbEquipesTournoi;
    private boolean takeClt = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_type_phase);

        mCtx = this;

        pDisplayDate = (TextView) findViewById(R.id.date_phase);
        pPickDate = (Button) findViewById(R.id.pickDate);

        /** Listener for click event of the button */
        pPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        /** Get the current date */
        final Calendar cal = Calendar.getInstance();
        pYear = cal.get(Calendar.YEAR);
        pMonth = cal.get(Calendar.MONTH);
        pDay = cal.get(Calendar.DAY_OF_MONTH);
        updateDisplay();

        tournoiId = getIntent().getIntExtra(KeyTable.ARG_ID_TOURNOI, -1);
        DBTournoiDAO tableTournoi = new DBTournoiDAO(this);
        nbEquipesTournoi = tableTournoi.getWithId(tournoiId).getNbEquipeMax();

        buttonFinale = (Button) findViewById(R.id.buttonFinale);
        buttonPoule = (Button) findViewById(R.id.buttonPoule);
        editNomPḧase = (EditText) findViewById(R.id.editText_nom_phase);


        buttonPoule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editNomPḧase.getText().toString().trim().isEmpty())
                    createAlert();
                else {
                    lancerPoule();
                }
            }
        });

        buttonFinale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editNomPḧase.getText().toString().trim().isEmpty())
                    createAlert();
                else {
                    lancerTableau();
                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choix_type_phase, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createAlert(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mCtx);
        alertDialogBuilder.setTitle(getResources().getString(R.string.missing_information));

        alertDialogBuilder
                .setMessage("Vous n'avez pas saisie de nom pour la phase en cours")
                .setCancelable(false)
                .setNegativeButton(getResources().getString(R.string.dialog_retour), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private DatePickerDialog.OnDateSetListener pDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    pYear = year;
                    pMonth = monthOfYear;
                    pDay = dayOfMonth;
                    updateDisplay();
                    displayToast();
                }
            };

    /** Updates the date in the TextView */
    private void updateDisplay() {
        pDisplayDate.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(pDay).append("/")
                        .append(pMonth + 1).append("/")
                        .append(pYear).append(" "));
    }

    /** Displays a notification when the date is updated */
    private void displayToast() {
        Toast.makeText(this, new StringBuilder().append("Date choosen is ").append(pDisplayDate.getText()), Toast.LENGTH_SHORT).show();

    }

    private void lancerPoule(){
        DBPhaseDAO tablePhase = new DBPhaseDAO(mCtx);
        int phaseId = -1;
        Phase phase = new Phase(getIntent().getIntExtra(KeyTable.ARG_ID_TOURNOI,-1),
                getIntent().getIntExtra(KeyTable.ARG_NUM_PHASE,-1),
                editNomPḧase.getText().toString().trim(),
                Phase.TYPE_POULE);
        tablePhase.insert(phase);
        phaseId = tablePhase.getLast().getId();
        Log.d("ChoixPhase", "insertion phase "+phaseId+" liée au tournoi"+phase.getTournoiId());
        Log.d("TAG", "Récupération id phase: "+phaseId);
        Intent intent = new Intent(mCtx, OptionsPhasePoule.class);
        intent.putExtra(KeyTable.ARG_ID_PHASE, phaseId);
        intent.putExtra(KeyTable.ARG_ID_TOURNOI, getIntent().getIntExtra(KeyTable.ARG_ID_TOURNOI, -1));
        startActivityForResult(intent, REQUEST_NOUVELLE_PHASE);
    }

    private void lancerTableau(){
        DBPhaseDAO tablePhase = new DBPhaseDAO(mCtx);
        int phaseId = -1;
        Phase phase = new Phase(getIntent().getIntExtra(KeyTable.ARG_ID_TOURNOI,-1),
                getIntent().getIntExtra(KeyTable.ARG_NUM_PHASE,-1),
                editNomPḧase.getText().toString().trim(),
                Phase.TYPE_TABLEAU);
        tablePhase.insert(phase);
        phaseId = tablePhase.getLast().getId();
        Log.d("ChoixPhase", "insertion phase "+phaseId+" liée au tournoi"+phase.getTournoiId());

        Log.d("TAG", "Récupération id phase: "+phaseId);
        Intent intent = new Intent(mCtx, OptionsPhaseTableau.class);
        intent.putExtra(KeyTable.ARG_ID_PHASE, phaseId);
        intent.putExtra(KeyTable.ARG_ID_TOURNOI, getIntent().getIntExtra(KeyTable.ARG_ID_TOURNOI, -1));
        startActivityForResult(intent, REQUEST_NOUVELLE_PHASE);
    }

    /** Create a new dialog for date picker */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        pDateSetListener,
                        pYear, pMonth, pDay);
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_NOUVELLE_PHASE) {
            if (resultCode == RESULT_OK) {
                Intent result = new Intent();
                setResult(RESULT_OK, result);
                finish();
            } else if (resultCode == RESULT_CANCELED){
                DBPhaseDAO tablePhase = new DBPhaseDAO(mCtx);
                tablePhase.removeWithId(tablePhase.getLast().getId());
                Log.d("TAG", "suppression phase");
            }
        }
    }
}
