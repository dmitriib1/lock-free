package io.github.dmitriibundin.lockfree;

import io.github.dmitriibundin.lockfree.queue.ConcurrentLinkedQueue;
import io.github.dmitriibundin.lockfree.queue.Queue;

import java.util.ArrayList;
import java.util.List;

public class Main {
    // A simple experiment with ConcurrentBoundedArrayStack
    public static void main(String args[]) throws InterruptedException {
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
        Thread reader = new Thread(() -> {
            List<Integer> values = new ArrayList<>();
            while(values.size() < 8192 * 2) {
                Integer tail = q.remove();
                if(tail == null) continue;
                values.add(tail);
            }
            System.out.println(values);
        });
        writer1.start();
        writer2.start();
        reader.start();
        writer1.join();
        writer2.join();
        reader.join();
    }
}
