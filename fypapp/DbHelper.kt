package com.example.fypapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import org.mindrot.jbcrypt.BCrypt
import java.text.SimpleDateFormat
import java.util.*

class GoalsDbHelper(context: Context) : SQLiteOpenHelper(context, "goalList", null, 22){

    companion object{
        val TABLE_NAME="goals"
        val TASK_COL ="TASK"
        var COMP_COL = "COMPLETED"
        var DATE = "DATE"
        var PRI_COL = "PRIORITY"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TASK_COL + " TEXT, " +
                COMP_COL + " TEXT, " +
                DATE + " DATE, " +
                PRI_COL + " TEXT);")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun checkDb(taskName: String): Cursor? {
        val db=this.readableDatabase
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + TASK_COL + " = ?", arrayOf(taskName))
    }

    fun addTask(taskName: String, priority: String){
        val db=this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(TASK_COL, taskName)
        contentValues.put(PRI_COL, priority)
        contentValues.put(COMP_COL, "0")
        db.insert(TABLE_NAME, null, contentValues)
    }

    fun deleteTask(taskName: String) {
        val db=this.writableDatabase
        db.delete(TABLE_NAME, TASK_COL + " = ?", arrayOf(taskName))
    }

    fun setCompleted(task: String){
        val db=this.writableDatabase
        val contentValues = ContentValues()

        val date = Calendar.getInstance().time
        val df = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val formattedDate = df.format(date)

        contentValues.put(COMP_COL, "1")
        contentValues.put(DATE, formattedDate)
        db.update(TABLE_NAME, contentValues, TASK_COL + " = ?", arrayOf(task))

    }

    val getCompleted: Cursor
        get() {
            val db = this.readableDatabase
            return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COMP_COL + " = ? ORDER BY " + TASK_COL + " ASC", arrayOf("1"))
    }

    val getGoals: Cursor
        get(){
            val db = this.readableDatabase
            return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COMP_COL + " = ?", arrayOf("0"))

        }

    fun filterGoals(filter: String): Cursor? {
        val db=this.readableDatabase
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + PRI_COL + " = ? AND " + COMP_COL + " = ?", arrayOf(filter,"0"))
    }

}

class LoginDbHelper(val context: Context) : SQLiteOpenHelper(context, "loginInfo", null, 10) {

    val filename = "loginInfo.txt"

    companion object{
        val TABLE_NAME="info"
        val ID = "ID"
        val NAME = "NAME"
        val PASSWORD = "PASSWORD"
        val EMAIL = "EMAIL"

    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME + " TEXT, " +
                PASSWORD + " TEXT, " +
                EMAIL + " TEXT);")

        loadInfoToDb(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)

        onCreate(db)
    }

    private fun loadInfoToDb(db: SQLiteDatabase){
        var strings: List<String> = ArrayList()
        context.assets.open(filename).bufferedReader().use{ reader ->
            var line: String? = reader.readLine()
            while (line != null) {
                strings  = line.split(": ").map { it.trim() }
                if (strings.size < 3) continue
                line = reader.readLine()
                addInfo(strings[0], strings[1], strings[2], db)
            }
        }

    }

    fun addInfo(name: String, email: String, pass: String, db: SQLiteDatabase): Long {
        val p = generateHashedPass(pass)
        val initialValues = ContentValues().apply {
            put(NAME, name)
            put(EMAIL, email)
            put(PASSWORD, p)
        }
       // return db.rawQuery("INSERT INTO " + TABLE_NAME + " (NAME, PASSWORD) " + "VALUES ('" + name + "','" + pass + "')", null)
        return db.insert(TABLE_NAME, null, initialValues)
    }

    fun checkInfo(name: String, pass: String) : Boolean{
        val db = this.readableDatabase

        val t: Cursor =  db.rawQuery("SELECT PASSWORD FROM " + TABLE_NAME + " WHERE " + NAME + " = ?", arrayOf(name))

        return if(t.count > 0) {
            t.moveToFirst()

            Log.d("lol", t.getString(t.getColumnIndex("PASSWORD")))
            isValid(pass, t.getString(t.getColumnIndex("PASSWORD")))

        } else{
            t.close()
            db.close()
            false
        }

    }

    fun signUp(uName: String, email: String, pass: String): Long {
        val db = this.writableDatabase
        val p = generateHashedPass(pass)
        val initialValues = ContentValues().apply {
            put(NAME, uName)
            put(EMAIL, email)
            put(PASSWORD, p)
        }
        return db.insert(TABLE_NAME, null, initialValues)
    }

    private fun generateHashedPass(pass: String): String? {
        // hash a plaintext password using the typical log rounds (10)
        return BCrypt.hashpw(pass, BCrypt.gensalt())
    }

    private fun isValid(clearTextPassword: String, hashedPass: String): Boolean {
        // returns true if password matches hash
        return BCrypt.checkpw(clearTextPassword, hashedPass)
    }

}

