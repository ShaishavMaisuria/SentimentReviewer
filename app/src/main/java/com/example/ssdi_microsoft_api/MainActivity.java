package com.example.ssdi_microsoft_api;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener , CreateNewAccount.NewAccountListener , DisplayScreen.displayScreenListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().add(R.id.rootView ,new LoginFragment()).commit();
//        getSupportFragmentManager().beginTransaction().add(R.id.rootView ,new DisplayScreen()).commit();

    }

    @Override
    public void createNewAccountToLogin() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootView ,new LoginFragment())
                .commit();
    }

    @Override
    public void OnSuccesfulLogin() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootView ,new DisplayScreen())
                .commit();
    }

    @Override
    public void loginToCreateNewAccount() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootView ,new CreateNewAccount())
                .commit();
    }

    @Override
    public void logout() {
        getSupportFragmentManager().beginTransaction().replace(R.id.rootView ,new LoginFragment()).commit();
    }
}