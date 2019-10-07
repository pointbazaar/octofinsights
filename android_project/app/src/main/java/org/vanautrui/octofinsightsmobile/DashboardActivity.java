package org.vanautrui.octofinsightsmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.vanautrui.octofinsightsmobile.leads.LeadsActivity;
import org.vanautrui.octofinsightsmobile.services.OctofinsightsAPIService;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        final DashboardActivity dashboardActivity = this;

        final Button leads_btn= findViewById(R.id.btn_leads);
        leads_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(dashboardActivity, LeadsActivity.class);
                startActivity(intent);
            }
        });

        Button btn_balance = findViewById(R.id.btn_balance);
        try {
            btn_balance.setText(OctofinsightsAPIService.getBalance()+"");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}