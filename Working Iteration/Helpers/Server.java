package Helpers;

import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;


public class Server implements Runnable{
    private final Socket socket;
    private Cafe cafe;

    public Server(Socket socket, Cafe cafe){
        this.socket = socket;
        this.cafe = cafe;
    }

    public static void toJson(String customerName, String cafeLog) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        JSONObject dateAndTime = new JSONObject();
        JSONObject name = new JSONObject();
        dateAndTime.put("Current Date and Time ", dtf.format(now));
        name.put("Customer Name", customerName);
        try(FileWriter file = new FileWriter("myJSON.json", true)){
            dateAndTime.put("Current Date and Time ", dtf.format(now));
            name.put("Customer Name", customerName);
            file.append(String.valueOf(dateAndTime));
            file.append(System.getProperty("line.separator"));
            file.append(String.valueOf(name));
            file.append(System.getProperty("line.separator"));
            for (String s : cafeLog.split("\\n")){
                JSONObject currentCafe = new JSONObject();
                currentCafe.put(s.split(" ")[0], s);
                file.append(String.valueOf(currentCafe));
                file.append(System.getProperty("line.separator"));
            }
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String customerName = "";
        int customerNumber = cafe.createCustomerNumber();
        try (Scanner scanner = new Scanner(socket.getInputStream());
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
            try {
                Timer timer = new Timer();
                String finalCustomerName = customerName;
                TimerTask tt = new TimerTask() {
                    @Override
                    public void run() {
                        toJson(finalCustomerName, cafe.log());
//                        writer.println("server is saying hello");
//                        if(cafe.customers.get(customerNumber).equalsIgnoreCase("idle")) {
//                            writer.println("server Orders for " + finalCustomerName + " are Complete ");
//                        }
                    }
                };
                timer.schedule(tt, 30000, 30000);
                customerName = scanner.nextLine();
                System.out.println("New connection; customer  " + customerName);
                writer.println("SUCCESS");
                while (true) {
                    String line = scanner.nextLine();
                    String[] substrings = line.split(" ");
                    switch (substrings[0].toLowerCase()) {
                        case "order" -> {
                            StringBuilder string = new StringBuilder();
                            for (int i = 1; i < substrings.length; i++) {
                                string.append(" ").append(substrings[i]);
                            }
                            new Thread(new OrderThread(customerNumber, customerName, cafe, string.toString())).start();
                            writer.println("Order received for " + customerName + "(" + string + " )");
                        }
                        case "status" -> {
                            List<String> customerOrders;
                            customerOrders = cafe.getListOfOrders(customerName);
                            writer.println(customerOrders.size());
                            for (String orders : customerOrders) {
                                writer.println(orders);
                            }
                        }
                        case "exit" -> cafe.removeOrders(customerNumber);
                        default -> throw new Exception("Command not recognised: " + line);
                    }
                }
            } catch (Exception e) {
                writer.println("ERROR " + e.getMessage());
                socket.close();
            }
        } catch (Exception e) {
        } finally {
            System.out.println("Customer " + customerName + " has left the cafe.");
        }
    }
    
}
