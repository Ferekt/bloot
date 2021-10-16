package com.mobil_prog.bloot;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Space;
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
    public List<TextView> textViewList = null;
    public List<LinearLayout> linearLayoutList=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_befiz);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    protected void onStop() {
        super.onStop();
        lin_lay.removeAllViews();
        lin_lay=null;
        stringList= null;
        CBA=null;
        textViewList = null;
        linearLayoutList=null;
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
        List<TextView> textViews= new ArrayList<>();
        List<LinearLayout>linearLayouts = new ArrayList<>();
        for(int j=0;j<le_array.size();j++){
            LinearLayout hor_lay= new LinearLayout(lin_lay.getContext());
            hor_lay.setBackgroundResource(R.drawable.edit_text_back_ground);
            lin_lay.addView(hor_lay);
            CheckBox cb = new CheckBox(linlay.getContext());
            cb.setButtonDrawable(R.drawable.checkbox_selector);
            cb.setText("");
            cb.setTextColor(Color.BLACK);
            hor_lay.setOrientation(LinearLayout.HORIZONTAL);
            cb.setInputType(InputType.TYPE_NULL);
            cb.setFocusable(false);
            hor_lay.addView(cb);
            Space space=new Space(hor_lay.getContext());
            space.setMinimumWidth(200);
            hor_lay.addView(space);
            EditText person = new EditText(hor_lay.getContext());
            person.setLayoutParams(hor_lay.getLayoutParams());
            person.setText(le_array.get(j));
            person.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            person.setBackgroundResource(R.drawable.make_underline_disappear);
            person.setInputType(InputType.TYPE_NULL);
            person.setFocusable(false);
            hor_lay.addView(person);
            Space space2=new Space(hor_lay.getContext());
            space2.setMinimumWidth(200);
            hor_lay.addView(space2);
            CBT.add(cb);
            textViews.add(person);
            linearLayouts.add(hor_lay);
        }
        CBA=CBT;
        linearLayoutList=linearLayouts;
        textViewList=textViews;
        for (int i=0;i< CBA.size();i++) {
            int finalI = i;
            CBA.get(i).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked){
                        textViewList.get(finalI).setTextColor(Color.parseColor("#558d3a"));
                        linearLayoutList.get(finalI).setBackgroundResource(R.drawable.edit_text_bg_green);
                    }else{
                        textViewList.get(finalI).setTextColor(Color.BLACK);
                        linearLayoutList.get(finalI).setBackgroundResource(R.drawable.edit_text_back_ground);
                    }
                   // Log.e("bree", "onCheckedChanged: "+linearLayoutList.get(finalI).getBackground());
                }
            });
        }
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