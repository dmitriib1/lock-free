package io.github.dmitriibundin.lockfree.stack;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Array-based bounded concurrent lock-free stack implementation.
 *
 * @param <T> - the type of the element
 */
public class ConcurrentBoundedArrayStack<T> implements Stack<T> {
    private static final VarHandle ARRAY_VAR_HANDLE = MethodHandles.arrayElementVarHandle(Object[].class);

    private final Object[] array;
    private final AtomicInteger size;

    /**
     * Creates a stack with the predefined capacity
     *
     * @param maxCapacity - the maximum number of elements this instance of Stack can hold
     */
    public ConcurrentBoundedArrayStack(int maxCapacity) {
        this.array = new Object[maxCapacity];
        this.size = new AtomicInteger();
    }

    @Override
    public T push(T t) {
        if(t == null) {
            throw new NullPointerException("Null elements are not allowed");
        }

        int currentSize;
        T currentValue;
        do {
            currentSize = size.get();
            if(currentSize == array.length) {
                return null;
            }
            //noinspection unchecked
            currentValue = (T) ARRAY_VAR_HANDLE.getAcquire(array, currentSize);
        } while(currentValue != null || !size.compareAndSet(currentSize, currentSize + 1));
        ARRAY_VAR_HANDLE.setRelease(array, currentSize, t);
        return t;
    }

    @Override
    public T pop() {
        int currentSize;
        T currentValue;
        do {
            currentSize = size.get();
            if(currentSize == 0) {
                return null;
            }
            //noinspection unchecked
            currentValue = (T) ARRAY_VAR_HANDLE.getAcquire(array, currentSize - 1);
        } while(currentValue == null || !size.compareAndSet(currentSize, currentSize - 1));
        ARRAY_VAR_HANDLE.setRelease(array, currentSize - 1, null);
        return currentValue;
    }
}
