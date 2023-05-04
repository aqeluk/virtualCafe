package Helpers;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class CustomerProcessor implements AutoCloseable {
    final int port = 8888;
    private final Scanner reader;
    private final PrintWriter writer;

    public CustomerProcessor(String customerName) throws Exception {
        Socket socket = new Socket("localhost", port);
        reader = new Scanner(socket.getInputStream());
        writer = new PrintWriter(socket.getOutputStream(), true);
        writer.println(customerName);
        String line = reader.nextLine();
        if (line.trim().compareToIgnoreCase("success") != 0) throw new Exception(line);
    }

    public String[] getOrders() {
        writer.println("STATUS");
        String line = reader.nextLine();
        int numberOfOrders = Integer.parseInt(line);
        String[] orders = new String[numberOfOrders];
        for (int i = 0; i < numberOfOrders; i++) {
            line = reader.nextLine();
            orders[i] = line;
        }
        return orders;
    }

    public void makeOrder(String order) {
        writer.println("ORDER " + order);
        System.out.println("Order Created");
        System.out.println(reader.nextLine());
    }

    public String listen() {
        String nextLine = "";
        try {
            if (reader.hasNextLine()) {
                nextLine = reader.nextLine();
            }
        } catch (IndexOutOfBoundsException be) {
        }
        return nextLine;
    }


    @Override
    public void close() {
        writer.println("exit");
        reader.close();
        writer.close();
        System.out.println("Goodbye, we hope you enjoyed!");
        System.exit(0);
    }
}
