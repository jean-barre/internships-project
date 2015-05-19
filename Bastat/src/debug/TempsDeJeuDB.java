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
import com.enseirb.pfa.bastats.data.DAO.DBTempsDeJeuDAO;
import com.enseirb.pfa.bastats.data.model.TempsDeJeu;

import java.util.ArrayList;
import java.util.List;


public class TempsDeJeuDB extends ActionBarActivity {
    private ListView mListView;
    private ArrayAdapter<TempsDeJeu> arrayAdapter;
    private List<TempsDeJeu> mListe;

    private DBTempsDeJeuDAO tableTempsDeJeu;

    private EditText editIdTemps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temps_de_jeu);

        editIdTemps = (EditText) findViewById(R.id.editIdTemps);

        mListView = (ListView) findViewById(R.id.listViewTemps);

        mListe = new ArrayList<TempsDeJeu>();
        arrayAdapter = new ArrayAdapter<TempsDeJeu>(this, android.R.layout.simple_list_item_1, mListe);

        tableTempsDeJeu = new DBTempsDeJeuDAO(this);


        final Button add = (Button) findViewById(R.id.ajouter);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TempsDeJeu nouveau = new TempsDeJeu(rand(5), String.valueOf(rand(3600)), rand(6), rand(6));
                tableTempsDeJeu.insert(nouveau);
                viderEdit();
                afficherTout();
            }
        });

        final Button modify = (Button) findViewById(R.id.modifier);
        modify.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TempsDeJeu modifier = new TempsDeJeu(rand(5), String.valueOf(rand(3600)), rand(6), rand(6));
                int id = Integer.parseInt(editIdTemps.getText().toString());
                tableTempsDeJeu.update(id, modifier);
                viderEdit();
                afficherTout();
            }
        });

        final Button delete = (Button) findViewById(R.id.supprimer);
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tableTempsDeJeu.removeWithId(Integer.parseInt(editIdTemps.getText().toString()));
                viderEdit();
                afficherTout();
            }
        });
        afficherTout();
        mListView.setAdapter(arrayAdapter);
    }

    private void viderEdit(){
        editIdTemps.setText("");
    }

    public void afficherTout(){
        if (tableTempsDeJeu.getAll() != null) {
            mListe.clear();
            mListe.addAll(tableTempsDeJeu.getAll());
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
