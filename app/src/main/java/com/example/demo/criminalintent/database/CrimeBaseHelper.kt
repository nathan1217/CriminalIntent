package com.example.demo.criminalintent.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class CrimeBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "create table " + CrimeDbSchema.CrimeTable.NAME + "(" +
                    " _id integer primary key autoincrement, " +
                    CrimeDbSchema.CrimeTable.Cols.UUID + ", " +
                    CrimeDbSchema.CrimeTable.Cols.TITLE + ", " +
                    CrimeDbSchema.CrimeTable.Cols.DATE + ", " +
                    CrimeDbSchema.CrimeTable.Cols.SOLVED +", " +
                    CrimeDbSchema.CrimeTable.Cols.REQUIRED_POLICE +
                    ")"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    companion object {
        private const val VERSION = 1
        private const val DATABASE_NAME = "crimeBase.db"
    }
}