package com.enseirb.pfa.bastats.data.DAO.action;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.enseirb.pfa.bastats.data.DAO.BaseDAO;
import com.enseirb.pfa.bastats.data.DAO.DBJoueurDAO;
import com.enseirb.pfa.bastats.data.DAO.DBTempsDeJeuDAO;
import com.enseirb.pfa.bastats.data.model.action.Action;

import java.util.ArrayList;
import java.util.List;


public class DBActionDAO extends BaseDAO {

    public static final String TABLE_NAME                   = "ACTION";

    public static final String ID_FIELD_NAME               = "_ID";
    public static final String TEMPS_DE_JEU_FIELD_NAME     = "TEMPS_DE_JEU_ID";
    public static final String JOUEUR_ACTEUR_ID_FIELD_NAME = "JOUEUR_ACTEUR_ID";
    public static final String COMMENTAIRE_FIELD_NAME      = "COMMENTAIRE";
    public static final String TYPE_FIELD_NAME             = "TYPE";

    private static final String ID_FIELD_TYPE               = "INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String TEMPS_DE_JEU_FIELD_TYPE     = "INTEGER";
    private static final String JOUEUR_ACTEUR_ID_FIELD_TYPE = "INTEGER";
    private static final String COMMENTAIRE_FIELD_TYPE      = "TEXT";
    private static final String TYPE_FIELD_TYPE             = "INTEGER";

    private static final int NUM_COL_ID          = 0;
    private static final int NUM_COL_TDJ         = 1;
    private static final int NUM_COL_ACTEUR      = 2;
    private static final int NUM_COL_COMMENTAIRE = 3;
    private static final int NUM_COL_TYPE        = 4;

    public static final String CREATE_TABLE_STATEMENT = ID_FIELD_NAME + " " + ID_FIELD_TYPE
            + ", " + TEMPS_DE_JEU_FIELD_NAME     + " " + TEMPS_DE_JEU_FIELD_TYPE
            + ", " + JOUEUR_ACTEUR_ID_FIELD_NAME + " " + JOUEUR_ACTEUR_ID_FIELD_TYPE
            + ", " + COMMENTAIRE_FIELD_NAME      + " " + COMMENTAIRE_FIELD_TYPE
            + ", " + TYPE_FIELD_NAME             + " " + TYPE_FIELD_TYPE
            + ", " + "FOREIGN KEY (" + TEMPS_DE_JEU_FIELD_NAME+") "
            + "REFERENCES "+ DBTempsDeJeuDAO.TABLE_NAME+"(ID)"
            + ", " + "FOREIGN KEY (" + JOUEUR_ACTEUR_ID_FIELD_NAME+") "
            + "REFERENCES "+ DBJoueurDAO.TABLE_NAME+"(ID)";


    public DBActionDAO (Context context){
        super(context);
        this.mDb = this.open();
    }

    public long insert(Action action){
        ContentValues values = new ContentValues();

        values.put(TEMPS_DE_JEU_FIELD_NAME, action.getTempsDeJeu());
        values.put(JOUEUR_ACTEUR_ID_FIELD_NAME, action.getJoueurActeur());
        values.put(COMMENTAIRE_FIELD_NAME, action.getCommentaire());
        values.put(TYPE_FIELD_NAME, action.getType());

        return super.mDb.insert(TABLE_NAME, null, values);
    }

    public String ActionJSON(int id){
        StringBuilder str=new StringBuilder("\"action\" :{");
        Cursor c = super.mDb.query(TABLE_NAME, null,ID_FIELD_NAME +"=" + id, null, null, null, null);
        c.moveToFirst();
        int tpsId=c.getInt(NUM_COL_TDJ);
        DBTempsDeJeuDAO tps=new DBTempsDeJeuDAO(mCtx);
        String tpsJson=tps.TempsdeJeuJSON(tpsId);
        for(int i=2;i<c.getColumnCount();i++) {
            String columnName=c.getColumnName(i), columnValue=c.getString(i);
            str.append("\""+columnName+"\": "+"\""+columnValue+"\""+" ,\n");

        }
        str.append("},\n");
        str.append(tpsJson);
        return str.toString();
    }




    public int update(int id, Action action){
        ContentValues values = new ContentValues();

        values.put(TEMPS_DE_JEU_FIELD_NAME, action.getTempsDeJeu());
        values.put(JOUEUR_ACTEUR_ID_FIELD_NAME, action.getJoueurActeur());
        values.put(COMMENTAIRE_FIELD_NAME, action.getCommentaire());
        values.put(TYPE_FIELD_NAME, action.getType());

        return super.mDb.update(TABLE_NAME, values, ID_FIELD_NAME + " = " + id, null);
    }

