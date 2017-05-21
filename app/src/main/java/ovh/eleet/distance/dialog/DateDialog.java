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
 * Created by Marek on 21.05.2017.
 */

public class DateDialog extends DialogFragment{

    private EditText etDateFrom;
    private EditText etDateTo;

    public interface DateDialogListener {
        void onDateDialogPositiveClick(DialogFragment dialog, String fromDate, String toDate);
        void onDateDialogNegativeClick(DialogFragment dialog);
    }

    DateDialog.DateDialogListener dateListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            dateListener = (DateDialog.DateDialogListener) context;
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


        View layout=inflater.inflate(R.layout.dialog_date,null);
        builder.setView(layout);

        etDateFrom = (EditText) layout.findViewById(R.id.etDateFrom);
        etDateTo = (EditText) layout.findViewById(R.id.etDateTo);

        builder.setMessage("Enter date range")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dateListener.onDateDialogPositiveClick(DateDialog.this, etDateFrom.getText().toString(), etDateTo.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dateListener.onDateDialogNegativeClick(DateDialog.this);
                    }
                });
        return builder.create();



    }
}
