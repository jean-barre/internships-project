package com.enseirb.pfa.bastats.tournoi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.enseirb.pfa.bastats.R;
import com.enseirb.pfa.bastats.data.DAO.DBEquipeDAO;
import com.enseirb.pfa.bastats.data.DAO.DBFormationDAO;
import com.enseirb.pfa.bastats.data.DAO.DBMatchDAO;
import com.enseirb.pfa.bastats.data.DAO.DBTableauEquipeDAO;
import com.enseirb.pfa.bastats.data.DAO.DBTournoiEquipeDAO;
import com.enseirb.pfa.bastats.data.model.Equipe;
import com.enseirb.pfa.bastats.data.model.Match;

import java.util.ArrayList;
import java.util.List;

public class SelectTeamTableau extends ActionBarActivity {
    private int matchId;
    private int tournoiId;
    private List<Integer> idsA;
    private List<Integer> idsB;
    private Context mCtx;
    private TextView mLibelle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_team_tableau);
        mCtx = this;
        idsA = new ArrayList<>(getIntent().getIntegerArrayListExtra(KeyTable.LIST_EQUIPE_ID));
        idsB = new ArrayList<>(getIntent().getIntegerArrayListExtra(KeyTable.LIST_EQUIPE_ID));
        matchId = getIntent().getIntExtra(KeyTable.MATCH_ID, -1);
        tournoiId = getIntent().getIntExtra(KeyTable.ARG_ID_TOURNOI,-1);
        final Spinner spinner_equipe_A = (Spinner) findViewById(R.id.spinner_equipe_A);
        setSpinnerEquipes(spinner_equipe_A, idsA);

        final Spinner spinner_equipe_B = (Spinner) findViewById(R.id.spinner_equipe_B);
        setSpinnerEquipes(spinner_equipe_B, idsB);

        Button valider = (Button) findViewById(R.id.valider);
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idsA.get(spinner_equipe_A.getSelectedItemPosition()) ==
                                idsB.get(spinner_equipe_B.getSelectedItemPosition())){
                    createAlert("Erreur de sélection", "Veuillez sélectionner deux équipes différentes");
                } else {
                    DBMatchDAO tableMatch = new DBMatchDAO(mCtx);
                    DBFormationDAO tableFormation = new DBFormationDAO(mCtx);
                    Match match = tableMatch.getWithId(matchId);
                    match.setFormationEquipeA(
                            tableFormation.getDefaultFormation(idsA.get(spinner_equipe_A
                                    .getSelectedItemPosition()))
                                    .getId());
                    Log.d("SELECT", "id eq: " + idsA.get(spinner_equipe_A.getSelectedItemPosition()));
                    match.setFormationEquipeB(
                            tableFormation.getDefaultFormation(idsB.get(spinner_equipe_B
                                    .getSelectedItemPosition()))
                                    .getId());
                    Log.d("SELECT", "id eq: " + idsB.get(spinner_equipe_B.getSelectedItemPosition()));
                    tableMatch.update(matchId, match);
                /*Intent i = new Intent(mCtx, NavigationTableau.class);
                startActivity(i);*/
                    Intent i = new Intent();
                    setResult(RESULT_OK, i);
                    finish();
                }
            }
        });

        mLibelle = (TextView) findViewById(R.id.match_libelle);
        mLibelle.setText(getIntent().getStringExtra(KeyTable.LIBELLE));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_team_tableau, menu);
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

    private void setSpinnerEquipes(Spinner sp, List<Integer> equipeIds) {
        List<String> liste_equipes = new ArrayList<String>();
        DBEquipeDAO tableEquipe = new DBEquipeDAO(mCtx);
        for (int id : equipeIds) {
            Equipe element = tableEquipe.getWithId(id);
            liste_equipes.add(element.getNom());
        }
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, liste_equipes);
        spinner_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sp.setAdapter(spinner_adapter);
        Log.d("SELECT", liste_equipes.toString());
        Log.d("SELECT", equipeIds.toString());
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
}
