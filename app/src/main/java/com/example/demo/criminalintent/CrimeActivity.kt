package com.example.demo.criminalintent

import android.content.Context
import android.support.v4.app.Fragment
import com.example.demo.criminalintent.fragment.CrimeFragment
import java.util.*
import android.content.Intent

class CrimeActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment {
        val crimeId = intent.getSerializableExtra(EXTRA_CRIME_ID) as UUID
        return CrimeFragment.newInstance(crimeId)
    }

    companion object {
       private const val EXTRA_CRIME_ID = "com.example.demo.criminalintent.crime_id"
        fun newIntent(packageContext: Context, crimeId: UUID): Intent {
            val intent = Intent(packageContext, CrimeActivity::class.java)
            intent.putExtra(EXTRA_CRIME_ID, crimeId)
            return intent
        }
    }
}
