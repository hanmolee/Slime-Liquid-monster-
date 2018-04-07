package hanmo.com.slime

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.view.View
import android.widget.TextView
import com.jakewharton.rxbinding2.view.clicks
import hanmo.com.slime.favorite.FavoriteActivity
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

        setupButton()

    }

    private fun setupButton() {
        searchButton.clicks()
                .subscribe {
                    val intent = SearchActivity.newIntent(this@MainActivity)
                    val searchNameHolder = findViewById<TextView>(R.id.txt_search)
                    val holderPair = android.support.v4.util.Pair.create(searchNameHolder as View, "searchHolder")
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity, holderPair)
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
