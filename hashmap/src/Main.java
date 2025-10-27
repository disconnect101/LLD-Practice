import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        MyMap<String, Integer> m = new MyHashMap<>(10);
        m.put("A", 70);
        m.put("B", 180);
        m.put("C", 130);

        System.out.println("value for 'C': " + m.get("C"));

        m.remove("B");

        m.printMap();
    }
}



// add to map
// remove from map
// get from map
// resize map
// shrink back map

interface MyMap<T, V> {
    public void put(T key, V value);
    public V get(T key);
    public void remove(T key);
    public void printMap();
}

class MyHashMap<T, V> implements MyMap<T, V> {

    private int size;
    private Node []list;

    MyHashMap(int size) {
        this.size = size;
        this.list = new Node[size];
    }

    @Override
    public void put(T key, V value) {
        int hash = key.hashCode();
        int idx = hash % this.size;

        if (this.list[idx] == null) {
            this.list[idx] = new Node<T, V>(key, value);
        } else {
            Node<T, V> node = this.list[idx];
            while (node.getNext() != null) {
                node = node.getNext();
            }
            node.setNext(new Node<>(key, value));
        }
    }

    @Override
    public V get(T key) {
        int hash = key.hashCode();
        int idx = hash % this.size;

        Node<T, V> node = this.list[idx];
        while (node != null) {
            if (node.getKey() == key) return node.getValue();
        }
        return null;
    }

    @Override
    public void remove(T key) {
        int hash = key.hashCode();
        int idx = hash % this.size;

        Node<T, V> node = this.list[idx];
        if (node != null && node.getKey() == key) {
            this.list[idx] = node.getNext();
            return;
        }
        while (node != null && node.getNext() != null) {
            if (node.getNext().getKey() == key) {
                node.setNext(node.getNext().getNext());
                return;
            }
        }
    }

    @Override
    public void printMap() {
        for (Node<T, V> node : this.list) {
            while (node != null) {
                System.out.println(node.getKey() + ": " + node.getValue());
                node = node.getNext();
            }
        }
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