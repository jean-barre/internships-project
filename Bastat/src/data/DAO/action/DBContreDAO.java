package com.enseirb.pfa.bastats.data.DAO.action;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.enseirb.pfa.bastats.data.DAO.BaseDAO;
import com.enseirb.pfa.bastats.data.DAO.DBJoueurDAO;
import com.enseirb.pfa.bastats.data.DAO.DBTempsDeJeuDAO;
import com.enseirb.pfa.bastats.data.model.Joueur;
import com.enseirb.pfa.bastats.data.model.action.Contre;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rchabot on 25/01/15.
 */
public class DBContreDAO extends BaseDAO {
    public static final String TABLE_NAME = "CONTRE";

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

    public DBContreDAO(Context context) {
        super(context);
        this.mDb = this.open();
    }




    public String ContresJSON(int matchId,List<Joueur> joueurs){
        StringBuilder str=new StringBuilder();


        for(Joueur j: joueurs) {
            List<Contre> contres=getAll(j,matchId);
            if(contres!=null) {
                for (Contre contre : contres) {
                    String contreJson = ContreJSON(contre.getId());

                    str.append(contreJson);

                }
            }
        }

        return str.toString();
    }

    public List<Contre> getAll(Joueur j,int matchId){
        int joueurId=j.getId();

        //select shoot.* from action, tempsdejeu,shoot where joueur_acteur_id=joueurid and tempsdejeu.match_id=matchId and action.tdjid=tempsdejeu.id and shoot.actionid=action.id
        Cursor c = super.mDb.rawQuery("SELECT "+TABLE_NAME+".*"+
                        " FROM "+TABLE_NAME+" , "+DBActionDAO.TABLE_NAME+" , "+DBTempsDeJeuDAO.TABLE_NAME+
                        " WHERE " +DBActionDAO.TABLE_NAME+"."+DBActionDAO.JOUEUR_ACTEUR_ID_FIELD_NAME +"="+joueurId
                        +" AND "+DBTempsDeJeuDAO.TABLE_NAME+"."+DBTempsDeJeuDAO.MATCH_ID_FIELD_NAME+"="+matchId
                        +" AND "+DBTempsDeJeuDAO.TABLE_NAME+"."+DBTempsDeJeuDAO.ID_FIELD_NAME+"="+DBActionDAO.TABLE_NAME+"."+DBActionDAO.TEMPS_DE_JEU_FIELD_NAME
                        +" AND "+DBContreDAO.TABLE_NAME+"."+DBContreDAO.ACTION_ID_FIELD_NAME+"="+DBActionDAO.TABLE_NAME+"."+DBActionDAO.ID_FIELD_NAME
                ,
                null);
        return cursorToListContre(c);

    }





    public String ContreJSON(int id){
        StringBuilder str=new StringBuilder("{\"TypeAction\" :{");
        Cursor c = super.mDb.query(TABLE_NAME, null,ID_FIELD_NAME +"=" + id, null, null, null, null);
        c.moveToFirst();
        int actionId=c.getInt(NUM_COL_ACTION);
        DBActionDAO dba=new DBActionDAO(mCtx);

        String actionJSON=dba.ActionJSON(actionId);
        str.append("\""+"Type:"+"\": "+"\""+"CONTRE"+"\""+" ,\n");

        for(int i=2;i<c.getColumnCount();i++) {
            String columnName=c.getColumnName(i), columnValue=c.getString(i);
            str.append("\""+columnName+"\": "+"\""+columnValue+"\""+" ,\n");

        }
        str.append("},\n");
        str.append(actionJSON);
        return str.toString();
    }






    public long insert(Contre contre){
        ContentValues values = new ContentValues();
        DBActionDAO dba=new DBActionDAO(mCtx);
        dba.insert(contre);

        values.put(ACTION_ID_FIELD_NAME,dba.getLast().getActionId());
        values.put(JOUEUR_CIBLE_ID_FIELD_NAME,contre.getJoueurCible());
        return  mDb.insert(TABLE_NAME, null, values);
    }

    public long update(int id, Contre contre){
        ContentValues values = new ContentValues();
        values.put(JOUEUR_CIBLE_ID_FIELD_NAME,contre.getJoueurCible());


        return mDb.update(TABLE_NAME, values, ID_FIELD_NAME + " = " + id, null);
    }


    public int removeWithId(int id){
        int actionId=getWithId(id).getActionId();
        mDb.delete("ACTION","ID="+actionId,null);
        return mDb.delete(TABLE_NAME, ID_FIELD_NAME + " = " +id, null);
    }


    public Contre getWithId(int id){
        Cursor c = super.mDb.query(TABLE_NAME, null,
                ID_FIELD_NAME +"=" + id, null, null, null, null);
        return cursorToContre(c);
    }


    public List<Contre> getAll(){
        Cursor c = super.mDb.query(TABLE_NAME, null,
                null, null, null, null, null);
        return cursorToListContre(c);
    }


    private List<Contre> cursorToListContre(Cursor c){
        if (c.getCount() == 0)
            return null;

        List<Contre> liste = new ArrayList<Contre>();
        liste.clear();

        if (c.moveToFirst()) {
            do {
                Contre contre = new Contre();
                contre.setId(c.getInt(NUM_COL_ID));
                contre.setActionId(c.getInt(NUM_COL_ACTION));
                contre.setJoueurCible(c.getInt(NUM_COL_JOUEUR_CIBLE_ID));
                liste.add(contre);
            } while (c.moveToNext());
        }
        c.close();
        return liste;
    }


    private Contre cursorToContre(Cursor c){
        if (c.getCount() == 0)
            return null;

        c.moveToFirst();
        Contre contre = new Contre();

        contre.setId(c.getInt(NUM_COL_ID));
        contre.setActionId(c.getInt(NUM_COL_ACTION));
        contre.setJoueurCible(c.getInt(NUM_COL_JOUEUR_CIBLE_ID));

        c.close();

        return contre;
    }

    public Contre getLast(){
    Cursor c = mDb.query(TABLE_NAME,null, null, null, null, null, ID_FIELD_NAME+" DESC", null);
    c.moveToLast();
    return cursorToContre(c);
}

}
