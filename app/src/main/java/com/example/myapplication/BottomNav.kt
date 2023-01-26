package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity


fun setUpBottomNav(context: Activity, activeItem: String)  {
    val ibHome = context.findViewById<ImageButton>(R.id.ibHome)
    val ibAddCard = context.findViewById<ImageButton>(R.id.ibAddCard)
    val ibQuiz = context.findViewById<ImageButton>(R.id.ibQuiz)
    val ibStats = context.findViewById<ImageButton>(R.id.ibStats)
    val llHome = context.findViewById<LinearLayout>(R.id.llHome)
    val llAddCard = context.findViewById<LinearLayout>(R.id.llAddCard)
    val llQuiz = context.findViewById<LinearLayout>(R.id.llQuiz)
    val llStats = context.findViewById<LinearLayout>(R.id.llStats)


    val activeColor = ContextCompat.getColor(context, R.color.project_active_bottom)

    if(activeItem == "llHome") llHome.setBackgroundResource(R.drawable.bottom_nav_active)
    //if(activeItem == "llHome") llHome.setBackgroundColor(activeColor)
    if(activeItem == "llAddCard") llAddCard.setBackgroundResource(R.drawable.bottom_nav_active)
    if(activeItem == "llQuiz") llQuiz.setBackgroundResource(R.drawable.bottom_nav_active)
    if(activeItem == "llStats") llStats.setBackgroundResource(R.drawable.bottom_nav_active)

    llHome.setOnClickListener {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(context, intent, null)
    }
    ibHome.setOnClickListener {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(context, intent, null)
    }

    llAddCard.setOnClickListener {
        val intent = Intent(context, ActivityAddCard::class.java)
        startActivity(context, intent, null)
    }
    ibAddCard.setOnClickListener {
        val intent = Intent(context, ActivityAddCard::class.java)
        startActivity(context, intent, null)
    }

    llQuiz.setOnClickListener {
        val intent = Intent(context, CategorySelectionForFlipquizActivity::class.java)
        startActivity(context, intent, null)
    }
    ibQuiz.setOnClickListener {
        val intent = Intent(context, CategorySelectionForFlipquizActivity::class.java)
        startActivity(context, intent, null)
    }

    llStats.setOnClickListener {
        val intent = Intent(context, ResultActivity::class.java)
        startActivity(context, intent, null)
    }
    ibStats.setOnClickListener {
        val intent = Intent(context, ResultActivity::class.java)
        startActivity(context, intent, null)
    }
}