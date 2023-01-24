package com.example.myapplication

import Cards
import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import java.lang.Float.max
import java.lang.Float.min

class DetailViewPagerAdapter(
    private val cardsList: List<Cards>,
    private val leftCornerGradient: ImageView,
    private val rightCornerGradient: ImageView,
    private val leftCornerText: TextView,
    private val rightCornerText: TextView,
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

        fun isBackFacing(): Boolean {
            return cardBack.alpha == 1.0f
        }

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
        val distanceCamera = 8000 * scale
        holder.cardFront.cameraDistance = distanceCamera
        holder.cardBack.cameraDistance = distanceCamera

        holder.itemView.apply {
            setOnTouchListener { _, motionEvent ->

                val displayMetrics = resources.displayMetrics
                val cardWidth = holder.cardBack.width
                val cardStart = (displayMetrics.widthPixels.toFloat() / 2) - (cardWidth / 2)
                val minSwipeDistance = 360

                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        cursorInitialPosition = motionEvent.rawX
                    }
                    MotionEvent.ACTION_UP -> {
                        leftCornerText.alpha = 0f
                        leftCornerGradient.alpha = 0f
                        rightCornerText.alpha = 0f
                        rightCornerGradient.alpha = 0f
                        cursorLastPosition = motionEvent.rawX
                        val distY = kotlin.math.abs(cursorLastPosition - cursorInitialPosition)
                        if (holder.isBackFacing()) {
                            if (distY > minSwipeDistance) {
                                fadeScaleOut(holder.cardBack, 350)
                                Handler(Looper.getMainLooper()).postDelayed({
                                    tempList = tempList.drop(1)
                                    notifyItemRemoved(0)
                                }, 320)
                            } else {
                                holder.cardBack.animate()
                                    .x(cardStart)
                                    .rotation(0f)
                                    .duration = 350

                            }
                        }

                        if (kotlin.math.abs(cursorLastPosition - cursorInitialPosition) < 2) {
                            if (!animationRunning) {
                                val visibleCard =
                                    if (holder.isBackFacing()) holder.cardBack
                                    else holder.cardFront
                                val notVisibleCard =
                                    if (!holder.isBackFacing()) holder.cardBack
                                    else holder.cardFront

                                bindAnimation(visibleCard, frontAnim)
                                bindAnimation(notVisibleCard, backAnim)
                            }
                        }
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val newX = motionEvent.rawX
                        val scaleRotation = kotlin.math.abs(newX - cursorInitialPosition) / 60
                        val scaleAlpha = kotlin.math.abs(newX - cursorInitialPosition)

                        if (!holder.isBackFacing())  // check if card turned
                            return@setOnTouchListener true

                        if (newX >= cardStart + cardWidth) // check if cursor in card
                            return@setOnTouchListener true

                        if (newX < cursorInitialPosition) { //swipe left
                            rightCornerText.alpha = 0f
                            rightCornerGradient.alpha = 0f
                            cornerAlphaOnMove(leftCornerGradient, leftCornerText, scaleAlpha)
                            holder.cardBack.animate()
                                .x(
                                    min(
                                        cardStart, newX - (cardWidth / 2)
                                    )
                                )
                                .rotation(-scaleRotation)
                                .setDuration(0)
                                .start()
                        } else if (newX > cursorInitialPosition) { // swipe right
                            leftCornerText.alpha = 0f
                            leftCornerGradient.alpha = 0f
                            cornerAlphaOnMove(rightCornerGradient, rightCornerText, scaleAlpha)
                            holder.cardBack.animate()
                                .x(
                                    max(cardStart, newX - (cardWidth / 2))
                                )
                                .rotation(scaleRotation)
                                .setDuration(0)
                                .start()
                        }
                    }
                }
                return@setOnTouchListener true
            }
        }

    }

    override fun getItemCount() = tempList.size

    private fun cornerAlphaOnMove(iv: ImageView, tv: TextView, scale: Float) {
        iv.animate()
            .alpha(min(scale* 0.002f, 1f))
            .setDuration(0)
            .start()
        tv.animate()
            .alpha(min(scale * 0.0007f, 1f))
            .setDuration(0)
            .start()
    }

    private fun fadeScaleOut(view: View, duration: Long) {
        view.animate()
            .alpha(0f)
            .scaleY(1.1f)
            .duration = duration
    }

    private fun bindAnimation(element: CardView, anim: AnimatorSet) {
        animationRunning = true
        anim.setTarget(element)
        anim.start()

        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {
                // not used
            }

            override fun onAnimationEnd(p0: Animator) {
                animationRunning = false
            }

            override fun onAnimationCancel(p0: Animator) {
                // not used
            }

            override fun onAnimationRepeat(p0: Animator) {
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