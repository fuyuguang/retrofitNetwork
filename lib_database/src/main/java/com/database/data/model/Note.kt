package com.database.data.model
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = Note.TABLE_NAME)
data class Note(

    /** 内容  */
    @NonNull @ColumnInfo(name = "date", typeAffinity = ColumnInfo.TEXT)
    var date: String,

    /** 版本2,添加创建时间  */
    @NonNull @ColumnInfo(name = "createdTime")
    var createdTime: Long =
        System.currentTimeMillis(),



) : Serializable {

    /** 上面代码指定了 uid做为主键，并且设置了自增，但是该属性放在构造方法的位置，书实例化User 这个对象的时候，这个 uid 的值是必须填写的。所以要不指定主键，让其自增，该如下实现  */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id", typeAffinity = ColumnInfo.INTEGER)
    var id: Int = 0

//    val createdAtDateFormat: String
//        get() = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT)
//            .format(createdTime)


    companion object {
        const val TABLE_NAME = "all_notes"
    }
}
