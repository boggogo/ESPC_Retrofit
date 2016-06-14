package xdesign.georgi.espc_retrofit.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xdesign.georgi.espc_retrofit.Utils.Property;
import xdesign.georgi.espc_retrofit.R;

/**
 * Created by georgi on 14/06/16.
 */
public class PropertyAdapter_NOT_USED extends ArrayAdapter<Property> {
    private static final String TAG = PropertyAdapter_NOT_USED.class.getSimpleName();
    private List<Property> mProperties;
    private Context mContext;


    public PropertyAdapter_NOT_USED(Context context, int layoutResourceId, ArrayList<Property> properties) {
        super(context, layoutResourceId, properties);
        mProperties = properties;
        mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       final ViewHolder holder;

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.property_list_item,null);
            holder = new ViewHolder();
            holder.propertyAddress = (TextView)convertView.findViewById(R.id.address);
            holder.propertyPrice = (TextView)convertView.findViewById(R.id.price);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        Property property = mProperties.get(position);

        holder.propertyAddress.setText(property.getAddress());
        holder.propertyPrice.setText(property.getPrice()+"");


        return convertView;
    }





    private static class ViewHolder {
        TextView propertyAddress;
        TextView propertyPrice;
    }










}







