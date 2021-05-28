package com.yomplex.tests.interfaces

import android.view.View
import com.yomplex.tests.model.Books

interface BooksClickListener {

    fun onStarClick(title: String,category:String,status:Int)

    fun onReadClick(view: View, books:Books,status:Int)
}