import Helpers.CustomerProcessor;
import sun.misc.Signal;

import java.util.Scanner;

public class Customer {

    public static void main(String[] args) {
        System.out.println("Please enter your Name:");
        String customerName = "";
        try {
            try (Scanner in = new Scanner(System.in)) {
                customerName = in.nextLine();
                try (CustomerProcessor customer = new CustomerProcessor(customerName)) {
                    System.out.println("Hello " + customerName + "! Welcome to the Virtual Cafe!");
                    System.out.println("You may order teas (30s) or coffees (45s) by entering 'order' followed by your order");
                    System.out.println("You can check the status of your orders by entering 'order status' ");
                    System.out.println("You can leave by entering 'exit'");
                    System.out.println("Please Enter how we can help?");
                    Signal.handle(new Signal("INT"),  // SIGINT
                            signal -> customer.close());
                    while (true) {
                        String input = in.nextLine();
                        String[] substrings = input.split(" ");
                        switch (substrings[0].toLowerCase()){
                            case "exit":
                                customer.close();
                                break;
                            case "order":
                                if (substrings[1].equalsIgnoreCase("status")) {
                                    String[] orderList = customer.getOrders();
                                    if (orderList.length == 0) {
                                        System.out.println("No orders found for " + customerName);
                                    } else {
                                        for (String orders : orderList) {
                                            System.out.println(orders);
                                        }
                                    }
                                } else {
                                    customer.makeOrder(input);
                                }
                                break;
                            default:
                                System.out.println("Sorry but we are not sure how we can help; Please try again.");
                                break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

    
