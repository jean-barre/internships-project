package com.enseirb.pfa.bastats.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.enseirb.pfa.bastats.data.model.TempsDeJeu;

import java.util.ArrayList;
import java.util.List;

public class DBTempsDeJeuDAO extends BaseDAO{

    public static final String TABLE_NAME                   = "TEMPS_DE_JEU";

    public static final String ID_FIELD_NAME               = "_ID";
    public static final String MATCH_ID_FIELD_NAME         = "MATCH_ID";
    private static final String NUMERO_QT_FIELD_NAME        = "NUMERO_QT";
    private static final String CHRONOMETRE_FIELD_NAME      = "CHRONOMETRE";
    private static final String NB_FAUTE_A_FIELD_NAME       = "NB_FAUTE_A";
    private static final String NB_FAUTE_B_FIELD_NAME       = "NB_FAUTE_B";
    private static final String LIBELLE_FIELD_NAME          = "LIBELLE";

    private static final String ID_FIELD_TYPE               = "INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String MATCH_ID_FIELD_TYPE         = "INTEGER";
    private static final String NUMERO_QT_FIELD_TYPE        = "INTEGER";
    private static final String CHRONOMETRE_FIELD_TYPE      = "TEXT";
    private static final String NB_FAUTE_A_FIELD_TYPE       = "INTEGER";
    private static final String NB_FAUTE_B_FIELD_TYPE       = "INTEGER";
    private static final String LIBELLE_FIELD_TYPE          = "TEXT";

    private static final int NUM_COL_ID = 0;
    private static final int NUM_COL_MATCHID = 1;
    private static final int NUM_COL_NUMERO_QT = 2;
    private static final int NUM_COL_CHRONOMETRE = 3;
    private static final int NUM_COL_FAUTE_A = 4;
    private static final int NUM_COL_FAUTE_B = 5;
    private static final int NUM_COL_LIBELLE = 6;

    public static final String CREATE_TABLE_STATEMENT = ID_FIELD_NAME + " " + ID_FIELD_TYPE
            + ", " + MATCH_ID_FIELD_NAME     + " " + MATCH_ID_FIELD_TYPE
            + ", " + NUMERO_QT_FIELD_NAME    + " " + NUMERO_QT_FIELD_TYPE
            + ", " + CHRONOMETRE_FIELD_NAME  + " " + CHRONOMETRE_FIELD_TYPE
            + ", " + NB_FAUTE_A_FIELD_NAME   + " " + NB_FAUTE_A_FIELD_TYPE
            + ", " + NB_FAUTE_B_FIELD_NAME   + " " + NB_FAUTE_B_FIELD_TYPE
            + ", " + LIBELLE_FIELD_NAME + " " + LIBELLE_FIELD_TYPE
            + ", " + "FOREIGN KEY (" + MATCH_ID_FIELD_NAME+") "
            + "REFERENCES "+ DBMatchDAO.TABLE_NAME+"(ID)";

    public DBTempsDeJeuDAO(Context context){
        super(context);
        super.mDb = this.open();
    }

    public long insert(TempsDeJeu tempsDeJeu){
        ContentValues values = new ContentValues();

        values.put(MATCH_ID_FIELD_NAME, tempsDeJeu.getMatchId());
        values.put(NUMERO_QT_FIELD_NAME, tempsDeJeu.getNumeroQT());
        values.put(CHRONOMETRE_FIELD_NAME, tempsDeJeu.getChronometre());
        values.put(NB_FAUTE_A_FIELD_NAME, tempsDeJeu.getNbFauteEquipeA());
        values.put(NB_FAUTE_B_FIELD_NAME, tempsDeJeu.getNbFauteEquipeB());
        values.put(LIBELLE_FIELD_NAME, tempsDeJeu.getLibelle());

        return super.mDb.insert(TABLE_NAME, null, values);
    }

    public int update(int id, TempsDeJeu tempsDeJeu){
        ContentValues values = new ContentValues();

        values.put(MATCH_ID_FIELD_NAME, tempsDeJeu.getMatchId());
        values.put(NUMERO_QT_FIELD_NAME, tempsDeJeu.getNumeroQT());
        values.put(CHRONOMETRE_FIELD_NAME, tempsDeJeu.getChronometre());
        values.put(NB_FAUTE_A_FIELD_NAME, tempsDeJeu.getNbFauteEquipeA());
        values.put(NB_FAUTE_B_FIELD_NAME, tempsDeJeu.getNbFauteEquipeB());
        values.put(LIBELLE_FIELD_NAME, tempsDeJeu.getLibelle());

        return super.mDb.update(TABLE_NAME, values, ID_FIELD_NAME + " = " + id, null);
    }

