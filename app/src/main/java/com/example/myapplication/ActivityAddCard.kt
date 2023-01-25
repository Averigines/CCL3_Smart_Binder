package com.example.myapplication

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.room.Room

class ActivityAddCard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        val db = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "smartBinderDB"
        ).allowMainThreadQueries().build()

        var fromCardEdit = false

        val spinnerCategories: Spinner = findViewById(R.id.spinnerCategory)
        val spinnerTopics: Spinner = findViewById(R.id.spinnerTopic)
        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etContent = findViewById<EditText>(R.id.etContent)
        val btnAddCard = findViewById<Button>(R.id.btnAddCard)
        lateinit var cardToEdit : Card
        var spinnerLayout = R.layout.spinner_item

        if (intent.hasExtra("card")) {
            fromCardEdit = true
            cardToEdit = intent.getSerializableExtra("card") as Card

            etTitle.setText(cardToEdit.title)
            etContent.setText(cardToEdit.content)

            spinnerCategories.isEnabled = false
            spinnerTopics.isEnabled = false
            spinnerLayout = R.layout.spinner_item_disabled
            val color = ContextCompat.getColor(this, R.color.project_Darker_Gray)
            val colorStateList = ColorStateList.valueOf(color)
            spinnerCategories.backgroundTintList = colorStateList
            spinnerTopics.backgroundTintList = colorStateList

            btnAddCard.text = "Save Changes"

            val topicOfCard = db.topicDao().getById(cardToEdit.topicId)
            val categoryOfCard = db.categoryDao().getById(topicOfCard.categoryId)

            intent.putExtra("topic", topicOfCard)
            intent.putExtra("category", categoryOfCard)
        }

        var fromCategories = false

        val inflater = LayoutInflater.from(this)
        val contentView = inflater.inflate(R.layout.popup_new_category, null)
        val contentViewTopic = inflater.inflate(R.layout.popup_new_topic, null)
        val popup = PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        val popupTopic = PopupWindow(contentViewTopic, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)

        val btnAddCategory = contentView.findViewById<Button>(R.id.btnAddCategory)
        val etCategory = contentView.findViewById<EditText>(R.id.etCategory)
        val btnAddTopic = contentViewTopic.findViewById<Button>(R.id.btnAddTopic)
        val etTopic = contentViewTopic.findViewById<EditText>(R.id.etTopic)



        var allCategoriesWithTopics = db.categoryDao().getCategoriesWithTopics()
        var categoryNames = ArrayList<String>()
        for(category in allCategoriesWithTopics) {
            categoryNames.add(category.category.name)
        }

        var spinnerCategoryOptions = categoryNames.toTypedArray() + "New Category"
        val spinnerCategoryAdapter = ArrayAdapter(this, spinnerLayout, spinnerCategoryOptions)
        spinnerCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategories.adapter = spinnerCategoryAdapter
        if (intent.hasExtra("category")) {
            val activeCategory = intent.getSerializableExtra("category") as Category
            intent.removeExtra("category")
            val activeCategoryIndex: Int
            for (i in allCategoriesWithTopics.indices) {
                if (allCategoriesWithTopics[i].category.name == activeCategory.name) {
                    activeCategoryIndex = i
                    spinnerCategories.setSelection(activeCategoryIndex)
                    break
                }
            }
        }

        var selectedCategoryName = spinnerCategories.selectedItem.toString()
        var selectedCategory = db.categoryDao().getCategoryByName(selectedCategoryName)

        var topicNames = ArrayList<String>()

        var relatedTopics = mutableListOf<Topic>()
        for (category in allCategoriesWithTopics) {
            if (category.category == selectedCategory) {
                relatedTopics.addAll(category.topics)
            }
        }
        for(topic in relatedTopics) {
            topicNames.add(topic.name)
        }
        var spinnerTopicsOptions = topicNames.toTypedArray() + "New Topic"
        var spinnerTopicsAdapter = ArrayAdapter(this, spinnerLayout, spinnerTopicsOptions)
        spinnerTopicsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTopics.adapter = spinnerTopicsAdapter


        btnAddCategory.setOnClickListener {
            val categoryName = etCategory.text.toString()
            etCategory.text.clear()
            val newCategory = Category(0, categoryName)
            db.categoryDao().insert(newCategory)
            categoryNames.add(categoryName)
            spinnerCategoryOptions = categoryNames.toTypedArray() + "New Category"
            val newAdapter = ArrayAdapter(this, spinnerLayout, spinnerCategoryOptions)
            newAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCategories.adapter = newAdapter
            allCategoriesWithTopics = db.categoryDao().getCategoriesWithTopics()
            val activeCategoryIndex: Int
            for (i in allCategoriesWithTopics.indices) {
                if (allCategoriesWithTopics[i].category.name == categoryName) {
                    activeCategoryIndex = i
                    spinnerCategories.setSelection(activeCategoryIndex)
                    break
                }
            }
            popup.dismiss()
        }

        btnAddTopic.setOnClickListener {
            val topicName = etTopic.text.toString()
            etTopic.text.clear()
            val newTopic = Topic(0, topicName, selectedCategory.id)
            db.topicDao().insert(newTopic)
            topicNames.add(topicName)
            spinnerTopicsOptions = topicNames.toTypedArray() + "New Topic"
            val newAdapter = ArrayAdapter(this, spinnerLayout, spinnerTopicsOptions)
            newAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerTopics.adapter = newAdapter

            allCategoriesWithTopics = db.categoryDao().getCategoriesWithTopics()
            relatedTopics.clear()
            for (category in allCategoriesWithTopics) {
                if (category.category == selectedCategory) {
                    relatedTopics.addAll(category.topics)
                }
            }
            val activeTopicIndex: Int
            for (i in relatedTopics.indices) {
                if (relatedTopics[i].name == topicName) {
                    activeTopicIndex = i
                    spinnerTopics.setSelection(activeTopicIndex)
                    break
                }
            }
            popupTopic.dismiss()
        }

        spinnerCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (position >= allCategoriesWithTopics.size) {
                    popup.isOutsideTouchable = false
                    popup.showAtLocation(window.decorView, Gravity.CENTER, 0, 0)
                }
                else {
                    selectedCategory = allCategoriesWithTopics[position].category
                    relatedTopics = allCategoriesWithTopics[position].topics as MutableList<Topic>
                    topicNames.clear()
                    for(topic in relatedTopics) {
                        topicNames.add(topic.name)
                    }
                    spinnerTopicsOptions = topicNames.toTypedArray() + "New Topic"
                    val newAdapter = ArrayAdapter(view.context, spinnerLayout, spinnerTopicsOptions)
                    newAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerTopics.adapter = newAdapter
                    if (intent.hasExtra("topic")) {
                        fromCategories = true
                        val activeTopic = intent.getSerializableExtra("topic") as Topic
                        intent.removeExtra("topic")
                        val activeTopicIndex: Int
                        for (i in relatedTopics.indices) {
                            if (relatedTopics[i].name == activeTopic.name) {
                                activeTopicIndex = i
                                spinnerTopics.setSelection(activeTopicIndex)
                                break
                            }
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        spinnerTopics.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position >= relatedTopics.size) {
                    popupTopic.isOutsideTouchable = false
                    popupTopic.showAtLocation(window.decorView, Gravity.CENTER, 0, 0)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        btnAddCard.setOnClickListener {
            if (etTitle.text.toString() == "" && etContent.text.toString() == "") {
            Toast.makeText(this, "Please provide a Title and Content for your card!", Toast.LENGTH_SHORT).show()
            }
            else if (etTitle.text.toString() == "") {
                Toast.makeText(this, "Please provide a Title for your card!", Toast.LENGTH_SHORT).show()
            }
            else if (etContent.text.toString() == "") {
                Toast.makeText(this, "Please provide content for your card!", Toast.LENGTH_SHORT).show()
            }
            else if (relatedTopics.size == 0) {
                Toast.makeText(this, "Please create a Topic!", Toast.LENGTH_SHORT).show()
            }
            else if (fromCardEdit) {
                cardToEdit.title = etTitle.text.toString()
                cardToEdit.content = etContent.text.toString()
                db.cardDao().update(cardToEdit)
                Toast.makeText(this, "Card successfully updated!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
            }
            else {
                db.cardDao().insert(Card(0,etTitle.text.toString(), etContent.text.toString(),relatedTopics[spinnerTopics.selectedItemPosition].id))
                Toast.makeText(this, "Card successfully added!", Toast.LENGTH_SHORT).show()
                if (fromCategories) {
                    val selectedTopicName = spinnerTopics.selectedItem.toString()
                    val selectedTopic = db.topicDao().getTopicByName(selectedTopicName)
                    val intent = Intent(this, ActivityCategory::class.java)
                    intent.putExtra("topic", selectedTopic)
                    startActivity(intent)
                }
                else {
                    startActivity(Intent(this, MainActivity::class.java))
                }
            }
        }
    }
}