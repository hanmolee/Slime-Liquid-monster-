package hanmo.com.slime.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hanmo.com.slime.R
import hanmo.com.slime.model.History
import kotlinx.android.synthetic.main.item_searchhistory.view.*

/**
 * Created by hanmo on 2018. 4. 8..
 */
class HistoryAdapter(val item : ArrayList<History>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is SearchHistoryHolder -> holder.bindView(item[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SearchHistoryHolder(parent)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    inner class SearchHistoryHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_searchhistory, parent, false)), View.OnClickListener {

        fun bindView(item: History) {
            itemView.setOnClickListener(this)
            with(itemView){
                itemView.itemHisrotyName.text = item.name

            }

        }

        override fun onClick(v: View?) {
            when(v) {
                itemView -> {
                    //히스토리 아이템 클릭 시 디테일 뷰 연동
                }
            }
        }

    }

}