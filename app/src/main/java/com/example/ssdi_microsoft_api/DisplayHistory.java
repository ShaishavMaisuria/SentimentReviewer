package com.example.ssdi_microsoft_api;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**   This class is used to display history from firebase firestore databases
 * A simple {@link Fragment} subclass.
 * Use the {@link DisplayHistory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DisplayHistory extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "DisplayHistory";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DisplayHistory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DisplayHistory.
     */
    // TODO: Rename and change types and number of parameters
    public static DisplayHistory newInstance(String param1, String param2) {
        DisplayHistory fragment = new DisplayHistory();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    UserEachCommentRecylerAdapter adapter;
    /* @onCreateView method
     * On CreateView method is used to inflate and produce all the attach xml and entire code interaction is happened over here
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_history, container, false);
        try {
            getActivity().setTitle("History");
        }catch (Exception e){
            e.printStackTrace();
        }
        getFireStoreUserHistory();

        recyclerView = view.findViewById(R.id.recylerViewDisplayHistory);
        adapter = new UserEachCommentRecylerAdapter(userHistoryListData);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        return view;
    }

    ArrayList<FirbaseClassMonkeyMicrosoftAPIInformation> userHistoryListData = new ArrayList<>();

    ArrayList<String> userHistoryCommentsList = new ArrayList<>();

    /**
     * getFireStoreUserHistory This is method is used to interact with firestore
     * to obtain the history of the user and format into right object and create arraylist for showing all the information
     */
    void getFireStoreUserHistory() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        try {


            db.collection("userComments").document(mAuth.getCurrentUser().getUid()).collection("comments").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    userHistoryListData.clear();
                    userHistoryCommentsList.clear();
                    for (QueryDocumentSnapshot document : value) {
                        Log.d(TAG, "FireStore Response" + document.getData());
                        Log.d(TAG, "doucment Comment ID" + document.getId());
                        FirbaseClassMonkeyMicrosoftAPIInformation commentData = document.toObject(FirbaseClassMonkeyMicrosoftAPIInformation.class);
                        commentData.setCommentID(document.getId());
                        userHistoryListData.add(commentData);
                        userHistoryCommentsList.add(commentData.userSentence);

                        Log.d(TAG, "userHistoryListData commentData " + commentData.toString());


                    }
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "userhistoryList " + userHistoryListData.toString());
                    Log.d(TAG, "userHistoryCommentsList " + userHistoryCommentsList.toString());
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    RecyclerView recyclerView;
    LinearLayoutManager mLayoutManager;

    /**
     * This class is used to show the list of all the comments that is get from firebase
     */
    class UserEachCommentRecylerAdapter extends RecyclerView.Adapter<UserEachCommentRecylerAdapter.UserEachCommentRecylerView> {
        ArrayList<FirbaseClassMonkeyMicrosoftAPIInformation> userCommentObject;

        public UserEachCommentRecylerAdapter(ArrayList<FirbaseClassMonkeyMicrosoftAPIInformation> userHistoryListData) {
            this.userCommentObject = userHistoryListData;
        }

        @NonNull
        @Override
        public UserEachCommentRecylerView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_user_each_comment, parent, false);
            UserEachCommentRecylerView userViewHolder = new UserEachCommentRecylerView(view);

            return userViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull UserEachCommentRecylerView holder, int position) {
            FirbaseClassMonkeyMicrosoftAPIInformation comment = userCommentObject.get(position);
            holder.setupNewComments(comment);
            holder.index = position;


        }

        @Override
        public int getItemCount() {
            return userCommentObject.size();
        }

        public class UserEachCommentRecylerView extends RecyclerView.ViewHolder {
            FirbaseClassMonkeyMicrosoftAPIInformation userComment;
            TextView textViewdisplayComment;
            int index;

            public UserEachCommentRecylerView(@NonNull View itemView) {
                super(itemView);
                textViewdisplayComment = itemView.findViewById(R.id.textViewEachComment);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "adapter Listener" + index);
                        Log.d(TAG, "Entire Comment Array Object" + userComment.toString());
                        mlistener.displayhistoryCommentDetailHistory(userComment.commentID);
                    }
                });
            }

            public void setupNewComments(FirbaseClassMonkeyMicrosoftAPIInformation comment) {
                this.userComment = comment;
                textViewdisplayComment.setText(this.userComment.userSentence);

            }
        }


    }

    DisplayHistoryListener mlistener;
    /* @onAttach
     * we use this method to connect interface with mainactivity
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof DisplayHistoryListener) {
            mlistener = (DisplayHistoryListener) context;
        } else {
            throw new RuntimeException(context.toString() + "Must implement DisplayHistoryListener");
        }
    }
    /* @DisplayHistoryListener
     *  We use this method to interact with main activity to send the data and pass the data around
     */
    interface DisplayHistoryListener {
        void displayhistoryCommentDetailHistory(String commentID);

    }
}
