package com.example.bloodbank.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

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

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameEt, cityEt, bookGroupEt, passwordEt, mobileEt;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameEt = findViewById(R.id.name);
        cityEt = findViewById(R.id.city);
        bookGroupEt = findViewById(R.id.book_category);
        passwordEt = findViewById(R.id.password);
        mobileEt = findViewById(R.id.contact_no);
        registerButton = findViewById(R.id.registerbut);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, nic, city, book_category, password = "", contact_no;
                name = nameEt.getText().toString();
                city = cityEt.getText().toString();
                book_category = bookGroupEt.getText().toString();
                password = passwordEt.getText().toString();
                contact_no = mobileEt.getText().toString();

                //showMessage(name+"\n"+city+"\n"+blood_group+"\n"+password+"\n"+contact_no);

                if(isValid(name, city, book_category, password, contact_no)){
                    register(name, city, book_category, password, contact_no);
                }
            }
        });

    }

//    private String encrypt(String password) throws Exception{
//
//        SecretKeySpec key = generateKey(password);
//    }
//
//    private SecretKeySpec generateKey(String password) throws Exception{
//        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
//        byte[] bytes = password.getBytes("UTF-8");
//        digest.update(bytes, 0, bytes.length);
//        byte[] key = digest.digest();
//        SecretKeySpec secretKeySpec = new SecretKeySpec("AES");
//    }


    private void register(final String name, final String city, final String book_category, final String password, final String contact_no){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.register_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("Success")){
                    //--newly added in 12
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("city", city).apply();
                    //--/newly added in 12
                    Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    //--newly added in 10-2
                    //PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("number", contact_no).apply();
                    //--/newly added in 10-2
                    RegisterActivity.this.finish();
                }else{
                    Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this,"something went wrong:(", Toast.LENGTH_SHORT).show();
                Log.d("VOLLEY", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("city", city);
                params.put("book_category", book_category);
                params.put("password", password);
                params.put("number", contact_no);

                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private boolean isValid(String name, String city, String book_category, String password, String contact_no){

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

        if(name.isEmpty()){
            showMessage("Name is Empty!");
            return false;
        }else if(city.isEmpty()){
                showMessage("City is Required!");
                return false;
        }else if(!valid_book_categry.contains(book_category)){
            showMessage("Book Category Invalid choose from "+ valid_book_categry);
            return false;
        }else if(contact_no.isEmpty() || contact_no.length() != 10){
            showMessage("Invalid Contact No, Please Check Again!");
            return false;
        }

        return true;
    }

    private void showMessage(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
