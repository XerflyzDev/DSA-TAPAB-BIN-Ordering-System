// Order class for storing customer order details
// It keeps all information needed for one completed or waiting order.
public class Order {
    // Unique ID of the order
    int orderId;
    // Drink selected in this order
    Node drink;
    // Sweetness level selected by the customer
    String sweetness;
    // Ice level selected by the customer
    String iceLevel;
    // Number of drinks ordered
    int quantity;
    // Total price of the order
    double totalPrice;

    // Constructor to create a new order object
    // It saves the selected drink and customer order settings.
    public Order(int orderId, Node drink, String sweetness, String iceLevel, int quantity, double totalPrice) {
        this.orderId = orderId;
        this.drink = drink;
        this.sweetness = sweetness;
        this.iceLevel = iceLevel;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    // Return a formatted text summary of the order
    // It combines the stored order data into one readable line.
    public String summary() {
        return String.format(
            "Order #%d | %s x%d | Sweetness: %s | Ice: %s | Total: %.2f THB",
            orderId,
            drink.name,
            quantity,
            sweetness,
            iceLevel,
            totalPrice
        );
    }
}
