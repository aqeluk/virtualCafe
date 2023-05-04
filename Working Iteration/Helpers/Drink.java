package Helpers;

public class Drink {
    private final int customerNumber;
    private final int drinkNumber;
    private final int orderNumber;
    private final String customerName;
    private final String drink;
    private String orderStatus;

    
    public Drink(int customerNumber, int drinkNumber, int orderNumber, String customerName, String drink){
        this.customerNumber = customerNumber;
        this.drinkNumber = drinkNumber;
        this.customerName = customerName;
        this.drink = drink;
        this.orderNumber = orderNumber;
    }

    public String getCustomerName() {return customerName;}
    public String getDrink() {return drink;}
    public int getOrderNumber() {return orderNumber;}
    public String getDrinkStatus() {return orderStatus;}
    public void setDrinkStatus(String newStatus) {orderStatus = newStatus;}

    public int getDrinkNumber() {
        return drinkNumber;
    }

    public int getCustomerNumber() {
        return customerNumber;
    }
}
