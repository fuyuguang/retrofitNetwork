package com.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.database.DatabaseMigrations.Version2
import com.database.data.model.FootPrint
import com.database.data.model.Note
import com.database.provider.DataBaseProvider


@Database(
    entities = [Note::class, FootPrint::class],
    /** 所谓编译时常量就是在编译阶段已经可以确定其值的常量。*/
//    version = Migration.,
    version = DatabaseMigrations.DB_VERSION,

    /**
    警告: Schema export directory is not provided to the annotation processor so we cannot export the schema. You can either provide `room.schemaLocation` annotation processor argument OR set exportSchema to false.
    a1,未向批注处理器提供架构导出目录，因此无法导出架构。你可以提供room.schemaLocation注释处理器参数或将exportSchema设置为false

    a2,警告: 未向注释处理器提供架构导出目录，因此无法导出架构。你可以提供空间。schemaLocation`注释处理器参数或将exportSchema设置为false。

    //https://blog.csdn.net/guaisou/article/details/117108362
    Schema是数据库的组织和结构，exportSchema指暴露数据库的组织架构到一个文件夹，这个文件夹通过room.schemaLocation指定。
    Schema记录了数据库的组织和结构，并带有版本信息，所以不适合在发布的app中的文件夹中，而是最好指定到版本控制系统中，
    默认为true打开状态。所以系统编译是，提醒你。

        []()
        */
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getNoteDao(): NoteDao
    abstract fun getFootPrintDao(): FootPrintDao



    companion object {
        private const val DB_NAME = "jx_database"
        @JvmStatic
          val  Migration : IMigration = Version2()

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(): AppDatabase {
            return getInstance(DataBaseProvider.mContext)
//            return getInstance(ContextUtil.getInstance())
        }

        fun getInstance(context: Context): AppDatabase {

            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                        /** 升级数据库  */
                .addMigrations(*DatabaseMigrations.MIGRATIONS)
//                .addMigrations(*Migration.getMigrations().toTypedArray())
                        /** 发生错误时直接删除旧版数据，重新创建  */
                    .fallbackToDestructiveMigration()
                        /** 可以在主线程操作  */
//                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
