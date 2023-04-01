package com.example.group_project.ui.Chat
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.group_project.R

class UserHolder(itemView: View) : ViewHolder(itemView) {
    //views

    var mImageFlag: ImageView
    var mUserName: TextView
    var mUserInfo: TextView
    var mChat_num: TextView
    var mRelative: RelativeLayout

    init {
        mImageFlag = itemView.findViewById(R.id.cardImageflag)
        mUserName = itemView.findViewById(R.id.cardUserName)
        mUserInfo = itemView.findViewById(R.id.cardUserInfo)
        mChat_num = itemView.findViewById(R.id.Chat_num)
        mRelative = itemView.findViewById(R.id.relative)
    }
}