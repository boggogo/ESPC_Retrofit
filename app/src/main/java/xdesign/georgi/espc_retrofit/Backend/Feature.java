package xdesign.georgi.espc_retrofit.Backend;

import java.math.BigInteger;

public class Feature {
        private int id;
        private String name;
        private int roomID;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getRoomID() {
            return roomID;
        }

        public void setRoomID(int roomID) {
            this.roomID = roomID;
        }

        @Override
        public String toString() {
            return "Feature{" +
                    "name='" + name + '\'' +
                    ", roomID=" + roomID +
                    '}';
        }
    }