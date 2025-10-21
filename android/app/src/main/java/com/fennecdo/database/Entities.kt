package com.fennecdo.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "tags")
data class TagEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val color: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val priority: String, // "high", "medium", "low"
    val completed: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "todo_tags",
    primaryKeys = ["todoId", "tagId"]
)
data class TodoTagCrossRef(
    val todoId: String,
    val tagId: String
)

// Data class for Todo with Tags
data class TodoWithTags(
    val todo: TodoEntity,
    val tags: List<TagEntity>
)
