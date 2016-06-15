package xdesign.georgi.espc_retrofit.UI.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import xdesign.georgi.espc_retrofit.Backend.Property;
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
    private static final String DIALOG_DELETING_PROPERTY = "property_to_be_deleted";
    // String variable to hold the dialog title
    private String mTitle;
    // String message variable to hold the dialog message
    private String mMessage;
    // Reference to the activity that showed this dialog
    private Activity mParent;
    private Property propertyToBeUpdated;

    public static UpdatePropertyDialog newInstance(Activity parent, Property property, String title, String message){
        UpdatePropertyDialog fragment = new UpdatePropertyDialog();
        Bundle args = new Bundle();
        args.putString(DIALOG_TITLE,title);
        args.putString(DIALOG_MESSAGE,message);
        args.putSerializable(DIALOG_PARENT, (MainActivity)parent);
        args.putSerializable(DIALOG_DELETING_PROPERTY, property);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mTitle = getArguments().getString(DIALOG_TITLE);
        mMessage = getArguments().getString(DIALOG_MESSAGE);
        mParent = (MainActivity) getArguments().getSerializable(DIALOG_PARENT);
        propertyToBeUpdated = (Property) getArguments().getSerializable(DIALOG_DELETING_PROPERTY);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder
                .setTitle(mTitle)
                .setMessage(mMessage)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Add new Property here...
                        ((MainActivity) (mParent)).onPositiveDeletePropertyById(propertyToBeUpdated);
                    }
                })
                .setNegativeButton("Cancel", null);

        return alertDialogBuilder.create();
    }
}
