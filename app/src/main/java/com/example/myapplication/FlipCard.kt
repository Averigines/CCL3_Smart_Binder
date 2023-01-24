package com.example.myapplication

import Cards
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.viewpager2.widget.ViewPager2


private lateinit var cardsViewPager: ViewPager2

class FlipCard : AppCompatActivity() {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flip_card)

        cardsViewPager = findViewById(R.id.vp_fragmentHolder)
        val cardsList: List<Cards> = Cards.cardsList(applicationContext)
        val tempCardList5: List<Cards> = cardsList
        val leftCornerGradient: ImageView = findViewById(R.id.gradient_left)
        val rightCornerGradient: ImageView = findViewById(R.id.gradient_right)
        val leftCornerText: TextView = findViewById(R.id.tv_leftGradient)
        val rightCornerText: TextView = findViewById(R.id.tv_rightGradient)
        val infoBtn: TextView = findViewById(R.id.tv_contentCheck)
        val cardInfo: CardView = findViewById(R.id.cv_cardInfo)

        val viewPagerAdapter =
            DetailViewPagerAdapter(tempCardList5, leftCornerGradient, rightCornerGradient, leftCornerText, rightCornerText)
        cardsViewPager.adapter = viewPagerAdapter

        cardsViewPager.isUserInputEnabled = false

        infoBtn.apply {
            setOnTouchListener { _, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        cardsViewPager.visibility = View.GONE
                        cardInfo.visibility = View.VISIBLE
                        return@setOnTouchListener true
                    }
                    MotionEvent.ACTION_UP -> {
                        cardsViewPager.visibility = View.VISIBLE
                        cardInfo.visibility = View.GONE
                        return@setOnTouchListener true
                    }
                    else -> {return@setOnTouchListener false}
                }
            }
        }

        cardsViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
            }
        })


        // fade off
        val nextItemVisiblePx = 12f
        val currentItemHorizontalMarginPx = 28f
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val cardTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            page.alpha = 0.25f + (1 - kotlin.math.abs(position))
        }
        cardsViewPager.setPageTransformer(cardTransformer)
    }
}