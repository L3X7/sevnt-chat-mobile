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
    fun insertUser(user: UserDBModel): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put(UserDatabaseContract.UserEntry.ID_USER, user.idUser)
        values.put(UserDatabaseContract.UserEntry.USER_NAME, user.userName)
        values.put(UserDatabaseContract.UserEntry.FIRST_NAME, user.firstName)
        values.put(UserDatabaseContract.UserEntry.SURNAME, user.surname)
        val newRowId = db.insert(UserDatabaseContract.UserEntry.TABLE_NAME, null, values)
        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun deleteUSer(userId: String): Boolean {
        val db = writableDatabase
        val queryDelete = UserDatabaseContract.UserEntry.ID_USER + "=" + userId
        db.delete(UserDatabaseContract.UserEntry.TABLE_NAME, queryDelete, null)
        return true
    }

    companion object {
        val DATABASE_NAME = "SEVNTCHAT.db"
        val DATABASE_VERSION = 1


        //        Users
        private val SQL_CREATE_USER_ENTRIES =
                "CREATE TABLE" + UserDatabaseContract.UserEntry.TABLE_NAME + "( " +
                        UserDatabaseContract.UserEntry.ID_USER + " TEXT PRIMARY KEY," +
                        UserDatabaseContract.UserEntry.USER_NAME + " TEXT," +
                        UserDatabaseContract.UserEntry.FIRST_NAME + " TEXT," +
                        UserDatabaseContract.UserEntry.SURNAME + " TEXT)"

        private val SQL_DELETE_USER_ENTRIES = "DROP TABLE IF EXIST" + UserDatabaseContract.UserEntry.TABLE_NAME

    }
}