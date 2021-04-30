package com.example.ssdi_microsoft_api;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.widget.Toast.LENGTH_LONG;
/* DisplayScreem class is used for taking user data, communicating with apis microsoft api and monkey api
* Also interact with firestore to update the user entered information
 */

public class DisplayScreen extends Fragment {


    public DisplayScreen() {
        // Required empty public constructor
    }

    private static final String TAG = "DisplayScreen";
    /* @onCreate method is called on creation of the fragement
     *
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    EditText editTextUserInput;
    TextView resultMicroSoftApi;
    TextView textViewmonkeyApi;
    String userInput;
    Boolean monkeyBool = false;
    Boolean microsoftBool = false;
    Boolean pressToGetScorePressed = false;
    /* @onCreateView method
     * On CreateView method is used to inflate and produce all the attach xml and entire code interaction is happened over here
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_screen, container, false);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        try {
            getActivity().setTitle("Welcome!!! " + mAuth.getCurrentUser().getDisplayName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String id = "123";

        editTextUserInput = view.findViewById(R.id.editTextuserInput);
        textViewmonkeyApi = view.findViewById(R.id.textViewMonkeyApiEachComment);
        resultMicroSoftApi = view.findViewById(R.id.textViewMicrosoftEachComment);
        // find the view, use the view to find button, and create Listener whenever the history button is clicked
        // user will be navigated to other page
        //  also call call firebase method to update the value of user
        view.findViewById(R.id.buttonHistory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (monkeyBool && microsoftBool) {

                    monkeyBool = false;
                    microsoftBool = false;
                    // object creation
                    FirbaseClassMonkeyMicrosoftAPIInformation firebaseObject = new FirbaseClassMonkeyMicrosoftAPIInformation(
                            userInput,
                            fullSentenceAnalysisMicrosoft.scoreMicrosoftAPI,
                            fullSentenceAnalysisMicrosoft.sentiment,
                            fullSentenceAnalysisMonkey.tag_name,
                            fullSentenceAnalysisMonkey.confidence);

                    giveToFirebase(firebaseObject);

                    // triggering new activity signal
                    mlistener.displayHistory();
                    editTextUserInput.setText("");
                    pressToGetScorePressed = false;
                } else if (editTextUserInput.getText().toString().isEmpty()) {
                    mlistener.displayHistory();
                    pressToGetScorePressed = false;
                } else {
                    if (!pressToGetScorePressed) {
                        editTextUserInput.setText("");
                        mlistener.displayHistory();
                    } else {
                        // used for validation
                        Toast.makeText(getActivity(), R.string.ToastAPILoading, LENGTH_LONG).show();
                    }
                }


            }
        });
        view.findViewById(R.id.buttonSubmitQuery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userInputValidation = editTextUserInput.getText().toString();
                Log.d(TAG, "userInputValidation   = " + userInputValidation + userInputValidation.isEmpty());
                if (userInputValidation.isEmpty()) {
                    Toast.makeText(getActivity(), R.string.ToastWriteCommentValidation, LENGTH_LONG).show();
                    Log.d(TAG, "userInputValidation not satified");
                } else {
                    try {
                        Log.d(TAG, "userInput  satified = " + userInput);
                        Log.d(TAG, "userInputValidation  satified = " + userInputValidation);
                        userInput = userInputValidation;
                        Log.d(TAG, "userInput  satified = " + userInput);

                        // setting the ui fields
                        textViewmonkeyApi.setText(R.string.ToastAPILoading);
                        resultMicroSoftApi.setText(R.string.ToastAPILoading);

                        // giving user data to apis in formatted manner as per api request rules
                        String jsonFormattedMicrosoft = getGsonFormattedData(userInput, id);
                        // sending formatted data to apis
                        getSentimentalScore(jsonFormattedMicrosoft);
                        String jsonFormmattedMonkey = getMonkeyUserData(userInput);
                        getMonkey(jsonFormmattedMonkey);

                        // is used to know where pressToGetScorePressed or not
                        pressToGetScorePressed = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // find the view, use the view to find button, and create Listener whenever the Logout button is clicked and trigger to open logout fragment

        view.findViewById(R.id.buttonLogOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.logout();
            }
        });
        return view;
    }
/* @giveToFirebase
* This method is used to provide all obtaine scores to firebase and update based on user.
 */
    void giveToFirebase(FirbaseClassMonkeyMicrosoftAPIInformation firbaseClassMonkeyMicrosoftAPIInformation) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        try {
            db.collection("userComments").document(mAuth.getCurrentUser().getUid()).collection("comments").document().set(firbaseClassMonkeyMicrosoftAPIInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d(TAG, "Message Sent");
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Task Succesfull");
                    } else {
                        Log.d(TAG, "Task UnSuccesfull");
                        task.getException().printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * getGsonFormattedData method is to format the user data to json format as it is requested by api microsoft
     * @param text
     * @param id
     * @return
     */
    private String getGsonFormattedData(String text, String id) {
        String json = "     {\n" +
                "        \"documents\": [\n" +
                "            {\n" +
                "                \"language\": \"en\",\n" +
                "                \"id\": \"" + id + "\",\n" +
                "                \"text\": \"" + text + "\"\n" +
                "            }" +
                "        ]\n" +
                "    }   ";

        Log.d(TAG, "jsonFormated String = " + json);
        return json;
    }


    private final OkHttpClient client = new OkHttpClient();
    FullSentenceAnalysisMicrosoft fullSentenceAnalysisMicrosoft;

    /**
     * getSentimentalScore from microsoft API
     * @param jsonFormattedString
     * @throws IOException
     */
    void getSentimentalScore(String jsonFormattedString) throws IOException {
        MediaType MEDIA_TYPE_MARKDOWN
                = MediaType.parse("application/json; charset=utf-8");

        // creating request object
        Request request = new Request.Builder()
                .url("https://shaishav.cognitiveservices.azure.com/text/analytics/v3.1-preview.4/sentiment?opinionMining=true")
                .addHeader("Ocp-Apim-Subscription-Key", "07ad692e599c454881b4401ae74fa44a")
                .addHeader("Content-Type", "application/json;charset=UTF-8")
                .addHeader("Accept", "application/json")
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, jsonFormattedString))
                .build();

        // forming the client with request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }
        // get the response from the sent request
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                ResponseBody responseBody = response.body();
                try {
                    String body = responseBody.string();
                    if (response.isSuccessful()) {
                        Log.d(TAG, "OnSucccesful Response" + body.toString());
                        try {
                            JSONObject rootObject = new JSONObject(body);
                            JSONArray documentList = rootObject.getJSONArray("documents");

                            for (int i = 0; i < documentList.length(); i++) {
                                if (i == 0) {
                                    // creation of object
                                    fullSentenceAnalysisMicrosoft = new FullSentenceAnalysisMicrosoft(documentList.getJSONObject(0));
                                    Log.d(TAG, "json object" + fullSentenceAnalysisMicrosoft.toString());
                                    resultMicroSoftApi.setText(fullSentenceAnalysisMicrosoft.toString());
                                    microsoftBool = true;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {

                        Log.d(TAG, "OnSucccesful Fialure repsponse" + body.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * getMonkeyUserData method is used to format the user data to feed the monkey API
     * @param userData
     * @return
     */
    private String getMonkeyUserData(String userData) {
        String monkeyJson = "     {\n" +
                "       \"data\": [" + "\n" +
                "       \"" + userData + "\"" + "\n" +
                "       ]" + "\n" +
                "   }";
        return monkeyJson;
    }

    FullSentenceAnalysisMonkey fullSentenceAnalysisMonkey;

    /**
     * getMonkey is used to interact with monkey API and therefore helping send request and get response
     * @param jsonFormattedData
     */
    void getMonkey(String jsonFormattedData) {
        Log.d("Monkey", "MonkeyDAta" + jsonFormattedData);
        MediaType MEDIA_TYPE_MARKDOWN
                = MediaType.parse("application/json; charset=utf-8");
// creating request object
        Request request = new Request.Builder()
                .url("https://api.monkeylearn.com/v3/classifiers/cl_pi3C7JiL/classify/")
                .addHeader("Authorization", "Token a50f4f1be10935633e797a4c3d0cb8d092b0f245")
                .addHeader("Content-Type", "application/json")
                .addHeader("modelId", "cl_pi3C7JiL")
                .addHeader("Accept", "application/json")
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, jsonFormattedData))
                .build();

        // forming the client with request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }
            // get the response from the sent request
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                ResponseBody responseBody = response.body();
                try {
                    String body = responseBody.string();

                    if (response.isSuccessful()) {

                        Log.d("Monkey", "OnSucccesful Moneky Response" + body.toString());

                        try {
                            JSONArray rootArray = new JSONArray(body);
                            Log.d("Monkey", "Printing rootArray" + rootArray);
                            // creation of object
                            fullSentenceAnalysisMonkey = new FullSentenceAnalysisMonkey(rootArray);
                            Log.d("Monkey", "fullSentence Anlaysis" + fullSentenceAnalysisMonkey.toString());

                            textViewmonkeyApi.setText(fullSentenceAnalysisMonkey.toString());
                            monkeyBool = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.d("Monkey", "unSuccessful Monkey Response" + body.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    displayScreenListener mlistener;
    /* @onAttach
     * we use this method to connect interface with mainactivity
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof displayScreenListener) {
            mlistener = (displayScreenListener) context;
        } else {
            throw new RuntimeException(context.toString() + "Must implement displayScreenListener");
        }
    }
    /* @displayScreenListener
     *  We use this method to interact with main activity to send the data and pass the data around
     */
    interface displayScreenListener {
        void logout();

        void displayHistory();
    }

}


