package com.example.viktoriasemina.webservicejson;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    EditText username, email, password;
    Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = findViewById(R.id.edtxtName);
        email = findViewById(R.id.edtxtEmail);
        password = findViewById(R.id.edtxtPassword);
        signUp = findViewById(R.id.btnSignUp);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uName = username.getText().toString().trim();
                String mail = email.getText().toString().trim();
                String pass = password.getText().toString().trim();

                String hashpass = null;

                try {
                    hashpass = hashPassword(pass);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                userSignUp(uName, mail, hashpass);
            }
        });

    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(password.getBytes(), 0, password.length());
        return new BigInteger(1, messageDigest.digest()).toString(16);
    }

     private  void userSignUp(final String uName, final String mail, final String pass){
         StringRequest stringRequest = new StringRequest(Request.Method.POST, Configure.REGISTER_URL,
                 new Response.Listener<String>() {
                     @Override
                     public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            boolean error = jsonObject.getBoolean("error");
                            if(!error){
                                JSONObject user = jsonObject.getJSONObject("User");
                                //getting items
                                String uName = user.getString("name");
                                String pass = user.getString("password");
                                toast("OK inserted");
                            }
                            else {
                                //IF there is an error, something's wrong with PHP
                                String errorMsg = jsonObject.getString("error_msg");
                                toast(errorMsg);
                            }
                        } catch (JSONException e){
                            //if something's wrong with json format
                            e.printStackTrace();
                            toast("Json error" + e.getMessage());
                         }
                     }
                 }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                    toast("Unknown error occured");
             }
         })

         {
             @Override
             protected Map<String, String> getParams() throws AuthFailureError {
                 Map<String,String> params = new HashMap<>();
                 params.put("name", uName);
                 params.put("email", mail);
                 params.put("password", pass);
                 return params;
             }
         };

         String myTag = "req_signup";

         //adding request to the request queue - Android request control
         AndroidLoginController.getmInstance().addToRequestQueue(stringRequest, myTag);

     }

     public void toast(String x){
         Toast.makeText(this, x,Toast.LENGTH_SHORT).show();
     }

    public void moveToSignInActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(intent);
    }
}
