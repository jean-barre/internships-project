package com.enseirb.pfa.bastats.lancement;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.enseirb.pfa.bastats.R;
import com.enseirb.pfa.bastats.data.DAO.DBFormationDAO;
import com.enseirb.pfa.bastats.data.DAO.DBFormationJoueurDAO;
import com.enseirb.pfa.bastats.data.DAO.DBJoueurDAO;
import com.enseirb.pfa.bastats.data.DAO.DBMatchDAO;
import com.enseirb.pfa.bastats.data.model.Formation;
import com.enseirb.pfa.bastats.data.model.Joueur;
import com.enseirb.pfa.bastats.data.model.Match;
import com.enseirb.pfa.bastats.match.InterfaceMatch;
import com.enseirb.pfa.bastats.tournoi.KeyTable;
import com.enseirb.pfa.bastats.tournoi.fragment.CalendrierMatchPouleFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rsabir on 12/02/15.
 */
public class ActivitySelectionJoueur extends ActionBarActivity {
    private static final String TAG = "SEL JOUEUR";
    private static final int MATCH_REQUEST = 1;

    private Context mCtx;

    private List<Joueur> joueursEqA;
    private List<Joueur> joueursEqB;

    private List<HashMap<String,String>> mViewEquipeA;
    private List<HashMap<String,String>> mViewEquipeB;

    private ArrayList<Boolean> isSelectedJoueurA;
    private ArrayList<Boolean> isSelectedJoueurB;

    private int nbSelectedJoueurA=0;
    private int nbSelectedJoueurB=0;

    private TextView viewNbSelectedJoueurA;
    private TextView viewNbSelectedJoueurB;
    private TextView nomEquipeA;
    private TextView nomEquipeB;
    private int equipeAId;
    private int equipeBId;
    private int neutreAId;
    private int neutreBId;

    private SimpleAdapter adapterA;
    private SimpleAdapter adapterB;

    private Button next;

