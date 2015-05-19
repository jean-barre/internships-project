package com.enseirb.pfa.bastats.match;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.SystemClock;
import android.widget.TextView;

import com.enseirb.pfa.bastats.R;

import java.util.concurrent.TimeUnit;

class Watcher implements TickListener {
    public void onTick(Chronometer chronometer) {
        System.out.println("seconds:" + chronometer.getPastSeconds());
    }
}

/**
 * Created by lionel on 26/01/15.
 */
public class ChronoFragment extends Fragment {
    private Chronometer chrono;
    private static long time;
    private long timeWhenStopped = 0;
    private int currentQuarter = 1;
    boolean pauseState = true;
    private ChronoModel mModel = new ChronoModel(4, 10);
    TextView min;
    TextView sec;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        chrono = new Chronometer();
        chrono.setBaseTime(System.currentTimeMillis());
        View view = inflater.inflate(R.layout.fragment_chrono,
                container, false);
        final TextView quarter = (TextView) view.findViewById(R.id.quarterView);
        min = (TextView) view.findViewById(R.id.min);
        sec = (TextView) view.findViewById(R.id.sec);
        quarter.setText("n° " + currentQuarter);
        min.setText("" + mModel.getQuarterLength());
        final Button start = (Button) view.findViewById(R.id.button);
        Button reset = (Button) view.findViewById(R.id.button2);

        quarter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuarter < mModel.getQuarter()) {
                    currentQuarter++;
                    quarter.setText("n° " + currentQuarter);
                } else {
                    currentQuarter = 1;
                    quarter.setText("n° " + currentQuarter);
                }
            }

        });

        chrono.setOnTickListener(new Watcher() {
            @Override
            public void onTick(Chronometer chronometer) {
                time = chrono.getPastSeconds();
                setTime();
                Log.d("Chrono", "Donne moi le temps putain" + getTime());
            }
        });

        start.setText("START");
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  if (pauseState) {
                    //chrono.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                    chrono.start();
                    pauseState = false;
                    start.setText("STOP");
                } else {
                    //timeWhenStopped = chrono.getBase() - SystemClock.elapsedRealtime();
                    chrono.stop();
                    System.out.println(TimeUnit.MILLISECONDS.toMinutes(timeWhenStopped) + ":" + TimeUnit.MILLISECONDS.toSeconds(timeWhenStopped));
                    System.out.println(getTime());
                    pauseState = true;
                    start.setText("START");
                }*/
            }
        });

        reset.setText("RESET");
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                timeWhenStopped = 0;
                //chrono.setBase(SystemClock.elapsedRealtime());
                chrono.stop();
            }
        });

        return view;
    }

    public void setTime() {
        min.setText(String.format("%02d" ,TimeUnit.MILLISECONDS.toMinutes(time)));
        sec.setText(String.format("%02d" ,(TimeUnit.MILLISECONDS.toSeconds(time) % 60)));
    }

    public static String getTime() {
        return String.format("%02d:%02d" ,TimeUnit.MILLISECONDS.toMinutes(time), (TimeUnit.MILLISECONDS.toSeconds(time) % 60));
    }

}
