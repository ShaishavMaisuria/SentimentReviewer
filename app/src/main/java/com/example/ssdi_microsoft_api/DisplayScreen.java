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
import java.nio.charset.StandardCharsets;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
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
//        HttpClient httpclient = HttpClients.createDefault();
//        URI.Builder builder = new URIBuilder("https://westus.api.cognitive.microsoft.com/text/analytics/v2.0/sentiment");
//
//try{
//        URI uri = builder.build();
//        HttpPost request = new HttpPost(uri);
//        request.setHeader("Content-Type", "application/json");
//        request.setHeader("Ocp-Apim-Subscription-Key", "{subscription key}");
//
//
//        // Request body
//        StringEntity reqEntity = new StringEntity("{body}");
//        request.setEntity(reqEntity);
//
//        HttpResponse response = httpclient.execute(request);
//        HttpEntity entity = response.getEntity();
//
//        if (entity != null)
//        {
//            System.out.println(EntityUtils.toString(entity));
//        }
//    }
//        catch (Exception e)
//    {
//        System.out.println(e.getMessage());
//    }

//
//RequestBody formBody = new FormBody.Builder()
//        .add("Ocp-Apim-Subscription-Key","7bde380b30d64969a615ad381a97f9fd")
//        .add("Content-Type","application/json")
//        .add("Accept","application/json")
//        .build();


        String targetURL= "https://shaishav.cognitiveservices.azure.com/text/analytics/v3.1-preview.4/sentiment";
        RequestBody formBody = new FormBody.Builder()
                .add("opinionMining", "true")
                .build();


        Request request = new Request.Builder()
                .url("https://shaishav.cognitiveservices.azure.com/text/analytics/v3.1-preview.4/sentiment")
                .addHeader("Ocp-Apim-Subscription-Key", "7bde380b30d64969a615ad381a97f9fd")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .post(formBody)

                .build();
//        String urlParameters  = "param1=data1&param2=data2&param3=data3";
//        byte[] postData = new byte[0];
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//            postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
//        }
//        int postDataLength = postData.length;
//        String request = targetURL;
//        URL url = new URL( request );
//        HttpURLConnection conn= (HttpURLConnection) url.openConnection();
//
//        conn.setDoOutput(true);
//        conn.setInstanceFollowRedirects(false);
//        conn.setRequestMethod("POST");
//        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//        conn.setRequestProperty("charset", "utf-8");
//        conn.setRequestProperty("Content-Length", Integer.toString(postDataLength ));
//        conn.setUseCaches(false);
//        try(DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
//            wr.write( postData );
//        }


//        String command = "curl -X POST https://shaishav.cognitiveservices.azure.com/text/analytics/v3.1-preview.4/sentiment?opinionMining=true --data foo1=bar1&foo2=bar2";
//        Process process = Runtime.getRuntime().exec(command);
//        Log.d(TAG, "" + process);
//        process.getInputStream();


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