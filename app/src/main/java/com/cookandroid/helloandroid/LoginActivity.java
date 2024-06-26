package com.cookandroid.helloandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.helloandroid.model.UserDTO;
import com.cookandroid.helloandroid.retrofit.RetrofitService;
import com.cookandroid.helloandroid.retrofit.UserAPI;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        initializeComponents();
    }

    private void initializeComponents() {
        EditText inputEditID = findViewById(R.id.edit_id);
        EditText inputEditPassword = findViewById(R.id.edit_pw);

        Button buttonLogin = findViewById(R.id.button_login);

        RetrofitService retrofitService = new RetrofitService();
        UserAPI userAPI = retrofitService.getRetrofit().create(UserAPI.class);

        buttonLogin.setOnClickListener(view -> {
            String id = String.valueOf(inputEditID.getText());
            String password = String.valueOf(inputEditPassword.getText());

            UserDTO userDTO = new UserDTO();
            userDTO.setId(id);
            userDTO.setPassword(password);

            userAPI.login(userDTO).enqueue(new Callback<UserDTO>() {
                @Override
                public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        UserDTO result = response.body();
                        if ("Login successful".equals(result.getLoginMessage())) {
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class); // MainActivity로 이동
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, result.getLoginMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Login failed: Invalid response", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<UserDTO> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_LONG).show();
                    Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE, "Error occurred" + t.getMessage(), t);
                }
            });
        });
    }
}
