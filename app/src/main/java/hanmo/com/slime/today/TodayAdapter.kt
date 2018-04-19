package hanmo.com.slime.today

import android.animation.Animator
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import hanmo.com.slime.R
import hanmo.com.slime.constants.Type
import hanmo.com.slime.db.FavoriteSlime
import hanmo.com.slime.db.RealmHelper
import hanmo.com.slime.model.SlimeData
import hanmo.com.slime.model.SlimeImage
import hanmo.com.slime.util.DLog
import kotlinx.android.synthetic.main.item_search_slime.view.*

/**
 * Created by hanmo on 2018. 4. 8..
 */
class TodayAdapter(val slime: ArrayList<SlimeData?>, private val type: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        DLog.e(type.toString())

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

        fun bindView(slime: SlimeData?) {
            with(itemView) {
                this.slimeName.text = slime?.slimeName
                Picasso.with(context).load(SlimeImage(slime?.slimeName!!).getImageResourceId(context)).into(this.slimeImage)

                val favoriteSlime = RealmHelper.instance.queryAll(FavoriteSlime::class.java)
                favoriteSlime?.forEach {
                    when(slime.slimeId) {
                        it.slimeId -> {
                            this.hasFavorite.isChecked = true
                        }
                    }
                }

                /*this.playVideoButton.setOnClickListener {
                    val detailIntent = SlimeDetailActivity.newIntent(context)
                    context.startActivity(detailIntent)
                }*/

                this.hasFavorite.setOnClickListener{
                    if (this.hasFavorite.isChecked){

                        RealmHelper.instance.insertFavoriteData(slime.slimeId, slime.slimeName)

                        favoriteLottie.visibility = View.VISIBLE
                        favoriteLottie.playAnimation()
                        favoriteLottie.addAnimatorListener(object : Animator.AnimatorListener {
                            override fun onAnimationRepeat(animation: Animator?) {

                            }

                            override fun onAnimationEnd(animation: Animator?) {
                                favoriteLottie.visibility = View.INVISIBLE
                            }

                            override fun onAnimationCancel(animation: Animator?) {

                            }

                            override fun onAnimationStart(animation: Animator?) {

                            }

                        })

                    } else {
                        //데이터가 없으면 예외처리 해주어야 한다.
                        RealmHelper.instance.deleteFavoriteData(slime.slimeId)

                    }
                }
            }
        }

    }

    inner class SlimeCardViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_card_search_slime, parent, false)) {

        fun bindView(slime: SlimeData?) {
            with(itemView){
                this.slimeName.text = slime?.slimeName
                Picasso.with(context).load(SlimeImage(slime?.slimeName!!).getImageResourceId(context)).into(this.slimeImage)
            }
        }
    }

}