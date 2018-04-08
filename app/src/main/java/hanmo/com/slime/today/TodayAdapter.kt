package hanmo.com.slime.today

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import hanmo.com.slime.R
import hanmo.com.slime.model.SlimeImage
import kotlinx.android.synthetic.main.item_search_slime.view.*

/**
 * Created by hanmo on 2018. 4. 8..
 */
class TodayAdapter(val slimeName: Array<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SlimeHolder(parent)

    }

    override fun getItemCount(): Int {
        return 6
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is SlimeHolder -> holder.bindView(slimeName[position])
        }
    }

    inner class SlimeHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_search_slime, parent, false)) {

        fun bindView(slime: String) {
            with(itemView){
                adapterPosition
                itemView.slimeName.text = slime
                Picasso.with(context).load(SlimeImage(slime).getImageResourceId(context)).into(itemView.slimeImage)


            }

        }

    }

}