package xdesign.georgi.espc_retrofit.UI.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import xdesign.georgi.espc_retrofit.R;
import xdesign.georgi.espc_retrofit.UI.MainActivity;

/**
 * Created by georgi on 15/06/16.
 */
public class UpdatePropertyDialog extends DialogFragment {
    private String TAG = UpdatePropertyDialog.class.getSimpleName();
    // Title key value
    private static final String DIALOG_TITLE = "alert_dialog_title";
    // Message key value
    private static final String DIALOG_MESSAGE = "alert_dialog_message";
    // Parent key
    private static final String DIALOG_PARENT = "dialog_parent";
    // Dialog delete property
    private static final String DIALOG_DELETING_PROPERTY_INDEX = "property_to_be_deleted";
    // String variable to hold the dialog title
    private String mTitle;
    // String message variable to hold the dialog message
    private String mMessage;
    // Reference to the activity that showed this dialog
    private Activity mParent;
    private int propertyToBeUpdatedIndex;

    public static UpdatePropertyDialog newInstance(int indexOfThePropertyToBeUpdated, String title, String message){
        UpdatePropertyDialog fragment = new UpdatePropertyDialog();
        Bundle args = new Bundle();
        args.putString(DIALOG_TITLE,title);
        args.putString(DIALOG_MESSAGE,message);
        args.putInt(DIALOG_DELETING_PROPERTY_INDEX, indexOfThePropertyToBeUpdated);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View add_property_layout = LayoutInflater.from(getActivity()).inflate(R.layout.add_property_details_dialog_layout,null);
        final EditText addressEditText = (EditText) add_property_layout.findViewById(R.id.address);
        final EditText priceEditText =  (EditText) add_property_layout.findViewById(R.id.price);



        mTitle = getArguments().getString(DIALOG_TITLE);
        mMessage = getArguments().getString(DIALOG_MESSAGE);
        mParent = getActivity();
        propertyToBeUpdatedIndex = getArguments().getInt(DIALOG_DELETING_PROPERTY_INDEX);


        addressEditText.setText(((MainActivity) (mParent)).mProperties.get(propertyToBeUpdatedIndex).getProperty_column_address());
        priceEditText.setText(((MainActivity) (mParent)).mProperties.get(propertyToBeUpdatedIndex).getProperty_column_price());
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder
                .setTitle(mTitle)
                .setView(add_property_layout)
                .setMessage(mMessage)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Add new Property here...


                        String address = addressEditText.getText().toString().trim();
                        String price = priceEditText.getText().toString().trim();
                        // Add new Property here...
                        ((MainActivity) (mParent)).onPositiveUpdatePropertyDetails(propertyToBeUpdatedIndex, address, price);
                    }
                })
                .setNegativeButton("Cancel", null);

        return alertDialogBuilder.create();
    }
}
