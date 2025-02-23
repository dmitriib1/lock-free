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
            for (int i = 0; i < 8192; i++){
                q.enqueu(i);
            }
        });
        Thread writer2 = new Thread(() -> {
            for (int i = 0; i < 8192; i++){
                q.enqueu(i);
            }
        });
        Thread reader1 = new Thread(() -> {
            for(int i = 0; i < 1000000; i++) {
                Integer tail = q.remove();
                if(tail == null) continue;
                values.add(tail);
            }
        });
        Thread reader2 = new Thread(() -> {
            for(int i = 0; i < 1000000; i++) {
                Integer tail = q.remove();
                if(tail == null) continue;
                values.add(tail);
            }
        });
        writer1.start();
        writer2.start();
        reader1.start();
        reader2.start();
        writer1.join();
        writer2.join();
        reader1.join();
        reader2.join();
        System.out.println(values.size());
    }
}
