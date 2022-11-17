package com.database



import androidx.lifecycle.LiveData
import androidx.room.*
import com.database.data.model.Note
import kotlinx.coroutines.flow.Flow



@Dao
interface NoteDao {

    /** 错误: Type of the parameter must be a class annotated with @Entity or a collection/array of it.
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);  */
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertNote(note: Note)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    /*suspend*/ fun insertNote(notes: List<Note>)


    @Update(onConflict = OnConflictStrategy.REPLACE)
    /*suspend*/ fun updateNote(note: Note)

    @Delete
    /*suspend*/ fun deleteNote(note: Note)

    @Query("SELECT * FROM ${Note.TABLE_NAME} ORDER by id DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM ${Note.TABLE_NAME} ORDER by id DESC")
    fun getAllNotesWithLiveData(): LiveData<List<Note>>

    @Query("SELECT * FROM ${Note.TABLE_NAME} WHERE id = :id")
    fun getNoteByID(id: Int): Flow<Note>

    @Query("DELETE FROM ${Note.TABLE_NAME} WHERE id = :id")
    /*suspend*/ fun deleteNoteByID(id: Int)


    /** 删除表  */
    @Query("DELETE FROM ${Note.TABLE_NAME}")
    fun deleteAll()
}