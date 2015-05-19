package com.enseirb.pfa.bastats.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rchabot on 24/02/15.
 */
public class DBTournoiEquipeDAO extends BaseDAO{
    public static final String TABLE_NAME = "TOURNOI_EQUIPE";

    private static final String ID_TOURNOI_FIELD_NAME      = "ID_TOURNOI";
    private static final String ID_EQUIPE_FIELD_NAME       = "ID_EQUIPE";

    private static final String ID_TOURNOI_FIELD_TYPE      = "INTEGER NOT NULL";
    private static final String ID_EQUIPE_FIELD_TYPE       = "INTEGER NOT NULL";

    private static final int NUM_COL_ID_TOURNOI            = 0;
    private static final int NUM_COL_ID_EQUIPE             = 1;


    public static final String CREATE_TABLE_STATEMENT = ID_TOURNOI_FIELD_NAME + " " + ID_TOURNOI_FIELD_TYPE
            + "," + ID_EQUIPE_FIELD_NAME + " " + ID_EQUIPE_FIELD_TYPE
            + ", " + "PRIMARY KEY(" + ID_EQUIPE_FIELD_NAME + ", " + ID_TOURNOI_FIELD_NAME + ")"
            + ", " + "FOREIGN KEY (" + ID_TOURNOI_FIELD_NAME+") "
            + "REFERENCES "+ DBTournoiDAO.TABLE_NAME+"(ID)"
            + ", " + "FOREIGN KEY (" + ID_EQUIPE_FIELD_NAME +") "
            + "REFERENCES "+ DBEquipeDAO.TABLE_NAME+"(ID) ";


    public DBTournoiEquipeDAO(Context context) {
        super(context);
        this.mDb = this.open();
    }

    public long insert(int tournoiId, int equipeId) {
        ContentValues values = new ContentValues();

        values.put(ID_TOURNOI_FIELD_NAME, tournoiId);
        values.put(ID_EQUIPE_FIELD_NAME, equipeId);

        return mDb.insert(TABLE_NAME, null, values);
    }

    public List<Integer> getTeamsList(int tournoiId){
        Cursor c = super.mDb.rawQuery("SELECT "+ID_EQUIPE_FIELD_NAME+
                " FROM "+TABLE_NAME+
                " WHERE "+TABLE_NAME+"."+ID_TOURNOI_FIELD_NAME+"="+tournoiId, null);

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
