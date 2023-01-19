package com.example.myapplication

import androidx.room.*
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.migration.AutoMigrationSpec
import java.io.Serializable

@Entity
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String
)

@Entity(foreignKeys = [ForeignKey(entity = Category::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("categoryId"),
    onDelete = CASCADE
)]
)
data class Topic(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val categoryId: Int
) : Serializable

@Entity(foreignKeys = [ForeignKey(entity = Topic::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("topicId"),
    onDelete = CASCADE)]
)
data class Card(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val content: String,
    val topicId: Int
)

data class CategoryWithTopics(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val topics: List<Topic>
)

@Dao
interface CategoryDao {

    @Query("SELECT * FROM category")
    fun getAll(): List<Category>

    @Transaction
    @Query("SELECT * FROM category")
    fun getCategoriesWithTopics(): List<CategoryWithTopics>

    @Query("SELECT * FROM category WHERE id = :id")
    fun getById(id: Int): Category

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(category: Category)

    @Update
    fun update(category: Category)

    @Delete
    fun delete(category: Category)

    @Query("DELETE FROM category")
    fun deleteAll()
}

@Dao
interface TopicDao {
    @Query("SELECT * FROM topic")
    fun getAll(): List<Topic>

    @Query("SELECT * FROM topic WHERE id = :id")
    fun getById(id: Int): Topic

    @Query("SELECT * FROM topic WHERE categoryId = :id")
    fun getTopicsOfCategory(id: Int): List<Topic>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(topic: Topic)

    @Update
    fun update(topic: Topic)

    @Delete
    fun delete(topic: Topic)

    @Query("DELETE FROM topic")
    fun deleteAll()
}

@Dao
interface CardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(card: Card)

    @Update
    fun update(card: Card)

    @Delete
    fun delete(card: Card)

    @Query("DELETE FROM card")
    fun deleteAll()

    @Query("SELECT * FROM card")
    fun getAll(): List<Card>

    @Query("SELECT * FROM card WHERE topicId = :id")
    fun getCardsofTopic(id: Int): List<Card>
}

@Database(entities = [Category::class, Topic::class, Card::class], version = 5, autoMigrations = [
        AutoMigration (from = 4, to = 5, spec = AppDatabase.MyAutoMigration::class)
])
abstract class AppDatabase : RoomDatabase() {

    //@DeleteColumn("Topic","title")
    //@RenameColumn("topic", "content", "name")
    class MyAutoMigration : AutoMigrationSpec
    abstract fun categoryDao(): CategoryDao
    abstract fun topicDao(): TopicDao
    abstract fun cardDao(): CardDao
}