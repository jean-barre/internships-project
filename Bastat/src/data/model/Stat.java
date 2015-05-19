package com.enseirb.pfa.bastats.data.model;

/**
 * Created by Jean on 11/03/15.
 */
public class Stat {

    private int idJoueur;
    private String nomJoueur;
    private int nbPasseDecisive;
    private int nbFautes;
    private int nbContre;
    private int nbRebondOff;
    private int nbRebondDef;
    private int nbInterception;
    private int nbPerteBalle;
    private int nbLfS;
    private int nbLfF;
    private int nbShoot2S;
    private int nbShoot2F;
    private int nbShoot3S;
    private int nbShoot3F;

    public Stat(int id) {
        this.idJoueur = id;
    }

    public Stat(){
        setNbContre(0);
        setNbFautes(0);
        setNbPasseDecisive(0);
        setNbInterception(0);
        setNbRebondOff(0);
        setNbRebondDef(0);
        setNbPerteBalle(0);
        setNbLfS(0);
        setNbLfF(0);
        setNbShoot2S(0);
        setNbShoot2F(0);
        setNbShoot3S(0);
        setNbShoot3F(0);
    }

    // Getters

    public int getIdJoueur() {
        return this.idJoueur;
    }

    public String getNomJoueur() {
        return this.nomJoueur;
    }

    public int getNbPasseDecisive() {
        return this.nbPasseDecisive;
    }

    public int getNbFautes() {
        return this.nbFautes;
    }

    public int getNbContre() {
        return this.nbContre;
    }

    public int getNbRebondOff() {
        return this.nbRebondOff;
    }

    public int getNbRebondDef() {
        return this.nbRebondDef;
    }

    public int getNbInterception() {
        return this.nbInterception;
    }

    public int getNbPerteBalle() {
        return this.nbPerteBalle;
    }

    public int getNbLfS() { return this.nbLfS; }

    public int getNbLfF() { return this.nbLfF; }

    public int getNbShoot2S() {
        return this.nbShoot2S;
    }

    public int getNbShoot2F() {
        return this.nbShoot2F;
    }

    public int getNbShoot3S() {
        return this.nbShoot3S;
    }

    public int getNbShoot3F() {
        return this.nbShoot3F;
    }



    //Setters

    public void setNomJoueur(String nom) {
        this.nomJoueur = nom;
    }

    public void setNbPasseDecisive(int nb) {
        this.nbPasseDecisive = nb;
    }

    public void setNbFautes(int nb) {
        this.nbFautes = nb;
    }

    public void setNbContre(int nb) {
        this.nbContre = nb;
    }

    public void setNbRebondOff(int nb) {
        this.nbRebondOff = nb;
    }

    public void setNbRebondDef(int nb) {
        this.nbRebondDef = nb;
    }

    public void setNbInterception(int nb) {
        this.nbInterception = nb;
    }

    public void setNbPerteBalle(int nb) {
        this.nbPerteBalle = nb;
    }

    public void setNbLfS(int nb) { this.nbLfS = nb; }

    public void setNbLfF(int nb) { this.nbLfF = nb; }

    public void setNbShoot2S(int nb) {
        this.nbShoot2S = nb;
    }

    public void setNbShoot2F(int nb) {
        this.nbShoot2F = nb;
    }

    public void setNbShoot3S(int nb) {
        this.nbShoot3S = nb;
    }

    public void setNbShoot3F(int nb) {
        this.nbShoot3F = nb;
    }

    @Override
    public String toString(){
        return getNomJoueur()+", 2s:"+getNbShoot2S()+", 2f:"+getNbShoot2F()+", "+getNbShoot3S()+", "
                +getNbShoot3F()+", ctr:"+getNbContre()+", ftes"+getNbFautes()
                +", pdec:"+getNbPasseDecisive()+", int:"+getNbInterception();
    }
}
