/*
  Created by Abdullin Ruslan on 13/02/2022.
 */

package AbstractDataType.Queue;

/**
 * ADT in which the operations are performed based on FIFO principle
 * and the last position is connected back to the first position to make a circle.
 * @param <T> type of data to store
 */
public class CircularBoundedQueue<T> implements ICircularBoundedQueue<T> {
    private QueueNode<T> head, tail;
    private int size;
    private final int capacity;

    /**
     * set the maximum size of the queue,
     * the initial number of elements,
     * and the value of the "head" and "tail"
     *
     * @param capacity maximum size of the queue, defined by user
     */
    public CircularBoundedQueue(int capacity) {
        head = null;
        tail = null;
        size = 0;
        this.capacity = capacity;
    }

    /**
     * insert an element to the "tail" of the queue
     * overwrite the "head" elements when the queue is full
     *
     * @param value to added in the queue
     */
    @Override
    public void offer(T value) {
        QueueNode<T> QueueNode = new QueueNode<>(value);
        if (isEmpty()) head = QueueNode;
        else {
            if (isFull()) poll();
            tail.next = QueueNode;
        }
        tail = QueueNode;
        size++;
    }

    /**
     * remove an element from the "head" of the queue
     *
     * @return the value of the element being deleted
     */
    @Override
    public T poll() {
        if (isEmpty())
            throw new IllegalStateException("Queue is empty");
        T output = head.value;
        head = head.next;
        size--;
        return output;
    }

    /**
     * return element at the "head" of the queue
     *
     * @return current "head" value
     */
    @Override
    public T peek() {
        if (isEmpty())
            throw new IllegalStateException("Queue is empty");
        return head.value;
    }

    /**
     * remove all queue elements
     */
    @Override
    public void flush() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * check if the queue is empty
     *
     * @return true if queue is empty, otherwise return false
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * check if the queue is full
     *
     * @return true if queue is full, otherwise return false
     */
    @Override
    public boolean isFull() {
        return size == capacity;
    }

    /**
     * return current number of elements in a queue
     *
     * @return queue size
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * return the maximum possible number of elements
     *
     * @return queue capacity
     */
    @Override
    public int capacity() {
        return capacity;
    }
}