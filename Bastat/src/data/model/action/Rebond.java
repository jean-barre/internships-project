package com.enseirb.pfa.bastats.data.model.action;

import com.enseirb.pfa.bastats.data.model.TempsDeJeu;
import com.enseirb.pfa.bastats.data.model.Joueur;

public class Rebond extends Action{
    public static int NO_ID = -1;
    public static final int OFFENSIF = 1;
    public static final int DEFENSIF = 2;
    // 4 champs action
    private int     id;
    private int caractere; // OFF ou DEF

    public Rebond(){
        setCaractere(NO_ID);
        setCommentaire("");
    }

    public Rebond(TempsDeJeu tdj, Joueur joueur, int caractere){
        setTempsDeJeu(tdj.getId());
        setJoueurActeur(joueur.getId());
        setCaractere(caractere);
        setCommentaire("");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public int getCaractere() {
        return caractere;
    }

    public void setCaractere(int caractère) {
        this.caractere = caractère;
    }

    @Override
    public String toString(){
        return "Tps:"+getTempsDeJeu()+"\tJoueur: "+getJoueurActeur()+"\tGenre: "+getCaractere()+
                "\tId: "+getId();
    }
}
