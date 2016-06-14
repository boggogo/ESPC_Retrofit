package xdesign.georgi.espc_retrofit;

import java.math.BigInteger;

/**
 * Created by georgi on 14/06/16.
 */
public class Property {

    private int id;
    private String address;
    private double price;

    public Property() {
        this.id = 0;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
