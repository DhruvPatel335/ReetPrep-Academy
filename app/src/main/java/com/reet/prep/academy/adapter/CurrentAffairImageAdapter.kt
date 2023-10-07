package com.reet.prep.academy.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.reet.prep.academy.R

class CurrentAffairImageAdapter(private val context: Context, private val sliderItems:List<String>) :PagerAdapter(){
    private val mLayoutInflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return sliderItems.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as CardView
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = mLayoutInflater.inflate(R.layout.current_affair_images_item, container, false)
        val imageView: ImageView = itemView.findViewById(R.id.ivImageView)
        Glide.with(context).load(Uri.parse(sliderItems[position])).into(imageView)

        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as CardView)
    }
}