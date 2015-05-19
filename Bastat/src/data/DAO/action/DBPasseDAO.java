package com.enseirb.pfa.bastats.data.DAO.action;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.enseirb.pfa.bastats.data.DAO.BaseDAO;
import com.enseirb.pfa.bastats.data.DAO.DBJoueurDAO;
import com.enseirb.pfa.bastats.data.DAO.DBTempsDeJeuDAO;
import com.enseirb.pfa.bastats.data.model.Joueur;
import com.enseirb.pfa.bastats.data.model.action.Passe;

import java.util.ArrayList;
import java.util.List;

public class DBPasseDAO extends BaseDAO {

    public static final String TABLE_NAME = "PASSE_DECISIVE";

    private static final String ID_FIELD_NAME               = "_ID";
    private static final String ACTION_ID_FIELD_NAME        = "ACTION_ID";
    private static final String JOUEUR_CIBLE_ID_FIELD_NAME  = "JOUEUR_CIBLE_ID";

    private static final String ID_FIELD_TYPE               = "INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String ACTION_ID_FIELD_TYPE        = "INTEGER";
    private static final String JOUEUR_CIBLE_ID_FIELD_TYPE  = "INTEGER";


    private static final int NUM_COL_ID                     = 0;
    private static final int NUM_COL_ACTION                 = 2;
    private static final int NUM_COL_JOUEUR_CIBLE_ID        = 1;


    public static final String CREATE_TABLE_STATEMENT = ID_FIELD_NAME + " " + ID_FIELD_TYPE
            + ", " + JOUEUR_CIBLE_ID_FIELD_NAME + " " + JOUEUR_CIBLE_ID_FIELD_TYPE
            + ", " + ACTION_ID_FIELD_NAME+ " " + ACTION_ID_FIELD_TYPE
            + ", " + "FOREIGN KEY (" + JOUEUR_CIBLE_ID_FIELD_NAME+") "
            + "REFERENCES "+ DBJoueurDAO.TABLE_NAME+"(ID)"
            + ", " + "FOREIGN KEY (" + ACTION_ID_FIELD_NAME +") "
            + "REFERENCES "+ DBActionDAO.TABLE_NAME+"(ID) ";

    public DBPasseDAO(Context context) {
        super(context);
        this.mDb = this.open();
    }


    public String PassesJSON(int matchId,List<Joueur> joueurs){
        StringBuilder str=new StringBuilder();


        for(Joueur j: joueurs) {
            List<Passe> passes=getAll(j,matchId);
            if(passes!=null) {
                for (Passe passe : passes) {
                    String passeJson = PasseJSON(passe.getId());

                    str.append(passeJson);

                }
            }
        }

        return str.toString();
    }

    public List<Passe> getAll(Joueur j,int matchId){
        int joueurId=j.getId();

        //select shoot.* from action, tempsdejeu,shoot where joueur_acteur_id=joueurid and tempsdejeu.match_id=matchId and action.tdjid=tempsdejeu.id and shoot.actionid=action.id
        Cursor c = super.mDb.rawQuery("SELECT "+TABLE_NAME+".*"+
                        " FROM "+TABLE_NAME+" , "+DBActionDAO.TABLE_NAME+" , "+DBTempsDeJeuDAO.TABLE_NAME+
                        " WHERE " +DBActionDAO.TABLE_NAME+"."+DBActionDAO.JOUEUR_ACTEUR_ID_FIELD_NAME +"="+joueurId
                        +" AND "+DBTempsDeJeuDAO.TABLE_NAME+"."+DBTempsDeJeuDAO.MATCH_ID_FIELD_NAME+"="+matchId
                        +" AND "+DBTempsDeJeuDAO.TABLE_NAME+"."+DBTempsDeJeuDAO.ID_FIELD_NAME+"="+DBActionDAO.TABLE_NAME+"."+DBActionDAO.TEMPS_DE_JEU_FIELD_NAME
                        +" AND "+DBPasseDAO.TABLE_NAME+"."+DBPasseDAO.ACTION_ID_FIELD_NAME+"="+DBActionDAO.TABLE_NAME+"."+DBActionDAO.ID_FIELD_NAME
                ,
                null);
        return cursorToListPasse(c);

    }



