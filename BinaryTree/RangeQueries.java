package BinaryTree;// ru.abdullin@innopolis.university
// Ruslan Abdullin

import java.util.*;

public class RangeQueries {
    public static void main(String[] args) {
        RangeMap<String, Integer> RangeQueries = new RangeMap<>();
        ArrayList<Integer> result = new ArrayList<>();
        String[] input = BTIO.Read();

        for (String line : input) {
            boolean report = line.split(" ").length == 5 && line.split(" ")[0].equals("REPORT");
            if (line.split(" ").length == 3) {
                String date = line.split(" ")[0], type = line.split(" ")[1];
                int amount = Integer.parseInt(line.split(" ")[2]);
                if (type.equals("DEPOSIT")) RangeQueries.add(date, amount);
                else if (type.equals("WITHDRAW")) RangeQueries.add(date, -amount);
            } else if (report) {
                int total = 0;
                if (!RangeQueries.isEmpty()) {
                    for (Integer arr : RangeQueries.lookupRange(line.split(" ")[2], line.split(" ")[4]))
                        total += arr;
                }
                result.add(total);
            }
        }

        BTIO.Write(result);
    }
}
interface IRangeMap<K, V> {
    int size();

    boolean isEmpty();

    void add(K key, V value);

    boolean contains(K key);

    V lookup(K key);

    List<V> lookupRange(K from, K to);

    Objects remove(K key);
}

class RangeMap<K, V> implements IRangeMap<K, V> {
    public BTree<K, V> btree;
    private int size;

    public RangeMap() {
        btree = new BTree<>();
        size = 0;
    }

    @Override // O(1)
    public int size() {
        return this.size;
    }

    @Override // O(1)
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override // O(1)
    public void add(K key, V value) {
        this.btree.insert(key, value);
        this.size += 1;
    }

    @Override // O(1)
    public boolean contains(K key) {
        return this.btree.contains(key);
    }

    @Override // O(1)
    public V lookup(K key) {
        return this.btree.lookupValue(key);
    }

    @Override // O(1)
    public List<V> lookupRange(K from, K to) {
        return this.btree.traverseInorder(from, to);
    }

    @Override
    public Objects remove(K key) {
        return null;
    }
}

class BTree<K, V> {
    private final List<V> range;
    private Node<K, V> root;

    // O(1)
    public BTree() {
        this.root = new Node<>();
        this.range = new ArrayList<>();
    }

    // O(1)
    public V lookupValue(K key) {
        return getValue(root, key);
    }

    // O(log(n))
    private V getValue(Node<K, V> node, K key) {
        if (node == null) return null;
        int i;
        for (i = 0; i < node.n && key.toString().compareTo(node.key[i].toString()) < 0; i++) {
            if (!key.equals(node.key[i])) continue;
            if (getValue(node.child[i], key) == null) return node.values[i];
            return getValue(node.child[i], key);
        }
        if (node.isLeaf) return null;
        return getValue(node.child[i], key);
    }

    public List<V> traverseInorder(K st, K fn) {
        this.range.clear();
        traverseInorderNode(this.root, st, fn);
        return this.range;
    }

    // O(n)
    private void traverseInorderNode(Node<K, V> node, K st, K fn) {
        int i;
        for (i = 0; i < node.n; i++) {
            if (inRange(node.key[i], st, fn)) {
                if (!node.isLeaf) traverseInorderNode(node.child[i], st, fn);
                this.range.add(node.values[i]);
            } else if (node.key[i].toString().compareTo(fn.toString()) > 0 && !node.isLeaf) {
                traverseInorderNode(node.child[i], st, fn);
                return;
            }
        }

        if (inRange(node.key[i - 1], st, fn) && !node.isLeaf)
            traverseInorderNode(node.child[i], st, fn);
        else if (node.key[i - 1].toString().compareTo(st.toString()) < 0 && !node.isLeaf)
            traverseInorderNode(node.child[i], st, fn);
    }

    // O(1)
    private boolean inRange(K value, K st, K fn) {
        return value.toString().compareTo(st.toString()) >= 0 && value.toString().compareTo(fn.toString()) <= 0;
    }

    // O(n)
    private void split(Node<K, V> x, Node<K, V> y, int id) {
        Node<K, V> z = new Node<>();
        z.isLeaf = y.isLeaf;
        z.n = 1;
        z.key[0] = y.key[2];
        z.values[0] = y.values[2];

        if (!y.isLeaf) System.arraycopy(y.child, 2, z.child, 0, 2);
        y.n = 1;

        if (x.n - id >= 0) System.arraycopy(x.child, id + 1, x.child, id + 2, x.n - id);
        x.child[id + 1] = z;
        for (int j = x.n - 1; j >= id; j--) {
            x.key[j + 1] = x.key[j];
            x.values[j + 1] = x.values[j];
        }
        x.key[id] = y.key[1];
        x.values[id] = y.values[1];
        x.n += 1;
    }

    // O(log(n))
    public void insert(K key, V value) {
        Node<K, V> root = this.root;
        if (root.n == 3) {
            Node<K, V> node = new Node<>();
            this.root = node;
            node.isLeaf = false;
            node.n = 0;
            node.child[0] = root;
            split(node, root, 0);
            insertValue(node, key, value);
        } else insertValue(root, key, value);
    }

    // O(log(n))
    private void insertValue(Node<K, V> node, K key, V value) {
        int i;
        if (node.isLeaf) {
            for (i = node.n - 1; i >= 0 && key.toString().compareTo(node.key[i].toString()) <= 0; i--) {
                node.key[i + 1] = node.key[i];
                node.values[i + 1] = node.values[i];
            }
            node.key[i + 1] = key;
            node.values[i + 1] = value;
            node.n += 1;
        } else {
            for (i = node.n - 1; i >= 0 && key.toString().compareTo(node.key[i].toString()) <= 0; i--) {
            }
            i++;

            Node<K, V> tmp = node.child[i];
            if (tmp.n == 3) {
                split(node, tmp, i);
                if (key.toString().compareTo(node.key[i].toString()) > 0) i++;
            }
            insertValue(node.child[i], key, value);
        }
    }

    // O(log(n))
    public boolean contains(K key) {
        return this.getValue(root, key) != null;
    }
}

class Node<k, v> {
    int n;
    k[] key;
    v[] values;
    Node<k, v>[] child;
    boolean isLeaf;

    public Node() {
        n = 0;
        isLeaf = true;
        key = (k[]) new Object[3];
        values = (v[]) new Object[3];
        child = new Node[4];
    }
}

class BTIO {
    public static String[] Read() {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt(); sc.nextLine();
        String[] input = new String[N];
        for (int i = 0; i < N; i++) input[i] = sc.nextLine();
        sc.close();
        return input;
    }

    public static void Write(ArrayList<Integer> result) {
        for (Integer r : result) System.out.println(r);
    }
}