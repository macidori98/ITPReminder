package com.example.itpreminder.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itpreminder.R;
import com.example.itpreminder.adapter.CarAdapter;
import com.example.itpreminder.dialogs.AddCarFragmentDialog;
import com.example.itpreminder.interfaces.AddNewCarListener;
import com.example.itpreminder.interfaces.OnItemClickListener;
import com.example.itpreminder.model.Car;
import com.example.itpreminder.utils.Constant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements AddNewCarListener {
    public static final String TAG = HomeFragment.class.getSimpleName();

    private View view;
    private RecyclerView rvLists;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private CarAdapter carAdapter;
    private List<Car> carList;
    private ImageButton imgBtnAddNewCar;
    private TextView tvNoCarAdded;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initializeElements(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getCars();
        imgBtnAddNewCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCar();
            }
        });
    }

    private void addCar() {
        AddCarFragmentDialog addCarFragmentDialog = new AddCarFragmentDialog();
        addCarFragmentDialog.setListener(this);
        addCarFragmentDialog.show(getActivity().getSupportFragmentManager(), "ADD NEW CAR");
    }

    private void getCars() {
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                carList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String addedById = snapshot.child("addedById").getValue().toString();
                    if (addedById.equals(Constant.CURRENT_USER.getId())) {
                        String id = snapshot.child("id").getValue().toString();
                        String phoneNumber = snapshot.child("phoneNumber").getValue().toString();
                        String carType = snapshot.child("carType").getValue().toString();
                        String plateNumber = snapshot.child("plateNumber").getValue().toString();
                        String doneDate = snapshot.child("doneDate").getValue().toString();
                        String expireDate = snapshot.child("expireDate").getValue().toString();
                        int itpPeriod = Integer.valueOf(snapshot.child("itpPeriod").getValue().toString());

                        Car car = new Car(id, phoneNumber, carType, plateNumber, doneDate, expireDate, itpPeriod, addedById);

                        boolean found = false;
                        for (Car c : carList) {
                            if (c.getId().equals(id)) {
                                found = true;
                                break;
                            }
                        }

                        if (!found) {
                            carList.add(car);
                        }
                    }
                }


                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                rvLists.setLayoutManager(linearLayoutManager);
                carAdapter = new CarAdapter(carList, getContext(), getActivity());
                carAdapter.setOnClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Toast.makeText(getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                    }
                });
                if (carList.size() > 0) {
                    tvNoCarAdded.setVisibility(View.GONE);
                }
                rvLists.setAdapter(carAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i(TAG, "onCancelled: ");
            }
        });
    }

    private void initializeElements(View view) {
        rvLists = view.findViewById(R.id.recyclerView_cars);
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference(Constant.CARS);
        carList = new ArrayList<>();
        imgBtnAddNewCar = view.findViewById(R.id.imageButton_addNewCar);
        tvNoCarAdded = view.findViewById(R.id.no_car_added);
    }

    @Override
    public void addCar(Car cas) {
        carList.add(cas);
        carAdapter.notifyItemInserted(carList.size());
    }
}
