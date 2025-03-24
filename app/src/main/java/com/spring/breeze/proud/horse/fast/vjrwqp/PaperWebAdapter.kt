package com.spring.breeze.proud.horse.fast.vjrwqp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.spring.breeze.proud.horse.fast.R
import com.spring.breeze.proud.horse.fast.vjiropa.verv.WkvrnBean
import org.jsoup.Jsoup
import java.io.InputStream
import java.net.URL

class PaperWebAdapter(private var dataList: MutableList<WkvrnBean>) :
    RecyclerView.Adapter<PaperWebAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(date: String)
        fun onItemDelete(date: String)
    }



    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val tvUrl: TextView = itemView.findViewById(R.id.tv_url)
        val imgDelete: ImageView = itemView.findViewById(R.id.img_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view =
            inflater.inflate(R.layout.lay_web_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        setVisibility(data.isGone, holder.itemView)
        holder.tvTitle.text = data.title
        holder.tvUrl.text = data.url

        holder.itemView.setOnClickListener {
            listener?.onItemClick(data.date)
        }
        holder.imgDelete.setOnClickListener {
            listener?.onItemDelete(data.date)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    private fun setVisibility(isVisible: Boolean, itemView: View) {
        val param = itemView.layoutParams as RecyclerView.LayoutParams
        if (!isVisible) {
            param.height = LinearLayout.LayoutParams.WRAP_CONTENT
            param.width = LinearLayout.LayoutParams.MATCH_PARENT
            itemView.visibility = View.VISIBLE
        } else {
            itemView.visibility = View.GONE
            param.height = 0
            param.width = 0
        }
        itemView.layoutParams = param
    }
    fun submitList(newList: List<WkvrnBean>) {
        dataList = newList.toMutableList()
        notifyDataSetChanged()
    }

}