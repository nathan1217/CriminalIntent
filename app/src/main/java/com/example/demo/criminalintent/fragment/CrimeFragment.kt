package com.example.demo.criminalintent.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.demo.criminalintent.R
import com.example.demo.criminalintent.model.Crime
import android.text.Editable
import com.example.demo.criminalintent.model.CrimeLab
import java.text.DateFormat
import java.util.*
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.ContactsContract
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.widget.*
import com.example.demo.criminalintent.PictureUtils
import java.io.File


class CrimeFragment : Fragment() {
    public var mIsAdd: Boolean = false
    private lateinit var mCrime: Crime
    private lateinit var mTitleField: EditText
    private lateinit var mDateButton: Button
    private lateinit var mSolvedCheckBox: CheckBox
    private lateinit var mPoliceCheckBox: CheckBox
    private lateinit var mBtnCrimeReport: Button
    private lateinit var mBtnSuspect: Button
    private lateinit var mBtnTakePhoto: Button
    private lateinit var mImgView: ImageView
    private lateinit var mPhotoFile: File
    private var mCallBacks: CallBacks? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val crimeId: UUID = arguments!!.getSerializable(ARG_CRIME_ID) as UUID
        mCrime = CrimeLab[context!!].getCrime(crimeId)
        mPhotoFile = CrimeLab[activity!!].getPhotoFile(mCrime)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_crime, container, false)
        mTitleField = view.findViewById(R.id.crime_title)
        mTitleField.setText(mCrime.mTitle)
        mTitleField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {
                // This space intentionally left blank
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int
            ) {
                mCrime.mTitle = s.toString()
                updateCrime()
            }

            override fun afterTextChanged(s: Editable) {
                // This one too
            }
        })

        mDateButton = view.findViewById(R.id.crime_date)
        updateDate()
        mDateButton.setOnClickListener { _ ->
            var manager: FragmentManager? = fragmentManager
            var dialog: DatePickerFragment = DatePickerFragment.newInstance(mCrime.mDate)
            dialog.setTargetFragment(this@CrimeFragment, REQUEST_DATE)
            dialog.show(manager, DIALOG_DATE)
        }

        mSolvedCheckBox = view.findViewById(R.id.crime_solved)
        mSolvedCheckBox.isChecked = mCrime.mSolved
        mSolvedCheckBox.setOnCheckedChangeListener { _, isChecked ->
            mCrime.mSolved = isChecked
            updateCrime()
        }

        mPoliceCheckBox = view.findViewById(R.id.crime_police)
        mPoliceCheckBox.isChecked = mCrime.mRequiresPolice
        mPoliceCheckBox.setOnCheckedChangeListener { _, isChecked ->
            mCrime.mRequiresPolice = isChecked
            updateCrime()
        }

        mBtnCrimeReport = view.findViewById(R.id.crime_report)
        mBtnCrimeReport.setOnClickListener { _ ->
            var intent: Intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport())
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject))
            intent = Intent.createChooser(intent, getString(R.string.send_report))
            startActivity(intent)
        }
        var pickContact: Intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
//        pickContact.addCategory(Intent.CATEGORY_HOME)
        mBtnSuspect = view.findViewById(R.id.crime_suspect)
        mBtnSuspect.setOnClickListener {
            startActivityForResult(pickContact, REQUEST_CONTACT)
        }

        if (mCrime.mSuspect != null) {
            mBtnSuspect.text = mCrime.mSuspect
        }

        var manager: PackageManager = activity!!.packageManager
        if (manager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mBtnSuspect.isEnabled = false
        }

        mImgView = view.findViewById(R.id.crime_phone)
        mBtnTakePhoto = view.findViewById(R.id.crime_camera)
        var captureImg: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var canTakePhoto: Boolean = mPhotoFile != null && captureImg.resolveActivity(manager) != null
        mBtnTakePhoto.isEnabled = canTakePhoto

        mBtnTakePhoto.setOnClickListener {
            var uri: Uri =
                FileProvider.getUriForFile(activity!!, "com.example.demo.criminalintent.fileprovider", mPhotoFile)
            captureImg.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            var cameraActivities: List<ResolveInfo> =
                activity!!.packageManager.queryIntentActivities(captureImg, PackageManager.MATCH_DEFAULT_ONLY)
            for (activity in cameraActivities) {
                getActivity()!!.grantUriPermission(
                    activity.activityInfo.packageName,
                    uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
            }
            startActivityForResult(captureImg, REQUEST_PHOTO)
        }
        updatePhotoView()
        return view
    }

    override fun onPause() {
        super.onPause()
        CrimeLab[activity!!].updateCrime(mCrime)
    }

    private fun updateDate() {
        mDateButton.text = DateFormat.getDateInstance(1).format(mCrime.mDate)
    }

    private fun getCrimeReport(): String {
        var solvedString: String? = null
        if (mCrime.mSolved) {
            solvedString = getString(R.string.crime_report_solved)
        } else {
            solvedString = getString(R.string.crime_report_unsolved)
        }
        val dateFormat = "EEE, MMM dd"
        val dateString = DateFormat.getDateInstance(1).format(mCrime.mDate)
        var suspect = mCrime.mSuspect
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect)
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect)
        }
        return getString(
            R.string.crime_report,
            mCrime.mTitle, dateString, solvedString, suspect
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode === REQUEST_DATE) {
            val date = data!!.getSerializableExtra(DatePickerFragment.EXTRA_DATE) as Date
            mCrime.mDate = date
            updateDate()
            updateCrime()
        } else if (requestCode === REQUEST_CONTACT) {
            var contactUrl: Uri = data!!.getData()
            var quesryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
            var cursor: Cursor = activity!!.contentResolver.query(contactUrl, quesryFields, null, null, null)
            try {
                // Double-check that you actually got results
                if (cursor.count === 0) {
                    return
                }
                // Pull out the first column of the first row of data -
                // that is your suspect's name
                cursor.moveToFirst()
                val suspect = cursor.getString(0)
                mCrime.mSuspect = suspect
                mBtnSuspect.text = suspect

                updateCrime()
            } finally {
                cursor.close()
            }
        } else if (requestCode == REQUEST_PHOTO) {
            var uri = FileProvider.getUriForFile(
                activity!!,
                "com.example.demo.criminalintent.fileprovider",
                mPhotoFile
            )
            activity!!.revokeUriPermission(
                uri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            );
            updatePhotoView()

            updateCrime()
        }
    }

    fun updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mImgView.setImageDrawable(null)
        } else {
            var bitmap: Bitmap = PictureUtils().getScaledBitmap(
                mPhotoFile.path, activity!!
            )
            mImgView.setImageBitmap(bitmap)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mCallBacks = context as CallBacks
    }

    override fun onDetach() {
        super.onDetach()
        mCallBacks = null
    }

    fun updateCrime() {
        CrimeLab.get(activity!!).updateCrime(mCrime)
        mCallBacks!!.onCrimeUpdated(mCrime)
    }

    interface CallBacks {
        fun onCrimeUpdated(crime: Crime)
    }

    companion object {
        private const val ARG_CRIME_ID: String = "crime_id"
        private const val DIALOG_DATE: String = "DialogDate"
        private const val REQUEST_DATE: Int = 0
        private const val REQUEST_CONTACT = 1
        private const val REQUEST_PHOTO = 2
        fun newInstance(crimeId: UUID): CrimeFragment {
            var args: Bundle = Bundle()
            args.putSerializable(ARG_CRIME_ID, crimeId)

            var fragment: CrimeFragment = CrimeFragment()
            fragment.arguments = args
            return fragment
        }
    }
}