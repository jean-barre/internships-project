package com.enseirb.pfa.bastats.lancement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.enseirb.pfa.bastats.R;
import com.enseirb.pfa.bastats.data.DAO.DBEquipeDAO;
import com.enseirb.pfa.bastats.data.DAO.DBFormationDAO;
import com.enseirb.pfa.bastats.data.DAO.DBFormationJoueurDAO;
import com.enseirb.pfa.bastats.data.DAO.DBJoueurDAO;
import com.enseirb.pfa.bastats.data.model.Formation;
import com.enseirb.pfa.bastats.data.model.Joueur;
import com.enseirb.pfa.bastats.tournoi.KeyTable;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rsabir on 12/02/15.
 */
public class ActivityNouveauJoueur extends ActionBarActivity {
    private static final int MATCH_REQUEST = 1;

    private Context mCtx;

    private CharSequence[] charSequenceItems;
    private List<HashMap<String,String>> mListeJoueursDb;


    private List<Joueur> mJoueursEquipeADb;
    private List<Joueur> mJoueursEquipeBDb;
    private int neutreAId;
    private int neutreBId;

    private List<HashMap<String,String>> mViewEquipeA;
    private List<HashMap<String,String>> mViewEquipeB;

    private Button ajouterJoueurA;
    private Button ajouterJoueurB;
    //private Button findJoueurA;
    //private Button findJoueurB;
    private Button next;

    private SimpleAdapter adapterA;
    private SimpleAdapter adapterB;

