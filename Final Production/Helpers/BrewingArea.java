public class BrewingArea {
    private int numTeas;
    private int numCoffees;

    public BrewingArea() {
        this.numTeas = 0;
        this.numCoffees = 0;
    }

    public boolean canBrewTea() {
        return this.numTeas < 2;
    }

    public boolean canBrewCoffee() {
        return this.numCoffees < 2;
    }

    public void brewTea() {
        this.numTeas++;
        // Simulate tea preparation time
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.numTeas--;
    }

    public void brewCoffee() {
        this.numCoffees++;
        // Simulate coffee preparation time
        try {
            Thread.sleep(45000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.numCoffees--;
    }

    public int getNumTeas() {
        return this.numTeas;
    }

    public int getNumCoffees() {
        return this.numCoffees;
    }
}
