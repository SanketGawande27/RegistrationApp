package com.example.registrationapp.model;

import android.os.CountDownTimer;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public abstract  class CountUpTimer extends CountDownTimer {
    private static final long INTERVAL_MS = 1000;
    private final long duration;
    protected CountUpTimer(long durationMs) {
        super(durationMs, INTERVAL_MS);
        this.duration = durationMs;
    }
    public abstract void onTick(int second);

    @Override
    public void onTick(long msUntilFinished) {
        // Used for formatting digit to be in 2 digits only
        int second = (int) ((duration - msUntilFinished) / 1000);
        onTick(second);
    }

    @Override
    public void onFinish() {
        onTick(duration / 1000);
    }

}
