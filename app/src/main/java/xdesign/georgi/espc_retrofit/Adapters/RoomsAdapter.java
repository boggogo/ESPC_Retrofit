package xdesign.georgi.espc_retrofit.Adapters;

import android.app.Activity;
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

import xdesign.georgi.espc_retrofit.Backend.Room;
import xdesign.georgi.espc_retrofit.R;
import xdesign.georgi.espc_retrofit.UI.Dialogs.ConfDeleteRoomDialog;
import xdesign.georgi.espc_retrofit.UI.Dialogs.UpdateRoomDialog;

/**
 * Created by georgi on 14/06/16.
 */
public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.RoomViewHolder> {
    private static final String TAG = RoomsAdapter.class.getSimpleName();
    private static ArrayList<Room> mRooms;
    public static Activity mParent;

    public static int propertyPosition;

    public RoomsAdapter(Activity activity, ArrayList<Room> mRooms) {
        this.mRooms = mRooms;
        this.mParent = activity;
    }

    @Override
    public RoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rooms_list_item, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RoomViewHolder holder, int position) {
        Room room = mRooms.get(position);
        holder.roomName.setText(room.getName());
    }

    @Override
    public int getItemCount() {
        return mRooms.size();
    }


    public static class RoomViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, PopupMenu.OnMenuItemClickListener {
        TextView roomName;

        public RoomViewHolder(View view) {
            super(view);
            roomName = (TextView) view.findViewById(R.id.roomName);
            view.setOnLongClickListener(RoomViewHolder.this);
        }


        @Override
        public boolean onLongClick(View v) {
            PopupMenu popupMenu = new PopupMenu(mParent, v);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.popup_rooms_menu_layout, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(RoomViewHolder.this);

            // save the position of the item that was long pressed...
            propertyPosition = getAdapterPosition();
            Log.d(TAG,"Item position: " + propertyPosition);
            popupMenu.show();


            return true;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.popup_deleteRoom:
                    //....
                    Log.d(TAG, " delete pop up menu Item clicked. Property index: " + propertyPosition);

                    ConfDeleteRoomDialog confDeleteRoomDialog = ConfDeleteRoomDialog.newInstance(mRooms.get(propertyPosition),"Delete Room","You are about to delete this room. Please confirm by pressing Delete.");
                    confDeleteRoomDialog.show(mParent.getFragmentManager(),"confirm_delete_room_dialog_tag");

                    return true;
                case R.id.popup_updateRoom:
                    //....
                    Log.d(TAG, " update pop up menu Item clicked. Property index: " + propertyPosition);
//                    UpdatePropertyRatingDialog dialog = UpdatePropertyRatingDialog.newInstance(propertyPosition, "Update Rating", "Set the new rating for this property.");
//                    dialog.show(mParent.getFragmentManager(), "update_property_rating_dialog_tag");

                    UpdateRoomDialog dialog = UpdateRoomDialog.newInstance(mRooms.get(propertyPosition),"Update details","Update the details of this room");
                    dialog.show(mParent.getFragmentManager(),"update_room_details_dialog");

                    return true;

                default:
                    return false;
            }

        }
    }

}
