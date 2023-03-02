import java.util.HashMap;
import java.util.Map;

public class TrayArea {
    private Map<String, Order> orders;

    public TrayArea() {
        this.orders = new HashMap<>();
    }

    public void addOrder(Order order) {
        this.orders.put(order.getName(), order);
    }

    public Order getOrder(String name) {
        return this.orders.get(name);
    }

    public Order removeOrder(String name) {
        return this.orders.remove(name);
    }

    public boolean containsOrder(String name) {
        return this.orders.containsKey(name);
    }

    public int getNumOrders() {
        return this.orders.size();
    }
}
