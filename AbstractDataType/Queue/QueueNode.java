package AbstractDataType.Queue;

/**
 * queue node for linked implementation of circular bounded queue
 *
 * @param <T> type of data to store
 */
class QueueNode<T> {
    T value;
    QueueNode<T> next;

    public QueueNode(T value) {
        this.value = value;
        next = null;
    }
}
