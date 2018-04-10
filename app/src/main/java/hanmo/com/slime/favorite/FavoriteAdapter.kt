package hanmo.com.slime.favorite

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import hanmo.com.slime.R
import hanmo.com.slime.model.SlimeData
import hanmo.com.slime.model.SlimeImage
import kotlinx.android.synthetic.main.item_search_slime.view.*

/**
 * Created by hanmo on 2018. 4. 10..
 */
class FavoriteAdapter(val slime: ArrayList<SlimeData?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FavoriteHolder(parent)
    }

    override fun getItemCount(): Int {
        return slime.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is FavoriteHolder -> { holder.bindView(slime[position]) }
        }
    }

    inner class FavoriteHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_card_search_slime, parent, false)) {

        fun bindView(slime: SlimeData?) {
            with(itemView){
                this.slimeName.text = slime?.slimeName
                Picasso.with(context).load(SlimeImage(slime?.slimeName!!).getImageResourceId(context)).into(this.slimeImage)
            }

        }
    }

}