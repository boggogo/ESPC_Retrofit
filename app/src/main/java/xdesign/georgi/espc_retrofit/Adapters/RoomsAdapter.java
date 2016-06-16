package xdesign.georgi.espc_retrofit.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xdesign.georgi.espc_retrofit.Backend.Property;
import xdesign.georgi.espc_retrofit.Backend.Room;
import xdesign.georgi.espc_retrofit.R;
import xdesign.georgi.espc_retrofit.UI.Dialogs.ConfDelPropertyDialog;
import xdesign.georgi.espc_retrofit.UI.Dialogs.UpdatePropertyDialog;
import xdesign.georgi.espc_retrofit.UI.MainActivity;
import xdesign.georgi.espc_retrofit.UI.PropertyDetailsActivity;
import xdesign.georgi.espc_retrofit.UI.RatingsActivity;
import xdesign.georgi.espc_retrofit.Utils.Constants;

/**
 * Created by georgi on 14/06/16.
 */
public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.PropertyViewHolder> {
    private static final String TAG = RoomsAdapter.class.getSimpleName();
    private static ArrayList<Room> rooms;
    public static Activity mParent;
    public static int propertyPosition;

    public RoomsAdapter(Activity activity, ArrayList<Room> rooms) {
        this.rooms = rooms;
        this.mParent = activity;
    }

    @Override
    public PropertyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rooms_list_item, parent, false);
        return new PropertyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PropertyViewHolder holder, int position) {
        Room room = rooms.get(position);
        holder.roomName.setText(room.getName());
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }


    public static class PropertyViewHolder extends RecyclerView.ViewHolder{
        TextView roomName;

        public PropertyViewHolder(View view) {
            super(view);
            roomName = (TextView) view.findViewById(R.id.roomName);
        }

    }

}
