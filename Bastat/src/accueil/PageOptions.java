package com.enseirb.pfa.bastats.accueil;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.enseirb.pfa.bastats.R;

/**
 * Created by rsabir on 20/01/15.
 */
public class PageOptions extends Activity {

    private SharedPreferences sharedPreferences;

    //les valeurs qu'on recuperera du formulaire
    private int nbPeriodes;
    private String nbMinutesParPeriodes;
    private boolean automatique;

    //les index des valeurs dans les Preferences
    private static final String INDEX_NBPERIODES= "options_nbPeriodes";
    private static final String INDEX_NBMINUTESPARPERIODES= "options_nbMiutesParPeriodes";
    private static final String INDEX_AUTOMATIQUE= "options_automatique";

    //la valeur par default des valeurs si c'est la première fois qu'on les assigne
    private int nbPeriodes_pardefault=4;
    private int nbMinutesParPeriodes_pardefault=10;
    private boolean automatique_pardefault=true;

    //les views
    private TextView view_nbPeriodes;
    private TextView view_nbMinutesParPeriodes;
    private Switch view_automatique;
    private Button view_valider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_match);
        sharedPreferences = getSharedPreferences("PageOptions", Context.MODE_PRIVATE);

        OptionsGetters optionsGetters= new OptionsGetters(sharedPreferences);
        nbPeriodes = optionsGetters.getNbPeriodes();//sharedPreferences.getInt(INDEX_NBPERIODES, nbPeriodes_pardefault);
        nbMinutesParPeriodes = optionsGetters.getNbMinutesParPeriodes();//sharedPreferences.getInt(INDEX_NBMINUTESPARPERIODES, nbMinutesParPeriodes_pardefault);
        automatique = optionsGetters.isAutomatique();///sharedPreferences.getBoolean(INDEX_AUTOMATIQUE, automatique_pardefault);

        view_nbPeriodes = (TextView) findViewById(R.id.option_nombrePeriodes);
        view_nbMinutesParPeriodes = (TextView) findViewById(R.id.option_minutesPeriodes);
        view_automatique = (Switch) findViewById(R.id.option_automatique);
        view_valider = (Button) findViewById(R.id.option_valider);

        view_nbPeriodes.setText(String.valueOf(nbPeriodes));
        view_nbMinutesParPeriodes.setText(String.valueOf(nbMinutesParPeriodes));
        view_automatique.setChecked(!automatique);

        view_nbPeriodes.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                v = (TextView) v;
                String tmp = ((TextView) v).getText().toString();
                nbPeriodes = Integer.parseInt(tmp);
            }
        });

        view_nbMinutesParPeriodes.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                v = (TextView) v;
                String tmp = ((TextView) v).getText().toString();
                nbMinutesParPeriodes = tmp;
            }
        });

        view_automatique.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v = (Switch) v;
                automatique = ((Switch) v).isChecked();
                return false;
            }
        });

        view_valider.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final Toast t = Toast.makeText(v.getContext(), "Modification Enregistré", Toast.LENGTH_SHORT);
                t.show();
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        t.cancel();
                    }
                }, 500);
                finish();
                return true;
            }
        });


    }


    @Override
    protected void onStop () {
        super.onStop();
        view_nbPeriodes.clearFocus();
        view_nbMinutesParPeriodes.clearFocus();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(INDEX_NBPERIODES, nbPeriodes);
        editor.putString(INDEX_NBMINUTESPARPERIODES, nbMinutesParPeriodes);
        editor.putBoolean(INDEX_AUTOMATIQUE, automatique);
        editor.commit();

    }
}