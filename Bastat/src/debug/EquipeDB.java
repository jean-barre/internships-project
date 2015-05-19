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
import com.enseirb.pfa.bastats.data.model.Equipe;

import java.util.ArrayList;
import java.util.List;


public class EquipeDB extends ActionBarActivity {
    private ListView mListView;
    private ArrayAdapter<Equipe> arrayAdapter;
    private List<Equipe> mListe;

    private DBEquipeDAO tableEquipe;

    private EditText editIdEquipe;
    private EditText editNomEquipe;
    private EditText editInitiales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipe_db);

        editIdEquipe = (EditText) findViewById(R.id.editIdEquipe);
        editNomEquipe = (EditText) findViewById(R.id.editNomEquipe);
        editInitiales = (EditText) findViewById(R.id.editInitiales);

        mListView = (ListView) findViewById(R.id.listViewEquipe);

        mListe = new ArrayList<Equipe>();
        arrayAdapter = new ArrayAdapter<Equipe>(this, android.R.layout.simple_list_item_1, mListe);

        tableEquipe = new DBEquipeDAO(this);


        final Button addPlayer = (Button) findViewById(R.id.ajouter);
        addPlayer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Equipe nouveau = new Equipe(editNomEquipe.getText().toString(),
                                            editInitiales.getText().toString());
                tableEquipe.insert(nouveau);
                viderEdit();
                afficherTout();
            }
        });

        final Button modifyPlayer = (Button) findViewById(R.id.modifier);
        modifyPlayer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Equipe modifier = new Equipe(editNomEquipe.getText().toString(),
                                             editInitiales.getText().toString());
                // Vérifier id non nul
                int id = Integer.parseInt(editIdEquipe.getText().toString());
                tableEquipe.update(id, modifier);
                viderEdit();
                afficherTout();
            }
        });

        final Button deletePlayer = (Button) findViewById(R.id.supprimer);
        deletePlayer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tableEquipe.removeWithId(Integer.parseInt(editIdEquipe.getText().toString()));
                viderEdit();
                afficherTout();
            }
        });

        afficherTout();
        mListView.setAdapter(arrayAdapter);
    }

    private void viderEdit(){
        editIdEquipe.setText("");
        editNomEquipe.setText("");
        editInitiales.setText("");
    }

    public void afficherTout(){
        if (tableEquipe.getAll() != null) {
            mListe.clear();
            mListe.addAll(tableEquipe.getAll());
            arrayAdapter.notifyDataSetChanged();
        }
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
