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
import android.widget.EditText;

import com.enseirb.pfa.bastats.R;
import com.enseirb.pfa.bastats.data.DAO.DBTournoiDAO;
import com.enseirb.pfa.bastats.data.model.Tournoi;


public class NouveauTournoi extends ActionBarActivity {

    private Button buttonSuivant;
    private EditText editTextLieuTournoi;
    private EditText editTextNomTournoi;

    private Context mCtx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nouveau_tournoi);

        mCtx = this;
        editTextLieuTournoi = (EditText) findViewById(R.id.editText_lieu_tournoi);
        editTextNomTournoi = (EditText) findViewById(R.id.editText_nom_tournoi);

        buttonSuivant = (Button) findViewById(R.id.button_valider);

        buttonSuivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextNomTournoi.getText().toString().trim().isEmpty()){
                    createAlert("Vous n'avez pas saisie de libellé pour le tournoi");

                }
                else if (editTextLieuTournoi.getText().toString().trim().isEmpty()){
                    createAlert("Vous n'avez pas saisi de lieux pour le tournoi");
                }
                else {
                    int id = -1;
                    DBTournoiDAO tableTournoi = new DBTournoiDAO(mCtx);
                    Tournoi tournoi = new Tournoi(editTextNomTournoi.getText().toString().trim(),
                        editTextLieuTournoi.getText().toString().trim());
                    tableTournoi.insert(tournoi);
                    id = tableTournoi.getLast().getId();
                    Log.d("TAG", "Insertion tournoi avec id: " + id);

                    Intent intent = new Intent(mCtx, SelectionEquipeTournoiActivity.class);
                    intent.putExtra(KeyTable.ARG_ID_TOURNOI, id);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nouveau_tournoi, menu);
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

    private void createAlert(String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mCtx);
        alertDialogBuilder.setTitle(getResources().getString(R.string.missing_information));

        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton(getResources().getString(R.string.dialog_retour), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}
