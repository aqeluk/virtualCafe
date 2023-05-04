package Helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Cafe{
        public final Map<Integer, Drink> ticketLine = new TreeMap<>();
        public final Map<Integer, String> customers = new TreeMap<>();
        public final Map<Integer, String> orders = new TreeMap<>();
        private final List<Integer> waiting = new ArrayList<>();
        private final List<Integer> coffeeMachine = new ArrayList<>();
        private final List<Integer> teaMachine = new ArrayList<>();
        public final List<Integer> tray = new ArrayList<>();

        public int createCustomerNumber(){
                synchronized (customers){
                        int customerNumber = 1;
                        if (!customers.isEmpty()){
                                customerNumber = customers.size();
                                customerNumber++;
                        }
                        customers.put(customerNumber, "idle");
                        return customerNumber;
                }
        }

        public void createOrder(int customerNumber, int orderNumber, String customerName, String drink){
                synchronized(ticketLine){
                        int maxDrinkNumber = 0;
                        for(Integer number : ticketLine.keySet())
                                if(maxDrinkNumber < number)
                                        maxDrinkNumber = number;
                        maxDrinkNumber++;
                        customers.put(customerNumber, "waiting");
                        createOrder(customerNumber, maxDrinkNumber, orderNumber, customerName, drink);
                }
        }

        public void createOrder(int customerNumber, int drinkNumber, int orderNumber, String customerName, String drink){
                Drink newDrink = new Drink(customerNumber, drinkNumber, orderNumber, customerName, drink);
                orders.put(orderNumber, "Waiting Area");
                newDrink.setDrinkStatus("Waiting Area");
                waiting.add(newDrink.getDrinkNumber());
                ticketLine.put(drinkNumber,newDrink);
                processOrder();
//                System.out.println(log());
        }

        public void processOrder(){
           synchronized(waiting){
                if(!waiting.isEmpty()){
                        for(int i = 0; i < waiting.size(); i++){
                                Drink drink = ticketLine.get(waiting.get(i));
                                String drinkChoice = drink.getDrink();
                                String[] substrings = drinkChoice.split("");
                                try{
                                        switch (substrings[0].toLowerCase()) {
                                                case "c" -> {
                                                        int coffeeSize = coffeeMachine.size();
                                                        if (coffeeSize < 2) {
                                                                drink.setDrinkStatus("Brewing Area");
                                                                System.out.println("Drink Number: " + drink.getDrinkNumber() + " has now started brewing");
                                                                coffeeMachine.add(waiting.get(i));
                                                                waiting.remove(i);
                                                        }
                                                }
                                                case "t" -> {
                                                        int teaSize = teaMachine.size();
                                                        if (teaSize < 2) {
                                                                drink.setDrinkStatus("Brewing Area");
                                                                System.out.println("Drink Number: " + drink.getDrinkNumber() + " has now started brewing");
                                                                teaMachine.add(waiting.get(i));
                                                                waiting.remove(i);
                                                        }
                                                }
                                        }
                                }catch(Exception e) {System.out.println(e);}
                        }
                }
//                System.out.println(log());
           }
        }

        public void brewTea(){
                try{
                     synchronized (teaMachine){
                             while(!teaMachine.isEmpty()){
                                     Drink drink = ticketLine.get(teaMachine.get(0));
                                     Thread.sleep(30000);
                                     drink.setDrinkStatus("Tray Area");
                                     System.out.println("Drink " + drink.getDrinkNumber() + " has now been moved to the tray area");
                                     tray.add(teaMachine.get(0));
                                     if (ticketLine.get(teaMachine.get(1)) != null){
                                             Drink tea2 = ticketLine.get(teaMachine.get(1));
                                             tea2.setDrinkStatus("Tray Area");
                                             System.out.println("Drink " + tea2.getDrinkNumber() + " has now been moved to the tray area");
                                             tray.add(teaMachine.get(1));
                                             teaMachine.remove(1);
                                     }
                                     teaMachine.remove(0);
                                     processOrder();
                                     serveCustomer();
                               }
//                             System.out.println(log());
                       }
                }catch(Exception exception){ System.out.println("there is no teas left to brew");}
        }

        public void brewCoffee(){
                try{
                        synchronized (coffeeMachine){
                                if(!coffeeMachine.isEmpty()){
                                        Drink drink = ticketLine.get(coffeeMachine.get(0));
                                        Thread.sleep(45000);
                                        drink.setDrinkStatus("Tray Area");
                                        System.out.println("Drink " + drink.getDrinkNumber() + " has now been moved to the tray area");
                                        tray.add(coffeeMachine.get(0));
                                        if (ticketLine.get(coffeeMachine.get(1)) != null){
                                                Drink coffee2 = ticketLine.get(coffeeMachine.get(1));
                                                coffee2.setDrinkStatus("Tray Area");
                                                System.out.println("Drink " + coffee2.getDrinkNumber() + " has now been moved to the tray area");
                                                tray.add(coffeeMachine.get(1));
                                                coffeeMachine.remove(1);
                                        }
                                        coffeeMachine.remove(0);
                                        processOrder();
                                        serveCustomer();
                                }
//                                System.out.println(log());
                        }
                }catch(Exception exception){ System.out.println("there is no coffees left to brew");}
        }

        public void serveCustomer(){
                synchronized (tray){
                        while (!tray.isEmpty()){
                                try {
                                        boolean result = true;
                                        int orderNumber = 0;
                                        for (int i = 0; i < tray.size(); i++) {
                                                Drink drink = ticketLine.get(tray.get(i));
                                                drink.setDrinkStatus("Served Area");
                                                tray.remove(i);
                                                System.out.println("Drink: " + drink.getDrink() + " has been served for " + drink.getCustomerName());
                                                orderNumber = drink.getOrderNumber();
                                                for (Drink drinks : ticketLine.values()){
                                                        if (drinks.getOrderNumber() == drink.getOrderNumber() && !drinks.getDrinkStatus().equalsIgnoreCase("served area")) {
                                                                result = false;
                                                                break;
                                                        }
                                                }
                                        }
                                        if (result){
                                                serveOrder(orderNumber);
                                        }
                                } catch(Exception e) {System.out.println(e);}
                        }
//                        System.out.println(log());
                }
        }
        public void serveOrder(int orderNumber) {
                synchronized (orders) {
                        int customerNumber = 0;
                        boolean ordersDone = true;
                        if (orders.containsKey(orderNumber)) {
                                orders.remove(orderNumber, orders.get(orderNumber));
                                System.out.println("Successfully served order number: " + orderNumber);
                                for (Drink drink : ticketLine.values()) {
                                        if (drink.getOrderNumber() == orderNumber) {
                                                customerNumber = drink.getCustomerNumber();
                                        } else if (drink.getCustomerNumber() == customerNumber && !drink.getDrinkStatus().equalsIgnoreCase("served")) {
                                                ordersDone = false;
                                        }
                                }
                                if (ordersDone) {
                                        customers.put(customerNumber, "idle");

                                }
                        }
                }
        }

        public List<String> getListOfOrders(String customerName){
                List<String> orders = new ArrayList<>();
                for(Drink drink : ticketLine.values()){
                        if (drink.getCustomerName().equals(customerName)){
                                String line = ("Order status for " + customerName  + " [Order Number: " + drink.getOrderNumber() + "] Drink: " + drink.getDrinkNumber() + " is currently in the " + drink.getDrinkStatus());
                                orders.add(line);
                        }
                }
                return orders;
        }

        public boolean checkDrink(Drink drink){
                boolean removed = false;
                synchronized (waiting){
                        for (Integer i : waiting){
                                Drink newDrink = ticketLine.get(i);
                                if (newDrink.getCustomerNumber() != drink.getCustomerNumber() && newDrink.getDrink().equalsIgnoreCase(drink.getDrink())){
                                        Drink result = transfer(newDrink, drink);
                                        ticketLine.remove(newDrink.getDrinkNumber(), newDrink);
                                        ticketLine.put(result.getDrinkNumber(), result);
                                        System.out.println("Drink: " + drink.getDrink() + " has been transferred from " + drink.getCustomerName() + " to " + result.getCustomerName());
                                        removed = true;
                                }
                        }
                }
                return removed;
        }

        private Drink transfer(Drink newDrink, Drink drink) {
                Drink resultDrink = new Drink(newDrink.getCustomerNumber(), drink.getDrinkNumber(), newDrink.getOrderNumber(), newDrink.getCustomerName(), newDrink.getDrink());
                resultDrink.setDrinkStatus(drink.getDrinkStatus());
                return resultDrink;
        }

        public void removeOrders(int customerNumber) {
                System.out.println("starting method");
                synchronized (waiting) {
                        for (Integer i : waiting) {
                                System.out.println("waiting");
                                Drink drink = ticketLine.get(i);
                                if (drink.getCustomerNumber() == customerNumber) {
                                        System.out.println("found waiting");
                                        waiting.remove(i);
                                        System.out.println("Drink: " + drink.getDrink() + " has been removed from waiting");
                                        ticketLine.remove(drink.getDrinkNumber(), drink);
                                }
                        }
                }
                synchronized (teaMachine) {
                        for (Integer i : teaMachine) {
                                System.out.println("tea");
                                Drink drink = ticketLine.get(i);
                                if (drink.getCustomerNumber() == customerNumber) {
                                        System.out.println("found tea");
                                        if (!checkDrink(drink)) {
                                                teaMachine.remove(i);
                                                System.out.println("Drink: " + drink.getDrink() + " has been removed from teaMachine");
                                                ticketLine.remove(i, drink);
                                        }
                                }
                        }
                }
                synchronized (coffeeMachine) {
                        for (Integer i : coffeeMachine) {
                                System.out.println("coffee");
                                Drink drink = ticketLine.get(i);
                                if (drink.getCustomerNumber() == customerNumber) {
                                        System.out.println("found coffee");
                                        if (!checkDrink(drink)) {
                                                coffeeMachine.remove(i);
                                                System.out.println("Drink: " + drink.getDrink() + " has been removed from coffeeMachine");
                                                ticketLine.remove(i, drink);
                                        }
                                }
                        }
                }
                synchronized (tray) {
                        for (Integer i : tray) {
                                System.out.println("tray");
                                Drink drink = ticketLine.get(i);
                                if (drink.getCustomerNumber() == customerNumber) {
                                        System.out.println("found tray");
                                        if (!checkDrink(drink)) {
                                                tray.remove(drink.getDrinkNumber());
                                                System.out.println("Drink: " + drink.getDrink() + " has been removed from tray");
                                                ticketLine.remove(i, drink);
                                        }
                                }
                        }
                }
                System.out.println("All orders for " + customerNumber + " have been removed");
                //                        System.out.println(log());
        }



//        public boolean getOrderStatus(int orderNumber){
//                synchronized (ticketLine){
//                        String status = "";
//                        for (Drink drink : ticketLine.values()){
//                                if (drink.getOrderNumber() == orderNumber){
//                                        if (status.isEmpty()){
//                                                status = drink.getDrinkStatus();
//                                        } else {
//                                                switch (status.toLowerCase()){
//                                                        case "tray area":
//                                                                status = drink.getDrinkStatus();
//                                                                break;
//                                                        case "brewing area":
//                                                                if (drink.getDrinkStatus().equalsIgnoreCase("waiting area"))
//                                                                        status = drink.getDrinkStatus();
//                                                                break;
//                                                        default:
//                                                                status = drink.getDrinkStatus();
//                                                }
//                                        }
//                                }
//                        }
//                        boolean result = false;
//                        if (status.equalsIgnoreCase("tray area"))
//                                result = true;
//                        return result;
//                }
//        }

        public String log(){
                int waitingItems = waiting.size();
                int teaItems = teaMachine.size();
                int coffeeItems = coffeeMachine.size();
                int trayItems = tray.size();
                int customersInCafe = customers.size();
                int idleCustomers = 0;
                int waitingCustomers = 0;
                StringBuilder waitingDrinks = new StringBuilder();
                StringBuilder brewingDrinks = new StringBuilder();
                StringBuilder trayDrinks = new StringBuilder();

                for (String s : customers.values()){
                        if (s.equalsIgnoreCase("idle")){
                                idleCustomers++;
                        } else {
                                waitingCustomers++;
                        }
                }
                for (Integer i : waiting){
                        if (ticketLine.get(i).getDrink() != null) {
                                waitingDrinks.append(ticketLine.get(i).getDrink()).append(" ");
                        }
                }
                for (Integer i : teaMachine){
                        if (ticketLine.get(i).getDrink() != null) {
                                brewingDrinks.append("1 tea ");
                        }
                }
                for (Integer i : coffeeMachine) {
                        if (ticketLine.get(i).getDrink() != null) {
                                brewingDrinks.append("1 coffee ");
                        }
                }
                for (Integer i : tray){
                        if (ticketLine.get(i).getDrink() != null) {
                                trayDrinks.append(ticketLine.get(i).getDrink()).append(" ");
                        }
                }

                return "Waiting Number of items: " + waitingItems +
                        "\nWaitingArea Items: " + waitingDrinks +
                        "\nteaMachine Number of items: " + teaItems +
                        "\ncoffeeMachine Number of items: " + coffeeItems +
                        "\nBrewingArea Items:  " + brewingDrinks +
                        "\nTray Number of items: " + trayItems +
                        "\ntrayDrinks Items: " + trayDrinks +
                        "\nCustomers in Cafe Number: " + customersInCafe +
                        "\nidle customers in Cafe: " + idleCustomers +
                        "\nwaiting customers in Cafe: " + waitingCustomers;
        }
//        public void serialise(Drink drink, String filename) {
//                try {
//                        ObjectOutputStream out = new ObjectOutputStream((new FileOutputStream(filename)));
//                        out.writeObject(drink);
//                        System.out.println("Serialisation successful.");
//                } catch (IOException e) {
//                        e.printStackTrace();
//                }
//        }
//
//        public Drink deserialise(String filename){
//                Drink drink = null;
//                try {
//                        ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
//                        drink = (Drink) in.readObject();
//                        System.out.println("De-serialisation successful.");
//                } catch (IOException | ClassNotFoundException e) {
//                        e.printStackTrace();
//                }
//                return drink;
//        }

}
   

