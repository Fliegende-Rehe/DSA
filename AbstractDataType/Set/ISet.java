/*
  Created by Abdullin Ruslan on 13/02/2022.
 */

package AbstractDataType.Set;

/**
 * interface declare command list for double hash set
 *
 * @param <T> type of data to store
 */
interface ISet<T> {
    void add(T item); // time complexity (tc) - O(n)

    void remove(T item); // time complexity (tc) - O(n)

    boolean contains(T item); // time complexity (tc) - O(n)

    int size(); // time complexity (tc) - O(1)

    boolean isEmpty(); // time complexity (tc) - O(1)
}
