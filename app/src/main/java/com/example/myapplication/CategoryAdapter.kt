package com.example.myapplication

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

interface OnTopicClickListener {
    fun onTopicClick(topic: Topic)
}

class CategoryAdapter(private val categories: List<CategoryWithTopics>, private val listener: OnTopicClickListener) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.tvCategory)
        val topicList: RecyclerView = itemView.findViewById(R.id.rvTopics)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        holder.categoryName.text = category.category.name
        holder.topicList.visibility = View.GONE
        holder.itemView.setOnClickListener {
            if(holder.topicList.visibility == View.GONE) {
                setUpTopicList(holder, category.topics)
                holder.topicList.visibility = View.VISIBLE
            }
            else {
                holder.topicList.visibility = View.GONE
            }
        }
    }

    override fun getItemCount() = categories.size

    private fun setUpTopicList(holder: ViewHolder, topics: List<Topic>) {
        //val topicsWithAdd = topics as MutableList
        //topicsWithAdd.add(Topic)
        holder.topicList.apply {
            layoutManager = LinearLayoutManager(holder.topicList.context)
            adapter = TopicAdapter(topics, listener)
        }
    }
}

class TopicAdapter(private val topics: List<Topic>, private val listener: OnTopicClickListener) :
    RecyclerView.Adapter<TopicAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val topicName: TextView = itemView.findViewById(R.id.topic_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_topic, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val topic = topics[position]
        holder.topicName.text = topic.name
        holder.itemView.setOnClickListener {
            listener.onTopicClick(topic)
        }
    }

    override fun getItemCount() = topics.size
}