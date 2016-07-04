package xdesign.georgi.espc_retrofit.Backend;

/**
 * Created by georgi on 04/07/16.
 */
public class UserRoomRating {
    private int id;
    private int userroomrating_column_roomid;
    private int userroomrating_column_userid;
    private int userroomrating_column_rating;


    public int getUserroomrating_column_rating() {
        return userroomrating_column_rating;
    }

    public void setUSERROOMRATING_COLUMN_RATING(int userroomrating_column_rating) {
        this.userroomrating_column_rating = userroomrating_column_rating;
    }

    public int getUserroomrating_column_userid() {
        return userroomrating_column_userid;
    }

    public void setUSERROOMRATING_COLUMN_USERID(int userroomrating_column_userid) {
        this.userroomrating_column_userid = userroomrating_column_userid;
    }

    public int getUserroomrating_column_roomid() {
        return userroomrating_column_roomid;
    }

    public void setUSERROOMRATING_COLUMN_ROOMID(int userroomrating_column_roomid) {
        this.userroomrating_column_roomid = userroomrating_column_roomid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