    public String TempsdeJeuJSON(int id){
        StringBuilder str=new StringBuilder("\"tempsDeJeu\" :{");
        Cursor c = super.mDb.query(TABLE_NAME, null,ID_FIELD_NAME +"=" + id, null, null, null, null);
        c.moveToFirst();
        for(int i=1;i<c.getColumnCount();i++) {
            String columnName=c.getColumnName(i), columnValue=c.getString(i);
            str.append("\""+columnName+"\": "+"\""+columnValue+"\""+" ,\n");

        }
        str.append("}\n}");
        return str.toString();
    }






    public int removeWithId(int id){
        return super.mDb.delete(TABLE_NAME, ID_FIELD_NAME + " = " +id, null);
    }

    public TempsDeJeu getWithId(int id){
        Cursor c = super.mDb.query(TABLE_NAME, new String[] {ID_FIELD_NAME, MATCH_ID_FIELD_NAME,
                        NUMERO_QT_FIELD_NAME, CHRONOMETRE_FIELD_NAME, NB_FAUTE_A_FIELD_NAME,
                        NB_FAUTE_B_FIELD_NAME, LIBELLE_FIELD_NAME},
                ID_FIELD_NAME +"=" + id, null, null, null, null);
        return cursorToTempsDeJeu(c);
    }

    public List<TempsDeJeu> getAll(){
        Cursor c = super.mDb.query(TABLE_NAME, new String[] {ID_FIELD_NAME, MATCH_ID_FIELD_NAME,
                NUMERO_QT_FIELD_NAME, CHRONOMETRE_FIELD_NAME, NB_FAUTE_A_FIELD_NAME,
                NB_FAUTE_B_FIELD_NAME, LIBELLE_FIELD_NAME} , null, null, null, null, null);
        return cursorToListTemps(c);
    }

    private List<TempsDeJeu> cursorToListTemps(Cursor c){
        if (c.getCount() == 0)
            return null;

        List<TempsDeJeu> liste = new ArrayList<TempsDeJeu>();
        liste.clear();

        if (c.moveToFirst()) {
            do {
                TempsDeJeu tempsDeJeu = new TempsDeJeu();
                tempsDeJeu.setId(c.getInt(NUM_COL_ID));
                tempsDeJeu.setMatchId(c.getInt(NUM_COL_MATCHID));
                tempsDeJeu.setNumeroQT(c.getInt(NUM_COL_NUMERO_QT));
                tempsDeJeu.setChronometre(c.getString(NUM_COL_CHRONOMETRE));
                tempsDeJeu.setNbFauteEquipeA(c.getInt(NUM_COL_FAUTE_A));
                tempsDeJeu.setNbFauteEquipeB(c.getInt(NUM_COL_FAUTE_B));
                tempsDeJeu.setLibelle(c.getString(NUM_COL_LIBELLE));
                liste.add(tempsDeJeu);
            } while (c.moveToNext());
        }
        c.close();
        return liste;
    }

    private TempsDeJeu cursorToTempsDeJeu(Cursor c){
        if (c.getCount() == 0)
            return null;

        c.moveToFirst();

        TempsDeJeu tempsDeJeu = new TempsDeJeu();
        tempsDeJeu.setId(c.getInt(NUM_COL_ID));
        tempsDeJeu.setMatchId(c.getInt(NUM_COL_MATCHID));
        tempsDeJeu.setNumeroQT(c.getInt(NUM_COL_NUMERO_QT));
        tempsDeJeu.setChronometre(c.getString(NUM_COL_CHRONOMETRE));
        tempsDeJeu.setNbFauteEquipeA(c.getInt(NUM_COL_FAUTE_A));
        tempsDeJeu.setNbFauteEquipeB(c.getInt(NUM_COL_FAUTE_B));
        tempsDeJeu.setLibelle(c.getString(NUM_COL_LIBELLE));

        c.close();

        return tempsDeJeu;
    }

    public TempsDeJeu getLast(){
        Cursor c = mDb.query(TABLE_NAME,null, null, null, null, null, ID_FIELD_NAME+" DESC", null);
        c.moveToLast();
        return cursorToTempsDeJeu(c);
    }


}
