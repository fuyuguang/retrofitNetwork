package com.fyg.networklib.retrofit;

import android.text.TextUtils;

import com.fyg.networklib.IGenerateParam;
import com.fyg.networklib.IbuildMap;

import java.util.Map;
import java.util.Map.Entry;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.HttpUrl.Builder;
import okhttp3.MultipartBody;
import okhttp3.Request;

/**
 * Created by fuyuguang on 2022/7/8 11:33 上午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
    []()
    []()

 */
public class GetGenerateParam implements IGenerateParam {

	private final IbuildMap mBuildMap;

	public GetGenerateParam(IbuildMap buildMap){
		this.mBuildMap = buildMap;
	}

	@Override
	public Request generateParam(Request request) {
//		return generateParam(request,buildGenerateParams());
		return generateParam(request,mBuildMap != null? mBuildMap.buildMap() : null);
	}

	public Request generateParam(Request request,Map<String, String> generateParams){

		Request.Builder requestBuilder = request.newBuilder();
		if ("GET".equals(request.method())){
			Builder modifiedUrl = request.url().newBuilder();
			buildGetParam(modifiedUrl,generateParams);
			return requestBuilder.url(modifiedUrl.build()).build();
		}
		else if ("POST".equals(request.method())){

			FormBody.Builder formBodyBuilder = new FormBody.Builder();
			if (request.body() instanceof FormBody) {
				FormBody formBody = (FormBody) request.body();
				for (int i = 0; i < formBody.size(); i++) {
					formBodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
				}

				buildPostParam(formBodyBuilder,generateParams);
				return requestBuilder.post(formBodyBuilder.build()).build();

			}else if (request.body() instanceof MultipartBody){
//				MultipartBody multipartBody = (MultipartBody) request.body();
//				List<Part> list = multipartBody.parts();
//				for (int i = 0; i < multipartBody.size(); i++) {
//				}
				/** 自定义的RequestBody 原路返回，防止数据丢失  */
				return request;
			}else{
				/** 自定义的RequestBody 原路返回，防止数据丢失  */
				return request;
			}
//			buildPostParam(formBodyBuilder,generateParams);
//			return requestBuilder.post(formBodyBuilder.build()).build();
		}
		return requestBuilder.build();
	}

	public Builder buildGetParam(Builder httpUrlBuilder, Map<String, String> generateParams){

		if (httpUrlBuilder != null && generateParams != null && !generateParams.isEmpty()){
			for (Entry<String,String> entry : generateParams.entrySet()) {
				httpUrlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
			}
		}
		return httpUrlBuilder;
	}


	public FormBody.Builder buildPostParam(FormBody.Builder formBodyBuilder,Map<String, String> generateParams){
		if (formBodyBuilder != null && generateParams != null && !generateParams.isEmpty()){
			for (Entry<String, String> entry : generateParams.entrySet()) {
				formBodyBuilder.add(entry.getKey(), TextUtils.isEmpty(entry.getValue()) ? "" : entry.getValue());
			}
		}
		return formBodyBuilder;
	}



//	protected Map<String, String> buildGenerateParams() {
//
//		Map<String, String> map = new TreeMap<>();
//		map.put(BaseApi.KEY_APP_VERSION, BuildInfoHelper.getVersionName());
//		map.put(BaseApi.KEY_DEVICE_TYPE, BaseApi.VALUE_DEVICE_TYPE);
//		map.put(BaseApi.KEY_CHANNEL_ID, BuildInfoHelper.getChannel());
//		map.put(BaseApi.KEY_APP_SHUZLM_ID, CheckDeviceType.getUDID());
//		map.put(BaseApi.KEY_APP_SHUZLM_TYPE, CheckDeviceType.getDeviceType());
//		map.put(BaseApi.KEY_APP_KEY, CheckDeviceType.getUDID());
//		map.put(BaseApi.KEY_USER_TOKEN, BuildInfoHelper.getToken());
//		map.put(BaseApi.KEY_AREA_ID, JZKUserDataHelper.getJZKCityCode());
//		map.put(BaseApi.KEY_SYSTEM_VERSION, BuildInfoHelper.getSystemVersion());
//		map.put(BaseApi.KEY_SCREEN_RESLOLUTION, BuildInfoHelper.getScreenReslolution());
//		map.put(BaseApi.KEY_DEVICE_MODE, BuildInfoHelper.getDeviceMode());
//
//		if (!TextUtils.isEmpty(Util.getPushToken())) {
//			map.put(BaseApi.KEY_PUSH_TOKEN, Util.getPushToken());
//		}
//
//		if (!TextUtils.isEmpty(Util.getPushChannlCode())) {
//			map.put(BaseApi.KEY_PUSH_CHANNEL_CODE, Util.getPushChannlCode());
//		}
//
//
//		map.put(BaseApi.KEY_SUPPORTWEBP, (BaseApi.isWebpConfigEnable() && BuildInfoHelper.isSupportWebp()) ? WebpHelper.ENABLE : WebpHelper.DISABLE);
//		map.put(BaseApi.KEY_NET, BuildInfoHelper.getNetEnv());
//
////		if (BaseApi.isSupport304Cache() && !BaseApi.isForceRefresh()) {
////			modifiedUrl.addQueryParameter(BaseApi.KEY_ETAG, BaseApi.getApiETag());  //    getApiETag
////		}
//
//		map.put(BaseApi.KEY_CITY_CODE, UserDataHelper.getNowCityCode());
//		map.put(BaseApi.KEY_LNG, UserDataHelper.getNowLongitude());
//		map.put(BaseApi.KEY_LAT, UserDataHelper.getNowLatitude());
//		if (!StringUtils.isEmpty(JZKUserDataHelper.getJZKLat())) {
//			map.put(BaseApi.KEY_LATI, JZKUserDataHelper.getJZKLat());
//		}
//		if (!StringUtils.isEmpty(JZKUserDataHelper.getJZKLat())) {
//			map.put(BaseApi.KEY_LONGI, JZKUserDataHelper.getJZKLon());
//		}
//
//
//		map.put("xlsAddressId", UserInfoHelper.getXlsAddressId());
//		if (UserDataHelper.getDepartmentId() != 0) {
//			map.put(BaseApi.KEY_DEPARTMENT_ID, String.valueOf(UserDataHelper.getDepartmentId()));
//		}
//
//
//		return map;
//	}



}