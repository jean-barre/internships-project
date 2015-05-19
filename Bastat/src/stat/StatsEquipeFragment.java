package com.enseirb.pfa.bastats.stat;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.enseirb.pfa.bastats.R;
import com.enseirb.pfa.bastats.data.DAO.DBEquipeDAO;
import com.enseirb.pfa.bastats.data.DAO.DBFormationDAO;
import com.enseirb.pfa.bastats.data.DAO.DBJoueurDAO;
import com.enseirb.pfa.bastats.data.DAO.DBStatDAO;
import com.enseirb.pfa.bastats.data.model.Joueur;
import com.enseirb.pfa.bastats.data.model.Stat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class StatsEquipeFragment extends Fragment {
    private int formationId=-1;
    private int matchId=-1;
    private String nomEquipe;
    private static final String FORMATION_ID = "formationId";
    private static final String MATCH_ID = "matchId";

    private int[] colors = new int[]{Color.parseColor("#F0F0F0"), Color.parseColor("#d7d7d7")};

    private static final String JOUEUR = "joueur";
    private static final String PTS = "pts";
    private static final String TIR = "tir";
    private static final String PTS_2 = "2pts";
    private static final String PTS_3 = "3pts";
    private static final String CONTRE = "contre";
    private static final String TURNOVER = "turnover";
    private static final String RB_OFF = "rb_off";
    private static final String RB_DEF = "rd_def";
    private static final String RB_TOT = "rb_tot";
    private static final String FAUTES = "fautes";
    private static final String LF = "lf";
    private static final String INTERCEPTION = "int";
    private static final String PASSE_DEC = "p_dec";
    private static final String EVALUATION = "eval";

    private List<Joueur> joueurs;
    private List<Stat> stats;
    private List<HashMap<String,String>> mapStats;
    private Context mCtx;

    private Stat statsEquipe = new Stat();

    public static StatsEquipeFragment newInstance(int id, int matchId) {
        StatsEquipeFragment fragment = new StatsEquipeFragment();
        Bundle args = new Bundle();
        args.putInt(FORMATION_ID, id);
        args.putInt(MATCH_ID, matchId);
        fragment.setArguments(args);
        return fragment;
    }

    public StatsEquipeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            formationId = getArguments().getInt(FORMATION_ID);
            matchId = getArguments().getInt(MATCH_ID);
        }
        mCtx = getActivity();
        DBJoueurDAO joueurDAO = new DBJoueurDAO(mCtx);
        joueurs = new ArrayList<>(joueurDAO.getFormationMatch(formationId));
        DBStatDAO statDAO = new DBStatDAO(mCtx);
        stats = new ArrayList<>();
        if (joueurs.isEmpty())
            stats.clear();
        else {
            Iterator<Joueur> i = joueurs.iterator();
            do {
                Joueur joueur = i.next();
                stats.add(statDAO.getStatFromJoueur(joueur,matchId));
            } while (i.hasNext());
        }

        mapStats = new ArrayList<HashMap<String,String>>();
        fillStats(stats,mapStats);
        DBFormationDAO tableFormation = new DBFormationDAO(mCtx);
        DBEquipeDAO tableEquipe = new DBEquipeDAO(mCtx);
        if (formationId != -1)
            nomEquipe = tableEquipe.getWithId(tableFormation.getWithId(formationId).getEquipeId()).getNom();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stats_equipe_v2, container, false);
        // Keys used in Hashmap
       /* String[] from = { "joueurs" , "tirs" , "p3G" , "p2G" , "p3P" , "p2P" , "lfG" , "lfP"
                , "rebounds" , "turnovers" , "assists" , "steals" , "contres" , "dFouls" , "oFouls"};

        // Ids of views in listview_layout
        int[] to = { R.id.item_statistics_joueur,R.id.item_statistics_tirs,R.id.item_statistics_3pG,
                R.id.item_statistics_2pG,R.id.item_statistics_3pP,R.id.item_statistics_2pP,
                R.id.item_statistics_lancerfG,R.id.item_statistics_lancerfP,
                R.id.item_statistics_rebounds,R.id.item_statistics_pertes,R.id.item_statistics_assists,
                R.id.item_statistics_steals, R.id.item_statistics_contres,R.id.item_statistics_fautesD,
                R.id.item_statistics_fautesO};*/
        String[] from = {JOUEUR, PTS, TIR, PTS_2, PTS_3, LF,RB_OFF, RB_DEF, RB_TOT, CONTRE, PASSE_DEC,
            INTERCEPTION, TURNOVER, FAUTES, EVALUATION};

        int[] to = {R.id.item_statistics_joueur,R.id.item_statistics_pts, R.id.item_statistics_tirs,
            R.id.item_statistics_2pts, R.id.item_statistics_3pts, R.id.item_statistics_lf,
            R.id.item_statistics_rb_off,R.id.item_statistics_rb_def, R.id.item_statistics_rb_tot,
            R.id.item_statistics_contres, R.id.item_statistics_passe_dec, R.id.item_statistics_interception,
            R.id.item_statistics_turnover, R.id.item_statistics_fautes, R.id.item_statistics_evaluation};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(mCtx, mapStats,
                R.layout.item_listview_stats_v2, from, to) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                int colorPos = position % colors.length;
                view.setBackgroundColor(colors[colorPos]);
                return view;
            }
        };
        remplirTotalEquipe(view);
        ListView lStats = (ListView) view.findViewById(R.id.resultasts_stats_listview);
        lStats.setAdapter(adapter);
        TextView equipe = (TextView) view.findViewById(R.id.nom_equipe);
        equipe.setText(nomEquipe);
        return view;

    }

    private void fillStats(List<Stat> from, List<HashMap<String,String>> to){
        for(Stat stat : from){
            HashMap<String,String> hm = new HashMap<String,String>();
            Log.d("TAG", stat.toString());
            hm.put(JOUEUR,stat.getNomJoueur());
            hm.put(PTS,String.valueOf(calculerPts(stat)));
            hm.put(PTS_2,stat.getNbShoot2S()+"/"+(stat.getNbShoot2S()+stat.getNbShoot2F()));
            hm.put(PTS_3,stat.getNbShoot3S()+"/"+(stat.getNbShoot3S()+stat.getNbShoot3F()));
            hm.put(LF,stat.getNbLfS()+"/"+(stat.getNbLfS()+stat.getNbLfF()));
            hm.put(RB_OFF,String.valueOf(stat.getNbRebondOff()));
            hm.put(RB_DEF,String.valueOf(stat.getNbRebondDef()));
            hm.put(RB_TOT,String.valueOf(stat.getNbRebondOff()+stat.getNbRebondDef()));
            hm.put(TURNOVER,String.valueOf(stat.getNbPerteBalle()));
            hm.put(PASSE_DEC,String.valueOf(stat.getNbPasseDecisive()));
            hm.put(INTERCEPTION,String.valueOf(stat.getNbInterception()));
            hm.put(CONTRE,String.valueOf(stat.getNbContre()));
            hm.put(FAUTES,String.valueOf(stat.getNbFautes()));
            hm.put(TIR, calculerTirsReussi(stat)+"/"+(calculerTirsReussi(stat)+calculerTirsRate(stat)));
            hm.put(EVALUATION, String.valueOf(calculerEvaluation(stat)));
            ajoutTotalEquipe(stat);
            to.add(hm);
        }
    }

    private int calculerPts(Stat stat) {
        return stat.getNbShoot2S()*2+stat.getNbShoot3S()*3+stat.getNbLfS();
    }

    private int calculerTirsReussi(Stat stat){
        return stat.getNbShoot2S()+stat.getNbShoot3S()+stat.getNbLfS();
    }

    private int calculerTirsRate(Stat stat){
        return stat.getNbShoot2F()+stat.getNbShoot3F()+stat.getNbLfF();
    }

    private int calculerEvaluation(Stat stat){
        return calculerPts(stat)+(stat.getNbRebondOff()+stat.getNbRebondDef())
                +stat.getNbPasseDecisive()+stat.getNbInterception()+stat.getNbContre()
                -stat.getNbShoot2F()-stat.getNbShoot3F()-stat.getNbLfF()-stat.getNbPerteBalle();
    }

    private void ajoutTotalEquipe(Stat stat){
        statsEquipe.setNbContre(statsEquipe.getNbContre() + stat.getNbContre());
        statsEquipe.setNbFautes(statsEquipe.getNbFautes() + stat.getNbFautes());
        statsEquipe.setNbRebondOff(statsEquipe.getNbRebondOff() + stat.getNbRebondOff());
        statsEquipe.setNbRebondDef(statsEquipe.getNbRebondDef() + stat.getNbRebondDef());
        statsEquipe.setNbPasseDecisive(statsEquipe.getNbPasseDecisive() + stat.getNbPasseDecisive());
        statsEquipe.setNbInterception(statsEquipe.getNbInterception() + stat.getNbInterception());
        statsEquipe.setNbPerteBalle(statsEquipe.getNbPerteBalle()+ stat.getNbPerteBalle());
        statsEquipe.setNbShoot2S(statsEquipe.getNbShoot2S() + stat.getNbShoot2S());
        statsEquipe.setNbShoot2F(statsEquipe.getNbShoot3F() + stat.getNbShoot2F());
        statsEquipe.setNbShoot3S(statsEquipe.getNbShoot3S() + stat.getNbShoot3S());
        statsEquipe.setNbShoot3F(statsEquipe.getNbShoot3F() + stat.getNbShoot3F());
        statsEquipe.setNbLfS(statsEquipe.getNbLfS() + stat.getNbLfS());
        statsEquipe.setNbLfF(statsEquipe.getNbLfF() + stat.getNbLfF());
    }

    private void remplirTotalEquipe(View v){
        TextView tir = (TextView) v.findViewById(R.id.tirEquipe);
        tir.setText(calculerTirsReussi(statsEquipe)+"/"+(calculerTirsRate(statsEquipe)+calculerTirsReussi(statsEquipe)));
        TextView pts2 = (TextView) v.findViewById(R.id.deuxPtsEquipe);
        pts2.setText(statsEquipe.getNbShoot2S()+"/"+(statsEquipe.getNbShoot2S()+statsEquipe.getNbShoot2F()));
        TextView pts3 = (TextView) v.findViewById(R.id.troisPtsEquipe);
        pts3.setText(statsEquipe.getNbShoot3S()+"/"+(statsEquipe.getNbShoot3S()+statsEquipe.getNbShoot3F()));
        TextView pts = (TextView) v.findViewById(R.id.ptsTotEquipe);
        pts.setText(Integer.toString(calculerPts(statsEquipe)));
        TextView contre = (TextView) v.findViewById(R.id.ctrEquipe);
        contre.setText(Integer.toString(statsEquipe.getNbContre()));
        TextView interception = (TextView) v.findViewById(R.id.interceptionEquipe);
        interception.setText(Integer.toString(statsEquipe.getNbInterception()));
        TextView pdec = (TextView) v.findViewById(R.id.pdecEquipe);
        pdec.setText(Integer.toString(statsEquipe.getNbPasseDecisive()));
        TextView bp = (TextView) v.findViewById(R.id.pertBalleEquipe);
        bp.setText(Integer.toString(statsEquipe.getNbPerteBalle()));
        TextView fautes = (TextView) v.findViewById(R.id.fautesEquipe);
        fautes.setText(Integer.toString(statsEquipe.getNbFautes()));
        TextView lf = (TextView) v.findViewById(R.id.lfEquipe);
        lf.setText(statsEquipe.getNbLfS()+"/"+(statsEquipe.getNbLfS()+statsEquipe.getNbLfF()));
        TextView rbOff = (TextView) v.findViewById(R.id.rbOffEquipe);
        rbOff.setText(Integer.toString(statsEquipe.getNbRebondOff()));
        TextView rbDef = (TextView) v.findViewById(R.id.rbDefEquipe);
        rbDef.setText(Integer.toString(statsEquipe.getNbRebondDef()));
        TextView rbTot = (TextView) v.findViewById(R.id.rbTotEquipe);
        rbTot.setText(Integer.toString(statsEquipe.getNbRebondDef()+statsEquipe.getNbRebondOff()));
    }

}
