package keksdose.fwkib.modules;

import java.util.concurrent.Semaphore;

public class TensorLock {
    static final Semaphore lock = new Semaphore(2);

    public static void getLock() {
        try {
            lock.acquire();
        } catch (InterruptedException e) {
            return;
        }
    }

    public static void releaseLock() {
        lock.release();
    }
}