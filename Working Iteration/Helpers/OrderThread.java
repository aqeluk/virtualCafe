package Helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderThread implements Runnable {
    private final int customerNumber;
    private final String order;
    private Cafe cafe;
    private final String customerName;

    
    public OrderThread(int customerNumber, String customerName, Cafe cafe, String order){
        this.customerNumber = customerNumber;
        this.customerName = customerName;
        this.cafe = cafe;
        this.order = order;
    }
    
    @Override
    public void run() {
        System.out.println("Order received from " + customerName + " " + order);
        int orderNumber = 1;
        if (!cafe.orders.isEmpty() && cafe.customers.get(customerNumber).equalsIgnoreCase("idle")){
            orderNumber = cafe.orders.size();
            orderNumber++;
        }
        String regex = "(\\d)\\s([TtCc]\\w+)";
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(order);
        while (matcher.find()) {
            int quantity = Integer.parseInt(matcher.group(1));
            for (int i = 1; i <= quantity; i++)
                switch (matcher.group(2).split("")[0].toLowerCase()) {
                    case "c" -> cafe.createOrder(customerNumber, orderNumber, customerName, "coffee");
                    case "t" -> cafe.createOrder(customerNumber, orderNumber, customerName, "tea");
                }
            System.out.println("Order  for " + customerName + " ( " + quantity + " " + matcher.group(2).toLowerCase() + " ) has been created");
        }
        cafe.processOrder();
        Thread teaThread = new Thread(new teaThread(cafe));
        teaThread.start();
        Thread coffeeThread = new Thread(new coffeeThread(cafe));
        coffeeThread.start();
        try {
            teaThread.join();
            coffeeThread.join();
        } catch (Exception e) {
            System.out.println("No drinks left to move to brewing");
        }
    }
}
