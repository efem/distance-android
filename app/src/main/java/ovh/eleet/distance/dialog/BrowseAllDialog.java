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
 * Created by Marek on 29.05.2017.
 */

public class BrowseAllDialog extends DialogFragment {
    private EditText etPageNo;
    private EditText etPageSize;

    public interface BrowseAllDialogListener {
        void onBrowseAllDialogPositiveClick(DialogFragment dialog, String PageNo, String etPageSize);
        void onBrowseAllDialogNegativeClick(DialogFragment dialog);
    }

    BrowseAllDialogListener browseAllDialogListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            browseAllDialogListener = (BrowseAllDialogListener) context;
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


        View layout=inflater.inflate(R.layout.dialog_browse_all,null);
        builder.setView(layout);

        etPageNo = (EditText) layout.findViewById(R.id.etPageNo);
        etPageSize = (EditText) layout.findViewById(R.id.etPageSize);

        builder.setMessage("Enter browsing conditions")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        browseAllDialogListener.onBrowseAllDialogPositiveClick(BrowseAllDialog.this, etPageNo.getText().toString(), etPageSize.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        browseAllDialogListener.onBrowseAllDialogNegativeClick(BrowseAllDialog.this);
                    }
                });
        return builder.create();



    }

}
