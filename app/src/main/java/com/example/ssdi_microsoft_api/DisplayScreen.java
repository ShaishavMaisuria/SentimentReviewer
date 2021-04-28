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

//import com.google.gson.JsonArray;
//import com.google.gson.JsonElement;
//import com.google.gson.reflect.TypeToken;
//import com.google.gson.stream.JsonReader;
//import com.monkeylearn.MonkeyLearn;
//import com.monkeylearn.MonkeyLearnResponse;
//import com.monkeylearn.MonkeyLearnException;
//import com.google.gson.Gson;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//



public class DisplayScreen extends Fragment {


    public DisplayScreen() {
        // Required empty public constructor
    }

    private static final String TAG = "DisplayScreen";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    EditText editTextUserInput;
    TextView resultMicroSoftApi;
    TextView textViewmonkeyApi;
    String userInput;
    Boolean monkeyBool=false;
    Boolean microsoftBool=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_screen, container, false);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        getActivity().setTitle("Welcome!!! " + mAuth.getCurrentUser().getDisplayName());
        String id = "123";

        editTextUserInput=view.findViewById(R.id.editTextuserInput);
        textViewmonkeyApi=view.findViewById(R.id.textViewMonkeyApiEachComment);
        resultMicroSoftApi=view.findViewById(R.id.textViewMicrosoftEachComment);

        view.findViewById(R.id.buttonHistory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                if(monkeyBool && microsoftBool)
                {

                    monkeyBool=false;
                    microsoftBool=false;

                    FirbaseClassMonkeyMicrosoftAPIInformation firebaseObject = new FirbaseClassMonkeyMicrosoftAPIInformation(
                            userInput,
                            fullSentenceAnalysisMicrosoft.scoreMicrosoftAPI,
                            fullSentenceAnalysisMicrosoft.sentiment,
                            fullSentenceAnalysisMonkey.tag_name,
                            fullSentenceAnalysisMonkey.confidence);
                    giveToFirebase(firebaseObject);
                    mlistener.displayHistory();
                }else if(editTextUserInput.getText().toString().isEmpty()){
                    mlistener.displayHistory();
                    }else {
                    Toast.makeText(getActivity(),"API Loading",LENGTH_LONG).show();
                }





            }
        });
        view.findViewById(R.id.buttonSubmitQuery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 String userInputValidation=editTextUserInput.getText().toString();
                Log.d(TAG,"userInputValidation   = "+userInputValidation + userInputValidation.isEmpty());
                if(userInputValidation.isEmpty()){
                    Toast.makeText(getActivity(),"Please write something before Submitting", LENGTH_LONG).show();
                    Log.d(TAG,"userInputValidation not satified");
                } else {
                    try {
                        Log.d(TAG,"userInput  satified = "+userInput);
                        Log.d(TAG,"userInputValidation  satified = "+userInputValidation);
                        userInput=userInputValidation;
                        Log.d(TAG,"userInput  satified = "+userInput);
                        textViewmonkeyApi.setText("Waiting For API Response");
                        resultMicroSoftApi.setText("Waiting For API Response");
                        String jsonFormattedMicrosoft = getGsonFormattedData(userInput, id);
                        getSentimentalScore(jsonFormattedMicrosoft);
                        String jsonFormmattedMonkey=getMonkeyUserData(userInput);
                        getMonkey(jsonFormmattedMonkey);
//                        if(userInput.isEmpty()){
//                            Toast.makeText(getActivity(),"Please write something before Submitting", LENGTH_LONG);
//                        } else {

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }
        });


        view.findViewById(R.id.buttonLogOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.logout();
            }
        });


        return view;

    }
    void giveToFirebase(FirbaseClassMonkeyMicrosoftAPIInformation firbaseClassMonkeyMicrosoftAPIInformation){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        db.collection("userComments").document(mAuth.getCurrentUser().getUid()).collection("comments").document().set(firbaseClassMonkeyMicrosoftAPIInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG,"Message Sent");
                if(task.isSuccessful()){
                    Log.d(TAG,"Task Succesfull");
                }else{
                    Log.d(TAG,"Task UnSuccesfull");
                    task.getException().printStackTrace();
                }
            }
        });
    }
