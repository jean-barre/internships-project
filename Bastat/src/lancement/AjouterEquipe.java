package com.enseirb.pfa.bastats.lancement;

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
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.enseirb.pfa.bastats.R;
import com.enseirb.pfa.bastats.data.DAO.DBEquipeDAO;
import com.enseirb.pfa.bastats.data.DAO.DBFormationDAO;
import com.enseirb.pfa.bastats.data.DAO.DBFormationJoueurDAO;
import com.enseirb.pfa.bastats.data.DAO.DBJoueurDAO;
import com.enseirb.pfa.bastats.data.model.Equipe;
import com.enseirb.pfa.bastats.data.model.Formation;
import com.enseirb.pfa.bastats.data.model.Joueur;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class AjouterEquipe extends ActionBarActivity {
    private static final String TAG = "Ajout Equipe";
    private List<Joueur> listeJoueur;
    //private ArrayAdapter<Joueur> mAdapter;
    private SimpleAdapter mAdapter;
    private ListView mListeView;

    private ArrayList<HashMap<String, String>> mListeJoueur;
    private ArrayList<HashMap<String,String>> mListeJoueursDb;

    private boolean[] selectVal;
    private CharSequence[] charSequenceItems;
    private List<Integer> toRemove;

    private Context mCtx;
    private Button buttonNewPlayer;
    private Button buttonValider;
    private Button buttonFindPlayer;
    private Spinner colorChoice;
    private EditText nomEquipe;
    private TextView couleurEquipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_equipe);

        mCtx = this;

        buttonNewPlayer = (Button) findViewById(R.id.button_new_player);
        buttonValider = (Button) findViewById(R.id.button_valider);
        nomEquipe = (EditText) findViewById(R.id.editText_nom_equipe);
        colorChoice = (Spinner) findViewById(R.id.spinner_couleur);

        setSpinnerMailots(colorChoice);

        buttonNewPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPlayerDialog();
            }
        });

        mListeView = (ListView) findViewById(R.id.liste_joueurs);
        mListeJoueur = new ArrayList<HashMap<String,String>>();
        mAdapter=new SimpleAdapter(this,mListeJoueur,R.layout.player_list_row,
                new String[]{"nom","pseudo","bouton"},new int[]{R.id.nomJoueur,R.id.pseudoJoueur,R.id.button}) {

            public View getView(int position, View convertView, ViewGroup parent) {
                final int index = position;
                View v = super.getView(position, convertView, parent);
                ImageButton b=(ImageButton)v.findViewById(R.id.supprimerJoueur);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String,String> map = new HashMap<String,String>();
                        map.put("nom", mListeJoueur.get(index).get("nom"));
                        map.put("id", mListeJoueur.get(index).get("id"));
                        map.put("pseudo", mListeJoueur.get(index).get("pseudo"));
                        mListeJoueursDb.add(map);
                        mListeJoueur.remove(index);
                        setUpSelection();
                        mAdapter.notifyDataSetChanged();
                    }
                });
                return v;
            }
        };
        //mAdapter = new ArrayAdapter<Joueur>(this, android.R.layout.simple_list_item_1, mListeJoueur);
        mListeView.setAdapter(mAdapter);

        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nomEquipe.getText().toString().trim().isEmpty()){
                    createAlert(getResources().getString(R.string.missing_information),
                            "Veuillez entrer un nom d'équipe");
                } else {
                    DBEquipeDAO tableEquipe = new DBEquipeDAO(mCtx);
                    DBJoueurDAO tableJoueur = new DBJoueurDAO(mCtx);
                    DBFormationDAO tableFormation = new DBFormationDAO(mCtx);
                    DBFormationJoueurDAO tableFormationJoueur = new DBFormationJoueurDAO(mCtx);

                    List<Joueur> listeJoueur = new ArrayList<Joueur>();
                    for (HashMap<String, String> map : mListeJoueur) {
                        listeJoueur.add(new Joueur(tableJoueur.getWithId(Integer.parseInt(map.get("id")))));
                    }

                    tableEquipe.insert(new Equipe(nomEquipe.getText().toString().trim(),
                            colorChoice.getSelectedItem().toString()));
                    Equipe equipe = new Equipe(tableEquipe.getLast());
                    tableFormation.insert(equipe.getId(), Formation.FORMATION_PAR_DEFAUT);

                    int formationId = tableFormation.getLast().getId();
                    Joueur joueurNeutre = new Joueur("JoueurNeutre", "NeutralPlayer", "Joueur "+equipe.getNom(), "Eq");
                    tableJoueur.insert(joueurNeutre);
                    tableFormationJoueur.insert(formationId, tableJoueur.getLast().getId());
                    Log.d(TAG, "insertion du joueur nutre " + tableJoueur.getLast().getId()
                            + " à la formation " + formationId);
                    for (Joueur joueur : listeJoueur) {
                        tableFormationJoueur.insert(formationId, joueur.getId());
                        Log.d(TAG, "insertion du joueur prééxistant " + joueur.getId()
                                + " à la formation " + formationId);
                    }
                    Intent returnIntent = new Intent();
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            }
        });

        getPlayersFromDb();
        buttonFindPlayer = (Button) findViewById(R.id.button_find_player);

        buttonFindPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPlayerDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.global, menu);
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

    public String tronquer(String str, int max){
        int len = str.length();
        if (len > max)
            return str.substring(0,max);
        else return str;
    }

    private void findPlayerDialog() {
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
                for (int i = 0; i < mListeJoueursDb.size(); i++) {
                    if (selectVal[i]) {
                        Log.d(TAG,"valeur de i:"+i);
                        HashMap<String,String> map = new HashMap<String,String>();
                        map.put("nom", mListeJoueursDb.get(i).get("nom"));
                        map.put("id", mListeJoueursDb.get(i).get("id"));
                        map.put("pseudo", mListeJoueursDb.get(i).get("pseudo"));
                        Log.d(TAG, "Selection joueur "+map.get("nom")+" avec l'id "+map.get("id"));
                        mListeJoueur.add(map);
                        toRemove.add(i);
                    }
                }
                Collections.sort(toRemove);
                Collections.reverse(toRemove);
                // On les ranges dans l'ordre décroissant pour supprimer de droite à gauche
                Log.d(TAG,toRemove.toString());
                for(int i : toRemove){
                    mListeJoueursDb.remove(i);
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

    private void createPlayerDialog(){
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
                Log.d(TAG, " insertion joueur " + joueur.toString());
                joueur.setId(tableJoueur.getLast().getId());
                addToListView(joueur);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void addToListView(Joueur joueur){
        HashMap<String, String > map = new HashMap<String, String>();
        map.put("nom", "Nom: "+tronquer(joueur.getNom(), 17)+ " \tPrénom: " + tronquer(joueur.getPrenom(), 17));
        map.put("id", String.valueOf(joueur.getId()));
        map.put("pseudo", "Pseudo: "+tronquer(joueur.getPseudo(), 30));
        mListeJoueur.add(map);
        mAdapter.notifyDataSetChanged();
    }

    public void getPlayersFromDb(){
        DBJoueurDAO tableJoueur = new DBJoueurDAO(mCtx);
        List<Joueur> tmp = new ArrayList<>();
        if (tableJoueur.getLast() != null)
            tmp.addAll(tableJoueur.getAll());
        Log.d(TAG, "Copie des joueurs de la bdd");
        mListeJoueursDb = new ArrayList<>();
        for (Joueur joueur : tmp){
            if (!joueur.getNom().equals("JoueurNeutre") && !joueur.getPrenom().equals("NeutralPlayer")) {
                HashMap<String, String> map = new HashMap<>();
                map.put("nom", "Nom: " + tronquer(joueur.getNom(), 17) + " \tPrénom: " + tronquer(joueur.getPrenom(), 17));
                map.put("id", String.valueOf(joueur.getId()));
                map.put("pseudo", "Pseudo: " + tronquer(joueur.getPseudo(), 30));
                Log.d(TAG, "Copie joueur " + joueur.getNom() + " avec l'id " + joueur.getId());
                mListeJoueursDb.add(map);
            }
        }

        setUpSelection();
    }

    public void setUpSelection(){
        int size = mListeJoueursDb.size();
        Log.d(TAG,"Nombre de joueurs: "+size);
        //Log.d(TAG,mListeEquipesDb.toString());
        selectVal = new boolean[size];
        charSequenceItems = new CharSequence[size];
        toRemove = new ArrayList<>();
        for (int i=0; i < size; i++){
            selectVal[i] = false;
            charSequenceItems[i] = mListeJoueursDb.get(i).get("nom");
            //Log.d(TAG, "Ajout nom equipe"+mListeEquipesDb.get(i).get("nom"));
        }
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

    private void setSpinnerMailots(Spinner sp) {
        ArrayAdapter<CharSequence> spinner_adapter = ArrayAdapter.createFromResource(this, R.array.couleurs_maillots_array, android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sp.setAdapter(spinner_adapter);
    }


}
