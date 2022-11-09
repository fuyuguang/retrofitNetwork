package com.fyg.networklib;

/**
 * Created by fuyuguang on 2022/9/13 3:58 下午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */
public class NetWorkManager {


    public static void init(ITokenExpired tokenExpired, IbuildMap buildMap, IShowToast showToast,String baseUrl,IinitServerAddress initServerAddress,boolean isDebug) {
        if (initServerAddress != null){
            initServerAddress.initServerAddress();
        }

        Ktx.Companion.setDebugMode(isDebug);

        NetWorkManager instance = getInstance();
        NetConfig netConfig = new NetConfig();
        netConfig.mITokenExpired = tokenExpired;
        netConfig.mIbuildMap = buildMap;
        netConfig.mShowToast = showToast;
        netConfig.mBaseUrl = baseUrl;
        instance.mNetConfig = netConfig;
        netConfig.isDebug = isDebug;
    }



    public NetConfig getNetConfig() {
        return mNetConfig;
    }

    public void setNetConfig(NetConfig netConfig) {
        this.mNetConfig = netConfig;
    }

    public NetConfig mNetConfig;



//    public static void main(String[] args) {
//        NetWorkManager.init(new ITokenExpired() {
//            @Override
//            public boolean isExpired(BaseResponse<?> data) {
//                return false;
//            }
//
//            @Override
//            public void tokenExpiredHandler(BaseResponse<?> data) {
//
//            }
//        }, new IbuildMap() {
//            @Override
//            public Map<String, String> buildMap() {
//                return null;
//            }
//        }, new IShowToast() {
//            @Override
//            public void showShort(String data) {
//
//            }
//
//            @Override
//            public void showDebugShort(String data) {
//
//            }
//        },null,null);
//    }


    private NetWorkManager(){

    }
    private static class InnerSingleton {
        private static final NetWorkManager INSTANCE = new NetWorkManager();
    }

    public static NetWorkManager getInstance() {
        return InnerSingleton.INSTANCE;
    }
}
