package ovh.eleet.distance.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import java.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Locale;

import ovh.eleet.distance.R;

/**
 * Created by Marek on 21.05.2017.
 */

public class DateDialog extends DialogFragment {

    private EditText etDateFrom;
    private EditText etTimeFrom;
    private EditText etDateTo;
    private EditText etTimeTo;

    private static EditText editTextToUpdate = null;

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(true);
        }

    };


    TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            myCalendar.set(Calendar.MINUTE, minute);
            updateLabel(false);

        }
    };

    private void updateLabel(boolean isThisDate) {

        String dateFormat = "yyyy-MM-dd";
        String timeFormat = "HH:mm";
        SimpleDateFormat sdf;

        if (isThisDate)
            sdf = new SimpleDateFormat(dateFormat);
        else
            sdf = new SimpleDateFormat(timeFormat);

        editTextToUpdate.setText(sdf.format(myCalendar.getTime()));
    }



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
        etTimeFrom = (EditText) layout.findViewById(R.id.etTimeFrom);
        etTimeTo = (EditText) layout.findViewById(R.id.etTimeTo);

        etDateFrom.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                editTextToUpdate = etDateFrom;
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        etDateTo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                editTextToUpdate = etDateTo;
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        etTimeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextToUpdate = etTimeFrom;
                new TimePickerDialog(getContext(), time, myCalendar.get(Calendar.HOUR),
                        myCalendar.get(Calendar.MINUTE), true).show();
            }
        });

        etTimeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextToUpdate = etTimeTo;
                new TimePickerDialog(getContext(), time, myCalendar.get(Calendar.HOUR),
                        myCalendar.get(Calendar.MINUTE), true).show();
            }
        });

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
