package com.enseirb.pfa.bastats.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rchabot on 12/03/15.
 */
public class DBTableauEquipeDAO extends BaseDAO{
    public static final String TABLE_NAME = "TABLEAU_EQUIPE";

    private static final String ID_TABLEAU_FIELD_NAME      = "ID_TABLEAU";
    private static final String ID_EQUIPE_FIELD_NAME       = "ID_EQUIPE";

    private static final String ID_TABLEAU_FIELD_TYPE      = "INTEGER NOT NULL";
    private static final String ID_EQUIPE_FIELD_TYPE       = "INTEGER NOT NULL";

    private static final int NUM_COL_ID_TABLEAU            = 0;
    private static final int NUM_COL_ID_EQUIPE             = 1;


    public static final String CREATE_TABLE_STATEMENT = ID_TABLEAU_FIELD_NAME + " " + ID_TABLEAU_FIELD_TYPE
            + "," + ID_EQUIPE_FIELD_NAME + " " + ID_EQUIPE_FIELD_TYPE
            + ", " + "PRIMARY KEY(" + ID_TABLEAU_FIELD_NAME + ", " + ID_EQUIPE_FIELD_NAME + ")"
            + ", " + "FOREIGN KEY (" + ID_TABLEAU_FIELD_NAME+") "
            + "REFERENCES "+ DBPhaseTableauDAO.TABLE_NAME+"(ID)"
            + ", " + "FOREIGN KEY (" + ID_EQUIPE_FIELD_NAME +") "
            + "REFERENCES "+ DBEquipeDAO.TABLE_NAME+"(ID) ";


    public DBTableauEquipeDAO(Context context) {
        super(context);
        this.mDb = this.open();
    }

    public long insert(int tableauId, int equipeId) {
        ContentValues values = new ContentValues();

        values.put(ID_TABLEAU_FIELD_NAME, tableauId);
        values.put(ID_EQUIPE_FIELD_NAME, equipeId);

        return mDb.insert(TABLE_NAME, null, values);
    }

    public List<Integer> getTeamsList(int tableauId){
        Cursor c = super.mDb.rawQuery("SELECT "+ID_EQUIPE_FIELD_NAME+
                " FROM "+TABLE_NAME+
                " WHERE "+TABLE_NAME+"."+ID_TABLEAU_FIELD_NAME+"="+tableauId, null);

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
