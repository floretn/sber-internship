package org.sberinsur.tutorial.queue;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayInteger implements Delayed {

    int i;
    long timeStart;
    long howLong;

    public DelayInteger(int i, long howLong) {
        this.i = i;
        timeStart = System.currentTimeMillis();
        this.howLong = howLong;
    }

    @Override
    public long getDelay(TimeUnit timeUnit) {
        return howLong - (System.currentTimeMillis() - timeStart);
    }

    @Override
    public int compareTo(Delayed delayed) {
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) - delayed.getDelay(TimeUnit.MILLISECONDS));
    }
}
