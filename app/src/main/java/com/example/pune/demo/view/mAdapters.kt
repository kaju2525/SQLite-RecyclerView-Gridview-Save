package com.example.pune.demo.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.pune.demo.R
import com.example.pune.demo.model.DataModel
import kotlinx.android.synthetic.main.ist_item.view.*
import java.io.File


class mAdapters(private val list : ArrayList<DataModel>) : RecyclerView.Adapter<mAdapters.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.ist_item,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(list[position],position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),View.OnClickListener {
        private val context: Context =itemView.context

        fun bindItems(model: DataModel, position: Int){
            itemView.setOnClickListener(this) // bind the listener

            if(model.avatarPath !="1") {
                itemView.add_image.setImageBitmap(load(model.avatarPath!!))
                itemView.tv_title.text = model.name
            }else{
              if(position==0) {
                  itemView.tv_title.text = model.name
                  itemView.add_image.setImageResource(R.drawable.ic_add_circle)
                  itemView.add_image.setOnClickListener {
                      if (context is MainActivity) {
                          context.addData()
                      }
                  }
              }}
        }

        override fun onClick(v: View?) {
         //  Toast.makeText(context,catg_name,Toast.LENGTH_SHORT).show()
        }


        fun ImageView.loadFromUrl(imageUrl:String){
            Glide.with(this).load(imageUrl).into(this)
        }


        private fun load(path:String):Bitmap{

            var bitmap:Bitmap?=null
            val imgFile = File("/storage/sdcard0/storage/sdcard0/MyEmployee/$path.jpg")
            if (imgFile.exists()) {
                bitmap=BitmapFactory.decodeFile(imgFile.absolutePath)
            }
            return bitmap!!
        }
    }



}

