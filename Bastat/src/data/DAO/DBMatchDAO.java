package com.enseirb.pfa.bastats.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.enseirb.pfa.bastats.data.DAO.action.DBContreDAO;
import com.enseirb.pfa.bastats.data.DAO.action.DBFauteDAO;
import com.enseirb.pfa.bastats.data.DAO.action.DBInterceptionDAO;
import com.enseirb.pfa.bastats.data.DAO.action.DBPasseDAO;
import com.enseirb.pfa.bastats.data.DAO.action.DBPerteBalleDAO;
import com.enseirb.pfa.bastats.data.DAO.action.DBRebondDAO;
import com.enseirb.pfa.bastats.data.DAO.action.DBShootDAO;
import com.enseirb.pfa.bastats.data.model.Formation;
import com.enseirb.pfa.bastats.data.model.Joueur;
import com.enseirb.pfa.bastats.data.model.Match;
import com.enseirb.pfa.bastats.data.model.PhasePoule;
import com.enseirb.pfa.bastats.data.model.action.Action;
import com.enseirb.pfa.bastats.debug.ListeJoueurs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dohko on 14/02/15.
 */
public class DBMatchDAO extends BaseDAO {

    public static final String TABLE_NAME = "MATCH";

    private static final String ID_FIELD_NAME      = "_ID";
    private static final String LIBELLE_FIELD_NAME = "LIBELLE";
    private static final String DATE_FIELD_NAME    = "DATE";
    private static final String FORMATION1_ID_FIELD_NAME= "FORMATION1_ID";
    private static final String FORMATION2_ID_FIELD_NAME= "FORMATION2_ID";
    private static final String REGLE_FORMATION1_FIELD_NAME= "REGLE_FORMATION1";
    private static final String REGLE_FORMATION2_FIELD_NAME= "REGLE_FORMATION2";
    private static final String RESULTAT_FIELD_NAME        = "RESULTAT";
    private static final String SCORE_EQUIPE_1_FIELD_NAME= "SCORE1";
    private static final String SCORE_EQUIPE_2_FIELD_NAME= "SCORE2";
    private static final String PHASE_ID_FIELD_NAME ="PHASE_ID";
    private static final String ARBITRE1_FIELD_NAME = "ARBITRE1";
    private static final String ARBITRE2_FIELD_NAME = "ARBITRE2";

    private static final String ID_FIELD_TYPE = "INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String LIBELLE_FIELD_TYPE="TEXT";
    private static final String DATE_FIELD_TYPE="TEXT";
    private static final String FORMATION1_ID_FIELD_TYPE="INTEGER";
    private static final String FORMATION2_ID_FIELD_TYPE="INTEGER";
    private static final String REGLE_FORMATION1_FIELD_TYPE="TEXT";
    private static final String REGLE_FORMATION2_FIELD_TYPE="TEXT";
    private static final String RESULTAT_FIELD_TYPE="INTEGER";
    private static final String SCORE_EQUIPE_1_FIELD_TYPE="INTEGER";
    private static final String SCORE_EQUIPE_2_FIELD_TYPE="INTEGER";
    private static final String PHASE_ID_FIELD_TYPE="INTEGER";
    private static final String ARBITRE1_FIELD_TYPE="TEXT";
    private static final String ARBITRE2_FIELD_TYPE="TEXT";

    private static final int NUM_COL_ID = 0;
    private static final int NUM_COL_LIBELLE = 1;
    private static final int NUM_COL_DATE = 2;
    private static final int NUM_COL_FORMATION1 = 3;
    private static final int NUM_COL_FORMATION2 = 4;
    private static final int NUM_COL_REGLE_FORMATION1 = 5;
    private static final int NUM_COL_REGLE_FORMATION2 = 6;
    private static final int NUM_COL_RESULTAT = 7;
    private static final int NUM_COL_SCORE_EQUIPE_1 = 8;
    private static final int NUM_COL_SCORE_EQUIPE_2 = 9;
    private static final int NUM_COL_PHASE_ID = 10;
    private static final int NUM_COL_ARBITRE1 = 11;
    private static final int NUM_COL_ARBITRE2 = 12;

