package com.example.mapsfragment.adapters

import com.example.mapsfragment.models.ToDo

interface ToDoListener {
    fun onItemClick(todo: ToDo)

    fun onLongClick(todo: ToDo)

    fun onItemEditClick(todo: ToDo)

    fun onBtSaveClick()

    fun onBtDeleteClick(todo: ToDo)

    fun onBtShareClick(todo: ToDo)

}