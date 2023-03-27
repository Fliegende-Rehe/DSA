/*
  Created by Abdullin Ruslan on 13/02/2022.
 */

package AbstractDataType.Stack;

/**
 * interface declare command list for bounded stack
 *
 * @param <T> type of data to store
 */
interface IBoundedStack<T> {
    void push(T value); // time complexity (tc) - O(n)

    T pop(); // tc - O(1)

    T top(); // tc - O(1)

    void flush(); // tc - O(1)

    boolean isEmpty(); // tc - O(1)

    boolean isFull(); // tc - O(1)

    int size(); // tc - O(1)

    int capacity(); // tc - O(1)
}
