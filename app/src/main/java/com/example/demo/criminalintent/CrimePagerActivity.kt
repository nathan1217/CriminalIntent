package com.example.demo.criminalintent

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import com.example.demo.criminalintent.model.Crime
import android.support.v4.view.ViewPager
import com.example.demo.criminalintent.fragment.CrimeFragment
import com.example.demo.criminalintent.model.CrimeLab
import android.content.Intent
import java.util.*

class CrimePagerActivity : AppCompatActivity() {
    private lateinit var mViewPager: ViewPager
    private lateinit var mCrimes: List<Crime>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crime_pager)
        val crimeId = intent
            .getSerializableExtra(EXTRA_CRIME_ID) as UUID
        mViewPager = findViewById(R.id.crime_view_pager)
        mCrimes = CrimeLab[this].getCrimes()
        val fragmentManager = supportFragmentManager
        mViewPager.adapter = object : FragmentStatePagerAdapter(fragmentManager) {
            override fun getItem(position: Int): Fragment {
                val crime = mCrimes[position]
                return CrimeFragment.newInstance(crime.mId)
            }

            override fun getCount(): Int {
                return mCrimes.size
            }
        }
        for (i in 0 until mCrimes.size) {
            if (mCrimes[i].mId == crimeId) {
                mViewPager.currentItem = i
                break
            }
        }
    }

    companion object {
        private const val EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crime_id"
        fun newIntent(packageContext: Context, crimeId: UUID): Intent {
            val intent = Intent(packageContext, CrimePagerActivity::class.java)
            intent.putExtra(EXTRA_CRIME_ID, crimeId)
            return intent
        }
    }
}