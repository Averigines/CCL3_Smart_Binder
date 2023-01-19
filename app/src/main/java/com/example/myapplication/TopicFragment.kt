package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TopicFragment : Fragment() {
    private lateinit var topic: Topic
    private lateinit var cards: List<Card>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_topic, container, false)
        val header = view.findViewById<TextView>(R.id.tvTopic)
        header.text = topic.name

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvCards)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = CardAdapter(cards)

        return view
    }

    companion object {
        fun newInstance(topic: Topic, cards: List<Card>) = TopicFragment().apply {
            this.topic = topic
            this.cards = cards
        }
    }
}