package com.fennecdo.repository

import com.fennecdo.api.ApiService
import com.fennecdo.models.*

class TodoRepository(private val apiService: ApiService) {

    suspend fun sync(): Result<SyncResponse> {
        return try {
            val response = apiService.sync()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to sync: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createTodo(title: String, priority: String, tags: List<String> = emptyList()): Result<Todo> {
        return try {
            val response = apiService.createTodo(CreateTodoRequest(title, priority, tags))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to create todo"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateTodo(todo: Todo): Result<Todo> {
        return try {
            val request = UpdateTodoRequest(
                title = todo.title,
                priority = todo.priority,
                completed = todo.completed,
                tags = todo.tags.map { it.id }
            )
            val response = apiService.updateTodo(todo.id, request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to update todo"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteTodo(id: String): Result<Unit> {
        return try {
            val response = apiService.deleteTodo(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to delete todo"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createTag(name: String, color: String): Result<Tag> {
        return try {
            val response = apiService.createTag(CreateTagRequest(name, color))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to create tag"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteTag(id: String): Result<Unit> {
        return try {
            val response = apiService.deleteTag(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to delete tag"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
