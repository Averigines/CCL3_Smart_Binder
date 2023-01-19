package com.example.myapplication

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView

class FlipCard : AppCompatActivity() {

    private lateinit var frontAnim:AnimatorSet
    private lateinit var backAnim:AnimatorSet
    private var isFront: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flip_card)

        val btnFlip: Button = findViewById(R.id.btn_flip)
        val cardFront: CardView = findViewById(R.id.cv_cardFront)
        val cardBack: TextView = findViewById(R.id.tv_cardBack)

        val scale = applicationContext.resources.displayMetrics.density
        cardFront.cameraDistance = 8000 * scale
        cardBack.cameraDistance = 8000 * scale

        frontAnim = AnimatorInflater.loadAnimator(applicationContext, R.animator.front_animator) as AnimatorSet
        backAnim = AnimatorInflater.loadAnimator(applicationContext, R.animator.back_animator) as AnimatorSet

        btnFlip.setOnClickListener {
            if(isFront) {
                frontAnim.setTarget(cardFront)
                backAnim.setTarget(cardBack)
                frontAnim.start()
                backAnim.start()
                isFront = false
            } else {
                frontAnim.setTarget(cardBack)
                backAnim.setTarget(cardFront)
                backAnim.start()
                frontAnim.start()
                isFront = true
            }
        }



    }
}