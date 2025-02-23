package io.github.dmitriibundin.lockfree.queue;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Michael-Scott algorithm implementation for linked queue.
 * Unbounded lock-free queue without {@code null} values.
 *
 * @param <T>
 */
public class ConcurrentLinkedQueue<T> implements Queue<T> {
    private static class Node<T> {
        private final AtomicReference<T> value;
        private final AtomicReference<Node<T>> next;

        private Node(T value, Node<T> next) {
            this.value = new AtomicReference<>(value);
            this.next = new AtomicReference<>(next);
        }

        private Node(T value) {
            this(value, null);
        }

        private Node() {
            this(null, null);
        }
    }

    private AtomicReference<Node<T>> head;
    private AtomicReference<Node<T>> tail;

    public ConcurrentLinkedQueue() {
        Node<T> initial = new Node<>();
        head = new AtomicReference<>(initial);
        tail = new AtomicReference<>(initial);
    }

    @Override
    public void enqueu(T t) {
        if(t == null) throw new NullPointerException();

        while(true) {
            Node<T> currentHead = head.get();
            Node<T> next = currentHead.next.get();
            if(next == null) {
                if(currentHead.next.compareAndSet(null, new Node<>(t))) break;
            } else {
                head.compareAndSet(currentHead, next);
            }
        }
    }

    @Override
    public T remove() {
        while(true) {
            Node<T> currentTail = tail.get();
            T currentTailValue = currentTail.value.get();
            Node<T> next = currentTail.next.get();
            if(next == null) {
                if(currentTail.value.compareAndSet(currentTailValue, null)) return currentTailValue;
            } else if (currentTailValue == null) {
                tail.compareAndSet(currentTail, next);
            } else {
                if(tail.compareAndSet(currentTail, next)) return currentTailValue;
            }
        }
    }
}
