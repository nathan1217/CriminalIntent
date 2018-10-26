package com.example.demo.criminalintent.model

import java.util.*

class Crime @JvmOverloads constructor(var mId: UUID = UUID.randomUUID()) {
    var mDate: Date = Date()
    var mTitle: String? = null
    var mSolved: Boolean = false
    var mRequiresPolice: Boolean = false

}