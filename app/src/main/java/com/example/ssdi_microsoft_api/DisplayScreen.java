package com.example.ssdi_microsoft_api;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.Buffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    EditText editTextUserInput;
    TextView resultMicroSoftApi;
    TextView textViewmonkeyApi;
    String userInput;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_screen, container, false);


        String id = "123";
        String text="We love this trail and make the trip every year. I  hate everyone, I hate this, I hate that, I dont like goldie";
        editTextUserInput=view.findViewById(R.id.editTextuserInput);
        textViewmonkeyApi=view.findViewById(R.id.textViewMonkeyApi);
        resultMicroSoftApi=view.findViewById(R.id.textViewDisplayResult);
        view.findViewById(R.id.buttonSubmitQuery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 userInput=editTextUserInput.getText().toString();
                if(userInput.isEmpty()){
                    Toast.makeText(getContext(),"Please write something before Submitting",Toast.LENGTH_LONG);
                } else {
                    try {
                        String jsonFormattedMicrosoft = getGsonFormattedData(userInput, id);
                        getSentimentalScore(jsonFormattedMicrosoft);
                        String jsonFormmattedMonkey=getMonkeyUserData(userInput);
                        getMonkey(jsonFormmattedMonkey);

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





    String TAG = "sentimentalScore";
    private final OkHttpClient client = new OkHttpClient();

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
                                FullSentenceAnalysisMicrosoft fullSentenceAnalysisMicrosoft = new FullSentenceAnalysisMicrosoft(documentList.getJSONObject(0));
                                Log.d(TAG,"json object"+fullSentenceAnalysisMicrosoft.toString());
                                resultMicroSoftApi.setText(fullSentenceAnalysisMicrosoft.toString());

                                giveToFirebase(fullSentenceAnalysisMicrosoft);

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

void giveToFirebase(FullSentenceAnalysisMicrosoft fullSentenceAnalysisMicrosoftObject){
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    db.collection("userComments").document(mAuth.getCurrentUser().getUid()).set(fullSentenceAnalysisMicrosoftObject).addOnCompleteListener(new OnCompleteListener<Void>() {
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
    }

    private String getMonkeyUserData(String userData){
//String input="I hate this and that";

        String monkeyJson ="     {\n" +
                "       \"data\": ["+"\n"  +
                "       \""+userData   +"\""+"\n" +
                "       ]"+"\n" +
                "   }";
        return monkeyJson;
    }
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
                    FullSentenceAnalysisMonkey fullSentenceAnalysisMonkey= new FullSentenceAnalysisMonkey(rootArray);
                    Log.d("Monkey","fullSentence Anlaysis"+fullSentenceAnalysisMonkey.toString());

                    textViewmonkeyApi.setText(fullSentenceAnalysisMonkey.toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }else{
                Log.d("Monkey", "unSuccessful Monkey Response" + body.toString());
            }

            }
    });




        }
    }


