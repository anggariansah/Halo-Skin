package com.solvedev.haloskin.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.solvedev.haloskin.R;
import com.solvedev.haloskin.model.UserRespons;
import com.solvedev.haloskin.network.ApiRequest;
import com.solvedev.haloskin.network.RetrofitServer;
import com.solvedev.haloskin.utils.Base;
import com.solvedev.haloskin.utils.UserPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvLogin;
    SignInButton btnSignGoogle;

    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1;
    final int REQUEST_CODE = 101;


    //FB
    CallbackManager callbackManager;
    LoginButton loginButton;

    private ProgressDialog progressDialog;
    private UserPreferences preference;

    String nama, email, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvLogin = findViewById(R.id.tv_login);
        btnSignGoogle = findViewById(R.id.btn_sign_google);

        loginButton = findViewById(R.id.login_button);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }

        progressDialog = new ProgressDialog(LoginActivity.this);
        preference = new UserPreferences(LoginActivity.this);

        facebookLogin();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        tvLogin.setOnClickListener(this);
        btnSignGoogle.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                Intent intentRegis = new Intent(LoginActivity.this, LoginAccountActivity.class);
                startActivity(intentRegis);
                break;
            case R.id.btn_sign_google:
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            nama = account.getDisplayName();
            email = account.getEmail();
            password = account.getId();

            loginAccount(nama, email, "google", "google");

            Log.w("LOGIN", "Login Bisa:"+nama);


        } catch (ApiException e) {
            Log.w("LOGIN", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void facebookLogin(){
        callbackManager = CallbackManager.Factory.create();
        loginButton.setPermissions("email", "public_profile", "user_friends");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.e("aa", ""+response.toString());
                                try {
                                    nama = object.getString("name");
                                    email = object.getString("email");
                                    password = object.getString("id");

                                    loginAccount(nama, email, password, "facebook");


                                } catch(JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplication(), "Login cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplication(), "Login Error :"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("cek","onStart");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("cek","onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("cek","onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("cek","onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("cek","onDestroy");
    }


    private void loginAccount(final String nama, final String email, final String password,final String type) {
        progressDialog.setMessage("Harap Tunggu ..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<UserRespons> login = api.login(email, password, type, "user", Base.apiToken);
        login.enqueue(new Callback<UserRespons>() {
            @Override
            public void onResponse(Call<UserRespons> call, Response<UserRespons> response) {
                progressDialog.dismiss();

                if(response.body() != null){

                    if (!response.body().getError()) {


                        Intent intent = new Intent(LoginActivity.this, VerifikasiActivity.class);
                        intent.putExtra("email", email);
                        intent.putExtra("password", password);
                        startActivity(intent);
                        finish();

                        Toast.makeText(LoginActivity.this, "Login Berhasil!!", Toast.LENGTH_SHORT).show();


                    } else {
                        registerAccount(nama, email, password);
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "error null", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UserRespons> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Network Error LOGIN " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void registerAccount(String nama, final String email, final String password) {
        progressDialog.setMessage("Harap Tunggu ..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiRequest api = RetrofitServer.getClient().create(ApiRequest.class);
        Call<UserRespons> login = api.registerGmail(nama, email, password, "user");
        login.enqueue(new Callback<UserRespons>() {
            @Override
            public void onResponse(Call<UserRespons> call, Response<UserRespons> response) {
                progressDialog.dismiss();

                if(response.body() != null){

                    if (!response.body().getError()) {

                        Intent intent = new Intent(LoginActivity.this, VerifikasiActivity.class);
                        intent.putExtra("email", email);
                        intent.putExtra("password", password);
                        startActivity(intent);
                        finish();

                        Toast.makeText(LoginActivity.this, "Register Berhasil!!", Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(LoginActivity.this, "Register Gagal : "+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "error null", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<UserRespons> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Network Error REGISTER " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
