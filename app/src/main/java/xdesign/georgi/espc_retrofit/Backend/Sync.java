package xdesign.georgi.espc_retrofit.Backend;

/**
 * Created by georgi on 22/06/16.
 */
public class Sync {

    private int id;
    private String uuid;
    private String table;
    private String action;
    private long timeChanged;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSync_column_uuid() {
        return uuid;
    }

    public void setSYNC_COLUMN_UUID(String uuid) {
        this.uuid = uuid;
    }

    public String getSync_column_table() {
        return table;
    }

    public void setSYNC_COLUMN_TABLE(String table) {
        this.table = table;
    }

    public String getSync_column_action() {
        return action;
    }

    public void setSYNC_COLUMN_ACTION(String action) {
        this.action = action;
    }

    public long getSync_column_timechanged() {
        return timeChanged;
    }

    public void setSYNC_COLUMN_TIMECHANGED(long timeChanged) {
        this.timeChanged = timeChanged;
    }

    @Override
    public String toString() {
        return "id: " + getId() + " uuid: " + getSync_column_uuid() + " table: " + getSync_column_table() + " action: " + getSync_column_action() + " timeStamp: " + getSync_column_timechanged();
    }

//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Sync sync = (Sync) o;
//        return Objects.equals(id, prop.getId());
//    }
}
