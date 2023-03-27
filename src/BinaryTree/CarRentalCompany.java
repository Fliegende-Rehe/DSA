package BinaryTree;// ru.abdullin@innopolis.university
// Ruslan Abdullin

import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class CarRentalCompany {
    public static void main(String[] args) {
        Graph<String, Double> gr = new Graph<>();
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        sc.nextLine();
        for (int i = 0; i < n; i++) {
            String command = sc.nextLine();
            String[] splitedCommand = command.split(" ");
            switch (splitedCommand[0]) {
                case "ADD":
                    gr.insertVertex(splitedCommand[1], Double.parseDouble(splitedCommand[2]));
                    break;
                case "CONNECT":
                    Vertex<String, Double> from, to;
                    from = gr.vertices.get(splitedCommand[1]);
                    to = gr.vertices.get(splitedCommand[2]);
                    gr.insertEdge(from, to, Double.parseDouble(splitedCommand[3]));
                    break;
                case "PRINT_MIN":
                    Double[] key = new Double[gr.connectedVertices.size()];
                    boolean[] isInPQ = new boolean[gr.connectedVertices.size()];
                    for (int j = 0; j < gr.connectedVertices.size(); j++) {
                        key[j] = 9999999.9;
                        isInPQ[j] = false;
                    }
                    DoublyLinkedList<Vertex<String, Double>> vertexToCheck = new DoublyLinkedList<>();
                    HashMap<String, DoublyLinkedList.Node> refToNodes = new HashMap<>();
                    PriorityQueue<Double, Vertex<String, Double>> pq = new PriorityQueue<>();
                    Vertex<String, Double>[] parent = new Vertex[gr.connectedVertices.size()];
                    Node<Double, Vertex<String, Double>>[] referenceToEdge = new Node[gr.connectedVertices.size()];
                    for (Vertex<String, Double> v : gr.connectedVertices) refToNodes.put(v.value, vertexToCheck.add(v));
                    while (vertexToCheck.size() != 0)
                        findMinimumSpanningTree(gr, vertexToCheck.head.value, key, isInPQ, refToNodes, vertexToCheck, pq, parent, referenceToEdge);
                    System.out.println();
                    for (Vertex<String, Double> v : gr.connectedVertices) v.isInTree = false;
                    break;
            }
        }
    }

    interface IGraph<V, E> {
        Object insertVertex(V v, Double penalty);

        Edge<V, E> insertEdge(Vertex<V, E> from, Vertex<V, E> to, E w);

        void removeVertex(Vertex<V, E> v);

        void removeEdge(Edge<V, E> e);

        boolean areAdjacent(Vertex<V, E> v, Vertex<V, E> u);

        int degree(Vertex<V, E> v);
    }

    interface IPriorityQueue<K, V> {
        void insert(Node<K, V> item);

        V findMin();

        Object extractMin();

        void decreaseKey(Object item, K newKey);

        void delete(Object item);

        void union(PriorityQueue anotherQueue);
    }

    public static void findMinimumSpanningTree(Graph<String, Double> graph, Vertex<String, Double> beginningVertex,
                                               Double[] key, boolean[] isInPQ, HashMap<String, DoublyLinkedList.Node> refToNodes,
                                               DoublyLinkedList<Vertex<String, Double>> vertexToCheck, PriorityQueue<Double, Vertex<String, Double>> pq,
                                               Vertex<String, Double>[] parent, Node<Double, Vertex<String, Double>>[] referenceToEdge) {
        key[beginningVertex.index] = 0.0;
        isInPQ[beginningVertex.index] = true;
        parent[beginningVertex.index] = beginningVertex;
        Node<Double, Vertex<String, Double>> initialRef = new Node<>(0.0, beginningVertex);
        referenceToEdge[beginningVertex.index] = initialRef;
        pq.insert(initialRef);

        while (!pq.isEmpty()) {
            Vertex<String, Double> ver = (Vertex<String, Double>) pq.extractMin();
            ver.isInTree = true;
            vertexToCheck.remove(refToNodes.get(ver.value));
            if (!ver.value.equals(beginningVertex.value))
                System.out.print(ver.value + ":" + parent[ver.index].value + " ");

            for (int i = 0; i < graph.numOfVert; i++) { // adds new vertexes to PQ or decreases keys
                Edge<String, Double> edge = (Edge<String, Double>) graph.adjMatrix[ver.index][i];
                if (edge == null)
                    continue;

                if (!edge.to.isInTree && key[edge.to.index] > (edge.weight / (edge.from.penalty + edge.to.penalty))) {
                    key[edge.to.index] = (edge.weight / (edge.from.penalty + edge.to.penalty));
                    parent[edge.to.index] = ver;
                    if (!isInPQ[edge.to.index]) {
                        Node<Double, Vertex<String, Double>> newRef = new Node<>(key[edge.to.index], edge.to);
                        referenceToEdge[edge.to.index] = newRef;
                        pq.insert(newRef);
                        isInPQ[edge.to.index] = true;
                    } else pq.decreaseKey(referenceToEdge[edge.to.index], key[edge.to.index]);
                }
            }
        }
    }


    static class Graph<V, E> implements IGraph<V, E> {
        public int numOfVert, numOfConnectedVerts;
        public DoublyLinkedList<Edge<V, E>> edges;
        public HashMap<V, Vertex<V, E>> vertices;
        public Object[][] adjMatrix;
        public DoublyLinkedList<Vertex<V, E>> connectedVertices;

        public Graph() {
            edges = new DoublyLinkedList<>();
            vertices = new HashMap<>();
            connectedVertices = new DoublyLinkedList<>();
            int MAXNUMOFVERTICES = 3000;
            adjMatrix = new Object[MAXNUMOFVERTICES][MAXNUMOFVERTICES];
            this.numOfVert = 0;
            this.numOfConnectedVerts = 0;
        }

        @Override
        public Vertex<V, E> insertVertex(V v, Double penalty) {
            Vertex vertex = new Vertex(v, penalty, numOfVert);
            this.vertices.put(v, vertex);
            numOfVert++;
            return vertex;
        }

        @Override
        public Edge<V, E> insertEdge(Vertex<V, E> from, Vertex<V, E> to, E w) {
            if (from.isAlone) {
                from.isAlone = false;
                from.index = numOfConnectedVerts;
                numOfConnectedVerts++;
                this.connectedVertices.add(from);
            }
            if (to.isAlone) {
                to.isAlone = false;
                to.index = numOfConnectedVerts;
                numOfConnectedVerts++;
                this.connectedVertices.add(to);
            }

            Edge<V, E> edge = new Edge(w, from, to);
            edge.refEdge = this.edges.add(edge);
            adjMatrix[from.index][to.index] = edge;
            edge = new Edge(w, to, from);
            adjMatrix[to.index][from.index] = edge;
            return edge;
        }

        @Override
        public void removeVertex(Vertex<V, E> v) {
            this.vertices.remove(v);
        }

        @Override
        public void removeEdge(Edge<V, E> e) {
            this.edges.remove(e.refEdge);
            adjMatrix[e.from.index][e.to.index] = null;
            adjMatrix[e.to.index][e.from.index] = null;
        }

        @Override
        public boolean areAdjacent(Vertex<V, E> v, Vertex<V, E> u) {
            return adjMatrix[v.index][u.index] != null;
        }

        @Override
        public int degree(Vertex<V, E> v) {
            int deg = 0;
            for (int i = 0; i < numOfVert; i++) {
                if (adjMatrix[v.index][i] != null) {
                    deg++;
                }
            }
            return deg;
        }
    }

    static class Vertex<V, E> {
        V value;
        Double penalty;
        boolean isInTree;
        boolean isAlone;
        int index;

        public Vertex(V v, Double penalty, int index) {
            this.value = v;
            this.penalty = penalty;
            this.isInTree = false;
            this.index = index;
            this.isAlone = true;
        }
    }

    static class Edge<V, E> {
        Vertex<V, E> from;
        Vertex<V, E> to;
        DoublyLinkedList.Node refEdge;
        E weight;

        public Edge(E w, Vertex<V, E> from, Vertex<V, E> to) {
            this.weight = w;
            this.from = from;
            this.to = to;
        }
    }

    static class PriorityQueue<K, V> implements IPriorityQueue<K, V> {
        public FibonacciHeap fheap;
        public int size = 0;

        PriorityQueue() {
            fheap = new FibonacciHeap();
        }

        @Override
        public void insert(Node<K, V> item) {
            this.size++;
            fheap.insert(item);
        }

        public boolean isEmpty() {
            return this.size == 0;

        }

        @Override
        public V findMin() {
            return (V) fheap.find_min();
        }

        @Override
        public Object extractMin() {
            this.size--;
            return fheap.extract_min();
        }

        @Override
        public void decreaseKey(Object item, K newKey) {
            fheap.decreaseKey((Node) item, newKey);
        }

        @Override
        public void delete(Object item) {
            this.size--;
            fheap.delete(fheap.find(item));
        }

        @Override
        public void union(PriorityQueue anotherQueue) {
            fheap = fheap.union(fheap, anotherQueue.fheap);
        }
    }

    static class Node<K, V> {
        int degree;
        boolean mark;
        K key;
        V value;
        Node<K, V> parent, left, right, child;

        public Node() {
            this.degree = 0;
            this.mark = false;
            this.parent = null;
            this.left = this;
            this.right = this;
            this.child = null;
        }

        Node(K key, V value) {
            this();
            this.key = key;
            this.value = value;
        }

        public void cascadingCut(Node<K, V> min) {
            Node<K, V> z = this.parent;
            if (z == null) return;
            if (mark) {
                z.cut(this, min);
                z.cascadingCut(min);
            } else this.mark = true;
        }

        public void cut(Node<K, V> x, Node<K, V> min) {
            x.left.right = x.right;
            x.right.left = x.left;
            this.degree--;
            if (this.degree == 0) this.child = null;
            else if (this.child == x) this.child = x.right;
            x.right = min;
            x.left = min.left;
            min.left = x;
            x.left.right = x;
            x.parent = null;
            x.mark = false;
        }

        public void link(Node<K, V> parent) {
            this.left.right = this.right;
            this.right.left = this.left;
            this.parent = parent;
            if (parent.child == null) {
                parent.child = this;
                this.right = this;
                this.left = this;
            } else {
                this.left = parent.child;
                this.right = parent.child.right;
                parent.child.right = this;
                this.right.left = this;
            }
            parent.degree++;
            this.mark = false;
        }

        K get_key() {
            return this.key;
        }

        public Node<K, V> get_child() {
            return this.child;
        }

        public Node<K, V> get_right() {
            return this.right;
        }
    }

    static class FibonacciHeap<K, V> {
        public Node<K, V> min;
        private int size;
        private Node<K, V> found;

        public static FibonacciHeap union(FibonacciHeap H1, FibonacciHeap H2) {
            FibonacciHeap H = new FibonacciHeap();
            if (H1 != null && H2 != null) {
                H.min = H1.min;
                if (H.min != null && H2.min != null) {
                    H.min.right.left = H2.min.left;
                    H2.min.left.right = H.min.right;
                    H.min.right = H2.min;
                    H2.min.left = H.min;
                    if ((Double) H2.min.key < (Double) H1.min.key) H.min = H2.min;
                } else H.min = H2.min;
                H.size = H1.size + H2.size;
            }
            return H;
        }

        public Node find(K k) {
            found = null;
            find(k, this.min);
            return found;
        }

        private void find(K key, Node<K, V> node1) {
            if (found != null || node1 == null) return;
            Node<K, V> temp = node1;
            do {
                if (key != temp.get_key()) {
                    Node<K, V> k = temp.get_child();
                    find(key, k);
                    temp = temp.get_right();
                } else found = temp;
            } while (temp != node1 && found == null);
        }

        private void consolidate() {
            Node<K, V>[] arr = new Node[45];
            Node<K, V> start = min, p = min;
            do {
                Node<K, V> x = p;
                Node<K, V> nextW = p.right;
                int d = x.degree;
                while (arr[d] != null) {
                    Node<K, V> y = arr[d];
                    if ((Double) x.key > (Double) y.key) {
                        Node<K, V> temp = y;
                        y = x;
                        x = temp;
                    }
                    if (y == start) start = start.right;
                    if (y == nextW) nextW = nextW.right;
                    y.link(x);
                    arr[d] = null;
                    d++;
                }
                arr[d] = x;
                p = nextW;
            } while (p != start);
            this.min = start;
            for (Node<K, V> a : arr) {
                if (a != null && (Double) a.key < (Double) min.key) this.min = a;
            }
        }

        public void decreaseKey(Node<K, V> node1, K newKey) {
            decreaseKey(node1, newKey, false);
        }

        private void decreaseKey(Node<K, V> node1, K newKey, boolean delete) {
            if (!delete && (Double) newKey > (Double) node1.key) return;
            node1.key = newKey;
            Node<K, V> par = node1.parent;
            if (par != null && (delete || (Double) newKey < (Double) par.key)) {
                par.cut(node1, this.min);
                par.cascadingCut(this.min);
            }
            if (delete || (Double) newKey < (Double) this.min.key) this.min = node1;
        }

        public void delete(Node<K, V> x) {
            decreaseKey(x, null, true);
            extract_min();
        }

        public boolean isEmpty() {
            return min == null;
        }

        public Node<K, V> insert(Node<K, V> x) {
            if (min != null) {
                x.right = min;
                x.left = min.left;
                min.left = x;
                x.left.right = x;
                if ((Double) x.key < (Double) min.key) min = x;
            } else min = x;
            size++;
            return x;
        }

        public V extract_min() {
            Node<K, V> l = this.min;
            if (l == null) return null;
            if (l.child != null) {
                l.child.parent = null;
                for (Node<K, V> i = l.child.right; i != l.child; i = i.right) {
                    i.parent = null;
                }
                Node<K, V> minleft = this.min.left;
                Node<K, V> lchildleft = l.child.left;
                this.min.left = lchildleft;
                lchildleft.right = this.min;
                l.child.left = minleft;
                minleft.right = l.child;
            }
            l.left.right = l.right;
            l.right.left = l.left;
            if (l != l.right) {
                this.min = l.right;
                consolidate();
            } else this.min = null;
            this.size--;
            return l.value;
        }

        public int size() {
            return this.size;
        }

        public V find_min() {
            return min.value;
        }
    }

    static class DoublyLinkedList<E> implements Iterable<E> {
        public Node head;
        private int size;

        public DoublyLinkedList() {
            this.head = null;
            this.size = 0;
        }

        @Override
        public Iterator<E> iterator() {
            return new DoublyLinkedListIterator(this.head);
        }

        public Node add(E value) {
            Node node = new Node(value);
            if (head != null) {
                node.next = head;
                head.prev = node;
            }
            head = node;
            size += 1;

            return node;
        }

        public int size() {
            return this.size;
        }

        public void remove(Node n) {
            if (n.prev == null) {
                head = n.next;
                if (n.next != null) n.next.prev = null;
            } else if (n.next == null) {
                n.prev.next = null;
            } else {
                n.next.prev = n.prev;
                n.prev.next = n.next;
            }
            size -= 1;
        }

        public class DoublyLinkedListIterator implements Iterator<E> {
            Node current;

            public DoublyLinkedListIterator(Node current) {
                this.current = current;
            }

            @Override
            public boolean hasNext() {
                return (current != null);
            }

            @Override
            public E next() {
                E value = current.value;
                current = current.next;
                return value;
            }
        }

        public class Node {
            E value;
            Node prev = null;
            Node next = null;

            public Node(E value) {
                this.value = value;
            }
        }
    }
}