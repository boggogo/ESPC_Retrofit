package xdesign.georgi.espc_retrofit.UI.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.shawnlin.numberpicker.NumberPicker;

import xdesign.georgi.espc_retrofit.R;
import xdesign.georgi.espc_retrofit.UI.RatingsActivity;

/**
 * Created by georgi on 17/06/16.
 */
public class UpdatePropertyRatingDialog extends DialogFragment {
    private String TAG = UpdatePropertyRatingDialog.class.getSimpleName();
    // Title key value
    private static final String DIALOG_TITLE = "alert_dialog_title";
    // Message key value
    private static final String DIALOG_MESSAGE = "alert_dialog_message";
    // Parent key
    private static final String DIALOG_PARENT = "dialog_parent";
    // Dialog delete property
    private static final String DIALOG_UPDATING_PROPERTY_RATING_INDEX = "property_to_be_deleted";
    // String variable to hold the dialog title
    private String mTitle;
    // String message variable to hold the dialog message
    private String mMessage;
    // Reference to the activity that showed this dialog
    private Activity mParent;

    private int propertyToBeUpdatedIndex;

    private NumberPicker numberPicker;


    public static UpdatePropertyRatingDialog newInstance(int indexOfThePropertyToBeUpdated, String title, String message) {
       UpdatePropertyRatingDialog dialog = new UpdatePropertyRatingDialog();
        Bundle args = new Bundle();
        args.putString(DIALOG_TITLE, title);
        args.putString(DIALOG_MESSAGE, message);
        args.putInt(DIALOG_UPDATING_PROPERTY_RATING_INDEX, indexOfThePropertyToBeUpdated);

        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mTitle = getArguments().getString(DIALOG_TITLE);
        mMessage = getArguments().getString(DIALOG_MESSAGE);
        mParent = getActivity();
        propertyToBeUpdatedIndex = getArguments().getInt(DIALOG_UPDATING_PROPERTY_RATING_INDEX);

        final View add_property_rating_layout = LayoutInflater.from(getActivity()).inflate(R.layout.add_property_rating_dialog_layout,null);

        numberPicker = (NumberPicker) add_property_rating_layout.findViewById(R.id.number_picker);
        numberPicker.setDividerColor(getResources().getColor(R.color.colorPrimary));

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder
                .setTitle(mTitle)
                .setView(add_property_rating_layout)
                .setMessage(mMessage)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int selectedValue = numberPicker.getValue();

                        Log.d(TAG,"Selected number: " + selectedValue);

                        ((RatingsActivity) mParent).onPositiveUpdatePropRating(selectedValue, propertyToBeUpdatedIndex);
                    }
                })
                .setNegativeButton("Cancel", null);

        return alertDialogBuilder.create();

    }
}
