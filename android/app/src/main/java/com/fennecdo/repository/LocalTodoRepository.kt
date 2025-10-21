package com.fennecdo.repository

import com.fennecdo.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalTodoRepository(
    private val todoDao: TodoDao,
    private val tagDao: TagDao,
    private val todoTagDao: TodoTagDao
) {

    suspend fun getAllTodosWithTags(): List<TodoWithTags> = withContext(Dispatchers.IO) {
        val todosWithTagsResult = todoTagDao.getTodosWithTags()
        todosWithTagsResult.map { result ->
            TodoWithTags(
                todo = result.todo,
                tags = result.tags
            )
        }
    }

    suspend fun createTodo(title: String, priority: String, tagIds: List<String> = emptyList()): TodoEntity {
        return withContext(Dispatchers.IO) {
            val todo = TodoEntity(
                title = title,
                priority = priority,
                completed = false
            )
            todoDao.insertTodo(todo)

            // Add tags
            tagIds.forEach { tagId ->
                todoTagDao.insertTodoTag(TodoTagCrossRef(todo.id, tagId))
            }

            todo
        }
    }

    suspend fun updateTodo(todo: TodoEntity, tagIds: List<String>? = null) {
        withContext(Dispatchers.IO) {
            val updatedTodo = todo.copy(updatedAt = System.currentTimeMillis())
            todoDao.updateTodo(updatedTodo)

            // Update tags if provided
            if (tagIds != null) {
                todoTagDao.deleteTagsForTodo(todo.id)
                tagIds.forEach { tagId ->
                    todoTagDao.insertTodoTag(TodoTagCrossRef(todo.id, tagId))
                }
            }
        }
    }

    suspend fun deleteTodo(todoId: String) {
        withContext(Dispatchers.IO) {
            todoTagDao.deleteTagsForTodo(todoId)
            todoDao.deleteTodoById(todoId)
        }
    }

    suspend fun getAllTags(): List<TagEntity> = withContext(Dispatchers.IO) {
        tagDao.getAllTagsList()
    }

    suspend fun createTag(name: String, color: String): TagEntity {
        return withContext(Dispatchers.IO) {
            val tag = TagEntity(
                name = name,
                color = color
            )
            tagDao.insertTag(tag)
            tag
        }
    }

    suspend fun deleteTag(tagId: String) {
        withContext(Dispatchers.IO) {
            tagDao.deleteTagById(tagId)
        }
    }

    suspend fun getTagsForTodo(todoId: String): List<TagEntity> = withContext(Dispatchers.IO) {
        todoTagDao.getTagsForTodo(todoId)
    }
}
