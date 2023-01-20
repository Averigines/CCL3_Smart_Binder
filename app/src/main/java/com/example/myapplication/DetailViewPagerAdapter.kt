package com.example.myapplication

import Cards
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class DetailViewPagerAdapter(
    private val cardsList: List<Cards>,
    private val activity: FlipCard,
) : RecyclerView.Adapter<DetailViewPagerAdapter.DetailViewHolder>() {

    //declaration for card animation
    private lateinit var frontAnim: AnimatorSet
    private lateinit var backAnim: AnimatorSet
    private var isFront: Boolean = true
    private val tempList: List<Cards> = cardsList


    class DetailViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val cardFront: CardView = itemView.findViewById(R.id.cv_cardFront)
        val cardBack: CardView = itemView.findViewById(R.id.cv_cardBack)
        private val frontTitle: TextView = itemView.findViewById(R.id.tv_cardFrontTitle)
        private val frontContent: TextView = itemView.findViewById(R.id.tv_cardFrontContent)
        private val backContent: TextView = itemView.findViewById(R.id.tv_cardBack)

        fun bind(
            card: Cards
        ) {
            frontTitle.text = card.topic
            frontContent.text = card.title
            backContent.text = card.content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {

        return DetailViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.fragment_card, parent, false
            )
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val cardArgs: Cards = cardsList[position]
        holder.bind(cardArgs)

        // Init of the animations
        frontAnim = AnimatorInflater.loadAnimator(
            holder.itemView.context,
            R.animator.front_animator
        ) as AnimatorSet
        backAnim = AnimatorInflater.loadAnimator(
            holder.itemView.context,
            R.animator.back_animator
        ) as AnimatorSet

        val scale = holder.itemView.context.resources.displayMetrics.density
        holder.cardFront.cameraDistance = 8000 * scale
        holder.cardBack.cameraDistance = 8000 * scale

        holder.itemView.apply {
            setOnClickListener {
                if (holder.cardFront.alpha == 1.0f) {
                    bindAnimation(holder.cardFront, frontAnim)
                    bindAnimation(holder.cardBack, backAnim)
                } else if (holder.cardBack.alpha == 1.0f) {
                    bindAnimation(holder.cardBack, frontAnim)
                    bindAnimation(holder.cardFront, backAnim)
                }
            }
        }
    }

    override fun getItemCount() = cardsList.size

    private fun bindAnimation(element: CardView, anim: AnimatorSet) {
        anim.setTarget(element)
        anim.start()
    }

    private fun animInit(context: Context) {
        frontAnim = AnimatorInflater.loadAnimator(
            context,
            R.animator.front_animator
        ) as AnimatorSet
        backAnim = AnimatorInflater.loadAnimator(
            context,
            R.animator.back_animator
        ) as AnimatorSet
    }
}