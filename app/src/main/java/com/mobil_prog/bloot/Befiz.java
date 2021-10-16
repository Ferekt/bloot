package com.mobil_prog.bloot;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
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
        while (MainActivity.group == null) {
            Log.e("sad", "onStart: " + "bree");
        }
        try {
            reg_form.put("subject", "group_data");
            reg_form.put("group", MainActivity.group);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset= utf-8"), reg_form.toString());
        postRequest(MainActivity.postUrl, body);
        double startTime = System.nanoTime();
        double elapsedTime = 0;
        while ((lin_lay == null || stringList == null) && elapsedTime < 10000) {
            elapsedTime = (System.nanoTime() - startTime) / 1000000;
            Log.e("sad", "onStart: " + String.valueOf(elapsedTime));
        }
        if (!(lin_lay == null || stringList == null)) {
            TextView group = findViewById(R.id.Groupname_bef);
            group.setText(MainActivity.group);
            handleRadio(stringList, lin_lay);
        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(Befiz.this, "Cannot reach server", Toast.LENGTH_SHORT).show();
                }
            });
            Back_from_befiz(findViewById(R.id.Back_from_befiz));
        }
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Befiz.this, "Cannot reach server", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Befiz.this, "payment successful", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    public void handleRadio(List<String> le_array,LinearLayout linlay){
        List<CheckBox>CBT= new ArrayList<CheckBox>();
        for(int j=0;j<le_array.size();j++){
            //if(!le_array.get(j).equals(MainActivity.user)) {
                CheckBox cb = new CheckBox(linlay.getContext());
                cb.setButtonDrawable(R.drawable.checkbox_selector);
                cb.setText(le_array.get(j));
                cb.setTextColor(Color.BLACK);
                cb.setTextSize(30);

            linlay.addView(cb, 200, 100);
                CBT.add(cb);
            //}
        }
        CBA=CBT;
    }
    public void pay(View v){
        JSONObject reg_form = new JSONObject();
        EditText pay = findViewById(R.id.amount);
        int j=0;
        for (CheckBox c:CBA) {
            if (c.isChecked()) j++;
        }
        int value = Integer.parseInt(String.valueOf(pay.getText()))/j;
        value=5*(Math.round(value/5));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Payment");
        builder.setMessage("Do you wish to proceed?");
        int finalValue = value;
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (CheckBox c : CBA) {
                            if (c.isChecked() && !c.getText().equals(MainActivity.user)) {
                                try {
                                    reg_form.put("subject", "update");
                                    reg_form.put("sender", MainActivity.user);
                                    reg_form.put("value", finalValue);
                                    reg_form.put("name", c.getText());
                                    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset= utf-8"), reg_form.toString());
                                    postRequest2(MainActivity.postUrl, body);
                                    Back_from_befiz(findViewById(R.id.Back_from_befiz));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }
                    });
                builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }


}