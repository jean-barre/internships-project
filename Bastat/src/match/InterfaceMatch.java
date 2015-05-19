package com.enseirb.pfa.bastats.match;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TextView;

import com.enseirb.pfa.bastats.R;
import com.enseirb.pfa.bastats.accueil.VerificationDB;
import com.enseirb.pfa.bastats.data.DAO.DBEquipeDAO;
import com.enseirb.pfa.bastats.data.DAO.DBFormationDAO;
import com.enseirb.pfa.bastats.data.DAO.DBFormationJoueurDAO;
import com.enseirb.pfa.bastats.data.DAO.DBJoueurDAO;
import com.enseirb.pfa.bastats.data.DAO.DBMatchDAO;
import com.enseirb.pfa.bastats.data.DAO.DBStatDAO;
import com.enseirb.pfa.bastats.data.DAO.DBTempsDeJeuDAO;
import com.enseirb.pfa.bastats.data.DAO.action.DBActionDAO;
import com.enseirb.pfa.bastats.data.DAO.action.DBContreDAO;
import com.enseirb.pfa.bastats.data.DAO.action.DBFauteDAO;
import com.enseirb.pfa.bastats.data.DAO.action.DBInterceptionDAO;
import com.enseirb.pfa.bastats.data.DAO.action.DBPasseDAO;
import com.enseirb.pfa.bastats.data.DAO.action.DBPerteBalleDAO;
import com.enseirb.pfa.bastats.data.DAO.action.DBRebondDAO;
import com.enseirb.pfa.bastats.data.DAO.action.DBShootDAO;
import com.enseirb.pfa.bastats.data.model.FormationJoueur;
import com.enseirb.pfa.bastats.data.model.Match;
import com.enseirb.pfa.bastats.data.model.TempsDeJeu;
import com.enseirb.pfa.bastats.data.model.action.Contre;
import com.enseirb.pfa.bastats.data.model.action.Faute;
import com.enseirb.pfa.bastats.data.model.action.Interception;
import com.enseirb.pfa.bastats.data.model.Joueur;
import com.enseirb.pfa.bastats.data.model.action.Passe;
import com.enseirb.pfa.bastats.data.model.action.PerteBalle;
import com.enseirb.pfa.bastats.data.model.action.Rebond;
import com.enseirb.pfa.bastats.data.model.action.Shoot;
import com.enseirb.pfa.bastats.stat.VueStatsMatch;
import com.enseirb.pfa.bastats.tournoi.KeyTable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class InterfaceMatch extends ActionBarActivity implements FragmentMenuMatch.menuMatch, FragmentEquipeMatch.EquipeMatch, FragmentAction2Clicks.Action2licks{
    private Context mCtx;
    private String nomEquipeA;
    private String nomEquipeB;

    private int scoreEquipeA = 0;
    private int scoreEquipeB = 0;
    private FragmentMenuMatch fragmentMenu;
    private FragmentEquipeMatch fragmentEquipeA;
    private FragmentEquipeMatch fragmentEquipeB;
    private FragmentAction2Clicks fragmentAction2Clicks;

    int equipeOff;

    private DBEquipeDAO tableEquipe;
    private DBJoueurDAO tableJoueur;
    private DBShootDAO tableShoot;
    private DBContreDAO tableContre;
    private DBFauteDAO tableFaute;
    private DBInterceptionDAO tableInterception;
    private DBTempsDeJeuDAO tableTempsDeJeu;
    private DBRebondDAO tableRebond;
    private DBPasseDAO tablePasse;
    private DBPerteBalleDAO tablePerteBalle;
    private DBActionDAO tableAction;
    private DBMatchDAO tableMatch;

    private int idA;
    private int idB;

    private int matchId;

    private Boolean oneClick;

    private ListView mHistoriqueListView;
    private ArrayAdapter<String> mHistoriqueListAdapter;
    private List<String> mHistorique;
    private ArrayList<HashMap<String,String>> listeh;
    private SimpleAdapter adaptH;

    private int nbPeriodes;
    private String dureePeriode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interface_match);

        mCtx = this;
        fragmentMenu = (FragmentMenuMatch) getFragmentManager().findFragmentById(R.id.menuMatch);
        fragmentEquipeA = (FragmentEquipeMatch) getFragmentManager().findFragmentById(R.id.EquipeA);
        fragmentEquipeB = (FragmentEquipeMatch) getFragmentManager().findFragmentById(R.id.EquipeB);
        oneClick=false;
        initDAO(mCtx);
        initHistorique();
        Intent intent = getIntent();
        initTeamLayouts(intent);
        nbPeriodes = intent.getIntExtra(KeyTable.ARG_NB_PERIODES,4);
        dureePeriode = intent.getStringExtra(KeyTable.DUREE_PERIODE);
        Log.d("Interface", "Match de "+nbPeriodes+" périodes de "+dureePeriode+" chacune");

    }

    public void tir3Click() {
        // Perform action on click
        if (!oneClick) {
        TempsDeJeu tps = new TempsDeJeu(matchId,-1, ChronoFragment.getTime(), -1, -1);
        tableTempsDeJeu.insert(tps);
        // Insertion bdd
        onSelectedActions(tps.getChronometre() + "\ttir à 3 pts réussi de\t","tir3");
        // actions

            if (fragmentEquipeA.getEquipeOff()) {
                scoreEquipeA += 3;
            } else {
                scoreEquipeB += 3;
            }
            updateScore();
            oneClick=true;
        }


    }

    public void tir3rClick() {
        // Perform action on click
        if (!oneClick) {
            TempsDeJeu tps = new TempsDeJeu(matchId, -1, ChronoFragment.getTime(), -1, -1);
            tableTempsDeJeu.insert(tps);
            // Insertion bdd
            onSelectedActions(tps.getChronometre() + "\ttir à 3pts manqué de\t", "tir3r");
            oneClick=true;
        }
        // actions
    }

    public void lancerFrancRClick() {
        if (!oneClick) {
            // Perform action on click
            TempsDeJeu tps = new TempsDeJeu(matchId, -1, ChronoFragment.getTime(), -1, -1);
            tableTempsDeJeu.insert(tps);
            // Insertion bdd
            onSelectedActions(tps.getChronometre() + "\tlancer franc manqué de\t", "lfr");
          oneClick=true;
        }
        // actions
    }

    public void lancerFrancClick() {
        if (!oneClick) {
            // Perform action on click
            TempsDeJeu tps = new TempsDeJeu(matchId, -1, ChronoFragment.getTime(), -1, -1);
            tableTempsDeJeu.insert(tps);
            // Insertion bdd
            onSelectedActions(tps.getChronometre() + "\tlancer franc réussi de\t", "lf");
            // actions
            if (fragmentEquipeA.getEquipeOff()) {
                scoreEquipeA += 1;
            } else {
                scoreEquipeB += 1;
            }
            updateScore();
            oneClick=true;
        }
    }

    public void assistClick() {
        if (!oneClick) {
            // Perform action on click
            TempsDeJeu tps = new TempsDeJeu(matchId, -1, ChronoFragment.getTime(), -1, -1);
            tableTempsDeJeu.insert(tps);
            // Insertion bdd
            onSelectedActions(tps.getChronometre() + "\tpasse décisive de\t", "assist");

            oneClick=true;
        }
        // actions
    }

    public void onSelectedActions(String msg,String action){
        fragmentEquipeA.onSelectedActions(msg,action);
        fragmentEquipeB.onSelectedActions(msg,action);

    }

    public void onSelectDefenseLayout(TableLayout tl, List<Joueur> listeJoueurs, String msg){
        for (int j = 0; j < listeJoueurs.size(); j++) {
            Joueur tmp = listeJoueurs.get(j);
            //  createOnSelectedTableRow(tl, j, tmp.getNumero() + " " + tmp.getPseudo(), msg);
        }
    }

    private void initTeamLayouts(Intent intent) {
        DBFormationDAO tableFormation = new DBFormationDAO(mCtx);
        DBMatchDAO tableMatch = new DBMatchDAO(mCtx);
        DBEquipeDAO tableEquipe = new DBEquipeDAO(mCtx);

        matchId = intent.getIntExtra(KeyTable.MATCH_ID,-1);

        Match match = new Match(tableMatch.getWithId(matchId));
        int formationA = match.getFormationEquipeA();
        int formationB = match.getFormationEquipeB();
        idA = tableFormation.getWithId(formationA).getEquipeId();
        idB = tableFormation.getWithId(formationB).getEquipeId();
        fragmentMenu.setEquipeA(tableEquipe.getWithId(idA).getNom());
        fragmentMenu.setEquipeB(tableEquipe.getWithId(idB).getNom());
        Log.d("Interface", "match entre "+idA+ " (formation "+formationA
                +") et "+idB+" (formation "+formationB+")");
        Log.d("Interface", "COULEUR EQUIPES " + tableEquipe.getWithId(idA).getCouleur()
                + tableEquipe.getWithId(idB).getCouleur());

        int colorA=getRgb(tableEquipe.getWithId(idA).getCouleur());
        int colorB=getRgb(tableEquipe.getWithId(idB).getCouleur());
        fragmentEquipeA.setRgb(colorA);
        fragmentEquipeB.setRgb(colorB);
        fragmentMenu.setColorA(colorA);
        fragmentMenu.setColorB(colorB);
        fragmentEquipeA.setEquipeA(true);
        fragmentEquipeB.setEquipeOff(true);

        equipeOff = idB;

        Log.d("interface",tableJoueur.getFormationMatch(formationA).toString());
        Log.d("interface",tableJoueur.getFormationMatch(formationB).toString());
        List<Joueur> tmpA = new ArrayList<Joueur>(tableJoueur.getFormationMatch(formationA));
        List<Joueur> tmpB = new ArrayList<Joueur>(tableJoueur.getFormationMatch(formationB));
        Joueur joueurA = new Joueur(getNeutralPlayer(tmpA));
        Joueur joueurB = new Joueur(getNeutralPlayer(tmpB));
        tmpA.add(0,joueurA);
        tmpB.add(0,joueurB);
        fragmentEquipeA.setListeJoueurs(tmpA);
        fragmentEquipeB.setListeJoueurs(tmpB);

        /*fragmentEquipeA.setListeJoueurs(tableJoueur.getJoueursEquipe(idA));
        fragmentEquipeB.setListeJoueurs(tableJoueur.getJoueursEquipe(idB));*/
    }

    public int getRgb(String color) {
        int rgb=0;
        switch (color){
            case "Rouge":
                rgb= Color.rgb(255, 50, 50);
                break;
            case "Bleu":
                rgb=Color.rgb(0, 0, 255);
                break;
            case "Vert":
                rgb=Color.GREEN;
                break;
            case "Orange":
                rgb=Color.rgb(255,105,0);
                break;
            case "Jaune":
                rgb=Color.rgb(255,255,0);
                break;
            case "Marron":
                rgb=Color.rgb(115,74,18);
                break;
            case "Noir":
                rgb=Color.rgb(0,0,0);
                break;
            case "Blanc":
                rgb=Color.rgb(255,255,255);
                break;
            case "Gris":
                rgb=Color.rgb(150,150,150);
                break;
            case "Violet":
                rgb=Color.rgb(75,0,130);
                break;
            case "Rose":
                rgb=Color.rgb(239,89,123);
                break;
        }
            return rgb;
    }

    @Override
    public void tableRowAction(View v,int index,String message,String action) {
        Joueur acteur;
        if (fragmentEquipeA.getEquipeOff())
            acteur = fragmentEquipeA.getListeJoueurs().get(index);
        else
            acteur = fragmentEquipeB.getListeJoueurs().get(index);

        switch(action){
            case "tir3":
                Shoot tir = new Shoot(tableTempsDeJeu.getLast(), acteur, 3, 1);
                tableShoot.insert(tir);
                addHistorique(message + acteur.getPseudo(),"SHOOT",tableShoot.getLast().getId());
                Log.v("TAG", "Insertion shoot réussi à" + 3 + " pts");
                break;

            case "tir3r":
                tir = new Shoot(tableTempsDeJeu.getLast(), acteur, 3, 0);
                tableShoot.insert(tir);
                addHistorique(message + acteur.getPseudo(),"SHOOT",tableShoot.getLast().getId());
                Log.v("TAG", "Insertion shoot raté à" + 3 + " pts");
                break;

            case "lf":
                tir = new Shoot(tableTempsDeJeu.getLast(), acteur, 1, 1);
                tableShoot.insert(tir);
                addHistorique(message + acteur.getPseudo(),"SHOOT",tableShoot.getLast().getId());
                Log.v("TAG", "Insertion lancer franc reussi ");
                break;

            case "lfr":
                tir = new Shoot(tableTempsDeJeu.getLast(), acteur, 1, 0);
                tableShoot.insert(tir);
                addHistorique(message + acteur.getPseudo(),"SHOOT",tableShoot.getLast().getId());
                Log.v("TAG", "Insertion lancer franc raté");
                break;

            case "assist":
                Passe passe=new Passe(tableTempsDeJeu.getLast(),acteur);
                tablePasse.insert(passe);
                addHistorique(message + acteur.getPseudo(),"PASSE_DECISIVE",tablePasse.getLast().getId());

                break;

        }

        //addHistorique(message+" "+acteur.getPseudo(),"",0);
        fragmentEquipeA.clearLayouts();
        fragmentEquipeB.clearLayouts();
        fragmentEquipeA.fillTeamLayouts();
        fragmentEquipeB.fillTeamLayouts();
        setOneClick();
    }

    public void setOneClick(){
        oneClick=false;

    }






    public void action(View v) {
        ViewGroup p = (ViewGroup) v.getParent();
        LinearLayout l=(LinearLayout) p.findViewById(R.id.layoutFautes);

        TextView textView = (TextView) p.getChildAt(0);
        int index = Integer.parseInt(textView.getText().toString());

        TempsDeJeu tps = new TempsDeJeu(matchId,-1, ChronoFragment.getTime(), -1, -1);
        tableTempsDeJeu.insert(tps);

        switch (v.getId()) {
            case R.id.tir_2:
                Log.v("TAG", "Tir 2 Pts: " + textView.getText().toString());
                tirReussi(tps, getActeurOff(index),2);
                if (fragmentEquipeA.getEquipeOff()) {
                    scoreEquipeA += 2;
                } else {
                    scoreEquipeB += 2;
                }
                updateScore();
                switchPossession();
                break;

            case R.id.tir_2r:

                Log.v("TAG", "Tir 2 Pts Raté: " + textView.getText().toString());
                tirRate(tps, getActeurOff(index), 2);
                break;

            case R.id.faute_off:
                if(fragmentEquipeA.getEquipeOff())
                    fragmentEquipeA.addFautes(index);//fragmentEquipeA.createPoint(l);
                else
                    fragmentEquipeB.addFautes(index);//fragmentEquipeB.createPoint(l);
                Log.v("TAG", "Faute Off: " +textView.getText().toString());
                fauteOffensive(tps, getActeurOff(index),index);
                switchPossession();
                break;

            case R.id.rebond_off:
                Log.v("TAG", "Rebond Off: " +textView.getText().toString());
                rebondOffensif(tps, getActeurOff(index));
                break;

            case R.id.turnover:
                Log.v("TAG", "Turnover: " +textView.getText().toString());
                perteDeBalle(tps, getActeurOff(index));
                switchPossession();
                break;

            case R.id.block:
                Log.v("TAG", "Block: " +textView.getText().toString());
                contre(tps, getActeurDef(index));
                break;

            case R.id.rebond_def:
                Log.v("TAG", "Rebond Def: " +textView.getText().toString());
                rebondDefensif(tps, getActeurDef(index));
                switchPossession();
                break;

            case R.id.faute_def:
                if(!fragmentEquipeA.getEquipeOff()) {
                    fragmentEquipeA.addFautes(index);
                    fragmentEquipeA.createPoint(l);
                }
                else {
                    fragmentEquipeB.createPoint(l);
                    fragmentEquipeB.addFautes(index);
                }
                Log.v("TAG", "Faute Def: " + textView.getText().toString());
                fauteDefensive(tps, getActeurDef(index),index);
                break;

            case R.id.steal:
                Log.v("TAG", "Steal: " +textView.getText().toString());
                interception(tps, getActeurDef(index));
                switchPossession();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_interface_match, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.voir_bdd) {
            Intent intent = new Intent(this, VerificationDB.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.terminer) {
            Intent returnIntent = new Intent();
            setResult(RESULT_OK, returnIntent);
            DBMatchDAO tableMatch = new DBMatchDAO(mCtx);
            // TODO Mettre à jour le match
            // On met dans le champ du vainqueur EQUIPE_ID et pas FORMATION_ID
            Match match = new Match(tableMatch.getWithId(matchId));
            // Fake data
            match.setResultat(idA);
            match.setScoreEquipeA(65);
            match.setScoreEquipeB(50);
            tableMatch.update(match.getId(), match);
            Log.d("Interface", "setResult()");
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void endMatch(View v) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        DBMatchDAO tableMatch = new DBMatchDAO(mCtx);
        // On met dans le champ du vainqueur EQUIPE_ID et pas FORMATION_ID
        Match match = new Match(tableMatch.getWithId(matchId));
        /** Get the current date */
        final Calendar cal = Calendar.getInstance();
        int pYear = cal.get(Calendar.YEAR);
        int pMonth = cal.get(Calendar.MONTH);
        int pDay = cal.get(Calendar.DAY_OF_MONTH);
        String date = pDay+"/"+pMonth+"/"+pYear;
        match.setDate(date);
        if (scoreEquipeA > scoreEquipeB) {
            match.setResultat(idA);
        } else if (scoreEquipeA < scoreEquipeB) {
            match.setResultat(idB);
        } else {
            match.setResultat(Match.RESULTAT_NUL);
        }
        match.setScoreEquipeA(scoreEquipeA);
        match.setScoreEquipeB(scoreEquipeB);
        tableMatch.update(match.getId(), match);
        //Log.v("finish","MATCH TERMINE");

        int formationA = match.getFormationEquipeA();
        int formationB = match.getFormationEquipeB();
        List<Joueur> tmpA = new ArrayList<>(tableJoueur.getFormationMatch(formationA));
        List<Joueur> tmpB = new ArrayList<>(tableJoueur.getFormationMatch(formationB));
        String JSON=tableMatch.MatchJSON(matchId,tmpA,tmpB,formationA,formationB);
        Log.d("JSON",JSON);
        Log.d("Interface", "setResult()");
        finish();
    }

    public void displayStats(View v){
        Intent i = new Intent(mCtx, VueStatsMatch.class);
        i.putExtra(KeyTable.MATCH_ID, matchId);
        startActivity(i);
    }

    public void initDAO(Context context){
        tableEquipe = new DBEquipeDAO(context);
        tableJoueur = new DBJoueurDAO(context);
        tableShoot = new DBShootDAO(context);
        tableContre = new DBContreDAO(context);
        tableFaute = new DBFauteDAO(context);
        tableInterception = new DBInterceptionDAO(context);
        tablePasse = new DBPasseDAO(context);
        tablePerteBalle = new DBPerteBalleDAO(context);
        tableRebond = new DBRebondDAO(context);
        tableTempsDeJeu = new DBTempsDeJeuDAO(context);
        tableAction  =  new DBActionDAO(context);
        tableMatch   =  new DBMatchDAO(context);
    }

    public void tirReussi(TempsDeJeu tps, Joueur acteur, int pts){
        Shoot tir = new Shoot(tableTempsDeJeu.getLast(), acteur, pts, Shoot.REUSSI);
        tableShoot.insert(tir);
        addHistorique(tps.getChronometre() + "\ttir 2 pts réussi par\t" + acteur.getPseudo(),"SHOOT",tableShoot.getLast().getId());
        Log.v("TAG", "Insertion shoot réussi à" + pts + " pts");
    }

    public void tirRate(TempsDeJeu tps, Joueur acteur, int pts){
        Shoot tir = new Shoot(tableTempsDeJeu.getLast(), acteur, pts, Shoot.RATE);
        tableShoot.insert(tir);
        addHistorique(tps.getChronometre() + "\ttir 2 pts manqué par\t"+acteur.getPseudo(),"SHOOT",tableShoot.getLast().getId());
        Log.v("TAG", "Insertion shoot loupé à" + pts + " pts");
    }

    public void rebondOffensif(TempsDeJeu tps, Joueur acteur){
        tableRebond.insert(new Rebond(tableTempsDeJeu.getLast(), acteur, Rebond.OFFENSIF));
        addHistorique(tps.getChronometre() + "\trebond offensif de\t"+acteur.getPseudo(),"REBOND",tableRebond.getLast().getId());
        Log.v("TAG", "Insertion rebond off");

    }

    public void rebondDefensif(TempsDeJeu tps, Joueur acteur){
        tableRebond.insert(new Rebond(tableTempsDeJeu.getLast(), acteur, Rebond.DEFENSIF));
        addHistorique(tps.getChronometre() + "\trebond défensif de\t"+acteur.getPseudo(),"REBOND",tableRebond.getLast().getId());
        Log.v("TAG", "Insertion rebond def");
    }

    public void fauteOffensive(TempsDeJeu tps, Joueur acteur,int index){
        tableFaute.insert(new Faute(tableTempsDeJeu.getLast(), acteur));
        String equipeFaute="FAUTEA";
        if (fragmentEquipeB.getEquipeOff())
            equipeFaute="FAUTEB";
        addHistorique(tps.getChronometre() + "\tfaute de\t"+acteur.getPseudo(),equipeFaute,tableFaute.getLast().getId(),index);

    }

    public void fauteDefensive(TempsDeJeu tps, Joueur acteur,int index){
        tableFaute.insert(new Faute(tableTempsDeJeu.getLast(), acteur));
        String equipeFaute="FAUTEA";
        if (!fragmentEquipeB.getEquipeOff())
            equipeFaute="FAUTEB";
        addHistorique(tps.getChronometre() + "\tfaute de\t"+acteur.getPseudo(),equipeFaute,tableFaute.getLast().getId(),index);
    }

    public void interception(TempsDeJeu tps, Joueur acteur){
        tableInterception.insert(new Interception(tableTempsDeJeu.getLast(), acteur));
        addHistorique(tps.getChronometre() + "\tinterception de\t"+acteur.getPseudo(),"INTERCEPTION",tableInterception.getLast().getId());

    }

    public void perteDeBalle(TempsDeJeu tps, Joueur acteur){
        tablePerteBalle.insert(new PerteBalle(tableTempsDeJeu.getLast(), acteur));
        addHistorique(tps.getChronometre() + "\tballon perdu par\t"+acteur.getPseudo(),"PERTE_BALLE",tablePerteBalle.getLast().getId());
    }

    public void contre(TempsDeJeu tps, Joueur acteur){
        tableContre.insert(new Contre(tableTempsDeJeu.getLast(), acteur));
        addHistorique(tps.getChronometre() + "\tcontre de\t"+acteur.getPseudo(),"CONTRE",tableContre.getLast().getId());
    }

    public Joueur getActeurOff(int index){
        if (fragmentEquipeA.getEquipeOff())
            return fragmentEquipeA.getListeJoueurs().get(index);
        else
            return fragmentEquipeB.getListeJoueurs().get(index);

    }

    public Joueur getActeurDef(int index){

        if (!fragmentEquipeA.getEquipeOff())
            return fragmentEquipeA.getListeJoueurs().get(index);
        else
            return fragmentEquipeB.getListeJoueurs().get(index);


    }

    public void switchPossession(){

        fragmentEquipeA.setEquipeOff(!fragmentEquipeA.getEquipeOff());
        fragmentEquipeB.setEquipeOff(!fragmentEquipeB.getEquipeOff());
        if(fragmentEquipeA.getEquipeOff())
            fragmentMenu.setBallonA();
        else
            fragmentMenu.setBallonB();

        fragmentEquipeA.clearLayouts();
        fragmentEquipeB.clearLayouts();
        fragmentEquipeA.fillTeamLayouts();
        fragmentEquipeB.fillTeamLayouts();

    }

    public void initHistorique(){
        mHistoriqueListView = (ListView) findViewById(R.id.history_listview);
        mHistorique = new ArrayList<String>();
        listeh= new ArrayList<HashMap<String,String>>();
        adaptH=new SimpleAdapter(InterfaceMatch.this,listeh,R.layout.historique,new String[]{"event","bouton","action","indexJoueur"},new int[]{R.id.action,R.id.button}) {

            public View getView(int position, View convertView, ViewGroup parent) {
                final int x=position;
                View v = super.getView(position, convertView, parent);
                ImageButton b=(ImageButton)v.findViewById(R.id.supprimerAction);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String action=listeh.get(x).get("action");
						// Gestion suppression d'un shoot
						if (action.equals("SHOOT")) {
							Shoot sh = tableShoot.getWithId(Integer.parseInt(listeh.get(x).get("bouton")));
                            Log.d("Interface",listeh.get(x).get("bouton"));
                            Log.d("SHOOT!!!!!",""+sh);
							if (sh.getReussi() == Shoot.REUSSI) {
								// On recherche quelle equipe a fait le shoot
								Boolean shootEquipeA = false;
								int acteur = sh.getJoueurActeur();

								List<Joueur> list = fragmentEquipeA.getListeJoueurs();
								for(Joueur j: list) {
                                    Log.d("ACTEURID!!!",acteur+"  "+j.getId());
									if (j.getId() == acteur) {
										scoreEquipeA -= sh.getPts();
										shootEquipeA = true;
									}
								}
								if (!shootEquipeA) {
									scoreEquipeB -= sh.getPts();
								}
								updateScore();
							}
						}

                        if (action.equals("FAUTEA")){
                            int index=Integer.parseInt(listeh.get(x).get("indexJoueur"));
                            fragmentEquipeA.removeFautes(index);

                            fragmentEquipeA.clearLayouts();
                            fragmentEquipeB.clearLayouts();
                            fragmentEquipeA.fillTeamLayouts();
                            fragmentEquipeB.fillTeamLayouts();
                            action="FAUTE";

                        }

                        if (action.equals("FAUTEB")){
                            int index=Integer.parseInt(listeh.get(x).get("indexJoueur"));
                            fragmentEquipeB.removeFautes(index);
                            fragmentEquipeA.clearLayouts();
                            fragmentEquipeB.clearLayouts();
                            fragmentEquipeA.fillTeamLayouts();
                            fragmentEquipeB.fillTeamLayouts();
                            action="FAUTE";

                        }
                        tableAction.removeWithId(action, Integer.parseInt(listeh.get(x).get("bouton")));
                        listeh.remove(x);
                        adaptH.notifyDataSetChanged();

                    }
                });
                return v;
            }
        }
        ;

        mHistoriqueListView.setAdapter(adaptH);
    }

    public void addHistorique(String str,String action,int id){
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("event",fragmentEquipeA.truncate(str,50));
        map.put("action", action);
        map.put("bouton",Integer.toString(id));
        listeh.add(0, map);
        adaptH.notifyDataSetChanged();
        mHistoriqueListView.setAdapter(adaptH);
    }

    public void addHistorique(String str,String action,int id, int index){
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("event",fragmentEquipeA.truncate(str,50));
        map.put("action", action);
        map.put("bouton",Integer.toString(id));
        map.put("indexJoueur",Integer.toString(index));
        listeh.add(0, map);
        adaptH.notifyDataSetChanged();
        mHistoriqueListView.setAdapter(adaptH);

    }

    public void deleteHistorique(){
        //TODO with onSelectedItem imo sur la listView
    }

    public void updateScore() {
        TextView scoreATextView = (TextView) findViewById(R.id.score_team_1);
        TextView scoreBTextView = (TextView) findViewById(R.id.score_team_2);

        scoreATextView.setText(String.valueOf(scoreEquipeA));
        scoreBTextView.setText(String.valueOf(scoreEquipeB));
    }

    private Joueur getNeutralPlayer(List<Joueur> mListeJoueur){
        for (Joueur cmp : mListeJoueur){
            if (cmp.getNom().equals("JoueurNeutre") && cmp.getPrenom().equals("NeutralPlayer")){
                mListeJoueur.remove(cmp);
                Log.d("NEUTRE", "Joueur neutre trouvé (id:"+cmp.getId()+"), suppression ");
                return cmp;
            }
        }
        return null;
    }
}
