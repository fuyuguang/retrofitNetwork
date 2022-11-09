package com.fyg.networklib;

import okhttp3.Request;

/**
 * @author fyg
 * @date : 2021/7/29
 * @E-Mail ：2355245065@qq.com
 * @Wechat :fyg13522647431
 * @Tel : 13522647431
 * @desc : 生成公参接口
 */
public interface IGenerateParam {
	Request generateParam(Request request);
}