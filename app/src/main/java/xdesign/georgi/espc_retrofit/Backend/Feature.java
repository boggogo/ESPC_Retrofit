package xdesign.georgi.espc_retrofit.Backend;

import java.math.BigInteger;

public class Feature {
        private int id;
        private String name;
        private int roomID;

        public String getFeature_column_name() {
            return name;
        }

        public void setFEATURE_COLUMN_NAME(String name) {
            this.name = name;
        }

        public int getFeature_column_roomid() {
            return roomID;
        }

        public void setFEATURE_COLUMN_ROOMID(int roomID) {
            this.roomID = roomID;
        }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
        public String toString() {
            return "Feature{" +
                    "name='" + name + '\'' +
                    ", roomID=" + roomID +
                    '}';
        }
    }