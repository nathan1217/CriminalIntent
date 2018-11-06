package com.example.demo.criminalintent

import android.support.v4.app.Fragment
import com.example.demo.criminalintent.fragment.CrimeListFragment
import com.example.demo.criminalintent.model.Crime
import com.example.demo.criminalintent.fragment.CrimeFragment
import android.view.View

class CrimeListActivity : SingleFragmentActivity(), CrimeListFragment.CallBacks, CrimeFragment.CallBacks {
    override fun createFragment(): Fragment {
        return CrimeListFragment()
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_masterdetail
    }

    override fun onCrimeSelected(crime: Crime) {
        if (findViewById<View>(R.id.detail_fragment_container) == null) {
            val intent = CrimePagerActivity.newIntent(this, crime.mId)
            startActivity(intent)
        } else {
            val newDetail = CrimeFragment.newInstance(crime.mId)
            supportFragmentManager.beginTransaction()
                .replace(R.id.detail_fragment_container, newDetail)
                .commit()
        }
    }

    override fun onCrimeUpdated(crime: Crime) {
        val listFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container) as CrimeListFragment?
        listFragment!!.updateUI()
    }
}