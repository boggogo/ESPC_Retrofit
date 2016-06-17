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
import android.widget.EditText;

import xdesign.georgi.espc_retrofit.R;
import xdesign.georgi.espc_retrofit.UI.PropertyDetailsActivity;

/**
 * Created by georgi on 17/06/16.
 */
public class AddNewRoomDialog extends DialogFragment {
    private static final String TAG = AddNewRoomDialog.class.getSimpleName();
    private static String DIALOG_TITLE = "dialog title";
    private static String DIALOG_MESSAGE = "dialog title";
    private Activity mParent;


    // String variable to hold the dialog title
    private String mTitle;
    // String message variable to hold the dialog message
    private String mMessage;

    public static AddNewRoomDialog newInstance(String title, String message) {
        AddNewRoomDialog dialog = new AddNewRoomDialog();

        Bundle args = new Bundle();

        args.putString(DIALOG_TITLE, title);
        args.putString(DIALOG_MESSAGE, message);

        dialog.setArguments(args);

        return dialog;
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mParent = getActivity();

        mTitle = getArguments().getString(DIALOG_TITLE);
        mMessage = getArguments().getString(DIALOG_MESSAGE);

        final View addNewLayout = LayoutInflater.from(getActivity()).inflate(R.layout.room_dialog_layout,null);

        final EditText roomNameEditText = (EditText) addNewLayout.findViewById(R.id.newRoomName);

        AlertDialog.Builder ab = new AlertDialog.Builder(getActivity())
                .setTitle(mTitle)
                .setMessage(mMessage)
                .setView(addNewLayout)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG,"Adding new room...");

                        ((PropertyDetailsActivity)mParent).onPositiveAddNewRoom(roomNameEditText.getText().toString().trim());
                    }
                })
                .setNegativeButton("Cancel",null);

        return ab.create();
    }
}
