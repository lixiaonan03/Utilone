package com.lxn.utilone.activity.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.lxn.utilone.databinding.ItemRvtest1Binding
import com.lxn.utilone.util.Log

/**
 * @author：李晓楠
 * 时间：2023/1/31 17:26
 */
class Test1Adapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: ArrayList<Int> = ArrayList()

    fun setList(list: ArrayList<Int>) {
        this.list = list
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.i("lxnrv","onCreateViewHolder====$viewType")
        return ItemRvtest1Holder(ItemRvtest1Binding.inflate(LayoutInflater.from(parent.context), parent, false))

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.i("lxnrv","onBindViewHolder====$position==${holder.hashCode()}====${holder.toString()}")
        if (holder is ItemRvtest1Holder) {
            val viewBinding = holder.viewBinding
            viewBinding.tvName.text = list[position].toString()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ItemRvtest1Holder(val viewBinding: ItemRvtest1Binding) : RecyclerView.ViewHolder(viewBinding.root)
}