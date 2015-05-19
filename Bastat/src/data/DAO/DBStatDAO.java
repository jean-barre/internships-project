package com.enseirb.pfa.bastats.data.DAO;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.enseirb.pfa.bastats.data.DAO.action.*;
import com.enseirb.pfa.bastats.data.model.Joueur;
import com.enseirb.pfa.bastats.data.model.Stat;
import com.enseirb.pfa.bastats.data.model.action.Rebond;
import com.enseirb.pfa.bastats.data.model.action.Shoot;

/**
 * Created by Jean on 11/03/15.
 */
public class DBStatDAO extends BaseDAO {
    private static final String TAG = "Stats";

    public DBStatDAO(Context context) {
        super(context);
        this.mDb = this.open();
    }

    public Stat getStatFromJoueur(Joueur joueur, int matchId) {
        Stat stat = new Stat(joueur.getId());
        stat.setNomJoueur(joueur.getNom());
        Log.d(TAG, joueur.toString()+ " pour le match "+matchId);

        int nbPasseDecisive = getNbPasseDecisive(stat.getIdJoueur(), matchId);
        stat.setNbPasseDecisive(nbPasseDecisive);
        Log.d(TAG, "nbPasseDec: "+nbPasseDecisive);

        int nbFautes = getNbFautes(stat.getIdJoueur(), matchId);
        stat.setNbFautes(nbFautes);
        Log.d(TAG, "nbFautOffc: "+nbFautes);

        /*int nbFauteDef = getNbFauteDef(stat.getIdJoueur(), matchId);
        stat.setNbFauteDef(nbFauteDef);
        Log.d(TAG, "nbFauteDef: "+nbFauteDef);*/

        int nbContre = getNbContre(stat.getIdJoueur(), matchId);
        stat.setNbContre(nbContre);
        Log.d(TAG, "nbContre: "+nbContre);

        int nbRebondOff = getNbRebondOff(stat.getIdJoueur(), matchId);
        stat.setNbRebondOff(nbRebondOff);
        Log.d(TAG, "nbRebondOff: "+nbRebondOff);

        int nbRebondDef = getNbRebondDef(stat.getIdJoueur(), matchId);
        stat.setNbRebondDef(nbRebondDef);
        Log.d(TAG, "nbRebondDef: "+nbRebondDef);

        int nbInterception = getNbInterception(stat.getIdJoueur(), matchId);
        stat.setNbInterception(nbInterception);
        Log.d(TAG, "nbInterception: "+nbInterception);

        int nbPerteBalle = getNbPerteBalle(stat.getIdJoueur(), matchId);
        stat.setNbPerteBalle(nbPerteBalle);
        Log.d(TAG, "nbPerteBalle: "+nbPerteBalle);

        int nbLfS = getNbLfS(stat.getIdJoueur(), matchId);
        stat.setNbLfS(nbLfS);
        Log.d(TAG, "nbLancerFrancS: "+nbLfS);

        int nbLfF = getNbLfF(stat.getIdJoueur(), matchId);
        stat.setNbLfF(nbLfF);
        Log.d(TAG, "nbLancerFrancF: "+nbLfF);

        int nbShoot2S = getNbShoot2S(stat.getIdJoueur(), matchId);
        stat.setNbShoot2S(nbShoot2S);
        Log.d(TAG, "nbShoot2S: "+nbShoot2S);

        int nbShoot2F = getNbShoot2F(stat.getIdJoueur(), matchId);
        stat.setNbShoot2F(nbShoot2F);
        Log.d(TAG, "nbShoot2F: "+nbShoot2F);

        int nbShoot3S = getNbShoot3S(stat.getIdJoueur(), matchId);
        stat.setNbShoot3S(nbShoot3S);
        Log.d(TAG, "nbShoot3S: "+nbShoot3S);

        int nbShoot3F = getNbShoot3F(stat.getIdJoueur(), matchId);
        stat.setNbShoot3F(nbShoot3F);
        Log.d(TAG, "nbShoot3F: "+nbShoot3F);

        return stat;
    }

    public int getNbPasseDecisive(int joueurId, int matchId) {
        Cursor c = mDb.rawQuery("SELECT COUNT(*)" +
                " FROM "+ DBPasseDAO.TABLE_NAME + " AS TYPE, ACTION, TEMPS_DE_JEU" +
                " WHERE TEMPS_DE_JEU.MATCH_ID = " +Integer.toString(matchId)+
                " AND TEMPS_DE_JEU._ID = ACTION.TEMPS_DE_JEU_ID" +
                " AND ACTION._ID = TYPE.ACTION_ID" +
                " AND ACTION.JOUEUR_ACTEUR_ID = " +Integer.toString(joueurId),null);
        return cursorToInteger(c);
    }

