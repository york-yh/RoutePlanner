package com.example.y.routeplanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.y.routeplanner.gson.ResponseData;
import com.example.y.routeplanner.gson.User;
import com.example.y.routeplanner.util.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;



public class LoadActivity extends BaseActivity {
    private EditText telInput, passwordInput;
    private String tel = "", password = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_load);
        telInput = findViewById(R.id.tel);
        passwordInput = findViewById(R.id.passwd);
        Button load = findViewById(R.id.load_button);
        TextView register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoadActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tel = telInput.getText().toString();
                password = passwordInput.getText().toString();
                Log.i(TAG, "onClick: "+tel+"    "+password);
                if (!tel .equals("") && !password.equals("")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //登陆
                            OkHttpClient client = new OkHttpClient();
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("account", tel)
                                    .add("password", password)
                                    .build();
                            Request request = new Request.Builder()
                                    .url("http://120.77.170.124:8080/busis/user/login.do")
                                    .post(requestBody)
                                    .build();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String json = response.body().string();
                                    ResponseData responseData=new Gson().fromJson(json,new TypeToken<ResponseData<User>>(){}.getType());
                                    Log.i(TAG, "login: "+json);
                                    if (responseData.getCode()==1) {
                                        User user = (User) responseData.getData();
                                        new Util().login(LoadActivity.this,user);
                                    }else {
                                        sendMessage("用户名或密码错误！");
                                    }
                                }
                            });
                        }
                    });
                }else{
                    Toast.makeText(LoadActivity.this, "请输入登陆信息！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}