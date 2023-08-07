package give.away.good.deeds.core.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

/**
 * Parent ViewModel for all the app ViewModel
 *
 * @author Hitesh
 * @version 1.0
 * @since July 2023
 */
abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    protected val mContext = application.applicationContext
    val errorMessageLiveData: MutableLiveData<String> = MutableLiveData<String>()
    val isLoadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
}