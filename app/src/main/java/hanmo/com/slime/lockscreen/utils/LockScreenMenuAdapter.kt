package hanmo.com.slime.lockscreen.utils

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import hanmo.com.slime.R
import kotlinx.android.synthetic.main.item_lockscreen_menu.view.*

/**
 * Created by hanmo on 2018. 4. 23..
 */
class LockScreenMenuAdapter(val menuList: Array<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LcMenuViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is LcMenuViewHolder -> {
                holder.bindView(menuList[position])
            }
        }
    }

    inner class LcMenuViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_lockscreen_menu, parent, false)) {

        fun bindView(item: String) {
            with(itemView){
                this.menuTitle.text = item
            }
        }
    }


}