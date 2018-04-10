package hanmo.com.slime.favorite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import hanmo.com.slime.R
import hanmo.com.slime.db.FavoriteSlime
import hanmo.com.slime.db.RealmHelper
import hanmo.com.slime.model.SlimeData
import kotlinx.android.synthetic.main.activity_favorite.*

/**
 * Created by hanmo on 2018. 4. 7..
 */
class FavoriteActivity : AppCompatActivity() {

    private lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager

    companion object {

        fun newIntent(context: Context) : Intent {
            val intent = Intent(context, FavoriteActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        val slimeData = ArrayList<SlimeData?>()

        with(favoriteList) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            layoutManager = staggeredGridLayoutManager
            getFavoriteSlimeData(slimeData)
            adapter = FavoriteAdapter(slimeData)
        }
    }

    private fun getFavoriteSlimeData(slimeData: ArrayList<SlimeData?>) {
        val slime = RealmHelper.instance.queryAll(FavoriteSlime::class.java)
        slime?.forEach {
            slimeData.add(SlimeData(it.id, it.slimeName))
        }
    }
}