package com.database

import androidx.room.migration.Migration

/**
 * Created by fuyuguang on 2022/10/20 9:02 AM.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */
interface IMigration {


    fun getMigrations() : List<Migration>

    fun getDatabaseChang(): String

    companion object{
        const val getVersion:Int = 0
    }
}