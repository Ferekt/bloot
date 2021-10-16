package com.mobil_prog.bloot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Random;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.jjoe64.graphview.GraphView;
import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.charts.StackedBarChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;
import org.eazegraph.lib.models.StackedBarModel;

public class Stats extends AppCompatActivity {
    public ArrayList<Transaction> tran_list = new ArrayList<Transaction>();

    @Override
    protected void onStop() {
        tran_list=null;
        super.onStop();
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Stats.this, "Cannot reach server", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            JSONArray myResponse = new JSONArray();
            ArrayList<JSONObject> jObj = new ArrayList<JSONObject>();
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    myResponse = new JSONArray(response.body().string());
                    JSONObject asd = myResponse.getJSONObject(0);
                    Log.e("AAAAA",asd.getString("date").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    for(int i=0;i<myResponse.length();i++){
                        JSONObject asd = (JSONObject)myResponse.get(i);
                        jObj.add(asd);
                    }

                    for (JSONObject temp: jObj) {
                        String sender = temp.getString("sender");
                        String reciever = temp.getString("reciever");
                        int value = temp.getInt("value");
                        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
                        Date date = formatter.parse(temp.getString("date"));
                        Transaction tran = new Transaction(sender, value, reciever, date);
                        tran_list.add(tran);
                        Log.e("DATE", date.toString());
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setPieChartData(tran_list);
                        }
                    });

                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        BarChart mBarChart = (BarChart) findViewById(R.id.barchart);

        JSONObject reg_form = new JSONObject();
        try {
            reg_form.put("subject", "group_tran");
            reg_form.put("group", MainActivity.group);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset= utf-8"), reg_form.toString());
        postRequest(MainActivity.postUrl, body);





        mBarChart.addBar(new BarModel(2.3f, 0xFF123456));
        mBarChart.addBar(new BarModel(2.f,  0xFF343456));
        mBarChart.addBar(new BarModel(3.3f, 0xFF563456));
        mBarChart.addBar(new BarModel(1.1f, 0xFF873F56));
        mBarChart.addBar(new BarModel(2.7f, 0xFF56B7F1));
        mBarChart.addBar(new BarModel(2.f,  0xFF343456));
        mBarChart.addBar(new BarModel(0.4f, 0xFF1FF4AC));
        mBarChart.addBar(new BarModel(4.f,  0xFF1BA4E6));

        mBarChart.startAnimation();

        StackedBarChart mStackedBarChart = (StackedBarChart) findViewById(R.id.stackedbarchart);

        StackedBarModel s1 = new StackedBarModel("12.4");

        s1.addBar(new BarModel(2.3f, 0xFF63CBB0));
        s1.addBar(new BarModel(2.3f, 0xFF56B7F1));
        s1.addBar(new BarModel(2.3f, 0xFFCDA67F));

        StackedBarModel s2 = new StackedBarModel("13.4");
        s2.addBar(new BarModel(1.1f, 0xFF63CBB0));
        s2.addBar(new BarModel(2.7f, 0xFF56B7F1));
        s2.addBar(new BarModel(0.7f, 0xFFCDA67F));

        StackedBarModel s3 = new StackedBarModel("14.4");

        s3.addBar(new BarModel(2.3f, 0xFF63CBB0));
        s3.addBar(new BarModel(2.f, 0xFF56B7F1));
        s3.addBar(new BarModel(3.3f, 0xFFCDA67F));

        StackedBarModel s4 = new StackedBarModel("15.4");
        s4.addBar(new BarModel(1.f, 0xFF63CBB0));
        s4.addBar(new BarModel(4.2f, 0xFF56B7F1));
        s4.addBar(new BarModel(2.1f, 0xFFCDA67F));

        mStackedBarChart.addBar(s1);
        mStackedBarChart.addBar(s2);
        mStackedBarChart.addBar(s3);
        mStackedBarChart.addBar(s4);

        mStackedBarChart.startAnimation();


    }
    public void Back_from_stats (View v){
        Intent intent = new Intent(this, Home_activity.class);
        startActivity(intent);
        this.finish();
    }

    public void setPieChartData(ArrayList<Transaction> list){
        PieChart mPieChart = (PieChart) findViewById(R.id.piechart);
        HashMap<String,Integer> PieData = new HashMap<>();
        for (Transaction tran:tran_list)
        {
            if (PieData.containsKey(tran.sender))
            {
                int old_value = PieData.get(tran.sender);
                old_value+=tran.value;
                PieData.put(tran.sender,old_value);
            }
            else
                {
                    PieData.put(tran.sender,tran.value);
                }
        }
        String color = "#FE6DA8";
        ArrayList<String> colors = new ArrayList<>();
        colors.add("#23351a");
        colors.add("#3c612a");
        colors.add("#558d3a");
        colors.add("#0a0a0a");
        int counter = 0;
        for (String key : PieData.keySet() )
        {

            mPieChart.addPieSlice(new PieModel(key, PieData.get(key), Color.parseColor(colors.get(counter))));
            counter++;
            if(counter>colors.size()){counter=0;}


        }





        mPieChart.startAnimation();
    }

}