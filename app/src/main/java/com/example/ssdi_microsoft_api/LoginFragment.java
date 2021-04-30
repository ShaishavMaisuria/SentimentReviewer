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

    /* @onCreate method is called on creation of the fragement
     *
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    EditText editTextNewAccountEmailAddress;
    EditText editTextNewAccountPassword;

    /* @onCreateView method
     * On CreateView method is used to inflate and produce all the attach xml and entire code interaction is happened over here
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        editTextNewAccountEmailAddress = view.findViewById(R.id.editTextNewAccountEmailAddress);
        editTextNewAccountPassword = view.findViewById(R.id.editTextNewAccountPassword);
        getActivity().setTitle("Login");

        editTextNewAccountEmailAddress.setText("ssdi6156@uncc.com");
        editTextNewAccountPassword.setText("test123");
        // find the view, use the view to find button, and create Listener whenever button is clicked we login using email and password
        view.findViewById(R.id.buttonNewAccountSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editTextNewAccountEmailAddress.getText().toString();
                String password = editTextNewAccountPassword.getText().toString();
                //performing field validation whether is empty or not
                if (email.isEmpty()) {
                    Toast.makeText(getActivity(), "Email cant be empty", Toast.LENGTH_LONG).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(getActivity(), "password cant be empty", Toast.LENGTH_LONG).show();
                } else {
                    loginUser(email, password);
                }
            }
        });
        // find the view, use view to find button, and set listener whenever new account login is clicked
        view.findViewById(R.id.buttonCreateNewAccontLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.loginToCreateNewAccount();
            }
        });

        return view;
    }

    private FirebaseAuth mAuth;

    /* @LoginUser method
     * This method interact with Firbase authentication and send the user inputted username and email to login the app
     */
    void loginUser(String email, String password) {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "Inside firebase");
                if (task.isSuccessful()) {
                    Log.d(TAG, "Login Succesful");
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if (currentUser != null) {
                        String uid = currentUser.getUid();
                        Log.d(TAG, "User Id " + uid);
                    }

                    mlistener.OnSuccesfulLogin();

                } else {
                    // what to do if the login is not successful and show the error reported by firebase
                    Log.d(TAG, "Login unSuccesful" + task.getException().getMessage());
                    String errorMessage = task.getException().getMessage();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

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

    /* @onAttach
     * we use this method to connect interface with mainactivity
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof LoginListener) {
            mlistener = (LoginListener) context;
        } else {
            throw new RuntimeException(context.toString() + "Must implement LoginListener");
        }
    }

    /* @LoginListener
     *  We use this method to interact with main activity to send the data and pass the data around
     */
    interface LoginListener {
        void OnSuccesfulLogin();

        void loginToCreateNewAccount();
    }
}