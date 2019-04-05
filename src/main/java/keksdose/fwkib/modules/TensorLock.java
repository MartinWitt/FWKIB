package keksdose.fwkib.modules;

import java.util.concurrent.Semaphore;

public class TensorLock {
    static final Semaphore lock = new Semaphore(2, true);

    public static synchronized void getLock() {
        try {
            lock.acquire();
        } catch (InterruptedException e) {
            return;
        }
    }

    public static synchronized void releaseLock() {
        lock.release();
    }
}