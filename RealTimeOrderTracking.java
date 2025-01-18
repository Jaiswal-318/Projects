package projects;

import java.util.*;

class Order {
    int orderId;
    String customerName;
    String address;
    String status; // Pending, In-Transit, Delivered

    public Order(int orderId, String customerName, String address) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.address = address;
        this.status = "Pending";
    }

    @Override
    public String toString() {
        return "Order ID: " + orderId + ", Customer: " + customerName + ", Address: " + address + ", Status: " + status;
    }
}

class DeliveryRoute {
    private final Map<String, List<String>> graph = new HashMap<>();

    public void addRoute(String from, String to) {
        graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
        graph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
    }

    public List<String> findShortestRoute(String start, String end) {
        Queue<List<String>> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        queue.add(Collections.singletonList(start));

        while (!queue.isEmpty()) {
            List<String> path = queue.poll();
            String lastNode = path.get(path.size() - 1);

            if (lastNode.equals(end)) {
                return path;
            }

            if (!visited.contains(lastNode)) {
                visited.add(lastNode);
                for (String neighbor : graph.getOrDefault(lastNode, new ArrayList<>())) {
                    List<String> newPath = new ArrayList<>(path);
                    newPath.add(neighbor);
                    queue.add(newPath);
                }
            }
        }
        return new ArrayList<>(); // No route found
    }
}

public class RealTimeOrderTracking {
    private final Queue<Order> orderQueue = new LinkedList<>();
    private final DeliveryRoute deliveryRoute = new DeliveryRoute();

    public RealTimeOrderTracking() {
        // Initialize sample delivery routes
        deliveryRoute.addRoute("Warehouse", "Area1");
        deliveryRoute.addRoute("Area1", "Area2");
        deliveryRoute.addRoute("Area2", "Area3");
        deliveryRoute.addRoute("Area3", "CustomerLocation");
    }

    public void placeOrder(Order order) {
        orderQueue.add(order);
        System.out.println("Order placed: " + order);
    }

    public void processOrder() {
        if (orderQueue.isEmpty()) {
            System.out.println("No orders to process.");
            return;
        }

        Order order = orderQueue.poll();
        System.out.println("Processing order: " + order);

        // Simulate finding the delivery route
        List<String> route = deliveryRoute.findShortestRoute("Warehouse", "CustomerLocation");
        if (!route.isEmpty()) {
            System.out.println("Route for delivery: " + route);
            order.status = "In-Transit";
        } else {
            System.out.println("No delivery route found for: " + order);
        }
    }

    public void updateOrderStatus(int orderId, String status) {
        for (Order order : orderQueue) {
            if (order.orderId == orderId) {
                order.status = status;
                System.out.println("Updated order: " + order);
                return;
            }
        }
        System.out.println("Order not found.");
    }

    public void displayOrders() {
        if (orderQueue.isEmpty()) {
            System.out.println("No active orders.");
            return;
        }

        System.out.println("Active Orders:");
        for (Order order : orderQueue) {
            System.out.println(order);
        }
    }

    public static void main(String[] args) {
        RealTimeOrderTracking trackingSystem = new RealTimeOrderTracking();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Real-Time Order Tracking System ---");
            System.out.println("1. Place Order");
            System.out.println("2. Process Order");
            System.out.println("3. Update Order Status");
            System.out.println("4. Display Orders");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter customer name: ");
                    String customerName = scanner.nextLine();
                    System.out.print("Enter address: ");
                    String address = scanner.nextLine();
                    Order order = new Order(new Random().nextInt(1000), customerName, address);
                    trackingSystem.placeOrder(order);
                }
                case 2 -> trackingSystem.processOrder();
                case 3 -> {
                    System.out.print("Enter order ID to update: ");
                    int orderId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter new status (Pending/In-Transit/Delivered): ");
                    String status = scanner.nextLine();
                    trackingSystem.updateOrderStatus(orderId, status);
                }
                case 4 -> trackingSystem.displayOrders();
                case 5 -> {
                    System.out.println("Exiting system...");
                    return;
                }
                default -> System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}

