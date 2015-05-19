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
import com.enseirb.pfa.bastats.data.DAO.action.DBRebondDAO;
import com.enseirb.pfa.bastats.data.model.TempsDeJeu;
import com.enseirb.pfa.bastats.data.model.Joueur;
import com.enseirb.pfa.bastats.data.model.action.Rebond;

import java.util.ArrayList;
import java.util.List;


public class RebondDB extends ActionBarActivity {
    private ListView mListView;
    private ArrayAdapter<Rebond> arrayAdapter;
    private List<Rebond> mListe;

    private DBRebondDAO tableRebond;
    private DBTempsDeJeuDAO tableTempsDeJeu;
    private DBJoueurDAO tableJoueur;

    private EditText editIdTemps;
    private EditText editIdJoueur;
    private EditText editIdRebond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rebond_db);

        editIdTemps = (EditText) findViewById(R.id.editIdTemps);
        editIdJoueur = (EditText) findViewById(R.id.editIdJoueur);
        editIdRebond = (EditText) findViewById(R.id.editIdRebond);

        mListView = (ListView) findViewById(R.id.listViewRebond);

        mListe = new ArrayList<Rebond>();
        arrayAdapter = new ArrayAdapter<Rebond>(this, android.R.layout.simple_list_item_1, mListe);

        tableRebond = new DBRebondDAO(this);
        tableTempsDeJeu = new DBTempsDeJeuDAO(this);
        tableJoueur = new DBJoueurDAO(this);

        final Button add = (Button) findViewById(R.id.ajouter);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TempsDeJeu tempsDeJeu = tableTempsDeJeu.getWithId(rand(21));
                Joueur joueur = tableJoueur.getWithId(rand(13));
                Rebond nouveau = new Rebond(tempsDeJeu, joueur, flip());
                tableRebond.insert(nouveau);
                viderEdit();
                afficherTout();
            }
        });

        final Button modify = (Button) findViewById(R.id.modifier);
        modify.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Rebond modifier = new Rebond(tableTempsDeJeu.getWithId(rand(21)),
                        tableJoueur.getWithId(rand(13)),
                        flip());
                int id = Integer.parseInt(editIdRebond.getText().toString());
                tableRebond.update(id, modifier);
                viderEdit();
                afficherTout();
            }
        });

        final Button delete = (Button) findViewById(R.id.supprimer);
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tableRebond.removeWithId(Integer.parseInt(editIdRebond.getText().toString()));
                viderEdit();
                afficherTout();
            }
        });
        afficherTout();
        mListView.setAdapter(arrayAdapter);
    }

    private void viderEdit(){
        editIdJoueur.setText("");
        editIdRebond.setText("");
        editIdTemps.setText("");
    }

    public void afficherTout() {
        if (tableRebond.getAll() != null) {
            mListe.clear();
            mListe.addAll(tableRebond.getAll());
            arrayAdapter.notifyDataSetChanged();
        }
    }

    private int rand(int max){
        return (int) (Math.random() * max + 1);
    }

    private int flip(){
        int i = rand(2);
        if (i == 0)
            return 0;
        if (i == 1)
            return 1;
        else return -1;
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
