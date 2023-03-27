/*
  Created by Abdullin Ruslan on 13/02/2022.
 */

package AbstractDataType.Stack;

import AbstractDataType.Queue.CircularBoundedQueue;

public class QueuedBoundedStack<T> implements IBoundedStack<T> {
    private final int capacity;
    private CircularBoundedQueue<T> stack;

    /**
     * set the maximum size of the stack,
     * the initial number of elements,
     * and the queue that will be used to implement the stack
     *
     * @param capacity maximum size of the stack, defined by user
     */
    public QueuedBoundedStack(int capacity) {
        this.capacity = capacity;
        stack = new CircularBoundedQueue<>(capacity);
    }

    /**
     * push an element onto the stack
     * remove the oldest element if stack is full
     *
     * @param value to push
     */
    @Override
    public void push(T value) {
        CircularBoundedQueue<T> queue = new CircularBoundedQueue<T>(capacity);
        queue.offer(value);
        int limit = stack.size();
        if (!stack.isFull())
            limit--;
        for (int i = 0; i < limit; i++)
            queue.offer(stack.poll());
        stack = queue;
    }

    /**
     * remove an element from the top of the stack
     *
     * @return the value of the element being deleted
     */
    @Override
    public T pop() {
        if (isEmpty())
            throw new IllegalStateException("Stack is empty");
        T output = stack.peek();
        stack.poll();
        return output;
    }

    /**
     * look at the element at the top of the stack
     * @return top value
     */
    @Override
    public T top() {
        if (isEmpty())
            throw new IllegalStateException("Stack is empty");
        return stack.peek();
    }

    /**
     * remove all stack elements
     */
    @Override
    public void flush() {
        stack.flush();
    }

    /**
     * check if the stack is empty
     *
     * @return true if stack is empty, otherwise return false
     */
    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    /**
     * check if the stack is full
     *
     * @return true if stack is full, otherwise return false
     */
    @Override
    public boolean isFull() {
        return stack.isFull();
    }

    /**
     * return current number of elements in a stack
     *
     * @return stack size
     */
    @Override
    public int size() {
        return stack.size();
    }

    /**
     * return the maximum possible number of elements
     *
     * @return stack capacity
     */
    @Override
    public int capacity() {
        return capacity;
    }
}
