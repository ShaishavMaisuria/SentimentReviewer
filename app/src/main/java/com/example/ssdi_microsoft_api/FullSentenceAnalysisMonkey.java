package com.example.ssdi_microsoft_api;

import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FullSentenceAnalysisMonkey {
    String tag_name,confidence;

    public FullSentenceAnalysisMonkey() {
    }
    public FullSentenceAnalysisMonkey(JSONArray rootArray) throws JSONException {

        JSONObject js = rootArray.getJSONObject(0);
        Log.d("Monkey"," js Printing RootObject"+js);
        JSONArray jsArray= js.getJSONArray("classifications");
        Log.d("Monkey","jsArray Printing RootObject"+jsArray);
        JSONObject jsArrayObject= jsArray.getJSONObject(0);
        Log.d("Monkey","jsArrayObject Printing RootObject "+jsArrayObject);
        String tagnam=jsArrayObject.getString("tag_name");
        Log.d("Monkey","tag_name Printing RootObject "+tagnam);
        String confidenc=jsArrayObject.getString("confidence");
        Log.d("Monkey","Printing confidence "+confidenc);

        this.confidence=confidenc;
        this.tag_name=tagnam;
    }

    @Override
    public String toString() {
        return
                "tag_name= " + tag_name + "\n" +
                "confidence= " + confidence;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public String getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }
    //  OnSucccesful Moneky Response[{"text":"I hate this and that","external_id":null
//  ,"error":false,"classifications":[{"tag_name":"Negative","tag_id":122921385,"confidence":0.991}]}]
//    JSONArray root = res.arrayResult;
//    JSONArray headOne = (JSONArray) root.get(0);
//    JSONArray headTwo = (JSONArray) headOne.get(0);
//        for (Object a : headTwo){
//        JSONObject c =(JSONObject) a;
//        System.out.println(c.get("category_id"));
//    }
//        System.out.println("");






}
