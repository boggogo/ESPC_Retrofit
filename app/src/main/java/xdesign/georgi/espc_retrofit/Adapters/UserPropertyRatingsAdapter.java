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

import xdesign.georgi.espc_retrofit.Backend.UserPropertyRating;
import xdesign.georgi.espc_retrofit.Backend.User_ESPC;
import xdesign.georgi.espc_retrofit.R;
import xdesign.georgi.espc_retrofit.UI.Dialogs.ConfDelPropRatingDialog;
import xdesign.georgi.espc_retrofit.UI.Dialogs.UpdatePropertyRatingDialog;
import xdesign.georgi.espc_retrofit.UI.RatingsActivity;

/**
 * Created by georgi on 14/06/16.
 */
public class UserPropertyRatingsAdapter extends RecyclerView.Adapter<UserPropertyRatingsAdapter.UserPropertyRatingViewHolder> {
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
    public UserPropertyRatingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_property_list_item, parent, false);
        return new UserPropertyRatingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserPropertyRatingViewHolder holder, int position) {
        UserPropertyRating userPropertyRating = mPropertyRatings.get(position);
        holder.addedByUserView.setText(getUserNameByUserID(userPropertyRating.getUserID()));
        holder.mRatingsContainer.setText(userPropertyRating.getOverallRating()+"");



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


    public static class UserPropertyRatingViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, PopupMenu.OnMenuItemClickListener{
        TextView addedByUserView, mRatingsContainer;

        public UserPropertyRatingViewHolder(View view) {
            super(view);
            addedByUserView = (TextView) view.findViewById(R.id.addedByUser);
            mRatingsContainer = (TextView) view.findViewById(R.id.userRating);
            view.setOnLongClickListener(this);
        }


        @Override
        public boolean onLongClick(View v) {
            PopupMenu popupMenu = new PopupMenu(mParent, v);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.popup_ratings_menu_layout, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(UserPropertyRatingViewHolder.this);

            // save the position of the item that was long pressed...
            propertyPosition = getAdapterPosition();
            Log.d(TAG,"Item position: " + propertyPosition);
            popupMenu.show();


            return false;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.popup_deletePropertyRating:
                    //....
                    Log.d(TAG," delete pop up menu Item clicked. Property index: " + propertyPosition);
//                    mParent.onPositiveDeletePropertyById(mProperties.get(propertyPosition));
//                    ConfDelPropertyDialog dialog = ConfDelPropertyDialog.newInstance(mPropertyRatings.get(propertyPosition)
//                            , mParent.getString(R.string.conf_delete_property_dialog_title)
//                            , mParent.getString(R.string.conf_delete_property_dialog_message));
//                    dialog.show(mParent.getFragmentManager(), mParent.getString(R.string.conf_delete_property_dialog_tag));
//                    ((RatingsActivity) mParent).onPositiveDeletePropertyRatingById();
                    ConfDelPropRatingDialog confirmDeleteOfRatingDialog = ConfDelPropRatingDialog.newInstance(mPropertyRatings.get(propertyPosition),"Delete rating","You are about to delete this rating for this property. Press Delete to confirm.");
                    confirmDeleteOfRatingDialog.show(mParent.getFragmentManager(),"confirm_delete_of_this_property_rating");

                    return true;
                case R.id.popup_updatePropertyRating:
                    //....
                    Log.d(TAG," update pop up menu Item clicked. Property index: " + propertyPosition);
                    UpdatePropertyRatingDialog dialog = UpdatePropertyRatingDialog.newInstance(propertyPosition,"Update Rating","Set the new rating for this property.");
                    dialog.show(mParent.getFragmentManager(),"update_property_rating_dialog_tag");

                    return true;

                default:
                    return false;
            }
        }
    }

}
