package com.mvvm.baseapp.repos

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flow

/**
 * Created by fuyuguang on 2022/10/26 11:57 AM.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */
//abstract class BaseRepository(var remoteRepo: RemoteRepo?, var localRepo: LocalRepo?)  {
abstract class BaseRepository()  {

    /**
     *  T 可指代 BaseResponse<T>
     *  <T> createResourceFlow(t: suspend () -> BaseResponse<T>): Flow<BaseResponse<T>>
     *  */
    inline fun <reified T> createFlow(noinline t: suspend BaseRepository.() -> T): Flow<T> {
        return flow {
            emit(t())
        }.cancellable()
    }



}