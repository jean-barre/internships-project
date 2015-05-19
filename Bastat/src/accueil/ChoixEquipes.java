package com.enseirb.pfa.bastats.accueil;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.enseirb.pfa.bastats.data.DAO.DBEquipeDAO;
import com.enseirb.pfa.bastats.data.DAO.DBMatchDAO;
import com.enseirb.pfa.bastats.data.model.Equipe;
import com.enseirb.pfa.bastats.R;
import com.enseirb.pfa.bastats.data.model.Match;
import com.enseirb.pfa.bastats.lancement.AjouterEquipe;
import com.enseirb.pfa.bastats.lancement.ActivityNouveauJoueur;
import com.enseirb.pfa.bastats.tournoi.KeyTable;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

// TODO
// OK       Pouvoir ajouter un libellé pour le match
// OK       Ne pas appeler la fonction getEquipeId(string name) en theorie.
// OK       Pouvoir initialiser les noms de equipes avec 2 equipes id en parametre de l'intent
//          et du coup bloquer le spinner sur les equipes selectionnées
// X        Afficher la couleur de l'equipe par defaut

public class ChoixEquipes extends ActionBarActivity {
    private DBEquipeDAO tableEquipe;
    private SharedPreferences sharedPreferences;
    private Context mCtx;
    //les valeurs qu'on recuperera du formulaire
    private int nbPeriodes;
    private String nbMinutesParPeriodes;
    private boolean automatique;
    private int matchId = -1;
    private int init_equipeAId = 0;
    private int init_equipeBId = 0;
    private List<Equipe> listAllTeams;

    //les index des valeurs dans les Preferences
    private static final String INDEX_NBPERIODES            = "options_nbPeriodes";
    private static final String INDEX_NBMINUTESPARPERIODES  = "options_nbMiutesParPeriodes";
    private static final String INDEX_AUTOMATIQUE           = "options_automatique";

    //la valeur par default des valeurs si c'est la première fois qu'on les assigne
    private int nbPeriodes_pardefault               = 4;
    private int nbMinutesParPeriodes_pardefault     = 10;
    private boolean automatique_pardefault          = true;

    //les views
    private TextView view_nbPeriodes;
    private TextView view_nbMinutesParPeriodes;
    private Switch view_automatique;

    // Request codes
    private final int NEW_TEAM_REQUEST = 11;
    private static final int MATCH_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_equipes);
        initDAO(this);

        mCtx                = this;
        sharedPreferences   = getSharedPreferences("PageOptions", Context.MODE_PRIVATE);
        final EditText textView_libelleMatch = (EditText) findViewById(R.id.libelle_match);
        final EditText textView_arbitre1 = (EditText) findViewById(R.id.arbitre1);
        final EditText textView_arbitre2 = (EditText) findViewById(R.id.arbitre2);

        if (getIntent().getStringExtra(KeyTable.INTENT_RECEIVE).equals(KeyTable.FROM_CALENDRIER)){
            Intent i = getIntent();
            matchId = i.getIntExtra(KeyTable.MATCH_ID,-1);
            init_equipeAId = i.getIntExtra(KeyTable.ID_EQUIPE_A,-1);
            init_equipeBId = i.getIntExtra(KeyTable.ID_EQUIPE_B,-1);
            nbMinutesParPeriodes = i.getStringExtra(KeyTable.DUREE_PERIODE);
            nbPeriodes = i.getIntExtra(KeyTable.ARG_NB_PERIODES,4);

            DBMatchDAO tableMatch = new DBMatchDAO(mCtx);
            textView_libelleMatch.setText(tableMatch.getWithId(matchId).getLibelle());
        } else {
            listAllTeams = tableEquipe.getAll();
            OptionsGetters optionsGetters = new OptionsGetters(sharedPreferences);
            nbPeriodes = optionsGetters.getNbPeriodes();
            nbMinutesParPeriodes = optionsGetters.getNbMinutesParPeriodes();
            automatique = optionsGetters.isAutomatique();
        }

        view_nbPeriodes             = (TextView)    findViewById(R.id.option_nombrePeriodes);
        view_nbMinutesParPeriodes   = (TextView)    findViewById(R.id.option_minutesPeriodes);
        view_automatique            = (Switch)      findViewById(R.id.option_automatique);