    public static final String CREATE_TABLE_STATEMENT = ID_FIELD_NAME + " " + ID_FIELD_TYPE
            + ", " + LIBELLE_FIELD_NAME + " " + LIBELLE_FIELD_TYPE
            + ", " + DATE_FIELD_NAME + " " + DATE_FIELD_TYPE
            + ", " + FORMATION1_ID_FIELD_NAME + " " + FORMATION1_ID_FIELD_TYPE
            + ", " + FORMATION2_ID_FIELD_NAME + " " + FORMATION2_ID_FIELD_TYPE
            + ", " + REGLE_FORMATION1_FIELD_NAME + " " + REGLE_FORMATION1_FIELD_TYPE
            + ", " + REGLE_FORMATION2_FIELD_NAME + " " + REGLE_FORMATION2_FIELD_TYPE
            + ", " + RESULTAT_FIELD_NAME + " " + RESULTAT_FIELD_TYPE
            + ", " + SCORE_EQUIPE_1_FIELD_NAME + " " + SCORE_EQUIPE_1_FIELD_TYPE
            + ", " + SCORE_EQUIPE_2_FIELD_NAME + " " + SCORE_EQUIPE_2_FIELD_TYPE
            + ", " + PHASE_ID_FIELD_NAME + " " + PHASE_ID_FIELD_TYPE
            + ", " + ARBITRE1_FIELD_NAME + " " + ARBITRE1_FIELD_TYPE
            + ", " + ARBITRE2_FIELD_NAME + " " + ARBITRE2_FIELD_TYPE
            + ", " + "FOREIGN KEY (" + FORMATION1_ID_FIELD_NAME + ") "
            + "REFERENCES " + DBFormationDAO.TABLE_NAME + "(ID)"
            + ", " + "FOREIGN KEY (" + FORMATION2_ID_FIELD_NAME + ") "
            + "REFERENCES " + DBFormationDAO.TABLE_NAME + "(ID)"
            + ", " + "FOREIGN KEY (" + PHASE_ID_FIELD_NAME + ") "
            + "REFERENCES " + DBPhaseDAO.TABLE_NAME + "(ID)";

    public DBMatchDAO(Context context) {
        super(context);
        super.mDb = this.open();
    }

    public long insert(Match match){
        ContentValues values = new ContentValues();

        values.put(LIBELLE_FIELD_NAME,match.getLibelle());
        values.put(DATE_FIELD_NAME,match.getDate());
        values.put(FORMATION1_ID_FIELD_NAME,match.getFormationEquipeA());
        values.put(FORMATION2_ID_FIELD_NAME,match.getFormationEquipeB());
        values.put(REGLE_FORMATION1_FIELD_NAME,match.getRegleFormationA());
        values.put(REGLE_FORMATION2_FIELD_NAME,match.getRegleFormationB());
        values.put(RESULTAT_FIELD_NAME,match.getResultat());
        values.put(SCORE_EQUIPE_1_FIELD_NAME,match.getScoreEquipeA());
        values.put(SCORE_EQUIPE_2_FIELD_NAME,match.getScoreEquipeB());
        values.put(PHASE_ID_FIELD_NAME,match.getPhaseId());
        values.put(ARBITRE1_FIELD_NAME, match.getArbitreChamp());
        values.put(ARBITRE2_FIELD_NAME, match.getArbitreAssistant());


        return super.mDb.insert(TABLE_NAME, null, values);
    }

    public int update(int id, Match match){
        ContentValues values = new ContentValues();
        values.put(LIBELLE_FIELD_NAME,match.getLibelle());
        values.put(DATE_FIELD_NAME,match.getDate());
        values.put(FORMATION1_ID_FIELD_NAME,match.getFormationEquipeA());
        values.put(FORMATION2_ID_FIELD_NAME,match.getFormationEquipeB());
        values.put(REGLE_FORMATION1_FIELD_NAME,match.getRegleFormationA());
        values.put(REGLE_FORMATION2_FIELD_NAME,match.getRegleFormationB());
        values.put(RESULTAT_FIELD_NAME,match.getResultat());
        values.put(SCORE_EQUIPE_1_FIELD_NAME,match.getScoreEquipeA());
        values.put(SCORE_EQUIPE_2_FIELD_NAME,match.getScoreEquipeB());
        values.put(PHASE_ID_FIELD_NAME,match.getPhaseId());
        values.put(ARBITRE1_FIELD_NAME, match.getArbitreChamp());
        values.put(ARBITRE2_FIELD_NAME, match.getArbitreAssistant());

        return super.mDb.update(TABLE_NAME, values, ID_FIELD_NAME + " = " + id, null);
    }

