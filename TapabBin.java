import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Queue;
import java.util.Scanner;

// Main class of the Tapab Bin vending machine system
// It controls menus, ordering flow, and admin functions.
public class TapabBin {
    // ANSI color code for red text
    private static final String RED = "\u001B[31m";
    // ANSI color code to reset text color
    private static final String RESET = "\u001B[0m";

    // Menu object that stores all drinks
    private final Menu menu;
    // Queue that stores customer orders waiting to be processed
    private final Queue<Order> orderQueue;
    // Stack that stores completed orders
    private final Deque<Order> orderHistoryStack;
    // Scanner used for reading user input
    private final Scanner scanner;
    // Counter used to create the next order ID
    private int nextOrderId;
    // Total sales amount of completed orders
    private double salesTotal;

    // Constructor that initializes the vending machine system
    // It creates the needed objects and loads default drinks.
    public TapabBin() {
        this.menu = new Menu();
        this.orderQueue = new LinkedList<>();
        this.orderHistoryStack = new ArrayDeque<>();
        this.scanner = new Scanner(System.in);
        this.nextOrderId = 1;
        this.salesTotal = 0.0;
        seedMenu();
    }

    // Main method that starts the program
    // It creates the main object and runs the application.
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        TapabBin machine = new TapabBin();
        machine.run();
    }

    // Add default drinks to the menu when the program starts
    // It inserts predefined drinks into the linked list menu.
    private void seedMenu() {
        menu.addDrink("Thai Milk Tea", "Tea", 45);
        menu.addDrink("Matcha Latte", "Milk", 55);
        menu.addDrink("Americano", "Coffee", 50);
        menu.addDrink("Cocoa", "Milk", 40);
        menu.addDrink("Lemon Tea", "Tea", 35);
    }
    // Display the main menu
    // It loops until the user chooses to exit the program.
    private void run() {
        while (true) {
            clearScreen();
            System.out.println("\n=== TAPAB BIN ===");
            System.out.println("1. Customer Menu");
            System.out.println("2. Admin Menu");
            System.out.println("3. Exit");

            int choice = readMenuChoice(1, 3);
            switch (choice) {
                case 1:
                    clearScreen();
                    customerMenu();
                    break;
                case 2:
                    clearScreen();
                    adminMenu();
                    break;
                case 3:
                    System.out.println("Thank you for using the system.");
                    return;
                default:
                    break;
            }
        }
    }

    // Display the customer menu and handle customer options
    // It keeps showing customer choices until the user goes back.
    private void customerMenu() {
        while (true) {
            clearScreen();
            System.out.println("\n--- CUSTOMER MENU ---");
            System.out.println("1. View Category");
            System.out.println("2. Show Waiting Queue");
            System.out.println("3. Back");

            int choice = readMenuChoice(1, 3);
            switch (choice) {
                case 1:
                    viewMenu();
                    break;
                case 2:
                    showQueue();
                    pauseAndContinue();
                    break;
                case 3:
                    clearScreen();
                    return;
                default:
                    break;
            }
        }
    }

    // Display the admin menu and handle admin options
    // It keeps showing admin choices until the user goes back.
    private void adminMenu() {
        while (true) {
            clearScreen();
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. Add Drink");
            System.out.println("2. Remove Drink");
            System.out.println("3. Process Next Order");
            System.out.println("4. Show Last Completed Order");
            System.out.println("5. Show Sales Summary");
            System.out.println("6. Back");

            int choice = readMenuChoice(1, 6);
            switch (choice) {
                case 1:
                    addDrink();
                    pauseAndContinue();
                    break;
                case 2:
                    removeDrink();
                    pauseAndContinue();
                    break;
                case 3:
                    processNextOrder();
                    pauseAndContinue();
                    break;
                case 4:
                    showLastCompletedOrder();
                    pauseAndContinue();
                    break;
                case 5:
                    showSalesSummary();
                    pauseAndContinue();
                    break;
                case 6:
                    clearScreen();
                    return;
                default:
                    break;
            }
        }
    }

    // Show all drinks in the menu
    // It prints every drink node by calling the menu display method.
    private void showMenu() {
        System.out.println("\n--- DRINK MENU ---");
        menu.displayRecursive();
    }

    // Show drink categories for customers to browse
    // It lets the user choose one category or all drinks.
    private void viewMenu() {
        while (true) {
            clearScreen();
            System.out.println("\n--- VIEW CATEGORY ---");
            System.out.println("1. Milk");
            System.out.println("2. Coffee");
            System.out.println("3. Tea");
            System.out.println("4. All Drinks");
            System.out.println("5. Back");

            int choice = readMenuChoice(1, 5);
            switch (choice) {
                case 1:
                    showCategoryMenu("Milk");
                    break;
                case 2:
                    showCategoryMenu("Coffee");
                    break;
                case 3:
                    showCategoryMenu("Tea");
                    break;
                case 4:
                    showAllDrinksAndOrder();
                    break;
                case 5:
                    return;
                default:
                    break;
            }
        }
    }

    // Show drinks in one selected category
    // It filters drinks by category and lets the user order from that list.
    private void showCategoryMenu(String category) {
        clearScreen();
        System.out.println("\n--- " + category.toUpperCase() + " MENU ---");
        menu.displayByCategory(category);
        System.out.println("\n1. Order from this category");
        System.out.println("2. Back");

        int choice = readMenuChoice(1, 2);
        if (choice == 1) {
            placeOrder(category);
        }
    }

    // Show all drinks and allow ordering without category filtering
    // It displays the full menu and opens the ordering step.
    private void showAllDrinksAndOrder() {
        clearScreen();
        showMenu();
        System.out.println("\n1. Order from all drinks");
        System.out.println("2. Back");

        int choice = readMenuChoice(1, 2);
        if (choice == 1) {
            placeOrder(null);
        }
    }

    // Create a new order and add it to the waiting queue
    // It collects order details, validates payment, and enqueues the order.
    private void placeOrder(String categoryFilter) {
        clearScreen();
        if (categoryFilter == null) {
            showMenu();
        } else {
            System.out.println("\n--- " + categoryFilter.toUpperCase() + " MENU ---");
            menu.displayByCategory(categoryFilter);
        }

        System.out.println("0. Back");
        int drinkId = readInt("Please enter drink ID: ", 0);
        if (drinkId == 0) {
            return;
        }
        Node drink = menu.findByIdIterative(drinkId);

        if (drink == null) {
            showError("Invalid drink ID.");
            return;
        }

        if (categoryFilter != null && !drink.category.equalsIgnoreCase(categoryFilter)) {
            showError("Please choose a drink from the selected category only.");
            return;
        }

        String sweetness = readSweetnessLevel();
        String iceLevel = readIceLevel();

        int quantity = 1;
        double totalPrice = drink.price;
        System.out.printf("Total price: %.2f THB%n", totalPrice);
        double payment = readPaymentAmount(totalPrice);
        double change = payment - totalPrice;

        Order order = new Order(nextOrderId, drink, sweetness, iceLevel, quantity, totalPrice);
        nextOrderId++;
        orderQueue.offer(order);

        System.out.println("\nOrder placed successfully.");
        System.out.println(order.summary());
        System.out.printf("Payment: %.2f THB%n", payment);
        System.out.printf("Change: %.2f THB%n", change);
        System.out.println("Thank you for your order.");
        System.out.print("\nPress Enter to view the waiting queue...");
        scanner.nextLine();
        clearScreen();
        showQueue();
        pauseAndContinue();
    }

    // Display all orders currently waiting in the queue
    // It traverses the queue and prints each waiting order in order.
    private void showQueue() {
        System.out.println("\n--- WAITING QUEUE ---");
        if (orderQueue.isEmpty()) {
            showError("No orders in queue.");
            return;
        }

        int position = 1;
        for (Order order : orderQueue) {
            System.out.println(position + ". " + order.summary());
            position++;
        }
    }

    // Add a new drink to the menu
    // It reads the new drink data and appends it to the linked list.
    private void addDrink() {
        System.out.println("Enter 0 to cancel.");
        System.out.print("Please enter new drink name: ");
        String name = scanner.nextLine().trim();
        if (name.equals("0")) {
            return;
        }
        if (name.isEmpty()) {
            showError("Drink name cannot be empty.");
            return;
        }

        String category = chooseCategory();
        if (category == null) {
            return;
        }
        Double price = readPriceForAddDrink();
        if (price == null) {
            return;
        }
        Node node = menu.addDrink(name, category, price);
        System.out.printf("Added drink [%d] %s successfully.%n", node.id, node.name);
    }

    // Remove a drink from the menu by its ID
    // It finds the matching node and deletes it from the linked list.
    private void removeDrink() {
        showMenu();
        System.out.println("0. Back");
        int drinkId = readInt("Please enter drink ID to remove: ", 0);
        if (drinkId == 0) {
            return;
        }
        Node removed = menu.removeDrink(drinkId);
        if (removed != null) {
            System.out.println("Removed " + removed.name + " successfully.");
        } else {
            showError("Drink ID not found.");
        }
    }

    // Process the next order from the queue and move it to the completed stack
    // It dequeues the oldest order, then pushes it into the history stack.
    private void processNextOrder() {
        if (orderQueue.isEmpty()) {
            showError("No orders to process.");
            return;
        }

        Order order = orderQueue.poll();
        orderHistoryStack.push(order);
        salesTotal += order.totalPrice;

        System.out.println("\nPreparing drink...");
        System.out.println("Completed " + order.summary());
    }

    // Show the latest completed order from the stack
    // It reads the top item of the stack without removing it.
    private void showLastCompletedOrder() {
        System.out.println("\n--- LAST COMPLETED ORDER ---");
        if (orderHistoryStack.isEmpty()) {
            showError("No completed orders yet.");
            return;
        }

        System.out.println(orderHistoryStack.peek().summary());
    }

    // Show a summary of queue size, completed orders, and total sales
    // It prints current queue count, completed count, and revenue.
    private void showSalesSummary() {
        System.out.println("\n--- SALES SUMMARY ---");
        System.out.println("Orders waiting in queue: " + orderQueue.size());
        System.out.println("Completed orders: " + orderHistoryStack.size());
        System.out.printf("Total sales: %.2f THB%n", salesTotal);
    }

    // Read and validate integer input from the user
    // It keeps asking until the user enters a valid integer in range.
    private int readInt(String prompt, int minimum) {
        while (true) {
            System.out.print(prompt);
            String raw = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(raw);
                if (value < minimum) {
                    showError("Please enter a number greater than or equal to " + minimum + ".");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                showError("Please enter a valid integer.");
            }
        }
    }

    // Read and validate decimal input from the user
    // It keeps asking until the user enters a valid decimal in range.
    private double readDouble(String prompt, double minimum) {
        while (true) {
            System.out.print(prompt);
            String raw = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(raw);
                if (value < minimum) {
                    showError(String.format("Please enter a number greater than or equal to %.2f.", minimum));
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                showError("Please enter a valid number.");
            }
        }
    }

    // Read drink price for add drink and allow cancel with C
    // It accepts a valid price or returns null when the user cancels.
    private Double readPriceForAddDrink() {
        while (true) {
            System.out.print("Please enter price (or C to cancel): ");
            String raw = scanner.nextLine().trim();

            if (raw.equalsIgnoreCase("C")) {
                return null;
            }

            try {
                double value = Double.parseDouble(raw);
                if (value < 1) {
                    showError("Please enter a number greater than or equal to 1.00.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                showError("Please enter a valid number or C to cancel.");
            }
        }
    }

    // Read and validate menu choices within a specific range
    // It repeats until the user selects one allowed menu number.
    private int readMenuChoice(int min, int max) {
        while (true) {
            System.out.print("Please enter your choice: ");
            String raw = scanner.nextLine().trim();
            try {
                int choice = Integer.parseInt(raw);
                if (choice < min || choice > max) {
                    showError("Please enter a number from " + min + " to " + max + " only.");
                    continue;
                }
                clearScreen();
                return choice;
            } catch (NumberFormatException e) {
                showError("Please enter a number from " + min + " to " + max + " only.");
            }
        }
    }

    // Let the admin choose a category for a new drink
    // It returns the selected category name or null when canceled.
    private String chooseCategory() {
        System.out.println("Select category:");
        System.out.println("0. Cancel");
        System.out.println("1. Milk");
        System.out.println("2. Coffee");
        System.out.println("3. Tea");

        int choice = readMenuChoice(0, 3);
        switch (choice) {
            case 0:
                return null;
            case 1:
                return "Milk";
            case 2:
                return "Coffee";
            default:
                return "Tea";
        }
    }

    // Read and validate the sweetness level for an order
    // It only accepts one of the predefined sweetness values.
    private String readSweetnessLevel() {
        while (true) {
            System.out.print("Please enter sweetness level (0/25/50/75/100): ");
            String sweetness = scanner.nextLine().trim();

            if (
                sweetness.equals("0") ||
                sweetness.equals("25") ||
                sweetness.equals("50") ||
                sweetness.equals("75") ||
                sweetness.equals("100")
            ) {
                return sweetness;
            }

            showError("Please enter 0, 25, 50, 75, or 100 only.");
        }
    }

    // Read and validate the ice level for an order
    // It only accepts one of the predefined ice level options.
    private String readIceLevel() {
        while (true) {
            System.out.print("Please enter ice level (No ice/Less/Normal): ");
            String iceLevel = scanner.nextLine().trim();

            if (iceLevel.equalsIgnoreCase("No ice")) {
                return "No ice";
            }
            if (iceLevel.equalsIgnoreCase("Less")) {
                return "Less";
            }
            if (iceLevel.equalsIgnoreCase("Normal")) {
                return "Normal";
            }

            showError("Please enter No ice, Less, or Normal only.");
        }
    }

    // Read payment amount and prevent insufficient payment
    // It keeps asking until the payment is enough for the total price.
    private double readPaymentAmount(double totalPrice) {
        while (true) {
            double payment = readDouble("Please enter payment amount: ", 0);
            if (payment < totalPrice) {
                showError(String.format("Insufficient payment. Please pay at least %.2f THB.", totalPrice));
                continue;
            }
            return payment;
        }
    }

    // Show an error message in red text
    // It wraps the message with color codes before printing.
    private void showError(String message) {
        System.out.println(RED + message + RESET);
    }

    // Pause the screen so the user can read the current result
    // It waits for Enter before continuing to the next screen.
    private void pauseAndContinue() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    // Print blank lines to make the console look cleaner
    // It pushes old text upward so the next screen looks clearer.
    private void clearScreen() {
        System.out.print("\033[H\033[2J"); // Clear the console screen
        System.out.flush(); // Flush the output buffer
    }
}