    private TextView nomEquipeA;
    private TextView nomEquipeB;
    private int equipeAId;
    private int equipeBId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ajouter_joueur_equipe);

        mCtx = this;
        nomEquipeA = (TextView) findViewById(R.id.nom_equipeA);
        nomEquipeB = (TextView) findViewById(R.id.nom_equipeB);

        equipeAId = getIntent().getIntExtra(KeyTable.ID_EQUIPE_A,-1);
        equipeBId = getIntent().getIntExtra(KeyTable.ID_EQUIPE_B,-1);
        DBEquipeDAO tableEquipe = new DBEquipeDAO(mCtx);
        nomEquipeA.setText(tableEquipe.getWithId(equipeAId).getNom());
        nomEquipeB.setText(tableEquipe.getWithId(equipeBId).getNom());

        getTeamPlayersFromDb();

        listviewInit();

        ajouterJoueurA = (Button) findViewById(R.id.button_equipeA);
        ajouterJoueurB = (Button) findViewById(R.id.button_equipeB);
        next = (Button) findViewById(R.id.button_next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, ActivitySelectionJoueur.class);
                Log.d("COULEUR","COULEUR" +getIntent().getStringExtra("couleur_equipe_A")+getIntent().getStringExtra("couleur_equipe_B"));
                ArrayList<Integer> playersAIds = new ArrayList<Integer>();
                ArrayList<Integer> playersBIds = new ArrayList<Integer>();
                for (Joueur joueur : mJoueursEquipeADb)
                    playersAIds.add(joueur.getId());
                for (Joueur joueur : mJoueursEquipeBDb)
                    playersBIds.add(joueur.getId());
                intent.putIntegerArrayListExtra(KeyTable.JOUEURS_EQ_A, playersAIds);
                intent.putIntegerArrayListExtra(KeyTable.JOUEURS_EQ_B, playersBIds);
                intent.putExtra(KeyTable.ID_EQUIPE_A, equipeAId);
                intent.putExtra(KeyTable.ID_EQUIPE_B, equipeBId);
                intent.putExtra(KeyTable.NOM_EQUIPE_A, nomEquipeA.getText().toString());
                intent.putExtra(KeyTable.NOM_EQUIPE_B, nomEquipeB.getText().toString());
                intent.putExtra(KeyTable.ID_NEUTRE_A, neutreAId);
                intent.putExtra(KeyTable.ID_NEUTRE_B, neutreBId);
                intent.putExtra(KeyTable.INTENT_RECEIVE, getIntent().getStringExtra(KeyTable.INTENT_RECEIVE));
                intent.putExtra(KeyTable.ARG_NB_PERIODES, getIntent().getIntExtra(KeyTable.ARG_NB_PERIODES, -1));
                intent.putExtra(KeyTable.DUREE_PERIODE, getIntent().getStringExtra(KeyTable.DUREE_PERIODE));
                intent.putExtra(KeyTable.MATCH_ID, getIntent().getIntExtra(KeyTable.MATCH_ID, -1));
                intent.putExtra(KeyTable.COULEUR_A, getIntent().getStringExtra(KeyTable.COULEUR_A));
                intent.putExtra(KeyTable.COULEUR_B, getIntent().getStringExtra(KeyTable.COULEUR_B));
                startActivityForResult(intent, MATCH_REQUEST);
            }
        });
        initialiseDialog();
    }

    private void addToListView(Joueur joueur, List<HashMap<String,String>> listView, SimpleAdapter mAdapter){
        HashMap<String, String > map = new HashMap<String, String>();
        map.put("nom", "Nom: "+tronquer(joueur.getNom(), 17)+ " \tPrénom: " + tronquer(joueur.getPrenom(), 17));
        map.put("numero", tronquer(joueur.getNumero(),4));
        map.put("pseudo", "Pseudo: "+tronquer(joueur.getPseudo(),30));
        listView.add(map);
        mAdapter.notifyDataSetChanged();
    }

    private void listviewInit(){
        mViewEquipeA = new ArrayList<>();
        mViewEquipeB = new ArrayList<>();

        String[] from = {"nom","pseudo","numero"};
        int[] to = { R.id.nomJoueur,R.id.pseudoJoueur, R.id.numero_joueur };
        adapterA = new SimpleAdapter(ActivityNouveauJoueur.this, mViewEquipeA, R.layout.element_ajouter_joueur, from, to);
        adapterB = new SimpleAdapter(ActivityNouveauJoueur.this, mViewEquipeB, R.layout.element_ajouter_joueur, from, to);

        ListView eqA = (ListView) findViewById(R.id.listview_selection_joueur_A);
        ListView eqB = (ListView) findViewById(R.id.listview_selection_joueur_B);
        eqA.setAdapter(adapterA);
        eqB.setAdapter(adapterB);

        for(Joueur joueur : mJoueursEquipeADb)
            addToListView(joueur, mViewEquipeA, adapterA);

        for(Joueur joueur : mJoueursEquipeBDb)
            addToListView(joueur, mViewEquipeB, adapterB);
     }

    private void getTeamPlayersFromDb(){
        DBJoueurDAO tableJoueur = new DBJoueurDAO(mCtx);
        if (tableJoueur.getJoueursEquipe(equipeAId) != null) {
            mJoueursEquipeADb = new ArrayList<>(tableJoueur.getJoueursEquipe(equipeAId));
        }else {
            // Exception
        }
        if (tableJoueur.getJoueursEquipe(equipeBId) != null) {
            mJoueursEquipeBDb = new ArrayList<>(tableJoueur.getJoueursEquipe(equipeBId));
        } else {
            // Exception
        }
        neutreAId = getNeutralPlayer(mJoueursEquipeADb);
        neutreBId = getNeutralPlayer(mJoueursEquipeBDb);
        /*neutreAId = mJoueursEquipeADb.get(mJoueursEquipeADb.size()-1).getId();
        mJoueursEquipeADb.remove(mJoueursEquipeADb.size()-1);
        neutreBId = mJoueursEquipeBDb.get(mJoueursEquipeBDb.size()-1).getId();
        mJoueursEquipeBDb.remove(mJoueursEquipeBDb.size()-1);*/
        Log.d("TAG", "neutre A "+neutreAId+ " neutreB "+neutreBId);
    }


    private void initialiseDialog(){

        ajouterJoueurA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlert("Nouveau joueur","Choisissez le mode d'ajout",
                        mViewEquipeA, adapterA, mJoueursEquipeADb, equipeAId);
            }
        });

        ajouterJoueurB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlert("Nouveau joueur","Choisissez le mode d'ajout",
                        mViewEquipeB, adapterB, mJoueursEquipeBDb, equipeBId);
            }
        });

    }

    private void createAlert(String title, String message,final List<HashMap<String,String>> mListView, final SimpleAdapter mAdapter,
                             final List<Joueur> mListDb, final int equipeId){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mCtx);
        alertDialogBuilder.setTitle(title);

        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Trouver joueur", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        findPlayerDialog(mListView, mAdapter, mListDb, equipeId);
                    }
                })
                .setNegativeButton("Ajouter joueur", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        createPlayerDialog(mListView, mAdapter, mListDb, equipeId);
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void createPlayerDialog(final List<HashMap<String,String>> mListView, final SimpleAdapter mAdapter,
                                    final List<Joueur> mListDb, final int equipeId){
        final Dialog dialog = new Dialog(mCtx);
        dialog.setContentView(R.layout.dialogue_ajouter_joueur);

        final EditText nom = (EditText) dialog.findViewById(R.id.edit_text_nom);
        final EditText prenom = (EditText) dialog.findViewById(R.id.edit_text_prenom);
        final EditText pseudo = (EditText) dialog.findViewById(R.id.edit_text_pseudo);

        dialog.setTitle("Nouveau joueur");
        dialog.setCancelable(true);

        Button button_positive = (Button) dialog.findViewById(R.id.ajouter);
        Button button_negative = (Button) dialog.findViewById(R.id.cancel);

        button_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        button_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nom.getText().toString().equals("") || prenom.getText().toString().equals("")
                        || pseudo.getText().toString().equals("")) {
                    Toast.makeText(v.getContext(), "Veuillez saisir tout les champs", Toast.LENGTH_SHORT).show();
                    return;
                }
                Joueur joueur = new Joueur(nom.getText().toString().trim(),
                        prenom.getText().toString().trim(),
                        pseudo.getText().toString().trim(),
                        Joueur.NO_NUM);
                DBJoueurDAO tableJoueur = new DBJoueurDAO(mCtx);
                tableJoueur.insert(joueur);
                Log.d("TAG", " insertion joueur " + joueur.toString());
                joueur.setId(tableJoueur.getLast().getId());
                DBFormationDAO tableFormation = new DBFormationDAO(mCtx);
                int formationId = tableFormation.getDefaultFormation(equipeId).getId();
                DBFormationJoueurDAO tableFormationJoueur = new DBFormationJoueurDAO(mCtx);
                tableFormationJoueur.insert(formationId, joueur.getId());
                Log.d("TAG", "insertion joueur " + joueur.getId() + "à la formation par défaut " + formationId
                        + " de l'équipe " + equipeId);
                addToListView(joueur, mListView, mAdapter);
                mListDb.add(joueur);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void findPlayerDialog(final List<HashMap<String,String>> mListView, final SimpleAdapter mAdapter,
                                  final List<Joueur> mListDb, final int equipeId){

        mListeJoueursDb = new ArrayList<>();
        getPlayersFromDb();
        AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
        builder.setTitle("Trouver un joueur")
                .setSingleChoiceItems(charSequenceItems, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //showToast("Some actions maybe? Selected index: " + arg1);
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        int selectedPosition =
                                ((AlertDialog) dialog).getListView().getCheckedItemPosition();

                        DBFormationDAO tableFormation = new DBFormationDAO(mCtx);
                        int formationId = tableFormation.getDefaultFormation(equipeId).getId();
                        DBFormationJoueurDAO tableFormationJoueur = new DBFormationJoueurDAO(mCtx);
                        DBJoueurDAO tableJoueur = new DBJoueurDAO(mCtx);
                        Joueur joueur = tableJoueur.getWithId(Integer.valueOf(mListeJoueursDb.get(selectedPosition).get("id")));
                        tableFormationJoueur.insert(formationId, joueur.getId() );
                        Log.d("TAG", "insertion joueur " + joueur.getId()
                                + "à la formation par défaut " + formationId
                                + " de l'équipe " + equipeId);
                        addToListView(joueur, mListView, mAdapter);
                        mListDb.add(joueur);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.dialog_retour),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // removes the dialog from the screen
                            }
                        })

                .show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == MATCH_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
                Log.d("Nouveau", "setResult()");
                finish();
            }
        }
    }

    public String tronquer(String str, int max){
        int len = str.length();
        if (len > max)
            return str.substring(0,max);
        else return str;
    }

    private int getNeutralPlayer(List<Joueur> mListeJoueur){
        for (Joueur cmp : mListeJoueur){
            if (cmp.getNom().equals("JoueurNeutre") && cmp.getPrenom().equals("NeutralPlayer")){
                mListeJoueur.remove(cmp);
                Log.d("NEUTRE", "Joueur neutre trouvé (id:"+cmp.getId()+"), suppression ");
                return cmp.getId();
            }
        }
        return -1;
    }

    public void getPlayersFromDb(){
        DBJoueurDAO tableTournoi = new DBJoueurDAO(mCtx);
        List<Joueur> tmp = new ArrayList<>();
        if (tableTournoi.getAll() != null)
            tmp.addAll(tableTournoi.getAll());
        Log.d("NOUVEAU", "Copie des joueurs de la bdd");
        for (Joueur joueur : tmp){
            if (!joueur.getNom().equals("JoueurNeutre") && !joueur.getPrenom().equals("NeutralPlayer")
                    && (!appartientSelection(joueur,mJoueursEquipeADb) && !appartientSelection(joueur,mJoueursEquipeBDb))) {
                HashMap<String, String> map = new HashMap<>();
                map.put("pseudo", joueur.getPseudo());
                map.put("nom", joueur.getNom());
                map.put("prenom", joueur.getPrenom());
                map.put("id", String.valueOf(joueur.getId()));
                mListeJoueursDb.add(map);
            }
        }

        setUpSelection();
    }

    public void setUpSelection(){
        int size = mListeJoueursDb.size();
        Log.d("NOUVEAU","Nombre de joueurs: "+size);
        charSequenceItems = new CharSequence[size];
        for (int i=0; i < size; i++){
            charSequenceItems[i] = mListeJoueursDb.get(i).get("pseudo");
        }
    }

    private boolean appartientSelection(Joueur joueur, List<Joueur> mListJoueur){
        for (Joueur joueur1 : mListJoueur){
            if (joueur1.getId() == joueur.getId())
                return true;
        }
        return false;
    }


}
