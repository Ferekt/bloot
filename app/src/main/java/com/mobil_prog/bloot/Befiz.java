package com.mobil_prog.bloot;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class Befiz extends AppCompatActivity {
    public LinearLayout lin_lay = null;
    public List<String> stringList= null;
    public List<CheckBox> CBA =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_befiz);
    }

    @Override
    protected void onStop() {
        super.onStop();
        lin_lay.removeAllViews();
        lin_lay=null;
        stringList= null;
        CBA=null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        JSONObject reg_form = new JSONObject();
        while(MainActivity.group==null){
            Log.e("sad", "onStart: "+"bree");
        }
        try {
            reg_form.put("subject", "group_data");
            reg_form.put("group",MainActivity.group);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset= utf-8"), reg_form.toString());
        postRequest(MainActivity.postUrl, body);
        while (lin_lay == null || stringList == null){
            Log.e("sad", "onStart: "+"bree");
        }
        TextView group= findViewById(R.id.Groupname_bef);
        group.setText(MainActivity.group);
        handleRadio(stringList,lin_lay);
    }

    public void Back_from_befiz (View v){
        Intent intent = new Intent(this, Home_activity.class);
        startActivity(intent);
        this.finish();
    }

    public void postRequest(String postUrl, RequestBody postbody) {
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(postUrl)
                .post(postbody)
                .header("Accept", "application/json")
                .header("Content-type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                Log.d("Fail", e.getMessage());
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
               Iterator<String>keys= le_Json.keys();
               List<String>le_array=new ArrayList<String>();
               while (keys.hasNext()){
                   String key= keys.next();
                   try {
                       le_array.add(le_Json.getString(key));
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
               }
               LinearLayout linlay= findViewById(R.id.linlay);
               lin_lay =linlay;
               stringList=le_array;
            }
        });
    }
    public void postRequest2(String postUrl, RequestBody postbody) {
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(postUrl)
                .post(postbody)
                .header("Accept", "application/json")
                .header("Content-type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                Log.d("Fail", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("k",response.body().string().trim());
            }
        });
    }
    public void handleRadio(List<String> le_array,LinearLayout linlay){
        List<CheckBox>CBT= new ArrayList<CheckBox>();
        for(int j=0;j<le_array.size();j++){
            if(!le_array.get(j).equals(MainActivity.user)) {
                CheckBox cb = new CheckBox(linlay.getContext());
                cb.setButtonDrawable(R.drawable.checkbox_selector);
                cb.setText(le_array.get(j));
                linlay.addView(cb, 200, 100);
                CBT.add(cb);
            }
        }
        CBA=CBT;
    }
    public void pay(View v){
        JSONObject reg_form = new JSONObject();
        EditText pay = findViewById(R.id.amount);
        int j=1;
        for (CheckBox c:CBA) {
            if (c.isChecked()) j++;
        }
        int value = Integer.parseInt(String.valueOf(pay.getText()))/j;
        value=5*(Math.round(value/5));
        for (CheckBox c:CBA) {
            if (c.isChecked()) {
                try {
                    reg_form.put("subject", "update");
                    reg_form.put("sender", MainActivity.user);
                    reg_form.put("value", value);
                    reg_form.put("name", c.getText());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset= utf-8"), reg_form.toString());
                postRequest2(MainActivity.postUrl, body);
            }
        }
    }
}