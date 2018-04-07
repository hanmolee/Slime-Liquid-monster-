package hanmo.com.slime.db

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by hanmo on 2018. 4. 7..
 */
open class SearchHistory : RealmObject() {

    @PrimaryKey
    open var id : Int = 0
    open var historyName : String? = null
    open var searchTime : Long? = null
}