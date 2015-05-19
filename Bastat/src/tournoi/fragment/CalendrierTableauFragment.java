package com.enseirb.pfa.bastats.tournoi.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;


import com.enseirb.pfa.bastats.R;
import com.enseirb.pfa.bastats.accueil.ChoixEquipes;
import com.enseirb.pfa.bastats.accueil.OptionsGetters;
import com.enseirb.pfa.bastats.data.DAO.DBEquipeDAO;
import com.enseirb.pfa.bastats.data.DAO.DBFormationDAO;
import com.enseirb.pfa.bastats.data.DAO.DBFormationJoueurDAO;
import com.enseirb.pfa.bastats.data.DAO.DBJoueurDAO;
import com.enseirb.pfa.bastats.data.DAO.DBMatchDAO;
import com.enseirb.pfa.bastats.data.DAO.DBPhasePouleDAO;
import com.enseirb.pfa.bastats.data.DAO.DBPouleDAO;
import com.enseirb.pfa.bastats.data.DAO.DBStatDAO;
import com.enseirb.pfa.bastats.data.DAO.DBTableauEquipeDAO;
import com.enseirb.pfa.bastats.data.model.Equipe;
import com.enseirb.pfa.bastats.data.model.Formation;
import com.enseirb.pfa.bastats.data.model.FormationJoueur;
import com.enseirb.pfa.bastats.data.model.Joueur;
import com.enseirb.pfa.bastats.data.model.Match;
import com.enseirb.pfa.bastats.lancement.ActivityNouveauJoueur;
import com.enseirb.pfa.bastats.stat.VueStatsMatch;
import com.enseirb.pfa.bastats.tournoi.KeyTable;
import com.enseirb.pfa.bastats.tournoi.SelectTeamTableau;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalendrierTableauFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalendrierTableauFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendrierTableauFragment extends Fragment {
    public static final String TAG = "CalTableau";

    private ListView mListeView;
    private ArrayList<HashMap<String,String>> mListeMatch;
    private SimpleAdapter mAdapter;
    private List<Match> mCalendrierMatch;

    private boolean isFinish;

    private static final int MATCH_REQUEST = 1;
    private static final int DEFINE_TEAMS_REQUEST = 2;

    private static final String ARG_MATCH_LIST = "liste";
    private ArrayList<Integer> mMatchIds;
    private String mTitle;
    private int tournoiId;
    private static final String ARG_TITLE = "title";
    private static final String ARG_TOURNOI_ID = "tournoiId";

    private OnFragmentInteractionListener mListener;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private TextView viewTitle;

    private ArrayList<Integer> selectionInterne;

    // Artifice pour sélectionner correctement la liste des équipes:
    private ArrayList<Integer> listeQualifier;

    public void setSelectionInterne(ArrayList<Integer> selectionInterne) {
        this.selectionInterne = selectionInterne;
    }

    public String getmTitle() {
        return mTitle;
    }


    public interface OnFragmentInteractionListener {
        public void onNextPoule();
        public void onPreviousPoule();
    }

    public void next() {
        if (mListener != null) {
            mListener.onNextPoule();
        }
    }

    public void previous() {
        if (mListener != null) {
            mListener.onPreviousPoule();
        }
    }

    public static CalendrierTableauFragment newInstance(String title, ArrayList<Integer> mListMatch,
                                                        int tournoiId) {
        CalendrierTableauFragment fragment = new CalendrierTableauFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TOURNOI_ID, tournoiId);
        args.putIntegerArrayList(ARG_MATCH_LIST, mListMatch);
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    public CalendrierTableauFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCalendrierMatch = new ArrayList<>();
        if (getArguments() != null) {
            mMatchIds = getArguments().getIntegerArrayList(ARG_MATCH_LIST);
            DBMatchDAO tableMatch = new DBMatchDAO(getActivity());
            for (int id : mMatchIds){
                mCalendrierMatch.add(new Match(tableMatch.getWithId(id)));
            }
            mTitle = getArguments().getString(ARG_TITLE);
            tournoiId = getArguments().getInt(ARG_TOURNOI_ID);
        }
        initTeamsList();
        if (isFinish()){
            isFinish = true;
            Log.d(TAG, "isFinsh: "+isFinish);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendrier_tableau, container, false);
        // Inflate the layout for this fragment
        viewTitle = (TextView) view.findViewById(R.id.nomPhaseTableau);
        viewTitle.setText(mTitle);

        mListeView = (ListView) view.findViewById(R.id.liste_match_journee);

        mListeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mCalendrierMatch.get(position).getFormationEquipeA() == Match.NO_ID
                        && mCalendrierMatch.get(position).getFormationEquipeB() == Match.NO_ID){
                    Intent i = new Intent(getActivity(), SelectTeamTableau.class);
                    i.putExtra(KeyTable.MATCH_ID, mCalendrierMatch.get(position).getId());
                    i.putExtra(KeyTable.ARG_ID_TOURNOI, tournoiId);
                    i.putExtra(KeyTable.LIBELLE, mCalendrierMatch.get(position).getLibelle());
                    i.putExtra(KeyTable.LIST_EQUIPE_ID, selectionInterne);
                    getActivity().startActivityForResult(i, DEFINE_TEAMS_REQUEST);
                } else if (mCalendrierMatch.get(position).getResultat() != Match.MATCH_NON_JOUE) {
                    Toast.makeText(getActivity(), "Match sélectionné", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getActivity(), VueStatsMatch.class);
                    i.putExtra(KeyTable.MATCH_ID, mCalendrierMatch.get(position).getId());
                    startActivity(i);
                } else {
                    Toast.makeText(getActivity(), "Erreur", Toast.LENGTH_LONG).show();
                }
            }
        });

        // TODO onLongClick Supprimer le match donc ajouter création nouveau match

        mListeMatch = new ArrayList<HashMap<String, String>>();
        mAdapter = new SimpleAdapter(getActivity(), mListeMatch,R.layout.row_calendrier_match_tableau,
                new String[]{"libelle","date","equipeA","scoreA","scoreB","equipeB","button"},
                new int[]{R.id.libelle,R.id.date_match, R.id.equipe_A, R.id.score_A, R.id.score_B,
                        R.id.equipe_B,R.id.state_button}) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                final int index = position;
                final Button button = (Button) view.findViewById(R.id.state_button);
                final LinearLayout modif = (LinearLayout) view.findViewById(R.id.modifier_selection_tableau);

                modif.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "Modification", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getActivity(), SelectTeamTableau.class);
                        i.putExtra(KeyTable.MATCH_ID, mCalendrierMatch.get(index).getId());
                        i.putExtra(KeyTable.ARG_ID_TOURNOI, tournoiId);
                        i.putExtra(KeyTable.LIBELLE, mCalendrierMatch.get(index).getLibelle());
                        DBTableauEquipeDAO tableTableauEquipe = new DBTableauEquipeDAO(getActivity());
                        ArrayList<Integer> tmp = new ArrayList<>();
                        tmp.clear();
                        tmp.addAll(tableTableauEquipe.getTeamsList(tournoiId));
                        i.putExtra(KeyTable.LIST_EQUIPE_ID, tmp);
                        getActivity().startActivityForResult(i, DEFINE_TEAMS_REQUEST);
                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), ChoixEquipes.class);
                        i.putExtra(KeyTable.MATCH_ID, mCalendrierMatch.get(index).getId());
                        Log.d("CAL","Match id "+mCalendrierMatch.get(index).getId());
                        DBFormationDAO tableFormation = new DBFormationDAO(getActivity());
                        i.putExtra(KeyTable.ID_EQUIPE_A,
                                tableFormation.getWithId(mCalendrierMatch.get(index).getFormationEquipeA())
                                        .getEquipeId());
                        i.putExtra(KeyTable.ID_EQUIPE_B, tableFormation.getWithId(mCalendrierMatch.get(index).getFormationEquipeB())
                                .getEquipeId());
                        i.putExtra(KeyTable.INTENT_RECEIVE, KeyTable.FROM_CALENDRIER);
                        getActivity().startActivityForResult(i, MATCH_REQUEST);
                    }
                });
                final TextView statut = (TextView) view.findViewById(R.id.state_game);
                final TextView tableau = (TextView) view.findViewById(R.id.define_teams);
                if (mCalendrierMatch.get(index).getFormationEquipeA() == Match.NO_ID
                        && mCalendrierMatch.get(index).getFormationEquipeB() == Match.NO_ID){
                    modif.setClickable(false);
                    button.setVisibility(Button.INVISIBLE);
                    statut.setVisibility(TextView.INVISIBLE);
                    tableau.setVisibility(TextView.VISIBLE);
                } else if (mCalendrierMatch.get(index).getResultat() == Match.MATCH_NON_JOUE) {
                    modif.setClickable(true);
                    button.setVisibility(Button.VISIBLE);
                    statut.setVisibility(TextView.INVISIBLE);
                    tableau.setVisibility(TextView.INVISIBLE);
                } else {
                    modif.setClickable(false);
                    button.setVisibility(Button.INVISIBLE);
                    statut.setVisibility(TextView.VISIBLE);
                    tableau.setVisibility(TextView.INVISIBLE);
                }

                return view;
            }
        };

        remplirCalendrier();
        mListeView.setAdapter(mAdapter);

        nextButton = (ImageButton) view.findViewById(R.id.next_poule);
        prevButton = (ImageButton) view.findViewById(R.id.previous_poule);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previous();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void remplirCalendrier(){
        for (Match match : mCalendrierMatch) {
            DBEquipeDAO tableEquipe = new DBEquipeDAO(getActivity());
            DBFormationDAO tableFormation = new DBFormationDAO(getActivity());
            Equipe equipeA = new Equipe();
            Equipe equipeB = new Equipe();
            if (match.getFormationEquipeA() != Match.NO_ID) {
                equipeA = new Equipe(tableEquipe.getWithId(
                        tableFormation.getWithId(match.getFormationEquipeA()).getEquipeId()));
            }
            if (match.getFormationEquipeB() != Match.NO_ID) {
                equipeB = new Equipe(tableEquipe.getWithId(
                        tableFormation.getWithId(match.getFormationEquipeB()).getEquipeId()));
            }

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("libelle",match.getLibelle());
            map.put("date", match.getDate());
            map.put("equipeA", equipeA.getNom());
            map.put("scoreA", String.valueOf(match.getScoreEquipeA()));
            map.put("scoreB", String.valueOf(match.getScoreEquipeB()));
            map.put("equipeB", equipeB.getNom());

            mListeMatch.add(map);
            mAdapter.notifyDataSetChanged();
        }
    }

    public boolean isFinish(){
        for (Match match : mCalendrierMatch){
            //Log.d(TAG, match.getLibelle()+ " etat "+ match.getResultat());
            if (match.getResultat() == Match.MATCH_NON_JOUE)
                return false;
        }
        return true;
    }

    /*
     * Retourne la liste des id des équipes ayant gagné leur match
     */
    public ArrayList<Integer> getWinners(){
        ArrayList<Integer> winners = new ArrayList<>();
        for (Match match : mCalendrierMatch){
            winners.add(match.getResultat());
        }
        Log.d(TAG, "vainqueurs:"+winners.toString());
        return winners;
    }

    /*
     * Retourne la liste des id des équipes ayant perdu leur match
     */
    public ArrayList<Integer> getLosers(){
        DBFormationDAO tableFormation = new DBFormationDAO(getActivity());
        ArrayList<Integer> losers = new ArrayList<>();
        for (Match match : mCalendrierMatch){
            int formationA = match.getFormationEquipeA();
            int formationB = match.getFormationEquipeB();
            int idA = tableFormation.getWithId(formationA).getEquipeId();
            int idB = tableFormation.getWithId(formationB).getEquipeId();
            int result = match.getResultat();
            if (result == idA)
                losers.add(idB);
            else if (result == idB)
                losers.add(idA);
            else {
                // CAS nul qui ne devrait pas se produire
                losers.add(idB);
                losers.add(idA);
            }
        }
        Log.d(TAG, "perdants:"+losers.toString());
        return losers;
    }

    /*
     * Update la vue après qu'un match ait été joué
     */
    public void update(){
        Log.d(TAG, "call update method");
        DBMatchDAO tableMatch = new DBMatchDAO(getActivity());
        for (int i=0; i < mCalendrierMatch.size(); i++){
            Log.d(TAG, "match avant: "+mCalendrierMatch.get(i));
            int id = mCalendrierMatch.get(i).getId();
            mCalendrierMatch.set(i, new Match(tableMatch.getWithId(id)));
            Log.d(TAG, "match après: "+mCalendrierMatch.get(i));
        }
        mListeMatch.clear();
        remplirCalendrier();
        updateTeamsList();
        if (isFinish()){
            isFinish = true;
            Log.d(TAG, "isFinsh: "+isFinish);
        }
    }

    private void initTeamsList(){
        DBTableauEquipeDAO tableTableauEquipe = new DBTableauEquipeDAO(getActivity());
        selectionInterne = new ArrayList<>();
        selectionInterne.clear();
        selectionInterne.addAll(tableTableauEquipe.getTeamsList(tournoiId));
        updateTeamsList();
    }

    private void updateTeamsList(){
        Integer equipeAId = null;
        Integer equipeBId = null;
        DBFormationDAO tableFormation = new DBFormationDAO(getActivity());
        for (int i=0; i < mCalendrierMatch.size(); i++){
            Log.d(TAG, "eqA id"+ mCalendrierMatch.get(i).getFormationEquipeA());
            Log.d(TAG, "eqB id"+ mCalendrierMatch.get(i).getFormationEquipeB());
            if (mCalendrierMatch.get(i).getFormationEquipeA() != Match.NO_ID)
            equipeAId = tableFormation.getWithId(mCalendrierMatch.get(i).getFormationEquipeA())
                    .getEquipeId();
            if (mCalendrierMatch.get(i).getFormationEquipeB() != Match.NO_ID)
            equipeBId = tableFormation.getWithId(mCalendrierMatch.get(i).getFormationEquipeB())
                    .getEquipeId();

            if (selectionInterne.contains(equipeAId)) {
                Log.d(TAG, selectionInterne.toString());
                selectionInterne.remove(equipeAId);
                Log.d(TAG, selectionInterne.toString());
            }else if (selectionInterne.contains(equipeBId)){
                Log.d(TAG, selectionInterne.toString());
                selectionInterne.remove(equipeBId);
                Log.d(TAG, selectionInterne.toString());
            }
        }
    }
}
