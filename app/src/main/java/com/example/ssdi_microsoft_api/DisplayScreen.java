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

import org.jetbrains.annotations.NotNull;

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_screen, container, false);


        String id = "123";
        String text="We love this trail and make the trip every year. I  hate everyone, I hate this, I hate that, I dont like goldie";
        editTextUserInput=view.findViewById(R.id.editTextuserInput);

        resultMicroSoftApi=view.findViewById(R.id.textViewDisplayResult);
        view.findViewById(R.id.buttonSubmitQuery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInput=editTextUserInput.getText().toString();
                if(userInput.isEmpty()){
                    Toast.makeText(getContext(),"Please write something before Submitting",Toast.LENGTH_LONG);
                } else {
                    try {
                        String jsonFormatted = getGsonFormattedData(userInput, id);
                        getSentimentalScore(jsonFormatted);


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
                .addHeader("Ocp-Apim-Subscription-Key", "7bde380b30d64969a615ad381a97f9fd")
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

                    resultMicroSoftApi.setText(body.toString());


                } else {

                    Log.d(TAG, "OnSucccesful Fialure repsponse" + body.toString());
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



}