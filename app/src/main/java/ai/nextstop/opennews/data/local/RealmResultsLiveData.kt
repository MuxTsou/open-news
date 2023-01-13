package ai.nextstop.opennews.data.local

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.realm.OrderedRealmCollectionChangeListener
import io.realm.RealmModel
import io.realm.RealmResults


class RealmResultsLiveData<T : RealmModel> @MainThread constructor(private val results: RealmResults<T>) :
    MutableLiveData<List<T>>() {

    init {
        if (!results.isValid) {
            throw IllegalArgumentException("The provided RealmResults is no longer valid, the Realm instance it belongs to is closed. It can no longer be observed for changes.");
        }
        if(results.isLoaded) {
            value = results
        }
    }

    private val listener = OrderedRealmCollectionChangeListener<RealmResults<T>> { updates, changeSet ->
        value = updates
    }

    override fun onActive() {
        super.onActive()
        if(results.isValid)
            results.addChangeListener(listener)
    }

    override fun onInactive() {
        super.onInactive()
        if(results.isValid)
            results.removeChangeListener(listener)
    }
}

fun <T : RealmModel> RealmResults<T>.asLiveData(): LiveData<List<T>> {
    return RealmResultsLiveData(this)
}