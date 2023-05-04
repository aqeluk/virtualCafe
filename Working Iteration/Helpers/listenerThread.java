package Helpers;

public class listenerThread implements Runnable {
    public CustomerProcessor customer;
    private boolean exit;

    public listenerThread(CustomerProcessor customer) {
        this.customer = customer;
        exit = false;
    }

    public void setStatus(String status) {
        switch (status.toLowerCase()) {
            case "pause" -> {
                exit = true;
                System.out.println("listener paused");
            }
            case "resume" -> {
                exit = false;
                System.out.println("listener resumed");
            }
        }
    }

    @Override
    public void run() {
        while (!exit){
            System.out.println("checking");
            String server = customer.listen();
            if (server.split("")[0].equalsIgnoreCase("server")) {
                System.out.println(server);
            }
        }
    }
}

