package com.fyg.networklib.callback;

import com.fyg.networklib.BaseResponse;
import com.fyg.networklib.IBusinessHandler;
import com.fyg.networklib.NetWorkManager;
import com.fyg.networklib.callbacklib.exception.ParseNetThrowable;

/**
 * Created by fuyuguang on 2022/6/1 2:36 下午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */
public class BusinessHandler implements IBusinessHandler, IBusinessHandler.Factory {



    @Override
    public boolean isExpired(BaseResponse<?> data) {

        return NetWorkManager.getInstance().mNetConfig != null && NetWorkManager.getInstance().mNetConfig.mITokenExpired.isExpired(data);

//        return mITokenExpired != null && mITokenExpired.isExpired(data);
//        return ApiResponse.Companion.isTokenDated(data);
    }

    @Override
    public void tokenExpiredHandler(BaseResponse<?> data) {


        if (NetWorkManager.getInstance().mNetConfig != null){
            NetWorkManager.getInstance().mNetConfig.mITokenExpired.tokenExpiredHandler(data);
        }


//        if (mITokenExpired != null){
//            mITokenExpired.tokenExpiredHandler(data);
//        }
        //UserInfoManager.userTokenDatedHandle(data);
    }

    @Override
    public boolean onFailure(Throwable throwable) {

//        if (throwable != null) {
//            JXToast.showShort(ParseNetThrowable.parseThrowable(throwable).toString());
//        } else {
//            JXToast.showDebugShort("BaseCallback  onFailure method args throwable  is null");
//        }

        if (throwable != null) {
            NetWorkManager.getInstance().mNetConfig.mShowToast.showShort(ParseNetThrowable.parseThrowable(throwable).toString());
        } else {
            NetWorkManager.getInstance().mNetConfig.mShowToast.showDebugShort("BaseCallback  onFailure method args throwable  is null");
        }

        return true;
    }

    @Override
    public boolean onError(BaseResponse<?> data) {

        if (data == null){
            NetWorkManager.getInstance().mNetConfig.mShowToast.showShort("response is null !");
            return true;
        }
        NetWorkManager.getInstance().mNetConfig.mShowToast.showShort(data.getResponseMsg());
        return true;
    }

    @Override
    public void onNullResult(BaseResponse<?> data) {
        NetWorkManager.getInstance().mNetConfig.mShowToast.showShort("response result is null !");
    }


    private BusinessHandler() {
    }

    @Override
    public IBusinessHandler getBusinessHandler() {
        return getInstance();
    }

    private static class InnerSingleton {
        private static final BusinessHandler INSTANCE = new BusinessHandler();
    }

    public static BusinessHandler getInstance() {
        return InnerSingleton.INSTANCE;
    }

//    public static BusinessHandler getInstance() {
////        return InnerSingleton.INSTANCE;
//
//        if (INSTANCE == null){
//            synchronized (BusinessHandler.class){
//                if (INSTANCE == null){
//                    INSTANCE = new BusinessHandler();
//                }
//            }
//        }
//        return INSTANCE;
//    }

}
