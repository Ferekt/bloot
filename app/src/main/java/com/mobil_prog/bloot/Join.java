package com.mobil_prog.bloot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class Join extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
    }
    public void Back_from_join (View v){
        Intent intent = new Intent(this, Home_activity.class);
        startActivity(intent);
        this.finish();
    }
    public void Join_group(View v){
        TextView Groupname = findViewById(R.id.group_name);
        String Group = Groupname.getText().toString().trim();
        JSONObject reg_form = new JSONObject();
        try {
            reg_form.put("subject", "join_group");
            reg_form.put("sender", MainActivity.user );
            reg_form.put("group",Group);

        } catch (
                JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset= utf-8"), reg_form.toString());
        postRequest(MainActivity.postUrl,body);
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Join.this, "Cannot reach server", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                        public void run() {
                            Toast.makeText(Join.this, "You have joined a group", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}