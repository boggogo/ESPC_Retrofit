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

import xdesign.georgi.espc_retrofit.Backend.Room;
import xdesign.georgi.espc_retrofit.R;
import xdesign.georgi.espc_retrofit.UI.PropertyDetailsActivity;

/**
 * Created by georgi on 17/06/16.
 */
public class UpdateRoomDialog extends DialogFragment {
    private static String DIALOG_TITLE = "dialog  title";
    private static String DIALOG_MESSAGE = "dialog message";
    private static String ROOM_INDEX = "dialog room index";

    private static String ROOM_KEY = "room_to_update";

    private String mTitle;
    private String mMessage;

    private Activity mParent;

    /***
     * @param roomToBeUpdated room to the updated
     * @param title
     * @param message
     * @return
     */

    public static UpdateRoomDialog newInstance(Room roomToBeUpdated, String title, String message) {
        UpdateRoomDialog dialogFragment = new UpdateRoomDialog();
        Bundle args = new Bundle();

        args.putString(DIALOG_TITLE, title);
        args.putString(DIALOG_MESSAGE, message);
        args.putSerializable(ROOM_INDEX, roomToBeUpdated);

        dialogFragment.setArguments(args);

        return dialogFragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Room roomToUpdate;

        mParent = getActivity();
        Bundle args = getArguments();
        roomToUpdate = (Room) args.getSerializable(ROOM_INDEX);

        mTitle = args.getString(DIALOG_TITLE);
        mMessage = args.getString(DIALOG_MESSAGE);


        View room_dialog_layout = LayoutInflater.from(mParent).inflate(R.layout.room_dialog_layout, null);
        final EditText roomNameEditText = (EditText) room_dialog_layout.findViewById(R.id.newRoomName);

        roomNameEditText.setText(roomToUpdate.getRoom_column_name());

        AlertDialog.Builder ab = new AlertDialog.Builder(mParent)
                .setTitle(mTitle)
                .setView(room_dialog_layout)
                .setMessage(mMessage)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((PropertyDetailsActivity)mParent).onPositiveUpdateRoom(roomToUpdate, (roomNameEditText.getText().toString().trim()));
                    }
                })
                .setNegativeButton("Cansel", null);

        return ab.create();


    }
}
