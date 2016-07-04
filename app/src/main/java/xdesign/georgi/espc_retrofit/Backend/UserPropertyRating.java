package xdesign.georgi.espc_retrofit.Backend;

import java.io.Serializable;

/**
 * Created by georgi on 15/06/16.
 */
public class UserPropertyRating implements Serializable {
    private int id;
    private String uuid;
    private int userID;
    private int propertyID;
    private int overallRating;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserpropertyrating_column_uuid() {
        return uuid;
    }

    public void setUSERPROPERTYRATING_COLUMN_UUID(String uuid) {
        this.uuid = uuid;
    }

    public int getUserpropertyrating_column_userid() {
        return userID;
    }

    public void setUSERPROPERTYRATING_COLUMN_USERID(int userID) {
        this.userID = userID;
    }

    public int getUserpropertyrating_column_propertyid() {
        return propertyID;
    }

    public void setUSERPROPERTYRATING_COLUMN_PROPERTYID(int propertyID) {
        this.propertyID = propertyID;
    }

    public int getUserpropertyrating_column_overallrating() {
        return overallRating;
    }

    public void setUSERPROPERTYRATING_COLUMN_OVERALLRATING(int overallRating) {
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
