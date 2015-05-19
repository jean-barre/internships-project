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
import com.enseirb.pfa.bastats.data.DAO.action.DBPasseDAO;
import com.enseirb.pfa.bastats.data.model.TempsDeJeu;
import com.enseirb.pfa.bastats.data.model.Joueur;
import com.enseirb.pfa.bastats.data.model.action.Passe;

import java.util.ArrayList;
import java.util.List;


public class PasseDB extends ActionBarActivity {
    private ListView mListView;
    private ArrayAdapter<Passe> arrayAdapter;
    private List<Passe> mListe;

    private DBPasseDAO tablePasse;
    private DBTempsDeJeuDAO tableTempsDeJeu;
    private DBJoueurDAO tableJoueur;

    private EditText editIdTemps;
    private EditText editIdJoueur;
    private EditText editIdPasse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passe_db);

        editIdTemps = (EditText) findViewById(R.id.editIdTemps);
        editIdJoueur = (EditText) findViewById(R.id.editIdJoueur);
        editIdPasse = (EditText) findViewById(R.id.editIdPasse);

        mListView = (ListView) findViewById(R.id.listViewPasse);

        mListe = new ArrayList<Passe>();
        arrayAdapter = new ArrayAdapter<Passe>(this, android.R.layout.simple_list_item_1, mListe);

        tablePasse = new DBPasseDAO(this);
        tableTempsDeJeu = new DBTempsDeJeuDAO(this);
        tableJoueur = new DBJoueurDAO(this);

        final Button add = (Button) findViewById(R.id.ajouter);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TempsDeJeu tempsDeJeu = tableTempsDeJeu.getWithId(rand(21));
                Joueur joueur = tableJoueur.getWithId(rand(13));
                Joueur cible = tableJoueur.getWithId(rand(13));
                Passe nouveau = new Passe(tempsDeJeu, joueur, cible);
                tablePasse.insert(nouveau);
                viderEdit();
                afficherTout();
            }
        });

        final Button modify = (Button) findViewById(R.id.modifier);
        modify.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Passe modifier = new Passe(tableTempsDeJeu.getWithId(rand(21)),
                        tableJoueur.getWithId(rand(13)),
                        tableJoueur.getWithId(rand(13)));
                int id = Integer.parseInt(editIdPasse.getText().toString());
                tablePasse.update(id, modifier);
                viderEdit();
                afficherTout();
            }
        });

        final Button delete = (Button) findViewById(R.id.supprimer);
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tablePasse.removeWithId(Integer.parseInt(editIdPasse.getText().toString()));
                viderEdit();
                afficherTout();
            }
        });
        afficherTout();
        mListView.setAdapter(arrayAdapter);
    }

    private void viderEdit(){
        editIdJoueur.setText("");
        editIdPasse.setText("");
        editIdTemps.setText("");
    }

    public void afficherTout() {
        if (tablePasse.getAll() != null) {
            mListe.clear();
            mListe.addAll(tablePasse.getAll());
            arrayAdapter.notifyDataSetChanged();
        }
    }

    private int rand(int max){
        return (int) (Math.random() * max + 1 );
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