    public int removeWithId(int id){
        return super.mDb.delete(TABLE_NAME, ID_FIELD_NAME + " = " +id, null);
    }

    public int removeWithId(String table,int id){
        Cursor c=super.mDb.query(table,new String[] {"ACTION_ID"},"_ID ="+id,null,null,null,null);
        c.moveToFirst();
        int actionId=c.getInt(0);
        Action action=getWithId(actionId);
        DBTempsDeJeuDAO tps=new DBTempsDeJeuDAO(mCtx);

        int i= super.mDb.delete(table, ID_FIELD_NAME + " = " +id, null);
        tps.removeWithId(action.getTempsDeJeu());
        //mDb.execSQL("DELETE " + " FROM ACTION "+ " WHERE ID = (select ACTION_ID FROM " + table+ " WHERE ID="+1+")");
        mDb.execSQL("DELETE " + " FROM ACTION "+ " WHERE _ID ="+actionId);
        return i;
    }

    public Action getWithId(int id){
        Cursor c = super.mDb.query(TABLE_NAME, null,
                ID_FIELD_NAME +"=" + id, null, null, null, null);
        return cursorToAction(c);
    }

    public List<Action> getActionsJoueur(int joueurId){
        Cursor c = super.mDb.query(TABLE_NAME, null,
                JOUEUR_ACTEUR_ID_FIELD_NAME +"=" + joueurId, null, null, null, null);
        return cursorToListAction(c);

    }

    public List<Action> getActionsMatch(int matchId){
        Cursor c = mDb.rawQuery("SELECT "+TABLE_NAME+".*" +
                " FROM "+ TABLE_NAME + " JOIN " + DBTempsDeJeuDAO.TABLE_NAME +
                " as TPS ON " + TABLE_NAME + "." + TEMPS_DE_JEU_FIELD_NAME + " = " +
                " TPS." + DBTempsDeJeuDAO.ID_FIELD_NAME + ") " +
                " WHERE TPS.MATCH_ID = " +Integer.toString(matchId), null);
        return cursorToListAction(c);
    }

    public List<Action> getAll(){
        Cursor c = super.mDb.query(TABLE_NAME, null , null, null, null, null, null);
        return cursorToListAction(c);
    }

    private List<Action> cursorToListAction(Cursor c){
        if (c.getCount() == 0)
            return null;

        List<Action> listeActions = new ArrayList<Action>();
        listeActions.clear();

        if (c.moveToFirst()) {
            do {
                Action action = new Action();
                action.setActionId(c.getInt(NUM_COL_ID));
                action.setJoueurActeur(c.getInt(NUM_COL_ACTEUR));
                action.setTempsDeJeu(c.getInt(NUM_COL_TDJ));
                action.setCommentaire(c.getString(NUM_COL_COMMENTAIRE));
                action.setType(c.getInt(NUM_COL_TYPE));
                listeActions.add(action);
            } while (c.moveToNext());
        }
        c.close();
        return listeActions;
    }

    private Action cursorToAction(Cursor c){
        if (c.getCount() == 0)
            return null;

        c.moveToFirst();
        Action action = new Action();
        action.setActionId(c.getInt(NUM_COL_ID));
        action.setJoueurActeur(c.getInt(NUM_COL_ACTEUR));
        action.setTempsDeJeu(c.getInt(NUM_COL_TDJ));
        action.setCommentaire(c.getString(NUM_COL_COMMENTAIRE));
        action.setType(c.getInt(NUM_COL_TYPE));
        c.close();

        return action;
    }

    public Action getLast(){
        Cursor c = mDb.query(TABLE_NAME,null, null, null, null, null, ID_FIELD_NAME+" DESC", null);
        c.moveToLast();
        return cursorToAction(c);
    }

    public boolean isAContre(int actionId) {
        Cursor c = mDb.query(DBContreDAO.TABLE_NAME,null,"ACTION_ID ="+actionId,null,null,null,null,null);
        if (c.getCount() == 0) {
            c.close();
            return false;
        }
        c.close();
        return true;
    }

    public boolean isAOffFaute(int actionId) {
        Cursor c = mDb.query(DBFauteDAO.TABLE_NAME,null,"ACTION_ID ="+actionId+" AND TYPE=0",null,null,null,null,null);
        if (c.getCount() == 0) {
            c.close();
            return false;
        }
        c.close();
        return true;
    }

    public boolean isADeffFaute(int actionId) {
        Cursor c = mDb.query(DBFauteDAO.TABLE_NAME,null,"ACTION_ID ="+actionId+" AND TYPE=1",null,null,null,null,null);
        if (c.getCount() == 0) {
            c.close();
            return false;
        }
        c.close();
        return true;
    }

