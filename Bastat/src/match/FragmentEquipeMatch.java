package com.enseirb.pfa.bastats.match;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.enseirb.pfa.bastats.R;
import com.enseirb.pfa.bastats.data.model.Joueur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FragmentEquipeMatch extends Fragment {

    public Boolean getEquipeOff() {
        return equipeOff;
    }
    public Boolean getEquipeA () { return equipeA;}

    public void setEquipeA(Boolean equipeA) {
        this.equipeA = equipeA;
    }

    public HashMap getFautes() {
        return fautes;
    }

    public void addFautes(int index){
        int faute=(int) getFautes().get(index);
        faute++;
        getFautes().put(index,faute);


    }

    public void removeFautes(int index){
        int faute=(int) getFautes().get(index);
        faute--;
        getFautes().put(index,faute);



    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setRgb(int rgb) {
    this.rgb=rgb;

    }



    public interface  EquipeMatch{
        public void action(View v);
        public void tableRowAction(View v,int index,String msg,String action);

    }



    private String color="Rose";
    private int rgb;
    private static final int RED = 0;
    private static final int GREEN = 1;
    private static final int GRAY = 2;
    private Context Ctx;
    private List<Joueur> listeJoueurs = new ArrayList<Joueur>();

    private TableLayout table;
    private Boolean equipeOff=false;
    private Boolean equipeA=false;
    private EquipeMatch callBackAction;
    private HashMap fautes=new HashMap<Integer,Integer>();

    private int[] row_buttons_str_off = {
            R.string.tir_2pts,
            R.string.tir_2pts,
            R.string.rebond_off,
            R.string.faute_off,
            R.string.turnover
    };

    private int[] row_buttons_str_def = {
            R.string.block ,
            R.string.rebond_def,
            R.string.faute_def,
            R.string.steal
    };

    private int[] row_buttons_id_off = {
            R.id.tir_2,
            R.id.tir_2r,
            R.id.rebond_off,
            R.id.faute_off,
            R.id.turnover
    };

    private int[] row_buttons_id_def = {
            R.id.block,
            R.id.rebond_def,
            R.id.faute_def,
            R.id.steal
    };

    private int[] row_buttons_color_off = {
            GREEN,
            RED,
            GRAY,
            GRAY,
            GRAY
    };

    private int[] row_buttons_color_def = {
            GRAY,
            GRAY,
            GRAY,
            GRAY
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_equipe_match,container,false);
        table= (TableLayout) v.findViewById(R.id.interface_match_left_table);


        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Ctx=getActivity();
        for (int i=0;i<listeJoueurs.size();i++)
            getFautes().put(i, 0);

        fillTeamLayouts();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callBackAction=(EquipeMatch) activity;
    }



    public void fillTeamLayouts(){
        if (getEquipeOff()) {
            createOffenseLayout(table, getListeJoueurs());

        }
        else {

            createDefenseLayout(table, getListeJoueurs());
        }
    }

    public void createOffenseLayout(TableLayout tl, List<Joueur> listeJoueurs) {
        for (int j = 0; j < listeJoueurs.size(); j++) {
            Joueur tmp = listeJoueurs.get(j);
            createTableRow(tl, j, tmp.getNumero() ,tmp.getPseudo(),
                    row_buttons_id_off, row_buttons_str_off, row_buttons_color_off);
        }
    }

    public void createDefenseLayout(TableLayout tl, List<Joueur> listeJoueurs) {
        for (int j = 0; j < listeJoueurs.size(); j++) {
            Joueur tmp = listeJoueurs.get(j);
            createTableRow(tl, j, tmp.getNumero(),tmp.getPseudo(),
                    row_buttons_id_def, row_buttons_str_def, row_buttons_color_def);
        }
    }

    public void createTableRow(TableLayout tl, int index, String numero,String playerName, int[] id_array, int[] str_array, int[] colors_array) {
        float dp = tl.getResources().getDisplayMetrics().density;
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT);
        TableRow tableRow = new TableRow(Ctx);



        TextView indexView = new TextView(Ctx);
        indexView.setText(String.valueOf(index));
        indexView.setVisibility(View.GONE);
       // if(equipeA) {
            GradientDrawable gd=new GradientDrawable();
            gd.setShape(GradientDrawable.RECTANGLE);
            gd.setColor(getResources().getColor(R.color.background_material_light));
            gd.setStroke((int) (4* dp),rgb);

            tableRow.setBackground(gd);

            tableRow.addView(indexView);


        createText(tableRow, numero,playerName,index);
        View spacerColumn = new View(Ctx);
        tableRow.addView(spacerColumn, new TableRow.LayoutParams(1,(int) (60*dp)));
        for (int j = 0; j < id_array.length; j++) {
            createButton(tableRow, str_array[j], id_array[j], colors_array[j]);
        }


        tl.addView(tableRow, rowParams);
        View v=new View(Ctx);
        TableRow.LayoutParams rowParam= new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,5);

        tl.addView(v,rowParam);
    }
    public void createOnSelectedTableRow(TableLayout tl, int index, String numero,String playerName, String msg,String action) {
        float dp = tl.getResources().getDisplayMetrics().density;
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        final TableRow tableRow = new TableRow(Ctx);
        final String message = msg;
        final String Action = action;
        TextView indexView = new TextView(Ctx);
        indexView.setText(String.valueOf(index));
        indexView.setVisibility(View.GONE);
        tableRow.addView(indexView);
        GradientDrawable gd=new GradientDrawable();
        gd.setShape(GradientDrawable.RECTANGLE);
        gd.setColor(getResources().getColor(R.color.background_material_light));
        gd.setStroke((int) (4* dp),rgb);

        tableRow.setBackground(gd);

        createText(tableRow, numero,playerName,index);
        View spacerColumn = new View(Ctx);
        tableRow.addView(spacerColumn, new TableRow.LayoutParams(1,(int) (60*dp)));

        tableRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TextView textView = (TextView) tableRow.getChildAt(0);
                int index = Integer.parseInt(textView.getText().toString());
                callBackAction.tableRowAction(v,index,message,Action);

            }
        });

        tl.addView(tableRow, rowParams);
        View v=new View(Ctx);
        TableRow.LayoutParams rowParam= new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,5);

        tl.addView(v,rowParam);




    }




    public void createText(TableRow parent, String numero,String text,int index) {
        float dp = parent.getResources().getDisplayMetrics().density;

        TextView textView = new TextView(Ctx);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, (int)(5 * dp), (int)(5 * dp), 0);

        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
        textView.setHeight((int) (40 * dp));
        textView.setWidth((int) (40 * dp));
        textView.setTextColor(getResources().getColor(R.color.black));
        textView.setText(truncate(numero,3));
        textView.setPadding((int)(5*dp),0,0,0);


        parent.addView(textView,lp);
        View v=new View(Ctx);
        TableRow.LayoutParams rowParam= new TableRow.LayoutParams(5,TableRow.LayoutParams.MATCH_PARENT);


        v.setBackgroundColor(rgb);
        parent.addView(v,rowParam);
        View v2=View.inflate(Ctx,R.layout.player_display,parent);
        TextView player=(TextView) v2.findViewById(R.id.nomJoueur);
        player.setText(truncate(text,6));
        View separator=new View(Ctx);
        TableRow.LayoutParams rowParam2= new TableRow.LayoutParams(2,TableRow.LayoutParams.MATCH_PARENT);
        LinearLayout l=(LinearLayout) v2.findViewById(R.id.layoutFautes);
        for(int i=0;i<(int)fautes.get(index);i++)
            createPoint(l);


        separator.setBackgroundColor(Color.rgb(0, 0, 0));
        parent.addView(separator,rowParam2);

    }

    public void createPoint(LinearLayout l){
        float dp = l.getResources().getDisplayMetrics().density;

        TextView point=new TextView(Ctx);
        point.setWidth((int) (10 * dp));
        point.setHeight((int) (10 * dp));
        point.setBackgroundResource(R.drawable.round_corner_equipe_b_player);
        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity= Gravity.CENTER_VERTICAL;
        l.addView(point,params);



    }

    public void createButton(TableRow parent, int text, Integer id, Integer flags) {
        float dp = parent.getResources().getDisplayMetrics().density;
        if (flags == null) {
            flags = GRAY;
        }

		Button btn = new Button(Ctx);

        switch (flags) {
            case RED:

				btn.setBackgroundResource(R.drawable.button_red);
                break;
            case GREEN:

				btn.setBackgroundResource(R.drawable.button_green);
                break;
            default:

				btn.setBackgroundResource(R.drawable.button_grey);
                break;
        }
        btn.setText(text);
        btn.setTextColor(0xFF111111);
        if (id != null) {
            btn.setId(id);
        }

        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        lp.setMargins((int) (5 * dp), (int) (5 * dp), (int) (5 * dp), (int) (5 * dp));


        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callBackAction.action(v);
            }

        });




        parent.addView(btn, lp);

    }

    public void onSelectedActions(String msg,String action){
        clearLayouts();
        if (equipeOff) {
            onSelectOffenseLayout(table, listeJoueurs, msg,action);

        }

    }


    public void onSelectOffenseLayout(TableLayout tl, List<Joueur> listeJoueurs, String msg,String action){
        for (int j = 0; j < listeJoueurs.size(); j++) {
            Joueur tmp = listeJoueurs.get(j);
            createOnSelectedTableRow(tl, j, tmp.getNumero(),  tmp.getPseudo(), msg, action);
        }
    }








   public String truncate(String string, int n){
       if (string.length()<=n)
           return string;
       return string.substring(0,n);

   }




    public void clearLayouts(){
        table.removeAllViews();

    }

    public void setEquipeOff(Boolean equipeOff) {
        this.equipeOff = equipeOff;
    }

    public List<Joueur> getListeJoueurs() {
        return listeJoueurs;
    }

    public void setListeJoueurs(List<Joueur> listeJoueurs) {
        this.listeJoueurs.addAll(listeJoueurs);
    }
}
