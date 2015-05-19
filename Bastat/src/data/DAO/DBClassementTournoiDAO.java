package com.enseirb.pfa.bastats.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.enseirb.pfa.bastats.data.model.ClassementTournoi;
import com.enseirb.pfa.bastats.tournoi.algorithme.Classement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rchabot on 13/03/15.
 */
public class DBClassementTournoiDAO extends BaseDAO {

    public static final String TABLE_NAME                                 = "CLASSEMENT_TOURNOI";

    private static final String ID_FIELD_NAME           		  = "_ID";
    private static final String REGLE_EQUIPE_FIELD_NAME           	  = "REGLE_EQUIPE";
    private static final String EQUIPE_ID_FIELD_NAME           		  = "EQUIPE_ID";
    private static final String POINTS_FIELD_NAME                         = "POINTS";
    private static final String PLACE_FIELD_NAME                          = "PLACE";
    private static final String TOURNOI_ID_FIELD_NAME                     = "TOURNOI_ID";

    private static final String ID_FIELD_TYPE          			  = "INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String REGLE_EQUIPE_FIELD_TYPE          	  = "TEXT";
    private static final String EQUIPE_ID_FIELD_TYPE          		  = "INTEGER NOT NULL";
    private static final String POINTS_FIELD_TYPE                         = "INTEGER NOT NULL";
    private static final String PLACE_FIELD_TYPE                          = "INTEGER NOT NULL";
    private static final String TOURNOI_ID_FIELD_TYPE          	          = "INTEGER NOT NULL";

    private static final int NUM_COL_ID          		  	  = 0;
    private static final int NUM_COL_REGLE_EQUIPE           		  = 1;
    private static final int NUM_COL_EQUIPE_ID      			  = 2;
    private static final int NUM_COL_POINTS       		          = 3;
    private static final int NUM_COL_PLACE   			          = 4;
    private static final int NUM_COL_TOURNOI_ID   	  		  = 5;

    public static final String CREATE_TABLE_STATEMENT = ID_FIELD_NAME + " " + ID_FIELD_TYPE
	+ ", " + REGLE_EQUIPE_FIELD_NAME         + " " + REGLE_EQUIPE_FIELD_TYPE
	+ ", " + EQUIPE_ID_FIELD_NAME   	 + " " + EQUIPE_ID_FIELD_TYPE
	+ ", " + POINTS_FIELD_NAME      	 + " " + POINTS_FIELD_TYPE
	+ ", " + PLACE_FIELD_NAME        	 + " " + PLACE_FIELD_TYPE
	+ ", " + TOURNOI_ID_FIELD_NAME   	 + " " + TOURNOI_ID_FIELD_TYPE
            + ", " + "FOREIGN KEY (" + TOURNOI_ID_FIELD_NAME+") "
            + "REFERENCES "+ DBTournoiDAO.TABLE_NAME+"(ID)"
            + ", " + "FOREIGN KEY (" + EQUIPE_ID_FIELD_NAME +") "
            + "REFERENCES "+ DBEquipeDAO.TABLE_NAME+"(ID) ";



    public DBClassementTournoiDAO(Context context){
        super(context);
        this.mDb = this.open();
    }

    public long insert(ClassementTournoi clt){
        ContentValues values = new ContentValues();

        values.put(REGLE_EQUIPE_FIELD_NAME,                clt.getRegleEquipe());
        values.put(EQUIPE_ID_FIELD_NAME,          		  clt.getEquipeId());
        values.put(POINTS_FIELD_NAME,        clt.getPoints());
        values.put(PLACE_FIELD_NAME,         clt.getPlace());
        values.put(TOURNOI_ID_FIELD_NAME,       	  clt.getTournoiId());

        return super.mDb.insert(TABLE_NAME, null, values);
    }

    public int update(int id, ClassementTournoi clt){
        ContentValues values = new ContentValues();

        values.put(REGLE_EQUIPE_FIELD_NAME,                clt.getRegleEquipe());
        values.put(EQUIPE_ID_FIELD_NAME,          		  clt.getEquipeId());
        values.put(POINTS_FIELD_NAME,        clt.getPoints());
        values.put(PLACE_FIELD_NAME,         clt.getPlace());
        values.put(TOURNOI_ID_FIELD_NAME,       	  clt.getTournoiId());
        
        return super.mDb.update(TABLE_NAME, values, ID_FIELD_NAME + " = " + id, null);
    }

    public int removeWithId(int id){
        return super.mDb.delete(TABLE_NAME, ID_FIELD_NAME + " = " +id, null);
    }

    public ClassementTournoi getWithId(int id){
        Cursor c = super.mDb.query(TABLE_NAME, null,ID_FIELD_NAME +"=" + id, null, null, null, null);
        return cursorToClassementTournoi(c);
    }

    public ClassementTournoi getLast(){
        Cursor c = mDb.query(TABLE_NAME,null, null, null, null, null, ID_FIELD_NAME+" DESC", null);
        c.moveToLast();
        return cursorToClassementTournoi(c);
    }

    public List<Integer> getTeamsList(int cltId){
        Cursor c = super.mDb.rawQuery("SELECT "+EQUIPE_ID_FIELD_NAME+
                " FROM "+TABLE_NAME+
                " WHERE "+TABLE_NAME+"."+ID_FIELD_NAME+"="+cltId, null);

        return cursorToTeamList(c);
    }

    public List<ClassementTournoi> getClassement(int tournoiId){
        Cursor c = super.mDb.query(TABLE_NAME, null, TOURNOI_ID_FIELD_NAME +"=" + tournoiId,
                null, null, null, null);
        return cursorToListClassementTournoi(c);
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

    private ClassementTournoi cursorToClassementTournoi(Cursor c) {
        if (c.getCount() == 0)
            return null;

        c.moveToFirst();
        ClassementTournoi clt = new ClassementTournoi();
        clt.setId(c.getInt(NUM_COL_ID));
        clt.setEquipeId(c.getInt(NUM_COL_EQUIPE_ID));
        clt.setRegleEquipe(c.getString(NUM_COL_REGLE_EQUIPE));
        clt.setPoints(c.getInt(NUM_COL_POINTS));
        clt.setTournoiId(c.getInt(NUM_COL_TOURNOI_ID));
        clt.setPlace(c.getInt(NUM_COL_PLACE));

        c.close();

        return clt;
    }

    private List<ClassementTournoi> cursorToListClassementTournoi(Cursor c){
        if (c.getCount() == 0)
            return null;

        List<ClassementTournoi> listeClassementTournois = new ArrayList<ClassementTournoi>();
        listeClassementTournois.clear();

        if (c.moveToFirst()) {
            do {
                ClassementTournoi clt = new ClassementTournoi();
                clt.setId(c.getInt(NUM_COL_ID));
                clt.setEquipeId(c.getInt(NUM_COL_EQUIPE_ID));
                clt.setRegleEquipe(c.getString(NUM_COL_REGLE_EQUIPE));
                clt.setPoints(c.getInt(NUM_COL_POINTS));
                clt.setTournoiId(c.getInt(NUM_COL_TOURNOI_ID));
                clt.setPlace(c.getInt(NUM_COL_PLACE));
                listeClassementTournois.add(clt);
            } while (c.moveToNext());
        }
        c.close();
        return listeClassementTournois;
    }
}
