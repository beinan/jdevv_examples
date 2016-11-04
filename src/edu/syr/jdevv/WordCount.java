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
    public static void main(String[] args) throws InterruptedException, IOException, URISyntaxException{
           Thread threads[] = new Thread[THREAD_COUNT];
//           InputStream is = WordCount.class.getClass().getResourceAsStream("/edu/syr/jdevv/words.txt");
//           Scanner scaner = new Scanner(is);
           List<String> lines = new ArrayList<String>();
           
           URI uri = WordCount.class.getClassLoader().getSystemResource("edu/syr/jdevv/words.txt").toURI();
           try (Stream<String> stream = Files.lines(Paths.get(uri))) {
               stream.forEach(lines::add);
           }
           
           WordCount counter = new WordCount();
           for(int i = 0; i < THREAD_COUNT; i++){
               threads[i] = new Thread(new WordCountWorker(i, counter, lines.toArray(new String[0])));
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
    private String[] lines;

    public WordCountWorker(int id, WordCount counter, String[] lines) {
        this.id = id;
        this.counter = counter;
        this.lines = lines;
        
    }

    @Override
    public void run() {
        for(int i = id; i < lines.length; i += WordCount.THREAD_COUNT){
            //synchronized(counter){
                counter.counter += lines[i].split(" ").length;
            //}
        }        
    }
    
}
