package xdesign.georgi.espc_retrofit.Backend;

import java.io.Serializable;

/**
 * Created by georgi on 15/06/16.
 */
public class UserPropertyRating implements Serializable{
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int userID;
    private int propertyID;
    private int overallRating;

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
                "userID=" + userID +
                ", propertyID=" + propertyID +
                ", overallRating=" + overallRating +
                '}';
    }
}
