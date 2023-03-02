public class Order {
    private String name;
    private int numCoffees;
    private int numTeas;

    public Order(String name, int numCoffees, int numTeas) {
        this.name = name;
        this.numCoffees = numCoffees;
        this.numTeas = numTeas;
    }

    public String getName() {
        return name;
    }

    public int getNumCoffees() {
        return numCoffees;
    }

    public void setNumCoffees(int numCoffees) {
        this.numCoffees = numCoffees;
    }

    public int getNumTeas() {
        return numTeas;
    }

    public void setNumTeas(int numTeas) {
        this.numTeas = numTeas;
    }

    @Override
    public String toString() {
        return numCoffees + " coffee(s) and " + numTeas + " tea(s)";
    }
}

