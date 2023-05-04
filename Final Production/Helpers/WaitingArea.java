import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WaitingArea {
    private Queue<Order> orders;

    public WaitingArea() {
        this.orders = new ConcurrentLinkedQueue<>();
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    public Order getNextOrder() {
        return this.orders.poll();
    }

    public int getNumOrders() {
        return this.orders.size();
    }
}
