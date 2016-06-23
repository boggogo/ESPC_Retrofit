package xdesign.georgi.espc_retrofit.Backend;

import java.util.Objects;

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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public long getTimeChanged() {
        return timeChanged;
    }

    public void setTimeChanged(long timeChanged) {
        this.timeChanged = timeChanged;
    }

    @Override
    public String toString() {
        return "id: " + getId() + " uuid: " + getUuid() + " table: " + getTable() + " action: " + getAction() + " timeStamp: " + getTimeChanged();
    }

//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Sync sync = (Sync) o;
//        return Objects.equals(id, prop.getId());
//    }
}
