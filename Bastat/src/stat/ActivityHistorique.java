package com.enseirb.pfa.bastats.stat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.enseirb.pfa.bastats.R;
import com.enseirb.pfa.bastats.data.DAO.DBJoueurDAO;
import com.enseirb.pfa.bastats.data.DAO.DBTempsDeJeuDAO;
import com.enseirb.pfa.bastats.data.DAO.action.DBActionDAO;
import com.enseirb.pfa.bastats.data.model.Joueur;
import com.enseirb.pfa.bastats.data.model.TempsDeJeu;
import com.enseirb.pfa.bastats.data.model.action.Action;
import com.enseirb.pfa.bastats.tournoi.KeyTable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Jean on 20/03/15.
 */
public class ActivityHistorique extends Activity {

    private Context mCtx;
    private int matchId;
    private List<Action> actionList;

    private ListView mHistoriqueListView;
    private List<String> mHistorique;

    DBActionDAO actionDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);

        matchId = getIntent().getIntExtra(KeyTable.MATCH_ID,-1);
        actionDAO = new DBActionDAO(mCtx);
        actionList = actionDAO.getActionsMatch(matchId);

        // Rempli mHistorique
        Iterator<Action> i = actionList.iterator();
        do {
            Action action = i.next();
            String descriptif = getDescriptif(action);
            mHistorique.add(descriptif);
        } while (i.hasNext());

        // Transmet mHistorique à mHistoriqueListView
        mHistoriqueListView = (ListView) findViewById(R.id.history_listview);
        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.activity_list_item , mHistorique);
        mHistoriqueListView.setAdapter(adapter);

    }

    private String getDescriptif(Action action) {

        DBTempsDeJeuDAO tempsDeJeuDAO = new DBTempsDeJeuDAO(mCtx);
        TempsDeJeu temps = tempsDeJeuDAO.getWithId(action.getTempsDeJeu());
        String chrono = temps.getChronometre();

        DBJoueurDAO joueurDAO = new DBJoueurDAO(mCtx);
        Joueur joueur = joueurDAO.getWithId(action.getJoueurActeur());
        String nom = joueur.getNom();
        String prenom = joueur.getPrenom();

        String actionType = getActionType(action.getActionId());

        return "TEMPS : "+chrono+"  JOUEUR : "+nom+" "+prenom+"   "+actionType;
    }


    private String getActionType(int actionId) {
        if (actionDAO.isAContre(actionId))
            return "CONTRE";
        else if (actionDAO.isAOffFaute(actionId))
            return "FAUTE OFFENSIVE";
        else if (actionDAO.isADefFaute(actionId))
            return "FAUTE DEFENSIVE";
        else if (actionDAO.isAInterception(actionId))
            return "INTERCEPTION";
        else if (actionDAO.isAPasse(actionId))
            return "PASSE";
        else if (actionDAO.isAPerteBalle(actionId))
            return "PERTE DE BALLE";
        else if (actionDAO.isAOffRebond(actionId))
            return "REBOND OFFENSIF";
        else if (actionDAO.isADefRebond(actionId))
            return "REBOND DEFENSIVE";
        else if (actionDAO.isAReussi1PT(actionId))
            return "LANCER FRANC REUSSI";
        else if (actionDAO.isAManque1PT(actionId))
            return "LANCER FRANC MANQUE";
        else if (actionDAO.isAReussi2PTS(actionId))
            return "2 POINTS REUSSI";
        else if (actionDAO.isAManque2PTS(actionId))
            return "2 POINTS MANQUE";
        else if (actionDAO.isAReussi3PTS(actionId))
            return "3 POINTS REUSSI";
        else if (actionDAO.isAManque3PTS(actionId))
            return "3 POINTS MANQUE";

        else
            return "?";
    }


}
