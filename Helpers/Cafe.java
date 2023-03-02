import Helpers.WaitingArea;
import Helpers.BrewingArea;
import Helpers.TrayArea;
import Helpers.Order;

public class Cafe {
    private WaitingArea waitingArea;
    private BrewingArea brewingArea;
    private TrayArea trayArea;

    public Cafe() {
        this.waitingArea = new WaitingArea();
        this.brewingArea = new BrewingArea();
        this.trayArea = new TrayArea();
    }

    public void receiveOrder(Order order) {
        this.waitingArea.addOrder(order);
        System.out.println("Order received for " + order.getName() + ": " + order);
    }

    public void checkOrderStatus(String name) {
        Order order = this.trayArea.getOrder(name);
        if (order == null) {
            System.out.println("No order found for " + name);
        } else {
            System.out.println("Order status for " + name + ":");
            System.out.println("- " + order.getNumCoffees() + " coffee(s) and " + order.getNumTeas() + " tea(s) in waiting area");
            if (this.brewingArea.getNumCoffees() > 0 || this.brewingArea.getNumTeas() > 0) {
                System.out.println("- " + this.brewingArea.getNumCoffees() + " coffee(s) and " + this.brewingArea.getNumTeas() + " tea(s) currently being prepared");
            }
            if (this.trayArea.containsOrder(name)) {
                System.out.println("- " + this.trayArea.getOrder(name).getNumCoffees() + " coffee(s) and " + this.trayArea.getOrder(name).getNumTeas() + " tea(s) currently in the tray");
            }
        }
    }

    public void prepareOrders() {
        while (true) {
            if (this.brewingArea.canBrewTea() && this.waitingArea.getNumOrders() > 0) {
                Order order = this.waitingArea.getNextOrder();
                int numTeasToBrew = Math.min(2 - this.brewingArea.getNumTeas(), order.getNumTeas());
                for (int i = 0; i < numTeasToBrew; i++) {
                    this.brewingArea.brewTea();
                }
                order.setNumTeas(order.getNumTeas() - numTeasToBrew);
            }

            if (this.brewingArea.canBrewCoffee() && this.waitingArea.getNumOrders() > 0) {
                Order order = this.waitingArea.getNextOrder();
                int numCoffeesToBrew = Math.min(2 - this.brewingArea.getNumCoffees(), order.getNumCoffees());
                for (int i = 0; i < numCoffeesToBrew; i++) {
                    this.brewingArea.brewCoffee();
                }
                order.setNumCoffees(order.getNumCoffees() - numCoffeesToBrew);
            }

            for (Order order : this.trayArea.orders.values()) {
                if (order.getNumCoffees() == 0 && order.getNumTeas() == 0) {
                    System.out.println("Order delivered to " + order.getName() + ": " + order);
                    this.trayArea.removeOrder(order.getName());
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void cancelOrder(String name) {
        Order order = this.waitingArea.removeOrder(name);
        if (order != null) {
            System.out.println("Order canceled for " + name + ": " + order);
            reassignDrinks(name); // Move drinks from brewing area back to waiting area
        } else {
            System.out.println("No order found for " + name);
        }
    }
    
    public void modifyOrder(String name, int numCoffees, int numTeas) {
        Order order = this.waitingArea.getOrder(name);
        if (order == null) {
            System.out.println("No order found for " + name);
        } else {
            // Move original drinks back to waiting area
            reassignDrinks(name);
            // Update order with new drinks
            order.setNumCoffees(numCoffees);
            order.setNumTeas(numTeas);
            System.out.println("Order modified for " + name + ": " + order);
        }
    }
    
    
    public void generateReceipt(String name) {
        Order order = this.trayArea.getOrder(name);
        if (order == null) {
            System.out.println("No order found for " + name);
        } else {
            double totalCost = order.getNumCoffees() * 1.5 + order.getNumTeas() * 1.0;
            System.out.println("Receipt for " + name + ":");
            System.out.println("- " + order.getNumCoffees() + " coffee(s) at $1.50 each: $" + String.format("%.2f", order.getNumCoffees() * 1.5));
            System.out.println("- " + order.getNumTeas() + " tea(s) at $1.00 each: $" + String.format("%.2f", order.getNumTeas() * 1.0));
            System.out.println("- Total: $" + String.format("%.2f", totalCost));
        }
    }

    public void reassignDrinks(String name) {
        Order order = this.brewingArea.removeOrder(name);
        if (order != null) {
            this.waitingArea.addOrder(order);
            System.out.println("Drinks reassigned for " + name + ": " + order);
        } else {
            System.out.println("No order found for " + name);
        }
    }
}
