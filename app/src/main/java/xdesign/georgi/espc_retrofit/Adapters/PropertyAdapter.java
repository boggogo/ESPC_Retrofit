package xdesign.georgi.espc_retrofit.Adapters;

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
public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder> {
    private static final String TAG = PropertyAdapter.class.getSimpleName();
    private static List<Property> mProperties;
    private static ArrayList<UserPropertyRating> mUserPropertyRatings;
    public static MainActivity mParent;
    public static int propertyPosition;

    public PropertyAdapter(MainActivity activity, List<Property> mProperties, ArrayList<UserPropertyRating> mUserPropertyRatings) {
        this.mProperties = mProperties;
        this.mParent = activity;
        this.mUserPropertyRatings = mUserPropertyRatings;
    }

    @Override
    public PropertyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.property_list_item, parent, false);
        return new PropertyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PropertyViewHolder holder, int position) {
        Property property = mProperties.get(position);
        holder.propertyAddress.setText(property.getAddress());
        holder.propertyPrice.setText(property.getPrice() + "");
        holder.propertyRating.setText(getOverallPropertyRatingById(property.getId())+"");
    }

    private int getOverallPropertyRatingById(int id) {
        double totalOverAllRating = 0.0;

        for(UserPropertyRating upr: mUserPropertyRatings){
            if(upr.getPropertyID() == id){
                totalOverAllRating += upr.getOverallRating();
                Log.d(TAG,"Overall rating total value:-> "+totalOverAllRating + "");
            }


        }
        Log.d(TAG,"===================================");
        // 10 is the maximum
        return ((int)(totalOverAllRating % 10));
    }

    @Override
    public int getItemCount() {
        return mProperties.size();
    }


    public static class PropertyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        TextView propertyAddress, propertyPrice, propertyRating;

        public PropertyViewHolder(View view) {
            super(view);
            propertyAddress = (TextView) view.findViewById(R.id.address);
            propertyPrice = (TextView) view.findViewById(R.id.price);
            propertyRating = (TextView)view.findViewById(R.id.propertyRating);
            view.setOnClickListener(this);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(mParent, v);
                    MenuInflater inflater = popupMenu.getMenuInflater();
                    inflater.inflate(R.menu.property_popup_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(PropertyViewHolder.this);

                    // save the position of the item that was long pressed...
                    propertyPosition = getAdapterPosition();
                    Log.d(TAG,"Item position: " + propertyPosition);
                    popupMenu.show();
                    return true;
                }
            });
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "Clicked view: " + getAdapterPosition());
            Intent intent = new Intent(mParent, PropertyDetailsActivity.class);

            Bundle bundle = new Bundle();

            bundle.putSerializable(Constants.KEY_PROPERTY_OBJECT, mProperties.get(getAdapterPosition()));

            intent.putExtras(bundle);
            mParent.startActivity(intent);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.popup_deleteProperty:
                    //....
                    Log.d(TAG," delete pop up menu Item clicked. Property index: " + propertyPosition);
//                    mParent.onPositiveDeletePropertyById(mProperties.get(propertyPosition));
                    ConfDelPropertyDialog dialog = ConfDelPropertyDialog.newInstance(mProperties.get(propertyPosition)
                            , mParent.getString(R.string.conf_delete_property_dialog_title)
                            , mParent.getString(R.string.conf_delete_property_dialog_message));
                    dialog.show(mParent.getFragmentManager(), mParent.getString(R.string.conf_delete_property_dialog_tag));

                    return true;
                case R.id.popup_updateProperty:
                    //....
                    Log.d(TAG," update pop up menu Item clicked. Property index: " + propertyPosition);
                    UpdatePropertyDialog updateDialog = UpdatePropertyDialog.newInstance(propertyPosition
                    , mParent.getString(R.string.update_property_dialog_title), mParent.getString(R.string.update_property_dialog_message));
                    updateDialog.show(mParent.getFragmentManager(), mParent.getString(R.string.update_property_dialog_tag));
                    return true;

                case R.id.popup_showRatingOfProperty:
                    // Show ratings of this property
                    Intent ratingsIntent = new Intent(mParent, RatingsActivity.class);
                    //put necessary data into the intent's bundle here

                    // save the clicked property's id in the bundle...
                    ratingsIntent.putExtra(Constants.KEY_PROPERTY_ID,mProperties.get(propertyPosition).getId());
                    Log.d(TAG,"set the property id to the intent: " + mProperties.get(propertyPosition).getId());

                    ratingsIntent.putExtra(Constants.KEY_PROPERTY_NAME,mProperties.get(propertyPosition).getAddress());
                    mParent.startActivity(ratingsIntent);

                default:
                    return false;
            }
        }

    }

}
