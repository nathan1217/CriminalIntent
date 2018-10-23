package com.example.demo.criminalintent

import android.content.Context
import android.support.v4.app.Fragment
import com.example.demo.criminalintent.fragment.CrimeFragment
import android.content.Intent
import java.util.*

class CrimeActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment {
        return CrimeFragment()
    }

    companion object {
        const val EXTRA_CRIME_ID = "com.example.demo.criminalintent.crime_id"

        fun newIntent(packageContext: Context, crimeId: UUID): Intent {
            val intent = Intent(packageContext, CrimeActivity::class.java)
            intent.putExtra(EXTRA_CRIME_ID, crimeId)
            return intent
        }
    }
}
