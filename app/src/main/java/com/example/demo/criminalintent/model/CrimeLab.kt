package com.example.demo.criminalintent.model

import android.content.Context
import java.util.*
import kotlin.collections.ArrayList

class CrimeLab private constructor(context: Context) {
    private var mCrimes = ArrayList<Crime>()

    init {
        for (i in 0..99) {
            val crime = Crime()
            crime.mTitle = "Crime #$i"
            crime.mSolved = i % 2 == 0// Every other one
            crime.mRequiresPolice = Random(i.toLong()).nextInt() % 2 === 0
            mCrimes.add(crime)
        }
    }

    fun getCrimes(): List<Crime> {
        return mCrimes
    }

    fun getCrime(id: UUID): Crime? {
        for (crime in mCrimes) {
            if (crime.mId == id) {
                return crime
            }
        }
        return null
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
    }
}