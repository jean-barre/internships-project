package com.enseirb.pfa.bastats.data.DAO.action;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.enseirb.pfa.bastats.data.DAO.BaseDAO;
import com.enseirb.pfa.bastats.data.DAO.DBTempsDeJeuDAO;
import com.enseirb.pfa.bastats.data.model.Joueur;
import com.enseirb.pfa.bastats.data.model.action.Action;
import com.enseirb.pfa.bastats.data.model.action.Shoot;

import java.util.ArrayList;
import java.util.List;

public class DBShootDAO extends BaseDAO {

    public static final  String TABLE_NAME                  = "SHOOT";

    private static final String ID_FIELD_NAME               = "_ID";
    private static final String ACTION_ID_FIELD_NAME        = "ACTION_ID";
    private static final String PTS_FIELD_NAME              = "PTS";
    private static final String REUSSI_FIELD_NAME           = "REUSSI";


    private static final String ID_FIELD_TYPE               = "INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String ACTION_ID_FIELD_TYPE        = "INTEGER";
    private static final String PTS_FIELD_TYPE              = "INTEGER";
    private static final String REUSSI_FIELD_TYPE           = "INTEGER";


    private static final int NUM_COL_ID                     =0;
    private static final int NUM_COL_ACTION                 =1;
    private static final int NUM_COL_PTS                    =2;
    private static final int NUM_COL_REUSSI                 =3;


    public static final String CREATE_TABLE_STATEMENT = ID_FIELD_NAME + " " + ID_FIELD_TYPE

    + ", " + ACTION_ID_FIELD_NAME+ " " + ACTION_ID_FIELD_TYPE
    + ", " + PTS_FIELD_NAME + " " + PTS_FIELD_TYPE
	+ ", " + REUSSI_FIELD_NAME  + " " + REUSSI_FIELD_TYPE
    + ", " + "FOREIGN KEY (" + ACTION_ID_FIELD_NAME +") "
           + "REFERENCES "+ DBActionDAO.TABLE_NAME+"(ID) ";


    public DBShootDAO(Context context){
	    super(context);
        this.mDb = this.open();
    }


    public String ShootsJSON(int matchId,List<Joueur> joueurs){
        StringBuilder str=new StringBuilder();


        for(Joueur j: joueurs) {
            List<Shoot> shoots=getAll(j,matchId);
             if(shoots!=null) {
                 for (Shoot shoot : shoots) {
                     String shootJson = ShootJSON(shoot.getId());

                     str.append(shootJson);

                 }
             }
        }

      return str.toString();
    }


    public String ShootJSON(int id){
        StringBuilder str=new StringBuilder("{\"TypeAction\" :{");
        Cursor c = super.mDb.query(TABLE_NAME, null,ID_FIELD_NAME +"=" + id, null, null, null, null);
        c.moveToFirst();
        int actionId=c.getInt(NUM_COL_ACTION);
        DBActionDAO dba=new DBActionDAO(mCtx);

        String actionJSON=dba.ActionJSON(actionId);
        str.append("\""+"Type:"+"\": "+"\""+"SHOOT"+"\""+" ,\n");

        for(int i=2;i<c.getColumnCount();i++) {
            String columnName=c.getColumnName(i), columnValue=c.getString(i);
            str.append("\""+columnName+"\": "+"\""+columnValue+"\""+" ,\n");

        }
        str.append("},\n");
        str.append(actionJSON);
        return str.toString();
    }


