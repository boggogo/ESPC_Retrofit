package xdesign.georgi.espc_retrofit.Utils;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
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