    public boolean isAInterception(int actionId) {
        Cursor c = mDb.query(DBInterceptionDAO.TABLE_NAME,null,"ACTION_ID ="+actionId,null,null,null,null,null);
        if (c.getCount() == 0) {
            c.close();
            return false;
        }
        c.close();
        return true;
    }

    public boolean isAPasse(int actionId) {
        Cursor c = mDb.query(DBPasseDAO.TABLE_NAME,null,"ACTION_ID ="+actionId,null,null,null,null,null);
        if (c.getCount() == 0) {
            c.close();
            return false;
        }
        c.close();
        return true;
    }

    public boolean isAPerteBalle(int actionId) {
        Cursor c = mDb.query(DBPerteBalleDAO.TABLE_NAME,null,"ACTION_ID ="+actionId,null,null,null,null,null);
        if (c.getCount() == 0) {
            c.close();
            return false;
        }
        c.close();
        return true;
    }

    public boolean isAOffRebond(int actionId) {
        Cursor c = mDb.query(DBRebondDAO.TABLE_NAME,null,"ACTION_ID ="+actionId+" AND TYPE=0",null,null,null,null,null);
        if (c.getCount() == 0){
            c.close();
            return false;
        }
        c.close();
        return true;
    }

    public boolean isADeffRebond(int actionId) {
        Cursor c = mDb.query(DBRebondDAO.TABLE_NAME,null,"ACTION_ID ="+actionId+" AND TYPE=1",null,null,null,null,null);
        if (c.getCount() == 0){
            c.close();
            return false;
        }
        c.close();
        return true;
    }

    public boolean isAShoot(int actionId) {
        Cursor c = mDb.query(DBShootDAO.TABLE_NAME,null,"ACTION_ID ="+actionId,null,null,null,null,null);
        if (c.getCount() == 0) {
            c.close();
            return false;
        }
        c.close();
        return true;
    }

    public boolean isAManque1PT(int actionId) {
        Cursor c = mDb.query(DBShootDAO.TABLE_NAME,null,"ACTION_ID ="+actionId+" AND TYPE=1 AND REUSSI=0",null,null,null,null,null);
        if (c.getCount() == 0) {
            c.close();
            return false;
        }
        c.close();
        return true;
    }

    public boolean isAReussi1PT(int actionId) {
        Cursor c = mDb.query(DBShootDAO.TABLE_NAME,null,"ACTION_ID ="+actionId+" AND TYPE=1 AND REUSSI=1",null,null,null,null,null);
        if (c.getCount() == 0) {
            c.close();
            return false;
        }
        c.close();
        return true;
    }

    public boolean isAManque2PTS(int actionId) {
        Cursor c = mDb.query(DBShootDAO.TABLE_NAME,null,"ACTION_ID ="+actionId+" AND TYPE=2 AND REUSSI=0",null,null,null,null,null);
        if (c.getCount() == 0) {
            c.close();
            return false;
        }
        c.close();
        return true;
    }

    public boolean isAReussi2PTS(int actionId) {
        Cursor c = mDb.query(DBShootDAO.TABLE_NAME,null,"ACTION_ID ="+actionId+" AND TYPE=2 AND REUSSI=1",null,null,null,null,null);
        if (c.getCount() == 0) {
            c.close();
            return false;
        }
        c.close();
        return true;
    }

    public boolean isAManque3PTS(int actionId) {
        Cursor c = mDb.query(DBShootDAO.TABLE_NAME,null,"ACTION_ID ="+actionId+" AND TYPE=3 AND REUSSI=0",null,null,null,null,null);
        if (c.getCount() == 0) {
            c.close();
            return false;
        }
        c.close();
        return true;
    }

    public boolean isAReussi3PTS(int actionId) {
        Cursor c = mDb.query(DBShootDAO.TABLE_NAME,null,"ACTION_ID ="+actionId+" AND TYPE=3 AND REUSSI=1",null,null,null,null,null);
        if (c.getCount() == 0) {
            c.close();
            return false;
        }
        c.close();
        return true;
    }

    public List<Action> getActionsMatch(int matchId){
        Cursor c = mDb.rawQuery("SELECT "+TABLE_NAME+".*" +
                " FROM ("+ TABLE_NAME + " JOIN " + DBTempsDeJeuDAO.TABLE_NAME +
                " as TPS ON " + TABLE_NAME + "." + TEMPS_DE_JEU_FIELD_NAME + " = " +
                " TPS." + DBTempsDeJeuDAO.ID_FIELD_NAME + ") " +
                " WHERE TPS.MATCH_ID = " +Integer.toString(matchId), null);
        return cursorToListAction(c);
    }
}
