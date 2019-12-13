package com.example.loginone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    EditText etName, etEmail, etPassword, etConfirmPass;
    Button btnRegister;
    TextView tvRegisterTwo;
    ProgressBar mProgressBar;

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPass = findViewById(R.id.etConfirmPass);
        btnRegister = findViewById(R.id.btnRegister);
        tvRegisterTwo = findViewById(R.id.tvRegistertwo);
        mProgressBar = findViewById(R.id.mProgressBar);

        openLoginActivity();
        registerUser();
        hideDialog();
    }

    private void registerUser() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Attempting to Register...");

                //Check for null values
                if (!isEmpty(etName.getText().toString()) &&
                    !isEmpty(etEmail.getText().toString()) &&
                    !isEmpty(etPassword.getText().toString()) &&
                    !isEmpty(etConfirmPass.getText().toString())){

                    //Check if passwords match
                    if (doStringsMatch(etPassword.getText().toString(), etConfirmPass.getText().toString())){
                        registerNewEmail(etEmail.getText().toString().trim(), etPassword.getText().toString().trim());

                    }
                    else {
                        Toast.makeText(RegisterActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        hideSoftKeyboard();
    }


    private void sendVerificationEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null){
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Verification Email sent",
                                        Toast.LENGTH_LONG).show();

                            }else {
                                Toast.makeText(RegisterActivity.this, "Couldn't send verification Email",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void openLoginActivity() {
        String text = "Already have an account? Login";

        SpannableString ss = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);

            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.WHITE);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 25, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvRegisterTwo.setText(ss);
        tvRegisterTwo.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * Return true if the @param is null
     * @param string
     * @return
     */
    private boolean isEmpty(String string){
        return string.equals("");
    }

    /**
     * Return true if @param 's1' matches @param 's2'
     * @param s1
     * @param s2
     * @return
     */
    private boolean doStringsMatch(String s1, String s2){
        return s1.equals(s2);
    }

    private void registerNewEmail(String email, String password){
        showDialog();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "onComplete: onComplete" + task.isSuccessful());

                if (task.isSuccessful()){
                    Log.d(TAG, "onComplete: AuthState" + FirebaseAuth.getInstance().getCurrentUser().getUid());

                    //Send email verification
                    sendVerificationEmail();

                    FirebaseAuth.getInstance().signOut();

                    //Redirect User to login screen
                    redirectLoginScreen();
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Unable to Register", Toast.LENGTH_SHORT).show();
                }
                hideDialog();
            }
        });

    }

    private void redirectLoginScreen(){
        Log.d(TAG, "redirectLoginScreen: redirecting to login screen.");

        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void showDialog(){
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideDialog(){
        if (mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
