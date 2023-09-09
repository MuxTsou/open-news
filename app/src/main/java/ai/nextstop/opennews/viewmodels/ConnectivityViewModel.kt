package ai.nextstop.opennews.viewmodels

import ai.nextstop.opennews.data.local.connectivity.ConnectionState
import ai.nextstop.opennews.data.local.connectivity.ConnectivityLiveData
import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class ConnectivityViewModel @Inject constructor(@ApplicationContext private val context: Context,) : ViewModel() {

    fun getConnectivityLiveData() : LiveData<ConnectionState>{
        return Transformations.distinctUntilChanged(ConnectivityLiveData(context))
    }
}