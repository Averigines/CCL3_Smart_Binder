package com.example.myapplication

import android.text.Editable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

interface OnTopicClickListener {
    fun onTopicClick(topic: Topic)
}

interface OnTopicAddClickListener {
    fun onTopicAddClick(topicName: String)
}

class CategoryAdapter(private val data: List<CategoryWithTopicsWithCards>, private val topicListener: OnTopicClickListener, private val db: AppDatabase) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.tvCategory)
        val categoryChildCount: TextView = itemView.findViewById(R.id.tvCategoryChildCount)
        val topicList: RecyclerView = itemView.findViewById(R.id.rvTopics)
        val fabAddTopic: com.google.android.material.floatingactionbutton.FloatingActionButton = itemView.findViewById(R.id.fabAddTopic)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = data[position].category

        // For displaying amount of topics per category
        val topicSize = data[position].topics.size

        holder.categoryChildCount.text = topicSize.toString()
        holder.categoryName.text = category.name
        holder.topicList.visibility = View.GONE
        holder.fabAddTopic.visibility = View.GONE

        val inflater = LayoutInflater.from(holder.itemView.context)
        val contentView = inflater.inflate(R.layout.popup_new_topic, null)
        val btnAddTopic = contentView.findViewById<Button>(R.id.btnAddTopic)
        val etTopic = contentView.findViewById<EditText>(R.id.etTopic)
        val popup = PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        val ibClosePopup = contentView.findViewById<ImageButton>(R.id.imageButton)
        ibClosePopup.setOnClickListener { popup.dismiss() }

        holder.fabAddTopic.setOnClickListener {
            popup.isOutsideTouchable = false
            popup.showAtLocation(holder.itemView, Gravity.CENTER, 0, 0)
        }

        btnAddTopic.setOnClickListener {
            val topicName = etTopic.text.toString()
            val newTopic = TopicWithCards(Topic(0, topicName, category.id), emptyList())
            db.topicDao().insert(Topic(0, topicName, category.id))
            data[position].topics += newTopic
            notifyItemChanged(position)
            popup.dismiss()

        }
        holder.categoryName.setOnClickListener {
            if(holder.topicList.visibility == View.GONE) {
                setUpTopicList(holder, data[position].topics)
                holder.topicList.visibility = View.VISIBLE
                holder.fabAddTopic.visibility = View.VISIBLE
            }
            else {
                holder.topicList.visibility = View.GONE
                holder.fabAddTopic.visibility = View.GONE
            }
        }
    }

    override fun getItemCount() = data.size

    private fun setUpTopicList(holder: ViewHolder, topicWithCards: List<TopicWithCards>) {
        holder.topicList.apply {
            layoutManager = LinearLayoutManager(holder.topicList.context)
            adapter = TopicAdapter(topicWithCards, topicListener)
        }
    }
}

class TopicAdapter(private val topics: List<TopicWithCards>, private val listener: OnTopicClickListener) :
    RecyclerView.Adapter<TopicAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val topicName: TextView = itemView.findViewById(R.id.topic_name)
        val topicChildCount: TextView = itemView.findViewById(R.id.tvTopicChildCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_topic, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val topic = topics[position].topic

        // For displaying cards per topic
        val cardSize = topics[position].cards.size

        holder.topicChildCount.text = cardSize.toString()
        holder.topicName.text = topic.name
        holder.itemView.setOnClickListener {
            listener.onTopicClick(topic)
        }
    }

    override fun getItemCount() = topics.size
}