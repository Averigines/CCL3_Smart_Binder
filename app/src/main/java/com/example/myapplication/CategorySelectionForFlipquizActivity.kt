package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.room.Room

class CategorySelectionForFlipquizActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_selection_for_flipquiz)

        val db = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "smartBinderDB"
        ).allowMainThreadQueries().build()

        val btnSwitch : Button = findViewById(R.id.btn_gotToFlip)
        val categoryList: List<Category> = db.categoryDao().getAll()
        val categoryArray: ArrayList<String> = arrayListOf("All categories")
        categoryList.forEach {
            categoryArray.add(it.name)
        }

        val sp_Category : Spinner = findViewById(R.id.sp_categorySpinner)
        val adapter = ArrayAdapter<String>(this, R.layout.spinner_item, categoryArray)
        sp_Category.adapter = adapter
        adapter.setDropDownViewResource(R.layout.spiner_style_item)

        btnSwitch.setOnClickListener{
            val flipCardIntent = Intent(this, FlipCard::class.java)
            val selectedCategory: String = sp_Category.selectedItem.toString()

            var cardsList: List<Card> = mutableListOf()

            if(selectedCategory.toString() == "All categories") {
                cardsList = db.cardDao().getAll() as MutableList<Card>
            } else {

            }


            flipCardIntent.putExtra("selectedCategory", selectedCategory)
        }
    }
}