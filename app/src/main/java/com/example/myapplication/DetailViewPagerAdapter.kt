package com.example.myapplication

import Cards
import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.animation.addListener
import androidx.recyclerview.widget.RecyclerView

class DetailViewPagerAdapter(
    private val cardsList: List<Cards>,
    private val activity: FlipCard,
) : RecyclerView.Adapter<DetailViewPagerAdapter.DetailViewHolder>() {

    //declaration for card animation
    private lateinit var frontAnim: AnimatorSet
    private lateinit var backAnim: AnimatorSet
    private var animationRunning: Boolean = false


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
        animInit(holder.itemView.context)

        val scale = holder.itemView.context.resources.displayMetrics.density
        holder.cardFront.cameraDistance = 8000 * scale
        holder.cardBack.cameraDistance = 8000 * scale

        holder.itemView.apply {
            setOnClickListener {
                if (!animationRunning) {
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

        /*
        holder.itemView.apply {
            setOnTouchListener(
                View.OnTouchListener { view, event ->

                    // variables to store current configuration of quote card.
                    val displayMetrics = activity.resources.displayMetrics
                    val cardHeight = holder.cardBack.height
                    val cardStartY = (displayMetrics.heightPixels.toFloat() / 2) - (cardHeight / 2)
                    val minSwipeDistance = 280

                    when (event.action) {

                        MotionEvent.ACTION_MOVE -> {
                            // get x coordinate of the touch
                            val newY = event.rawY
                            if (holder.cardBack.alpha == 1.0f) {
                            if (newY < cardStartY) {
                                Toast.makeText(holder.itemView.context, newY.toString(), Toast.LENGTH_SHORT).show()
                            } else if (newY > cardStartY + cardHeight) {
                                Toast.makeText(holder.itemView.context, newY.toString(), Toast.LENGTH_SHORT).show()
                            }
                            }
                        }
                        MotionEvent.ACTION_UP -> {
                            if (holder.cardFront.alpha == 1.0f) {
                                bindAnimation(holder.cardFront, frontAnim)
                                bindAnimation(holder.cardBack, backAnim)
                            } else if (holder.cardBack.alpha == 1.0f) {
                                bindAnimation(holder.cardBack, frontAnim)
                                bindAnimation(holder.cardFront, backAnim)
                            }
                        }
                    }

                    // required to by-pass lint warning
                    view.performClick()
                    return@OnTouchListener true
                }
            )
        }

         */

    }

    override fun getItemCount() = cardsList.size

    private fun bindAnimation(element: CardView, anim: AnimatorSet) {
        animationRunning = true
        anim.setTarget(element)
        anim.start()
        anim.addListener(object: Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator?) {
                // not used
            }

            override fun onAnimationEnd(p0: Animator?) {
                animationRunning = false
            }

            override fun onAnimationCancel(p0: Animator?) {
                // not used
            }

            override fun onAnimationRepeat(p0: Animator?) {
                // not used
            }

        }) // make element clickable at end of anim

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