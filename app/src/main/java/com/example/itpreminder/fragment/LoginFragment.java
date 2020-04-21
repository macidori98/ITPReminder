package com.example.itpreminder.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.itpreminder.R;
import com.example.itpreminder.model.User;
import com.example.itpreminder.utils.Constant;
import com.example.itpreminder.utils.FragmentNavigation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends Fragment {
    public static final String TAG = LoginFragment.class.getSimpleName();

    private View view;
    private EditText etName;
    private Button btnLogin;
    private CheckBox cbAutoLogin, cbRememberMe;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        initializeElements(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isRememberMeChecked();
        isAutoLoginChecked();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTextLengthOk(etName.getText().toString())){
                    saveDataSharedPreferences();
                    doesUserExists(etName.getText().toString());
                } else {
                    Toast.makeText(getContext(), R.string.name_fail, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isTextLengthOk(String string){
        if (string.length() >= 3 ){
            return true;
        }
        else {
            return false;
        }
    }

    private void doesUserExists(final String user){
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean exists = false;

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String sName = Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                    if (sName.equals(user)){
                        exists = true;
                        String ID = Objects.requireNonNull(snapshot.child("id").getValue()).toString();
                        Constant.CURRENT_USER = new User(ID, etName.getText().toString());
                        break;
                    }
                }



                if (exists){
                    login();
                } else {
                    String ID = mRef.push().getKey();
                    assert ID != null;
                    mRef.child(ID).setValue(new User(ID, user));
                    login();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i(TAG, "onCancelled: doesuserexists");
            }
        });
    }

    private void isRememberMeChecked(){
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(Constant.MY_LOGIN_DATA, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(Constant.REMEMBER_ME, false)){
            etName.setText(sharedPreferences.getString(Constant.USERNAME, Constant.EMPTY_STRING));
            cbRememberMe.setChecked(true);
        }
    }

    private void isAutoLoginChecked(){
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(Constant.MY_LOGIN_DATA, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(Constant.AUTO_LOGIN, false)){
            cbAutoLogin.setChecked(true);
            doesUserExists(etName.getText().toString());
        }
    }

    private void saveDataSharedPreferences(){
        SharedPreferences.Editor sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(Constant.MY_LOGIN_DATA, MODE_PRIVATE).edit();
        if (cbRememberMe.isChecked()){
            sharedPreferences.putString(Constant.USERNAME, etName.getText().toString());
            sharedPreferences.putBoolean(Constant.REMEMBER_ME, true);
        } else {
            sharedPreferences.putString(Constant.USERNAME, "");
            sharedPreferences.putBoolean(Constant.REMEMBER_ME, false);
        }

        if (cbAutoLogin.isChecked()){
            sharedPreferences.putBoolean(Constant.AUTO_LOGIN, true);
        } else {
            sharedPreferences.putBoolean(Constant.AUTO_LOGIN, false);
        }

        sharedPreferences.apply();
    }

    private void login(){
        FragmentNavigation.getInstance(getContext()).replaceFragment(new HomeFragment(), R.id.fragment_content);
    }

    private void initializeElements(View view)
    {
        etName = view.findViewById(R.id.editText_login_username);
        btnLogin = view.findViewById(R.id.button_login_login);
        cbAutoLogin = view.findViewById(R.id.checkbox_login_auto_login);
        cbRememberMe = view.findViewById(R.id.checkbox_login_remember_me);
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference(Constant.USERS);
    }
}
