package com.enseirb.pfa.bastats.tournoi.algorithme;

import android.util.Log;

import com.enseirb.pfa.bastats.data.model.Match;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rchabot on 25/02/15.
 */
public class GenerationCalendrierMatchPoule {
    private static final String TAG = "Calendrier";

    private List<Integer> equipesPoule;
    private List<Match> calendrierMatch;
    private int nbConfrontations;

    public GenerationCalendrierMatchPoule(List<Integer> equipesPoule, int nbConfrontations){
        this.equipesPoule = new ArrayList<>();
        this.equipesPoule.addAll(equipesPoule);
        Log.d(TAG,"nombre d'équipes: "+this.equipesPoule.size());
        this.calendrierMatch = new ArrayList<>();
        this.nbConfrontations = nbConfrontations;
    }

    public List<Match> simpleGeneration(){
        for (int j=0; j < this.nbConfrontations; j++) {
            for (int k = 0; k < equipesPoule.size(); k++) {
                int teamId = equipesPoule.get(k);
                for (int i = k + 1; i < equipesPoule.size(); i++) {
                    calendrierMatch.add(new Match(teamId, equipesPoule.get(i)));
                    Log.d(TAG, "Ajout confrontation entre " + teamId + " vs " + equipesPoule.get(i));
                }
            }
        }
        return calendrierMatch;
    }

}
