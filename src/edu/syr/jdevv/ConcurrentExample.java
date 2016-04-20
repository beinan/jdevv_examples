package edu.syr.jdevv;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ConcurrentExample {
    public static void main(String[] args) throws Throwable {
        Vec v = new Vec();
        Thread putter[] = new Thread[10];
        Thread getter[] = new Thread[2];
        for (int i = 0; i < 10; i++) {
            putter[i] = new Thread(new Putter(v, i));
            putter[i].start();
        }

        for (int i = 0; i < 2; i++) {
            getter[i] = new Thread(new Getter(v, i));
            getter[i].start();
        }
        for (int i = 0; i < 10; i++) {
            putter[i].join();
        }

        for (int i = 0; i < 2; i++) {
            getter[i].join();
        }

    }
}

class Vec{
    List<Integer> data = new ArrayList<Integer>();

    public void push_back(int j) {
        data.add(j);
        
    }

    public int size() {
        // TODO Auto-generated method stub
        return data.size();
    }

    public void clear() {
        data.clear();
        
    }

    public int get(int j) {
        // TODO Auto-generated method stub
        return data.get(j);
    }
    
}
class Putter implements Runnable {

    private Vec v;
    private int i;

    public Putter(Vec v, int i) {
        this.v = v;
        this.i = i;
    }

    @Override
    public void run() {
        int run = 9;
        while (run > 0) {
            synchronized(v){
            System.out.println(" put  " + i + " before wait");
            while (v.size() == 10)
                try {
                    v.wait();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            int j = ThreadLocalRandom.current().nextInt(0, 100);
            v.push_back(j);
            System.out.println("put " + i +  "  just added value " + j + "  to vec");
            run--;
            if (v.size() == 10) v.notifyAll();; //The moment the notificaiton is sent, this put thread also releases lock
            //to the waken thread
            }
        }
        synchronized(v){
            if (v.size() != 10) v.notifyAll();
        }
        //Without this line, the programm will encounter DEADLOCK!!!
    }

}

class Getter implements Runnable {

    private Vec v;
    private int i;

    public Getter(Vec v, int i) {
        this.v = v;
        this.i = i;
    }

    @Override
    public void run() {
      //vec contains 10 numbers
        int run = 4;
        while (run > 0) {
            int k = 0;
            synchronized(v){
                //locking mechanism requires constantly checking the lock status, which is very cpu intense.
                System.out.println( "get " + i + "  before wait");
                while (v.size() < 10)
                    try {
                        v.wait();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }; //The moment a thread is in sleep, it will at the
                //same time release the lock (i.e., unlock it).
                //sleep mode will not require checking lock status and is more cpu efficient.
                for (int j = 0; j < 10; j++) {
                    k += v.get(j) * v.get(j);
                }
                System.out.println( " result = " + k + "  by get " + i);
                v.clear();
                run--;
                v.notifyAll();
            }
        }

    }

}