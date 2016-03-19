package edu.syr.jdevv;

/**
 * Created by Chunxu on 2016/3/14.
 */
public class TrafficLight_MultiThread {
    public void start() {
        LightLock lock = new LightLock();
        (new Thread(new Road2(lock))).start();
        (new Thread(new Road1(lock))).start();
    }

    public static void main(String[] args) {
        TrafficLight_MultiThread trafficLight = new TrafficLight_MultiThread();
        trafficLight.start();
    }
}

class LightLock {
    private boolean isGreen = false;

    public boolean isAnyRoadGreen() {
        return isGreen;
    }

    synchronized void lockGreen() {
        isGreen = true;
    }

    synchronized void releaseLock() {
        isGreen = false;
        this.notifyAll();
    }
}

class Road1 implements Runnable {
    private int redLight;
    private int greenLight;
    private int yellowLight;
    private LightLock lock;

    public Road1(LightLock lock) {

        redLight = 0;
        greenLight = 0;
        yellowLight = 0;
        this.lock = lock;
    }

    public void red() {
        lock.releaseLock();
        redLight = 1;
        greenLight = 0;
        yellowLight = 0;

        System.out.println("-------------------------");
        System.out.println("Road 1 red light is on.");
        System.out.println("-------------------------");

        try {
            Thread.sleep(700);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public void green() throws InterruptedException {

        while (lock.isAnyRoadGreen()) {
            synchronized (lock) {
                lock.wait();
            }
        }

        redLight = 0;
        greenLight = 1;
        yellowLight = 0;

        System.out.println("-------------------------");
        System.out.println("Road 1 green light is on.");
        System.out.println("-------------------------");

        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public void yellow() {
        redLight = 0;
        greenLight = 0;
        yellowLight = 1;

        System.out.println("-------------------------");
        System.out.println("Road 1 yellow light is on.");
        System.out.println("-------------------------");

        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        for (int i = 0; i < 4; i++) {
            try {
                green();
                yellow();
                red();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}

class Road2 implements Runnable {
    private int redLight;
    private int greenLight;
    private int yellowLight;
    private LightLock lock;

    public Road2(LightLock lock) {
        redLight = 0;
        greenLight = 0;
        yellowLight = 0;
        this.lock = lock;
    }

    public void red() {
        lock.releaseLock();
        redLight = 1;
        greenLight = 0;
        yellowLight = 0;

        System.out.println("-------------------------");
        System.out.println("Road 2 red light is on.");
        System.out.println("-------------------------");

        try {
            Thread.sleep(700);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public void green() throws InterruptedException {
        while (lock.isAnyRoadGreen()) {
            synchronized (lock) {
                lock.wait();
            }
        }
        lock.lockGreen();
        redLight = 0;
        greenLight = 1;
        yellowLight = 0;

        System.out.println("-------------------------");
        System.out.println("Road 2 green light is on.");
        System.out.println("-------------------------");

        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public void yellow() {
        redLight = 0;
        greenLight = 0;
        yellowLight = 1;

        System.out.println("-------------------------");
        System.out.println("Road 2 yellow light is on.");
        System.out.println("-------------------------");

        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        for (int i = 0; i < 4; i++) {

            try {
                red();
                green();
                yellow();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
}
