import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import helpers.Order;
import helpers.Request;
import helpers.Response;
import helpers.ResponseType;

public class Customer {
    public static void main(String[] args) {
        String hostName = "localhost";
        int portNumber = 8000;

        try (
            Socket socket = new Socket(hostName, portNumber);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Scanner scanner = new Scanner(System.in)
        ) {
            while (true) {
                System.out.println("Enter command (add, check, modify, cancel, receipt, quit):");
                String command = scanner.nextLine();

                switch (command) {
                    case "add":
                        System.out.println("Enter name:");
                        String name = scanner.nextLine();
                        System.out.println("Enter number of coffees:");
                        int numCoffees = scanner.nextInt();
                        scanner.nextLine(); // Consume newline left over from nextInt()
                        System.out.println("Enter number of teas:");
                        int numTeas = scanner.nextInt();
                        scanner.nextLine(); // Consume newline left over from nextInt()

                        Order order = new Order(name, numCoffees, numTeas);
                        Request addRequest = new Request(RequestType.ADD_ORDER, order);
                        out.writeObject(addRequest);
                        Response addResponse = (Response) in.readObject();
                        if (addResponse.getType() == ResponseType.SUCCESS) {
                            System.out.println("Order added: " + order);
                        } else {
                            System.out.println("Failed to add order: " + addResponse.getMessage());
                        }
                        break;

                    case "check":
                        System.out.println("Enter name:");
                        String checkName = scanner.nextLine();

                        Request checkRequest = new Request(RequestType.CHECK_STATUS, checkName);
                        out.writeObject(checkRequest);
                        Response checkResponse = (Response) in.readObject();
                        if (checkResponse.getType() == ResponseType.SUCCESS) {
                            System.out.println("Order status: \n" + checkResponse.getMessage());
                        } else {
                            System.out.println("Failed to check order status: " + checkResponse.getMessage());
                        }
                        break;

                    case "modify":
                        System.out.println("Enter name:");
                        String modifyName = scanner.nextLine();
                        System.out.println("Enter number of coffees:");
                        int modifyNumCoffees = scanner.nextInt();
                        scanner.nextLine(); // Consume newline left over from nextInt()
                        System.out.println("Enter number of teas:");
                        int modifyNumTeas = scanner.nextInt();
                        scanner.nextLine(); // Consume newline left over from nextInt()

                        Request modifyRequest = new Request(RequestType.MODIFY_ORDER, modifyName, modifyNumCoffees, modifyNumTeas);
                        out.writeObject(modifyRequest);
                        Response modifyResponse = (Response) in.readObject();
                        if (modifyResponse.getType() == ResponseType.SUCCESS) {
                            System.out.println("Order modified: " + modifyResponse.getMessage());
                        } else {
                            System.out.println("Failed to modify order: " + modifyResponse.getMessage());
                        }
                        break;

                    case "cancel":
                        System.out.println("Enter name:");
                        String cancelName = scanner.nextLine();

                        Request cancelRequest = new Request(RequestType.CANCEL_ORDER, cancelName);
                        out.writeObject(cancelRequest);
                        Response cancelResponse = (Response) in.readObject();
                        if (cancelResponse.getType() == ResponseType.SUCCESS) {
                            System.out.println("Order canceled: " + cancelResponse.getMessage());
                        } else {
                            System.out.println("Failed to cancel order: " +
