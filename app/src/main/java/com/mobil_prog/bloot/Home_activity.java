package com.mobil_prog.bloot;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Home_activity extends AppCompatActivity {
    EditText Value;
    boolean done;
    Integer value = null;
    boolean just_deleted=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    }

    @Override
    protected void onStop() {
        super.onStop();
        done=false;
        value=null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Value=findViewById(R.id.Value);
        value =null;
        JSONObject reg_form = new JSONObject();
        try {
            reg_form.put("subject", "personal");
            reg_form.put("username", MainActivity.user);

        } catch (
                JSONException e) {
            e.printStackTrace();
        }
        RequestBody body;
        if(!just_deleted) {
            body = RequestBody.create(MediaType.parse("application/json; charset= utf-8"), reg_form.toString());
            postRequest(MainActivity.postUrl, body);
        }else just_deleted=false;
        double startTime= System.nanoTime();
        double elapsedTime= 0;
        while (!done&& elapsedTime< 10000){
            elapsedTime=(System.nanoTime()-startTime)/1000000;
            Log.d("wait", "onStart:"+ String.valueOf(elapsedTime));
        }
        if(!done){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(Home_activity.this, "Cannot reach server", Toast.LENGTH_SHORT).show();
                }
            });
            logout(findViewById(R.id.logout));
        }
        Value.setText(value+" Ft");
        Value.setInputType(InputType.TYPE_NULL);
    }

    public void logout(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void to_befiz(View v) {
        Intent intent = new Intent(this, Befiz.class);
        startActivity(intent);
        this.finish();
    }

    public void to_stats(View v) {
        Intent intent = new Intent(this, Stats.class);
        startActivity(intent);
        this.finish();
    }

    public void to_join(View v) {
        Intent intent = new Intent(this, Join.class);
        startActivity(intent);
        this.finish();
    }

    public void to_finances(View v) {
        Intent intent = new Intent(this, Finances.class);
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
            private Object EditText;

            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Home_activity.this, "Cannot reach server", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.d("Fail", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONObject le_Json = null;
                try {
                    le_Json = new JSONObject(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (le_Json!=null) {
                    try {
                        if (le_Json.has("group")) {
                            MainActivity.group = le_Json.getString("group");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Iterator<String> keys = le_Json.keys();
                    List<Integer> le_array = new ArrayList<Integer>();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        if (!(key.equals("id") || key.equals("username") || key.equals("group"))) {
                            if (key.contains("debit")) {
                                try {
                                    le_array.add(-1 * le_Json.getInt(key));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    le_array.add(le_Json.getInt(key));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    value = 0;
                    for (Integer j : le_array) {
                        value += j;
                    }
                    done = true;
                }
            }
        });
    }

    public void delete(View v) {
        JSONObject reg_form = new JSONObject();
        while (MainActivity.user == null) {
        }
        try {
            reg_form.put("subject", "delete");
            reg_form.put("username", MainActivity.user);

        } catch (
                JSONException e) {
            e.printStackTrace();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Are you sure?");
        builder.setMessage("Your account will be permanently deleted");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset= utf-8"), reg_form.toString());
                postRequest(MainActivity.postUrl, body);
                MainActivity.user=null;
                just_deleted=true;
                logout(v);
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