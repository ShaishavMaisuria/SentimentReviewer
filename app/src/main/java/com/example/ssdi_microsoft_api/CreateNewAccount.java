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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**t
 * This class is intended to create new accounts and navigate to appropriate pages
 */
public class CreateNewAccount extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "CreateNewAccount";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreateNewAccount() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateNewAccount.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateNewAccount newInstance(String param1, String param2) {
        CreateNewAccount fragment = new CreateNewAccount();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    /* @onCreateView method
     * On CreateView method is used to inflate and produce all the attach xml and entire code interaction is happened over here
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    EditText editTexNewAccountName;
    EditText editTextNewAccountPassword;
    EditText editTextNewAccountEmailAddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_new_account, container, false);
        try {
            getActivity().setTitle("Create New Account");
        } catch (Exception e){
            e.printStackTrace();
        }
        editTexNewAccountName = view.findViewById(R.id.editTexNewAccountName);
        editTextNewAccountPassword = view.findViewById(R.id.editTextNewAccountPassword);
        editTextNewAccountEmailAddress = view.findViewById(R.id.editTextNewAccountEmailAddress);
        // submit the button consist of validation for fields submitted by user
        view.findViewById(R.id.buttonNewAccountSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editTextNewAccountEmailAddress.getText().toString();
                String password = editTextNewAccountPassword.getText().toString();
                String name = editTexNewAccountName.getText().toString();

                // user validation

                if (email.isEmpty()) {
                    Toast.makeText(getActivity(), "Email cant be empty", Toast.LENGTH_LONG).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(getActivity(), "password cant be empty", Toast.LENGTH_LONG).show();
                } else if (name.isEmpty()) {
                    Toast.makeText(getActivity(), "name cant be empty", Toast.LENGTH_LONG).show();
                } else {

                    //new account creation with fireAuthentication
                    createNewAccount(name, email, password);
                }
            }
        });

        // cancel button is clicked and trigger to go back to the login page
        view.findViewById(R.id.buttonNewAccountCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.createNewAccountToLogin();


            }
        });
        return view;
    }

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    /**
     * createNewAccount is used to take user entered fields and provide to firebase Authentication where new user will be created and add to the system with
     * firbase systems.
     * @param name, user name
     * @param email, user email
     * @param password, user password
     */
    void createNewAccount(String name, String email, String password) {
        // calling the instance of firebase authentication in order to create new account
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "create new Account");

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    // adding name to the account cant do authomatically needs to be manual
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build();
                    try {
                        // working on updating the information
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User profile updated.");
                                            Toast.makeText(getActivity(), "successful login", Toast.LENGTH_LONG).show();
                                            mlistener.OnSuccesfulLogin();
                                        } else {
                                            Toast.makeText(getActivity(), "unsuccessful login" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                } else {
                    Log.d(TAG, "Login unSuccesful" + task.getException().getMessage());
                    String errorMessage = task.getException().getMessage();
                    // providing alert in case user authentication fails
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

    NewAccountListener mlistener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof LoginFragment.LoginListener) {
            mlistener = (NewAccountListener) context;
        } else {
            throw new RuntimeException(context.toString() + "Must implement LoginListener");
        }
    }

    interface NewAccountListener {
        void OnSuccesfulLogin();

        void createNewAccountToLogin();
    }

}