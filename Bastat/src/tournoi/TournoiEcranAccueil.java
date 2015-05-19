package com.enseirb.pfa.bastats.tournoi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.enseirb.pfa.bastats.R;
import com.enseirb.pfa.bastats.data.DAO.DBEquipeDAO;
import com.enseirb.pfa.bastats.data.DAO.DBTournoiDAO;
import com.enseirb.pfa.bastats.data.model.Equipe;
import com.enseirb.pfa.bastats.data.model.Tournoi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TournoiEcranAccueil extends ActionBarActivity {
    private static final String TAG = "TournoiEcranAccueill";

    private Button nouveauTournoi;
    private Button resumeTournoi;
    private Button deleteTournoi;

    private Context mCtx;

    private CharSequence[] charSequenceItems;
    private List<HashMap<String,String>> mListeTournoisDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournoi_ecran_accueil);

        mCtx = this;

        //createFakeTeam();
        mListeTournoisDb = new ArrayList<>();

        nouveauTournoi = (Button) findViewById(R.id.button_create_tournament);

        nouveauTournoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, NouveauTournoi.class);
                startActivity(intent);
            }
        });

        resumeTournoi = (Button) findViewById(R.id.buttonResumeTournament);

        getTournamentsFromDb();

        resumeTournoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
             builder.setTitle("Reprise d'un tournoi")
                  .setSingleChoiceItems(charSequenceItems, 0, new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface arg0, int arg1) {
                          //showToast("Some actions maybe? Selected index: " + arg1);
                      }

                  })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                if (!mListeTournoisDb.isEmpty()) {
                                    int selectedPosition =
                                            ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                    Intent intent = new Intent(mCtx, NavigationDrawerTournoi.class);
                                    intent.putExtra(KeyTable.ARG_ID_TOURNOI,
                                            Integer.parseInt(mListeTournoisDb.get(selectedPosition).get("id")));
                                    startActivity(intent);
                                }
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.dialog_retour),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        // removes the dialog from the screen
                                    }
                                })

                        .show();
                }
        });

        deleteTournoi = (Button) findViewById(R.id.buttonDeleteTournament);
        deleteTournoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setTitle("Reprise d'un tournoi")
                        .setSingleChoiceItems(charSequenceItems, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                //showToast("Some actions maybe? Selected index: " + arg1);
                            }

                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                int selectedPosition =
                                        ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                DBTournoiDAO tableTournoi = new DBTournoiDAO(mCtx);
                                tableTournoi.removeWithId(
                                        Integer.parseInt(mListeTournoisDb.get(selectedPosition).get("id")));
                                Log.d(TAG, "suppression tournoi "
                                        +mListeTournoisDb.get(selectedPosition).get("id"));
                                update();
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.dialog_retour),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        // removes the dialog from the screen
                                    }
                                })

                        .show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tournoi_ecran_accueil, menu);
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

    public void getTournamentsFromDb(){
        DBTournoiDAO tableTournoi = new DBTournoiDAO(mCtx);
        List<Tournoi> tmp = new ArrayList<>();
        if (tableTournoi.getAll() != null)
            tmp.addAll(tableTournoi.getAll());
        Log.d(TAG, "Copie des tournois de la bdd");
        for (Tournoi tournoi : tmp){
            HashMap<String,String> map = new HashMap<>();
            map.put("nom", tournoi.getLibelle());
            map.put("lieu", tournoi.getLieu());
            map.put("id",String.valueOf(tournoi.getId()));
            Log.d(TAG, "Copie tournoi "+ tournoi.getLibelle()+ "avec l'id "+tournoi.getId());
            mListeTournoisDb.add(map);
        }

        setUpSelection();
    }

    public void setUpSelection(){
        int size = mListeTournoisDb.size();
        Log.d(TAG,"Nombre de tournois: "+size);
        charSequenceItems = new CharSequence[size];
        for (int i=0; i < size; i++){
            charSequenceItems[i] = mListeTournoisDb.get(i).get("nom");
        }
    }

    private void update(){
        mListeTournoisDb.clear();
        getTournamentsFromDb();
        setUpSelection();
    }

    @Override
    public void onResume(){
        super.onResume();
        update();
    }
}
