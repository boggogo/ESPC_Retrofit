package xdesign.georgi.espc_retrofit.Backend;

import java.io.Serializable;

/**
 * Created by georgi on 15/06/16.
 */
public class Room implements Serializable {
    private int id;
    private int propertyID;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPropertyID() {
        return propertyID;
    }

    public void setPropertyID(int propertyID) {
        this.propertyID = propertyID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", propertyID=" + propertyID +
                ", name='" + name + '\'' +
                '}';
    }
}
