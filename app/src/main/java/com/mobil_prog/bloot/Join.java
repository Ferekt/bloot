package com.mobil_prog.bloot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Join extends AppCompatActivity {
    public Activity activity = this;
    public List<String> groups = null;
    public ListView list ;
    public TextView search ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        list = findViewById(R.id.listview);
        search = findViewById(R.id.searchView);
    }
    @Override
    protected void onStop() {
        super.onStop();
        groups= null;
    }
    @Override
    protected void onStart() {
        super.onStart();
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchHandler(search.getText().toString());
            }
        });
        JSONObject reg_form = new JSONObject();
        try {
            reg_form.put("subject", "send_groups");
        } catch (
                JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset= utf-8"), reg_form.toString());
        postRequestGroups(MainActivity.postUrl,body);

    }
    public void Back_from_join (View v){
        Intent intent = new Intent(this, Home_activity.class);
        startActivity(intent);
        this.finish();
    }
    public void Join_group(View v){
        TextView Groupname = findViewById(R.id.searchView);
        String Group = (String) Groupname.getText();
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

    public void postRequestGroups(String postUrl,RequestBody postbody){
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
                JSONObject le_Json = null;
                String le_String ="";
                try {
                    le_Json = new JSONObject(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Iterator<String> keys= le_Json.keys();
                List<String> le_array=new ArrayList<String>();
                while (keys.hasNext()){
                    String key= keys.next();
                    try {
                        le_array.add(le_Json.getString(key));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                groups=le_array;
                Log.e("bree", groups.toString());
            }
        });
    }
    public void postRequest(String postUrl,RequestBody postbody) {
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(postUrl)
                .post(postbody)
                .header("Accept", "application/json")
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
    public void searchHandler(String text){
        List<String> groups_filtered = new ArrayList<>();
        for (String string:groups) {
            if (string.toLowerCase().contains(text.toLowerCase())){
                groups_filtered.add(string);
            }
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.listview, groups_filtered);
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position,long id) {
                search.setText(list.getItemAtPosition(position).toString());
            }
        });
    }
}