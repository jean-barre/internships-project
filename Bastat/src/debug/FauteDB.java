package com.enseirb.pfa.bastats.debug;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.enseirb.pfa.bastats.R;
import com.enseirb.pfa.bastats.data.DAO.DBJoueurDAO;
import com.enseirb.pfa.bastats.data.DAO.DBTempsDeJeuDAO;
import com.enseirb.pfa.bastats.data.DAO.action.DBFauteDAO;
import com.enseirb.pfa.bastats.data.model.TempsDeJeu;
import com.enseirb.pfa.bastats.data.model.action.Faute;
import com.enseirb.pfa.bastats.data.model.Joueur;

import java.util.ArrayList;
import java.util.List;


public class FauteDB extends ActionBarActivity {
    private ListView mListView;
    private ArrayAdapter<Faute> arrayAdapter;
    private List<Faute> mListe;

    private DBFauteDAO tableFaute;
    private DBTempsDeJeuDAO tableTempsDeJeu;
    private DBJoueurDAO tableJoueur;

    private EditText editIdTemps;
    private EditText editIdJoueur;
    private EditText editIdFaute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faute_db);

        editIdTemps = (EditText) findViewById(R.id.editIdTemps);
        editIdJoueur = (EditText) findViewById(R.id.editIdJoueur);
        editIdFaute = (EditText) findViewById(R.id.editIdFaute);

        mListView = (ListView) findViewById(R.id.listViewFaute);

        mListe = new ArrayList<Faute>();
        arrayAdapter = new ArrayAdapter<Faute>(this, android.R.layout.simple_list_item_1, mListe);

        tableFaute = new DBFauteDAO(this);
        tableTempsDeJeu = new DBTempsDeJeuDAO(this);
        tableJoueur = new DBJoueurDAO(this);

        final Button add = (Button) findViewById(R.id.ajouter);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TempsDeJeu tempsDeJeu = tableTempsDeJeu.getWithId(rand(21));
                Joueur joueur = tableJoueur.getWithId(rand(13));
                Joueur cible = tableJoueur.getWithId(rand(13));
                Faute nouveau = new Faute(tempsDeJeu, joueur, cible);
                tableFaute.insert(nouveau);
                viderEdit();
                afficherTout();
            }
        });

        final Button modify = (Button) findViewById(R.id.modifier);
        modify.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Faute modifier = new Faute(tableTempsDeJeu.getWithId(rand(21)),
                        tableJoueur.getWithId(rand(13)),
                        tableJoueur.getWithId(rand(13)));
                int id = Integer.parseInt(editIdFaute.getText().toString());
                tableFaute.update(id, modifier);
                viderEdit();
                afficherTout();
            }
        });

        final Button delete = (Button) findViewById(R.id.supprimer);
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tableFaute.removeWithId(Integer.parseInt(editIdFaute.getText().toString()));
                viderEdit();
                afficherTout();
            }
        });
        afficherTout();
        mListView.setAdapter(arrayAdapter);
    }

    private void viderEdit(){
        editIdJoueur.setText("");
        editIdFaute.setText("");
        editIdTemps.setText("");
    }

    public void afficherTout() {
        if (tableFaute.getAll() != null) {
            mListe.clear();
            mListe.addAll(tableFaute.getAll());
            arrayAdapter.notifyDataSetChanged();
        }
    }

    private int rand(int max){
        return (int) (Math.random() * max + 1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ecran_accueil, menu);
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
}
