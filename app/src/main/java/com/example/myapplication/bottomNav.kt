package com.example.myapplication

import android.app.Activity
import android.content.Context
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

    if(activeItem == "llHome") llHome.setBackgroundColor(activeColor)
    if(activeItem == "llAddCard") llAddCard.setBackgroundColor(activeColor)
    if(activeItem == "llQuiz") llQuiz.setBackgroundColor(activeColor)
    if(activeItem == "llStats") llStats.setBackgroundColor(activeColor)

    ibHome.setOnClickListener {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(context, intent, null)
    }
    ibAddCard.setOnClickListener {
        val intent = Intent(context, ActivityAddCard::class.java)
        startActivity(context, intent, null)
    }
    ibQuiz.setOnClickListener {
        val intent = Intent(context, CategorySelectionForFlipquizActivity::class.java)
        startActivity(context, intent, null)
    }
    ibStats.setOnClickListener {
        val intent = Intent(context, ResultActivity::class.java)
        startActivity(context, intent, null)
    }
}
