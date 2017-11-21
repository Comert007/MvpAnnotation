package com.coder.mvpframe.base;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.coder.mvpframe.BaseApplication;
import com.coder.mvpframe.annotation.Layout;
import com.coder.mvpframe.annotation.MVAnnotation;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import butterknife.ButterKnife;

/**
 * Created by feng on 2017/10/23.
 */

public class PresenterActivity<V extends IView, M> extends AppCompatActivity implements
        IPresenter<V, M> {

    protected V v;
    protected M m;

    protected Dialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        beforeCreate(savedInstanceState);
        super.onCreate(savedInstanceState);

        Class<? extends PresenterActivity> aClass = this.getClass();
        Layout annotation = aClass.getAnnotation(Layout.class);
        if (annotation != null) {
            int layoutResId = annotation.layoutResId();
            setContentView(layoutResId);
        }

        ButterKnife.bind(this);
//        this.createRelationshipClass();
        this.createMV();

        afterCreate(savedInstanceState);
    }

    /**
     * onCreate 方法调用之前
     *
     * @param savedInstanceState
     */
    protected void beforeCreate(Bundle savedInstanceState) {
    }

    /**
     * onCreate 方法调用之后
     *
     * @param savedInstanceState
     */
    protected void afterCreate(Bundle savedInstanceState) {
    }

    protected void showProgress() {
        dismissProgress();
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
    }

    protected void dismissProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


    protected <T> T createRetrofitService(Class<T> service) {
        return BaseApplication.getInstance().createRetrofitService(service);
    }


    private void createMV() {
        final IPresenter presenter = this;
        IView view = null;
        Object model = null;

        Class<? extends PresenterActivity> aClass = this.getClass();
        MVAnnotation annotation = aClass.getAnnotation(MVAnnotation.class);

        String canonicalV = annotation.getViewClz().getCanonicalName();
        String canonicalM = annotation.getModelClz().getCanonicalName();

        try {
            view = (IView) Class.forName(canonicalV).newInstance();
            Class modelClz = Class.forName(canonicalM);
            if (modelClz.getClass().isAssignableFrom(IModel.class)) {
                model = modelClz.newInstance();
            } else {
                model = createRetrofitService(modelClz);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (view != null) {
            view.attachView(presenter, findViewById(android.R.id.content));
            presenter.setView(view);
        }

        if (model != null) {
            presenter.setModel(model);
        }

    }

    /**
     * 创建关系类
     */
    private void createRelationshipClass() {
        final IPresenter presenter = this;
        IView view = null;
        Object model = null;

        Class<? extends PresenterActivity> clz = this.getClass();
        try {
            Type genericSuperclass = clz.getGenericSuperclass();
            if (genericSuperclass instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                if (actualTypeArguments.length == 2) {
                    Type viewType = actualTypeArguments[0];// view
                    if (viewType instanceof Class) {
                        view = (IView) ((Class) viewType).newInstance();
                    }
                    Type modelType = actualTypeArguments[1];// model
                    if (IModel.class.isAssignableFrom((Class) modelType)) {
                        model = null;
                    } else {
                        model = createRetrofitService((Class) modelType);
                    }
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setView(V v) {
        this.v = v;
    }

    @Override
    public V getView() {
        return v;
    }

    @Override
    public void setModel(M m) {
        this.m = m;
    }

    @Override
    public M getModel() {
        return m;
    }

}
