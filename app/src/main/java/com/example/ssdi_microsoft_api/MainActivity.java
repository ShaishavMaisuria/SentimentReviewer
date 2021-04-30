package com.example.ssdi_microsoft_api;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/* MainActivity class
 *  This class is used to connect all different fragments with each other using implemetation of interfaces of each fragments
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
     *  passing the fragement from CreateNewAccount to Login Fragment
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