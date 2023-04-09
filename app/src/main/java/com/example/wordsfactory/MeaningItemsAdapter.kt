package com.example.wordsfactory

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.wordsfactory.databinding.MeaningsItemsBinding


class MeaningItemsAdapter(private val meaningsItem: List<Definitions>):RecyclerView.Adapter<MeaningItemsAdapter.MeaningItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeaningItemViewHolder {
        return MeaningItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.meanings_items, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return meaningsItem.size
    }

    override fun onBindViewHolder(holder: MeaningItemViewHolder, position: Int) {
        holder.bind(meaningsItem[position])
    }
    inner class MeaningItemViewHolder(view: View):RecyclerView.ViewHolder(view){
        private val viewBinding=MeaningsItemsBinding.bind(view)
        private var text=""
        private var spannableString=SpannableString("")
        private val foregroundColorSpanCyan=ForegroundColorSpan(ContextCompat.getColor(view.context,
            androidx.appcompat.R.color.material_blue_grey_950))

        fun bind(meaning: Definitions) {
            if(meaning.example==null){
                text="Example"
            }
            else{
                text="Example: "+meaning.example
            }
            spannableString= SpannableString(text)
            spannableString.setSpan(foregroundColorSpanCyan,0,8,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            viewBinding.textMeaning.text=meaning.definition
            viewBinding.textExample.text=spannableString
        }
    }
}
