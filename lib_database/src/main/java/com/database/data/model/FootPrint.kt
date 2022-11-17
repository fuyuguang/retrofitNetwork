package com.database.data.model
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = FootPrint.TABLE_NAME)
data class FootPrint(

    /** 内容  */
    @NonNull @ColumnInfo(name = "productId", typeAffinity = ColumnInfo.TEXT)
    var productId: String,

) : Serializable {

    /** 上面代码指定了 uid做为主键，并且设置了自增，但是该属性放在构造方法的位置，书实例化User 这个对象的时候，这个 uid 的值是必须填写的。所以要不指定主键，让其自增，该如下实现  */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id", typeAffinity = ColumnInfo.INTEGER)
    var id: Int = 0

    companion object {
        const val TABLE_NAME = "all_footprints"
    }
}
//Room cannot verify the data integrity .Looks like you`ve changed