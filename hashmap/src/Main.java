//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        MyMap<String, Integer> m = new MyHashMap<>(8, 0.75f);
        m.put("A", 70);
        m.put("B", 180);
        m.put("I", 1430);
        m.put("J", 430);
        m.put("C", 130);
        m.put("D", 130);

        m.printMap();

        m.put("H", 10);
        System.out.println("value for 'C': " + m.get("C"));

        m.printMap();

        m.remove("B");
        m.remove("J");
        m.put("Q", 90);

        m.printMap();

        m.remove("A");

        m.printMap();
    }
}



// add to map
// remove from map
// get from map
// resize map

interface MyMap<T, V> {
    public void put(T key, V value);
    public V get(T key);
    public void remove(T key);
    public void printMap();
}

class MyHashMap<T, V> implements MyMap<T, V> {

    private int size;
    private Node<T, V> []list;
    private float loadFactor;
    private int currentSize;

    MyHashMap(int size, float loadFactor) {
        this.size = getOptimalSize(size);
        this.list = new Node[size];
        this.loadFactor = loadFactor;
        this.currentSize = 0;
    }

    private int getOptimalSize(int size) {
        int optimalSize = 1;

        while (optimalSize < size ) {
            optimalSize <<= 1;
        }
        return optimalSize;
    }

    @Override
    public void put(T key, V value) {
        int idx = getIdx(key);

        if (this.list[idx] == null) {
            this.list[idx] = new Node<T, V>(key, value);
        } else {
            Node<T, V> node = this.list[idx];
            while (node.getNext() != null) {
                if (node.getKey() == key) {
                    node.setValue(value);
                    return;
                }
                node = node.getNext();
            }
            System.out.println("Key collision for key: " + key);
            node.setNext(new Node<>(key, value));
        }
        this.currentSize++;
        doResizeIfRequired();
    }

    @Override
    public V get(T key) {
        int idx = getIdx(key);

        Node<T, V> node = this.list[idx];
        while (node != null) {
            if (node.getKey() == key) return node.getValue();
            node = node.getNext();
        }
        return null;
    }

    @Override
    public void remove(T key) {
        int idx = getIdx(key);

        Node<T, V> node = this.list[idx];
        if (node != null && node.getKey() == key) {
            this.list[idx] = node.getNext();
            this.currentSize--;
            return;
        }
        while (node != null && node.getNext() != null) {
            if (node.getNext().getKey() == key) {
                node.setNext(node.getNext().getNext());
                this.currentSize--;
                System.out.println("Key removed: " + key);
                return;
            }
        }
    }

    @Override
    public void printMap() {
        int i = 0;
        for (Node<T, V> node : this.list) {
            System.out.print(i + ": ");
            while (node != null) {
                System.out.print(node.getKey() + ": " + node.getValue() + " ");
                node = node.getNext();
            }
            System.out.println();
            i++;
        }
        System.out.println("\n");
    }

    private int getIdx(T key) {
        int hash = key.hashCode();
        return hash & (this.size - 1);
    }

    private void doResizeIfRequired() {
        float currentLoad = (float) this.currentSize / this.size;
        if (currentLoad > this.loadFactor) {
            triggerResize();
        }
    }

    private void triggerResize() {
        System.out.println("Hashmap resize triggered!!");
        this.size <<= 1;
        Node<T, V> []newList = new Node[this.size];

        for (int i = 0 ; i<this.list.length ; i++) {
            Node<T, V> node = this.list[i];
            if (node == null) continue;

            while (node != null) {
                T key = node.getKey();
                int newIndex = getIdx(key);

                if (newList[newIndex] == null) newList[newIndex] = node;
                else {
                    Node<T, V> tempNode = newList[newIndex];
                    while (tempNode.getNext() != null) {
                        tempNode = tempNode.getNext();
                    }
                    tempNode.setNext(node);
                }
                Node<T, V> nextNode = node.getNext();
                node.setNext(null);
                node = nextNode;
            }
        }

        this.list = newList;
    }


}

class Node<T, V> {


    private T key;
    private V value;
    private Node<T, V> next;

    public T getKey() {
        return key;
    }

    public void setKey(T key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    Node(T key, V value) {
        this.key = key;
        this.value = value;
    }

    public Node<T, V> getNext() {
        return next;
    }
    public void setNext(Node<T, V> next) {
        this.next = next;
    }

}