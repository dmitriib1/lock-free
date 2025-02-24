package io.github.dmitriibundin.lockfree;

import io.github.dmitriibundin.lockfree.queue.ConcurrentLinkedQueue;
import io.github.dmitriibundin.lockfree.queue.Queue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    // A simple experiment with ConcurrentBoundedArrayStack
    public static void main(String args[]) throws InterruptedException {
        List<Integer> values = Collections.synchronizedList(new ArrayList<>());
        Queue<Integer> q = new ConcurrentLinkedQueue<>();
        Thread writer1 = new Thread(() -> {
            for (int i = 0; i < 8192 * 4; i++){
                q.enqueu(i);
            }
        });
        Thread writer2 = new Thread(() -> {
            for (int i = 0; i < 8192 * 4; i++){
                q.enqueu(i);
            }
        });
        Thread writer3 = new Thread(() -> {
            for (int i = 0; i < 8192 * 4; i++){
                q.enqueu(i);
            }
        });
        Thread reader1 = new Thread(() -> {
            for(int i = 0; i < 10000000; i++) {
                Integer tail = q.poll();
                if(tail == null) continue;
                values.add(tail);
            }
        });
        Thread reader2 = new Thread(() -> {
            for(int i = 0; i < 10000000; i++) {
                Integer tail = q.poll();
                if(tail == null) continue;
                values.add(tail);
            }
        });
        Thread reader3 = new Thread(() -> {
            for(int i = 0; i < 10000000; i++) {
                Integer tail = q.poll();
                if(tail == null) continue;
                values.add(tail);
            }
        });
        reader1.start();
        writer1.start();
        writer1.join();
        reader1.join();
        writer2.start();
        reader2.start();
//        writer3.start();
//        reader3.start();
        writer2.join();
//        writer3.join();
        reader2.join();
//        reader3.join();
        System.out.println(values.size());
    }
}
