package com.enseirb.pfa.bastats.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.enseirb.pfa.bastats.data.DAO.DBClassementTournoiDAO;
import com.enseirb.pfa.bastats.data.DAO.DBFormationDAO;
import com.enseirb.pfa.bastats.data.DAO.DBFormationJoueurDAO;
import com.enseirb.pfa.bastats.data.DAO.DBMatchDAO;
import com.enseirb.pfa.bastats.data.DAO.DBPhaseDAO;
import com.enseirb.pfa.bastats.data.DAO.DBPhasePouleDAO;
import com.enseirb.pfa.bastats.data.DAO.DBPhaseTableauDAO;
import com.enseirb.pfa.bastats.data.DAO.DBPouleDAO;
import com.enseirb.pfa.bastats.data.DAO.DBPouleEquipeDAO;
import com.enseirb.pfa.bastats.data.DAO.DBPouleMatchDAO;
import com.enseirb.pfa.bastats.data.DAO.DBTableauEquipeDAO;
import com.enseirb.pfa.bastats.data.DAO.DBTournoiDAO;
import com.enseirb.pfa.bastats.data.DAO.DBTournoiEquipeDAO;
import com.enseirb.pfa.bastats.data.DAO.action.DBActionDAO;
import com.enseirb.pfa.bastats.data.DAO.action.DBContreDAO;
import com.enseirb.pfa.bastats.data.DAO.DBEquipeDAO;
import com.enseirb.pfa.bastats.data.DAO.action.DBFauteDAO;
import com.enseirb.pfa.bastats.data.DAO.action.DBInterceptionDAO;
import com.enseirb.pfa.bastats.data.DAO.DBJoueurDAO;
import com.enseirb.pfa.bastats.data.DAO.action.DBPasseDAO;
import com.enseirb.pfa.bastats.data.DAO.action.DBPerteBalleDAO;
import com.enseirb.pfa.bastats.data.DAO.action.DBRebondDAO;
import com.enseirb.pfa.bastats.data.DAO.action.DBShootDAO;
import com.enseirb.pfa.bastats.data.DAO.DBTempsDeJeuDAO;

public class MaBaseSQLite extends SQLiteOpenHelper {

    public MaBaseSQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Model
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBJoueurDAO.TABLE_NAME + " ("
                + DBJoueurDAO.CREATE_TABLE_STATEMENT + ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBTempsDeJeuDAO.TABLE_NAME + " ("
                + DBTempsDeJeuDAO.CREATE_TABLE_STATEMENT + ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBEquipeDAO.TABLE_NAME + " ("
                + DBEquipeDAO.CREATE_TABLE_STATEMENT + ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBActionDAO.TABLE_NAME + " ("
                + DBActionDAO.CREATE_TABLE_STATEMENT + ")");
        // Actions
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBShootDAO.TABLE_NAME + " ("
                + DBShootDAO.CREATE_TABLE_STATEMENT + ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBRebondDAO.TABLE_NAME + " ("
                + DBRebondDAO.CREATE_TABLE_STATEMENT + ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBPasseDAO.TABLE_NAME + " ("
                + DBPasseDAO.CREATE_TABLE_STATEMENT + ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBContreDAO.TABLE_NAME + " ("
                + DBContreDAO.CREATE_TABLE_STATEMENT + ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBInterceptionDAO.TABLE_NAME + " ("
                + DBInterceptionDAO.CREATE_TABLE_STATEMENT + ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBFauteDAO.TABLE_NAME + " ("
                + DBFauteDAO.CREATE_TABLE_STATEMENT + ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBPerteBalleDAO.TABLE_NAME + " ("
                + DBPerteBalleDAO.CREATE_TABLE_STATEMENT + ")");

        // Match
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBMatchDAO.TABLE_NAME + " ("
                + DBMatchDAO.CREATE_TABLE_STATEMENT + ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBFormationDAO.TABLE_NAME + " ("
                + DBFormationDAO.CREATE_TABLE_STATEMENT + ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBFormationJoueurDAO.TABLE_NAME + " ("
                + DBFormationJoueurDAO.CREATE_TABLE_STATEMENT + ")");

        // Tournoi
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBTournoiDAO.TABLE_NAME + " ("
                + DBTournoiDAO.CREATE_TABLE_STATEMENT + ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBClassementTournoiDAO.TABLE_NAME + " ("
                + DBClassementTournoiDAO.CREATE_TABLE_STATEMENT + ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBTournoiEquipeDAO.TABLE_NAME + " ("
                + DBTournoiEquipeDAO.CREATE_TABLE_STATEMENT + ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBTableauEquipeDAO.TABLE_NAME + " ("
                + DBTableauEquipeDAO.CREATE_TABLE_STATEMENT + ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBPhasePouleDAO.TABLE_NAME + " ("
                + DBPhasePouleDAO.CREATE_TABLE_STATEMENT + ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBPhaseDAO.TABLE_NAME + " ("
                + DBPhaseDAO.CREATE_TABLE_STATEMENT + ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBPhaseTableauDAO.TABLE_NAME + " ("
                + DBPhaseTableauDAO.CREATE_TABLE_STATEMENT + ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBPouleEquipeDAO.TABLE_NAME + " ("
                + DBPouleEquipeDAO.CREATE_TABLE_STATEMENT + ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBPouleMatchDAO.TABLE_NAME + " ("
                + DBPouleMatchDAO.CREATE_TABLE_STATEMENT + ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBPouleDAO.TABLE_NAME + " ("
                + DBPouleDAO.CREATE_TABLE_STATEMENT + ")");

        db.execSQL("PRAGMA foreign_keys=ON;"); //allows to delete on cascade (foreign key)

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Model
        db.execSQL("DROP TABLE " + DBJoueurDAO.TABLE_NAME + ";");
        db.execSQL("DROP TABLE " + DBTempsDeJeuDAO.TABLE_NAME + ";");
        db.execSQL("DROP TABLE " + DBEquipeDAO.TABLE_NAME + ";");
        // Actions
        db.execSQL("DROP TABLE " + DBShootDAO.TABLE_NAME + ";");
        db.execSQL("DROP TABLE " + DBRebondDAO.TABLE_NAME + ";");
        db.execSQL("DROP TABLE " + DBPasseDAO.TABLE_NAME + ";");
        db.execSQL("DROP TABLE " + DBContreDAO.TABLE_NAME + ";");
        db.execSQL("DROP TABLE " + DBInterceptionDAO.TABLE_NAME + ";");
        db.execSQL("DROP TABLE " + DBFauteDAO.TABLE_NAME + ";");
        db.execSQL("DROP TABLE " + DBPerteBalleDAO.TABLE_NAME + ";");
        onCreate(db);


        //PS: il faudra adapter pour les autres table
    }

}

