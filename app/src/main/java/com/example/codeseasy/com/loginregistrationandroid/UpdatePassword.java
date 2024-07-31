package com.example.codeseasy.com.loginregistrationandroid;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class UpdatePassword extends AppCompatActivity {
    EditText editTextOldPassword, editTextNewPassword, editTextConfirmNewPassword;
    Button buttonUpdatePassword;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_password);

        editTextOldPassword = findViewById(R.id.old_password);
        editTextNewPassword = findViewById(R.id.new_password);
        editTextConfirmNewPassword = findViewById(R.id.confirm_new_password);
        buttonUpdatePassword = findViewById(R.id.update_password);
        sharedPreferences = getSharedPreferences("ApiKeys Grupo 5", MODE_PRIVATE);

        buttonUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPassword = editTextOldPassword.getText().toString();
                String newPassword = editTextNewPassword.getText().toString();
                String confirmNewPassword = editTextConfirmNewPassword.getText().toString();

                if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
                    Toast.makeText(UpdatePassword.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newPassword.equals(confirmNewPassword)) {
                    Toast.makeText(UpdatePassword.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    return;
                }

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://192.168.1.22/Grupo5_ApiKeys/update_password.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("Satisfactorio")) {
                                    Toast.makeText(UpdatePassword.this, "Contraseña actualizada con éxito", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), Login.class);
                                    startActivity(intent);
                                    finish();
                                } else
                                    Toast.makeText(UpdatePassword.this, response, Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("email", getIntent().getStringExtra("email"));
                        paramV.put("apiKey", getIntent().getStringExtra("apiKey"));
                        paramV.put("old_password", oldPassword);
                        paramV.put("new_password", newPassword);
                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }
        });
    }
}
