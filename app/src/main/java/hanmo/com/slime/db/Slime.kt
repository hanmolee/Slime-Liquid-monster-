package hanmo.com.slime.db

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by hanmo on 2018. 4. 8..
 */
open class Slime : RealmObject() {

    @PrimaryKey
    open var id : Int = 0

    open var slimeName : String? = null

}