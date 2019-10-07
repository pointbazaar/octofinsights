package org.vanautrui.octofinsightsmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.vanautrui.octofinsightsmobile.services.OctofinsightsAPIService;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final LoginActivity login_activity = this;

        final Button login_btn = findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText text_username = findViewById(R.id.text_username);
                EditText text_password= findViewById(R.id.text_password);

                try {
                    OctofinsightsAPIService.retrieve_and_store_access_token(text_username.getText().toString(),text_password.getText().toString());

                    final Intent intent = new Intent(login_activity,DashboardActivity.class);
                    startActivity(intent);

                } catch (Exception e) {
                    TextView error_text = findViewById(R.id.text_error);
                    error_text.setText(e.toString());
                    e.printStackTrace();
                }

            }
        });

    }
}
