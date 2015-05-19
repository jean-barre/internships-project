package com.enseirb.pfa.bastats.tournoi.algorithme;

import android.content.Context;
import android.util.Log;

import com.enseirb.pfa.bastats.data.DAO.DBFormationDAO;
import com.enseirb.pfa.bastats.data.DAO.DBFormationJoueurDAO;
import com.enseirb.pfa.bastats.data.DAO.DBMatchDAO;
import com.enseirb.pfa.bastats.data.DAO.action.DBFauteDAO;
import com.enseirb.pfa.bastats.data.model.Equipe;
import com.enseirb.pfa.bastats.data.model.Match;
import com.enseirb.pfa.bastats.data.model.Poule;
import com.enseirb.pfa.bastats.tournoi.KeyTable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rchabot on 25/02/15.
 */
public class ClassementEquipe  {
    private int position = 0;
    private String nomEquipe;
    private int equipeId=-1;
    private int nbJouer=0;
    private int nbVictoires=0;
    private int nbDefaite=0;
    private int nbNul=0;
    private int nbPts=0;
    private int paniersPour=0;
    private int paniersContre=0;
    private int difference=0;

    public ClassementEquipe(Poule poule, Equipe equipe, Context context){
        DBMatchDAO tableMatch = new DBMatchDAO(context);
        DBFormationDAO tableFormation = new DBFormationDAO(context);
        int ptsVictoire = poule.getPointsVictoire();
        int ptsDefaite = poule.getPointsDefaite();
        int ptsNul = poule.getPointsNul();

        int equipeId = equipe.getId();
        this.nomEquipe = equipe.getNom();
        this.equipeId = equipe.getId();

        List<Match> matchEquipe = new ArrayList<>(tableMatch.getMatchPouleDeEquipe(poule.getId(),equipeId));
        Log.d("Match de l'équipe"+equipeId+" dans la poule "+poule.getId(), matchEquipe.toString());

        for (Match match : matchEquipe){
            //if (match.getResultat() == equipeId){ // equipeId vainqueur
            if (tableFormation.belongTeam(match.getResultat(), equipeId)){
                this.nbVictoires++;
                this.nbPts+=ptsVictoire;
                this.nbJouer++;
                if (diffScore(match) > 0) { // scoreA > scoreB : A vainqueur
                    this.paniersPour += match.getScoreEquipeA();
                    this.paniersContre += match.getScoreEquipeB();
                    this.difference += diffScore(match);
                } else if (diffScore(match) < 0) { // scoreA < scoreB : B vainqueur
                    this.paniersPour += match.getScoreEquipeB();
                    this.paniersContre += match.getScoreEquipeA();
                    this.difference -= diffScore(match);
                } else {
                    Log.d("BUG", "Score nul mais équipe victoirieus");
                }

            } else if (match.getResultat() == Match.RESULTAT_NUL){ // Match nul
                this.nbNul++;
                this.nbPts+=ptsNul;
                this.nbJouer++;
                this.paniersContre+= match.getScoreEquipeA();
                this.paniersContre+= match.getScoreEquipeB();
                this.difference+=0;
            } else if (match.getResultat() == Match.MATCH_NON_JOUE){
                // Do nothing
            } else { // Défaite de equipeId
                this.nbDefaite++;
                this.nbPts+=ptsDefaite;
                this.nbJouer++;
                if (diffScore(match) < 0) { // scoreA < scoreB : A perdant
                    this.paniersPour += match.getScoreEquipeA();
                    this.paniersContre += match.getScoreEquipeB();
                    this.difference += diffScore(match);
                } else if (diffScore(match) > 0) { // scoreA > scoreB : B perdant
                    this.paniersPour += match.getScoreEquipeB();
                    this.paniersContre += match.getScoreEquipeA();
                    this.difference -= diffScore(match);
                } else {
                    Log.d("BUG", "Score nul mais équipe victoirieus");
                }
            }

        }
        Log.d("clt equipe", this.toString());

    }

    private int diffScore(Match match){
        return match.getScoreEquipeA() - match.getScoreEquipeB();
    }

    public HashMap<String,String> toHaspMapFormat(){
        HashMap<String,String> map = new HashMap<>();
        map.put(KeyTable.ID_EQUIPE_A, String.valueOf(this.equipeId));
        map.put(KeyTable.CLT, String.valueOf(this.position));
        map.put(KeyTable.EQUIPE, String.valueOf(this.nomEquipe));
        map.put(KeyTable.POINTS, String.valueOf(this.nbPts));
        map.put(KeyTable.NB_VICTOIRES, String.valueOf(this.nbVictoires));
        map.put(KeyTable.NB_NULS, String.valueOf(this.nbNul));
        map.put(KeyTable.NB_DEFAITES, String.valueOf(this.nbDefaite));
        map.put(KeyTable.JOUER,String.valueOf(this.nbJouer));
        map.put(KeyTable.POUR, String.valueOf(this.paniersPour));
        map.put(KeyTable.CONTRE, String.valueOf(this.paniersContre));
        map.put(KeyTable.DIFF, String.valueOf(this.difference));
        return map;
    }

    public void setPosition(int pos) {
        this.position = pos;
    }

    public int getNbPts() {
        return nbPts;
    }

    public static Comparator<ClassementEquipe> FruitNameComparator
            = new Comparator<ClassementEquipe>() {

        public int compare(ClassementEquipe eq1, ClassementEquipe eq2) {

            //ascending order
            return eq2.getNbPts() - eq1.getNbPts();

            //descending order
            //return fruitName2.compareTo(fruitName1);
        }

    };

    /*
    public int compareTo(ClassementEquipe clt) {

        int comparePts = clt.getNbPts();
        //ascending order
        return this.nbPts - comparePts;

        //descending order
        //return compareQuantity - this.quantity;

    }*/

    @Override
    public String toString(){
        return "\n"+position+" "+nomEquipe+" "+nbPts+" "+nbJouer+" "+nbVictoires+" "+nbNul+" "+nbDefaite+" "+paniersPour+" "
                +paniersContre+" "+difference;
    }

    /*
    public void show(String TAG, HashMap<String,String> map){
        Log.d(TAG,
        map.get("position")+" "+
        map.get("equipe")+" "+
        map.get("pts")+" "+
        map.get("victoire")+" "+
        map.get("nul")+" "+
        map.get("defaite")+" "+
        map.get("pour")+" "+
        map.get("contre")+" "+
	    map.get("difference"));
    }*/
}
