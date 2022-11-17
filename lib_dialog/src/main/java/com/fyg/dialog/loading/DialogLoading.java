package com.fyg.dialog.loading;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.fyg.dialog.BaseDialog;
import com.fyg.dialog.R;

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
public class DialogLoading extends BaseDialog {

    private Activity mParentActivity;
    private ImageView mCircle;

    private onDialogClickBackListener mListener;
    private Animation mLoadingAnim;

    public DialogLoading(Activity activity) {
        super(activity, R.style.customDialogStyleNotDim);
        mParentActivity = activity;
    }

    public void setOnDialogClickBackListener(onDialogClickBackListener listener) {
        this.mListener = listener;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        mCircle = (ImageView) findViewById(R.id.loading_circle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mCircle.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        mLoadingAnim = AnimationUtils.loadAnimation(mParentActivity, R.anim.loading_anim);
        mCircle.setAnimation(mLoadingAnim);
        setCancelable(true);
        setCanceledOnTouchOutside(false);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            event.startTracking();
            if (DialogLoading.this != null && DialogLoading.this.isShowing() && mListener != null) {
                mListener.onDialogClickBack();
            }
            return true;
        }
        return false;
    }

    public interface onDialogClickBackListener {
        public void onDialogClickBack();
    }

    @Override
    public void dismiss() {
        if (mParentActivity != null && !mParentActivity.isFinishing()) {
            super.dismiss();

            if (mCircle != null){
                mCircle.setLayerType(View.LAYER_TYPE_NONE, null);
            }

            if (mLoadingAnim != null) {
                mLoadingAnim.cancel();
            }
        }
    }

    @Override
    public void show() {
        if (mParentActivity != null && !mParentActivity.isFinishing()) {
            super.show();
            mLoadingAnim.reset();
            mCircle.setAnimation(mLoadingAnim);
            mLoadingAnim.start();
        }
    }
}
