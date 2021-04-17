package com.example.ssdi_microsoft_api;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
EditText editTextNewAccountEmailAddress;
    EditText editTextNewAccountPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        editTextNewAccountEmailAddress = view.findViewById(R.id.editTextNewAccountEmailAddress);
        editTextNewAccountPassword = view.findViewById(R.id.editTextNewAccountPassword);
        getActivity().setTitle("Login");

        editTextNewAccountEmailAddress.setText("t@t.com");
        editTextNewAccountPassword.setText("test123");
        //loginUser()


        view.findViewById(R.id.buttonNewAccountSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email= editTextNewAccountEmailAddress.getText().toString();
                String password= editTextNewAccountPassword.getText().toString();

                if(email.isEmpty()){
                    Toast.makeText(getActivity(),"Email cant be empty",Toast.LENGTH_LONG).show();
                } else if(password.isEmpty()){
                    Toast.makeText(getActivity(),"password cant be empty",Toast.LENGTH_LONG).show();
                }else{
                  loginUser(email,password);

//                    loginUser("a@a.com","test123");
                }
            }
        });

        view.findViewById(R.id.buttonCreateNewAccontLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.loginToCreateNewAccount();
            }
        });

        return view;
    }
    private FirebaseAuth mAuth ;


    void loginUser(String email, String password){
     // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG,"Inside firebase");
                if(task.isSuccessful()){
                    Log.d(TAG,"Login Succesful");
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if(currentUser != null){
                        String uid= currentUser.getUid();
                        Log.d(TAG,"User Id "+uid);

                    }

                        mlistener.OnSuccesfulLogin();

                }else{
                    Log.d(TAG,"Login unSuccesful"+task.getException().getMessage());
                    String errorMessage =task.getException().getMessage();
                    AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());

                    builder.setMessage(errorMessage).setTitle("Error").setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();

                }
            }
        });


    }

    LoginListener mlistener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof LoginListener){
            mlistener=(LoginListener)context;
        }else{
            throw new RuntimeException(context.toString()+"Must implement LoginListener");
        }
    }

    interface LoginListener {
        void OnSuccesfulLogin();
        void loginToCreateNewAccount();
    }
}