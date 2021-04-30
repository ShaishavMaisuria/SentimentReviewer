package com.example.ssdi_microsoft_api;

import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
/* FullSentenceAnalysisMonkey
* The purpose of this class is to create object,parse data for monkeyAPi, and get and set all the fields of the class
 */
public class FullSentenceAnalysisMonkey {
    String tag_name, confidence;

    public FullSentenceAnalysisMonkey() {
    }
/* @FullSentenceAnalysisMonkey
* This method is used to parse the json data sent from monkey API
* we use this constructor to populate all the fields for object creation
 */
    public FullSentenceAnalysisMonkey(JSONArray rootArray) throws JSONException {

        JSONObject js = rootArray.getJSONObject(0);
        Log.d("Monkey", " js Printing RootObject" + js);
        JSONArray jsArray = js.getJSONArray("classifications");
        Log.d("Monkey", "jsArray Printing RootObject" + jsArray);
        JSONObject jsArrayObject = jsArray.getJSONObject(0);
        Log.d("Monkey", "jsArrayObject Printing RootObject " + jsArrayObject);
        String tagnam = jsArrayObject.getString("tag_name");
        Log.d("Monkey", "tag_name Printing RootObject " + tagnam);
        String confidenc = jsArrayObject.getString("confidence");
        Log.d("Monkey", "Printing confidence " + confidenc);

        this.confidence = confidenc;
        this.tag_name = tagnam;
    }
/*toString the textual representation of object
*
 */
    @Override
    public String toString() {
        return
                "sentiment Decision =  " + tag_name + "\n" +
                        "Score = " + confidence;
    }
/*
setters and getter for all the fields
 */
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

}
