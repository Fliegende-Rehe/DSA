/*
  Created by Abdullin Ruslan on 13/02/2022.
 */

package AbstractDataType.Queue;

/**
 * interface declare command list for circular bounded queue
 *
 * @param <T> type of data to store
 */
interface ICircularBoundedQueue<T> {
    void offer(T value); // time complexity (tc) - O(1)

    T poll(); // tc - O(1)

    T peek(); // tc - O(1)

    void flush(); // tc - O(1)

    boolean isEmpty(); // tc - O(1)

    boolean isFull(); // tc - O(1)

    int size(); // tc - O(1)

    int capacity(); // tc - O(1)
}