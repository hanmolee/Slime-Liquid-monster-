package hanmo.com.slime.favorite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import hanmo.com.slime.R

/**
 * Created by hanmo on 2018. 4. 7..
 */
class FavoriteActivity : AppCompatActivity() {

    companion object {

        fun newIntent(context: Context) : Intent {
            val intent = Intent(context, FavoriteActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
    }
}