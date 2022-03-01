package com.example.retrofittesting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.retrofittesting.adapters.ItemAdapter;
import com.example.retrofittesting.interfaces.MainInterface;
import com.example.retrofittesting.models.MainData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class MainActivity extends AppCompatActivity  {

RecyclerView recyclerView ;
ArrayList<MainData> arrayList = new ArrayList<MainData>();

ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        MainData obj = new MainData();
        obj.setName1("Thura Linn");
        obj.setImage1(R.drawable.sone);

        MainData obj1 = new MainData();
        obj.setName1("Han Htut San");
        obj.setImage1(R.drawable.stwo);

        arrayList.add(obj);
        arrayList.add(obj1);

recyclerView = findViewById(R.id.rv_recycler_view);
adapter = new ItemAdapter(arrayList,this);

        //GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);


        getData();

    }

    private void getData() {
        //Initialize ProgressDialog
        ProgressDialog progressDialog = new ProgressDialog(this);
        //Set Message on Dialog
        progressDialog.setMessage("Please Wait...");
        //Set non Cancelable
        progressDialog.setCancelable(false);
        //show dialog
        progressDialog.show();


        //Initialize retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://picsum.photos/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();


        //Create Interface
        MainInterface mainInterface = retrofit.create(MainInterface.class);
        Call<String> stringCall = mainInterface.STRING_CALL();


        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful() && response.body() != null){
                        progressDialog.hide();

                        //Initialize response Json Array
                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        parseArray(jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });


    }

    private void parseArray(JSONArray jsonArray) {
        arrayList.clear();

        for(int i = 0 ;i < jsonArray.length();i++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                MainData mainData = new MainData();
                mainData.setImage(jsonObject.getString("download_url"));
                mainData.setName(jsonObject.getString("author"));
                arrayList.add(mainData);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapter = new ItemAdapter(arrayList,this);
            recyclerView.setAdapter(adapter);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }




}