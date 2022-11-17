package com.fyg.dialog;

import android.app.Activity;
import android.app.Dialog;

/**
 * Created by fuyuguang on 2022/11/7 3:18 PM.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
    []()
    []()
    
 */
public class BaseDialog extends Dialog {

    public BaseDialog(Activity activity, boolean cancelable, OnCancelListener cancelListener) {
        super(activity, cancelable, cancelListener);
    }

    public BaseDialog(Activity activity, int theme) {
        super(activity, theme);
    }

    public BaseDialog(Activity activity) {
        super(activity);
    }

    @Override
    public void show() {
        try {
            super.show();
        } catch (Exception e) {

        }
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {

        }
    }

    @Override
    public void cancel() {
        try {
            super.cancel();
        } catch (Exception e) {

        }
    }

    public void noCancel(boolean isCancel){
        try {
            super.setCancelable(!isCancel);
        } catch (Exception e) {

        }
    }

}
