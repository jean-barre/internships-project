package com.enseirb.pfa.bastats.match;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import com.enseirb.pfa.bastats.R;

/**
 * Created by dohko on 20/02/15.
 */
public class FragmentMenuMatch extends Fragment {

    public void setEquipeA(String equipe) {
        equipeA.setText(equipe);
          }

    public void setEquipeB(String equipe) {
       equipeB.setText(equipe);
    }



    public void setBallonA(){
        ballonA.setVisibility(View.VISIBLE);
        ballonB.setVisibility(View.INVISIBLE);

    }

    public void setBallonB(){
        ballonB.setVisibility(View.VISIBLE);
        ballonA.setVisibility(View.INVISIBLE);

    }

    public void setColorA(int color) {
        this.colorA = color;
    }

    public void setColorB(int color) {
        this.colorB = color;
    }
    public interface menuMatch{
        public void switchPossession();
    }


    private menuMatch switchCallback;
    private Context Ctx;

    private ImageView ballonA;
    private ImageView ballonB;
    private TextView equipeA;
    private TextView equipeB;
    private Button pause;
    private int colorA;
    private int colorB;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_menu_match,container,false);
        equipeA=(TextView) v.findViewById(R.id.team_1);
        equipeB=(TextView) v.findViewById(R.id.team_2);
        pause= (Button) v.findViewById(R.id.pause);
        ballonA=(ImageView) v.findViewById(R.id.ballonA);
        ballonB=(ImageView) v.findViewById(R.id.ballonB);
        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setEquipeA("hello");
        Ctx=getActivity();
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCallback.switchPossession();
            }
        });
        equipeA.setTextColor(colorA);
        equipeB.setTextColor(colorB);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        switchCallback=(menuMatch) activity;
    }
}
