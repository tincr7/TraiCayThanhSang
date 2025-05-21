package com.example.lehoanggiang.traicaythanhsang.activity;

import android.app.DownloadManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lehoanggiang.traicaythanhsang.R;
import com.example.lehoanggiang.traicaythanhsang.adapter.SauRiengAdapter;
import com.example.lehoanggiang.traicaythanhsang.model.Sanpham;
import com.example.lehoanggiang.traicaythanhsang.ultil.CheckConnection;
import com.example.lehoanggiang.traicaythanhsang.ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SauRiengActivity extends AppCompatActivity {
    Toolbar toolbarsr;
    ListView lvsr;
    SauRiengAdapter sauriengadapter;
    ArrayList<Sanpham> mangsr;
    int idsr =0;
    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sau_rieng);
        AnhXa();
        if (CheckConnection.isInternetAvailable(getApplicationContext()))
        {
            GetIdloaisp();
            ActionToolbar();
            GetData(page);

        }else
        {
            CheckConnection.ShowToast_Short(getApplicationContext(),"Bạn hãy kiểm tra lại Internet");
            finish();
        }



        GetIdloaisp();
        ActionToolbar();
        GetData(page);
    }

    private void GetData(int Page) {
        RequestQueue requestqueue = Volley.newRequestQueue(getApplicationContext());
        String duongdan = Server.Duongdansaurieng+String.valueOf(Page);
        StringRequest stringrequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id = 0;
                String Tensr ="";
                int Giasr = 0;
                String Hinhanhsr = "";
                String Mota = "";
                int Idspsr = 0;
                if(response !=null)
                {
                    try{
                        JSONArray jsonarray = new JSONArray(response);
                        for (int i = 0; i < jsonarray.length();i++)
                        {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            id = jsonobject.getInt("id");
                            Tensr = jsonobject.getString("tensp");
                            Giasr = jsonobject.getInt("giasp");
                            Hinhanhsr = jsonobject.getString("hinhanhsp");
                            Mota = jsonobject.getString("motasp");
                            Idspsr = jsonobject.getInt("idsanpham");
                            mangsr.add(new Sanpham(id,Tensr,Giasr,Hinhanhsr,Mota,Idspsr));
                            sauriengadapter.notifyDataSetChanged();
                        }



                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }

            }
        },new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<String, String>();
                param.put("idsanpham",String.valueOf(idsr));
                return param;
            }
        };
        requestqueue.add(stringrequest);
    }


    private void ActionToolbar() {
        setSupportActionBar(toolbarsr);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarsr.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void GetIdloaisp() {
        idsr = getIntent().getIntExtra("idloaisanpham",-1);
        Log.d("giatriloaisanpham",idsr+"");
    }

    private void AnhXa() {
        toolbarsr  = (Toolbar) findViewById(R.id.toolbarsaurieng);
        lvsr  = (ListView) findViewById(R.id.listviewsaurieng);
        mangsr = new ArrayList<>();
        sauriengadapter = new SauRiengAdapter(getApplicationContext(),mangsr);
        lvsr.setAdapter(sauriengadapter);
    }
}