//        view_valider = (Button) findViewById(R.id.option_valider);

        view_nbPeriodes.setText(String.valueOf(nbPeriodes));
        view_nbMinutesParPeriodes.setText(nbMinutesParPeriodes);
        //view_automatique.setChecked(!automatique);

        view_nbPeriodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberPicker("Nombre de périodes",1,10,view_nbPeriodes);
            }
        });
        view_nbMinutesParPeriodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker("Durée d'une période",0,60,view_nbMinutesParPeriodes);
            }
        });

        view_automatique.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                automatique = ((Switch) v).isChecked();
                return false;
            }
        });

        TableRow row_valider = (TableRow) findViewById(R.id.row_valider);
        row_valider.setVisibility(View.GONE);

        final Spinner spinner_equipe_A = (Spinner) findViewById(R.id.spinner_equipe_A);
        setSpinnerEquipes(spinner_equipe_A, init_equipeAId);

        final Spinner spinner_equipe_B = (Spinner) findViewById(R.id.spinner_equipe_B);
        setSpinnerEquipes(spinner_equipe_B, init_equipeBId);

        final Spinner spinner_couleur_maillot_A = (Spinner) findViewById(R.id.spinner_couleur_maillot_A);
        setSpinnerMaillots(spinner_couleur_maillot_A, init_equipeAId);

        final Spinner spinner_couleur_maillot_B = (Spinner) findViewById(R.id.spinner_couleur_maillot_B);
        setSpinnerMaillots(spinner_couleur_maillot_B, init_equipeBId);


        Button valider = (Button) findViewById(R.id.valider);
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ActivityNouveauJoueur.class);
                Log.v("Libellé match", textView_libelleMatch.getText().toString());
                Log.v("Equipe A", spinner_equipe_A.getSelectedItem().toString());
                Log.v("Equipe A", spinner_couleur_maillot_A.getSelectedItem().toString());
                Log.v("Equipe B", spinner_equipe_B.getSelectedItem().toString());
                Log.v("Equipe B", spinner_couleur_maillot_B.getSelectedItem().toString());

                int equipe_A_id = -1;
                int equipe_B_id = -1;
                if (init_equipeAId == 0)
                    equipe_A_id = listAllTeams.get(spinner_equipe_A.getSelectedItemPosition()).getId();
                else equipe_A_id = init_equipeAId;
                if (init_equipeBId == 0)
                    equipe_B_id = listAllTeams.get(spinner_equipe_B.getSelectedItemPosition()).getId();
                else equipe_B_id = init_equipeBId;

                if (equipe_A_id == equipe_B_id) {
                    Toast.makeText(getApplicationContext(), "Veuillez sélectionner 2 équipes différentes", Toast.LENGTH_SHORT).show();
                } else if (getIntent().getStringExtra(KeyTable.INTENT_RECEIVE).equals(KeyTable.FROM_CALENDRIER)){
                    i.putExtra(KeyTable.ID_EQUIPE_A, equipe_A_id);
                    i.putExtra(KeyTable.ID_EQUIPE_B, equipe_B_id);
                    i.putExtra(KeyTable.INTENT_RECEIVE, KeyTable.FROM_CALENDRIER);

                    DBEquipeDAO tableEquipe = new DBEquipeDAO(mCtx);
                    Equipe eqA = new Equipe(tableEquipe.getWithId(equipe_A_id));
                    eqA.setCouleur(spinner_couleur_maillot_A.getSelectedItem().toString());

                    Equipe eqB = new Equipe(tableEquipe.getWithId(equipe_B_id));
                    eqB.setCouleur(spinner_couleur_maillot_B.getSelectedItem().toString());

                    tableEquipe.update(equipe_A_id,eqA);
                    tableEquipe.update(equipe_B_id,eqB);

                    DBMatchDAO tableMatch = new DBMatchDAO(mCtx);
                    Match match = new Match(tableMatch.getWithId(matchId));
                    match.setLibelle((textView_libelleMatch.getText().toString()));
                    match.setArbitreChamp(textView_arbitre1.getText().toString());
                    match.setArbitreAssistant(textView_arbitre2.getText().toString());
                    tableMatch.update(matchId, match);

                    // Autre solution ci-dessous on garde la valeur par défaut (moins pratique?)
                    i.putExtra(KeyTable.COULEUR_A, spinner_couleur_maillot_A.getSelectedItem().toString());
                    i.putExtra(KeyTable.COULEUR_B, spinner_couleur_maillot_B.getSelectedItem().toString());
                    i.putExtra(KeyTable.MATCH_ID, matchId);
                    i.putExtra(KeyTable.ARG_NB_PERIODES, Integer.parseInt(view_nbPeriodes.getText().toString()));
                    i.putExtra(KeyTable.DUREE_PERIODE, view_nbMinutesParPeriodes.getText().toString());
                    startActivityForResult(i, MATCH_REQUEST);

                } else {
                    i.putExtra("libelle", textView_libelleMatch.getText().toString());
                    i.putExtra("arbitre1", textView_arbitre1.getText().toString());
                    i.putExtra("arbitre2", textView_arbitre2.getText().toString());

                    i.putExtra(KeyTable.ID_EQUIPE_A, equipe_A_id);
                    i.putExtra(KeyTable.ID_EQUIPE_B, equipe_B_id);

                    DBEquipeDAO tableEquipe = new DBEquipeDAO(mCtx);
                    Equipe eqA = new Equipe(tableEquipe.getWithId(equipe_A_id));
                    eqA.setCouleur(spinner_couleur_maillot_A.getSelectedItem().toString());
                    Equipe eqB = new Equipe(tableEquipe.getWithId(equipe_B_id));
                    eqB.setCouleur(spinner_couleur_maillot_B.getSelectedItem().toString());
                    tableEquipe.update(equipe_A_id,eqA);
                    tableEquipe.update(equipe_B_id,eqB);
                    // Autre méthode
                    i.putExtra(KeyTable.COULEUR_A, spinner_couleur_maillot_A.getSelectedItem().toString());
                    i.putExtra(KeyTable.COULEUR_B, spinner_couleur_maillot_B.getSelectedItem().toString());
                    i.putExtra(KeyTable.INTENT_RECEIVE, KeyTable.FROM_MATCH_AMICAL);
                    i.putExtra(KeyTable.MATCH_ID, matchId);
                    i.putExtra(KeyTable.ARG_NB_PERIODES, Integer.parseInt(view_nbPeriodes.getText().toString()));
                    i.putExtra(KeyTable.DUREE_PERIODE, view_nbMinutesParPeriodes.getText().toString());
                    startActivityForResult(i, MATCH_REQUEST);
                }
            }
        });
    }

    public void initDAO(Context context){
        tableEquipe = new DBEquipeDAO(context);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choix_equipes, menu);
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
    protected void onStop () {
        super.onStop();
        view_nbPeriodes.clearFocus();
        view_nbMinutesParPeriodes.clearFocus();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(INDEX_NBPERIODES, nbPeriodes);
        editor.putString(INDEX_NBMINUTESPARPERIODES, nbMinutesParPeriodes);
        editor.putBoolean(INDEX_AUTOMATIQUE, automatique);
        editor.commit();
    }

    private void setSpinnerEquipes(Spinner sp, int init_equipeId) {
        Boolean enabled = true;
        List<String> liste_equipes = new ArrayList<String>();
        List<Equipe> list = new ArrayList<Equipe>();

        if (init_equipeId != 0) {
            list.add(tableEquipe.getWithId(init_equipeId));
            enabled = false;
            // init_equipeAId = 0;
        } else {
            list = listAllTeams;
        }

        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                Equipe element = list.get(i);
                liste_equipes.add(element.getNom());
            }

            ArrayAdapter<String> spinner_adapter =
                    new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, liste_equipes);
            spinner_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

            sp.setEnabled(enabled);
            sp.setClickable(enabled);
            sp.setAdapter(spinner_adapter);
        }
    }

    private void setSpinnerMaillots(Spinner sp, int init_equipe_id) {
        ArrayAdapter<CharSequence> spinner_adapter =
                ArrayAdapter.createFromResource(this, R.array.couleurs_maillots_array, android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        Equipe eq = null;
        if (init_equipe_id != 0) {
            eq = tableEquipe.getWithId(init_equipe_id);
        }
        else {
            if (listAllTeams != null)
                eq = tableEquipe.getWithId(listAllTeams.get(0).getId());
        }

        int pos = 0;
        String[] colors = getResources().getStringArray(R.array.couleurs_maillots_array);

        /*for (int i = 0; i < colors.length; i++) {
            if (colors[i].equals(eq.getCouleur())) {
                pos = i;
                Log.d("CHOIX", "Couleur trouvée"+eq.getCouleur());
            }
        }*/

        sp.setSelection(pos);
        sp.setAdapter(spinner_adapter);
    }

    public void ajouterEquipe(View view) {
        Intent i = new Intent(this, AjouterEquipe.class);
        startActivityForResult(i, NEW_TEAM_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEW_TEAM_REQUEST) {
            if (resultCode == RESULT_OK) {
                listAllTeams = tableEquipe.getAll();

                final Spinner spinner_equipe_A = (Spinner) findViewById(R.id.spinner_equipe_A);
                setSpinnerEquipes(spinner_equipe_A, 0);

                final Spinner spinner_equipe_B = (Spinner) findViewById(R.id.spinner_equipe_B);
                setSpinnerEquipes(spinner_equipe_B, 0);
            }
        }
        if (requestCode == MATCH_REQUEST) {
            if (resultCode == RESULT_OK) {
                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
                Log.d("Nouveau", "setResult()");
                finish();
            }
        }
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

        // Buttons
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
}
