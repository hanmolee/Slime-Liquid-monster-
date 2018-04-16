package hanmo.com.slime.db

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by hanmo on 2018. 4. 15..
 */
open class LockScreenTable : RealmObject() {

    @PrimaryKey
    open var id : Int = 0

    open var IsLock : Boolean? = null

    open var IsSofyKey : Boolean? = null
}