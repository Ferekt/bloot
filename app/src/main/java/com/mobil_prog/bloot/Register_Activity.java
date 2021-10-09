package com.mobil_prog.bloot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Register_Activity extends AppCompatActivity {
    boolean gut;
    boolean thread_finish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
    @Override
    protected void onStop(){
        super.onStop();
        gut=false;
    }
    public void register(View v) {
        EditText reg_username = findViewById(R.id.Reg_Username);
        EditText reg_password = findViewById(R.id.Reg_Password);
        EditText reg_password_again = findViewById(R.id.Reg_PasswordAgain);

        String username = reg_username.getText().toString().trim();
        String password = reg_password.getText().toString().trim();
        String password_again = reg_password_again.getText().toString().trim();

        if (!password.equals(password_again)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(Register_Activity.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                    reg_password.setText("");
                    reg_password_again.setText("");
                }
            });
        } else {
            JSONObject reg_form = new JSONObject();
            try {
                reg_form.put("subject", "register");
                reg_form.put("username", username);
                reg_form.put("password", password.hashCode());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset= utf-8"), reg_form.toString());
            postRequest(MainActivity.postUrl, body,v);
        }
    }

    public void postRequest(String postUrl,RequestBody postbody,View v){
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(postUrl)
                .post(postbody)
                .header("Accept","application/json")
                .header("Content-type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                Log.d("Fail",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String reg_response= response.body().string().trim();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("g", "run: "+"bree");
                        if (reg_response.equals("ok")){
                            Toast.makeText(Register_Activity.this,"You in",Toast.LENGTH_SHORT).show();
                            gut=true;
                        }else{
                            Toast.makeText(Register_Activity.this,"You not in",Toast.LENGTH_SHORT).show();
                        }
                        thread_finish=true;
                    }
                });
            }
        });
    }
    public void back_to_login(View v){
        Intent intent = new Intent(this, Login_Activity.class);
        startActivity(intent);
        this.finish();
    }
}