package com.enseirb.pfa.bastats.tournoi;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.enseirb.pfa.bastats.R;
import com.enseirb.pfa.bastats.data.DAO.DBClassementTournoiDAO;
import com.enseirb.pfa.bastats.data.DAO.DBEquipeDAO;
import com.enseirb.pfa.bastats.data.DAO.DBMatchDAO;
import com.enseirb.pfa.bastats.data.DAO.DBPhaseDAO;
import com.enseirb.pfa.bastats.data.DAO.DBPhaseTableauDAO;
import com.enseirb.pfa.bastats.data.DAO.DBTournoiDAO;
import com.enseirb.pfa.bastats.data.model.ClassementTournoi;
import com.enseirb.pfa.bastats.data.model.Match;
import com.enseirb.pfa.bastats.data.model.Phase;
import com.enseirb.pfa.bastats.tournoi.algorithme.Calendrier;
import com.enseirb.pfa.bastats.tournoi.fragment.CalendrierTableauFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Pardonnez moi mon père mais j'ai pêché,
 * Ce code est déguelasse mais j'ai pas trouvé plus rapide comme solution
 * Ayez pitié de moi
 * Je ne recommencerai plus
 */

public class NavigationTableau extends ActionBarActivity implements
        CalendrierTableauFragment.OnFragmentInteractionListener {

    public static final String FINALE = "Finale ";
    public static final String DEMI_FINALE = "Demi-finale ";
    public static final String QUART_FINALE = "Quart de finale ";
    public static final String HUITUIEME_FINALE = "Huitième de finale ";
    public static final String SEIZIEME_FINALE = "Seizieme de finale ";
    public static final String PETITE_FINALE = "Petite finale ";
    public static final String ALL = "All";

    private int currentTableau = 0;

    private int nbEquipes;
    private int debutClt;
    private int finClt;

    private Context mCtx;
    private static final String TAG = "NavigationTableau";
    private static final int MATCH_REQUEST = 1;
    private static final int DEFINE_TEAMS_REQUEST = 2;

    private int nbTableau = 0;

    private int phaseTableauId;
    private int phaseId;
    private int tournoiId;

    private HashMap<String, List<Match>> mMatchSchedule;

    private ArrayList<CalendrierTableauFragment> mListeFragment;

    private Button doRanking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_tableau);

        mCtx = this;

        doRanking = (Button) findViewById(R.id.doClassement);
        doRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFinished()) {
                    DBTournoiDAO tableTournoi = new DBTournoiDAO(mCtx);
                    int nbEquipesTournoi = tableTournoi.getWithId(tournoiId).getNbEquipeMax();
                    if (!mMatchSchedule.get(PETITE_FINALE).isEmpty()) {
                        // Cas petite finale jouée
                        doClassement("Classement du vainqueur", 1, nbEquipesTournoi, true);
                    } else {
                        doClassement("Classement du vainqueur", 1, nbEquipesTournoi, false);
                    }
                }
            }
        });
        getDataFromIntent();
        currentTableau = 0;
        initFragment();

    }

    @Override
    public void onStart(){
        super.onStart();
        updateSelectionEquipe();
        updateEtatBouttonAjout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_navigation_tableau, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNextPoule(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(mListeFragment.get(currentTableau));

        if (currentTableau == nbTableau-1)
            currentTableau = 0;
        else
            currentTableau++;

        ft.show(mListeFragment.get(currentTableau));
        ft.commit();
    }

    @Override
    public void onPreviousPoule(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(mListeFragment.get(currentTableau));

        if (currentTableau == 0)
            currentTableau = nbTableau-1;
        else
            currentTableau--;

        ft.show(mListeFragment.get(currentTableau));
        ft.commit();
    }

    /*
     * Fonction appelée si besoin de
     */
    private void updateSelectionEquipe(){
        Log.d(TAG, "call update selection equipe");
        CalendrierTableauFragment prev = null;
        CalendrierTableauFragment cur;
        for (int i=0; i < mListeFragment.size(); i++){
            if (mListeFragment.get(i).getmTitle().contains(PETITE_FINALE)){
                prev = mListeFragment.get(i - 1);
                cur = mListeFragment.get(i);
                if (prev.isFinish()) {
                    cur.setSelectionInterne(prev.getLosers());
                    mListeFragment.get(i + 1).setSelectionInterne(prev.getWinners());
                }
                // On a trier la liste exprès pour avoir en dernier petite finale et finale
                // On peut donc sortir de la boucle for et on aura tout parcouru
                break;
            } else if (i > 0) {
                prev = mListeFragment.get(i - 1);
                cur = mListeFragment.get(i);
                if (prev != null) {
                    if (prev.isFinish() && i > 0) {
                        cur.setSelectionInterne(prev.getWinners());
                    }
                }
            }
        }
    }

    private void getDataFromIntent(){
        DBPhaseTableauDAO tablePhaseTableau = new DBPhaseTableauDAO(mCtx);
        Intent intent = getIntent();
        phaseId = intent.getIntExtra(KeyTable.ARG_ID_PHASE,-1);
        tournoiId = intent.getIntExtra(KeyTable.ARG_ID_TOURNOI,-1);
        int type = intent.getIntExtra(KeyTable.ARG_PHASE_TYPE,-1);
        if (type == Phase.TYPE_TABLEAU){
            phaseTableauId = tablePhaseTableau.getFromPhaseId(phaseId).getId();
            Log.d(TAG, "phaseTableauId: "+phaseTableauId);
        } else {
            // Impossible
        }
    }

    private void initSchedule() {
        mMatchSchedule = new HashMap<>();
        List<Match> finale = new ArrayList<>();
        List<Match> petite = new ArrayList<>();
        List<Match> demi = new ArrayList<>();
        List<Match> quart = new ArrayList<>();
        List<Match> huitieme = new ArrayList<>();
        List<Match> seizieme = new ArrayList<>();
        List<Match> all = new ArrayList<>();
        mMatchSchedule.put(FINALE, finale);
        mMatchSchedule.put(PETITE_FINALE,petite);
        mMatchSchedule.put(DEMI_FINALE, demi);
        mMatchSchedule.put(QUART_FINALE, quart);
        mMatchSchedule.put(HUITUIEME_FINALE, huitieme);
        mMatchSchedule.put(SEIZIEME_FINALE, seizieme);
        mMatchSchedule.put(ALL, all);
    }

    private void fillSchedule(){
        // Get initiale gameList
        DBMatchDAO tableMatch = new DBMatchDAO(mCtx);
        List<Match> initGames = tableMatch.getFromPhaseType(phaseId);
        int nbMatch = initGames.size();
        boolean recognizeAll = true;
        for (Match match : initGames){
            if (match.getLibelle().toLowerCase().contains(Calendrier.SEIZIEME.toLowerCase())){
                mMatchSchedule.get(SEIZIEME_FINALE).add(match);
            } else if (match.getLibelle().toLowerCase().contains(Calendrier.HUITUIEME.toLowerCase())){
                mMatchSchedule.get(HUITUIEME_FINALE).add(match);
            } else if (match.getLibelle().toLowerCase().contains(Calendrier.QUART.toLowerCase())){
                mMatchSchedule.get(QUART_FINALE).add(match);
            } else if (match.getLibelle().toLowerCase().contains(Calendrier.DEMI.toLowerCase())){
                mMatchSchedule.get(DEMI_FINALE).add(match);
            } else if (match.getLibelle().toLowerCase().contains(Calendrier.PETITE.toLowerCase())){
                mMatchSchedule.get(PETITE_FINALE).add(match);
            } else if (match.getLibelle().toLowerCase().contains(Calendrier.FINALE.toLowerCase())){
                mMatchSchedule.get(FINALE).add(match);
            } else {
                recognizeAll = false;
            }
        }
        if (!recognizeAll){
            // On est pas capable de reconnaître au moins un des matchs
            for (Match match1 : initGames){
                mMatchSchedule.get(ALL).add(match1);
            }
            mMatchSchedule.get(SEIZIEME_FINALE).clear();
            mMatchSchedule.get(HUITUIEME_FINALE).clear();
            mMatchSchedule.get(QUART_FINALE).clear();
            mMatchSchedule.get(DEMI_FINALE).clear();
            mMatchSchedule.get(FINALE).clear();
            mMatchSchedule.get(PETITE_FINALE).clear();
        }
    }

    private ArrayList<Integer> matchToInteger(List<Match> mListe){
        ArrayList<Integer> matchIds = new ArrayList<>();
        for (Match match : mListe)
            matchIds.add(match.getId());
        return matchIds;
    }

    private void orderFragment(){
        if (!mMatchSchedule.get(SEIZIEME_FINALE).isEmpty()) {
            mListeFragment.add(CalendrierTableauFragment.newInstance(SEIZIEME_FINALE,
                    matchToInteger(mMatchSchedule.get(SEIZIEME_FINALE)),
                    phaseTableauId));
        }
        if (!mMatchSchedule.get(HUITUIEME_FINALE).isEmpty()) {
            mListeFragment.add(CalendrierTableauFragment.newInstance(HUITUIEME_FINALE,
                    matchToInteger(mMatchSchedule.get(HUITUIEME_FINALE)),
                    phaseTableauId));
        }
        if (!mMatchSchedule.get(QUART_FINALE).isEmpty()) {
            mListeFragment.add(CalendrierTableauFragment.newInstance(QUART_FINALE,
                    matchToInteger(mMatchSchedule.get(QUART_FINALE)),
                    phaseTableauId));
        }
        if (!mMatchSchedule.get(DEMI_FINALE).isEmpty()) {
            mListeFragment.add(CalendrierTableauFragment.newInstance(DEMI_FINALE,
                    matchToInteger(mMatchSchedule.get(DEMI_FINALE)),
                    phaseTableauId));
        }
        if (!mMatchSchedule.get(PETITE_FINALE).isEmpty()) {
            mListeFragment.add(CalendrierTableauFragment.newInstance(PETITE_FINALE,
                    matchToInteger(mMatchSchedule.get(PETITE_FINALE)),
                    phaseTableauId));
        }
        if (!mMatchSchedule.get(FINALE).isEmpty()) {
            mListeFragment.add(CalendrierTableauFragment.newInstance(FINALE,
                    matchToInteger(mMatchSchedule.get(FINALE)),
                    phaseTableauId));
        }
    }

    private void initFragment(){
        initSchedule();
        fillSchedule();
        mListeFragment = new ArrayList<>();

        orderFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // On les ajoute à l'activité
        for (int i = 0; i < mListeFragment.size(); i++){
            ft.add(R.id.calendrier_match_fragment_container, mListeFragment.get(i));
            ft.hide(mListeFragment.get(i));
        }
        currentTableau = 0;
        ft.show(mListeFragment.get(currentTableau));
        ft.commit();
        nbTableau = mListeFragment.size();
    }

    private void setHigherNotFinishedVisibleFirst(){
        // Bonne idée ?
    }

    private void setNotAccessible(){
        // Bonne idée ?
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MATCH_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                mListeFragment.get(currentTableau).update();
                updateSelectionEquipe();
                updateEtatBouttonAjout();
            }
        }
        if (requestCode == DEFINE_TEAMS_REQUEST) {
            if (resultCode == RESULT_OK){
                mListeFragment.get(currentTableau).update();
                updateSelectionEquipe();
                updateEtatBouttonAjout();
            }
        }
    }

    private void updateEtatBouttonAjout(){
        if (isFinished()){
            doRanking.setVisibility(Button.VISIBLE);
        }
    }

    private void showRangeSeekBar(String title, int min, int max){
        final Dialog d = new Dialog(mCtx);
        d.setTitle(title);
        d.setContentView(R.layout.dialog_range_seek_bar);
        Button positiveButton = (Button) d.findViewById(R.id.positiveButton);
        Button negativeButton = (Button) d.findViewById(R.id.negativeButton);
        final TextView textMin = (TextView) d.findViewById(R.id.min_value);
        final TextView textMax = (TextView) d.findViewById(R.id.max_value);
        final TextView textNbEquipes = (TextView) d.findViewById(R.id.nombre_equipe);
        textMin.setText(String.valueOf(min));
        textMax.setText(String.valueOf(max));
        nbEquipes = max - min + 1;
        textNbEquipes.setText(String.valueOf(nbEquipes));
        RangeSeekBar<Integer> seekBar = new RangeSeekBar<Integer>(min, max, mCtx);
        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                // handle changed range values
                textMin.setText(String.valueOf(minValue));
                textMax.setText(String.valueOf(maxValue));
                debutClt = minValue;
                maxValue = minValue + 3;
                finClt = maxValue;
                nbEquipes = maxValue - minValue + 1;
                textNbEquipes.setText(String.valueOf(nbEquipes));
                Log.i("TAG", "User selected new range values: MIN=" + minValue + ", MAX=" + maxValue);
            }
        });
        // add RangeSeekBar to pre-defined layout
        ViewGroup layout = (ViewGroup) d.findViewById(R.id.seek_bar_container);
        layout.addView(seekBar);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                d.dismiss();
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }

    private boolean isFinished(){
        Log.d(TAG, "taille: "+mListeFragment.size());
        for (CalendrierTableauFragment frag : mListeFragment){
            if (!frag.isFinish())
                return false;
        }
        return true;
    }

    private void doClassement(String title, int min, int max, final boolean withPetiteFinale){
        final Dialog d = new Dialog(mCtx);
        d.setTitle(title);
        d.setContentView(R.layout.dialog_number_picker);
        Button positiveButton = (Button) d.findViewById(R.id.positiveButton);
        Button negativeButton = (Button) d.findViewById(R.id.negativeButton);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker);
        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        np.setMaxValue(max);
        np.setMinValue(min);
        positiveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                debutClt = np.getValue();
                enregistrer(withPetiteFinale);
                Intent i = new Intent();
                setResult(RESULT_OK,i);
                finish();
                d.dismiss();
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Toast.makeText(mCtx,"Pas de classement généré",Toast.LENGTH_LONG);
                d.dismiss();
            }
        });
        d.show();
    }

    private void enregistrer(boolean withPetiteFinale){
        DBClassementTournoiDAO tableClassement = new DBClassementTournoiDAO(mCtx);
        CalendrierTableauFragment finale = mListeFragment.get(mListeFragment.size()-1);
        int idVainqueur = finale.getWinners().get(0);
        int idLoser = finale.getLosers().get(0);
        tableClassement.insert(new ClassementTournoi(idVainqueur,tournoiId,debutClt,0));
        tableClassement.insert(new ClassementTournoi(idLoser,tournoiId,debutClt+1,0));
        if (withPetiteFinale){
            CalendrierTableauFragment petite = mListeFragment.get(mListeFragment.size()-2);
            int idV = petite.getWinners().get(0);
            int idL = petite.getLosers().get(0);
            tableClassement.insert(new ClassementTournoi(idV,tournoiId,debutClt+2,0));
            tableClassement.insert(new ClassementTournoi(idL,tournoiId,debutClt+3,0));
        }

    }
}
