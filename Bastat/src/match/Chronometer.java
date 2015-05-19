package com.enseirb.pfa.bastats.match;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by lionel on 20/03/15.
 */
interface TickListener {
    void onTick(Chronometer chronometer);
}

public class Chronometer {
    private ScheduledExecutorService scheduler;
    private long basetime;
    private List<TickListener> listner;
    private ScheduledFuture<?> tickHandle;
    public Chronometer() {
        listner = new ArrayList<TickListener>();
        scheduler = Executors.newScheduledThreadPool(1);
    }

    public void setBaseTime(long time) {
        this.basetime = time;
    }

    public long getPastSeconds() {
        return (System.currentTimeMillis() - this.basetime) / 1000;
    }

    public void setOnTickListener(TickListener lsnr) {
        listner.add(lsnr);
    }

    public void start() {
        final Runnable ticker = new Runnable() {
            public void run() {
                for (TickListener tl : listner) {
                    tl.onTick(Chronometer.this);
                }
            }
        };

        tickHandle = scheduler.scheduleAtFixedRate(ticker, 0, 1, TimeUnit.SECONDS);
    }

    public void stop() {
        scheduler.schedule(new Runnable() {
            public void run() {
                tickHandle.cancel(true);
            }}, 0, TimeUnit.SECONDS);
    }
}