package hanmo.com.slime

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import hanmo.com.slime.favorite.FavoriteActivity
import hanmo.com.slime.search.SearchActivity
import hanmo.com.slime.today.TodayActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        searchButton.setOnClickListener { startActivity(Intent(this, SearchActivity::class.java)) }
        todayButton.setOnClickListener { startActivity(Intent(this, TodayActivity::class.java)) }
        favoriteButton.setOnClickListener { startActivity(Intent(this, FavoriteActivity::class.java)) }
    }
}
