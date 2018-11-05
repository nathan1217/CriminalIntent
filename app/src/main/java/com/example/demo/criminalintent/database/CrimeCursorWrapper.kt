package com.example.demo.criminalintent.database

import android.database.Cursor
import com.example.demo.criminalintent.database.CrimeDbSchema.CrimeTable
import com.example.demo.criminalintent.model.Crime
import android.database.CursorWrapper
import java.util.*


class CrimeCursorWrapper(cursor: Cursor) : CursorWrapper(cursor) {
    val crime: Crime?
        get() {
            val uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID))
            val title = getString(getColumnIndex(CrimeTable.Cols.TITLE))
            val date = getLong(getColumnIndex(CrimeTable.Cols.DATE))
            val isSolved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED))
            val isRequirePolice = getInt(getColumnIndex(CrimeTable.Cols.REQUIRED_POLICE))
            val crime = Crime(UUID.fromString(uuidString))
            val suspect = getString(getColumnIndex(CrimeTable.Cols.SUSPECT))
            crime.mTitle = title
            crime.mDate = Date(date)
            crime.mSolved = (isSolved !== 0)
            crime.mRequiresPolice = (isRequirePolice !== 0)
            crime.mSuspect = suspect
            return crime
        }
}