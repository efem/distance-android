package ovh.eleet.distance.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import ovh.eleet.distance.R;

/**
 * Created by Marek on 20.05.2017.
 */

public class DistanceDialog extends DialogFragment {

    private EditText etDistanceFrom;
    private EditText etDistanceTo;

    public interface DistanceDialogListener {
        void onDialogPositiveClick(DialogFragment dialog, String fromDistance, String toDistance);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    DistanceDialogListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (DistanceDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater= getActivity().getLayoutInflater();


        View layout=inflater.inflate(R.layout.dialog_distance,null);
        builder.setView(layout);

        etDistanceFrom = (EditText) layout.findViewById(R.id.etDistanceFrom);
        etDistanceTo = (EditText) layout.findViewById(R.id.etDistanceTo);

        builder.setMessage("Enter distance range")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogPositiveClick(DistanceDialog.this, etDistanceFrom.getText().toString(), etDistanceTo.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(DistanceDialog.this);
                    }
                });
        return builder.create();



    }
}
