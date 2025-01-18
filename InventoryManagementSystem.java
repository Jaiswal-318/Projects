package projects;
import java.util.*;
import java.io.*;


class Product {
    String name;
    int quantity;

    public Product(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product{name='" + name + '\'' + ", quantity=" + quantity + '}';
    }
}


public class InventoryManagementSystem {

    private final HashMap<String, Product> inventory = new HashMap<>();
    private final PriorityQueue<Product> orderQueue = new PriorityQueue<>((a, b) -> b.quantity - a.quantity);
    private final String filePath = "inventory_data.txt";

    public InventoryManagementSystem() {
        loadInventoryFromFile();
    }

    private void loadInventoryFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String name = data[0].trim();
                int quantity = Integer.parseInt(data[1].trim());
                inventory.put(name, new Product(name, quantity));
            }
        } catch (IOException e) {
            System.out.println("Error loading inventory: " + e.getMessage());
        }
    }

    private void saveInventoryToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Product product : inventory.values()) {
                bw.write(product.name + "," + product.quantity);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }

    public void addProduct(String name, int quantity) {
        inventory.put(name, inventory.getOrDefault(name, new Product(name, 0)));
        inventory.get(name).quantity += quantity;
        saveInventoryToFile();
    }

    public void removeProduct(String name, int quantity) {
        if (inventory.containsKey(name)) {
            Product product = inventory.get(name);
            if (product.quantity >= quantity) {
                product.quantity -= quantity;
                if (product.quantity == 0) {
                    inventory.remove(name);
                }
                saveInventoryToFile();
            } else {
                System.out.println("Not enough stock to remove.");
            }
        } else {
            System.out.println("Product not found in inventory.");
        }
    }

    public void searchProduct(String name) {
        if (inventory.containsKey(name)) {
            System.out.println(inventory.get(name));
        } else {
            System.out.println("Product not found.");
        }
    }

    public void addToOrderQueue(String name, int quantity) {
        if (inventory.containsKey(name)) {
            Product product = inventory.get(name);
            orderQueue.add(new Product(name, quantity));
            System.out.println("Added to order queue: " + name);
        } else {
            System.out.println("Product not found.");
        }
    }

    public void processOrderQueue() {
        while (!orderQueue.isEmpty()) {
            Product order = orderQueue.poll();
            System.out.println("Processing order: " + order);
        }
    }

    public void displayInventory() {
        System.out.println("Current Inventory:");
        for (Product product : inventory.values()) {
            System.out.println(product);
        }
    }

    public static void main(String[] args) {
        InventoryManagementSystem ims = new InventoryManagementSystem();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Inventory Management System ---");
            System.out.println("1. Add Product");
            System.out.println("2. Remove Product");
            System.out.println("3. Search Product");
            System.out.println("4. Add to Order Queue");
            System.out.println("5. Process Order Queue");
            System.out.println("6. Display Inventory");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter product name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter quantity: ");
                    int quantity = scanner.nextInt();
                    ims.addProduct(name, quantity);
                }
                case 2 -> {
                    System.out.print("Enter product name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter quantity to remove: ");
                    int quantity = scanner.nextInt();
                    ims.removeProduct(name, quantity);
                }
                case 3 -> {
                    System.out.print("Enter product name to search: ");
                    String name = scanner.nextLine();
                    ims.searchProduct(name);
                }
                case 4 -> {
                    System.out.print("Enter product name to order: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter quantity to order: ");
                    int quantity = scanner.nextInt();
                    ims.addToOrderQueue(name, quantity);
                }
                case 5 -> ims.processOrderQueue();
                case 6 -> ims.displayInventory();
                case 7 -> {
                    System.out.println("Exiting system...");
                    return;
                }
                default -> System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}


