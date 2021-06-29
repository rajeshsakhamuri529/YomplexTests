package com.yomplex.tests.adapter


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.caverock.androidsvg.SVG
import com.yomplex.tests.R
import com.yomplex.tests.database.QuizGameDataBase
import com.yomplex.tests.interfaces.BooksClickListener
import com.yomplex.tests.model.Books
import com.yomplex.tests.utils.Utils
import kotlinx.android.synthetic.main.books_list_item.view.*
import java.io.File
import java.io.FileInputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class BooksAdapter(val context: Context, val bookslist: ArrayList<Books>, val booksClickListener: BooksClickListener) :
    RecyclerView.Adapter<BooksAdapter.BooksViewHolder>() {


    var databaseHandler: QuizGameDataBase?= null

    override fun getItemCount(): Int {
        return bookslist.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksViewHolder {
        databaseHandler = QuizGameDataBase(context);
        return BooksAdapter.BooksViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.books_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BooksViewHolder, position: Int) {

        var books = bookslist[position]
        if(books.readstatus == 0){
            holder.unreadRL1.visibility = View.VISIBLE
        }else{
            holder.unreadRL1.visibility = View.INVISIBLE
        }
        var dr: Drawable? = null
        try{
           // Log.e("books adapter","books.thumbnail...."+books.thumbnail)
            if(books.thumbnail != null){
                //Log.e("books adapter","books.thumbnail...if block....."+books.thumbnail)
                var path = File(books.thumbnail)

                //Log.e("books adapter","path.exists()...."+path.exists())
                if(path.exists()){
                    Log.e("books adapter","books.thumbnail...if path exist....."+books.thumbnail)
                    val fileInputStream = FileInputStream(path)

                    val svg: SVG = SVG.getFromInputStream(fileInputStream)



                    // Create a Bitmap to render our SVG to
                    val bm = Bitmap.createBitmap((svg.documentWidth).toInt(), (svg.documentHeight).toInt(), Bitmap.Config.ARGB_8888)
                    // Create a Canvas to use for rendering
                    val canvas = Canvas(bm)
                    // Now render the SVG to the Canvas

                    canvas.save();
                    // Now render the SVG to the Canvas
                    svg.renderToCanvas(canvas)

                    canvas.restore()


                    dr = BitmapDrawable(bm)
                    holder.bookRL.background = dr

                }else{
                    //Log.e("books adapter","books.thumbnail...else path exist....."+books.thumbnail)
                    // Log.e("books adapter","books category...."+books.category)
                    var str:String = "Books1/"+books.category+"/"+books.folderName+"/thumbnail.svg"
                    //Log.e("books adapter","str...."+str)
                    try{
                        val svg: SVG = SVG.getFromAsset(context.assets,str)



                        // Create a Bitmap to render our SVG to
                        val bm = Bitmap.createBitmap((svg.documentWidth).toInt(), (svg.documentHeight).toInt(), Bitmap.Config.ARGB_8888)
                        // Create a Canvas to use for rendering
                        val canvas = Canvas(bm)
                        // Now render the SVG to the Canvas

                        canvas.save();
                        // Now render the SVG to the Canvas
                        svg.renderToCanvas(canvas)

                        canvas.restore()


                        dr = BitmapDrawable(bm)
                        holder.bookRL.background = dr

                    }catch (e:Exception){
                        //Log.e("books adapter","books.thumbnail...if path exist....."+books.thumbnail)
                        holder.unreadRL1.visibility = View.INVISIBLE
                        holder.bookRL.background = context.resources.getDrawable(R.drawable.ic_dummy)
                    }

                }
            }else{
                 //Log.e("books adapter","books category...else block...."+books.category)
                var str:String = "Books1/"+books.category+"/"+books.folderName+"/thumbnail.svg"
                //Log.e("books adapter","str...."+str)
                try{
                    val svg: SVG = SVG.getFromAsset(context.assets,str)



                    // Create a Bitmap to render our SVG to
                    val bm = Bitmap.createBitmap((svg.documentWidth).toInt(), (svg.documentHeight).toInt(), Bitmap.Config.ARGB_8888)
                    // Create a Canvas to use for rendering
                    val canvas = Canvas(bm)
                    // Now render the SVG to the Canvas

                    canvas.save();
                    // Now render the SVG to the Canvas
                    svg.renderToCanvas(canvas)

                    canvas.restore()


                    dr = BitmapDrawable(bm)
                    holder.bookRL.background = dr

                }catch (e:Exception){
                    holder.unreadRL1.visibility = View.INVISIBLE
                    holder.bookRL.background = context.resources.getDrawable(R.drawable.ic_dummy)
                }
            }
        }catch (e:Exception){
             Log.e("books adapter","catch block......."+books.category)
            var str:String = "Books1/"+books.category+"/"+books.folderName+"/thumbnail.svg"
            //Log.e("books adapter","str...."+str)
            try{
                val svg: SVG = SVG.getFromAsset(context.assets,str)
                // Create a Bitmap to render our SVG to
                val bm = Bitmap.createBitmap((svg.documentWidth).toInt(), (svg.documentHeight).toInt(), Bitmap.Config.ARGB_8888)
                // Create a Canvas to use for rendering
                val canvas = Canvas(bm)
                // Now render the SVG to the Canvas

                canvas.save();
                // Now render the SVG to the Canvas
                svg.renderToCanvas(canvas)

                canvas.restore()


                dr = BitmapDrawable(bm)
                holder.bookRL.background = dr

            }catch (e:Exception){
                holder.unreadRL1.visibility = View.INVISIBLE
                holder.bookRL.background = context.resources.getDrawable(R.drawable.ic_dummy)
            }
        }





            /*// /data/data/com.yomplex.tests/cache/Books/OTHER TOPICS/axiomatic-probability/thumbnail.svg
            if (books.category.equals("ALGEBRA")) {
                // GlideToVectorYou.justLoadImageAsBackground(context as Activity?, Uri.parse(books.thumbnail), holder.bookRL)
                holder.bookRL.background = dr
                //context.resources.getDrawable(R.drawable.ic_alg_book)
                //context.resources.getDrawable(R.drawable.ic_thumbnail)
                // holder.topRL.background = context.resources.getDrawable(R.drawable.top_left_right_rounded)
                // holder.bottomRL.background = context.resources.getDrawable(R.drawable.bottom_left_right_rounded)
            } else if (books.category.equals("GEOMETRY")) {
                holder.bookRL.background = dr
                //context.resources.getDrawable(R.drawable.ic_thumbnail)
                // holder.topRL.background = context.resources.getDrawable(R.drawable.g_top_rounded)
                // holder.bottomRL.background = context.resources.getDrawable(R.drawable.g_bottom_rounded)
            } else if (books.category.equals("CALCULUS")) {
                holder.bookRL.background = dr
                *//*Glide.with(context).load(R.drawable.ic_alg_book).into(object : SimpleTarget<Drawable?>() {
                *//**//*override fun onResourceReady(resource: Drawable?, transition: Transition<in Drawable?>?) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        yourRelativeLayout.setBackground(resource)
                    }
                }*//**//*

                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                    holder.bookRL.background = resource
                            //context.resources.getDrawable(R.drawable.ic_geo_book)
                }


            })*//*

                // holder.topRL.background = context.resources.getDrawable(R.drawable.c_top_rounded)
                // holder.bottomRL.background = context.resources.getDrawable(R.drawable.c_bottom_rounded)
            } else {
                holder.bookRL.background = dr
                //  holder.topRL.background = context.resources.getDrawable(R.drawable.o_top_rounded)
                //  holder.bottomRL.background = context.resources.getDrawable(R.drawable.o_bottom_rounded)
            }*/

        holder.categoryTxt.text = books.category
        holder.titleTxt.text = books.title




       // Log.e("books adapter","books.starredstatus ......"+books.starredstatus)
        if(books.starredstatus == 1){
          //  Log.e("books adapter","books.starredstatus ...if..."+books.starredstatus)
            holder.starImage.background = context.resources.getDrawable(R.drawable.ic_star_light_selected)
        }else{
          //  Log.e("books adapter","books.starredstatus ...else..."+books.starredstatus)
            holder.starImage.background = context.resources.getDrawable(R.drawable.ic_star_light_unselected)
        }

        val format1 = SimpleDateFormat("yyyy-MM-dd")
        var dbdate = books.publishedOn
        //Log.e("books adapter","dbdate ......"+dbdate)
        //var s = format1.format(Utils.date)
        val calendar = Calendar.getInstance()
        calendar.setTime(Utils.date)
        calendar.add(Calendar.DATE, -7)
        System.out.println("Date = " + calendar.getTime())

        var s = format1.format(calendar.getTime())
        /*Log.e("books adapter","calendar.getTime() ......"+s)
        Log.e("books adapter","format1.parse(s) ......"+format1.parse(s))
        Log.e("books adapter","format1.parse(dbdate)) ......"+format1.parse(dbdate))*/
        if(format1.parse(s) < format1.parse(dbdate)){
            holder.newtagRL1.visibility = View.VISIBLE
        }else{
            holder.newtagRL1.visibility = View.GONE
        }

        holder.starredRL.setOnClickListener {

            var status = databaseHandler!!.getBooksStarredStatus(books.title,books.category)
            if(status == 1){

                booksClickListener.onStarClick(books.title,books.category,0)
                holder.starImage.background = context.resources.getDrawable(R.drawable.ic_star_light_unselected)
            }else{
               // Log.e("books adapter","else ......")
                booksClickListener.onStarClick(books.title,books.category,1)
                holder.starImage.background = context.resources.getDrawable(R.drawable.ic_star_light_selected)
            }
        }
        holder.starImage.setOnClickListener {
            var status = databaseHandler!!.getBooksStarredStatus(books.title,books.category)
            if(status == 1){

                booksClickListener.onStarClick(books.title,books.category,0)
                holder.starImage.background = context.resources.getDrawable(R.drawable.ic_star_light_unselected)
            }else{
                // Log.e("books adapter","else ......")
                booksClickListener.onStarClick(books.title,books.category,1)
                holder.starImage.background = context.resources.getDrawable(R.drawable.ic_star_light_selected)
            }
        }
       // holder.rl_single_topics.elevation = 15F
        //holder.rl_single_topics.translationZ = 10f
        holder.rl_single_topics.setOnClickListener {
            if(books.readstatus == 0){
               // buttonEffect(holder.rl_single_topics,true)
                booksClickListener.onReadClick(holder.rl_single_topics,books,1)
                holder.unreadRL1.visibility = View.INVISIBLE
            }else{
                //buttonEffect(holder.rl_single_topics,true)
                booksClickListener.onReadClick(holder.rl_single_topics,books,0)
            }
        }
        holder.bookRL.setOnClickListener {
            if(books.readstatus == 0){
                //buttonEffect(holder.bookRL,true)
                booksClickListener.onReadClick(holder.bookRL,books,1)
                holder.unreadRL1.visibility = View.INVISIBLE
            }else{
                //buttonEffect(holder.bookRL,true)
                booksClickListener.onReadClick(holder.bookRL,books,0)
            }
        }
//790BF8
        /*holder.rl_single_topics.setOnTouchListener { v, event ->

            when (event.action) {


                MotionEvent.ACTION_DOWN -> {
                    //if (isNext){
                    v.background.setColorFilter(Color.parseColor("#80000000"), PorterDuff.Mode.SRC_ATOP)
                    *//*}else{
                        v.background.setColorFilter(Color.parseColor("#FFBDB6F3"), PorterDuff.Mode.SRC_ATOP)
                    }*//*

                    v.invalidate()
                }
                MotionEvent.ACTION_UP -> {
                    v.background.clearColorFilter()
                    v.invalidate()
                }
            }
            false
        }
        holder.bookRL.setOnTouchListener { v, event ->

            when (event.action) {


                MotionEvent.ACTION_DOWN -> {
                    //if (isNext){
                        v.background.setColorFilter(Color.parseColor("#80000000"), PorterDuff.Mode.SRC_ATOP)
                    *//*}else{
                        v.background.setColorFilter(Color.parseColor("#FFBDB6F3"), PorterDuff.Mode.SRC_ATOP)
                    }*//*

                    v.invalidate()
                }
                MotionEvent.ACTION_UP -> {
                    v.background.clearColorFilter()
                    v.invalidate()
                }
            }
            false
        }*/
    }
    fun buttonEffect(button: View,isNext:Boolean) {



    }

    class BooksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bottomRL = itemView.bottomRL
        val categoryTxt = itemView.categoryTxt
        val titleTxt = itemView.titleTxt
        val topRL = itemView.topRL
        val bookRL = itemView.bookRL
        val progress_bar = itemView.progress_bar



        val unreadRL1 = itemView.unreadRL1
        val newtagRL1 = itemView.newtagRL1
        val starredRL = itemView.starredRL1
        val starImage = itemView.starImage1
        val rl_single_topics = itemView.rl_single_topics

        

    }
}