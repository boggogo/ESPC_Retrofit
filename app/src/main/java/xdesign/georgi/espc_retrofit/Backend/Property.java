package xdesign.georgi.espc_retrofit.Backend;

import android.annotation.TargetApi;
import android.os.Build;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

/**
 * Created by georgi on 14/06/16.
 */
public class Property implements Serializable{

    private int id;
    private int userID;
    private String address;
    private String price;
    private String lastUpdated;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "Property{" +
                "id=" + id +
                ", userID=" + userID +
                ", address='" + address + '\'' +
                ", price='" + price + '\'' +
                ", lastUpdated='" + lastUpdated + '\'' +
                '}';
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Property prop = (Property) o;
        return Objects.equals(id, prop.getId());
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
