package edu.syr.jdevv;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class WordCount {
    
    static final int THREAD_COUNT = 10;
    int counter = 0;
    List<String> lines = new ArrayList<String>();
    
    public static void main(String[] args) throws InterruptedException, IOException, URISyntaxException{
           Thread threads[] = new Thread[THREAD_COUNT];
//           InputStream is = WordCount.class.getClass().getResourceAsStream("/edu/syr/jdevv/words.txt");
//           Scanner scaner = new Scanner(is);
           
           
           URI uri = WordCount.class.getClassLoader().getSystemResource("edu/syr/jdevv/words.txt").toURI();
           WordCount counter = new WordCount();
           try (Stream<String> stream = Files.lines(Paths.get(uri))) {
               stream.forEach(counter.lines::add);
           }
           
           
           for(int i = 0; i < THREAD_COUNT; i++){
               threads[i] = new Thread(new WordCountWorker(i, counter));
               threads[i].start();
           }
           for(int i = 0; i < THREAD_COUNT; i++){
               threads[i].join();
           }
           System.out.println(counter.counter);
    }
}

class WordCountWorker implements Runnable{

    private int id;
    private WordCount counter;


    public WordCountWorker(int id, WordCount counter) {
        this.id = id;
        this.counter = counter;       
    }

    @Override
    public void run() {
        for(int i = id; i < counter.lines.size(); i += WordCount.THREAD_COUNT){
            //synchronized(counter){
                counter.counter += counter.lines.get(i).split(" ").length;
            //}
        }        
    }
    
}
