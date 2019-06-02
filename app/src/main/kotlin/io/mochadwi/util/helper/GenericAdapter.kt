package io.mochadwi.util.helper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import io.mochadwi.util.base.BaseBindableAdapter

abstract class GenericAdapter<DATA> :
        RecyclerView.Adapter<RecyclerView.ViewHolder>,
        BaseBindableAdapter<DATA> {


    var listItems: List<DATA>

    constructor(listItems: List<DATA>) {
        this.listItems = listItems
        notifyDataSetChanged()
    }

    constructor() {
        listItems = emptyList()
        notifyDataSetChanged()
    }

    override fun setData(items: List<DATA>) {
        this.listItems = items
        notifyDataSetChanged()
    }

    // TODO: To add header?
    override fun setHeader(items: DATA) {
        (this.listItems as MutableList<DATA>).add(items)
        notifyItemInserted(0)
    }

    // TODO: To add footer?
    override fun setFooter(items: DATA) {
        (this.listItems as MutableList<DATA>).add(items)
        notifyItemInserted(this.listItems.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return getViewHolder(
                DataBindingUtil.inflate(LayoutInflater.from(parent.context)
                        , viewType
                        , parent
                        , false)!!)
    }

    @SuppressWarnings("Unchecked cast")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? BaseBindableAdapter<DATA>)?.bind(listItems[position])
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return getLayoutId(position, listItems[position])
    }

    protected abstract fun getLayoutId(position: Int, obj: DATA): Int

    // TODO: Use generic ViewDataBinding
    abstract fun getViewHolder(viewBinding: ViewDataBinding): RecyclerView.ViewHolder
}