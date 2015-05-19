package com.enseirb.pfa.bastats.stat;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.enseirb.pfa.bastats.R;
import com.enseirb.pfa.bastats.data.DAO.DBJoueurDAO;
import com.enseirb.pfa.bastats.data.DAO.DBTempsDeJeuDAO;
import com.enseirb.pfa.bastats.data.DAO.action.DBActionDAO;
import com.enseirb.pfa.bastats.data.model.Joueur;
import com.enseirb.pfa.bastats.data.model.TempsDeJeu;
import com.enseirb.pfa.bastats.data.model.action.Action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HistoriqueFragment extends Fragment {
    private static final String ARG_MATCH_ID = "matchId";
    private int matchId;

    private Context mCtx;

    private List<String> mHistorique;

    DBActionDAO actionDAO;

    public static HistoriqueFragment newInstance(int matchId) {
        Log.d("HISTO", "call newInstance");
        HistoriqueFragment fragment = new HistoriqueFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MATCH_ID, matchId);
        fragment.setArguments(args);
        return fragment;
    }

    public HistoriqueFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("HISTO", "call onCreate");
        if (getArguments() != null) {
            matchId = getArguments().getInt(ARG_MATCH_ID);
        }
        mCtx = getActivity();
        mHistorique = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_historique, container, false);
        actionDAO = new DBActionDAO(mCtx);
        List<Action> actionList = new ArrayList<>(actionDAO.getActionsMatch(matchId));
        mHistorique.clear();
        // Rempli mHistorique
        Iterator<Action> i = actionList.iterator();
        do {
            Action action = i.next();
            String descriptif = getDescriptif(action);
            mHistorique.add(descriptif);
        } while (i.hasNext());

        // Transmet mHistorique à mHistoriqueListView
        ListView mHistoriqueListView = (ListView) view.findViewById(R.id.history_listview);
        ArrayAdapter adapter = new ArrayAdapter(mCtx,
                android.R.layout.simple_list_item_1 , mHistorique);
        Log.d("HISTO", "call oncreateView");
        mHistoriqueListView.setAdapter(adapter);
        return view;
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
        else if (actionDAO.isAFaute(actionId))
            return "FAUTE";
        else if (actionDAO.isAInterception(actionId))
            return "INTERCEPTION";
        else if (actionDAO.isAPasse(actionId))
            return "PASSE";
        else if (actionDAO.isAPerteBalle(actionId))
            return "PERTE DE BALLE";
        else if (actionDAO.isARebond(actionId))
            return "REBOND";
        else if (actionDAO.isAShoot(actionId))
            return "TIR";
        else
            return "?";
    }

}
