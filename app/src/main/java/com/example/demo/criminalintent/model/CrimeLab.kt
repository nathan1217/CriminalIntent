package com.example.demo.criminalintent.model

import android.content.ContentValues
import android.content.Context
import com.example.demo.criminalintent.database.CrimeBaseHelper
import android.database.sqlite.SQLiteDatabase
import java.util.*
import com.example.demo.criminalintent.database.CrimeDbSchema.CrimeTable
import com.example.demo.criminalintent.database.CrimeCursorWrapper


class CrimeLab private constructor(context: Context) {
    private val mContext: Context = context.applicationContext
    private val mDatabase: SQLiteDatabase

    init {
        mDatabase = CrimeBaseHelper(mContext)
            .writableDatabase
    }

    fun addCrime(data: Crime) {
        val values = getContentValues(data)
        mDatabase.insert(CrimeTable.NAME, null, values)
    }

    fun updateCrime(crime: Crime) {
        val uuidString = crime.mId.toString()
        val values = getContentValues(crime)
        mDatabase.update(
            CrimeTable.NAME, values,
            CrimeTable.Cols.UUID + " = ?",
            arrayOf(uuidString)
        )
    }

    fun getCrimes(): List<Crime> {
        val crimes = ArrayList<Crime>()
        val cursor = queryCrimes(null, null)
        cursor.use { cursor ->
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                crimes.add(cursor.crime!!)
                cursor.moveToNext()
            }
        }
        return crimes
    }

    fun getCrime(id: UUID): Crime {
        var cursor: CrimeCursorWrapper = queryCrimes(
            CrimeTable.Cols.UUID + " = ?",
            arrayOf(id.toString())
        )
        return try {
            if (cursor.count != 0) {
                cursor.moveToFirst()
                cursor.crime!!
            } else {
                Crime()
            }
        } finally {
            cursor.close()
        }
    }

    private fun queryCrimes(whereClause: String?, whereArgs: Array<String>?): CrimeCursorWrapper {
        return CrimeCursorWrapper(
            mDatabase.query(
                CrimeTable.NAME, // having
                null // orderBy
                , // Columns - null selects all columns
                whereClause,
                whereArgs, null, null, null
            )
        )
    }

    companion object {
        @Volatile
        private var sCrimeLab: CrimeLab? = null

        operator fun get(context: Context): CrimeLab {
            if (sCrimeLab == null) {
                synchronized(CrimeLab::class) {
                    if (sCrimeLab == null) {
                        sCrimeLab = CrimeLab(context)
                    }
                }
            }
            return sCrimeLab!!
        }

        fun getContentValues(crime: Crime): ContentValues {
            val values = ContentValues()
            values.put(CrimeTable.Cols.UUID, crime.mId.toString())
            values.put(CrimeTable.Cols.TITLE, crime.mTitle)
            values.put(CrimeTable.Cols.DATE, crime.mDate.time)
            values.put(CrimeTable.Cols.SOLVED, if (crime.mSolved) 1 else 0)
            values.put(CrimeTable.Cols.REQUIRED_POLICE, if (crime.mRequiresPolice) 1 else 0)
            values.put(CrimeTable.Cols.SUSPECT, crime.mSuspect)
            return values
        }
    }
}