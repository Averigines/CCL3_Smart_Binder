package com.example.myapplication

import Cards
import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.KeyEvent.ACTION_UP
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.animation.addListener
import androidx.recyclerview.widget.RecyclerView
import java.lang.Float.max
import java.lang.Float.min
import java.lang.Math.abs
import kotlin.concurrent.thread
import kotlin.properties.Delegates

class DetailViewPagerAdapter(
    private val cardsList: List<Cards>,
    private val textView: TextView
) : RecyclerView.Adapter<DetailViewPagerAdapter.DetailViewHolder>() {

    //declaration for card animation
    private lateinit var frontAnim: AnimatorSet
    private lateinit var backAnim: AnimatorSet
    private var animationRunning: Boolean = false
    private var cursorInitialPosition: Float = 0f
    private var cursorLastPosition: Float = 0f
    private var tempList: List<Cards> = cardsList

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
        val cardArgs: Cards = tempList[position]
        holder.bind(cardArgs)

        // Init of the animations
        animInit(holder.itemView.context)

        val scale = holder.itemView.context.resources.displayMetrics.density
        holder.cardFront.cameraDistance = 8000 * scale
        holder.cardBack.cameraDistance = 8000 * scale

        /*
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
         */

        holder.itemView.apply {
            setOnTouchListener { view, motionEvent ->

                var actionString: String = ""
                val displayMetrics = resources.displayMetrics
                val cardWidth = holder.cardBack.width
                val cardStart = (displayMetrics.widthPixels.toFloat() / 2) - (cardWidth / 2)
                val minSwipeDistance = 360

                when(motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        cursorInitialPosition = motionEvent.rawX
                    }
                    MotionEvent.ACTION_UP -> {
                        cursorLastPosition = motionEvent.rawX
                        var distY = abs(cursorLastPosition - cursorInitialPosition)
                            if (distY > minSwipeDistance) {
                                fadeScaleOut(holder.cardBack, 350, tempList)
                                    Handler().postDelayed({
                                        tempList = tempList.drop(1)
                                        notifyItemRemoved(0)
                                    }, 320)
                            } else {
                                holder.cardBack.animate()
                                    .x(cardStart).duration = 350
                            }

                        if (abs(cursorLastPosition - cursorInitialPosition) < 100) {
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
                    MotionEvent.ACTION_MOVE ->{
                        val newX = motionEvent.rawX

                        if (holder.cardBack.alpha == 1.0f) { // check if card turned
                            if (newX < cardStart + cardWidth) { // check if cursor in card
                                if (newX < cursorInitialPosition) { //swipe left
                                    holder.cardBack.animate()
                                        .x(
                                            min(cardStart,newX - (cardWidth / 2)
                                            )
                                        )
                                        .setDuration(0)
                                        .start()
                                } else if (newX > cursorInitialPosition) { // swipe right
                                    holder.cardBack.animate()
                                        .x(
                                            max(cardStart, newX - (cardWidth / 2))
                                        )
                                        .setDuration(0)
                                        .start()
                                }
                            }
                        }
                    }
                }
                return@setOnTouchListener true
            }
        }

        }

    override fun getItemCount() = tempList.size

    private fun fadeScaleOut(view: View, duration: Long, list: List<Cards>) {
        view.animate()
            .alpha(0f)
            .scaleY(1.1f)
            .duration = duration
    }

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