    public int getNbFautes(int joueurId, int matchId) {
        Cursor c = mDb.rawQuery("SELECT COUNT(*)" +
                " FROM "+ DBFauteDAO.TABLE_NAME + " AS TYPE, ACTION, TEMPS_DE_JEU" +
                " WHERE TEMPS_DE_JEU.MATCH_ID = " +Integer.toString(matchId)+
                " AND TEMPS_DE_JEU._ID = ACTION.TEMPS_DE_JEU_ID" +
                " AND ACTION._ID = TYPE.ACTION_ID" +
                " AND ACTION.JOUEUR_ACTEUR_ID = " +Integer.toString(joueurId),null);
        return cursorToInteger(c);
    }

    public int getNbContre(int joueurId, int matchId) {
        Cursor c = mDb.rawQuery("SELECT COUNT(*)" +
                " FROM "+ DBContreDAO.TABLE_NAME + " AS TYPE, ACTION, TEMPS_DE_JEU" +
                " WHERE TEMPS_DE_JEU.MATCH_ID = " +Integer.toString(matchId)+
                " AND TEMPS_DE_JEU._ID = ACTION.TEMPS_DE_JEU_ID" +
                " AND ACTION._ID = TYPE.ACTION_ID" +
                " AND ACTION.JOUEUR_ACTEUR_ID = " +Integer.toString(joueurId),null);
        return cursorToInteger(c);
    }

    public int getNbRebondOff(int joueurId, int matchId) {
        Cursor c = mDb.rawQuery("SELECT COUNT(*)" +
                " FROM "+ DBRebondDAO.TABLE_NAME + " AS TYPE, ACTION, TEMPS_DE_JEU" +
                " WHERE TEMPS_DE_JEU.MATCH_ID = " +Integer.toString(matchId)+
                " AND TEMPS_DE_JEU._ID = ACTION.TEMPS_DE_JEU_ID" +
                " AND ACTION._ID = TYPE.ACTION_ID" +
                " AND TYPE.TYPE = " + Rebond.OFFENSIF +
                " AND ACTION.JOUEUR_ACTEUR_ID = " +Integer.toString(joueurId),null);
        return cursorToInteger(c);
    }

    public int getNbRebondDef(int joueurId, int matchId) {
        Cursor c = mDb.rawQuery("SELECT COUNT(*)" +
                " FROM "+ DBRebondDAO.TABLE_NAME + " AS TYPE, ACTION, TEMPS_DE_JEU" +
                " WHERE TEMPS_DE_JEU.MATCH_ID = " +Integer.toString(matchId)+
                " AND TEMPS_DE_JEU._ID = ACTION.TEMPS_DE_JEU_ID" +
                " AND ACTION._ID = TYPE.ACTION_ID" +
                " AND TYPE.TYPE = " + Rebond.DEFENSIF +
                " AND ACTION.JOUEUR_ACTEUR_ID = " +Integer.toString(joueurId),null);
        return cursorToInteger(c);
    }

    public int getNbInterception(int joueurId, int matchId) {
        Cursor c = mDb.rawQuery("SELECT COUNT(*)" +
                " FROM "+ DBInterceptionDAO.TABLE_NAME + " AS TYPE, ACTION, TEMPS_DE_JEU" +
                " WHERE TEMPS_DE_JEU.MATCH_ID = " +Integer.toString(matchId)+
                " AND TEMPS_DE_JEU._ID = ACTION.TEMPS_DE_JEU_ID" +
                " AND ACTION._ID = TYPE.ACTION_ID" +
                " AND ACTION.JOUEUR_ACTEUR_ID = " +Integer.toString(joueurId),null);
        return cursorToInteger(c);
    }

    public int getNbPerteBalle(int joueurId, int matchId) {
        Cursor c = mDb.rawQuery("SELECT COUNT(*)" +
                " FROM "+ DBPerteBalleDAO.TABLE_NAME + " AS TYPE, ACTION, TEMPS_DE_JEU" +
                " WHERE TEMPS_DE_JEU.MATCH_ID = " + Integer.toString(matchId)+
                " AND TEMPS_DE_JEU._ID = ACTION.TEMPS_DE_JEU_ID" +
                " AND ACTION._ID = TYPE.ACTION_ID" +
                " AND ACTION.JOUEUR_ACTEUR_ID = " + Integer.toString(joueurId),null);
        return cursorToInteger(c);
    }

    public int getNbLfS(int joueurId, int matchId) {
        Cursor c = mDb.rawQuery("SELECT COUNT(*)" +
                " FROM "+ DBShootDAO.TABLE_NAME + " AS TYPE, ACTION, TEMPS_DE_JEU" +
                " WHERE TEMPS_DE_JEU.MATCH_ID = " +Integer.toString(matchId)+
                " AND TEMPS_DE_JEU._ID = ACTION.TEMPS_DE_JEU_ID" +
                " AND ACTION._ID = TYPE.ACTION_ID" +
                " AND TYPE.PTS = 1" +
                " AND TYPE.REUSSI = "+ Shoot.REUSSI+
                " AND ACTION.JOUEUR_ACTEUR_ID = " +Integer.toString(joueurId),null);
        return cursorToInteger(c);
    }

