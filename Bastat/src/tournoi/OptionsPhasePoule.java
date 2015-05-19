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
import android.widget.Switch;
import android.widget.TextView;

import com.enseirb.pfa.bastats.R;
import com.enseirb.pfa.bastats.data.DAO.DBPhaseDAO;
import com.enseirb.pfa.bastats.data.DAO.DBPhasePouleDAO;
import com.enseirb.pfa.bastats.data.model.PhasePoule;
import com.enseirb.pfa.bastats.tournoi.fragment.OverviewTournoi;


public class OptionsPhasePoule extends ActionBarActivity {
    private static final int REQUEST_SELECTION = 11;

    private int phaseId;
    private int tournoiId;

    private Context mCtx;

    private Button nbPoules;
    private Button nbConfrontations;
    private Button nbPeriodes;
    private Button dureePeriode;
    private Button buttonValider;

    private TextView textViewPoules;
    private TextView textViewConfrontations;
    private TextView textViewPeriodes;
    private TextView textViewDuree;
    private TextView valeurVictoire;
    private TextView valeurNul;
    private TextView valeurDefaite;

    private Switch repartitionAuto;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_phase_poule);

        mCtx = this;

        phaseId = getIntent().getIntExtra(KeyTable.ARG_ID_PHASE,-1);
        tournoiId = getIntent().getIntExtra(KeyTable.ARG_ID_TOURNOI,-1);

        nbPoules = (Button) findViewById(R.id.button_nb_poule);
        nbConfrontations = (Button) findViewById(R.id.button_nb_confrontations);
        nbPeriodes = (Button) findViewById(R.id.button_nb_periodes);
        dureePeriode = (Button) findViewById(R.id.button_duree_periode);
        buttonValider = (Button) findViewById(R.id.button_valider);

        textViewPoules = (TextView) findViewById(R.id.textView_nb_poules);
        textViewConfrontations = (TextView) findViewById(R.id.textView_nombre_confrontation);
        textViewPeriodes = (TextView) findViewById(R.id.textView_nombre_periode);
        textViewDuree = (TextView) findViewById(R.id.textView_duree_periode);
        valeurDefaite = (TextView) findViewById(R.id.defaite_value);
        valeurNul = (TextView) findViewById(R.id.nul_value);
        valeurVictoire = (TextView) findViewById(R.id.victoire_value);

        repartitionAuto = (Switch) findViewById(R.id.switch1);

        nbPoules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberPicker("Nombre de poules",1,24,textViewPoules);
            }
        });

        nbConfrontations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberPicker("Nombre de confrontations",1,10,textViewConfrontations);
            }
        });

        nbPeriodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberPicker("Nombre de périodes",1,4,textViewPeriodes);
            }
        });

        dureePeriode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker("Durée d'une période",0,59,textViewDuree);
            }
        });

        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textViewPoules.getText().toString().equals("0"))
                    createAlert(getResources().getString(R.string.missing_information),
                            "Attention vous n'avez pas défini le nombre de poules à créer pour " +
                                    "la phase en cours");
                else if (textViewPeriodes.getText().toString().equals("0"))
                    createAlert(getResources().getString(R.string.missing_information),
                            "Attention vous n'avez pas défini le nombre de périodes à jouer par mach");
                else if (textViewConfrontations.getText().toString().equals("0"))
                    createAlert(getResources().getString(R.string.missing_information),
                            "Attention vous n'avez pas défini le nombre de confrontations par entre chaque équipe");
                else if (textViewDuree.getText().toString().equals("00:00"))
                    createAlert(getResources().getString(R.string.missing_information),
                            "Attention vous n'avez pas défini de temps de jeu pour les match");
                else {

                    int phasePouleId = -1;

                    DBPhasePouleDAO tablePhasePoule = new DBPhasePouleDAO(mCtx);
                    PhasePoule phasePoule = new PhasePoule(phaseId,
                            Integer.parseInt(textViewPoules.getText().toString()),
                            Integer.parseInt(textViewPeriodes.getText().toString()),
                            textViewDuree.getText().toString());
                    tablePhasePoule.insert(phasePoule);

                    phasePouleId = tablePhasePoule.getLast().getId();
                    Log.d("TAG", "Récupération phasePouleId: " + phasePouleId);

                    Intent intent = new Intent(mCtx, RepartitionPouleActivity.class);
                    intent.putExtra(KeyTable.ARG_NB_POULES, phasePoule.getNbPoule());
                    intent.putExtra(KeyTable.ARG_PTS_V, Integer.parseInt(valeurVictoire.getText().toString()));
                    intent.putExtra(KeyTable.ARG_PTS_N, Integer.parseInt(valeurNul.getText().toString()));
                    intent.putExtra(KeyTable.ARG_PTS_D, Integer.parseInt(valeurDefaite.getText().toString()));
                    intent.putExtra(KeyTable.ARG_NB_CONFRONTATIONS, Integer.parseInt(textViewConfrontations.getText().toString()));
                    intent.putExtra(KeyTable.ARG_GENERATION_AUTO, repartitionAuto.isChecked());
                    intent.putExtra(KeyTable.ARG_PHASE_POULE_ID, phasePouleId);
                    intent.putExtra(KeyTable.ARG_ID_TOURNOI, tournoiId);
                    Log.d("TAG", "options poules tournoiId:" + tournoiId);
                    startActivityForResult(intent, REQUEST_SELECTION);
                }
            }
        });

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

    private void showTimePicker(String title, int min, int max, TextView textView){
        final Dialog d = new Dialog(mCtx);
        final TextView tv = textView;
        d.setTitle(title);
        d.setContentView(R.layout.dialog_time_picker);
        Button positiveButton = (Button) d.findViewById(R.id.positiveButton);
        Button negativeButton = (Button) d.findViewById(R.id.negativeButton);

        //secondes
        final NumberPicker pickerSecondes = (NumberPicker) d.findViewById(R.id.secondesPicker);
        pickerSecondes.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        pickerSecondes.setMaxValue(max);
        pickerSecondes.setMinValue(min);
        pickerSecondes.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });

        //minutes
        final NumberPicker pickerMinutes = (NumberPicker) d.findViewById(R.id.minutesPicker);
        pickerMinutes.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        pickerMinutes.setMaxValue(max);
        pickerMinutes.setMinValue(min);
        pickerMinutes.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });

        //bouttons
        positiveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                tv.setText(String.format("%02d",pickerMinutes.getValue()) +":" +String.format("%02d",pickerSecondes.getValue()));
                d.dismiss();
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_options_phase_poule, menu);
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

    public void valeurDefaite(View view){
        final Dialog d = new Dialog(mCtx);
        final TextView tv = valeurDefaite;
        d.setTitle(getResources().getString(R.string.pick_a_value));
        d.setContentView(R.layout.dialog_number_picker);
        Button positiveButton = (Button) d.findViewById(R.id.positiveButton);
        Button negativeButton = (Button) d.findViewById(R.id.negativeButton);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker);
        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        np.setMaxValue(100);
        np.setMinValue(0);
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

    public void valeurNul(View view){
        final Dialog d = new Dialog(mCtx);
        final TextView tv = valeurNul;
        d.setTitle(getResources().getString(R.string.pick_a_value));
        d.setContentView(R.layout.dialog_number_picker);
        Button positiveButton = (Button) d.findViewById(R.id.positiveButton);
        Button negativeButton = (Button) d.findViewById(R.id.negativeButton);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker);
        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        np.setMaxValue(100);
        np.setMinValue(0);
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

    public void valeurVictoire(View view){
        final Dialog d = new Dialog(mCtx);
        final TextView tv = valeurVictoire;
        d.setTitle(getResources().getString(R.string.pick_a_value));
        d.setContentView(R.layout.dialog_number_picker);
        Button positiveButton = (Button) d.findViewById(R.id.positiveButton);
        Button negativeButton = (Button) d.findViewById(R.id.negativeButton);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker);
        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        np.setMaxValue(100);
        np.setMinValue(0);
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
                DBPhasePouleDAO tablePhasePoule = new DBPhasePouleDAO(mCtx);
                tablePhasePoule.removeWithId(tablePhasePoule.getLast().getId());
                Log.d("TAG", "suppression phasePoule");
            }

        }
    }
}
