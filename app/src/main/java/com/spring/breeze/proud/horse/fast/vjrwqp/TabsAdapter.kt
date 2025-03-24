package com.spring.breeze.proud.horse.fast.vjrwqp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.spring.breeze.proud.horse.fast.R
import com.spring.breeze.proud.horse.fast.vjiropa.verv.TbasWebBean

class TabsAdapter(private var dataList: MutableList<TbasWebBean>) :
    RecyclerView.Adapter<TabsAdapter.ViewHolder>() {
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
        val imgDelete: ImageView = itemView.findViewById(R.id.img_delete)
        val imgShow: ImageView = itemView.findViewById(R.id.img_show)
        val llDelete: LinearLayout = itemView.findViewById(R.id.ll_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view =
            inflater.inflate(R.layout.item_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        Log.e("TAG", "onBindViewHolder: data=${data.webImage}")
        holder.tvTitle.text = data.webUrl
        data.webImage.let {
            if (it.isNotBlank()) {
                holder.imgShow.apply {
                    Glide.with(context)
                        .load(data.webImage)
                        .into(this)
                }
            } else {
                holder.imgShow.setImageResource(R.mipmap.icon_start_web)
            }
        }
        holder.itemView.setOnClickListener {
            listener?.onItemClick(data.id)
        }
        holder.imgDelete.setOnClickListener {
            listener?.onItemDelete(data.id)
        }
        holder.llDelete.setOnClickListener {
            listener?.onItemDelete(data.id)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }



    fun submitList(newList: MutableList<TbasWebBean>) {
        dataList = newList.toMutableList()
        notifyDataSetChanged()
    }



}