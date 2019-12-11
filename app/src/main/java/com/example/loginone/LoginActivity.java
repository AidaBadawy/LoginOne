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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    TextView tvRegisterOne, tvNeedHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvRegisterOne = findViewById(R.id.tvRegistertwo);
        tvNeedHelp = findViewById(R.id.tvNeedOne);

        openRegisterActivity();
        openHelpActivity();
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
                Toast.makeText(LoginActivity.this, "Clicked Register", Toast.LENGTH_SHORT).show();
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
}
