package com.example.loginone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    TextView tvRegisterOne, tvNeedHelp;
    EditText etEmailLogin, etPasswordLogin;
    Button btnLogin;
    ProgressBar mProgressBarLogin;

    private static final String TAG = "LoginActivity";

    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvRegisterOne = findViewById(R.id.tvRegistertwo);
        tvNeedHelp = findViewById(R.id.tvNeedOne);
        etEmailLogin = findViewById(R.id.etEmailLogin);
        etPasswordLogin = findViewById(R.id.etPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        mProgressBarLogin = findViewById(R.id.progressBarLogin);

        openRegisterActivity();
        openHelpActivity();
        loginAccount();
        setupFireBaseApp();
        hideDialog();
    }

    private void setupFireBaseApp() {
        mAuthListener =    new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null){

                    //check if email is verified
                    if (user.isEmailVerified()){
                        Log.d(TAG, "onAuthStateChanged: Signed_in: " + user.getUid());
                        Toast.makeText(LoginActivity.this, "Authenticated with: " + user.getEmail(),
                                Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(LoginActivity.this, "Check your Inbox for a verification link",
                                Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }


                }else {
                    Log.d(TAG, "onAuthStateChanged: Signed_out");
                }

            }
        };
    }

    private void loginAccount() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Check if fields are empty
                if (!isEmpty(etEmailLogin.getText().toString()) && !isEmpty(etPasswordLogin.getText().toString())){
                    Log.d(TAG, "onClick: attempting to authenticate");

                    showDialog();

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(etEmailLogin.getText().toString(),
                            etPasswordLogin.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            hideDialog();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                            hideDialog();

                        }
                    });
                }
                else {
                    Toast.makeText(LoginActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                }
                hideSoftKeyboard();
            }
        });

    }

    private void openHelpActivity() {
        tvNeedHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void openRegisterActivity() {
        String text = "Not a member yet? Register";

        SpannableString ss = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.WHITE);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 18 , 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvRegisterOne.setText(ss);
        tvRegisterOne.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private boolean isEmpty(String string){
        return string.equals("");
    }

    private void showDialog(){
        mProgressBarLogin.setVisibility(View.VISIBLE);

    }

    private void hideDialog(){
        if(mProgressBarLogin.getVisibility() == View.VISIBLE){
            mProgressBarLogin.setVisibility(View.INVISIBLE);
        }
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mAuthListener != null){
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }

}
