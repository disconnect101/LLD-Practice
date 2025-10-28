import java.util.HashMap;
import java.util.Map;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        LRUCache<Integer> lruCache = new LRUCache<>(4);

        lruCache.put("A", 31);
        lruCache.printCache();

        lruCache.put("B", 61);
        lruCache.put("C", 41);

        System.out.println("value of key B: " + lruCache.get("B"));
        lruCache.printCache();

        lruCache.put("D", 31);
        lruCache.get("A");
        lruCache.printCache();

        lruCache.put("Z", 90);
        lruCache.printCache();

        lruCache.put("B", 80);
        lruCache.put("M", 76);

        lruCache.printCache();

    }
}

class LRUCache<T> {

    private Node<T> head;
    private Node<T> tail;
    private Map<String, Node<T>> map;
    private int size;
    private int capacity;

    LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();
        head = new Node<>("dummy-head", null);
        tail = new Node<>("dummy-tail", null);

        head.setNext(tail);
        tail.setBack(head);
        head.setBack(null);
        tail.setNext(null);
    }

    public T get(String key) {
        Node<T> node = map.get(key);
        if (node == null) {
            return null;
        }
        remove(node);
        addFront(node);
        return node.getValue();
    }

    public void put(String key, T value) {
        Node<T> node = map.get(key);
        if (node != null) {
            node.setValue(value);
            remove(node);
            addFront(node);
            return;
        }

        Node<T> newNode = new Node<>(key, value);
        addFront(newNode);
        map.put(key, newNode);
        if (this.size == this.capacity) {
            map.remove(tail.getBack().getKey());
            remove(tail.getBack());
        } else {
            this.size++;
        }
    }

    private void addFront(Node<T> node) {
        Node<T> firstNode = head.getNext();
        head.setNext(node);
        node.setBack(head);
        node.setNext(firstNode);
        firstNode.setBack(node);
    }

    private void remove(Node<T> node) {
        Node<T> backNode = node.getBack();
        Node<T> nextNode = node.getNext();

        backNode.setNext(nextNode);
        nextNode.setBack(backNode);
    }

    public void printCache() {
        Node<T> node = head.getNext();

        System.out.print("Cache: ");
        while (!node.getKey().equals("dummy-tail")) {
            System.out.print(node.getKey() + ":" + node.getValue() + " ");
            node = node.getNext();
        }
        System.out.println();
    }

}


class Node<T> {
    public Node<T> getNext() {
        return next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }

    public Node<T> getBack() {
        return back;
    }

    public void setBack(Node<T> back) {
        this.back = back;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private Node<T> next;
    private Node<T> back;
    private String key;
    private T value;

    Node(String key, T value) {
        this.key = key;
        this.value = value;
    }



}