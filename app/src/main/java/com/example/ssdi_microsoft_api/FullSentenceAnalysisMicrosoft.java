package com.example.ssdi_microsoft_api;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.perfmark.Tag;
/* FullSentenceAnalysisMonkey
 * The purpose of this class is to create object,parse data for Microsft API, and get and set all the fields of the class
 */
public class FullSentenceAnalysisMicrosoft {

    private static final String TAG = "FullSentenceAnalysisMicrosoft";
    String id, sentiment, confidenceScores;
    ArrayList<Double> scoreMicrosoftAPI = new ArrayList<>();
    Double positive, neutral, negative;

    public FullSentenceAnalysisMicrosoft() {
    }
    /* @FullSentenceAnalysisMicrosoft
     * This method is used to parse the json data sent from Microsoft API
     * we use this constructor to populate all the fields for object creation
     */
    @SuppressLint("LongLogTag")
    public FullSentenceAnalysisMicrosoft(JSONObject body) throws JSONException {
        this.id = body.getString("id");
        this.sentiment = body.getString("sentiment");
        this.positive = body.getJSONObject("confidenceScores").getDouble("positive");
        this.neutral = body.getJSONObject("confidenceScores").getDouble("neutral");
        this.negative = body.getJSONObject("confidenceScores").getDouble("negative");


        JSONObject confidenceScoreObjct = new JSONObject(String.valueOf(body)).getJSONObject("confidenceScores");
        Log.d(TAG, "value object");

        this.positive = confidenceScoreObjct.getDouble("positive");

        scoreMicrosoftAPI.add(this.positive);
        scoreMicrosoftAPI.add(this.negative);
        scoreMicrosoftAPI.add(this.neutral);
    }
    /*@toStringFull the textual representation of object
     *
     */

    public String toStringFull() {
        return "FullSentenceAnalysisMicrosoft{" +
                "id='" + id + '\'' +
                ", sentiment='" + sentiment + '\'' +
                ", confidenceScores='" + confidenceScores + '\'' +
                ", positive=" + positive +
                ", neutral=" + neutral +
                ", negative=" + negative +
                '}';
    }
    /* toString the textual representation of object
     *
     */
    @Override
    public String toString() {
        return

                "sentiment Decision = " + sentiment + "\n " +

                        "positive Score = " + positive + "\n " +
                        "neutral Score = " + neutral + "\n " +
                        "negative Score = " + negative
                ;
    }
    /*
    setters and getter for all the fields
     */
    public ArrayList<Double> getScoreMicrosoftAPI() {
        return scoreMicrosoftAPI;
    }

    public void setScoreMicrosoftAPI(ArrayList<Double> scoreMicrosoftAPI) {
        this.scoreMicrosoftAPI = scoreMicrosoftAPI;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public String getConfidenceScores() {
        return confidenceScores;
    }

    public void setConfidenceScores(String confidenceScores) {
        this.confidenceScores = confidenceScores;
    }

    public Double getPositive() {
        return positive;
    }

    public void setPositive(Double positive) {
        this.positive = positive;
    }

    public Double getNeutral() {
        return neutral;
    }

    public void setNeutral(Double neutral) {
        this.neutral = neutral;
    }

    public Double getNegative() {
        return negative;
    }

    public void setNegative(Double negative) {
        this.negative = negative;
    }
}
