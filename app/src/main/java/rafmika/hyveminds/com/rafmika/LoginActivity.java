package rafmika.hyveminds.com.rafmika;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyveminds.rafmika.api.RafmikaAPI;
import com.hyveminds.rafmika.model.Challenge;
import com.hyveminds.rafmika.model.LoginModel;
import com.hyveminds.rafmika.response.GetChallengeResponse;
import com.hyveminds.rafmika.response.LoginResponse;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class LoginActivity extends AppCompatActivity {

//    private String accessKey = "UoHZ58IKGokhyzr1";
    private String userName;

    private static final int REQUEST_EVENT_LIST = 0;

    @Bind(R.id.input_username) EditText _usernameText;
    @Bind(R.id.input_accesskey) EditText _accesskeyText;
    @Bind(R.id.btn_login) Button _loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

//        this.userName = "aaron";
//        _usernameText.setText(this.userName);
//        _accesskeyText.setText(accessKey);
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getChallenge();
            }
        });
    }

    public void getChallenge() {
        if (!validate()) {
            onLoginFailed();
            return;
        }
        _loginButton.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RafmikaAPI.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RafmikaAPI service = retrofit.create(RafmikaAPI.class);
        Call<GetChallengeResponse> call = service.getChallengeDetail(this.userName);

        call.enqueue(new Callback<GetChallengeResponse>() {
            @Override
            public void onResponse(Response<GetChallengeResponse> response, Retrofit retrofit) {
                progressDialog.dismiss();
                _loginButton.setEnabled(true);
                try {
                    if (response.body().isSuccessful()) {
                        Challenge challenge = new Challenge();
                        challenge = response.body().getResult();

                        Log.d("response", challenge.getToken());

                        String token = challenge.getToken();
                        login(token);

                    } else {
                        Toast.makeText(LoginActivity.this, response.body().getError().getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }


                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    }
    public String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    public void login(String token) {
        _loginButton.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String accessKey = token + _accesskeyText.getText();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RafmikaAPI.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RafmikaAPI service = retrofit.create(RafmikaAPI.class);
        Call<LoginResponse> call = service.login("login", this.userName, md5(accessKey));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Response<LoginResponse> response, Retrofit retrofit) {
                _loginButton.setEnabled(true);
                progressDialog.dismiss();
                try {
                    if (response.body().isSuccessful()) {
                        LoginModel loginModel = response.body().getResult();

                        String sessionId = loginModel.getsessionName();
                        Log.d("Session ID", loginModel.getsessionName());

                        Intent intent = new Intent(getApplicationContext(), EventListActivity.class);
                        intent.putExtra("EXTRA_SESSION_ID", sessionId);
                        intent.putExtra("EXTRA_USER_ID", loginModel.getUserId());
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);


                    } else {
                        Toast.makeText(LoginActivity.this, response.body().getError().getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }


                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String username = _usernameText.getText().toString();
        String assesskey = _accesskeyText.getText().toString();

        if (username.isEmpty()) {
            _usernameText.setError("please enter user name");
            valid = false;
        } else {
            this.userName = username;
            _usernameText.setError(null);
        }

        if (assesskey.isEmpty()) {
            _accesskeyText.setError("please enter access key");
            valid = false;
        } else {
            _accesskeyText.setError(null);
        }

        return valid;
    }
}
