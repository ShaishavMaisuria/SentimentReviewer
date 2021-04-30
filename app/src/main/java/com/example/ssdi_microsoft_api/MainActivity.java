package com.example.ssdi_microsoft_api;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/* MainActivity class
 *  This class is used to connect all different fragments with each other using implemetation of interfaces of each fragments
 * Throgh out the project we  have also implement log to test and check the data passing around methods
 * we have added all possible places an Exception that could occur
 * We have also labelled the required methods that needs description
 *
 * TWO THINGS to KEEP in mind
 * 1) APIs KEYS may expire please account me @smaisuri@uncc.edu for the keys of the project APIs
 * 2) FIREBASE services might stop after 30 days, please contact me @smaisuri@uncc.edu to renew the service ( unfortunately we cannot renew in between)
 *
 *  Other important specification, The app is recommended to run on NEXUS 5X API 29 for performance measure
 *
 * How to run the project please take a look at the github
 * https://github.com/ShaishavMaisuria/SSDI_Microsoft_API
 */
public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener, DisplayHistory.DisplayHistoryListener, CreateNewAccount.NewAccountListener, DisplayScreen.displayScreenListener {
    /* @onCreate method is the main method run whenever the app is started it passess the view from activtiy to activity loginFragment
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().add(R.id.rootView, new LoginFragment()).commit();

    }

    /* @createNewAccountToLogin
     *  passing the fragement from CreateNewAccount fragment to Login Fragment
     */
    @Override
    public void createNewAccountToLogin() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootView, new LoginFragment())
                .commit();
    }

    /* @OnSuccesfulLogin
     * passing the fragement from Login fragment to Display Screen Fragment
     */
    @Override
    public void OnSuccesfulLogin() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootView, new DisplayScreen())
                .commit();
    }

    /* @loginToCreateNewAccount
     * passing the fragement from Login Fragment to CreateNewAccount
     */
    @Override
    public void loginToCreateNewAccount() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootView, new CreateNewAccount())
                .commit();
    }

    /* @logout
     *  passing the fragement from display screen to Login Fragment
     */
    @Override
    public void logout() {
        getSupportFragmentManager().beginTransaction().replace(R.id.rootView, new LoginFragment()).commit();
    }

    /* @displayHistory
     *  passing the fragement from  display screen Fragment to displayHistory fragment
     */
    @Override
    public void displayHistory() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootView, new DisplayHistory())
                .addToBackStack(null)
                .commit();
    }

    /* @displayhistoryCommentDetailHistory
     * passing the fragement from  displayHistory Fragment to CommentDetailHistoryFragment fragment
     */
    @Override
    public void displayhistoryCommentDetailHistory(String commentID) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootView, CommentDetailHistoryFragment.newInstance(commentID))
                .addToBackStack(null)
                .commit();
    }
}