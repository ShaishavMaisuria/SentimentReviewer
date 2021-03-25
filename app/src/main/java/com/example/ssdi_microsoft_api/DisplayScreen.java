package com.example.ssdi_microsoft_api;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_screen, container, false);

        try {
            getSentimentalScore();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return view;

    }

    String TAG = "sentimentalScore";
    private final OkHttpClient client = new OkHttpClient();

    void getSentimentalScore() throws IOException {


        String targetURL= "https://shaishav.cognitiveservices.azure.com/text/analytics/v3.1-preview.4/sentiment";


       MediaType MEDIA_TYPE_MARKDOWN
                = MediaType.parse("application/json; charset=utf-8");

        Request request = new Request.Builder()
                .url("https://shaishav.cognitiveservices.azure.com/text/analytics/v3.1-preview.4/sentiment?opinionMining=true")
                .addHeader("Ocp-Apim-Subscription-Key", "7bde380b30d64969a615ad381a97f9fd")
                .addHeader("Content-Type", "application/json;charset=UTF-8")
                .addHeader("Accept", "application/json")
//                .post(formBody)
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN,Data.variable))
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

                } else {

                    Log.d(TAG, "OnSucccesful Fialure repsponse" + body.toString());
                }
            }
        });


    }
}