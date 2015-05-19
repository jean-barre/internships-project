package com.enseirb.pfa.bastats.accueil;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.enseirb.pfa.bastats.R;
import com.enseirb.pfa.bastats.data.model.action.Action;
import com.enseirb.pfa.bastats.debug.ActionDB;
import com.enseirb.pfa.bastats.debug.ContreDB;
import com.enseirb.pfa.bastats.debug.EquipeDB;
import com.enseirb.pfa.bastats.debug.FauteDB;
import com.enseirb.pfa.bastats.debug.InterceptionDB;
import com.enseirb.pfa.bastats.debug.JoueurDB;
import com.enseirb.pfa.bastats.debug.ListeJoueurs;
import com.enseirb.pfa.bastats.debug.PasseDB;
import com.enseirb.pfa.bastats.debug.PerteBalleDB;
import com.enseirb.pfa.bastats.debug.RebondDB;
import com.enseirb.pfa.bastats.debug.ShootDB;
import com.enseirb.pfa.bastats.debug.TempsDeJeuDB;

import java.util.ArrayList;
import java.util.List;


public class VerificationDB extends ActionBarActivity {
    // Declare the UI components
    private ListView choiceListView;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> listeChoix;
    private Context mCtx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_db);
        mCtx = this;
        choiceListView = (ListView) findViewById(R.id.listViewChoice);
        listeChoix = new ArrayList<String>();
        listeChoix.add("Joueur");
        listeChoix.add("Temps de jeu");
        listeChoix.add("Shoot");
        listeChoix.add("Rebond");
        listeChoix.add("Passe");
        listeChoix.add("Faute");
        listeChoix.add("Interception");
        listeChoix.add("Contre");
        listeChoix.add("Turnover");
        listeChoix.add("Equipe");
        listeChoix.add("Match");
        listeChoix.add("Liste Joueurs");
        listeChoix.add("Formation Joueur");
        listeChoix.add("Action");
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listeChoix);
        choiceListView.setAdapter(arrayAdapter);

        choiceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch(position){
                    case (0):
                        intent = new Intent(mCtx, JoueurDB.class);
                        startActivity(intent);
                        break;
                    case (1):
                        intent = new Intent(mCtx, TempsDeJeuDB.class);
                        startActivity(intent);
                        break;
                    case (2):
                        intent = new Intent(mCtx, ShootDB.class);
                        startActivity(intent);
                        break;
                    case (3):
                        intent = new Intent(mCtx, ActionDB.class);
                        startActivity(intent);
                        break;

                    case (4):
                        intent = new Intent(mCtx, PasseDB.class);
                        startActivity(intent);
                        break;
                    case (5):
                        intent = new Intent(mCtx, FauteDB.class);
                        startActivity(intent);
                        break;
                    case (6):
                        intent = new Intent(mCtx, InterceptionDB.class);
                        startActivity(intent);
                        break;
                    case (7):
                        intent = new Intent(mCtx, ContreDB.class);
                        startActivity(intent);
                        break;
                    case (8):
                        intent = new Intent(mCtx, PerteBalleDB.class);
                        startActivity(intent);
                        break;
                    case (9):
                        intent = new Intent(mCtx, EquipeDB.class);
                        startActivity(intent);
                        break;
                    case (11):
                        intent = new Intent(mCtx, ListeJoueurs.class);
                        startActivity(intent);
                        break;
                    case (12):
                        intent = new Intent(mCtx, ActionDB.class);
                        startActivity(intent);
                        break;
                }

            }
        });
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