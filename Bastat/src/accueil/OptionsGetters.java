package com.enseirb.pfa.bastats.accueil;

import android.content.SharedPreferences;


/**
 * Created by rsabir on 21/01/15.
 */
public class OptionsGetters {

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
    private String nbMinutesParPeriodes_pardefault="10:00";
    private boolean automatique_pardefault=true;


    // L'argument doit être passé ainsi:
    // OptionsGetters optionsGetters = new OptionsGetters(getPreferences("PageOptions",Context.MODE_PRIVATE);

    public OptionsGetters(SharedPreferences sharedPreferences){

        nbPeriodes = sharedPreferences.getInt(INDEX_NBPERIODES, nbPeriodes_pardefault);
        nbMinutesParPeriodes = sharedPreferences.getString(INDEX_NBMINUTESPARPERIODES, nbMinutesParPeriodes_pardefault);
        automatique = sharedPreferences.getBoolean(INDEX_AUTOMATIQUE, automatique_pardefault);
    }

    public int getNbPeriodes(){
        return nbPeriodes;
    }

    public String getNbMinutesParPeriodes() {

        return nbMinutesParPeriodes;
    }

    public boolean isAutomatique() {
        return automatique;
    }

}

