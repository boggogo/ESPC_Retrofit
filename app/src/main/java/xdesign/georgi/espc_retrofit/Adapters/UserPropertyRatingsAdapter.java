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
import xdesign.georgi.espc_retrofit.Backend.UserPropertyRating;
import xdesign.georgi.espc_retrofit.Backend.User_ESPC;
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
public class UserPropertyRatingsAdapter extends RecyclerView.Adapter<UserPropertyRatingsAdapter.PropertyViewHolder> {
    private static final String TAG = UserPropertyRatingsAdapter.class.getSimpleName();
    private static ArrayList<UserPropertyRating> mPropertyRatings;
    private ArrayList<User_ESPC> mUsers;
    public static Activity mParent;
    public static int propertyPosition;

    public UserPropertyRatingsAdapter(RatingsActivity activity, ArrayList<UserPropertyRating> propertyRatings, ArrayList<User_ESPC> users) {
        this.mPropertyRatings = propertyRatings;
        this.mUsers = users;
        this.mParent = activity;
    }

    @Override
    public PropertyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_property_list_item, parent, false);
        return new PropertyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PropertyViewHolder holder, int position) {
        UserPropertyRating userPropertyRating = mPropertyRatings.get(position);
        holder.addedByUserView.setText(getUserNameByUserID(userPropertyRating.getUserID()));

    }

    private String getUserNameByUserID(int userID) {
        for(User_ESPC u: mUsers){
            if(u.getId() == userID){
                return u.getName();
            }
        }
        return "unknown";
    }

    @Override
    public int getItemCount() {
        return mPropertyRatings.size();
    }


    public static class PropertyViewHolder extends RecyclerView.ViewHolder{
        TextView addedByUserView;

        public PropertyViewHolder(View view) {
            super(view);
            addedByUserView = (TextView) view.findViewById(R.id.addedByUser);
        }



    }

}