    public String MatchJSON(int id,List<Joueur> joueursA,List<Joueur> joueursB,int formationA,int formationB){
        StringBuilder str=new StringBuilder("{\n \"match\":{\n");
        Cursor c = super.mDb.query(TABLE_NAME, null,ID_FIELD_NAME +"=" + id, null, null, null, null);
        c.moveToFirst();
        for(int i=0;i<c.getColumnCount();i++) {
          String columnName=c.getColumnName(i), columnValue=c.getString(i);
            str.append("\""+columnName+"\": "+"\""+columnValue+"\""+" ,\n");

        }
            str.append("\"equipeA\":\n{\n\"actions\": [\n");


        DBShootDAO dbs       =new DBShootDAO(mCtx);
        DBRebondDAO dbr      =new DBRebondDAO(mCtx);
        DBPerteBalleDAO dbpe =new DBPerteBalleDAO(mCtx);
        DBPasseDAO dbp       =new DBPasseDAO(mCtx);
        DBInterceptionDAO dbi=new DBInterceptionDAO(mCtx);
        DBFauteDAO        dbf =new DBFauteDAO(mCtx);
        DBContreDAO       dbc =new DBContreDAO(mCtx);
        DBFormationJoueurDAO dbfj =new DBFormationJoueurDAO(mCtx);

        str.append(dbs.ShootsJSON(id,joueursA));
        str.append(dbr.RebondsJSON(id, joueursA));
        str.append(dbpe.PerteBalleJSON(id, joueursA));
        str.append(dbp.PassesJSON(id, joueursA));
        str.append(dbi.InterceptionsJSON(id, joueursA));
        str.append(dbf.FautesJSON(id, joueursA));
        str.append(dbc.ContresJSON(id,joueursA));

        str.append("]\n\"formation\":[");
        str.append(dbfj.FormationsJoueurJSON(formationA,joueursA));

        str.append("]\n} ,\n \"equipeB\":\n{\"actions\":[\n");

        str.append(dbs.ShootsJSON(id,joueursB));
        str.append(dbr.RebondsJSON(id,joueursB));
        str.append(dbpe.PerteBalleJSON(id,joueursB));
        str.append(dbp.PassesJSON(id,joueursA));
        str.append(dbi.InterceptionsJSON(id,joueursB));
        str.append(dbf.FautesJSON(id,joueursB));
        str.append(dbc.ContresJSON(id,joueursB));

        str.append("]\n\"formation\":[");
        str.append(dbfj.FormationsJoueurJSON(formationB,joueursB));

        str.append("]\n}\n}\n}");


        return str.toString();
    }

    public int removeWithId(int id){
        return super.mDb.delete(TABLE_NAME, ID_FIELD_NAME + " = " +id, null);
    }

    public Match getWithId(int id){
        Cursor c = super.mDb.query(TABLE_NAME, null,ID_FIELD_NAME +"=" + id, null, null, null, null);
        return cursorToMatch(c);
    }

    public List<Match> getAll(){
        Cursor c = super.mDb.query(TABLE_NAME, null, null, null, null, null, null);
        return cursorToListMatch(c);
    }

    public Match getLast(){
        Cursor c = mDb.query(TABLE_NAME,null, null, null, null, null, ID_FIELD_NAME+" DESC", null);
        c.moveToLast();
        return cursorToMatch(c);
    }

    public List<Match> getMatchPoule(int pouleId){
        Cursor c = super.mDb.rawQuery("SELECT "+TABLE_NAME+".*"+
                " FROM "+TABLE_NAME+" , "+DBPouleMatchDAO.TABLE_NAME+
                " WHERE "+DBPouleMatchDAO.TABLE_NAME+"."+DBPouleMatchDAO.POULE_ID_FIELD_NAME+"="+pouleId
                +" AND "+DBPouleMatchDAO.TABLE_NAME+"."+DBPouleMatchDAO.MATCH_ID_FIELD_NAME+"="+ID_FIELD_NAME,
                null);
        return cursorToListMatch(c);
    }

