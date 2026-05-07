// Linked list class used to manage the drink menu
// It stores, searches, displays, and removes drink nodes.
public class Menu {
    // First node in the linked list
    private Node head;
    // Counter for assigning new drink IDs
    private int nextId;

    // Constructor to initialize an empty menu
    // It starts with no head node and the first available ID.
    public Menu() {
        this.head = null;
        this.nextId = 1;
    }

    // Add a new drink to the end of the linked list
    // It creates a node and links it after the current last node.
    public Node addDrink(String name, String category, double price) {
        Node node = new Node(nextId, name, category, price);
        nextId++;

        if (head == null) {
            head = node;
            return node;
        }

        Node current = head;
        while (current.next != null) {
            current = current.next;
        }
        current.next = node;
        return node;
    }

    // Remove a drink node from the linked list by ID
    // It relinks the previous node to skip the removed node.
    public Node removeDrink(int drinkId) {
        Node current = head;
        Node previous = null;

        while (current != null) {
            if (current.id == drinkId) {
                if (previous == null) {
                    head = current.next;
                } else {
                    previous.next = current.next;
                }
                return current;
            }
            previous = current;
            current = current.next;
        }
        return null;
    }

    // Find a drink by ID using iterative traversal
    // It moves node by node until the matching ID is found.
    public Node findByIdIterative(int drinkId) {
        Node current = head;
        while (current != null) {
            if (current.id == drinkId) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    // Find a drink by name using recursion
    // It starts the recursive search from the head node.
    public Node findByNameRecursive(String keyword) {
        return findByNameRecursive(keyword.toLowerCase(), head);
    }

    // Recursive helper method for searching drink names
    // It checks one node at a time and calls itself on the next node.
    private Node findByNameRecursive(String keyword, Node node) {
        if (node == null) {
            return null;
        }

        if (node.name.toLowerCase().contains(keyword)) {
            return node;
        }

        return findByNameRecursive(keyword, node.next);
    }

    // Display all drinks in the linked list using recursion
    // It prints from the head node until the end of the list.
    public void displayRecursive() {
        if (head == null) {
            System.out.println("No drinks available.");
            return;
        }
        displayRecursive(head);
    }

    // Display drinks that match a selected category
    // It prints only nodes whose category matches the requested value.
    public void displayByCategory(String category) {
        if (head == null) {
            System.out.println("No drinks available.");
            return;
        }

        if (!displayByCategoryRecursive(head, category.toLowerCase())) {
            System.out.println("No drinks found in this category.");
        }
    }

    // Recursive helper method for displaying all drinks
    // It prints the current node and then continues with the next node.
    private void displayRecursive(Node node) {
        System.out.printf("[%d] %-18s %-8s %.2f THB%n", node.id, node.name, node.category, node.price);
        if (node.next != null) {
            displayRecursive(node.next);
        }
    }

    // Recursive helper method for displaying drinks by category
    // It checks the category at each node and keeps scanning the list.
    private boolean displayByCategoryRecursive(Node node, String category) {
        if (node == null) {
            return false;
        }

        boolean foundCurrent = node.category.toLowerCase().equals(category);
        if (foundCurrent) {
            System.out.printf("[%d] %-18s %-8s %.2f THB%n", node.id, node.name, node.category, node.price);
        }

        boolean foundNext = displayByCategoryRecursive(node.next, category);
        return foundCurrent || foundNext;
    }
}
