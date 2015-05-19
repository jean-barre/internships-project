package com.enseirb.pfa.bastats.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class DBPouleEquipeDAO extends BaseDAO{

    public static final String TABLE_NAME                   = "POULE_EQUIPE";

    public static final String POULE_ID_FIELD_NAME         = "POULE_ID";
    public static final String EQUIPE_ID_FIELD_NAME        = "EQUIPE_ID";
    //private static final String REGLE_FIELD_NAME           	= "REGLE";

    private static final String ID_FIELD_TYPE          		= "INTEGER NOT NULL";
    private static final String EQUIPE_ID_FIELD_TYPE   		= "INTEGER NOT NULL";
    //private static final String REGLE_FIELD_TYPE      		= "TEXT";

    private static final int NUM_COL_POULE_ID          		  	= 0;
    private static final int NUM_COL_EQUIPE_ID            	= 1;
    //private static final int NUM_COL_REGLE      		    = 2;

    public static final String CREATE_TABLE_STATEMENT = POULE_ID_FIELD_NAME + " " + ID_FIELD_TYPE
	+ ", " + EQUIPE_ID_FIELD_NAME         + " " + EQUIPE_ID_FIELD_TYPE 
	//+ ", " + REGLE_FIELD_NAME      		  + " " + REGLE_FIELD_TYPE
            + ", " + "PRIMARY KEY(" + POULE_ID_FIELD_NAME + ", " + EQUIPE_ID_FIELD_NAME + ")"
            + ", " + "FOREIGN KEY (" + POULE_ID_FIELD_NAME+") "
            + "REFERENCES "+ DBPouleDAO.TABLE_NAME+"(ID)"
            + ", " + "FOREIGN KEY (" + EQUIPE_ID_FIELD_NAME +") "
            + "REFERENCES "+ DBEquipeDAO.TABLE_NAME+"(ID) ";

	public DBPouleEquipeDAO(Context context){
        super(context);
        this.mDb = this.open();
    }

    public long insert(int pouleId, int equipeId){
	    ContentValues values = new ContentValues();

        values.put(POULE_ID_FIELD_NAME, pouleId);
	    values.put(EQUIPE_ID_FIELD_NAME, equipeId);
	    //values.put(REGLE_FIELD_NAME,           pouleEquipe.getRegle());

	    return super.mDb.insert(TABLE_NAME, null, values);
    }

    public List<Integer> getTeamsList(int pouleId){
        Cursor c = super.mDb.rawQuery("SELECT "+EQUIPE_ID_FIELD_NAME+
                " FROM "+TABLE_NAME+
                " WHERE "+TABLE_NAME+"."+POULE_ID_FIELD_NAME+"="+pouleId, null);

        return cursorToTeamList(c);
    }

    public List<Integer> cursorToTeamList(Cursor c){
        if (c.getCount() == 0)
            return null;

        List<Integer> teamList = new ArrayList<>();
        teamList.clear();

        if (c.moveToFirst()) {
            do {
                teamList.add(c.getInt(0)); // 0 car on sélectionne seulement une colonne
            } while (c.moveToNext());
        }
        c.close();
        return teamList;
    }
}