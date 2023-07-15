package com.bilpa.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.bilpa.android.services.ApiService;
import com.bilpa.android.services.AsyncCallback;
import com.bilpa.android.services.actions.LoginResult;
import com.bilpa.android.utils.MessageUtils;
import com.bilpa.android.utils.SessionStore;
import com.crashlytics.android.Crashlytics;
import com.mautibla.utils.InputUtils;
import com.mautibla.utils.ViewUtils;


public class BilpaActivity extends ActionBarActivity implements View.OnClickListener {

    private ProgressBar vLoginProgress;
    private ScrollView vLoginFrom;
    private LinearLayout vLoginFromIn;
    private EditText vUser;
    private EditText vPass;
    private Button vBtnLogin;

    private boolean mTimeElapsed = false;
    private boolean mDataElapsed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bilpa);

        Crashlytics.setString("BuildType", BuildConfig.BUILD_TYPE);
        Crashlytics.setString("MyVersion", BuildConfig.VERSION_NAME + "(" + BuildConfig.VERSION_CODE + ")");

        vLoginProgress = (ProgressBar) findViewById(R.id.vLoginProgress);
        vLoginFrom = (ScrollView) findViewById(R.id.vLoginFrom);
        vLoginFromIn = (LinearLayout) findViewById(R.id.vLoginFromIn);
        vUser = (EditText) findViewById(R.id.vUser);
        vPass = (EditText) findViewById(R.id.vPass);
        vBtnLogin = (Button) findViewById(R.id.vBtnLogin);

        vBtnLogin.setOnClickListener(this);

        if (!SessionStore.isLogged(this)) {
            ViewUtils.visible(vLoginFrom);
            ViewUtils.gone(vLoginProgress);
        } else {

            vBtnLogin.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mTimeElapsed = true;
                    goToHome();
                }
            }, 1000);

            String user = SessionStore.getUser(this);
            String pass = SessionStore.getPass(this);
            login(user, pass);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.vBtnLogin:

                if (validate()) {
                    mTimeElapsed = true;
                    String user = InputUtils.getText(vUser);
                    String pass = InputUtils.getText(vPass);
                    login(user, pass);
                }

                break;

            default:
                break;
        }
    }

    private boolean validate() {

        if (InputUtils.isEmpty(vUser)) {
            MessageUtils.showMsg(this, R.string.login_error_empty_user);
            return false;
        }
        if (InputUtils.isEmpty(vPass)) {
            MessageUtils.showMsg(this, R.string.login_error_empty_pass);
            return false;
        }

        return true;
    }

    private void login(final String user, final String pass) {
        ViewUtils.gone(vLoginFrom);
        ViewUtils.visible(vLoginProgress);

        ApiService.login(user, pass, new AsyncCallback<LoginResult>(this) {
            @Override
            protected void onSuccess(LoginResult result) {
                SessionStore.setSession(BilpaActivity.this, user, pass, result.data);
                mDataElapsed = true;
                goToHome();
            }

            @Override
            protected void onFail(Throwable caught) {
                super.onFail(caught);
                ViewUtils.gone(vLoginProgress);
            }

            @Override
            protected void onServiceOperationFailOK() {
                super.onServiceOperationFailOK();
                finish();
            }
        });
    }

    private void goToHome() {
        if (mTimeElapsed && mDataElapsed) {
            Intent i = null;
            int defaultSection = SessionStore.getDefaultSection(this);
            switch (defaultSection) {
                case 0:
                    i = new Intent(this, BilpaHomeActivity.class);
                    break;
                case 1:
                    i = new Intent(this, HomeActivity.class);
                    break;
                case 2:
                    i = new Intent(this, CorrectivosActivity.class);
                    break;
            }
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BilpaApp.getInstance().cancelPendingRequests(ApiService.LOGIN);
    }


}



