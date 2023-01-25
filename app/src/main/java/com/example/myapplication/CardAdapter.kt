package com.example.myapplication

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class CardAdapter (private val cards: List<Card>) : RecyclerView.Adapter<CardAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val cardContent: TextView = itemView.findViewById(R.id.tvContent)
        val ibEditCard: ImageButton = itemView.findViewById(R.id.ibEditCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = cards[position]
        holder.cardTitle.text = card.title
        holder.cardContent.text = card.content
        holder.ibEditCard.setOnClickListener {
            val intent = Intent(holder.itemView.context, ActivityAddCard::class.java)
            intent.putExtra("card", card)
            startActivity(holder.itemView.context, intent, intent.extras)
        }
    }

    override fun getItemCount(): Int {
        return cards.size
    }
}