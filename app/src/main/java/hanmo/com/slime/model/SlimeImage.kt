package hanmo.com.slime.model

import android.content.Context

/**
 * Created by hanmo on 2018. 4. 8..
 */
class SlimeImage(private val imageName: String) {
    fun getImageResourceId(context: Context): Int {
        return context.resources.getIdentifier(this.imageName, "drawable", context.packageName)
    }
}