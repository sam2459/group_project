package com.example.group_project.ui.Chat
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ColorSpace
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.group_project.R
import java.io.ByteArrayOutputStream
import java.util.*
class UserAdapter (val c: Context, var models: ArrayList<friend>) :
    RecyclerView.Adapter<UserHolder>() {
    private var onItemClickListener: ItemClickListener? = null
    fun setOnItemClickListener(onItemClickListener: ItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder
    {
        // convert xml to view obj
        val v =
            LayoutInflater.from(parent.context).inflate(
                R.layout.user_item,
                null)
        return UserHolder(v)
    }
    override fun onBindViewHolder(
        holder: UserHolder,
        position: Int
    ) { //bind data to our views
        holder.mUserName.text = models[position].name
        holder.mUserInfo.text = models[position].msg
        holder.mChat_num.text = models[position].num.toString()
        //holder.mImageFlag.setImageResource(models[position].image)
        holder.mRelative.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val targetname = models[holder.getAdapterPosition()].name
                val stream = ByteArrayOutputStream()
                // compress image
                val bytes = stream.toByteArray()
                // intent, put data to intent, start activity
                val intent = Intent(c, Chat::class.java)
                intent.putExtra("tName", targetname)
                c.startActivity(intent)
            }
        })
        // Animation
        val animation = AnimationUtils.loadAnimation(c,
            android.R.anim.slide_in_left)
        holder.itemView.startAnimation(animation)
    }
    override fun getItemCount(): Int {
        return models.size
    }
}