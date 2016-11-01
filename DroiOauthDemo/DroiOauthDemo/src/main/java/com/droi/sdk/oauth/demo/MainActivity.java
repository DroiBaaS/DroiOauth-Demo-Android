package com.droi.sdk.oauth.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

import com.droi.sdk.core.OauthCoreHelper;
import com.droi.sdk.oauth.OauthError;
import com.droi.sdk.oauth.callback.CheckTokenStateCallback;
import com.droi.sdk.oauth.callback.DroiAccountLoginCallBack;
import com.droi.sdk.oauth.DroiOauth;
import com.droi.sdk.oauth.callback.GetAccountInfoCallBack;
import com.droi.sdk.oauth.Scope;

public class MainActivity extends Activity implements OnClickListener {

    private String mOpenId;
    private String mToken;
    private String mAppId;

    private String Tag = "account_demo";
    private Button mTokenAuth;

    private TextView resultView;
    DroiAccountLoginCallBack droiAccountListener;
    GetAccountInfoCallBack mAccountInfoCallback;
    CheckTokenStateCallback mTokenStateCallback;

    // get account info
    private Button mGetUserInfo;
    private Button mGetUserPhone;
    private Button mGetUserMail;
    private Button mGetTokenState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultView = (TextView) findViewById(R.id.result);

        mTokenAuth = (Button) findViewById(R.id.token);
        mTokenAuth.setOnClickListener(this);

        // get account info
        mGetUserInfo = (Button) findViewById(R.id.get_userinfo);
        mGetUserPhone = (Button) findViewById(R.id.get_userphone);
        mGetUserMail = (Button) findViewById(R.id.get_usermail);
        mGetTokenState = (Button) findViewById(R.id.get_tokenstate);
        mGetUserInfo.setOnClickListener(this);
        mGetUserPhone.setOnClickListener(this);
        mGetUserMail.setOnClickListener(this);
        mGetTokenState.setOnClickListener(this);

        droiAccountListener = new DroiAccountLoginCallBack() {
            @Override
            public void onSuccess(final String result) {
                Log.d(Tag, result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    // TODO Auto-generated method stub
                    resultView.setText(result);
                    }
                });
                parseOpenIdAndToken(result);
            }

            @Override
            public void onError(final String errorCode) {
                Log.d(Tag, errorCode);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        resultView.setText(errorCode);
                    }
                });
            }
        };

        mAccountInfoCallback = new GetAccountInfoCallBack() {
            @Override
            public void onGetAccountInfo(final OauthError error, final String result) {
                // TODO Auto-generated method stub
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if(error == null){
                            resultView.setText(result);
                        } else if(error.isOk()){
                            resultView.setText(result);
                        } else{
                            resultView.setText(error.getDescMessage());
                        }
                    }
                });
            }
        };

        mTokenStateCallback = new CheckTokenStateCallback() {
            @Override
            public void onTokenCheckResult(final OauthError oauthError, final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(oauthError == null){
                            resultView.setText(s);
                        } else if(oauthError.isOk()){
                            resultView.setText(s);
                        }else{
                            resultView.setText(oauthError.getDescMessage());
                        }
                    }
                });
            }
        };

        mAppId = OauthCoreHelper.getAppId();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.get_userinfo: {
            if (TextUtils.isEmpty(mOpenId) || TextUtils.isEmpty(mToken)) {
                resultView.setText(R.string.login_first_hint);
            } else {
                DroiOauth.getAccountInfo(mOpenId, mToken, new Scope[]{Scope.USERINFO}, mAccountInfoCallback);
            }
        }
            break;

        case R.id.get_userphone: {
            if (TextUtils.isEmpty(mOpenId) || TextUtils.isEmpty(mToken)) {
                resultView.setText(R.string.login_first_hint);
            } else {
                DroiOauth.getAccountInfo(mOpenId, mToken, new Scope[]{Scope.USERPHONE},mAccountInfoCallback);
            }

        }
            break;

        case R.id.get_usermail: {
            if (TextUtils.isEmpty(mOpenId) || TextUtils.isEmpty(mToken)) {
                resultView.setText(R.string.login_first_hint);
            } else {
                DroiOauth.getAccountInfo(mOpenId, mToken, new Scope[]{Scope.USERMAIL}, mAccountInfoCallback);
            }
        }
            break;

        case R.id.get_tokenstate:{
             if (TextUtils.isEmpty(mOpenId) || TextUtils.isEmpty(mToken)) {
                 resultView.setText(R.string.login_first_hint);
             } else {
                 DroiOauth.checkTokenExpire(mToken, mTokenStateCallback);
             }
        }
             break;

        case R.id.token: {
            authRequest();
        }
            break;

        default:
            break;
        }
    }

    private void authRequest() {
        String language = null;
        Locale locale = getResources().getConfiguration().locale;
        if (locale != null) {
            language = locale.getLanguage();
        }
        DroiOauth.setLanguage(language);
        DroiOauth.requestTokenAuth(MainActivity.this, droiAccountListener);
    }

    private void parseOpenIdAndToken(String result) {
        if (!TextUtils.isEmpty(result)) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("openid")) {
                    mOpenId = jsonObject.getString("openid");
                }
                if (jsonObject.has("token")) {
                    mToken = jsonObject.getString("token");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
