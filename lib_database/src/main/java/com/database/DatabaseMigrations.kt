package com.database
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

import com.database.IMigration.Companion.getVersion
import com.database.data.model.Note


object DatabaseMigrations {
//    const val DB_VERSION = 2
    /** Room cannot verify the data integrity .Looks like you`ve changed  */
    const val DB_VERSION = 3

    val MIGRATIONS: Array<Migration>
        get() = arrayOf<Migration>(
            MIGRATION_1_TO_2
        )




    /**
        1.addMigrations(Migration migrations...):一个迁移可以处理多个版本
        2.Migration(int startVersion, int endVersion):每次迁移都可以在定义的两个版本之间移动,初始版本和目标版本
        3.在重写的migrate方法中执行更新的sql,同时需要在对应的Entity类中添加相同的字段,来保证字段相同
     * */
    private var MIGRATION_1_TO_2: Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
//            database.execSQL("ALTER TABLE ${Note.TABLE_NAME} ADD COLUMN body TEXT")
            database.execSQL("ALTER TABLE ${Note.TABLE_NAME} ADD COLUMN createdTime INTEGER NOT NULL DEFAULT ${System.currentTimeMillis()}")
        }
    }







    open class Version1() : IMigration {
//        override var getVersion: Int = 1
        override fun getMigrations(): MutableList<Migration> = mutableListOf()
        override fun getDatabaseChang(): String {
            return "V${getVersion} : no change"
        }
    }

    class Version2 : Version1(){
//        override fun getVersion(): Int  = 2

//        override var getVersion: Int
//            get() = 2
//            set(value) {}
        override fun getMigrations(): MutableList<Migration>{

            val migrations = super.getMigrations();
            migrations.add(MIGRATION_1_TO_2)
            return migrations
        }

        override fun getDatabaseChang(): String {
            return "V${getVersion} : no change"
        }
    }

}
