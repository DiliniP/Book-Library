package com.example.bloodbank.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.bloodbank.R;
import com.example.bloodbank.Utils.Endpoints;
import com.example.bloodbank.Utils.VolleySingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final EditText et_book_category, et_city;
        et_book_category = findViewById(R.id.et_book_category);
        et_city = findViewById(R.id.et_city);
        Button submit_button = findViewById(R.id.find_donor_but);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String book_category = et_book_category.getText().toString();
                String city = et_city.getText().toString();
                if(isValid(book_category, city)){
                    get_search_results(book_category, city);
                }
            }
        });

    }


    private void get_search_results(final String book_category, final String city) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.search_donors, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //json response
                Intent intent = new Intent(SearchActivity.this, SearchResults.class);
                intent.putExtra("city", city);
                intent.putExtra("book_category", book_category);
                intent.putExtra("json", response);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchActivity.this, "Something went wrong:(", Toast.LENGTH_SHORT).show();
                Log.d("VOLLEY", Objects.requireNonNull(error.getMessage()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("city", city);
                params.put("book_category", book_category);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    private boolean isValid(String book_category, String city){
        List<String> valid_book_categry = new ArrayList<>();
        valid_book_categry.add("Phisics Related Books");
        valid_book_categry.add("Chemistry Related Books");
        valid_book_categry.add("Biology Related Books");
        valid_book_categry.add("Maths Related Books");
        valid_book_categry.add("History Related Books");
        valid_book_categry.add("Sinhala Related Books");
        valid_book_categry.add("Political Related Books");
        valid_book_categry.add("Accounts Related Books");
        valid_book_categry.add("Business Studies Related Books");
        valid_book_categry.add("Logic Related Books");
        valid_book_categry.add("Geography Related Books");

        if(!valid_book_categry.contains(book_category)){
            showMsg("Book category invalid choose from " + valid_book_categry);
            return false;
        }else if(city.isEmpty()){
            showMsg("Enter city");
            return false;
        }
        return true;
    }


    private void showMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }



}