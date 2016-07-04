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
    private int price;
    private String lastUpdated;
    private String uuid;


    public String getProperty_column_uuid() {
        return uuid;
    }

    public void setPROPERTY_COLUMN_UUID(String uuid) {
        this.uuid = uuid;
    }

    public int getProperty_column_userid() {
        return userID;
    }

    public void setPROPERTY_COLUMN_USERID(int userID) {
        this.userID = userID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProperty_column_address() {
        return address;
    }

    public void setPROPERTY_COLUMN_ADDRESS(String address) {
        this.address = address;
    }

    public int getProperty_column_price() {
        return price;
    }

    public void setPROPERTY_COLUMN_PRICE(int price) {
        this.price = price;
    }

    public String getProperty_column_lastupdated() {
        return lastUpdated;
    }

    public void setPROPERTY_COLUMN_LASTUPDATED(String lastUpdated) {
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
                ", uuid='" + uuid + '\'' +
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
