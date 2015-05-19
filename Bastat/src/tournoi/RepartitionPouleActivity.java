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
import android.widget.Toast;

import com.enseirb.pfa.bastats.R;
import com.enseirb.pfa.bastats.data.DAO.DBFormationDAO;
import com.enseirb.pfa.bastats.data.DAO.DBMatchDAO;
import com.enseirb.pfa.bastats.data.DAO.DBPhasePouleDAO;
import com.enseirb.pfa.bastats.data.DAO.DBPouleDAO;
import com.enseirb.pfa.bastats.data.DAO.DBPouleEquipeDAO;
import com.enseirb.pfa.bastats.data.DAO.DBPouleMatchDAO;
import com.enseirb.pfa.bastats.data.model.Equipe;
import com.enseirb.pfa.bastats.data.model.Formation;
import com.enseirb.pfa.bastats.data.model.Match;
import com.enseirb.pfa.bastats.data.model.Poule;
import com.enseirb.pfa.bastats.tournoi.algorithme.GenerationCalendrierMatchPoule;
import com.enseirb.pfa.bastats.tournoi.fragment.EquipePouleFragment;
import com.enseirb.pfa.bastats.tournoi.fragment.SelectionEquipePouleFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class RepartitionPouleActivity extends ActionBarActivity implements
        EquipePouleFragment.OnFragmentInteractionListener,
        SelectionEquipePouleFragment.OnFragmentInteractionListener {

    private static final String TAG = "RepartitionPoule";

    private Context mCtx;

    private int nbPoule = 1;
    private int tournoiId;
    private int phasePouleId;
    private int phaseId;

    private int ptsVictoire;
    private int ptsNul;
    private int ptsDefaite;

    private int nbConfrontations;

    private int currentPoule = 0;
    private ArrayList<EquipePouleFragment> listePoules = new ArrayList<>();
    private SelectionEquipePouleFragment fragmentSelectionEquipe;

    private Button buttonValider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repartition_poule);

        mCtx = this;

        // On récupère les informations de l'intent
        getDataFromIntent();

        buttonValider = (Button) findViewById(R.id.button_valider);

        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int poulesRemplies = 0;
                for (EquipePouleFragment fragment : listePoules) {
                    if (!fragment.isEmpty() && !fragment.isOneElement()) {
                        poulesRemplies++;
                    }
                }
                if (poulesRemplies == listePoules.size()) {
                    putInDatabase();
                    Intent result = new Intent();
                    setResult(RESULT_OK, result);
                    finish();
                }else{
                    createAlert("Poule vide", "Attention il y a des poules vides"
                            + " ou n'ayant qu'une seule équipe, veuillez les remplir");
                }
            }
        });


        // On initialise toutes les poules
        for (int i = 0; i < nbPoule; i++){
            listePoules.add(EquipePouleFragment.newInstance((char) ('A' + i)));
        }

        // On les ajoute à l'activité
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < nbPoule; i++){
            ft.add(R.id.poule_fragment_container, listePoules.get(i));
            ft.hide(listePoules.get(i));
        }
        ft.show(listePoules.get(currentPoule));
        fragmentSelectionEquipe = SelectionEquipePouleFragment.newInstance(tournoiId);
        ft.add(R.id.liste_equipe_fragment_container,fragmentSelectionEquipe);
        ft.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_selection_equipe_tournoi, menu);
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
        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // Replace the container with the new fragment
        ft.hide(listePoules.get(currentPoule));

        if (currentPoule == nbPoule-1)
            currentPoule = 0;
        else
            currentPoule++;

        ft.show(listePoules.get(currentPoule));

        // Execute the changes specified
        ft.commit();
    }


    @Override
    public void onPreviousPoule(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(listePoules.get(currentPoule));

        if (currentPoule == 0)
            currentPoule = nbPoule-1;
        else
            currentPoule--;

        ft.show(listePoules.get(currentPoule));
        ft.commit();
    }

    @Override
    public void onRemoveTeam(HashMap<String,String> team){
        fragmentSelectionEquipe.add(team);
    }

    @Override
    public void onAddTeam(HashMap<String,String> team){
        listePoules.get(currentPoule).add(team);
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

    private void getDataFromIntent(){
        Intent intent = getIntent();
        ptsVictoire = intent.getIntExtra(KeyTable.ARG_PTS_V, 1);
        ptsNul = intent.getIntExtra(KeyTable.ARG_PTS_N, 0);
        ptsDefaite = intent.getIntExtra(KeyTable.ARG_PTS_D, 0);
        nbConfrontations = intent.getIntExtra(KeyTable.ARG_NB_CONFRONTATIONS, 1);
        intent.getBooleanExtra(KeyTable.ARG_GENERATION_AUTO, true);
        phasePouleId = intent.getIntExtra(KeyTable.ARG_PHASE_POULE_ID, -1);
        nbPoule = intent.getIntExtra(KeyTable.ARG_NB_POULES,1);
        tournoiId = intent.getIntExtra(KeyTable.ARG_ID_TOURNOI, -1);
        Log.d("TAG", "Init from intent: id!Tournoi=" + tournoiId);
        DBPhasePouleDAO tPoule = new DBPhasePouleDAO(mCtx);
        phaseId = tPoule.getWithId(phasePouleId).getPhaseId();
    }

    private void putInDatabase() {

        DBPouleEquipeDAO tablePouleEquipe = new DBPouleEquipeDAO(mCtx);
        DBPouleDAO tablePoule = new DBPouleDAO(mCtx);
        for (EquipePouleFragment fragment : listePoules) {
            String title = fragment.getName();
            Poule poule = new Poule(title, phasePouleId, ptsVictoire, ptsNul, ptsDefaite, Poule.ETAT_EN_COURS);
            tablePoule.insert(poule);
            Log.d(TAG, "Insertion " + poule.toString());
            int pouleId = -1;
            pouleId = tablePoule.getLast().getId();
            Log.d(TAG, "Identifiant de la poule: " + pouleId);

            List<HashMap<String, String>> listeEquipes = new ArrayList<>();
            listeEquipes.addAll(fragment.getEquipesPoule());

            for (HashMap<String, String> equipe : listeEquipes) {
                int equipeId = Integer.parseInt(equipe.get("id"));
                tablePouleEquipe.insert(pouleId, equipeId);
                Log.d(TAG, "insertion de l'équipe " + equipeId + " à la poule " + pouleId);
            }

            genererCalendrierMatch(pouleId);
        }
    }

    private void genererCalendrierMatch(int pouleId){
        DBPouleMatchDAO tablePouleMatch = new DBPouleMatchDAO(mCtx);
        DBPouleEquipeDAO tablePouleEquipe = new DBPouleEquipeDAO(mCtx);
        DBMatchDAO tableMatch = new DBMatchDAO(mCtx);
        DBFormationDAO tableFormation = new DBFormationDAO(mCtx);

        List<Integer> tmp = new ArrayList<>(tablePouleEquipe.getTeamsList(pouleId));
        Log.d(TAG,"Taille: "+tmp.size());
        Log.d(TAG, tmp.toString());
        GenerationCalendrierMatchPoule calendrier =
                new GenerationCalendrierMatchPoule(tmp,nbConfrontations);

        List<Match> gameList = new ArrayList<>();
        gameList.clear();
        gameList.addAll(calendrier.simpleGeneration());

        for(Match match : gameList){
            int equipeAid = match.getFormationEquipeA();
            int equipeBid = match.getFormationEquipeB();

            if (tableFormation.getDefaultFormation(equipeAid) == null){
                tableFormation.insert(equipeAid, Formation.FORMATION_PAR_DEFAUT);
                match.setFormationEquipeA(tableFormation.getLast().getId());
            } else {
                match.setFormationEquipeA(tableFormation.getDefaultFormation(equipeAid).getId());
            }

            if (tableFormation.getDefaultFormation(equipeBid) == null){
                tableFormation.insert(equipeBid, Formation.FORMATION_PAR_DEFAUT);
                match.setFormationEquipeB(tableFormation.getLast().getId());
            } else {
                match.setFormationEquipeB(tableFormation.getDefaultFormation(equipeBid).getId());
            }
            Log.d(TAG, "Formations "+match.getFormationEquipeA()+" vs "+match.getFormationEquipeB());
            //simulateGame(match);
            match.setResultat(Match.MATCH_NON_JOUE);
            match.setPhaseId(phaseId);
            tableMatch.insert(match);
            int matchId = tableMatch.getLast().getId();
            tablePouleMatch.insert(pouleId, matchId);
            Log.d(TAG, "Match id "+matchId+" dans la poule "+pouleId);
        }
    }

    private void simulateGame(Match match){
        Random rand = new Random();
        if (0 != rand.nextInt(3)) {
            int scoreA = rand.nextInt(150);
            int scoreB = rand.nextInt(150);
            match.setScoreEquipeA(scoreA);
            match.setScoreEquipeB(scoreB);
            if (scoreA > scoreB) {
                match.setResultat(match.getFormationEquipeA());
            } else if (scoreA < scoreB) {
                match.setResultat(match.getFormationEquipeB());
            } else {
                match.setResultat(Match.RESULTAT_NUL);
            }
        }
        else {
            match.setResultat(Match.MATCH_NON_JOUE);
        }
    }

    @Override
    public void onBackPressed() {
        Intent result = new Intent();
        setResult(RESULT_CANCELED, result);
        finish();
    }
}
