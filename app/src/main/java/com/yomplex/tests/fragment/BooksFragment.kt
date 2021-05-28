package com.yomplex.tests.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.yomplex.tests.R
import com.yomplex.tests.Service.BooksDownloadService
import com.yomplex.tests.Service.CopyService
import com.yomplex.tests.activity.OpenBookActivity
import com.yomplex.tests.adapter.BooksAdapter
import com.yomplex.tests.database.QuizGameDataBase
import com.yomplex.tests.interfaces.BooksClickListener
import com.yomplex.tests.model.Books
import com.yomplex.tests.utils.*
import kotlinx.android.synthetic.main.books_fragment.*
import kotlinx.android.synthetic.main.books_fragment.view.*
import java.text.SimpleDateFormat


class BooksFragment: Fragment(), BooksClickListener {

    var adapter: BooksAdapter?= null
    var databaseHandler: QuizGameDataBase?= null
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    var unreadList: ArrayList<Books>? = null
    var sharedPrefs: SharedPrefs? = null
    var sound: Boolean = false
    private var positionList: ArrayList<Int>? = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.books_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //gradeTitle = arguments!!.getString(ConstantPath.TITLE_TOPIC)!!
        view.tests.elevation = 15F
        //setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        databaseHandler = QuizGameDataBase(context);
        sharedPrefs = SharedPrefs()
        firebaseAnalytics = Firebase.analytics

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "BooksTab")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "BooksFragment")
        }

        newtab.background = resources.getDrawable(R.drawable.books_stared_border)
        unreadtab.background = resources.getDrawable(R.drawable.books_stared_border)
        starredtab.background = resources.getDrawable(R.drawable.books_stared_border)
        alltab.background = resources.getDrawable(R.drawable.books_btn_enable)

        txt_starred.setTextColor(resources.getColor(R.color.pagination_text))
        txt_unread.setTextColor(resources.getColor(R.color.pagination_text))
        txt_new.setTextColor(resources.getColor(R.color.pagination_text))
        txt_all.setTextColor(resources.getColor(R.color.white))

        unreadList = databaseHandler!!.getAllBooks()

        if(unreadList!!.size == 0){
            startcopyService(activity!!)
        }else{
            val statuslist = databaseHandler!!.getbooksdownloadstatus()
            if(statuslist!!.contains(0)){
                startcopyService(activity!!)
            }else{
                adapter = BooksAdapter(context!!,unreadList!!,this)
                view.rcv_books.addItemDecoration(VerticalSpaceItemDecoration(getResources().getDimension(R.dimen._15sdp)
                    .toInt()));
                view.rcv_books.addItemDecoration(HorigontalSpaceItemDecoration(getResources().getDimension(R.dimen._25sdp)
                    .toInt()));
                //rcv_chapter.addItemDecoration(itemDecorator)
                //rcv_chapter.addItemDecoration(DividerItemDecoration(context,))
                view.rcv_books.adapter = adapter
            }

        }

        /*view.rcv_books.setOnTouchListener(OnTouchListener { v, event ->
            //yourChildView.getParent().requestDisallowInterceptTouchEvent(false)
             when (event.action) {


                MotionEvent.ACTION_DOWN -> {

                    v.background.setColorFilter(Color.parseColor("#80000000"), PorterDuff.Mode.SRC_ATOP)



                    v.invalidate()
                }
                MotionEvent.ACTION_UP -> {
                    v.background.clearColorFilter()
                    v.invalidate()
                }
            }
            false
        })*/

        // row click listener
        view.rcv_books.addOnItemTouchListener(RecyclerTouchListener(activity, view.rcv_books, object : RecyclerTouchListener.ClickListener {
            override fun onClick(view: View?, position: Int) {
                Log.e("Mainactivity", "onClick.....position...$position")
               // val movie = unreadList!!.get(position)
               // Toast.makeText(activity, "onClick..." + movie.getTitle().toString() + " is selected!", Toast.LENGTH_SHORT).show()
            }

            override fun onLongClick(view: View?, position: Int) {
                //val movie = unreadList!!.get(position)
                //Toast.makeText(activity, "onLongClick..." + movie.getTitle().toString() + " is selected!", Toast.LENGTH_SHORT).show()
            }
        }))


        var unreadList1 = databaseHandler!!.getAllBooksWithUnread()
        txt_unread.text = "Unread ("+unreadList1.size+")"
        var unreadList2 = databaseHandler!!.getAllBooksWithStarred()

        txt_starred.text = "Starred ("+unreadList2.size+")"

        var unreadList3 = databaseHandler!!.getAllBooksFromWeek()
        txt_new.text = "New ("+unreadList3.size+")"

        var unreadList4 = databaseHandler!!.getAllBooks()
        txt_all.text = "All ("+unreadList4.size+")"

        alltab.setOnClickListener {
            sound = sharedPrefs?.getBooleanPrefVal(activity!!, ConstantPath.SOUNDS) ?: true
            if(!sound){
                // mediaPlayer = MediaPlayer.create(this,R.raw.amount_low)
                //  mediaPlayer.start()
                if (Utils.loaded) {
                    Utils.soundPool.play(Utils.soundID, Utils.volume, Utils.volume, 1, 0, 1f);
                    Log.e("Test", "Played sound...volume..."+ Utils.volume);
                    //Toast.makeText(context,"end",Toast.LENGTH_SHORT).show()
                }
            }
            newtab.background = resources.getDrawable(R.drawable.books_stared_border)
            unreadtab.background = resources.getDrawable(R.drawable.books_stared_border)
            starredtab.background = resources.getDrawable(R.drawable.books_stared_border)
            alltab.background = resources.getDrawable(R.drawable.books_btn_enable)

            txt_starred.setTextColor(resources.getColor(R.color.pagination_text))
            txt_unread.setTextColor(resources.getColor(R.color.pagination_text))
            txt_new.setTextColor(resources.getColor(R.color.pagination_text))
            txt_all.setTextColor(resources.getColor(R.color.white))

            unreadList!!.clear()
            view.rcv_books.invalidate()
            view.rcv_books.removeAllViews()

            unreadList = databaseHandler!!.getAllBooks()
            txt_all.text = "All ("+unreadList!!.size+")"
            adapter = BooksAdapter(context!!,unreadList!!,this)
            /*view.rcv_books.addItemDecoration(VerticalSpaceItemDecoration(getResources().getDimension(R.dimen._15sdp)
                .toInt()));
            view.rcv_books.addItemDecoration(HorigontalSpaceItemDecoration(getResources().getDimension(R.dimen._25sdp)
                .toInt()));*/
            //rcv_chapter.addItemDecoration(itemDecorator)
            //rcv_chapter.addItemDecoration(DividerItemDecoration(context,))
            view.rcv_books.adapter = adapter
        }

        newtab.setOnClickListener {
            sound = sharedPrefs?.getBooleanPrefVal(activity!!, ConstantPath.SOUNDS) ?: true
            if(!sound){
                // mediaPlayer = MediaPlayer.create(this,R.raw.amount_low)
                //  mediaPlayer.start()
                if (Utils.loaded) {
                    Utils.soundPool.play(Utils.soundID, Utils.volume, Utils.volume, 1, 0, 1f);
                    Log.e("Test", "Played sound...volume..."+ Utils.volume);
                    //Toast.makeText(context,"end",Toast.LENGTH_SHORT).show()
                }
            }
            newtab.background = resources.getDrawable(R.drawable.books_btn_enable)
            unreadtab.background = resources.getDrawable(R.drawable.books_stared_border)
            starredtab.background = resources.getDrawable(R.drawable.books_stared_border)
            alltab.background = resources.getDrawable(R.drawable.books_stared_border)

            txt_starred.setTextColor(resources.getColor(R.color.pagination_text))
            txt_unread.setTextColor(resources.getColor(R.color.pagination_text))
            txt_new.setTextColor(resources.getColor(R.color.white))
            txt_all.setTextColor(resources.getColor(R.color.pagination_text))

            unreadList!!.clear()
            view.rcv_books.invalidate()
            view.rcv_books.removeAllViews()

            unreadList = databaseHandler!!.getAllBooksFromWeek()
            txt_new.text = "New ("+unreadList!!.size+")"
            adapter = BooksAdapter(context!!,unreadList!!,this)
            /*view.rcv_books.addItemDecoration(VerticalSpaceItemDecoration(getResources().getDimension(R.dimen._15sdp)
                .toInt()));
            view.rcv_books.addItemDecoration(HorigontalSpaceItemDecoration(getResources().getDimension(R.dimen._25sdp)
                .toInt()));*/
            //rcv_chapter.addItemDecoration(itemDecorator)
            //rcv_chapter.addItemDecoration(DividerItemDecoration(context,))
            view.rcv_books.adapter = adapter
        }
        unreadtab.setOnClickListener {
            sound = sharedPrefs?.getBooleanPrefVal(activity!!, ConstantPath.SOUNDS) ?: true
            if(!sound){
                // mediaPlayer = MediaPlayer.create(this,R.raw.amount_low)
                //  mediaPlayer.start()
                if (Utils.loaded) {
                    Utils.soundPool.play(Utils.soundID, Utils.volume, Utils.volume, 1, 0, 1f);
                    Log.e("Test", "Played sound...volume..."+ Utils.volume);
                    //Toast.makeText(context,"end",Toast.LENGTH_SHORT).show()
                }
            }
            newtab.background = resources.getDrawable(R.drawable.books_stared_border)
            unreadtab.background = resources.getDrawable(R.drawable.books_btn_enable)
            starredtab.background = resources.getDrawable(R.drawable.books_stared_border)
            alltab.background = resources.getDrawable(R.drawable.books_stared_border)

            txt_starred.setTextColor(resources.getColor(R.color.pagination_text))
            txt_unread.setTextColor(resources.getColor(R.color.white))
            txt_new.setTextColor(resources.getColor(R.color.pagination_text))
            txt_all.setTextColor(resources.getColor(R.color.pagination_text))
            unreadList!!.clear()
            view.rcv_books.invalidate()
            view.rcv_books.removeAllViews()

            unreadList = databaseHandler!!.getAllBooksWithUnread()
            txt_unread.text = "Unread ("+unreadList!!.size+")"
            adapter = BooksAdapter(context!!,unreadList!!,this)
            /*view.rcv_books.addItemDecoration(VerticalSpaceItemDecoration(getResources().getDimension(R.dimen._15sdp)
                .toInt()));
            view.rcv_books.addItemDecoration(HorigontalSpaceItemDecoration(getResources().getDimension(R.dimen._25sdp)
                .toInt()));*/
            //rcv_chapter.addItemDecoration(itemDecorator)
            //rcv_chapter.addItemDecoration(DividerItemDecoration(context,))
            view.rcv_books.adapter = adapter

        }
        starredtab.setOnClickListener {
            sound = sharedPrefs?.getBooleanPrefVal(activity!!, ConstantPath.SOUNDS) ?: true
            if(!sound){
                // mediaPlayer = MediaPlayer.create(this,R.raw.amount_low)
                //  mediaPlayer.start()
                if (Utils.loaded) {
                    Utils.soundPool.play(Utils.soundID, Utils.volume, Utils.volume, 1, 0, 1f);
                    Log.e("Test", "Played sound...volume..."+ Utils.volume);
                    //Toast.makeText(context,"end",Toast.LENGTH_SHORT).show()
                }
            }
            newtab.background = resources.getDrawable(R.drawable.books_stared_border)
            unreadtab.background = resources.getDrawable(R.drawable.books_stared_border)
            starredtab.background = resources.getDrawable(R.drawable.books_btn_enable)
            alltab.background = resources.getDrawable(R.drawable.books_stared_border)

            txt_starred.setTextColor(resources.getColor(R.color.white))
            txt_unread.setTextColor(resources.getColor(R.color.pagination_text))
            txt_new.setTextColor(resources.getColor(R.color.pagination_text))
            txt_all.setTextColor(resources.getColor(R.color.pagination_text))
            unreadList!!.clear()
            view.rcv_books.invalidate()
            view.rcv_books.removeAllViews()
            unreadList = databaseHandler!!.getAllBooksWithStarred()

            txt_starred.text = "Starred ("+unreadList!!.size+")"
            adapter = BooksAdapter(context!!,unreadList!!,this)
            /*view.rcv_books.addItemDecoration(VerticalSpaceItemDecoration(getResources().getDimension(R.dimen._15sdp)
                .toInt()));
            view.rcv_books.addItemDecoration(HorigontalSpaceItemDecoration(getResources().getDimension(R.dimen._25sdp)
                .toInt()));*/
            //rcv_chapter.addItemDecoration(itemDecorator)
            //rcv_chapter.addItemDecoration(DividerItemDecoration(context,))
            view.rcv_books.adapter = adapter



        }

        val format1 = SimpleDateFormat("yyyy-MM-dd")

        var dbdate = databaseHandler!!.getBookContentDate()
        if(dbdate != null){
            try{
                Log.e("tests fragment","db date......"+dbdate)
                Log.e("tests fragment","Utils.date......"+Utils.date)
                Log.e("tests fragment","format1.parse(dbdate)......"+format1.parse(dbdate))
                var s = format1.format(Utils.date)
                Log.e("tests fragment","format1.parse(s)......"+format1.parse(s))


                if(format1.parse(dbdate) < format1.parse(s)){
                    val connectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
                    val isConnected: Boolean = activeNetwork?.isConnected == true
                    Log.d("isConnected",isConnected.toString()+"!")
                    if(isNetworkConnected()) {
                        downloadServiceFromBackground(activity!!,db)
                    }

                }
            }catch (e:Exception){

            }

        }else{
            val connectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            val isConnected: Boolean = activeNetwork?.isConnected == true
            Log.d("isConnected",isConnected.toString()+"!")
            if(isNetworkConnected()) {
                downloadServiceFromBackground(activity!!,db)
            }
        }





    }
    private fun downloadServiceFromBackground(
        mainActivity: Activity, db: FirebaseFirestore
    ) {
        BooksDownloadService.enqueueWork(mainActivity, db)
    }
    fun isNetworkConnected(): Boolean {
        val connectivityManager = activity!!.getSystemService(Context.
        CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnected == true
        return isConnected
    }
    private fun startcopyService(mainActivity: Activity) {
        CopyService.enqueueWork(mainActivity)
    }
    override fun onStarClick(title: String, category: String,status:Int) {
        sound = sharedPrefs?.getBooleanPrefVal(activity!!, ConstantPath.SOUNDS) ?: true
        if(!sound){
            // mediaPlayer = MediaPlayer.create(this,R.raw.amount_low)
            //  mediaPlayer.start()
            if (Utils.loaded) {
                Utils.soundPool.play(Utils.soundID, Utils.volume, Utils.volume, 1, 0, 1f);
                Log.e("Test", "Played sound...volume..."+ Utils.volume);
                //Toast.makeText(context,"end",Toast.LENGTH_SHORT).show()
            }
        }
        databaseHandler!!.updateBooksStarredStatus(status,title,category)
        var unreadList2 = databaseHandler!!.getAllBooksWithStarred()

        txt_starred.text = "Starred ("+unreadList2.size+")"
        //unreadList!!.clear()
        //unreadList = databaseHandler!!.getAllBooksWithUnread()
       // adapter!!.notifyDataSetChanged()
    }
    override fun onReadClick(view:View,book:Books,status:Int) {
        val buttonClick = AlphaAnimation(1f, 0.6f)

        sound = sharedPrefs?.getBooleanPrefVal(activity!!, ConstantPath.SOUNDS) ?: true
        if(!sound){
            // mediaPlayer = MediaPlayer.create(this,R.raw.amount_low)
            //  mediaPlayer.start()
            if (Utils.loaded) {
                Utils.soundPool.play(Utils.soundID, Utils.volume, Utils.volume, 1, 0, 1f);
                Log.e("Test", "Played sound...volume..."+ Utils.volume);
                //Toast.makeText(context,"end",Toast.LENGTH_SHORT).show()
            }
        }
        if(status == 1){
            if(book.bookdownloadstatus == 1){
               // view.startAnimation(buttonClick)
                //buttonEffect(view,true)
                databaseHandler!!.updateBooksReadStatus(status,book.title,book.category)
                var unreadList1 = databaseHandler!!.getAllBooksWithUnread()
                txt_unread.text = "Unread ("+unreadList1.size+")"

                val i = Intent(activity, OpenBookActivity::class.java)
                i.putExtra("title", book.title)
                i.putExtra("category", book.category)
                i.putExtra("foldername", book.folderName)
                startActivity(i)
                (activity as Activity).overridePendingTransition(0, 0)
            }else{

            }

        }else{
            if(book.bookdownloadstatus == 1){
               // view.startAnimation(buttonClick)
                //buttonEffect(view,true)
                val i = Intent(activity, OpenBookActivity::class.java)
                i.putExtra("title", book.title)
                i.putExtra("category", book.category)
                i.putExtra("foldername", book.folderName)
                startActivity(i)
                (activity as Activity).overridePendingTransition(0, 0)
            }else{

            }

        }

    }

    fun buttonEffect(button: View,isNext:Boolean) {

        button.setOnTouchListener { v, event ->

            when (event.action) {


                MotionEvent.ACTION_DOWN -> {
                    //if (isNext){
                        v.background.setColorFilter(Color.parseColor("#80000000"), PorterDuff.Mode.SRC_ATOP)
                    /*}else{
                        v.background.setColorFilter(Color.parseColor("#FFBDB6F3"), PorterDuff.Mode.SRC_ATOP)
                    }*/

                    v.invalidate()

                }
                MotionEvent.ACTION_UP -> {
                    v.background.clearColorFilter()
                    v.invalidate()

                }
            }
            false
        }
    }
}

