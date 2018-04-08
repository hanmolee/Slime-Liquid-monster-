package hanmo.com.slime.today

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import hanmo.com.slime.R
import hanmo.com.slime.constants.Type
import hanmo.com.slime.model.SlimeImage
import kotlinx.android.synthetic.main.item_search_slime.view.*

/**
 * Created by hanmo on 2018. 4. 8..
 */
class TodayAdapter(val slime: ArrayList<String?>, private val type : Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(type) {
            Type.LinearList -> return SlimeHolder(parent)
            Type.CardList -> return SlimeCardViewHolder(parent)
        }

        throw IllegalArgumentException()

    }

    override fun getItemCount(): Int {
        return slime.size
    }

    override fun getItemViewType(position: Int): Int {
        return type
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is SlimeHolder -> holder.bindView(slime[position])
            is SlimeCardViewHolder -> holder.bindView(slime[position])
        }
    }

    inner class SlimeHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_search_slime, parent, false)) {

        fun bindView(slime: String?) {
            with(itemView){
                itemView.slimeName.text = slime
                Picasso.with(context).load(SlimeImage(slime!!).getImageResourceId(context)).into(itemView.slimeImage)

            }
        }
    }

    inner class SlimeCardViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_card_search_slime, parent, false)) {

        fun bindView(slime: String?) {
            with(itemView){
                itemView.slimeName.text = slime
                Picasso.with(context).load(SlimeImage(slime!!).getImageResourceId(context)).into(itemView.slimeImage)
            }
        }
    }

}