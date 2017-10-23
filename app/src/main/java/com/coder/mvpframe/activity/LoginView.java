package com.coder.mvpframe.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.coder.mvpframe.R;
import com.coder.mvpframe.base.IPresenter;
import com.coder.mvpframe.base.IView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by feng on 2017/10/23.
 */
public class LoginView implements IView {

    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;

    @Override
    public void attachView(IPresenter presenter, View view) {
        ButterKnife.bind(this, view);
    }

    public String getAccount() {
        return etAccount.getText().toString().trim();
    }

    public String getPwd() {
        return etPwd.getText().toString().trim();
    }

}
