package com.example.demo.criminalintent.model

import java.util.*


class Crime {
     var mId: UUID = UUID.randomUUID()
     var mTitle: String? = null
     var mDate: Date = Date()
     var mSolved: Boolean = false
     var mRequiresPolice: Boolean = false
}