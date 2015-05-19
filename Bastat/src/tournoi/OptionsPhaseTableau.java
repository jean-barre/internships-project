package com.enseirb.pfa.bastats.tournoi;

import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;

import com.enseirb.pfa.bastats.R;
import com.enseirb.pfa.bastats.data.DAO.DBPhasePouleDAO;
import com.enseirb.pfa.bastats.data.DAO.DBPhaseTableauDAO;
import com.enseirb.pfa.bastats.data.DAO.DBTournoiDAO;
import com.enseirb.pfa.bastats.data.DAO.DBTournoiEquipeDAO;
import com.enseirb.pfa.bastats.data.model.PhaseTableau;

public class OptionsPhaseTableau extends ActionBarActivity {
    private static final int REQUEST_SELECTION = 11;

    private int phaseId;
    private int tournoiId;
    private int nbEquipes;
    private boolean petiteFinale = false;
    private Button buttonNbEquipes;

    private TextView viewNbEquipes;

    private Context mCtx;

    private Button buttonValider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_phase_tableau);

        mCtx = this;

        phaseId = getIntent().getIntExtra(KeyTable.ARG_ID_PHASE,-1);
        tournoiId = getIntent().getIntExtra(KeyTable.ARG_ID_TOURNOI,-1);
        DBTournoiDAO tableTournoi = new DBTournoiDAO(mCtx);
        nbEquipes = tableTournoi.getWithId(tournoiId).getNbEquipeMax();

        viewNbEquipes = (TextView) findViewById(R.id.nb_equipes);
        viewNbEquipes.setText("0");

        buttonValider = (Button) findViewById(R.id.button_valider);

        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewNbEquipes.getText().toString().equals("0"))
                    createAlert(getResources().getString(R.string.missing_information),
                            "Attention vous n'avez pas défini le nombre d'équipes pour cette phase ");
                else {
                    int phaseTableauId = -1;

                    DBPhaseTableauDAO tablePhaseTableau = new DBPhaseTableauDAO(mCtx);
                    PhaseTableau phaseTableau = new PhaseTableau(phaseId);
                    tablePhaseTableau.insert(phaseTableau);

                    phaseTableauId = tablePhaseTableau.getLast().getId();
                    Log.d("PhaseTableau", "Récupération phaseTableauId: " + phaseTableauId);
                    Intent intent = new Intent(mCtx, RepartitionTableau.class);
                    intent.putExtra(KeyTable.NB_EQUIPES, Integer.parseInt(viewNbEquipes.getText().toString()));
                    intent.putExtra(KeyTable.ARG_PHASE_POULE_ID, phaseTableauId);
                    intent.putExtra(KeyTable.ARG_ID_TOURNOI, tournoiId);
                    intent.putExtra("PetiteFinale", petiteFinale);
                    startActivityForResult(intent, REQUEST_SELECTION);
                }
            }
        });

        buttonNbEquipes = (Button) findViewById(R.id.button_nb_equipes);
        buttonNbEquipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberPicker("Nombre d'équipes",2,nbEquipes, viewNbEquipes);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_options_phase_tableau, menu);
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

    public void onPetiteFinale(View view){
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.petiteYes:
                if (checked)
                    petiteFinale = true;
                break;
            case R.id.petiteNo:
                if (checked)
                    petiteFinale = false;
                break;
        }
    }

    private void showNumberPicker(String title, int min, int max, TextView textView){
        final Dialog d = new Dialog(mCtx);
        final TextView tv = textView;
        d.setTitle(title);
        d.setContentView(R.layout.dialog_number_picker);
        Button positiveButton = (Button) d.findViewById(R.id.positiveButton);
        Button negativeButton = (Button) d.findViewById(R.id.negativeButton);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker);
        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        np.setMaxValue(max);
        np.setMinValue(min);
        positiveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                tv.setText(String.valueOf(np.getValue()));
                d.dismiss();
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }

    private void createAlert(String title, String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mCtx);
        alertDialogBuilder.setTitle(title);

        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton(getResources().getString(R.string.dialog_retour), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        Intent result = new Intent();
        setResult(RESULT_CANCELED, result);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECTION) {
            if (resultCode == RESULT_OK) {
                Intent result = new Intent();
                setResult(RESULT_OK, result);
                finish();
            } else if (resultCode == RESULT_CANCELED){
                DBPhaseTableauDAO tablePhaseTableau = new DBPhaseTableauDAO(mCtx);
                tablePhaseTableau.removeWithId(tablePhaseTableau.getLast().getId());
                Log.d("TAG", "suppression phaseTableau");
            }

        }
    }
    // TODO SharedPreference pour la duree des périodes et la durée des matchs
}
