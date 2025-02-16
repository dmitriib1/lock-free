package io.github.dmitriibundin.lockfree.stack;

public interface Stack<T> {
    /**
     * Pushes the element to the stack.
     *
     * @param t
     * @return The element added to the stack.
     */
    T push(T t);

    /**
     * Removes the top of the stack.
     *
     * @return The top of the stack
     */
    T pop();
}
