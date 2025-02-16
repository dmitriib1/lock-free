package io.github.dmitriibundin.lockfree;

import io.github.dmitriibundin.lockfree.stack.ConcurrentBoundedArrayStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Main {
    // A simple experiment with ConcurrentBoundedArrayStack
    public static void main(String args[]) throws InterruptedException {
        ConcurrentBoundedArrayStack<Integer> stack = new ConcurrentBoundedArrayStack<>(8192);
        Thread writer = new Thread(() -> {
            for (int i = 0; i < 8192; i++){
                stack.push(i);
            }
        });
        Thread reader = new Thread(() -> {
            List<Integer> values = new ArrayList<>();
            int counter = 0;
            while(values.size() < 8192) {
                Integer value = stack.pop();
                if(value != null) values.add(value);
                if(++counter % 1000000 == 0){
                    System.out.println("size = " + values.size());
                }
            }
            System.out.println("Total size = " + values.size() + "; distinct size = " + new HashSet<>(values).size());
            System.out.println(values);
        });
        writer.start();
        reader.start();
        writer.join();
        reader.join();
    }
}
