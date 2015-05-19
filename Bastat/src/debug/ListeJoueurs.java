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
import com.enseirb.pfa.bastats.data.DAO.DBEquipeDAO;
import com.enseirb.pfa.bastats.data.DAO.DBJoueurDAO;
import com.enseirb.pfa.bastats.data.model.Joueur;

import java.util.ArrayList;
import java.util.List;


public class ListeJoueurs extends ActionBarActivity {
    private ListView playersListView;
    private ArrayAdapter<Joueur> arrayAdapter;
    private List<Joueur> listeJoueurs;

    private DBJoueurDAO tableJoueur;
    private DBEquipeDAO tableEquipe;

    private EditText editNomEquipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_joueurs);

        editNomEquipe = (EditText) findViewById(R.id.editNomEquipe);

        playersListView = (ListView) findViewById(R.id.listViewListeJoueurs);

        listeJoueurs = new ArrayList<Joueur>();
        arrayAdapter = new ArrayAdapter<Joueur>(this, android.R.layout.simple_list_item_1, listeJoueurs);

        tableJoueur = new DBJoueurDAO(this);
        tableEquipe = new DBEquipeDAO(this);

        final Button getListe = (Button) findViewById(R.id.getListe);
        getListe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listeJoueurs.clear();
                listeJoueurs.addAll(tableJoueur.getJoueursEquipe(tableEquipe.getEquipeId(editNomEquipe.getText().toString())));
                arrayAdapter.notifyDataSetChanged();
                playersListView.setAdapter(arrayAdapter);
            }
        });

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
