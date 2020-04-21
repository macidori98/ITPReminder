package com.example.itpreminder.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.itpreminder.R;
import com.example.itpreminder.activities.MainActivity;
import com.example.itpreminder.interfaces.AddNewCarListener;
import com.example.itpreminder.model.Car;
import com.example.itpreminder.utils.Constant;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class AddCarFragmentDialog extends AppCompatDialogFragment {
    public static final String TAG = AddCarFragmentDialog.class.getSimpleName();

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private EditText etPlateNumber, etPhoneNumber, etCarType, etItpDuration;
    private TextView tvDate;
    private AddNewCarListener listener;
    private DatePickerDialog.OnDateSetListener mDateSetListener;


    public AddCarFragmentDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fragment_add_car, null);
        etCarType = view.findViewById(R.id.editText_car_type);
        etPhoneNumber = view.findViewById(R.id.editText_phone_number);
        etPlateNumber = view.findViewById(R.id.editText_plate_num);
        etItpDuration = view.findViewById(R.id.editText_itp_duration);
        tvDate = view.findViewById(R.id.textView_date);
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "-" + day + "-" + year;
                tvDate.setText(date);
            }
        };
        addNewCar(builder,view);
        return builder.create();
    }

    private void addNewCar(AlertDialog.Builder builder, View view){
        builder.setView(view).setTitle("Add Car").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i(TAG, "onClick: ");
            }
        }).setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabase = FirebaseDatabase.getInstance();
                mRef = mDatabase.getReference(Constant.CARS);

                String id = mRef.push().getKey();
                String plateNumber = etPlateNumber.getText().toString();
                String phoneNumber = etPhoneNumber.getText().toString();
                String carType = etCarType.getText().toString();
                String itpDuration = etItpDuration.getText().toString();
                String doneDate = tvDate.getText().toString();

                SimpleDateFormat sdf = new SimpleDateFormat("d-mm-yyyy");
                Calendar c = Calendar.getInstance();
                try {
                    c.setTime(sdf.parse(doneDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                c.add(Calendar.YEAR, Integer.valueOf(itpDuration));

                String text = sdf.format(c.getTime());

                Car car = new Car(id, phoneNumber, carType, plateNumber, doneDate,
                        sdf.format(c.getTime()),Integer.valueOf(itpDuration), Constant.CURRENT_USER.getId());

                listener.addCar(car);
                mRef.child(id).setValue(car);

                Toast.makeText(getContext(), R.string.added, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private boolean isTextLengthOk(String string){
        if (string.length() >= 1 ){
            return true;
        }
        else {
            return false;
        }
    }

    public void setListener(AddNewCarListener listener){
        this.listener = listener;
    }
}
