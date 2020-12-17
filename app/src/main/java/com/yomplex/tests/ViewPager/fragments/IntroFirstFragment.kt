package com.yomplex.tests.ViewPager.fragments

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment



import com.yomplex.tests.R
import com.yomplex.tests.ViewPager.util.UiHelper


class IntroFirstFragment : Fragment() {

    private val uiHelper = UiHelper()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.intro_first_fragment, container, false)
        //view.findViewById<TextView>(R.id.notificationAlertsTextView).typeface = uiHelper.getTypeFace(
        //    TypeFaceEnum.HEADING_TYPEFACE, activity!!)
        //view.findViewById<TextView>(R.id.notificationAlertsSubTitleTextView).typeface = uiHelper.getTypeFace(TypeFaceEnum.SEMI_TITLE_TYPEFACE, activity!!)
        return view
    }

}