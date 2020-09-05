package com.example.mapsfragment.adapters

import android.content.Context
import android.graphics.Paint
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mapsfragment.R
import com.example.mapsfragment.api.ToDoService
import com.example.mapsfragment.fragments.MapFragmentListener
import com.example.mapsfragment.models.ToDo
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.item.view.*
import kotlinx.android.synthetic.main.item_edit.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ToDoAdapter(
    private val listener: ToDoListener, context: Context, private val mapListener: MapFragmentListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.3.2:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val service = retrofit.create(ToDoService::class.java)
    private var toDoList: MutableList<ToDo> = mutableListOf<ToDo>()
    private val EDIT_ITEM = 0
    private val NORMAL_ITEM = 1
    private val c: Context = context

    init {
        service.getAll().enqueue(object : Callback<List<ToDo>> {
            override fun onFailure(call: Call<List<ToDo>>, t: Throwable) {}

            override fun onResponse(call: Call<List<ToDo>>, response: Response<List<ToDo>>) {
                toDoList = response.body()!!.toMutableList()
                notifyDataSetChanged()
            }

        })
    }

    fun add(todo: ToDo): Int {
        val position = 0
        toDoList.add(position, todo)
        notifyItemInserted(position)
        return position
    }

    fun save(todo: ToDo) {
        service.insert(todo).enqueue(object : Callback<ToDo> {
            override fun onFailure(call: Call<ToDo>, t: Throwable) {}
            override fun onResponse(call: Call<ToDo>, response: Response<ToDo>) {
                todo.id = response.body()!!.id
                val p = returnPositionOfToDo(todo)
                notifyItemChanged(p)
            }
        })
    }

    fun update(todo: ToDo) {
        service.update(todo.id!!, todo).enqueue(object : Callback<ToDo> {
            override fun onFailure(call: Call<ToDo>, t: Throwable) {}
            override fun onResponse(call: Call<ToDo>, response: Response<ToDo>) {}
        })
    }

    fun getToDoInPosition(position: Int): ToDo {
        return toDoList[position]
    }

    fun removeToDoInPosition(position: Int) {
        val todo = getToDoInPosition(position)

        service.delete(todo.id!!).enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) { /* ... */ }
            override fun onResponse(call: Call<Void>, response: Response<Void>) {}
        })
        toDoList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun returnPositionOfToDo(todo: ToDo): Int {
        return toDoList.indexOf(todo)
    }

    override fun getItemViewType(position: Int): Int {
        return if(toDoList[position].status == "EDITANDO") {
            EDIT_ITEM
        }
        else {
            NORMAL_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == NORMAL_ITEM) {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item,
                parent, false)
            return ViewHolder(v)
        } else {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_edit,
                parent, false)
            return EditViewHolder(v)
        }

//        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item,
//            parent, false)
//        return ViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == EDIT_ITEM) {
            (holder as EditViewHolder).fillView(toDoList[position])
        }
        else {
            (holder as ViewHolder).fillView(toDoList[position])
        }
//        val todo = toDoList[position]
//        holder.fillView(todo)
    }
    override fun getItemCount() = toDoList.size

    inner class EditViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun fillView(todo: ToDo) {
            itemView.title_edit.setText(todo.title)
            itemView.description_edit.setText(todo.description)

            itemView.bt_save.setOnClickListener{
                listener.onBtSaveClick()
            }

            itemView.bt_delete.setOnClickListener{
                listener.onBtDeleteClick(todo)
            }

            itemView.setOnClickListener {
                listener.onItemEditClick(todo)
            }

//            itemView.setOnLongClickListener {
//                listener.onLongClick(todo)
//                true
//            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun fillView(todo: ToDo) {
            val done: String =  c.getString(R.string.done)

            if(todo.status == done) {
                itemView.textView1.paintFlags = itemView.textView1.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                itemView.bt_share.visibility = View.VISIBLE
            }
            else {
                itemView.textView1.paintFlags = Paint.ANTI_ALIAS_FLAG
                itemView.bt_share.visibility = View.GONE
            }

            itemView.textView1.text = Editable.Factory.getInstance().newEditable(todo.status + " " + todo.title)
            itemView.text_view_2.text = Editable.Factory.getInstance().newEditable(todo.description)

            itemView.setOnClickListener {
                listener.onItemClick(todo)
            }

            itemView.bt_share.setOnClickListener {
                listener.onBtShareClick(todo)
            }

            itemView.setOnLongClickListener {
                listener.onLongClick(todo)
                true
            }
        }
    }
}