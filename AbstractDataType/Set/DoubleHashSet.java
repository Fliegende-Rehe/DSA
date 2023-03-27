/*
  Created by Abdullin Ruslan on 13/02/2022.
 */

package AbstractDataType.Set;

/**
 * an element collection that uses a hash table for storage
 * double hashing allows avoiding collision
 *
 * @param <T> type of elements to store
 */
public class DoubleHashSet<T> implements ISet<T> {
    private final int capacity, prime;
    private int size;
    SetNode<T>[] table;

    /**
     * set the maximum size of the set,
     * the initial number of elements,
     * and array of nodes
     *
     * @param capacity maximum size of the set, defined by user
     */
    @SuppressWarnings("unchecked")
    public DoubleHashSet(int capacity) {
        this.capacity = capacity;
        size = 0;
        prime = getPrime(capacity);
        table = (SetNode<T>[]) new SetNode<?>[capacity];
        for (int i = 0; i < capacity; i++) table[i] = new SetNode<>();
    }

    /**
     * get prime number the closest to capacity for the second hash code
     *
     * @param n capacity of hash set
     * @return prime number
     */
    private int getPrime(int n) {
        if (n % 2 != 0) n -= 2;
        else n--;
        for (int i = n; i >= 2; i -= 2) {
            int j;
            for (j = 3; j <= Math.sqrt(i); j += 2) {
                if (i % j == 0) break;
            }
            if (j > Math.sqrt(i)) return i;
        }
        return 0;
    }

    /**
     * return first hash code of element
     *
     * @param key absolute value of element hash
     * @return first hash code of element
     */
    private int hashCode(int key) {
        return key % capacity;
    }

    /**
     * return second hash code of element
     * use prime number the closet to set capacity
     *
     * @param key absolute value of element hash
     * @return second hash code of element
     */
    private int hashCode2(int key) {
        return prime - (key % prime);
    }

    /**
     * add item in the set
     *
     * @param item to be added
     */
    @Override
    public void add(T item) {
        int key = Math.abs(item.hashCode());
        int h1 = hashCode(key);
        int h2 = hashCode2(key);
        if (table[h1].item != null) {
            table[h1].next = true;
            int h = (h1 + h2) % capacity;
            for (int i = 2; table[h].item != null; i++) {
                table[h].next = true;
                h = (h1 + h2 * i) % capacity;
            }
            h1 = h;
        }
        table[h1].item = item;
        size++;
    }

    /**
     * remove an item from a set
     *
     * @param item to be removed
     */
    @Override
    public void remove(T item) {
        int key = Math.abs(item.hashCode());
        int h1 = hashCode(key);
        int h2 = hashCode2(key);
        int h = h1 % capacity;
        for (int i = 1; table[h].item == null
                || !table[h].item.equals(item); i++) {
            if (!table[h].next) return;
            h = (h1 + h2 * i) % capacity;
        }
        table[h].item = null;
        size--;
    }

    /**
     * check if an item belongs to a set
     *
     * @param item to be checked
     * @return true id element belongs to set, otherwise return false
     */
    @Override
    public boolean contains(T item) {
        int key = Math.abs(item.hashCode());
        int h1 = hashCode(key);
        int h2 = hashCode2(key);
        int h = h1 % capacity;
        for (int i = 1; table[h].item == null
                || !table[h].item.equals(item); i++) {
            if (!table[h].next) return false;
            h = (h1 + h2 * i) % capacity;
        }
        return true;
    }

    /**
     * return current number of elements in a set
     *
     * @return set size
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * check if the set is empty
     *
     * @return true if set is empty, otherwise return false
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * print all elements from set
     */
    public void List() {
        for (int i = 0; i < capacity; i++) {
            if (table[i].item != null) System.out.print(table[i].item + " ");
        }
    }
}