    public int getNbLfF(int joueurId, int matchId) {
        Cursor c = mDb.rawQuery("SELECT COUNT(*)" +
                " FROM "+ DBShootDAO.TABLE_NAME + " AS TYPE, ACTION, TEMPS_DE_JEU" +
                " WHERE TEMPS_DE_JEU.MATCH_ID = " +Integer.toString(matchId)+
                " AND TEMPS_DE_JEU._ID = ACTION.TEMPS_DE_JEU_ID" +
                " AND ACTION._ID = TYPE.ACTION_ID" +
                " AND TYPE.PTS = 1" +
                " AND TYPE.REUSSI = "+ Shoot.RATE+
                " AND ACTION.JOUEUR_ACTEUR_ID = " +Integer.toString(joueurId),null);
        return cursorToInteger(c);
    }

    public int getNbShoot2S(int joueurId, int matchId) {
        Cursor c = mDb.rawQuery("SELECT COUNT(*)" +
                " FROM "+ DBShootDAO.TABLE_NAME + " AS TYPE, ACTION, TEMPS_DE_JEU" +
                " WHERE TEMPS_DE_JEU.MATCH_ID = " +Integer.toString(matchId)+
                " AND TEMPS_DE_JEU._ID = ACTION.TEMPS_DE_JEU_ID" +
                " AND ACTION._ID = TYPE.ACTION_ID" +
                " AND TYPE.PTS = 2" +
                " AND TYPE.REUSSI = "+ Shoot.REUSSI+
                " AND ACTION.JOUEUR_ACTEUR_ID = " +Integer.toString(joueurId),null);
        return cursorToInteger(c);
    }

    public int getNbShoot2F(int joueurId, int matchId) {
        Cursor c = mDb.rawQuery("SELECT COUNT(*)" +
                        " FROM "+ DBShootDAO.TABLE_NAME + " AS TYPE, ACTION, TEMPS_DE_JEU" +
                        " WHERE TEMPS_DE_JEU.MATCH_ID = " +Integer.toString(matchId)+
                        " AND TEMPS_DE_JEU._ID = ACTION.TEMPS_DE_JEU_ID" +
                        " AND ACTION._ID = TYPE.ACTION_ID" +
                        " AND TYPE.PTS = 2" +
                        " AND TYPE.REUSSI = " + Shoot.RATE+
                " AND ACTION.JOUEUR_ACTEUR_ID = " +Integer.toString(joueurId),null);
        return cursorToInteger(c);
    }

    public int getNbShoot3S(int joueurId, int matchId) {
        Cursor c = mDb.rawQuery("SELECT COUNT(*)" +
                        " FROM "+ DBShootDAO.TABLE_NAME + " AS TYPE, ACTION, TEMPS_DE_JEU" +
                        " WHERE TEMPS_DE_JEU.MATCH_ID = " +Integer.toString(matchId)+
                        " AND TEMPS_DE_JEU._ID = ACTION.TEMPS_DE_JEU_ID" +
                        " AND ACTION._ID = TYPE.ACTION_ID" +
                        " AND TYPE.PTS = 3" +
                        " AND TYPE.REUSSI = "+Shoot.REUSSI+
                " AND ACTION.JOUEUR_ACTEUR_ID = " +Integer.toString(joueurId),null);
        return cursorToInteger(c);
    }

    public int getNbShoot3F(int joueurId, int matchId) {
        Cursor c = mDb.rawQuery("SELECT COUNT(*)" +
                        " FROM "+ DBShootDAO.TABLE_NAME + " AS TYPE, ACTION, TEMPS_DE_JEU" +
                        " WHERE TEMPS_DE_JEU.MATCH_ID = " +Integer.toString(matchId)+
                        " AND TEMPS_DE_JEU._ID = ACTION.TEMPS_DE_JEU_ID" +
                        " AND ACTION._ID = TYPE.ACTION_ID" +
                        " AND TYPE.PTS = 3" +
                        " AND TYPE.REUSSI = "+Shoot.RATE+
                " AND ACTION.JOUEUR_ACTEUR_ID = " +Integer.toString(joueurId),null);
        return cursorToInteger(c);
    }

    // Liste les actions d'un match

    public Cursor getHistorique(int matchId) {
        Cursor c = mDb.rawQuery("SELECT ACTION._id, ACTION.joueur_acteur_id, TEMPS_DE_JEU.temps_restant " +
                "FROM" + DBTempsDeJeuDAO.TABLE_NAME+", "+DBMatchDAO.TABLE_NAME+", "+DBActionDAO.TABLE_NAME+
                "TEMPS_DE_JEU, MATCH, ACTION" +
                "WHERE" +
                "MATCH._id "+matchId+" AND "+
                "TEMPS_DE_JEU.match_id = MATCH._id AND " +
                "ACTION.temps_de_jeu_id = TEMPS_DE_JEU._id", null);
        return c;
    }

    public int cursorToInteger(Cursor c) {
        if (c.getCount() == 0) {
            return 0;
        }
        c.moveToFirst();
        int res = c.getInt(0);
        c.close();
        return res;
    }

}
