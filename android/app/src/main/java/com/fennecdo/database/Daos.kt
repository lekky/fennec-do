package com.fennecdo.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TagDao {
    @Query("SELECT * FROM tags ORDER BY createdAt DESC")
    fun getAllTags(): LiveData<List<TagEntity>>

    @Query("SELECT * FROM tags ORDER BY createdAt DESC")
    suspend fun getAllTagsList(): List<TagEntity>

    @Query("SELECT * FROM tags WHERE id = :tagId")
    suspend fun getTagById(tagId: String): TagEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: TagEntity)

    @Update
    suspend fun updateTag(tag: TagEntity)

    @Delete
    suspend fun deleteTag(tag: TagEntity)

    @Query("DELETE FROM tags WHERE id = :tagId")
    suspend fun deleteTagById(tagId: String)
}

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos ORDER BY createdAt DESC")
    suspend fun getAllTodos(): List<TodoEntity>

    @Query("SELECT * FROM todos WHERE id = :todoId")
    suspend fun getTodoById(todoId: String): TodoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: TodoEntity)

    @Update
    suspend fun updateTodo(todo: TodoEntity)

    @Delete
    suspend fun deleteTodo(todo: TodoEntity)

    @Query("DELETE FROM todos WHERE id = :todoId")
    suspend fun deleteTodoById(todoId: String)
}

@Dao
interface TodoTagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoTag(todoTag: TodoTagCrossRef)

    @Delete
    suspend fun deleteTodoTag(todoTag: TodoTagCrossRef)

    @Query("DELETE FROM todo_tags WHERE todoId = :todoId")
    suspend fun deleteTagsForTodo(todoId: String)

    @Query("SELECT * FROM tags WHERE id IN (SELECT tagId FROM todo_tags WHERE todoId = :todoId)")
    suspend fun getTagsForTodo(todoId: String): List<TagEntity>

    @Transaction
    @Query("SELECT * FROM todos ORDER BY createdAt DESC")
    suspend fun getTodosWithTags(): List<TodoWithTagsResult>
}

// Result class for Room transaction
data class TodoWithTagsResult(
    @Embedded val todo: TodoEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            TodoTagCrossRef::class,
            parentColumn = "todoId",
            entityColumn = "tagId"
        )
    )
    val tags: List<TagEntity>
)
