package helpers;

import java.io.Serializable;

public class Request implements Serializable {
    private RequestType type;
    private String name;
    private int numCoffees;
    private int numTeas;

    public Request(RequestType type, String name, int numCoffees, int numTeas) {
        this.type = type;
        this.name = name;
        this.numCoffees = numCoffees;
        this.numTeas = numTeas;
    }

    public RequestType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getNumCoffees() {
        return numCoffees;
    }

    public int getNumTeas() {
        return numTeas;
    }
}
