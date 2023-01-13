package ai.nextstop.opennews.data.local

import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmResults
import io.realm.kotlin.deleteFromRealm

open class Dao<T: RealmModel>(private val clazz: Class<T>) {

    fun copyOrUpdate(list: List<T>) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            it.copyToRealmOrUpdate(list)
        }
    }

    fun delete(t: T) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction { t.deleteFromRealm() }
    }

    fun deleteAll() {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            it.where(clazz).findAll().deleteAllFromRealm()
        }
    }

    fun findAllAsync(): RealmResults<T> {
        val realm = Realm.getDefaultInstance()
        return realm.where(clazz).findAllAsync()
    }

}