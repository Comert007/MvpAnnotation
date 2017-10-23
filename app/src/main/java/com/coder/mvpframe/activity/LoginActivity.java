package com.coder.mvpframe.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.coder.mvpframe.R;
import com.coder.mvpframe.annotation.Layout;
import com.coder.mvpframe.annotation.MVAnnotation;
import com.coder.mvpframe.base.PresenterActivity;
import com.google.gson.Gson;

import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by feng on 2017/10/23.
 */
@MVAnnotation(getModelClz = LoginModel.class,getViewClz = LoginView.class)
@Layout(layoutResId = R.layout.activity_login)
public class LoginActivity extends PresenterActivity<LoginView,LoginModel> {

    @OnClick(R.id.btn_login)
    public void onLoginClicked(View v) {

        String account = this.v.getAccount();
        String pwd = this.v.getPwd();

        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "请输入账号和密码", Toast.LENGTH_SHORT).show();
            return;
        }

        showProgress();
        m.login(account, pwd)
                .enqueue(new Callback<Gson>() {

                    @Override
                    public void onResponse(Call<Gson> call, Response<Gson> response) {
                        dismissProgress();
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Gson> call, Throwable t) {
                        dismissProgress();
                        Toast.makeText(LoginActivity.this, "登录失败" + t.toString(), Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });
    }

}