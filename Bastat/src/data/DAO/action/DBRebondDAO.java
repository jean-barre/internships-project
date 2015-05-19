package com.enseirb.pfa.bastats.data.DAO.action;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.enseirb.pfa.bastats.data.DAO.BaseDAO;
import com.enseirb.pfa.bastats.data.DAO.DBJoueurDAO;
import com.enseirb.pfa.bastats.data.DAO.DBTempsDeJeuDAO;
import com.enseirb.pfa.bastats.data.model.Joueur;
import com.enseirb.pfa.bastats.data.model.action.Rebond;

import java.util.ArrayList;
import java.util.List;

public class DBRebondDAO extends BaseDAO {

    public static final  String TABLE_NAME                  = "REBOND";

    private static final String ID_FIELD_NAME               = "_ID";
    private static final String ACTION_ID_FIELD_NAME        = "ACTION_ID";
    private static final String CARACTERE_FIELD_NAME        = "TYPE";

    private static final String ID_FIELD_TYPE               = "INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String ACTION_ID_FIELD_TYPE        = "INTEGER";
    private static final String CARACTERE_FIELD_TYPE        = "INTEGER";

    private static final int NUM_COL_ID                     = 0;
    private static final int NUM_COL_ACTION                 = 1;
    private static final int NUM_COL_CARACTERE              = 2;


    public static final String CREATE_TABLE_STATEMENT = ID_FIELD_NAME + " " + ID_FIELD_TYPE
            + ", " + ACTION_ID_FIELD_NAME+ " " + ACTION_ID_FIELD_TYPE
            + ", " + CARACTERE_FIELD_NAME + " " + CARACTERE_FIELD_TYPE
            + ", " + "FOREIGN KEY (" + ACTION_ID_FIELD_NAME +") "
            + "REFERENCES "+ DBActionDAO.TABLE_NAME+"(ID) ";


    public DBRebondDAO(Context context){
	    super(context);
        this.mDb = this.open();
    }


    public String RebondsJSON(int matchId,List<Joueur> joueurs){
        StringBuilder str=new StringBuilder();


        for(Joueur j: joueurs) {
            List<Rebond> rebonds=getAll(j,matchId);
            if(rebonds!=null) {
                for (Rebond rebond : rebonds) {
                    String rebondJson = RebondJSON(rebond.getId());

                    str.append(rebondJson);

                }
            }
        }

        return str.toString();
    }

    public List<Rebond> getAll(Joueur j,int matchId){
        int joueurId=j.getId();

        //select shoot.* from action, tempsdejeu,shoot where joueur_acteur_id=joueurid and tempsdejeu.match_id=matchId and action.tdjid=tempsdejeu.id and shoot.actionid=action.id
        Cursor c = super.mDb.rawQuery("SELECT "+TABLE_NAME+".*"+
                        " FROM "+TABLE_NAME+" , "+DBActionDAO.TABLE_NAME+" , "+DBTempsDeJeuDAO.TABLE_NAME+
                        " WHERE " +DBActionDAO.TABLE_NAME+"."+DBActionDAO.JOUEUR_ACTEUR_ID_FIELD_NAME +"="+joueurId
                        +" AND "+DBTempsDeJeuDAO.TABLE_NAME+"."+DBTempsDeJeuDAO.MATCH_ID_FIELD_NAME+"="+matchId
                        +" AND "+DBTempsDeJeuDAO.TABLE_NAME+"."+DBTempsDeJeuDAO.ID_FIELD_NAME+"="+DBActionDAO.TABLE_NAME+"."+DBActionDAO.TEMPS_DE_JEU_FIELD_NAME
                        +" AND "+DBRebondDAO.TABLE_NAME+"."+DBRebondDAO.ACTION_ID_FIELD_NAME+"="+DBActionDAO.TABLE_NAME+"."+DBActionDAO.ID_FIELD_NAME
                ,
                null);
        return cursorToListRebond(c);

    }







    public String RebondJSON(int id){
        StringBuilder str=new StringBuilder("{\"TypeAction\" :{");
        Cursor c = super.mDb.query(TABLE_NAME, null,ID_FIELD_NAME +"=" + id, null, null, null, null);
        c.moveToFirst();
        int actionId=c.getInt(NUM_COL_ACTION);
        DBActionDAO dba=new DBActionDAO(mCtx);

        String actionJSON=dba.ActionJSON(actionId);
        str.append("\""+"Type:"+"\": "+"\""+"REBOND"+"\""+" ,\n");

        for(int i=2;i<c.getColumnCount();i++) {
            String columnName=c.getColumnName(i), columnValue=c.getString(i);
            str.append("\""+columnName+"\": "+"\""+columnValue+"\""+" ,\n");

        }
        str.append("},\n");
        str.append(actionJSON);
        return str.toString();
    }












    public long insert(Rebond rebond){
        ContentValues values = new ContentValues();
        DBActionDAO dba=new DBActionDAO(mCtx);
        dba.insert(rebond);

        values.put(ACTION_ID_FIELD_NAME,dba.getLast().getActionId());
        values.put(CARACTERE_FIELD_NAME,rebond.getCaractere());
        return  mDb.insert(TABLE_NAME, null, values);
    }

    public long update(int id, Rebond rebond){
        ContentValues values = new ContentValues();

        values.put(CARACTERE_FIELD_NAME,rebond.getCaractere());
        return mDb.update(TABLE_NAME, values, ID_FIELD_NAME + " = " + id, null);
    }


    public int removeWithId(int id){
        return mDb.delete(TABLE_NAME, ID_FIELD_NAME + " = " +id, null);
        //mDb.execSQL("DELETE " + " FROM ACTION "+ " WHERE ID in (select ACTION_ID FROM" + TABLE_NAME+ "WHERE ID="+id+"));

    }


    public Rebond getWithId(int id){
        Cursor c = super.mDb.query(TABLE_NAME, null,
                ID_FIELD_NAME +"=" + id, null, null, null, null);
        return cursorToRebond(c);
    }


    public List<Rebond> getAll(){
        Cursor c = super.mDb.query(TABLE_NAME, null,
                null, null, null, null, null);
        return cursorToListRebond(c);
    }


    private List<Rebond> cursorToListRebond(Cursor c){
        if (c.getCount() == 0)
            return null;

        List<Rebond> liste = new ArrayList<Rebond>();
        liste.clear();

        if (c.moveToFirst()) {
            do {
                Rebond rebond = new Rebond();
                rebond.setId(c.getInt(NUM_COL_ID));
                rebond.setActionId(c.getInt(NUM_COL_ACTION));
                rebond.setCaractere(c.getInt(NUM_COL_CARACTERE));
                liste.add(rebond);
            } while (c.moveToNext());
        }
        c.close();
        return liste;
    }


    private Rebond cursorToRebond(Cursor c){
        if (c.getCount() == 0)
            return null;

        c.moveToFirst();
        Rebond rebond = new Rebond();

        rebond.setId(c.getInt(NUM_COL_ID));
        rebond.setActionId(c.getInt(NUM_COL_ACTION));
        rebond.setCaractere(c.getInt(NUM_COL_CARACTERE));

        c.close();

        return rebond;
    }



    public Rebond getLast(){
        Cursor c = mDb.query(TABLE_NAME,null, null, null, null, null, ID_FIELD_NAME+" DESC", null);
        c.moveToLast();
        return cursorToRebond(c);
    }







}
