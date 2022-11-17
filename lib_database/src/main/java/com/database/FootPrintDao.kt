package com.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.database.data.model.FootPrint
import com.database.provider.DataBaseProvider

import kotlinx.coroutines.flow.Flow


@Dao
//interface FootPrintDao : DaoRepo {
interface FootPrintDao /*: LocalRepo */{

    /** 错误: Type of the parameter must be a class annotated with @Entity or a collection/array of it.
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);  */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    /*suspend*/ fun insertFootPrint(footPrints: List<FootPrint>)




    @Transaction
    /*suspend*/ fun insertAndDeleteFootPrint(footPrints: List<FootPrint>){

        /** 先插入再删除，简单快捷  */
        insertFootPrint(footPrints);

        var allFootPrints = getAllFootPrintsWithRealType()
        var upperLimitCount = 50
        if (DataBaseProvider.Debug){
             upperLimitCount = 5
        }
        if (allFootPrints?.size >  upperLimitCount){
            var duoyuItem = allFootPrints?.size - upperLimitCount
            for (index in 0 until duoyuItem){
                deleteFootPrintByID(allFootPrints[index].id)
            }
        }
    }

    @Delete
    /*suspend*/ fun deleteNote(note: FootPrint)

//    @Query("SELECT * FROM ${FootPrint.TABLE_NAME} ORDER by id DESC")
    @Query("SELECT * FROM ${FootPrint.TABLE_NAME}")
    fun getAllFootPrintsWithRealType(): List<FootPrint>

    @Query("SELECT * FROM ${FootPrint.TABLE_NAME} ORDER by id DESC")
    fun getAllFootPrints(): Flow<List<FootPrint>>

    @Query("SELECT * FROM ${FootPrint.TABLE_NAME} ORDER by id DESC")
    fun getAllFootPrintsWithLiveData(): LiveData<List<FootPrint>>

    @Query("SELECT * FROM ${FootPrint.TABLE_NAME} WHERE id = :id")
    fun getFootPrintByID(id: Int): Flow<FootPrint>

    @Query("DELETE FROM ${FootPrint.TABLE_NAME} WHERE id = :id")
    /*suspend*/ fun deleteFootPrintByID(id: Int)


    /** 删除表  */
    @Query("DELETE FROM ${FootPrint.TABLE_NAME}")
    fun deleteAll()
}