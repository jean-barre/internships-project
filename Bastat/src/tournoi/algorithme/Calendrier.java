package com.enseirb.pfa.bastats.tournoi.algorithme;

import android.util.Log;
import android.view.ViewGroup;

import com.enseirb.pfa.bastats.data.model.Match;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rchabot on 08/03/15.
 */
public class Calendrier {
    private int nbEquipes;
    private List<Match> calendrierMatch;
    private int nbConfrontations;
    private boolean withPetiteFinale;
    private static final String TAG = "Calendrier";

    public static final String FINALE = "Finale";
    public static final String DEMI = "Demi";
    public static final String QUART = "Quart";
    public static final String HUITUIEME = "Huitième";
    public static final String SEIZIEME = "Seizieme";
    public static final String PETITE = "Petite";

    public Calendrier(int nbEquipes, boolean withPetiteFinale){
        this.calendrierMatch = new ArrayList<>();
        this.nbConfrontations = 1;
        this.nbEquipes = nbEquipes;
        this.withPetiteFinale = withPetiteFinale;
    }

    /*public Calendrier(ArrayList<Integer> listeEquipes){
        this.calendrierMatch = new ArrayList<>();
        this.nbConfrontations = 1;
        this.nbEquipes = nbEquipes;
    }*/

    public List<Match> fullGeneration(){
        Log.d(TAG, "nbEquipes = "+nbEquipes);
        int nbMatch = nbEquipes/2;
        Log.d(TAG, "nbMatch = "+nbMatch);
        do {
            makeTableau(nbMatch);
            nbMatch = nbMatch/2;
        } while (nbMatch != 0);
        if (withPetiteFinale){
            makeMatch(PETITE,1);
        }
        return calendrierMatch;
    }

    public List<Match> simpleGeneration(){
        int nbMatch = nbEquipes/2;
        Log.d(TAG, "nbMatch = "+nbMatch);
        makeTableau(nbMatch);
        return calendrierMatch;
    }

    private void makeTableau(int nbMatch){
        switch (nbMatch) {
            case 1:
                makeMatch(FINALE, 1);
                break;
            case 2:
                makeMatch(DEMI + "-finale ", 2);
                break;
            case 4:
                makeMatch(QUART + " de finale ", 4);
                break;
            case 8:
                makeMatch(HUITUIEME + " de finale ", 8);
                break;
            case 16:
                makeMatch(SEIZIEME + " de finale ", 16);
                break;
            case 32:
                makeMatch("Trente-deuxième de finale ", 32);
                break;
            default:
                Log.d(TAG, "nbMatch = " + nbMatch);
                break;
        }
    }

    private void makeMatch(String libelle, int nbMatch){
        if (nbMatch == 1){
            calendrierMatch.add(new Match(libelle));
            Log.d(TAG, "Création match: "+libelle);
        } else {
            for (int i = 1; i <= nbMatch; i++) {
                calendrierMatch.add(new Match(libelle + i));
                Log.d(TAG, "Création match: " + libelle + i);
            }
        }
    }
}
