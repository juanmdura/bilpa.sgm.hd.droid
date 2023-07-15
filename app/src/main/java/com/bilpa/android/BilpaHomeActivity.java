package com.bilpa.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;

import com.bilpa.android.utils.SessionStore;

public class BilpaHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView vCardPreventivos;
    private CardView vCardCorrectivos;
    private int bilpaRedN;
    private int bilpaRedP;
    private int bilpaBlueN;
    private int bilpaBlueP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bilpa_home);

        vCardPreventivos = (CardView) findViewById(R.id.vCardPreventivos);
        vCardCorrectivos = (CardView) findViewById(R.id.vCardCorrectivos);
        vCardPreventivos.setOnClickListener(this);
        vCardCorrectivos.setOnClickListener(this);


        bilpaBlueN = getResources().getColor(R.color.bilpa_blue);
        bilpaBlueP = getResources().getColor(R.color.bilpa_blue_highLighted);
        vCardPreventivos.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getActionMasked();
                if (action == MotionEvent.ACTION_DOWN) {
                    ((CardView) v).setCardBackgroundColor(bilpaBlueP);
                } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                    ((CardView) v).setCardBackgroundColor(bilpaBlueN);
                }
                return false;
            }
        });


        bilpaRedN = getResources().getColor(R.color.bilpa_red);
        bilpaRedP = getResources().getColor(R.color.bilpa_red_highLighted);
        vCardCorrectivos.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getActionMasked();
                if (action == MotionEvent.ACTION_DOWN) {
                    ((CardView) v).setCardBackgroundColor(bilpaRedP);
                } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                    ((CardView) v).setCardBackgroundColor(bilpaRedN);
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.vCardPreventivos:
                SessionStore.setDefaultSection(this, 1);
                Intent i1 = new Intent(this, HomeActivity.class);
                startActivity(i1);
                finish();
                break;

            case R.id.vCardCorrectivos:
                SessionStore.setDefaultSection(this, 2);
                Intent i2 = new Intent(this, CorrectivosActivity.class);
                startActivity(i2);
                finish();
                break;

        }
    }
}
