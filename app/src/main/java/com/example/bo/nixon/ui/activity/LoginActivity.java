package com.example.bo.nixon.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bo.nixon.R;
import com.example.bo.nixon.base.BaseMvpNotitleActivity;
import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.manager.FontManager;
import com.example.bo.nixon.presenter.LoginContract;
import com.example.bo.nixon.utils.Constant;
import com.example.bo.nixon.utils.SPUtils;
import com.example.bo.nixon.utils.StringUtils;
import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseMvpNotitleActivity<LoginContract.LoginPresenter>
    implements LoginContract.LoginNixonView, GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 10010;
    @BindView (R.id.login_login) TextView mLoginLogin;
    @BindView (R.id.login_forget) TextView mLoginForget;
    @BindView (R.id.login_to_register) TextView mLoginToRegister;
    @BindView (R.id.login_facebook) TextView mLoginFacebook;
    @BindView (R.id.login_google) TextView mLoginGoogle;
    @BindView (R.id.login_user_name) MaterialEditText mLoginUserName;
    @BindView (R.id.login_password) MaterialEditText mLoginPassword;
    @BindView (R.id.login_text) RelativeLayout mLoginText;
    @BindView (R.id.register_to_login) TextView mRegisterToLogin;
    @BindView (R.id.register_text) LinearLayout mRegisterText;
    @BindView (R.id.login_back) ImageView mLoginBack;
    private Bundle mExtras;
    private String mUserName;
    private String mPassWord;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mCallbackManager;
    private AccessTokenTracker mAccessTokenTracker;
    private Set<String> mPermissions;
    private Date mLastRefresh;
    private Date mExpires;
    private String mToken;
    private AccessTokenSource mSource;
    private Set<String> mDeclinedPermissions;
    private String mUserId;
    private InputMethodManager mImm;
    private String mS;
    private boolean isRequesting;

    @Override protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        mExtras = getIntent ().getExtras ();
        setContentView (R.layout.activity_login);
        ButterKnife.bind (this);
        FontManager.changeFonts ((ViewGroup) findViewById (R.id.activity_login));
        mImm = (InputMethodManager) getSystemService (NixonApplication.INPUT_METHOD_SERVICE);
        mCallbackManager = CallbackManager.Factory.create ();
        mAccessTokenTracker =

                new AccessTokenTracker () {
            @Override
            protected void onCurrentAccessTokenChanged (AccessToken oldAccessToken, AccessToken currentAccessToken) {
                String applicationId = currentAccessToken.getApplicationId ();
                mUserId = currentAccessToken.getUserId ();
                mDeclinedPermissions = currentAccessToken.getDeclinedPermissions ();
                mSource = currentAccessToken.getSource ();
                mToken = currentAccessToken.getToken ();
                mExpires = currentAccessToken.getExpires ();
                mLastRefresh = currentAccessToken.getLastRefresh ();
                mPermissions = currentAccessToken.getPermissions ();
                Log.e ("FACEBOOKLOGIN", "applicationId = "
                    + applicationId
                    + "  mUserId = "
                    + mUserId
                    + "  mDeclinedPermissions = "
                    + mDeclinedPermissions
                    + "  mSource = "
                    + mSource
                    + "  mToken = "
                    + mToken
                    + "  mExpires = "
                    + mExpires
                    + "  mLastRefresh = "
                    + mLastRefresh
                    + "  mPermissions = "
                    + mPermissions);
                presenter.facebookLoginRequest(currentAccessToken);
            }
        };
        initView ();
        initGoogle ();
    }

    private void initGoogle () {
        GoogleSignInOptions gso =
            new GoogleSignInOptions.Builder (GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail ().build ();
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder (this).enableAutoManage (this /* FragmentActivity */, this /* OnConnectionFailedListener */)
            .addApi (Auth.GOOGLE_SIGN_IN_API, gso)
            .build ();
    }

    private void initView () {
        if (mExtras != null && mExtras.getInt (Constant.START_CODE_KEY) == Constant.STARTCODE) { //注册页面
            mLoginLogin.setText (R.string.login_register);
            mLoginText.setVisibility (View.GONE);
            mRegisterText.setVisibility (View.VISIBLE);
            mLoginPassword.setHint (StringUtils.getSpannableString (R.string.register_pass_hint, 10));
            mLoginUserName.setHint (getResources ().getString (R.string.register_user_hint));
        } else {
            // 设置hint
            mLoginPassword.setHint (StringUtils.getSpannableString (R.string.login_pass_hint, 16));
        }
    }

    @Override protected void onStart () {
        super.onStart ();
        mS = SPUtils.getString (this, Constant.USERNAME_KEY, "");
        mLoginUserName.setText (mS);
        String s1 = SPUtils.getString (this, Constant.PASSWORD_KEY, "");
//        mLoginPassword.setText (s1);
    }

    @Override protected LoginContract.LoginPresenter createPresenter () {
        return new LoginContract.LoginPresenter ();
    }

    @OnClick ({
        R.id.login_login, R.id.login_forget, R.id.login_to_register, R.id.login_facebook, R.id.login_google,
        R.id.register_to_login, R.id.login_back
    }) public void onViewClicked (View view) {
        Intent intent;
        switch (view.getId ()) {
            case R.id.login_login:
                //if (isRequesting){
                //     return;
                // }
                mUserName = mLoginUserName.getText ().toString ().trim ();
                mPassWord = mLoginPassword.getText ().toString ().trim ();
                if (!StringUtils.isEmail (mUserName)) {
                    show1NewDialog (R.string.wrong_email);
                    return;
                }
                if (TextUtils.isEmpty (mPassWord)) {
                    show1NewDialog (R.string.wrong_password_setting);
                    return;
                }
                if (mExtras != null && mExtras.getInt (Constant.START_CODE_KEY) == Constant.STARTCODE) {
                    register ();
                } else {
                    login ();
                }
                break;
            case R.id.login_forget:
                intent = new Intent (this, ForgetPasswordActivity.class);
                startActivity (intent);
                break;
            case R.id.login_to_register:
                intent = new Intent (this, LoginActivity.class);
                intent.putExtra (Constant.START_CODE_KEY, Constant.STARTCODE);
                startActivity (intent);
                finish ();
                break;
            case R.id.login_facebook:
                facebookLogin ();
                break;
            case R.id.login_google:
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent (mGoogleApiClient);
                startActivityForResult (signInIntent, RC_SIGN_IN);
                break;
            case R.id.register_to_login:
                intent = new Intent (this, LoginActivity.class);
                startActivity (intent);
                finish ();
                break;
            case R.id.login_back:
                finish ();
                break;
        }
    }

    private void facebookLogin () {
        LoginManager instance = LoginManager.getInstance ();
        LoginButton loginButton = new LoginButton (this);
        loginButton.setReadPermissions ("email");
        Log.e ("FACEBOOKLOGIN", "  FACEBOOKLOGIN  ");
        instance.logInWithReadPermissions (this, Arrays.asList("email"));
        loginButton.registerCallback (mCallbackManager, new FacebookCallback<LoginResult> () {
            @Override public void onSuccess (LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken ();
                Log.e ("FACEBOOKLOGIN", "  cg  " + accessToken.toString ());
                mAccessTokenTracker.startTracking ();
            }

            @Override public void onCancel () {

            }

            @Override public void onError (FacebookException error) {
                Log.e ("FACEBOOKLOGIN", " cuowu   " + error.toString ());
            }
        });
    }

    private void register () {
        Map<String, String> params = new HashMap<> ();
        if(StringUtils.isPassword (mPassWord)){
            params.put ("subject", mUserName);
            params.put ("password", mPassWord);
            params.put ("repassword", mPassWord);
            isRequesting = true;
            presenter.registerRequest (params);
        }else{
            registerFailed();
        }
    }

    private void login () {
        Map<String, String> params = new HashMap<> ();
        params.put ("subject", mUserName);
        params.put ("password", mPassWord);
        isRequesting = true;
        presenter.loginRequest (params);
    }

    @Override protected void onDestroy () {
        super.onDestroy ();
        mAccessTokenTracker.stopTracking ();
    }

    /**
     * 根据点击的位置判断是否隐藏键盘
     *
     * @return
     */
    @Override public boolean dispatchTouchEvent (MotionEvent ev) {
        if (ev.getAction () == MotionEvent.ACTION_UP) {
            View view = getCurrentFocus ();
            if (inputIsShow (view, ev)) {
                if (null != mImm) {
                    mImm.hideSoftInputFromWindow (view.getWindowToken (), 0);
                }
            }
        }
        return super.dispatchTouchEvent (ev);
    }

    @Override public void newUserLoginSuccess () {
        //if (!mS.equals (mUserName)){ //现在登录的账号跟上次登录的账号不是同一个
        //    SPUtils.clear (NixonApplication.getContext ()); //清除之前本地保存的所有信息
        //}
        isRequesting = false;
        saveUserAndpass ();//保存登录信息
        //用户没有设置过个人信息
        Intent intent = new Intent (this, EditUserInfoActivity.class);
        startActivity (intent);
        finish ();
    }

    @Override public void oldUserLoginSuccess () {
        isRequesting = false;
        saveUserAndpass ();//保存登录信息
        //用户已经设置过个人信息
        Intent intent = new Intent (this, MainActivity.class);
        startActivity (intent);
        finish ();
    }

    @Override public void loginFailed () {
        isRequesting = false;
        show1NewMidDialog (R.string.login_error);
    }

    @Override public void noActivition () {
        //showErrorDialog ();
        show1NewMidDialog (R.string.no_activited);
        presenter.sendActivateLink (mUserName);
    }

    @Override public void registerSuccess () {
        isRequesting = false;
        show1NewMidDialog (R.string.register_successful, new View.OnClickListener () {
            @Override public void onClick (View view) {
                login ();
                hideDialog ();
            }
        });
    }

    @Override public void registerFailed () {
        isRequesting = false;
        show1NewDialog (R.string.wrong_password_setting);
    }

    @Override
    public void registerUserExist () {
        isRequesting = false;
        show1NewMidDialog(R.string.user_exist);
    }

    public void saveUserAndpass () {
        SPUtils.putString (this, Constant.USERNAME_KEY, mUserName);
        SPUtils.putString (this, Constant.PASSWORD_KEY, mPassWord);
    }

    @Override public void onConnectionFailed (@NonNull ConnectionResult connectionResult) {
        Log.e ("NIXONLOGIN", " google dneglu 连接失败");
    }

    @Override public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        mCallbackManager.onActivityResult (requestCode, resultCode, data);
        Log.e ("NIXONLOGIN", " google dneglu   ");
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent (data);
            if (null != result) {
                presenter.googleLoginRequest (result);
            }
            //handleSignInResult (result);
        } else {
            Log.e ("NIXONLOGIN", " google dneglu   失败 ");
        }
    }

    private void handleSignInResult (GoogleSignInResult result) {
        Log.d ("NIXONLOGIN", "handleSignInResult:" + result.isSuccess ());
        if (result.isSuccess ()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount ();
            toActivity (MainActivity.class);
        } else {

        }
    }
}

