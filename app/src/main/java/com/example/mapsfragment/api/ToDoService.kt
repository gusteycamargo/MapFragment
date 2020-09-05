package com.example.mapsfragment.api

import com.example.mapsfragment.models.ToDo
import retrofit2.Call
import retrofit2.http.*

interface ToDoService {
    @GET("todos")
    fun getAll(): Call<List<ToDo>>

    @GET("todos/{id}")
    fun getAllByIds(@Path("id") ids: IntArray): Call<ToDo>

    @Headers("Content-Type: application/json")
    @POST("todos")
    fun insert(@Body todo: ToDo): Call<ToDo>

    @Headers("Content-Type: application/json")
    @PATCH("todos/{id}")
    fun update(@Path("id") id: Int, @Body todo: ToDo): Call<ToDo>

    @DELETE("todos/{id}")
    fun delete(@Path("id") id: Int): Call<Void>
}