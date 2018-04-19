package hanmo.com.slime.menu

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jakewharton.rxbinding2.view.clicks
import hanmo.com.slime.R
import hanmo.com.slime.db.LockScreenTable
import hanmo.com.slime.db.RealmHelper
import hanmo.com.slime.db.Slime
import hanmo.com.slime.favorite.FavoriteActivity
import hanmo.com.slime.lockscreen.utils.utils.LockScreen
import hanmo.com.slime.search.SearchActivity
import hanmo.com.slime.today.TodayActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var compositeDisposable : CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        compositeDisposable = CompositeDisposable()
        
        insertSlimeSampleData()
        setupButton()
        setLockScreen()

    }

    private fun setLockScreen() {
        LockScreen.instance.init(this, true)
        switch_locksetting.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                LockScreen.instance.active()
            } else {
                LockScreen.instance.deactivate()
            }
        }

    }

    private fun insertSlimeSampleData() {
        val initSample = RealmHelper.instance.queryAll(Slime::class.java)
        if (initSample?.isEmpty()!!) {
            val slimeNameArray = arrayOf("slime1", "slime2", "slime3", "slime4", "slime5", "slime6", "slime1", "slime2", "slime3", "slime4", "slime5", "slime6")

            for (i in 0 until slimeNameArray.size){
                RealmHelper.instance.insertSlimeSampleData(slimeNameArray[i])
            }
        }
    }

    private fun setupButton() {
        searchButton.clicks()
                .subscribe {
                    val intent = SearchActivity.newIntent(this@MainActivity)
                    val searchNameHolder = findViewById<TextView>(R.id.txt_search)
                    val searchImageHolder = findViewById<ImageView>(R.id.img_menuSearch)
                    val holderPair = android.support.v4.util.Pair.create(searchNameHolder as View, "searchHolder")
                    val imageHolderPair = android.support.v4.util.Pair.create(searchImageHolder as View, "searchImageHolder")
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity, holderPair, imageHolderPair)
                    ActivityCompat.startActivity(this@MainActivity, intent, options.toBundle())
                }
                .apply { compositeDisposable.add(this) }

        todayButton.clicks()
                .subscribe {
                    val intent = TodayActivity.newIntent(this@MainActivity)
                    val todayNameHolder = findViewById<TextView>(R.id.txt_today)
                    val holderPair = android.support.v4.util.Pair.create(todayNameHolder as View, "todayHolder")
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity, holderPair)
                    ActivityCompat.startActivity(this@MainActivity, intent, options.toBundle())
                }
                .apply { compositeDisposable.add(this) }

        favoriteButton.clicks()
                .subscribe {
                    val intent = FavoriteActivity.newIntent(this@MainActivity)
                    val favoriteNameHolder = findViewById<TextView>(R.id.txt_favorite)
                    val holderPair = android.support.v4.util.Pair.create(favoriteNameHolder as View, "favoriteHolder")
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity, holderPair)
                    ActivityCompat.startActivity(this@MainActivity, intent, options.toBundle())
                }
                .apply { compositeDisposable.add(this) }

    }


    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}
