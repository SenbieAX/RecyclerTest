package com.strelkovax.testrecycler.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.strelkovax.testrecycler.R
import com.strelkovax.testrecycler.pojo.Element
import kotlinx.android.synthetic.main.item_element.view.*

class ElementAdapter(private val context: Context) :
    RecyclerView.Adapter<ElementAdapter.ElementViewHolder>() {

    var onButtonClickListener: OnButtonClickListener? = null
    private var lastPosition = -1

    var elementList: ArrayList<Element> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElementViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_element, parent, false)
        return ElementViewHolder(view)
    }

    override fun getItemCount() = elementList.size

    override fun onBindViewHolder(holder: ElementViewHolder, position: Int) {
        val element = elementList[position]
        holder.tvId.text = element.id.toString()
        holder.buttonDelete.setOnClickListener {
            onButtonClickListener?.onButtonClick(holder.itemView, element, position)
        }
        setAddAnimation(holder.itemView)
    }

    inner class ElementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvId: TextView = itemView.tvId
        val buttonDelete: Button = itemView.buttonDelete
    }

    interface OnButtonClickListener {
        fun onButtonClick(viewToAnimate: View, element: Element, position: Int)
    }

    fun remove(viewToAnimate: View, element: Element, position: Int) {
        elementList.remove(element)
        val animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
        viewToAnimate.startAnimation(animation)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    private fun setAddAnimation(viewToAnimate: View) {
//        if (position > lastPosition) {
        val animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
        viewToAnimate.startAnimation(animation)
//            lastPosition = position
    }
}

