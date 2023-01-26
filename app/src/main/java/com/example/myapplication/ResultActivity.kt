package com.example.myapplication

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Outline
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Database
import androidx.room.Room
import kotlin.math.ceil

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val rvDetailedScore: RecyclerView  = findViewById(R.id.rv_scoreByCat)

        val db = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "smartBinderDB"
        ).allowMainThreadQueries().build()

        setUpBottomNav(this, "llStats")

        val ibBack = findViewById<ImageButton>(R.id.ibBack)
        ibBack.setOnClickListener {
            finish()
        }

        //get from db
        val categoriesList = db.categoryDao().getAll().toMutableList()
        val cardsTurnedList = db.CardResultDao().getAll().toMutableList()
        val successCards: MutableList<CardResult> = mutableListOf()
        cardsTurnedList.forEach {
            if (it.success) {
                successCards.add(it)
            }
        }

        val tvScoreGeneral: TextView = findViewById(R.id.scoreGeneral)

        if (cardsTurnedList.isNotEmpty()) {
            val scoreGen: Double = ceil((successCards.size.toDouble() / cardsTurnedList.size.toDouble())*100)
            tvScoreGeneral.text = "${successCards.size} / ${cardsTurnedList.size}         ${scoreGen}%"
        } else {
            tvScoreGeneral.text = "No test yet"
        }

        rvDetailedScore.layoutManager = LinearLayoutManager(this)
        rvDetailedScore.adapter = DetailedResultAdapter(cardsTurnedList, categoriesList, db)

        val btnEraseDb : ImageView = findViewById(R.id.iv_btnDelete)

        btnEraseDb.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to erase all the statistics?")
                .setTitle("Erasing confirmation")
            builder.setPositiveButton("OK") { dialog, _ ->
                db.CardResultDao().deleteAll()
                categoriesList.clear()
                cardsTurnedList.clear()
                tvScoreGeneral.text = "No test yet"
                rvDetailedScore.adapter!!.notifyDataSetChanged()
                dialog.dismiss()
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }
    }
}

class DetailedResultAdapter(private val cardsTurnedList: List<CardResult>, private val categoriesList: List<Category>, private val db: AppDatabase) : RecyclerView.Adapter<DetailedResultAdapter.DetailedResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailedResultViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.detail_score_item,
            parent, false)
        return DetailedResultViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DetailedResultViewHolder, position: Int) {

        val currentCategory = categoriesList[position]
        val cardTurnedByCat: MutableList<CardResult> = mutableListOf()
        val successByCat: MutableList<CardResult> = mutableListOf()
        cardsTurnedList.forEach {
            if (it.categoryId == currentCategory.id) {
                cardTurnedByCat.add(it)
            }
        }
        cardTurnedByCat.forEach {
            if (it.success) {
                successByCat.add(it)
            }
        }

        if (cardTurnedByCat.isEmpty()) {
            holder.tvCategoryDetailedScore.text = currentCategory.name
            holder.tvPercent.text = "No test on this category"
            holder.bar.visibility = View.GONE

        } else {
            val scoreCategory: Double = ceil((successByCat.size.toDouble() / cardTurnedByCat.size.toDouble())*100)
            val scaledScore: Int =  (scoreCategory * 2).toInt()
            holder.bar.progress = scaledScore
            holder.tvCategoryDetailedScore.text = currentCategory.name
            holder.tvPercent.text = "${scoreCategory} %"
        }
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    class DetailedResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvCategoryDetailedScore : TextView = itemView.findViewById(R.id.tv_catNameResult)
        val bar: ProgressBar = itemView.findViewById(R.id.progressBar)
        val tvPercent: TextView = itemView.findViewById(R.id.tv_scoreCatPercent)

        }
    }
