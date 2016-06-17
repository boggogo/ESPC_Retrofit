package xdesign.georgi.espc_retrofit.UI.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import xdesign.georgi.espc_retrofit.Backend.Room;
import xdesign.georgi.espc_retrofit.Backend.UserPropertyRating;
import xdesign.georgi.espc_retrofit.UI.PropertyDetailsActivity;
import xdesign.georgi.espc_retrofit.UI.RatingsActivity;

/**
 * Created by georgi on 17/06/16.
 */
public class ConfDeleteRoomDialog extends DialogFragment {
    private String TAG = ConfDeleteRoomDialog.class.getSimpleName();
    // Title key value
    private static final String DIALOG_TITLE = "alert_dialog_title";
    // Message key value
    private static final String DIALOG_MESSAGE = "alert_dialog_message";
    // Dialog delete property
    private static final String DIALOG_DELETING_ROOM = "room_to_be_deleted";
    // save the room instance that will be deleted
    private Room roomToDelete;

    // String variable to hold the dialog title
    private String mTitle;
    // String message variable to hold the dialog message
    private String mMessage;
    // Reference to the activity that showed this dialog
    private Activity mParent;


    public static ConfDeleteRoomDialog newInstance(Room room, String title, String message){
        ConfDeleteRoomDialog dialog = new ConfDeleteRoomDialog();
        Bundle args = new Bundle();
        args.putString(DIALOG_TITLE, title);
        args.putString(DIALOG_MESSAGE, message);
        args.putSerializable(DIALOG_DELETING_ROOM,room);
        dialog.setArguments(args);

        return dialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mTitle = getArguments().getString(DIALOG_TITLE);
        mMessage = getArguments().getString(DIALOG_MESSAGE);
        mParent = getActivity();
        roomToDelete = (Room) getArguments().getSerializable(DIALOG_DELETING_ROOM);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder
                .setTitle(mTitle)
                .setMessage(mMessage)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Add new Property here...
                        ((PropertyDetailsActivity) (mParent)).onPositiveDeleteRoomById(roomToDelete);
                    }
                })
                .setNegativeButton("Cancel", null);

        return alertDialogBuilder.create();
    }
}
