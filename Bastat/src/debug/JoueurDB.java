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
import com.enseirb.pfa.bastats.data.model.Joueur;

import java.util.ArrayList;
import java.util.List;


public class JoueurDB extends ActionBarActivity {
    // Declare the UI components
    private ListView playersListView;
    private ArrayAdapter<Joueur> arrayAdapter;
    private List<Joueur> listeJoueurs;

    private DBJoueurDAO tableJoueur;

    private EditText editNom;
    private EditText editPrenom;
    private EditText editId;
    private EditText editNumero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);

        editNom = (EditText) findViewById(R.id.editNomJoueur);
        editPrenom = (EditText) findViewById(R.id.editPrenomJoueur);
        editId = (EditText) findViewById(R.id.editIdJoueur);
        editNumero = (EditText) findViewById(R.id.editNumeroJoueur);

        playersListView = (ListView) findViewById(R.id.listViewJoueurs);

        listeJoueurs = new ArrayList<Joueur>();
        arrayAdapter = new ArrayAdapter<Joueur>(this, android.R.layout.simple_list_item_1, listeJoueurs);

        tableJoueur = new DBJoueurDAO(this);


        final Button addPlayer = (Button) findViewById(R.id.ajouterJoueur);
        addPlayer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Joueur nouveauJoueur = new Joueur(editNom.getText().toString(),
                                                  editPrenom.getText().toString());
                tableJoueur.insert(nouveauJoueur);
                viderEdit();
                afficherTout();
            }
        });

        final Button modifyPlayer = (Button) findViewById(R.id.modifierJoueur);
        modifyPlayer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Joueur joueurModifier = new Joueur(editNom.getText().toString(),
                                                   editPrenom.getText().toString());
                // Vérifier id non nul
                int id = Integer.parseInt(editId.getText().toString());
                tableJoueur.update(id, joueurModifier);
                viderEdit();
                afficherTout();
            }
        });

        final Button deletePlayer = (Button) findViewById(R.id.supprimerJoueur);
        deletePlayer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tableJoueur.removeWithId(Integer.parseInt(editId.getText().toString()));
                viderEdit();
                afficherTout();
            }
        });

        afficherTout();
        playersListView.setAdapter(arrayAdapter);
    }

    private void viderEdit(){
        editNom.setText("");
        editPrenom.setText("");
        editId.setText("");
        editNumero.setText("");
    }

    public void afficherTout(){
        if (tableJoueur.getAll() != null) {
            listeJoueurs.clear();
            listeJoueurs.addAll(tableJoueur.getAll());
            arrayAdapter.notifyDataSetChanged();
        }
    }

    private int rand(int max){
        return (int) (Math.random() * max + 1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