    public List<Match> getMatchPouleDeEquipe(int pouleId, int equipeId){
        String pm = DBPouleMatchDAO.TABLE_NAME;
        String formation = DBFormationDAO.TABLE_NAME;
        String rawQuery = " SELECT "+TABLE_NAME+".*"
                +" FROM "+TABLE_NAME+" , "+pm
                +" WHERE "+TABLE_NAME+"."+ID_FIELD_NAME+"="+pm+"."+DBPouleMatchDAO.MATCH_ID_FIELD_NAME
                +" AND "+pm+"."+DBPouleMatchDAO.POULE_ID_FIELD_NAME+"="+pouleId
                +" AND ("+TABLE_NAME+"."+FORMATION1_ID_FIELD_NAME+" IN "
                    +" ( SELECT "+formation+"."+DBFormationDAO.ID_FIELD_NAME
                    +" FROM "+formation
                    +" WHERE "+formation+"."+DBFormationDAO.EQUIPE_ID_FIELD_NAME+"="+equipeId+")"
                    +" OR "
                    +TABLE_NAME+"."+FORMATION2_ID_FIELD_NAME+" IN "
                    +" ( SELECT "+formation+"."+DBFormationDAO.ID_FIELD_NAME
                    +" FROM "+formation
                    +" WHERE "+formation+"."+DBFormationDAO.EQUIPE_ID_FIELD_NAME+"="+equipeId+"))";
        Log.d("BDD Match", "Cherche match de l'équipe "+equipeId+" dans la poule "+pouleId);

        Cursor c = super.mDb.rawQuery(rawQuery,
                null);
        return cursorToListMatch(c);
    }

    public List<Match> getFromPhaseType(int phaseId){
        Cursor c = super.mDb.query(TABLE_NAME, null,
                PHASE_ID_FIELD_NAME + "=" + phaseId, null, null, null, null);
        return cursorToListMatch(c);
    }

    private List<Match> cursorToListMatch(Cursor c){
        if (c.getCount() == 0)
            return null;

        List<Match> listeMatchs = new ArrayList<Match>();
        listeMatchs.clear();

        if (c.moveToFirst()) {
            do {
                Match match = new Match();
                match.setId(c.getInt(NUM_COL_ID));
                match.setLibelle(c.getString(NUM_COL_LIBELLE));
                match.setDate(c.getString(NUM_COL_DATE));
                match.setFormationEquipeA(c.getInt(NUM_COL_FORMATION1));
                match.setFormationEquipeB(c.getInt(NUM_COL_FORMATION2));
                match.setRegleFormationA(c.getString(NUM_COL_REGLE_FORMATION1));
                match.setRegleFormationB(c.getString(NUM_COL_REGLE_FORMATION2));
                match.setResultat(c.getInt(NUM_COL_RESULTAT));
                match.setScoreEquipeA(c.getInt(NUM_COL_SCORE_EQUIPE_1));
                match.setScoreEquipeB(c.getInt(NUM_COL_SCORE_EQUIPE_2));
                match.setPhaseId(c.getInt(NUM_COL_PHASE_ID));
                match.setArbitreChamp(c.getString(NUM_COL_ARBITRE1));
                match.setArbitreAssistant(c.getString(NUM_COL_ARBITRE2));
                listeMatchs.add(match);
            } while (c.moveToNext());
        }
        c.close();
        return listeMatchs;
    }

    private Match cursorToMatch(Cursor c){
        if (c.getCount() == 0)
            return null;

        c.moveToFirst();
        Match match = new Match();
        match.setId(c.getInt(NUM_COL_ID));
        match.setLibelle(c.getString(NUM_COL_LIBELLE));
        match.setDate(c.getString(NUM_COL_DATE));
        match.setFormationEquipeA(c.getInt(NUM_COL_FORMATION1));
        match.setFormationEquipeB(c.getInt(NUM_COL_FORMATION2));
        match.setRegleFormationA(c.getString(NUM_COL_REGLE_FORMATION1));
        match.setRegleFormationB(c.getString(NUM_COL_REGLE_FORMATION2));
        match.setResultat(c.getInt(NUM_COL_RESULTAT));
        match.setScoreEquipeA(c.getInt(NUM_COL_SCORE_EQUIPE_1));
        match.setScoreEquipeB(c.getInt(NUM_COL_SCORE_EQUIPE_2));
        match.setPhaseId(c.getInt(NUM_COL_PHASE_ID));
        match.setArbitreChamp(c.getString(NUM_COL_ARBITRE1));
        match.setArbitreAssistant(c.getString(NUM_COL_ARBITRE2));

        c.close();

        return match;
    }

}
