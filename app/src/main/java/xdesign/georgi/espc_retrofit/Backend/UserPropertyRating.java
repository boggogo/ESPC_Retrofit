package xdesign.georgi.espc_retrofit.Backend;

import java.io.Serializable;

/**
 * Created by georgi on 15/06/16.
 */
public class UserPropertyRating implements Serializable{
    private int id;
    private String uuid;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int userID;
    private int propertyID;
    private int overallRating;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getPropertyID() {
        return propertyID;
    }

    public void setPropertyID(int propertyID) {
        this.propertyID = propertyID;
    }

    public int getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(int overallRating) {
        this.overallRating = overallRating;
    }

    @Override
    public String toString() {
        return "UserPropertyRating{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", userID=" + userID +
                ", propertyID=" + propertyID +
                ", overallRating=" + overallRating +
                '}';
    }
}
