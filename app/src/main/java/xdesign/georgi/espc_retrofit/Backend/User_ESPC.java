package xdesign.georgi.espc_retrofit.Backend;

/**
 * Created by georgi on 14/06/16.
 */
public class User_ESPC {
    private int id;
    private String name;
    private int password;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_espc_column_name() {
        return name;
    }

    public void setUSER_ESPC_COLUMN_NAME(String name) {
        this.name = name;
    }

    public int getUser_espc_column_password() {
        return password;
    }

    public void setUSER_ESPC_COLUMN_PASSWORD(int password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User_ESPC{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password=" + password +
                '}';
    }
}
