package com.mobil_prog.bloot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Entity;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Login_Activity extends AppCompatActivity {
    String username=null;
    String password=null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    @Override
    protected void onStop(){
        super.onStop();
        username=null;
        password=null;
    }
    public void login(View v) {
        EditText log_username = findViewById(R.id.Login_Username);
        EditText log_password = findViewById(R.id.Login_password);
        username = log_username.getText().toString().trim();
        password = log_password.getText().toString().trim();

        JSONObject reg_form = new JSONObject();
        try {
            reg_form.put("subject", "login");
            reg_form.put("username", username);
            reg_form.put("password",password.hashCode());

        } catch (
                JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset= utf-8"), reg_form.toString());
        postRequest(MainActivity.postUrl, body);
        to_Home(v);
    }
    public void postRequest(String postUrl,RequestBody postbody){
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(postUrl)
                .post(postbody)
                .header("Accept","application/json")
                .header("Content-type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            private Object EditText;

            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                Log.d("Fail",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            String le_response= response.body().string().trim();
            if(le_response.equals("ok")){
                MainActivity.user=username;
            }
            }
        });
    }
    public void to_Home(View v){
        Intent intent = new Intent(this, Home_activity.class);
        startActivity(intent);
        this.finish();
    }
}