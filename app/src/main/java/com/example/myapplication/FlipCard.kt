package com.example.myapplication

import Cards
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.viewpager2.widget.ViewPager2

private lateinit var cardsViewPager: ViewPager2
private lateinit var front: CardView
private lateinit var back: CardView

class FlipCard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flip_card2)

        cardsViewPager = findViewById(R.id.vp_fragmentHolder)
        val cardsList: List<Cards> = Cards.cardsList(applicationContext)

        val viewPagerAdapter = DetailViewPagerAdapter(cardsList, this)
        cardsViewPager.adapter = viewPagerAdapter

        cardsViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                //btnFlip.text = cardsList[position].title
            }
        })

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