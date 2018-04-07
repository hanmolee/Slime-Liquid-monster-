package hanmo.com.slime.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import hanmo.com.slime.R

/**
 * Created by hanmo on 2018. 4. 7..
 */
class SearchActivity : AppCompatActivity() {

    companion object {

        fun newIntent(context: Context) : Intent {
            val intent = Intent(context, SearchActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
    }
}