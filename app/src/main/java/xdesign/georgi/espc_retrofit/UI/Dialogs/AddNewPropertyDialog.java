package xdesign.georgi.espc_retrofit.UI.Dialogs;

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
 * Created by georgi on 14/06/16.
 */
public class AddNewPropertyDialog extends DialogFragment {
    private String TAG = AddNewPropertyDialog.class.getSimpleName().toString();
    // Title key value
    private static final String DIALOG_TITLE = "alert_dialog_title";
    // Message key value
    private static final String DIALOG_MESSAGE = "alert_dialog_message";
    // String variable to hold the dialog title
    private String mTitle;
    // String message variable to hold the dialog message
    private String mMessage;

    public static AddNewPropertyDialog newInstance(String title, String message){
        AddNewPropertyDialog fragment = new AddNewPropertyDialog();
        Bundle args = new Bundle();
        args.putString(DIALOG_TITLE,title);
        args.putString(DIALOG_MESSAGE,message);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mTitle = getArguments().getString(DIALOG_TITLE);
        mMessage = getArguments().getString(DIALOG_MESSAGE);

        final View add_property_layout = LayoutInflater.from(getActivity()).inflate(R.layout.add_property_details_dialog_layout,null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder
                .setTitle(mTitle)
                .setView(add_property_layout)
                .setMessage(mMessage)
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Add new Property here...
                        EditText addressEditText = (EditText) add_property_layout.findViewById(R.id.address);
                        EditText priceEditText =  (EditText) add_property_layout.findViewById(R.id.price);

                        String address = addressEditText.getText().toString().trim();
                        String price = priceEditText.getText().toString().trim();

                        MainActivity.onPositiveAddNewProperty(address,price, getActivity());
                    }
                })
                .setNegativeButton("Cancel", null);

        return alertDialogBuilder.create();
    }
}
