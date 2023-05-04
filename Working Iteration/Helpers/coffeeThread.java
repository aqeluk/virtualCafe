package Helpers;

public class coffeeThread implements Runnable{
    private Cafe cafe;
        
    public coffeeThread(Cafe cafe){this.cafe = cafe;}

    @Override
    public void run(){
        cafe.brewCoffee();
        cafe.serveCustomer();
        cafe.brewCoffee();
        cafe.serveCustomer();
    }

}
