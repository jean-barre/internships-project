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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import com.enseirb.pfa.bastats.R;
import com.enseirb.pfa.bastats.data.DAO.DBEquipeDAO;
import com.enseirb.pfa.bastats.data.DAO.DBTournoiDAO;
import com.enseirb.pfa.bastats.data.DAO.DBTournoiEquipeDAO;
import com.enseirb.pfa.bastats.data.model.Equipe;
import com.enseirb.pfa.bastats.data.model.Tournoi;
import com.enseirb.pfa.bastats.lancement.AjouterEquipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SelectionEquipeTournoiActivity extends ActionBarActivity {

    private static final String TAG = "SelectionEquipeTournoi";

    private static final int CODE_AJOUT_EQUIPE = 1;
    private static final int REQUEST_IMPORT = 22;

    private Context mCtx;
    private int tournoiId;

    private Button buttonSelectionEquipe;
    private Button buttonValider;
    private Button buttonAddTeam;

    private ArrayList<HashMap<String,String>> mListeEquipesSelection;
    private ArrayList<HashMap<String,String>> mListeEquipesDb;
    private ListView mListeView;
    private SimpleAdapter mAdapter;

    private boolean[] selectVal;
    private CharSequence[] charSequenceItems;
    private List<Integer> toRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_equipe_tournoi);

        mCtx = this;
        tournoiId = getIntent().getIntExtra(KeyTable.ARG_ID_TOURNOI, -1);
        Log.d(TAG, "Récupération de l'idTournoi: " + tournoiId);

        buttonValider = (Button) findViewById(R.id.button_valider);
        buttonAddTeam = (Button) findViewById(R.id.button_add_team);

        buttonAddTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, AjouterEquipe.class);
                startActivityForResult(intent, CODE_AJOUT_EQUIPE);
            }
        });

        mListeView = (ListView) findViewById(R.id.liste_equipes_tournoi);
        mListeEquipesSelection = new ArrayList<>();
        mListeEquipesDb = new ArrayList<>();

        getTeamsFromDb();

        mAdapter = new SimpleAdapter(this, mListeEquipesSelection,R.layout.row_equipe_poule,
                new String[]{"nom","bouton"},
                new int[]{R.id.nom_equipe, R.id.supprimer_equipe}) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                final int index = position;
                Button buttonDelete = (Button) view.findViewById(R.id.supprimer_equipe);
                buttonDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String,String> map = new HashMap<String,String>();
                        map.put("nom", mListeEquipesSelection.get(index).get("nom"));
                        map.put("id", mListeEquipesSelection.get(index).get("id"));
                        mListeEquipesDb.add(map);
                        mListeEquipesSelection.remove(index);
                        setUpSelection();
                        mAdapter.notifyDataSetChanged();
                    }
                });
                return view;
            }
        };

        buttonSelectionEquipe = (Button) findViewById(R.id.selection_equipes);

        buttonSelectionEquipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setTitle("Multiple Choice");
                builder.setMultiChoiceItems(charSequenceItems, selectVal,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                selectVal[which] = isChecked;
                            }
                        });
                builder.setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG,selectVal.toString());
                        for (int i = 0; i < mListeEquipesDb.size(); i++) {
                            if (selectVal[i]) {
                                Log.d(TAG,"valeur de i:"+i);
                                HashMap<String,String> map = new HashMap<String,String>();
                                map.put("nom", mListeEquipesDb.get(i).get("nom"));
                                map.put("id", mListeEquipesDb.get(i).get("id"));
                                Log.d(TAG, "Selection équipe "+map.get("nom")+" avec l'id "+map.get("id"));
                                mListeEquipesSelection.add(map);
                                toRemove.add(i);
                            }
                        }
                        Collections.sort(toRemove);
                        Collections.reverse(toRemove);
                        // On les ranges dans l'ordre décroissant pour supprimer de droite à gauche
                        Log.d(TAG,toRemove.toString());
                        for(int i : toRemove){
                            mListeEquipesDb.remove(i);
                            Log.d(TAG,"remove élément "+i);
                        }
                        setUpSelection();
                        mAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.dialog_cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBTournoiDAO tableTournoi = new DBTournoiDAO(mCtx);
                Tournoi tournoi = new Tournoi(tableTournoi.getWithId(tournoiId));
                tournoi.setNbEquipeMax(mListeEquipesSelection.size());
                tableTournoi.update(tournoiId, tournoi);
                Log.d(TAG," Nombre d'équipes du tournoi:"+mListeEquipesSelection.size());
                DBTournoiEquipeDAO tableTournoiEquipe = new DBTournoiEquipeDAO(mCtx);
                for (HashMap<String,String> team : mListeEquipesSelection){
                    int teamId = Integer.parseInt(team.get("id"));
                    tableTournoiEquipe.insert(tournoiId, teamId);
                    Log.d(TAG, "L'équipe " + teamId + " participe au tournoi "+tournoiId);
                }
                Intent intent = new Intent(mCtx, NavigationDrawerTournoi.class);
                intent.putExtra(KeyTable.ARG_ID_TOURNOI, tournoiId);
                startActivity(intent);
                finish();
            }
        });

        mListeView.setAdapter(mAdapter);

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

    public void getTeamsFromDb(){
        DBEquipeDAO tableEquipe = new DBEquipeDAO(mCtx);
        List<Equipe> tmp = new ArrayList<>();
        if (tableEquipe.getAll() != null)
            tmp.addAll(tableEquipe.getAll());
        Log.d(TAG,"Copie des équipes de la bdd");

        for (Equipe team : tmp){
            HashMap<String,String> map = new HashMap<>();
            map.put("nom",team.getNom());
            map.put("id",String.valueOf(team.getId()));
            Log.d(TAG, "Copie équipe "+ team.getNom()+ "avec l'id "+team.getId());
            mListeEquipesDb.add(map);
        }

        setUpSelection();
    }

    public void setUpSelection(){
        int size = mListeEquipesDb.size();
        Log.d(TAG,"Nombre d'équipes: "+size);
        //Log.d(TAG,mListeEquipesDb.toString());
        selectVal = new boolean[size];
        charSequenceItems = new CharSequence[size];
        toRemove = new ArrayList<>();
        for (int i=0; i < size; i++){
            selectVal[i] = false;
            charSequenceItems[i] = mListeEquipesDb.get(i).get("nom");
            //Log.d(TAG, "Ajout nom equipe"+mListeEquipesDb.get(i).get("nom"));
        }
    }

    public void delete(int i){
        mListeEquipesSelection.remove(i);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        DBTournoiDAO tableTournoi = new DBTournoiDAO(mCtx);
        int id = tableTournoi.getLast().getId();
        tableTournoi.removeWithId(id);
        finish();
    }

    public void importEquipe(View v){
        // TODO Lancer activité d'import
        // startActivityForResult(intent, REQUEST_IMPORT);
    }

    private void updateSelection(){
        DBEquipeDAO tableEquipe = new DBEquipeDAO(mCtx);
        if (tableEquipe.getLast() != null) {
            Equipe team = new Equipe(tableEquipe.getLast());
            HashMap<String, String> map = new HashMap<>();
            map.put("nom", team.getNom());
            map.put("id", String.valueOf(team.getId()));
            Log.d(TAG, "Copie équipe " + team.getNom() + "avec l'id " + team.getId());
            mListeEquipesDb.add(0, map);
            setUpSelection();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == CODE_AJOUT_EQUIPE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                updateSelection();
            }
        }
        if (requestCode == REQUEST_IMPORT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                updateSelection();
            }
        }
    }
}
