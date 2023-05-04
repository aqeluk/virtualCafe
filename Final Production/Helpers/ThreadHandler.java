import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ThreadHandler extends Thread {
    private Socket clientSocket;
    private Cafe cafe;

    public ThreadHandler(Socket socket, Cafe cafe) {
        this.clientSocket = socket;
        this.cafe = cafe;
    }

    @Override
    public void run() {
        try (
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())
        ) {
            while (true) {
                // Receive request from client
                Request request = (Request) in.readObject();

                // Handle request and send response to client
                switch (request.getType()) {
                    case ADD_ORDER:
                        cafe.receiveOrder(request.getOrder());
                        out.writeObject(new Response(ResponseType.SUCCESS, null));
                        break;
                    case CHECK_STATUS:
                        cafe.checkOrderStatus(request.getName());
                        out.writeObject(new Response(ResponseType.SUCCESS, null));
                        break;
                    case CANCEL_ORDER:
                        cafe.cancelOrder(request.getName());
                        out.writeObject(new Response(ResponseType.SUCCESS, null));
                        break;
                    case MODIFY_ORDER:
                        cafe.modifyOrder(request.getName(), request.getNumCoffees(), request.getNumTeas());
                        out.writeObject(new Response(ResponseType.SUCCESS, null));
                        break;
                    case GENERATE_RECEIPT:
                        cafe.generateReceipt(request.getName());
                        out.writeObject(new Response(ResponseType.SUCCESS, null));
                        break;
                    case QUIT:
                        clientSocket.close();
                        return;
                }
            }
        } catch (IOException e) {
            System.err.println("Error in ThreadHandler: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Error in ThreadHandler: " + e.getMessage());
        }
    }
}
