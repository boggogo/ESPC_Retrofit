package xdesign.georgi.espc_retrofit.UI.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.media.Rating;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.shawnlin.numberpicker.NumberPicker;

import xdesign.georgi.espc_retrofit.R;
import xdesign.georgi.espc_retrofit.UI.MainActivity;
import xdesign.georgi.espc_retrofit.UI.RatingsActivity;

/**
 * Created by georgi on 14/06/16.
 */
public class AddNewPropertyRatingDialog extends DialogFragment {
    private String TAG = AddNewPropertyRatingDialog.class.getSimpleName();
    // Title key value
    private static final String DIALOG_TITLE = "alert_dialog_title";
    // Message key value
    private static final String DIALOG_MESSAGE = "alert_dialog_message";
    // String variable to hold the dialog title
    private String mTitle;
    // String message variable to hold the dialog message
    private String mMessage;
    // keep track of the parent
    private Activity mParent;

    private NumberPicker numberPicker;

    public static AddNewPropertyRatingDialog newInstance(String title, String message){
        AddNewPropertyRatingDialog fragment = new AddNewPropertyRatingDialog();
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
        mParent =  getActivity();


        final View add_property_rating_layout = LayoutInflater.from(getActivity()).inflate(R.layout.add_property_rating_dialog_layout,null);

        numberPicker = (NumberPicker) add_property_rating_layout.findViewById(R.id.number_picker);
        numberPicker.setDividerColor(getResources().getColor(R.color.colorPrimary));

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder
                .setTitle(mTitle)
                .setView(add_property_rating_layout)
                .setMessage(mMessage)
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int selectedValue = numberPicker.getValue() -1;

                        Log.d(TAG,"Selected number: " + selectedValue);

                        ((RatingsActivity) mParent).onPositiveAddPropertyRating(selectedValue);
                    }
                })
                .setNegativeButton("Cancel", null);

        return alertDialogBuilder.create();
    }
}
