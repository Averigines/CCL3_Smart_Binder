package com.example.myapplication

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView


data class cardModel (val Category: String, val subCategory: String, val cardTitle: String, val cardContent: String)

class FlipCard : AppCompatActivity(), GestureDetector.OnGestureListener {

    //declaration for swipe gesture
    companion object {
        const val MIN_DISTANCE = 150
    }
    private lateinit var gestureDetector: GestureDetector
    private var x2: Float = 0.0f
    private var x1: Float = 0.0f
    private var y2: Float = 0.0f
    private var y1: Float = 0.0f

    //declaration for card animation
    private lateinit var frontAnim:AnimatorSet
    private lateinit var backAnim:AnimatorSet
    private var isFront: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flip_card)

        // swipe gesture
        gestureDetector = GestureDetector(this, this)



        // flip card animation
        val btnFlip: Button = findViewById(R.id.btn_flip)
        val cardFront: CardView = findViewById(R.id.cv_cardFront)
        val cardBack: CardView = findViewById(R.id.cv_cardBack)

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

    //implementation of GestureListener
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        gestureDetector.onTouchEvent(event)
        when (event?.action) {
            0-> //swipe start
            {
                x1 = event.x
                y1 = event.y
            }
            1-> //swipe end
            {
                x2 = event.x
                y2 = event.y

                val distX: Float = x2 - x1
                val distY: Float = y2 - y1

                if (kotlin.math.abs(distX) > MIN_DISTANCE) {
                    if (x2 > x1) {  //right swipe
                        Toast.makeText(this, "Swipe Right", Toast.LENGTH_SHORT).show()
                    } else {        //left swipe
                        Toast.makeText(this, "Swipe Left", Toast.LENGTH_SHORT).show()
                    }
                }
                else if (kotlin.math.abs(distY) > MIN_DISTANCE) {
                    if (y2 > y1) {  //down swipe
                        Toast.makeText(this, "Swipe Down", Toast.LENGTH_SHORT).show()
                    } else {        //uo swipe
                        Toast.makeText(this, "Swipe up", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDown(p0: MotionEvent?): Boolean {
        //Not used
        return false
    }

    override fun onShowPress(p0: MotionEvent?) {
        //Not used
    }

    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        //not used
        return false
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        //not used
        return false
    }

    override fun onLongPress(p0: MotionEvent?) {
        //not used
        //Toast.makeText(this, "Long press", Toast.LENGTH_SHORT).show()

    }

    override fun onFling(
        p0: MotionEvent?,
        p1: MotionEvent?,
        p2: Float,
        p3: Float)
        : Boolean {
        //Not used
        return false
    }
}