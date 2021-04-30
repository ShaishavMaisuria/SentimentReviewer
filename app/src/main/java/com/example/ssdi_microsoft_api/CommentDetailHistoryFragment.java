package com.example.ssdi_microsoft_api;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

/** This is class is used to show the each comment history details and help user know what is the scor obtained from apis in past
 * all information is stored in firestore and therefore reterival of the data from firestore and displaying to the user screen
 *
 */
public class CommentDetailHistoryFragment extends Fragment {


    private static final String ARG_PARAM1 = "CommentDetailHistoryFragment";
    private static final String TAG = "CommentDetailHistoryFragment";


    private String mcommentID;


    public CommentDetailHistoryFragment() {
        // Required empty public constructor
    }
/**
 * This method is used to get the comment ID
 * and populate to the fragment display
 */

    public static CommentDetailHistoryFragment newInstance(String param1) {
        CommentDetailHistoryFragment fragment = new CommentDetailHistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }

    /* @onCreate method is called on creation of the fragement
     *
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mcommentID = getArguments().getString(ARG_PARAM1);

        }
    }

    TextView usercommentTextView;
    TextView microsoftApiTextView;
    TextView monkeyAPITextView;
    /* @onCreateView method
     * On CreateView method is used to inflate and produce all the attach xml and entire code interaction is happened over here
     */
    @SuppressLint("LongLogTag")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_commonet_detail_history, container, false);
        Log.d(TAG, "comment ID " + mcommentID);

        try {
            getActivity().setTitle("Selected History Comment");
        } catch (Exception e) {
            e.printStackTrace();
        }
        usercommentTextView = view.findViewById(R.id.textViewEachUserComment);
        microsoftApiTextView = view.findViewById(R.id.textViewMicrosoftEachComment);
        monkeyAPITextView = view.findViewById(R.id.textViewMonkeyApiEachComment);
        getfirebaseComment();
        return view;
    }

    /**
     * This method is used to send request and obtain the user comments of particular selected id. The firebase will than reponse back with request comment information
     * based on the id
     */
    void getfirebaseComment() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        try {
            db.collection("userComments").document(mAuth.getCurrentUser().getUid()).collection("comments").document(mcommentID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            //object Creation
                            FirbaseClassMonkeyMicrosoftAPIInformation commentObject = document.toObject(FirbaseClassMonkeyMicrosoftAPIInformation.class);
                            Log.d(TAG, "commentObject data: " + commentObject.toString());
                            usercommentTextView.setText(commentObject.userSentence);
                            // creating the display
                            String microsoftAPIText = "Sentimental Decision = " + commentObject.microsoftAPISentimentalDecision + "Positive = " + commentObject.microSoftApiArrayPosNegNet.get(0) + "\n Negative = " +
                                    commentObject.microSoftApiArrayPosNegNet.get(1) + "\n Neutral = " +
                                    commentObject.microSoftApiArrayPosNegNet.get(2);
                            String monkeyAPIText = "Sentimental Decision = " + commentObject.userSentence +
                                    "\n Sentimental Score = " + commentObject.monkeyAPIScore;
                            microsoftApiTextView.setText(microsoftAPIText);
                            monkeyAPITextView.setText(monkeyAPIText);
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}