package com.example.loginone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btnLogin;
    TextView tvRegister;

    private MainActivity mContext = MainActivity.this;

    Animation myAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btnLoginOne);
        tvRegister = findViewById(R.id.tvRegisterone);

        OpenLogin();
        OpenRegister();
    }

    private void OpenRegister() {
        String text = "Not a member yet? Register";

        SpannableString ss = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
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
        tvRegister.setText(ss);
        tvRegister.setMovementMethod(LinkMovementMethod.getInstance());

    }

    private void OpenLogin() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, LoginActivity.class);
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(btnLogin, "buttonTransition");
                pairs[1] = new Pair<View, String>(tvRegister, "HelpTransition");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);

                startActivity(intent, options.toBundle());
            }
        });
    }
}