    public List<Shoot> getAll(Joueur j,int matchId){
        int joueurId=j.getId();


        Cursor c = super.mDb.rawQuery("SELECT "+TABLE_NAME+".*"+
                        " FROM "+TABLE_NAME+" , "+DBActionDAO.TABLE_NAME+" , "+DBTempsDeJeuDAO.TABLE_NAME+
                        " WHERE " +DBActionDAO.TABLE_NAME+"."+DBActionDAO.JOUEUR_ACTEUR_ID_FIELD_NAME +"="+joueurId
                        +" AND "+DBTempsDeJeuDAO.TABLE_NAME+"."+DBTempsDeJeuDAO.MATCH_ID_FIELD_NAME+"="+matchId
                        +" AND "+DBTempsDeJeuDAO.TABLE_NAME+"."+DBTempsDeJeuDAO.ID_FIELD_NAME+"="+DBActionDAO.TABLE_NAME+"."+DBActionDAO.TEMPS_DE_JEU_FIELD_NAME
                        +" AND "+DBShootDAO.TABLE_NAME+"."+DBShootDAO.ACTION_ID_FIELD_NAME+"="+DBActionDAO.TABLE_NAME+"."+DBActionDAO.ID_FIELD_NAME
                ,
                null);
        return cursorToListShoot(c);

    }






    public long insert(Shoot shoot){
        ContentValues values = new ContentValues();
        DBActionDAO dba=new DBActionDAO(mCtx);
        dba.insert(shoot);



        values.put(ACTION_ID_FIELD_NAME,dba.getLast().getActionId());
        values.put(REUSSI_FIELD_NAME,shoot.getReussi());
        values.put(PTS_FIELD_NAME, shoot.getPts());

        return  mDb.insert(TABLE_NAME, null, values);
    }

    public long update(int id, Shoot shoot){
	    ContentValues values = new ContentValues();


        values.put(REUSSI_FIELD_NAME,shoot.getReussi());
        values.put(PTS_FIELD_NAME, shoot.getPts());
	    return mDb.update(TABLE_NAME, values, ID_FIELD_NAME + " = " + id, null);
    }


    public int removeWithId(int id){
	    return mDb.delete(TABLE_NAME, ID_FIELD_NAME + " = " +id, null);

    }


    public Shoot getWithActionId(int actionId){
        Cursor c = super.mDb.query(TABLE_NAME, null,
                ACTION_ID_FIELD_NAME +"=" + actionId, null, null, null, null);
        return cursorToShoot(c);

    }

    public Shoot getWithId(int id){
        Cursor c = super.mDb.query(TABLE_NAME, null,
                ID_FIELD_NAME +"=" + id, null, null, null, null);

        Shoot shoot=cursorToShoot(c);
        DBActionDAO dba=new DBActionDAO(mCtx);
        Action action=dba.getWithId(shoot.getActionId());
        shoot.setCommentaire(action.getCommentaire());
        shoot.setJoueurActeur(action.getJoueurActeur());
        shoot.setTempsDeJeu(action.getTempsDeJeu());

        return shoot;

    }



    public List<Shoot> getAll(){
        Cursor c = super.mDb.query(TABLE_NAME, null,
                    null, null, null, null, null);
        return cursorToListShoot(c);
    }


 private List<Shoot> cursorToListShoot(Cursor c){
        if (c.getCount() == 0)
            return null;

        List<Shoot> liste = new ArrayList<Shoot>();
        liste.clear();

        if (c.moveToFirst()) {
            do {
                Shoot shoot = new Shoot();
                shoot.setId(c.getInt(NUM_COL_ID));
                shoot.setActionId(c.getInt(NUM_COL_ACTION));
                shoot.setPts(c.getInt(NUM_COL_PTS));
		        shoot.setReussi(c.getInt(NUM_COL_REUSSI));
                liste.add(shoot);
            } while (c.moveToNext());
        }
        c.close();
        return liste;
 }


   private Shoot cursorToShoot(Cursor c){
        if (c.getCount() == 0)
            return null;

        c.moveToFirst();
        Shoot shoot=new Shoot();

        shoot.setId(c.getInt(NUM_COL_ID));
        shoot.setActionId(c.getInt(NUM_COL_ACTION));
        shoot.setPts(c.getInt(NUM_COL_PTS));
        shoot.setReussi(c.getInt(NUM_COL_REUSSI));

        c.close();

        return shoot;
    }

    public Shoot getLast(){
        Cursor c = mDb.query(TABLE_NAME,null, null, null, null, null, ID_FIELD_NAME+" DESC", null);
        c.moveToLast();
        return cursorToShoot(c);
    }







}
