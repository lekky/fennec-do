package com.fennecdo.models

import com.google.gson.annotations.SerializedName

data class Todo(
    val id: String,
    val title: String,
    val priority: String,
    val completed: Boolean,
    @SerializedName("created_at")
    val createdAt: Long,
    @SerializedName("updated_at")
    val updatedAt: Long,
    val tags: List<Tag> = emptyList()
)

data class Tag(
    val id: String,
    val name: String,
    val color: String,
    @SerializedName("created_at")
    val createdAt: Long,
    @SerializedName("updated_at")
    val updatedAt: Long
)

data class CreateTodoRequest(
    val title: String,
    val priority: String,
    val tags: List<String> = emptyList()
)

data class UpdateTodoRequest(
    val title: String,
    val priority: String,
    val completed: Boolean,
    val tags: List<String> = emptyList()
)

data class CreateTagRequest(
    val name: String,
    val color: String
)

data class SyncResponse(
    val todos: List<Todo>,
    val tags: List<Tag>
)
