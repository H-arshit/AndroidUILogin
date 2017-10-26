package com.example.harshit.apiuse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Signup extends AppCompatActivity implements View.OnClickListener {


    EditText phone_no , email_signup , password_password;
    TextView signin_again;
    Button signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        password_password = (EditText)findViewById(R.id.password_password);
        email_signup = (EditText)findViewById(R.id.email_signup);
        phone_no = (EditText)findViewById(R.id.phone_no);
        signup = (Button)findViewById(R.id.signup);
        signin_again = (TextView)findViewById(R.id.signin_again);


        signin_again.setOnClickListener(this);
        signup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.signup) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Api.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Api api = retrofit.create(Api.class);


            RequestData requestData = new RequestData();
            requestData.setEmail(email_signup.getText().toString());
            requestData.setPassword(password_password.getText().toString());
            requestData.setPhone_no(phone_no.getText().toString());

            Call<ResultData> result = api.getresultsign(requestData);

            result.enqueue(new Callback<ResultData>() {
                @Override
                public void onResponse(Call<ResultData> call, Response<ResultData> response) {

                    ResultData resultData = response.body();
                   if (resultData.isvalid()) {








                       final String PREF_NAME = "com.data.wfi.userdetails";
                       SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                       SharedPreferences.Editor editorffordata = sharedPreferences.edit();

                       editorffordata.putString("AUTH_TOKEN",resultData.getAuth_token());
                       editorffordata.putString("EMAIL_ID",resultData.getEmail());
                       editorffordata.apply();






                       Intent intent = new Intent(getBaseContext(), Result.class);
                        intent.putExtra("email", resultData.getEmail());
                        intent.putExtra("auth_token", resultData.getAuth_token());









                       startActivity(intent);
                    }else{
                       Toast.makeText(getApplicationContext(), "Account Already Exists", Toast.LENGTH_LONG).show();

                   }


                }

                @Override
                public void onFailure(Call<ResultData> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
                }
            });
        }else{
            Intent intent = new Intent(getBaseContext(), MainActivity.class);

            startActivity(intent);
        }
    }
}
