package com.example.ssdi_microsoft_api;

import java.util.ArrayList;
import java.util.HashMap;

public class FirbaseClassMonkeyMicrosoftAPIInformation {

    String userSentence;
    String commentID;

    ArrayList<Double> microSoftApiArrayPosNegNet=new ArrayList<>();
    String microsoftAPISentimentalDecision;
    String monkeyAPISentimentalDecision;
    String monkeyAPIScore;

    public String getCommentID() {
        return commentID;
    }

    @Override
    public String toString() {
        return "FirbaseClassMonkeyMicrosoftAPIInformation{" +
                "userSentence='" + userSentence + '\'' +
                ", commentID='" + commentID + '\'' +
                ", microSoftApiArrayPosNegNet=" + microSoftApiArrayPosNegNet +
                ", microsoftAPISentimentalDecision='" + microsoftAPISentimentalDecision + '\'' +
                ", monkeyAPISentimentalDecision='" + monkeyAPISentimentalDecision + '\'' +
                ", monkeyAPIScore='" + monkeyAPIScore + '\'' +
                '}';
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public FirbaseClassMonkeyMicrosoftAPIInformation() {
    }

    public FirbaseClassMonkeyMicrosoftAPIInformation(String userSentence, ArrayList<Double> microSoftApiArrayPosNegNet, String microsoftAPISentimentalDecision, String monkeyAPISentimentalDecision, String monkeyAPIScore) {
        this.userSentence = userSentence;
        this.microSoftApiArrayPosNegNet = microSoftApiArrayPosNegNet;
        this.microsoftAPISentimentalDecision = microsoftAPISentimentalDecision;
        this.monkeyAPISentimentalDecision = monkeyAPISentimentalDecision;
        this.monkeyAPIScore = monkeyAPIScore;
    }

    public String getMicrosoftAPISentimentalDecision() {
        return microsoftAPISentimentalDecision;
    }

    public void setMicrosoftAPISentimentalDecision(String microsoftAPISentimentalDecision) {
        this.microsoftAPISentimentalDecision = microsoftAPISentimentalDecision;
    }

    public String getMonkeyAPISentimentalDecision() {
        return monkeyAPISentimentalDecision;
    }

    public void setMonkeyAPISentimentalDecision(String monkeyAPISentimentalDecision) {
        this.monkeyAPISentimentalDecision = monkeyAPISentimentalDecision;
    }

    public String getMonkeyAPIScore() {
        return monkeyAPIScore;
    }

    public void setMonkeyAPIScore(String monkeyAPIScore) {
        this.monkeyAPIScore = monkeyAPIScore;
    }

    public String getUserSentence() {
        return userSentence;
    }

    public void setUserSentence(String userSentence) {
        this.userSentence = userSentence;
    }


    public ArrayList<Double> getMicroSoftApiArrayPosNegNet() {
        return microSoftApiArrayPosNegNet;
    }

    public void setMicroSoftApiArrayPosNegNet(ArrayList<Double> microSoftApiArrayPosNegNet) {
        this.microSoftApiArrayPosNegNet = microSoftApiArrayPosNegNet;
    }

}
