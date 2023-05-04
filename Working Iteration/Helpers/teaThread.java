package Helpers;

public class teaThread implements Runnable {
   private Cafe cafe;
   
   public teaThread(Cafe cafe){
       this.cafe = cafe;
   }

   @Override
   public void run(){
     cafe.brewTea();
     cafe.serveCustomer();
     cafe.brewTea();
     cafe.serveCustomer();
   }
}
