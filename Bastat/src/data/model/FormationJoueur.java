package com.enseirb.pfa.bastats.data.model;

/**
 * Created by dohko on 12/02/15.
 */
public class FormationJoueur {
    private static int NO_ID = -1;

    private int formationId;
    private int joueurId;
    private String numero;

    public FormationJoueur(){
        setFormationId(NO_ID);
        setJoueurId(NO_ID);
        setNumero("");

    }

    public FormationJoueur(Formation formation,Joueur joueur,String numero){
        setNumero(numero);
        setJoueurId(joueur.getId());
        setFormationId(formation.getId());

    }




    public int getFormationId() {
        return formationId;
    }

    public void setFormationId(int formationId) {
        this.formationId = formationId;
    }

    public int getJoueurId() {
        return joueurId;
    }

    public void setJoueurId(int joueurId) {
        this.joueurId = joueurId;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
