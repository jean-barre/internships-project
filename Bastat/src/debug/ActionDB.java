package com.enseirb.pfa.bastats.debug;

import com.enseirb.pfa.bastats.R;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


import com.enseirb.pfa.bastats.data.DAO.DBJoueurDAO;
import com.enseirb.pfa.bastats.data.DAO.DBTempsDeJeuDAO;
import com.enseirb.pfa.bastats.data.DAO.action.DBActionDAO;
import com.enseirb.pfa.bastats.data.model.action.Action;

import java.util.ArrayList;
import java.util.List;


public class ActionDB extends ActionBarActivity {
    // Declare the UI components
    private ListView mListView;
    private ArrayAdapter<Action> arrayAdapter;
    private List<Action> mListe;

    private DBActionDAO tableAction;
    private DBTempsDeJeuDAO tableTempsDeJeu;
    private DBJoueurDAO tableJoueur;

    private EditText editIdTemps;
    private EditText editIdJoueur;
    private EditText editIdAction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);

        editIdTemps = (EditText) findViewById(R.id.editIdTemps);
        editIdJoueur = (EditText) findViewById(R.id.editIdJoueur);
        editIdAction = (EditText) findViewById(R.id.editIdAction);

        mListView = (ListView) findViewById(R.id.listViewAction);

        mListe = new ArrayList<Action>();
        arrayAdapter = new ArrayAdapter<Action>(this, android.R.layout.simple_list_item_1, mListe);

        tableAction = new DBActionDAO(this);
        tableTempsDeJeu = new DBTempsDeJeuDAO(this);
        tableJoueur = new DBJoueurDAO(this);

        final Button add = (Button) findViewById(R.id.ajouter);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
             /*   TempsDeJeu tempsDeJeu = tableTempsDeJeu.getWithId(rand(21));
                Joueur joueur = tableJoueur.getWithId(rand(13));
                Shoot nouveau = new Shoot(tempsDeJeu, joueur, rand(3), rand(2)-1);
                tableShoot.insert(nouveau);
                viderEdit();
                afficherTout();*/
            }
        });

        final Button modify = (Button) findViewById(R.id.modifier);
        modify.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {/*
                Shoot modifier = new Shoot(tableTempsDeJeu.getWithId(rand(21)),
                        tableJoueur.getWithId(rand(13)),
                        rand(3), rand(2)-1);
                int id = Integer.parseInt(editIdShoot.getText().toString());
                tableShoot.update(id, modifier);
                viderEdit();
                afficherTout();*/
            }
        });

        final Button delete = (Button) findViewById(R.id.supprimer);
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*
                tableShoot.removeWithId(Integer.parseInt(editIdShoot.getText().toString()));
                viderEdit();
                afficherTout();
                */
            }
        });
        afficherTout();
        mListView.setAdapter(arrayAdapter);
    }

    private void viderEdit(){
        editIdJoueur.setText("");
        editIdAction.setText("");
        editIdTemps.setText("");
    }

    public void afficherTout() {
        if (tableAction.getAll() != null) {
            mListe.clear();
            mListe.addAll(tableAction.getAll());
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
