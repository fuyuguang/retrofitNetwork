package com.fyg.networklib;

/**
 * Created by fuyuguang on 2022/9/13 5:01 下午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */
public class NetConfig {

    //
    public ITokenExpired mITokenExpired;
    public IbuildMap mIbuildMap;
    public IShowToast mShowToast;
    public String mBaseUrl;
    public boolean isDebug;

    public NetConfig() {
    }


    public NetConfig(ITokenExpired tokenExpired, IbuildMap buildMap, IShowToast showToast,String mBaseUrl) {
        mITokenExpired = tokenExpired;
        mIbuildMap = buildMap;
        mShowToast = showToast;
        this.mBaseUrl = mBaseUrl;
    }




    public static <T> T requireNonNull(T obj, String message) {
        if (obj == null)
            throw new NullPointerException(message);
        return obj;
    }

    public static final class Builder {
        private ITokenExpired mITokenExpired;
        private IbuildMap mIbuildMap;
        private IShowToast mShowToast;
        private String mBaseUrl;


        public Builder() {

        }

        public Builder baseUrl(String baseUrl) {
            this.mBaseUrl =  baseUrl;
            return this;
        }

        public Builder tokenExpired(ITokenExpired tokenExpired) {
            this.mITokenExpired = requireNonNull(tokenExpired, "tokenExpired == null");
            return this;
        }

        public Builder buildMap(IbuildMap buildMap) {
            this.mIbuildMap = requireNonNull(buildMap, "buildMap == null");
            return this;
        }

        public Builder showToast(IShowToast mShowToast) {
            this.mShowToast = requireNonNull(mShowToast, "mShowToast == null");
            return this;
        }


        public NetConfig build() {
            return new NetConfig(
                    mITokenExpired,
                    mIbuildMap,
                    mShowToast,mBaseUrl
            );
        }
    }



}
