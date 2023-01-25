package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.size
import androidx.room.Room
import androidx.viewpager2.widget.ViewPager2
import java.lang.Integer.min


private lateinit var cardsViewPager: ViewPager2

data class ViewsList()

class FlipCard : AppCompatActivity() {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flip_card)

        cardsViewPager = findViewById(R.id.vp_fragmentHolder)
        val db = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "smartBinderDB"
        ).allowMainThreadQueries().build()

        val selectedCategory: String = intent.getStringExtra("selectedCategory") as String

        var cardsList: List<Card> = listOf()

        cardsList =
            if (selectedCategory.toString() == "All categories") {
                db.cardDao().getAll()
            } else {
                db.cardDao().getAllCardsOfCategory(selectedCategory)
            }

        var numberOfElement: Int = min(5, cardsList.size)
        val randomListOf5 = cardsList.asSequence().shuffled().take(numberOfElement).toList()

        println(randomListOf5)

        val leftCornerGradient: ImageView = findViewById(R.id.gradient_left)
        val rightCornerGradient: ImageView = findViewById(R.id.gradient_right)
        val leftCornerText: TextView = findViewById(R.id.tv_leftGradient)
        val rightCornerText: TextView = findViewById(R.id.tv_rightGradient)
        val infoBtn: TextView = findViewById(R.id.tv_contentCheck)
        val cardInfo: CardView = findViewById(R.id.cv_cardInfo)
        val dao = db.topicDao()
        val daoCategory = db.categoryDao()
        val tvCategory: TextView = findViewById(R.id.tv_catNameOnFlip)
        val viewPagerAdapter =
            DetailViewPagerAdapter(
                randomListOf5,
                leftCornerGradient,
                rightCornerGradient,
                leftCornerText,
                rightCornerText,
                dao,
                daoCategory,
                tvCategory,
                infoBtn,
            )
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
                    else -> {
                        return@setOnTouchListener false
                    }
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