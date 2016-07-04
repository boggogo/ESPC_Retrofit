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

    public int getRoom_column_propertyid() {
        return propertyID;
    }

    public void setROOM_COLUMN_PROPERTYID(int propertyID) {
        this.propertyID = propertyID;
    }

    public String getRoom_column_name() {
        return name;
    }

    public void setROOM_COLUMN_NAME(String name) {
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