class ResultsDbHelper (val context: Context) : SQLiteOpenHelper(context, "resultList", null, 24){

    val filename = "resultData.txt"

    companion object{
        val TABLE_NAME="results"
        val ID = "ID"
        val CAT_COL ="CATEGORY"
        val RES_COL = "RESULT"
        val NUM_COL = "NUM_SCORE"
        val USER_ID = "USER_ID"
        val HEADER_COL = "HEADER"
        val LOADED_COL = "LOADED"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CAT_COL + " TEXT, " +
                RES_COL + " TEXT, " +
                NUM_COL + " INTEGER, " +
                USER_ID + " INTEGER, " +
                HEADER_COL + " TEXT, " +
                LOADED_COL + " TEXT DEFAULT '0');")

        loadResultsToDb(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)

        onCreate(db)
    }

    fun updateNum(id: Int, numScore: Int, userId: Int){
        val db=this.writableDatabase
        val contentValues = ContentValues()
        val idString = id.toString()
        val userString = userId.toString()
        contentValues.put(NUM_COL, numScore)
        db.update(TABLE_NAME, contentValues, CAT_COL + " = ? AND " + USER_ID + " = ?", arrayOf(idString, userString))
    }

    fun getID(category: String, userId: Int): Cursor? {
        val db=this.writableDatabase
        val userString = userId.toString()
        return db.rawQuery("SELECT " + ID + " FROM " + TABLE_NAME + " WHERE " + CAT_COL + " = ? AND " + USER_ID + " = ?", arrayOf(category, userString))
    }

    private fun loadResultsToDb(db: SQLiteDatabase){
        context.assets.open(filename).bufferedReader().use{ reader ->
            var line: String? = reader.readLine()
            while (line != null) {
                val strings: List<String> = line.split(": ").map { it.trim() }
                if (strings.size < 4) continue
                addResult(db, strings[0], strings[1], strings[2], strings[3])
                line = reader.readLine()
            }
        }
    }

    fun addResult(db: SQLiteDatabase, header: String, category: String, result: String, progress: String): Long {
        val initialValues = ContentValues().apply {
            put(CAT_COL, category)
            put(RES_COL, result)
            put(HEADER_COL, header)
            put(NUM_COL, progress.toInt())
        }

        return db.insert(TABLE_NAME, null, initialValues)
    }

    fun getResults(): Cursor? {
        val db = this.readableDatabase
        //val id= userId.toString()
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null)
    }

    fun getHeaders(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT DISTINCT " + HEADER_COL + " FROM " + TABLE_NAME, null)
    }

    fun setLoaded(){
        val db=this.writableDatabase
        val initialValues = ContentValues().apply {
            put(LOADED_COL, "1")
        }
        db.update(TABLE_NAME, initialValues, LOADED_COL + " = ?", arrayOf("0"))
    }

    fun getLoaded(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT " + LOADED_COL + " FROM " + TABLE_NAME, null)
    }

}
