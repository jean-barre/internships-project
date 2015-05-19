package com.enseirb.pfa.bastats.tournoi;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.enseirb.pfa.bastats.R;
import com.enseirb.pfa.bastats.data.DAO.DBClassementTournoiDAO;
import com.enseirb.pfa.bastats.data.DAO.DBEquipeDAO;
import com.enseirb.pfa.bastats.data.DAO.DBTournoiDAO;
import com.enseirb.pfa.bastats.data.DAO.DBTournoiEquipeDAO;
import com.enseirb.pfa.bastats.data.model.ClassementTournoi;
import com.enseirb.pfa.bastats.data.model.Equipe;
import com.enseirb.pfa.bastats.data.model.Tournoi;

import java.util.ArrayList;
import java.util.List;

public class AjouterClassement extends ActionBarActivity {
    private Button pPosition;
    private Button pPoints;
    private Button btnValider;
    private TextView vPoints;
    private TextView vPosition;
    private Spinner spinner;
    private List<Integer> lEquipe;
    private Context mCtx;
    private int tournoiId;
    private int nbEquipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_classement);

        mCtx = this;
        lEquipe = new ArrayList<>();
        tournoiId = getIntent().getIntExtra(KeyTable.ARG_ID_TOURNOI,-1);
        DBTournoiDAO tableTournoi = new DBTournoiDAO(mCtx);
        nbEquipes = tableTournoi.getWithId(tournoiId).getNbEquipeMax();
        vPosition = (TextView) findViewById(R.id.textView_position);
        vPoints = (TextView) findViewById(R.id.textView_points);

        pPosition = (Button) findViewById(R.id.button_position);
        pPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberPicker("Position au classement",1,nbEquipes,vPosition);
            }
        });

        pPoints = (Button) findViewById(R.id.button_points);
        pPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberPicker("Nombre de points",0,100,vPoints);
            }
        });

        spinner = (Spinner) findViewById(R.id.spinner_equipe);
        setSpinnerEquipes(spinner);

        btnValider = (Button) findViewById(R.id.button_valider);
        btnValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBClassementTournoiDAO cltDAO = new DBClassementTournoiDAO(mCtx);
                ClassementTournoi clt = new ClassementTournoi(
                        lEquipe.get(spinner.getSelectedItemPosition()),
                        tournoiId,
                        Integer.parseInt(vPosition.getText().toString()),
                        Integer.parseInt(vPoints.getText().toString()));
                cltDAO.insert(clt);
                Log.d("CLT_ADD", clt.toString());
                Intent i = new Intent();
                setResult(RESULT_OK,i);
                finish();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ajouter_classement, menu);
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

    private void showNumberPicker(String title, int min, int max, TextView textView){
        final Dialog d = new Dialog(mCtx);
        final TextView tv = textView;
        d.setTitle(title);
        d.setContentView(R.layout.dialog_number_picker);
        Button positiveButton = (Button) d.findViewById(R.id.positiveButton);
        Button negativeButton = (Button) d.findViewById(R.id.negativeButton);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker);
        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        np.setMaxValue(max);
        np.setMinValue(min);
        positiveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                tv.setText(String.valueOf(np.getValue()));
                d.dismiss();
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }

    private void setSpinnerEquipes(Spinner sp) {
        List<String> liste_equipes = new ArrayList<String>();
        DBEquipeDAO tableEquipe = new DBEquipeDAO(mCtx);
        DBTournoiEquipeDAO tableTournoiEquipe = new DBTournoiEquipeDAO(mCtx);
        lEquipe.clear();
        lEquipe.addAll(tableTournoiEquipe.getTeamsList(tournoiId));
        for (int id : lEquipe) {
            Equipe element = tableEquipe.getWithId(id);
            liste_equipes.add(element.getNom());
        }
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, liste_equipes);
        spinner_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sp.setAdapter(spinner_adapter);
    }
}
