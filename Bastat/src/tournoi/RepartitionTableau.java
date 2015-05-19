package com.enseirb.pfa.bastats.tournoi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.enseirb.pfa.bastats.R;
import com.enseirb.pfa.bastats.data.DAO.DBEquipeDAO;
import com.enseirb.pfa.bastats.data.DAO.DBMatchDAO;
import com.enseirb.pfa.bastats.data.DAO.DBPhaseTableauDAO;
import com.enseirb.pfa.bastats.data.DAO.DBTableauEquipeDAO;
import com.enseirb.pfa.bastats.data.model.Match;
import com.enseirb.pfa.bastats.tournoi.algorithme.Calendrier;
import com.enseirb.pfa.bastats.tournoi.fragment.EquipePouleFragment;
import com.enseirb.pfa.bastats.tournoi.fragment.SelectionEquipePouleFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RepartitionTableau extends ActionBarActivity implements
        EquipePouleFragment.OnFragmentInteractionListener,
        SelectionEquipePouleFragment.OnFragmentInteractionListener {

    private Button buttonValider;
    private Context mCtx;
    private int tournoiId;
    private int phaseId;
    private int phaseTableauId;
    private int nbEquipes;
    private boolean withPetiteFinale;

    private static final String TAG = "RepartitionTableau";

    private SelectionEquipePouleFragment fragmentSelectionEquipe;
    private EquipePouleFragment fragmentTableau;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repartition_tableau);

        mCtx = this;
        getDataFromIntent();

        buttonValider = (Button) findViewById(R.id.button_valider);

        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentTableau.getSize() == nbEquipes){
                    putInDatabase();
                    Intent result = new Intent();
                    setResult(RESULT_OK, result);
                    finish();
                }
                else
                    createAlert("Nombre d'équipes invalide","Vous n'avez pas sélectionné "+nbEquipes+" équipes");
            }
        });

        fragmentTableau = EquipePouleFragment.newInstance(nbEquipes);
        fragmentSelectionEquipe = SelectionEquipePouleFragment.newInstance(tournoiId);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.poule_fragment_container, fragmentTableau);
        ft.add(R.id.liste_equipe_fragment_container,fragmentSelectionEquipe);
        ft.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_repartition_tableau, menu);
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

    @Override
    public void onNextPoule(){
        // Do nothing
    }

    @Override
    public void onPreviousPoule(){
       // Do nothing
    }

    @Override
    public void onRemoveTeam(HashMap<String,String> team){
        fragmentSelectionEquipe.add(team);
    }

    @Override
    public void onAddTeam(HashMap<String,String> team){
        fragmentTableau.add(team);
    }

    private void putInDatabase(){
        DBTableauEquipeDAO tableauEquipeDAO = new DBTableauEquipeDAO(mCtx);

        List<HashMap<String,String>> listeEquipes = new ArrayList<>();
        listeEquipes.addAll(fragmentTableau.getEquipesPoule());

        for (HashMap<String,String> equipe : listeEquipes){
            int equipeId = Integer.parseInt(equipe.get("id"));
            tableauEquipeDAO.insert(phaseTableauId,equipeId);
            Log.d(TAG, "insertion de l'équipe "+equipeId+" à la phase tableau " + phaseTableauId);
        }

        genererCalendrierMatch();
    }

    private void genererCalendrierMatch(){
        DBMatchDAO tableMatch = new DBMatchDAO(mCtx);
        Calendrier calendrier =
                new Calendrier(nbEquipes, withPetiteFinale);

        List<Match> gameList = new ArrayList<>();
        gameList.clear();
        //gameList.addAll(calendrier.simpleGeneration());
        gameList.addAll(calendrier.fullGeneration());

        for(Match match : gameList){
            match.setResultat(Match.MATCH_NON_JOUE);
            match.setPhaseId(phaseId);
            tableMatch.insert(match);
            int matchId = tableMatch.getLast().getId();
            Log.d(TAG, "Match id "+matchId+" dans le tableau lié à la phase:"+match.getPhaseId());
        }
    }

    private void getDataFromIntent(){
        Intent intent = getIntent();
        phaseTableauId = intent.getIntExtra(KeyTable.ARG_PHASE_POULE_ID, -1);
        tournoiId = intent.getIntExtra(KeyTable.ARG_ID_TOURNOI, -1);
        nbEquipes = intent.getIntExtra(KeyTable.NB_EQUIPES, -1);
        Log.d(TAG, "Init from intent: id!Tournoi=" + tournoiId+" and idTableau="+phaseTableauId);
        DBPhaseTableauDAO tTableau = new DBPhaseTableauDAO(mCtx);
        phaseId = tTableau.getWithId(phaseTableauId).getPhaseId();
        withPetiteFinale = intent.getBooleanExtra("PetiteFinale",false);
    }

    private void createAlert(String title, String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mCtx);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.dialog_retour), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
