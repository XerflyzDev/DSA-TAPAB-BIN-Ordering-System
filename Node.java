// Node class for storing one drink in the linked list
// It holds the drink data and a link to the next node.
public class Node {
    // Unique ID of the drink
    int id;
    // Name of the drink
    String name;
    // Category of the drink
    String category;
    // Price of the drink
    double price;
    // Reference to the next node
    Node next;

    // Constructor to create a drink node
    // It assigns all drink values and sets next to null.
    public Node(int id, String name, String category, double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.next = null;
    }
}
