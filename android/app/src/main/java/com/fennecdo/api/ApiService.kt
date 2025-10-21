package com.fennecdo.api

import com.fennecdo.models.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {
    @GET("todos")
    suspend fun getTodos(): Response<List<Todo>>

    @POST("todos")
    suspend fun createTodo(@Body request: CreateTodoRequest): Response<Todo>

    @PUT("todos/{id}")
    suspend fun updateTodo(
        @Path("id") id: String,
        @Body request: UpdateTodoRequest
    ): Response<Todo>

    @DELETE("todos/{id}")
    suspend fun deleteTodo(@Path("id") id: String): Response<Unit>

    @GET("tags")
    suspend fun getTags(): Response<List<Tag>>

    @POST("tags")
    suspend fun createTag(@Body request: CreateTagRequest): Response<Tag>

    @DELETE("tags/{id}")
    suspend fun deleteTag(@Path("id") id: String): Response<Unit>

    @GET("sync")
    suspend fun sync(): Response<SyncResponse>

    companion object {
        private const val BASE_URL = "http://10.0.2.2:3000/api/"

        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}
