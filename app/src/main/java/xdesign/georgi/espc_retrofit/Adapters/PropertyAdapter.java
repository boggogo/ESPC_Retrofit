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

import java.util.List;

import xdesign.georgi.espc_retrofit.Backend.Property;
import xdesign.georgi.espc_retrofit.R;
import xdesign.georgi.espc_retrofit.UI.Dialogs.ConfDelPropertyDialog;
import xdesign.georgi.espc_retrofit.UI.Dialogs.UpdatePropertyDialog;
import xdesign.georgi.espc_retrofit.UI.MainActivity;
import xdesign.georgi.espc_retrofit.UI.PropertyDetailsActivity;
import xdesign.georgi.espc_retrofit.Utils.Constants;

/**
 * Created by georgi on 14/06/16.
 */
public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder> {
    private static final String TAG = PropertyAdapter.class.getSimpleName();
    private static List<Property> mProperties;
    public static MainActivity mParant;
    public static int propertyPosition;

    public PropertyAdapter(MainActivity activity, List<Property> mProperties) {
        this.mProperties = mProperties;
        this.mParant = activity;
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
    }

    @Override
    public int getItemCount() {
        return mProperties.size();
    }


    public static class PropertyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        TextView propertyAddress, propertyPrice;

        public PropertyViewHolder(View view) {
            super(view);
            propertyAddress = (TextView) view.findViewById(R.id.address);
            propertyPrice = (TextView) view.findViewById(R.id.price);
            view.setOnClickListener(this);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(mParant, v);
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
            Intent intent = new Intent(mParant, PropertyDetailsActivity.class);

            Bundle bundle = new Bundle();

            bundle.putSerializable(Constants.KEY_PROPERTY_OBJECT, mProperties.get(getAdapterPosition()));

            intent.putExtras(bundle);
            mParant.startActivity(intent);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.popup_deleteProperty:
                    //....
                    Log.d(TAG," delete pop up menu Item clicked. Property index: " + propertyPosition);
//                    mParant.onPositiveDeletePropertyById(mProperties.get(propertyPosition));
                    ConfDelPropertyDialog dialog = ConfDelPropertyDialog.newInstance(mProperties.get(propertyPosition)
                            ,mParant.getString(R.string.conf_delete_property_dialog_title)
                            ,mParant.getString(R.string.conf_delete_property_dialog_message));
                    dialog.show(mParant.getFragmentManager(),mParant.getString(R.string.conf_delete_property_dialog_tag));

                    return true;
                case R.id.popup_updateProperty:
                    //....
                    Log.d(TAG," update pop up menu Item clicked. Property index: " + propertyPosition);
                    UpdatePropertyDialog updateDialog = UpdatePropertyDialog.newInstance(propertyPosition
                    ,mParant.getString(R.string.update_property_dialog_title),mParant.getString(R.string.update_property_dialog_message));
                    updateDialog.show(mParant.getFragmentManager(),mParant.getString(R.string.update_property_dialog_tag));
                    return true;

                default:
                    return false;
            }
        }

    }

}
