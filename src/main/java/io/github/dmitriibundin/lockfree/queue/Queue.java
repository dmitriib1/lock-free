package io.github.dmitriibundin.lockfree.queue;

public interface Queue<T> {

    /**
     * Enqueues an element into the queue
     *
     * @param t
     * @throws {@code NullPointerException} if the element is {@code null}
     */
    void enqueu(T t);

    /**
     * Retrieves and removes an element from the queue.
     *
     * @return The latest first element of the queue
     *         {@code null} if the queue is empty
     */
    T remove();
}
