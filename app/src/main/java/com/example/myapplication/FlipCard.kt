package com.example.myapplication

import Cards
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.viewpager2.widget.ViewPager2

private lateinit var cardsViewPager: ViewPager2

class FlipCard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flip_card2)

        val btnFlip: Button = findViewById(R.id.btn_flip)

        cardsViewPager = findViewById(R.id.vp_fragmentHolder)
        val cardsList: List<Cards> = Cards.cardsList(applicationContext)

        val viewPagerAdapter = DetailViewPagerAdapter(cardsList, this, btnFlip)
        cardsViewPager.adapter = viewPagerAdapter

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