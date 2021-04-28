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
        View view= inflater.inflate(R.layout.fragment_create_new_account, container, false);
        getActivity().setTitle("Create New Account");
         editTexNewAccountName=view.findViewById(R.id.editTexNewAccountName);
         editTextNewAccountPassword=view.findViewById(R.id.editTextNewAccountPassword);
         editTextNewAccountEmailAddress=view.findViewById(R.id.editTextNewAccountEmailAddress);

        view.findViewById(R.id.buttonNewAccountSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email=editTextNewAccountEmailAddress.getText().toString();
                String password=editTextNewAccountPassword.getText().toString();
                String name=editTexNewAccountName.getText().toString();


                if(email.isEmpty()){
                    Toast.makeText(getActivity(),"Email cant be empty",Toast.LENGTH_LONG).show();
                } else if(password.isEmpty()){
                    Toast.makeText(getActivity(),"password cant be empty",Toast.LENGTH_LONG).show();
                }else if(name.isEmpty()){
                    Toast.makeText(getActivity(),"name cant be empty",Toast.LENGTH_LONG).show();
                }else{

                    createNewAccount(name,email,password);
                }
            }
        });

        view.findViewById(R.id.buttonNewAccountCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
mlistener.createNewAccountToLogin();


            }
        });
        return view;
    }
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    void createNewAccount(String name,String email,String password){
//        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG,"create new Account");
//
//                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User profile updated.");
                                            Toast.makeText(getActivity(),"successful login",Toast.LENGTH_LONG).show();
                                            mlistener.OnSuccesfulLogin();
                                    }
                                    else{
                                        Toast.makeText(getActivity(),"unsuccessful login"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                }
                            });


//                    if(currentUser != null){
//                        String uid= currentUser.getUid();
//                        Log.d(TAG,"User Id "+uid);
////                        createNewForumUser(uid,name);
//                    }


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

    void createNewForumUser(String uid,String userName){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("createByName", userName);

        db.collection("forums")
                .document(uid)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG,"createdNew user of name"+userName);
                    }
                });

    }

 NewAccountListener mlistener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof LoginFragment.LoginListener){
            mlistener=(NewAccountListener)context;
        }else{
            throw new RuntimeException(context.toString()+"Must implement LoginListener");
        }
    }

    interface NewAccountListener {
        void OnSuccesfulLogin();
        void createNewAccountToLogin();
    }

}