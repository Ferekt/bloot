package com.mobil_prog.bloot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
import android.view.View;
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

public class Finances extends AppCompatActivity {
    JSONObject personal = null;
    String Group ;
    LinearLayout lin_lay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finances);
        JSONObject reg_form = new JSONObject();
        try {
            reg_form.put("subject", "personal");
            reg_form.put("username", MainActivity.user);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset= utf-8"), reg_form.toString());
        postRequest(MainActivity.postUrl, body);
        postRequest(MainActivity.postUrl, body);
        double startTime = System.nanoTime();
        double elapsedTime = 0;
        while (personal == null && elapsedTime < 10000) {
            elapsedTime = (System.nanoTime() - startTime) / 1000000;
        }
        if (personal == null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(Finances.this, "Cannot reach server", Toast.LENGTH_SHORT).show();
                }
            });
            Back_from_finances(findViewById(R.id.Back_from_finances));
        } else {

            TextView t = findViewById(R.id.finances_text);
            t.setText(Group);
            Iterator<String> keys = personal.keys();
            List<Integer> le_array = new ArrayList<Integer>();
            List<String> array_keys = new ArrayList<String>();
            while (keys.hasNext()) {
                String key = keys.next();
                if (!(key.equals("id") || key.equals("username") || key.equals("group"))) {
                    if (key.contains("debit")) {
                        try {
                            le_array.add(-1 * personal.getInt(key));
                            array_keys.add(key.substring(6, key.length()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            array_keys.add(key);
                            le_array.add(personal.getInt(key));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            lin_lay = findViewById(R.id.lin_lay);
            UiBuilder(le_array, array_keys, lin_lay);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        personal=null;
    }

    public void Back_from_finances (View v){
        Intent intent = new Intent(this, Home_activity.class);
        startActivity(intent);
        this.finish();
    }
    public void postRequest(String postUrl, RequestBody postbody){
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
                JSONObject le_Json = null;
                String le_String ="";
                try {
                    le_Json = new JSONObject(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                personal=le_Json;
                try {
                    Group= personal.getString("group");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    public void UiBuilder(List<Integer> le_array,List<String>array_keys,LinearLayout lin_lay){
        for (int i=0;i< array_keys.size();i++){
            LinearLayout hor_lay= new LinearLayout(lin_lay.getContext());
            lin_lay.addView(hor_lay);
            hor_lay.setOrientation(LinearLayout.HORIZONTAL);
            Space space=new Space(hor_lay.getContext());
            space.setMinimumWidth(200);
            hor_lay.addView(space);
            EditText person = new EditText(hor_lay.getContext());
            person.setWidth(200);
            person.setText(array_keys.get(i));
            person.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            person.setTextColor(Color.BLACK);
            person.setInputType(InputType.TYPE_NULL);
            hor_lay.addView(person);
            Space space2=new Space(hor_lay.getContext());
            space2.setMinimumWidth(200);
            hor_lay.addView(space2);
            EditText money = new EditText(hor_lay.getContext());
            money.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            money.setText(Integer.toString(le_array.get(i))+"Ft");
            money.setInputType(InputType.TYPE_NULL);
            if (le_array.get(i)<0) {
                money.setTextColor(Color.RED);
            }else{
                money.setTextColor(Color.GREEN);
            }
            hor_lay.addView(money);
            Space space3=new Space(lin_lay.getContext());
            space3.setMinimumWidth(50);
            lin_lay.addView(space3);
        }
    }
}