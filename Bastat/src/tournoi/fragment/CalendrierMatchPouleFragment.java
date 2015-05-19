package com.enseirb.pfa.bastats.tournoi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.enseirb.pfa.bastats.R;
import com.enseirb.pfa.bastats.accueil.ChoixEquipes;
import com.enseirb.pfa.bastats.data.DAO.DBEquipeDAO;
import com.enseirb.pfa.bastats.data.DAO.DBFormationDAO;
import com.enseirb.pfa.bastats.data.DAO.DBFormationJoueurDAO;
import com.enseirb.pfa.bastats.data.DAO.DBJoueurDAO;
import com.enseirb.pfa.bastats.data.DAO.DBMatchDAO;
import com.enseirb.pfa.bastats.data.DAO.DBPhasePouleDAO;
import com.enseirb.pfa.bastats.data.DAO.DBPouleDAO;
import com.enseirb.pfa.bastats.data.DAO.DBPouleMatchDAO;
import com.enseirb.pfa.bastats.data.DAO.DBStatDAO;
import com.enseirb.pfa.bastats.data.model.Equipe;
import com.enseirb.pfa.bastats.data.model.FormationJoueur;
import com.enseirb.pfa.bastats.data.model.Joueur;
import com.enseirb.pfa.bastats.data.model.Match;
import com.enseirb.pfa.bastats.data.model.Poule;
import com.enseirb.pfa.bastats.lancement.ActivityNouveauJoueur;
import com.enseirb.pfa.bastats.stat.VueStatsMatch;
import com.enseirb.pfa.bastats.tournoi.KeyTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CalendrierMatchPouleFragment extends Fragment {
    private ArrayList<HashMap<String,String>> mListeMatch;
    private SimpleAdapter mAdapter;
    private ListView mListeView;
    private static final int MATCH_REQUEST = 1;

    private static final String ARG_POULE_ID = "poule_id";
    private int pouleId;


    private List<Match> mCalendrierMatch;

    public static CalendrierMatchPouleFragment newInstance(Poule poule) {
        CalendrierMatchPouleFragment fragment = new CalendrierMatchPouleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POULE_ID, poule.getId());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pouleId = getArguments().getInt(ARG_POULE_ID);
        }

        getAllGames();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calendrier_match_poule, container, false);
        // Inflate the layout for this fragment
        mListeView = (ListView) view.findViewById(R.id.liste_match_journee);

        mListeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),"Match sélectionné pour stats: "
                        +mCalendrierMatch.get(position).getId(),Toast.LENGTH_LONG).show();
                //statRequest(mCalendrierMatch.get(position).getId());
                Intent i = new Intent(getActivity(), VueStatsMatch.class);
                i.putExtra(KeyTable.MATCH_ID, mCalendrierMatch.get(position).getId());
                startActivity(i);
            }
        });

        mListeMatch = new ArrayList<HashMap<String, String>>();
        mAdapter = new SimpleAdapter(getActivity(), mListeMatch,R.layout.row_calendrier_match_poule,
                new String[]{"date","equipeA","scoreA","scoreB","equipeB","button"},
                new int[]{R.id.date_match, R.id.equipe_A, R.id.score_A, R.id.score_B,R.id.equipe_B,R.id.state_button}) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                final int index = position;
                final Button button = (Button) view.findViewById(R.id.state_button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DBPouleDAO tablePoule = new DBPouleDAO(getActivity());
                        DBPhasePouleDAO tablePhasePoule = new DBPhasePouleDAO(getActivity());
                        int phasePouleId = tablePoule.getWithId(pouleId).getPhasePouleId();
                        String duree = tablePhasePoule.getWithId(phasePouleId).getDureePeriodeMatch();
                        int nbPeriodes = tablePhasePoule.getWithId(phasePouleId).getNbPeriodeMatch();
                        Log.d("CAL","duree match dans la poule: "+duree+" pour "+nbPeriodes+" périodes");
                        Intent i = new Intent(getActivity(), ChoixEquipes.class);
                        i.putExtra(KeyTable.MATCH_ID, mCalendrierMatch.get(index).getId());
                        Log.d("CAL","Match id "+mCalendrierMatch.get(index).getId());
                        DBFormationDAO tableFormation = new DBFormationDAO(getActivity());
                        i.putExtra(KeyTable.ID_EQUIPE_A,
                                tableFormation.getWithId(mCalendrierMatch.get(index).getFormationEquipeA())
                                        .getEquipeId());
                        i.putExtra(KeyTable.ID_EQUIPE_B, tableFormation.getWithId(mCalendrierMatch.get(index).getFormationEquipeB())
                                .getEquipeId());
                        i.putExtra(KeyTable.ARG_NB_PERIODES, nbPeriodes);
                        i.putExtra(KeyTable.DUREE_PERIODE, duree);
                        i.putExtra(KeyTable.INTENT_RECEIVE, KeyTable.FROM_CALENDRIER);
                        getActivity().startActivityForResult(i, MATCH_REQUEST);
                    }
                });
                final TextView statut = (TextView) view.findViewById(R.id.state_game);
                if (mCalendrierMatch.get(index).getResultat() == Match.MATCH_NON_JOUE) {
                    button.setVisibility(Button.VISIBLE);
                    statut.setVisibility(TextView.INVISIBLE);
                }
                else {
                    button.setVisibility(Button.INVISIBLE);
                    statut.setVisibility(TextView.VISIBLE);
                }

                return view;
            }
        };

        remplirCalendrier();
        mListeView.setAdapter(mAdapter);

        return view;
    }

    public void remplirCalendrier(){
        for (Match match : mCalendrierMatch) {
            DBEquipeDAO tableEquipe = new DBEquipeDAO(getActivity());
            DBFormationDAO tableFormation = new DBFormationDAO(getActivity());
            Equipe equipeA = new Equipe(tableEquipe.getWithId(
                    tableFormation.getWithId(match.getFormationEquipeA()).getEquipeId()));
            Equipe equipeB = new Equipe(tableEquipe.getWithId(
                    tableFormation.getWithId(match.getFormationEquipeB()).getEquipeId()));

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("date", match.getDate());
            map.put("equipeA", equipeA.getNom());
            map.put("scoreA", String.valueOf(match.getScoreEquipeA()));
            map.put("scoreB", String.valueOf(match.getScoreEquipeB()));
            map.put("equipeB", equipeB.getNom());

            mListeMatch.add(map);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void getAllGames(){
        DBPouleMatchDAO tablePouleMatch = new DBPouleMatchDAO(getActivity());
        DBMatchDAO tableMatch = new DBMatchDAO(getActivity());
        List<Integer> matchIds = new ArrayList<>(tablePouleMatch.getMatchIdsFromPoule(pouleId));
        mCalendrierMatch = new ArrayList<>(tableMatch.getMatchPoule(pouleId));
        Log.d("FragmentCalendrier", mCalendrierMatch.toString());

    }

    public void update(){
        mListeMatch.clear();
        mCalendrierMatch.clear();
        getAllGames();
        remplirCalendrier();
    }

    public boolean isFinish(){
        for (Match match : mCalendrierMatch){
            if (match.getResultat() == Match.MATCH_NON_JOUE)
                return false;
        }
        return true;
    }

    private void statRequest(int matchId){
        DBStatDAO stats = new DBStatDAO(getActivity());
        DBMatchDAO tableMatch = new DBMatchDAO(getActivity());
        DBJoueurDAO tableJoueur = new DBJoueurDAO(getActivity());
        Match match = tableMatch.getWithId(matchId);
        DBFormationJoueurDAO tableFormationJoueur = new DBFormationJoueurDAO(getActivity());
        ArrayList<Joueur> joueursA = new ArrayList<>();
        ArrayList<Joueur> joueursB = new ArrayList<>();

        ArrayList<FormationJoueur> fjA
                = new ArrayList<>(tableFormationJoueur.getWithId(match.getFormationEquipeA()));
        ArrayList<FormationJoueur> fjB
                = new ArrayList<>(tableFormationJoueur.getWithId(match.getFormationEquipeB()));

        for (FormationJoueur formationJoueur : fjA){
            joueursA.add(new Joueur(tableJoueur.getWithId(formationJoueur.getJoueurId())));
        }

        for (FormationJoueur formationJoueur : fjB){
            joueursB.add(new Joueur(tableJoueur.getWithId(formationJoueur.getJoueurId())));
        }

        for (Joueur joueur : joueursA)
            stats.getStatFromJoueur(joueur,matchId);

        for (Joueur joueur : joueursB)
            stats.getStatFromJoueur(joueur,matchId);
    }
}
