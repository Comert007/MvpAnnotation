package com.coder.mvpframe.base;

/**
 * Created by feng on 2017/10/23.
 */
public interface IPresenter<V,M> {

    void setView(V v);

    V getView();

    void setModel(M m);

    M getModel();

}
