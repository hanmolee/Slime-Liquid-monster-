package hanmo.com.slime.db

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by hanmo on 2018. 4. 10..
 */
class FavoriteSlime : RealmObject() {

    @PrimaryKey
    open var id : Int = 0

    open var name : String? = null

}