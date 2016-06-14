package xdesign.georgi.espc_retrofit.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import xdesign.georgi.espc_retrofit.Backend.Property;
import xdesign.georgi.espc_retrofit.R;

/**
 * Created by georgi on 14/06/16.
 */
public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder> {
    private static final String TAG = PropertyAdapter_NOT_USED.class.getSimpleName();
    private List<Property> mProperties;
    //private Context mContext;

    public PropertyAdapter(List<Property> mProperties) {
        this.mProperties = mProperties;
        //this.mContext = mContext;
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
        holder.propertyPrice.setText(property.getPrice()+"");
    }

    @Override
    public int getItemCount() {
        return mProperties.size();
    }


    public static class PropertyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView propertyAddress, propertyPrice;

        public PropertyViewHolder(View view) {
            super(view);
            propertyAddress = (TextView)view.findViewById(R.id.address);
            propertyPrice = (TextView)view.findViewById(R.id.price);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "Clicked view: " + getAdapterPosition());
        }
    }

}