private String getGsonFormattedData(String text, String id) {
    String json= "     {\n" +
            "        \"documents\": [\n" +
            "            {\n" +
            "                \"language\": \"en\",\n" +
            "                \"id\": \""+id+"\",\n" +
            "                \"text\": \""+ text +"\"\n"   +
            "            }" +
            "        ]\n" +
            "    }   ";





        Log.d(TAG,"jsonFormated String = "+json);
    return json;
}






    private final OkHttpClient client = new OkHttpClient();
    FullSentenceAnalysisMicrosoft fullSentenceAnalysisMicrosoft;
    void getSentimentalScore(String jsonFormattedString) throws IOException {


        String targetURL= "https://shaishav.cognitiveservices.azure.com/text/analytics/v3.1-preview.4/sentiment";


       MediaType MEDIA_TYPE_MARKDOWN
                = MediaType.parse("application/json; charset=utf-8");

        Request request = new Request.Builder()
                .url("https://shaishav.cognitiveservices.azure.com/text/analytics/v3.1-preview.4/sentiment?opinionMining=true")
                .addHeader("Ocp-Apim-Subscription-Key", "07ad692e599c454881b4401ae74fa44a")
                .addHeader("Content-Type", "application/json;charset=UTF-8")
                .addHeader("Accept", "application/json")
//                .post(formBody)
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN,jsonFormattedString))
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                ResponseBody responseBody = response.body();
                String body = responseBody.string();
                if (response.isSuccessful()) {

                    Log.d(TAG, "OnSucccesful Response" + body.toString());


                    try {
                        JSONObject rootObject = new JSONObject(body);
                        JSONArray documentList= rootObject.getJSONArray("documents");

                        for(int i=0;i<documentList.length();i++){
                            if(i==0) {
                                 fullSentenceAnalysisMicrosoft = new FullSentenceAnalysisMicrosoft(documentList.getJSONObject(0));
                                Log.d(TAG,"json object"+fullSentenceAnalysisMicrosoft.toString());
                                resultMicroSoftApi.setText(fullSentenceAnalysisMicrosoft.toString());
                                microsoftBool=true;


                                }
                            }
                        }
                     catch (JSONException e) {
                        e.printStackTrace();
                    }



                } else {

                    Log.d(TAG, "OnSucccesful Fialure repsponse" + body.toString());
                }
            }
        });


    }



    //    This is monkey api code
    private String getMonkeyUserData(String userData){
//String input="I hate this and that";

        String monkeyJson ="     {\n" +
                "       \"data\": ["+"\n"  +
                "       \""+userData   +"\""+"\n" +
                "       ]"+"\n" +
                "   }";
        return monkeyJson;
    }
    FullSentenceAnalysisMonkey fullSentenceAnalysisMonkey;
void getMonkey(String jsonFormattedData){



Log.d("Monkey","MonkeyDAta"+jsonFormattedData);
    MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("application/json; charset=utf-8");

    Request request = new Request.Builder()
            .url("https://api.monkeylearn.com/v3/classifiers/cl_pi3C7JiL/classify/")
            .addHeader("Authorization", "Token a50f4f1be10935633e797a4c3d0cb8d092b0f245")
            .addHeader("Content-Type", "application/json")
            .addHeader("modelId","cl_pi3C7JiL")
            .addHeader("Accept", "application/json")
//                .post(formBody)
            .post(RequestBody.create(MEDIA_TYPE_MARKDOWN,jsonFormattedData))
            .build();


    client.newCall(request).enqueue(new Callback() {
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            e.printStackTrace();
        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            ResponseBody responseBody = response.body();
            String body = responseBody.string();
            if (response.isSuccessful()) {

                Log.d("Monkey", "OnSucccesful Moneky Response" + body.toString());

                try {
                    JSONArray rootArray = new JSONArray(body);
                    Log.d("Monkey","Printing rootArray"+rootArray);
                    fullSentenceAnalysisMonkey= new FullSentenceAnalysisMonkey(rootArray);
                    Log.d("Monkey","fullSentence Anlaysis"+fullSentenceAnalysisMonkey.toString());

                    textViewmonkeyApi.setText(fullSentenceAnalysisMonkey.toString());
                    monkeyBool=true;


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }else{
                Log.d("Monkey", "unSuccessful Monkey Response" + body.toString());
            }

            }
    });

     }



    displayScreenListener mlistener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof displayScreenListener){
            mlistener=(displayScreenListener)context;
        }else{
            throw new RuntimeException(context.toString()+"Must implement displayScreenListener");
        }
    }

    interface displayScreenListener {
        void logout();
        void displayHistory();
    }

}