    public String PasseJSON(int id){
        StringBuilder str=new StringBuilder("{\"TypeAction\" :{");
        Cursor c = super.mDb.query(TABLE_NAME, null,ID_FIELD_NAME +"=" + id, null, null, null, null);
        c.moveToFirst();
        int actionId=c.getInt(NUM_COL_ACTION);
        DBActionDAO dba=new DBActionDAO(mCtx);

        String actionJSON=dba.ActionJSON(actionId);
        str.append("\""+"Type:"+"\": "+"\""+"PASSE_DECISIVE"+"\""+" ,\n");

        for(int i=2;i<c.getColumnCount();i++) {
            String columnName=c.getColumnName(i), columnValue=c.getString(i);
            str.append("\""+columnName+"\": "+"\""+columnValue+"\""+" ,\n");

        }
        str.append("},\n");
        str.append(actionJSON);
        return str.toString();
    }









    public long insert(Passe passe){
        ContentValues values = new ContentValues();
        DBActionDAO dba=new DBActionDAO(mCtx);
        dba.insert(passe);

        values.put(ACTION_ID_FIELD_NAME,dba.getLast().getActionId());
        values.put(JOUEUR_CIBLE_ID_FIELD_NAME,passe.getJoueurCible());
        return  mDb.insert(TABLE_NAME, null, values);
    }

    public long update(int id, Passe passe){
        ContentValues values = new ContentValues();
        values.put(JOUEUR_CIBLE_ID_FIELD_NAME,passe.getJoueurCible());


        return mDb.update(TABLE_NAME, values, ID_FIELD_NAME + " = " + id, null);
    }


    public int removeWithId(int id){
        int actionId=getWithId(id).getActionId();
        mDb.delete("ACTION","ID="+actionId,null);
        return mDb.delete(TABLE_NAME, ID_FIELD_NAME + " = " +id, null);
    }


    public Passe getWithId(int id){
        Cursor c = super.mDb.query(TABLE_NAME, null,
                ID_FIELD_NAME +"=" + id, null, null, null, null);
        return cursorToPasse(c);
    }


    public List<Passe> getAll(){
        Cursor c = super.mDb.query(TABLE_NAME, null,
                null, null, null, null, null);
        return cursorToListPasse(c);
    }


    private List<Passe> cursorToListPasse(Cursor c){
        if (c.getCount() == 0)
            return null;

        List<Passe> liste = new ArrayList<Passe>();
        liste.clear();

        if (c.moveToFirst()) {
            do {
                Passe passe = new Passe();
                passe.setId(c.getInt(NUM_COL_ID));
                passe.setActionId(c.getInt(NUM_COL_ACTION));
                passe.setJoueurCible(c.getInt(NUM_COL_JOUEUR_CIBLE_ID));
                liste.add(passe);
            } while (c.moveToNext());
        }
        c.close();
        return liste;
    }


    private Passe cursorToPasse(Cursor c){
        if (c.getCount() == 0)
            return null;

        c.moveToFirst();
        Passe passe = new Passe();

        passe.setId(c.getInt(NUM_COL_ID));
        passe.setActionId(c.getInt(NUM_COL_ACTION));
        passe.setJoueurCible(c.getInt(NUM_COL_JOUEUR_CIBLE_ID));

        c.close();

        return passe;
    }

    public Passe getLast(){
        Cursor c = mDb.query(TABLE_NAME,null, null, null, null, null, ID_FIELD_NAME+" DESC", null);
        c.moveToLast();
        return cursorToPasse(c);
    }







}
