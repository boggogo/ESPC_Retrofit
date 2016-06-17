package xdesign.georgi.espc_retrofit.UI.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import xdesign.georgi.espc_retrofit.Backend.UserPropertyRating;
import xdesign.georgi.espc_retrofit.UI.RatingsActivity;

/**
 * Created by georgi on 15/06/16.
 */
public class ConfDelPropRatingDialog extends DialogFragment {
    private String TAG = ConfDelPropRatingDialog.class.getSimpleName();
    // Title key value
    private static final String DIALOG_TITLE = "alert_dialog_title";
    // Message key value
    private static final String DIALOG_MESSAGE = "alert_dialog_message";
    // Dialog delete property
    private static final String DIALOG_DELETING_PROPERTY = "property_rating_to_be_deleted";


    private UserPropertyRating propertyRaitingToDelete;


    // String variable to hold the dialog title
    private String mTitle;
    // String message variable to hold the dialog message
    private String mMessage;
    // Reference to the activity that showed this dialog
    private Activity mParent;

    public static ConfDelPropRatingDialog newInstance(UserPropertyRating userPropertyRating, String title, String message){
        ConfDelPropRatingDialog fragment = new ConfDelPropRatingDialog();
        Bundle args = new Bundle();
        args.putString(DIALOG_TITLE,title);
        args.putString(DIALOG_MESSAGE,message);
        args.putSerializable(DIALOG_DELETING_PROPERTY, userPropertyRating);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mTitle = getArguments().getString(DIALOG_TITLE);
        mMessage = getArguments().getString(DIALOG_MESSAGE);
        mParent = getActivity();
        propertyRaitingToDelete = (UserPropertyRating) getArguments().getSerializable(DIALOG_DELETING_PROPERTY);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder
                .setTitle(mTitle)
                .setMessage(mMessage)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Add new Property here...
                        ((RatingsActivity) (mParent)).onPositiveDeletePropertyRatingById(propertyRaitingToDelete);
                    }
                })
                .setNegativeButton("Cancel", null);

        return alertDialogBuilder.create();
    }
}
