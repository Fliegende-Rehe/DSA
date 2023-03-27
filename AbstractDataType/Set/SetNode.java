package AbstractDataType.Set;

/**
 * hash set node which stores the value of the element
 * and a variable that determines the presence of the next element
 *
 * @param <T> type of data to store
 */
class SetNode<T> {
    T item;
    boolean next;

    public SetNode() {
        item = null;
        next = false;
    }
}