    private ListView eqA;
    private ListView eqB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_selection_joueur);
        mCtx = this;
        viewNbSelectedJoueurA = (TextView) findViewById(R.id.nb_selectione_eqA);
        viewNbSelectedJoueurB = (TextView) findViewById(R.id.nb_selectione_eqB);
        nomEquipeA = (TextView) findViewById(R.id.nom_equipeA);
        nomEquipeB = (TextView) findViewById(R.id.nom_equipeB);

        getExtraData();

        initListView();

        next = (Button) findViewById(R.id.button_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nbSelectedJoueurA<5 || nbSelectedJoueurB<5){
                    //Toast.makeText(v.getContext(),"Vous devez selectionnez 5 joueurs au minimum par equipe",Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mCtx);
                    alertDialogBuilder.setTitle("Sélection incomplète");

                    alertDialogBuilder
                            .setMessage("Attention vous avez sélectionné moins de 5 joueurs pour le match à venir. Souhaitez-vous continuer ?")
                            .setCancelable(false)
                            .setPositiveButton("Continuer",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    lancerMatch();
                                }
                            })
                            .setNegativeButton(getResources().getString(R.string.dialog_retour), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                else {
                    lancerMatch();
                    //Toast.makeText(v.getContext(), "next", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initListView(){
        mViewEquipeA = new ArrayList<HashMap<String,String>>();
        mViewEquipeB = new ArrayList<HashMap<String,String>>();
        eqA = (ListView) findViewById(R.id.listview_selection_joueur_A);
        eqB = (ListView) findViewById(R.id.listview_selection_joueur_B);


        for(Joueur joueur : joueursEqA){
            HashMap<String,String> map = new HashMap<String,String>();
            map.put("nom", "Nom: "+tronquer(joueur.getNom(), 17)+ " \tPrénom: " + tronquer(joueur.getPrenom(), 17));
            map.put("numero", tronquer(joueur.getNumero(),4));
            map.put("pseudo", "Pseudo: "+tronquer(joueur.getPseudo(),30));
            //hm.put("id",String.valueOf(numeroJoueurA.get(i)));
            mViewEquipeA.add(map);
        }
        for(Joueur joueur : joueursEqB){
            HashMap<String,String> map = new HashMap<String,String>();
            map.put("nom", "Nom: "+tronquer(joueur.getNom(), 17)+ " \tPrénom: " + tronquer(joueur.getPrenom(), 17));
            map.put("numero", tronquer(joueur.getNumero(),4));
            map.put("pseudo", "Pseudo: "+tronquer(joueur.getPseudo(),30));
            //hm.put("id",String.valueOf(numeroJoueurB.get(i)));
            mViewEquipeB.add(map);
        }


        final View.OnClickListener Modifier_Numero_listener_A = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = (int) v.getTag();
                final Button b = (Button ) v;
                final String numeroActuel = ((Button) v).getText().toString();

                final AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setTitle("Changer le numero du joueur");
                //alert.setMessage("Please type the name of note: ");

                // Set an EditText view to get user input
                final EditText editText = new EditText(v.getContext());
                editText.setText(numeroActuel);
                alert.setView(editText);

                alert.setPositiveButton("Changer", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        final String value = editText.getText().toString();
                        if (value.equals(numeroActuel))
                            return;
                        InputMethodManager imm = (InputMethodManager) getSystemService(
                                INPUT_METHOD_SERVICE);
                        imm.showSoftInput(editText,InputMethodManager.SHOW_IMPLICIT);
                        eqA.smoothScrollToPosition(position);
                        mViewEquipeA.get(position).put("numero", value);
                        Log.d("TAG","position dans la liste: "+position+ "numéro changé en "+value );
                        adapterA.notifyDataSetChanged();
                        // Changement dans la Bdd
                        DBJoueurDAO tableJoueur = new DBJoueurDAO(mCtx);
                        Joueur joueur = new Joueur(tableJoueur.getWithId(joueursEqA.get(position).getId()));
                        joueur.setNumero(value);
                        tableJoueur.update(joueur.getId(), joueur);
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });

                alert.show();
            }
        };

        final View.OnClickListener Modifier_Numero_listener_B = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = (int) v.getTag();
                final String numeroActuel = ((Button) v).getText().toString();

                final AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setTitle("Changer le numero du joueur");
                //alert.setMessage("Please type the name of note: ");

                // Set an EditText view to get user input
                final EditText editText = new EditText(v.getContext());
                editText.setText(numeroActuel);
                alert.setView(editText);


                alert.setPositiveButton("Changer", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = editText.getText().toString();
                        if (value.equals(numeroActuel))
                            return;
                        InputMethodManager imm = (InputMethodManager) getSystemService(
                                INPUT_METHOD_SERVICE);
                        imm.showSoftInput(editText,InputMethodManager.SHOW_IMPLICIT);
                        eqB.smoothScrollToPosition(position);
                        mViewEquipeB.get(position).put("numero", value);
                        Log.d("TAG","position dans la liste: "+position+ "numéro changé en "+value );
                        adapterB.notifyDataSetChanged();
                        // Changement dans la bdd
                        DBJoueurDAO tableJoueur = new DBJoueurDAO(mCtx);
                        Joueur joueur = new Joueur(tableJoueur.getWithId(joueursEqB.get(position).getId()));
                        joueur.setNumero(value);
                        tableJoueur.update(joueur.getId(), joueur);
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });

                alert.show();
            }
        };

        String[] from = {"nom","pseudo", "numero"};
        int[] to = { R.id.nomJoueur,R.id.pseudoJoueur,R.id.changer_numero_joueur };
        adapterA = new SimpleAdapter(ActivitySelectionJoueur.this, mViewEquipeA, R.layout.element_selectioner_joueur, from, to){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View viewG = super.getView(position, convertView, parent);
                Button changer_numero = (Button) viewG.findViewById(R.id.changer_numero_joueur);
                changer_numero.setTag(position);
                changer_numero.setOnClickListener(Modifier_Numero_listener_A);
                if (isSelectedJoueurA.get(position))
                    viewG.setBackgroundColor(getResources().getColor(R.color.grey_stats));
                return viewG;
            }
        };
        adapterB = new SimpleAdapter(ActivitySelectionJoueur.this, mViewEquipeB, R.layout.element_selectioner_joueur, from, to){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View viewG = super.getView(position, convertView, parent);
                Button changer_numero = (Button) viewG.findViewById(R.id.changer_numero_joueur);
                changer_numero.setTag(position);
                changer_numero.setOnClickListener(Modifier_Numero_listener_B);
                if (isSelectedJoueurB.get(position))
                    viewG.setBackgroundColor(getResources().getColor(R.color.grey_stats));
                return viewG;
            }
        };

        eqA.setAdapter(adapterA);
        eqB.setAdapter(adapterB);

        eqA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (isSelectedJoueurA.get(position)) {
                isSelectedJoueurA.set(position,false);
                nbSelectedJoueurA--;
                view.setBackgroundColor(Color.TRANSPARENT);
            }else{
                isSelectedJoueurA.set(position,true);
                view.setEnabled(true);
                nbSelectedJoueurA++;
                view.setBackgroundColor(getResources().getColor(R.color.grey_stats));
            }
            viewNbSelectedJoueurA.setText("Joueurs Selectionnés: "+nbSelectedJoueurA);
            if (nbSelectedJoueurA<5)
                    viewNbSelectedJoueurA.setTextColor(getResources().getColor(R.color.red));
            else
                    viewNbSelectedJoueurA.setTextColor(getResources().getColor(R.color.green));
            }
        });

        eqB.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (isSelectedJoueurB.get(position)) {
                isSelectedJoueurB.set(position, false);
                nbSelectedJoueurB--;
                view.setBackgroundColor(Color.TRANSPARENT);
            } else {
                isSelectedJoueurB.set(position, true);
                view.setEnabled(true);
                nbSelectedJoueurB++;
                view.setBackgroundColor(getResources().getColor(R.color.grey_stats));
            }
            viewNbSelectedJoueurB.setText("Joueurs Selectionnés: " + nbSelectedJoueurB);
            if (nbSelectedJoueurB < 5)
                viewNbSelectedJoueurB.setTextColor(getResources().getColor(R.color.red));
            else
                viewNbSelectedJoueurB.setTextColor(getResources().getColor(R.color.green));
            }
        });

        setSelection(equipeAId, eqA, joueursEqA);
        setSelection(equipeBId, eqB, joueursEqB);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            Toast.makeText(this, "keyboard visible", Toast.LENGTH_SHORT).show();
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            Toast.makeText(this, "keyboard hidden", Toast.LENGTH_SHORT).show();
        }
    }

    private void getExtraData(){
        Intent i = getIntent();
        nomEquipeA.setText(i.getStringExtra(KeyTable.NOM_EQUIPE_A));
        nomEquipeB.setText(i.getStringExtra(KeyTable.NOM_EQUIPE_B));
        equipeAId = i.getIntExtra(KeyTable.ID_EQUIPE_A,-1);
        equipeBId = i.getIntExtra(KeyTable.ID_EQUIPE_B,-1);
        List<Integer> playerIdsA = new ArrayList<>(i.getIntegerArrayListExtra(KeyTable.JOUEURS_EQ_A));
        List<Integer> playerIdsB = new ArrayList<>(i.getIntegerArrayListExtra(KeyTable.JOUEURS_EQ_B));
        neutreAId = i.getIntExtra(KeyTable.ID_NEUTRE_A,-1);
        neutreBId = i.getIntExtra(KeyTable.ID_NEUTRE_B, -1);
        joueursEqA = new ArrayList<>();
        joueursEqB = new ArrayList<>();
        isSelectedJoueurA = new ArrayList<Boolean>();
        isSelectedJoueurB = new ArrayList<Boolean>();
        DBJoueurDAO tableJoueur = new DBJoueurDAO(mCtx);
        for (Integer id : playerIdsA){
            joueursEqA.add(tableJoueur.getWithId(id));
            isSelectedJoueurA.add(false);
        }
        for (Integer id : playerIdsB){
            joueursEqB.add(tableJoueur.getWithId(id));
            isSelectedJoueurB.add(false);
        }
    }

    private void lancerMatch(){
        DBMatchDAO tableMatch = new DBMatchDAO(mCtx);
        DBFormationDAO tableFormation = new DBFormationDAO(mCtx);
        DBFormationJoueurDAO tableFormationJoueur = new DBFormationJoueurDAO(mCtx);
        tableFormation.insert(equipeAId);
        int formationA = tableFormation.getLast().getId();
        Log.d(TAG, "nouvelle formation "+formationA+" pour l'équipe "+equipeAId);

        tableFormation.insert(equipeBId);
        int formationB = tableFormation.getLast().getId();
        Log.d(TAG, "nouvelle formation "+formationB+" pour l'équipe "+equipeBId);

        // On enregistre la sélection utilisée pour le match
        int lastFormationA = -1;
        int lastFormationB = -1;
        if (tableFormation.getLastFormationUse(equipeAId) != null) {
            lastFormationA = tableFormation.getLastFormationUse(equipeAId).getId();
            Log.d(TAG, "lastFormationA: "+lastFormationA);
            tableFormation.removeWithId(lastFormationA);
            Log.d(TAG, "Suppression formationA "+lastFormationA);
        }
        if (tableFormation.getLastFormationUse(equipeBId) != null) {
            lastFormationB = tableFormation.getLastFormationUse(equipeBId).getId();
            Log.d(TAG, "lastFormationB: "+lastFormationB);
            tableFormation.removeWithId(lastFormationB);
            Log.d(TAG, "Suppression formationB "+lastFormationB);
        }
        // On en créer une nouvelle qu'on va remplir
        tableFormation.insert(equipeAId, Formation.LAST);
        tableFormation.insert(equipeBId, Formation.LAST);
        lastFormationA = tableFormation.getLastFormationUse(equipeAId).getId();
        lastFormationB = tableFormation.getLastFormationUse(equipeBId).getId();
        Log.d(TAG, "insertion lastFormationA: "+lastFormationA);
        Log.d(TAG, "insertion lastFormationB: "+lastFormationB);


        for(int i = 0; i < joueursEqA.size(); i++){
            if (isSelectedJoueurA.get(i)){
                tableFormationJoueur.insert(formationA, joueursEqA.get(i).getId());
                tableFormationJoueur.insert(lastFormationA, joueursEqA.get(i).getId());
                Log.d(TAG, "insertion joueur "+joueursEqA.get(i).toString()+" à la formation "+formationA);
            }
        }

        for(int i = 0; i < joueursEqB.size(); i++){
            if (isSelectedJoueurB.get(i)){
                tableFormationJoueur.insert(formationB, joueursEqB.get(i).getId());
                tableFormationJoueur.insert(lastFormationB, joueursEqB.get(i).getId());
                Log.d(TAG, "insertion joueur "+joueursEqB.get(i).toString()+" à la formation "+formationB);
            }
        }

        // Insertion joueur neutre
        tableFormationJoueur.insert(formationA, neutreAId);
        tableFormationJoueur.insert(formationB, neutreBId);
        //tableFormationJoueur.insert(lastFormationA, neutreAId);
        //tableFormationJoueur.insert(lastFormationB, neutreBId);

        if (getIntent().getStringExtra(KeyTable.INTENT_RECEIVE).equals(KeyTable.FROM_CALENDRIER)){
            Log.d("TAG", "from match calendrier poule");
            int matchId = getIntent().getIntExtra(KeyTable.MATCH_ID,-1);
            Match match = tableMatch.getWithId(matchId);
            match.setFormationEquipeA(formationA);
            match.setFormationEquipeB(formationB);
            tableMatch.update(matchId, match);
            Intent i = new Intent(mCtx, InterfaceMatch.class);
            i.putExtra(KeyTable.MATCH_ID, matchId);
            i.putExtra(KeyTable.ARG_NB_PERIODES, getIntent().getIntExtra(KeyTable.ARG_NB_PERIODES,-1));
            i.putExtra(KeyTable.DUREE_PERIODE, getIntent().getStringExtra(KeyTable.DUREE_PERIODE));
            startActivityForResult(i, MATCH_REQUEST);
        }else if (getIntent().getStringExtra(KeyTable.INTENT_RECEIVE).equals(KeyTable.FROM_MATCH_AMICAL)) {
            /** Get the current date */
            final Calendar cal = Calendar.getInstance();
            int pYear = cal.get(Calendar.YEAR);
            int pMonth = cal.get(Calendar.MONTH);
            int pDay = cal.get(Calendar.DAY_OF_MONTH);
            Log.d("TAG", "from match amical");
            Match match = new Match(nomEquipeA + " vs " + nomEquipeB,
                    pDay+"/"+pMonth+"/"+pYear, formationA, formationB, Match.AMICAL);
            tableMatch.insert(match);
            int matchId = tableMatch.getLast().getId();
            Log.d("TAG", "insertion du match" + matchId);
            Intent i = new Intent(mCtx, InterfaceMatch.class);
            i.putExtra(KeyTable.MATCH_ID, matchId);
            i.putExtra(KeyTable.ARG_NB_PERIODES, getIntent().getIntExtra(KeyTable.ARG_NB_PERIODES, -1));
            i.putExtra(KeyTable.DUREE_PERIODE, getIntent().getStringExtra(KeyTable.DUREE_PERIODE));
            i.putExtra(KeyTable.COULEUR_A,getIntent().getStringExtra(KeyTable.COULEUR_A));
            i.putExtra(KeyTable.COULEUR_B,getIntent().getStringExtra(KeyTable.COULEUR_B));
            Log.d("CAL","duree match dans la poule: "+getIntent().getStringExtra(KeyTable.DUREE_PERIODE)
                    +" pour "+getIntent().getIntExtra(KeyTable.ARG_NB_PERIODES,-1)+" périodes");
            Log.d("TAG", "start match " + matchId + " NbPériodes = "
                    + getIntent().getIntExtra(KeyTable.ARG_NB_PERIODES, -1)
                    + " DuréePériode= " + getIntent().getStringExtra(KeyTable.DUREE_PERIODE));
            startActivityForResult(i, MATCH_REQUEST);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == MATCH_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
                Log.d("Selection", "setResult()");
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

    public void setSelection(int equipeId, ListView mList, List<Joueur> mListJoueurs){
        DBFormationDAO tableFormation = new DBFormationDAO(mCtx);
        DBFormationJoueurDAO tableFormationJoueur = new DBFormationJoueurDAO(mCtx);
        DBJoueurDAO tableJoueur = new DBJoueurDAO(mCtx);



        if (tableJoueur.getLastSelection(equipeId) != null){
            List<Joueur> derniereSelection = new ArrayList<>(tableJoueur.getLastSelection(equipeId));
            Log.d(TAG, "Derniere sélection: "+derniereSelection.toString());
            for (int i=0; i < mListJoueurs.size(); i++){
                Joueur joueur = mListJoueurs.get(i);
                if (belongTo(joueur, derniereSelection)){
                    mList.performItemClick(
                            mList.getAdapter().getView(i, null, null),
                            i,
                            mList.getAdapter().getItemId(i));
                }
            }
        }
    }

    public boolean belongTo(Joueur joueur, List<Joueur> mListeJoueur){
        int id = joueur.getId();
        for (Joueur cmp : mListeJoueur){
            if (cmp.getId() == id)
                return true;
        }
        return false;
    }
}
