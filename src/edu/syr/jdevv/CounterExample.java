// This program is based on the Java example in Java PathExplorer.
// 04/02/2016

package edu.syr.jdevv;

class Value {
    private int x = 1;

    public void add(Value v) {
        x = x + v.get();
    }

    public int get() {
        return x;
    }
}

public class CounterExample extends Thread {
    Value v1;
    Value v2;

    public CounterExample(Value v1, Value v2) {
        this.v1 = v1;
        this.v2 = v2;
        this.start();
    }

    public void run() {
        for (int i = 0; i < 10; i++) {
            v1.add(v2);
            //System.out.println("Thread " + Thread.currentThread().getId() + ": " + v1.get());
        }
    }

    public static void main(String[] args) {
        Value a = new Value();
        Value b = new Value();
        new CounterExample(a, b);
        new CounterExample(b, a);
    }
}
