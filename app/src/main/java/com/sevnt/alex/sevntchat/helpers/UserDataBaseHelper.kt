package com.sevnt.alex.sevntchat.helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.sevnt.alex.sevntchat.databasecontract.UserDatabaseContract
import com.sevnt.alex.sevntchat.models.UserDBModel

class UserDataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_USER_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_USER_ENTRIES)
        onCreate(db)
    }

    @Throws(SQLiteConstraintException::class)
    fun addUser(user: UserDBModel): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(UserDatabaseContract.UserEntry.ID_USER, user.idUser)
        values.put(UserDatabaseContract.UserEntry.USER_NAME, user.userName)
        values.put(UserDatabaseContract.UserEntry.FIRST_NAME, user.firstName)
        values.put(UserDatabaseContract.UserEntry.SURNAME, user.surname)
        db.insert(UserDatabaseContract.UserEntry.TABLE_NAME, null, values)
        db.close()
        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun deleteUSer(userId: String): Boolean {
        val db = this.writableDatabase
        val queryDelete = UserDatabaseContract.UserEntry.ID_USER + " = " + "\"" + userId +  "\""
        db.delete(UserDatabaseContract.UserEntry.TABLE_NAME, queryDelete, null)
        db.close()
        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun findFirstUser(): UserDBModel? {
        val db = this.writableDatabase
        var userDBModel: UserDBModel? = null
        val cursor = db.rawQuery(SQL_FIND_FIRST_USER_ENTRIES , null)
        if (cursor.moveToFirst()) {
            cursor.moveToFirst()
            val idUser = cursor.getString(1)
            val userName = cursor.getString(2)
            val firstName = cursor.getString(3)
            val surName = cursor.getString(4)
            userDBModel = UserDBModel(idUser, userName, firstName, surName)
            cursor.close()
        }
        db.close()
        return userDBModel

    }

    @Throws(SQLiteConstraintException::class)
    fun findUserById(userId: String): UserDBModel? {
        val db = this.writableDatabase
        var userDBModel: UserDBModel? = null
        val cursor = db.rawQuery(SQL_FIND_USER_ENTRIES + "\"" + userId +  "\"", null)
        if (cursor.moveToFirst()) {
            cursor.moveToFirst()
            val idUser = cursor.getString(1)
            val userName = cursor.getString(2)
            val firstName = cursor.getString(3)
            val surName = cursor.getString(4)
            userDBModel = UserDBModel(idUser, userName, firstName, surName)
            cursor.close()
        }
        db.close()
        return userDBModel

    }

    companion object {
        const val DATABASE_NAME = "SEVNTCHAT.db"
        const val DATABASE_VERSION = 1

        private val SQL_FIND_USER_ENTRIES = "SELECT * FROM " + UserDatabaseContract.UserEntry.TABLE_NAME +
                " WHERE " + UserDatabaseContract.UserEntry.ID_USER + " = "

        private val SQL_FIND_FIRST_USER_ENTRIES = "SELECT * FROM " + UserDatabaseContract.UserEntry.TABLE_NAME +
                " LIMIT 1"

        //        Users
        private val SQL_CREATE_USER_ENTRIES =
                "CREATE TABLE " + UserDatabaseContract.UserEntry.TABLE_NAME + "( " +
                        UserDatabaseContract.UserEntry.COLUMN_ID + " INTEGER PRIMARY KEY," +
                        UserDatabaseContract.UserEntry.ID_USER + " TEXT," +
                        UserDatabaseContract.UserEntry.USER_NAME + " TEXT," +
                        UserDatabaseContract.UserEntry.FIRST_NAME + " TEXT," +
                        UserDatabaseContract.UserEntry.SURNAME + " TEXT)"

        private val SQL_DELETE_USER_ENTRIES = "DROP TABLE IF EXIST " + UserDatabaseContract.UserEntry.TABLE_NAME

    }
}