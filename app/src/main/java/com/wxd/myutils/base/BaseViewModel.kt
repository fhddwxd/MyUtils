package com.wxd.myutils.base

import android.app.Activity
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import retrofit2.HttpException

/**
 *  Create by @author wxd
 *  @time 2019/9/25  上午 9:23
 *  @describe
 */
open class BaseViewModel : ViewModel(), LifecycleObserver {

    val mException: MutableLiveData<Throwable> = MutableLiveData()


    private fun launchOnUI(block: suspend CoroutineScope.() -> Unit) {

        viewModelScope.launch { block() }

    }

    suspend fun <T> launchIO(block: suspend CoroutineScope.() -> T) {
        withContext(Dispatchers.IO) {
            block
        }
    }

    fun launch(tryBlock: suspend CoroutineScope.() -> Unit) {
        launchOnUI {
            tryCatch(tryBlock, {}, {}, true)
        }
    }


    fun launchOnUITryCatch(tryBlock: suspend CoroutineScope.() -> Unit,
                           catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
                           finallyBlock: suspend CoroutineScope.() -> Unit,
                           handleCancellationExceptionManually: Boolean
    ) {
        launchOnUI {
            tryCatch(tryBlock, catchBlock, finallyBlock, handleCancellationExceptionManually)
        }
    }

    fun launchOnUITryCatch(tryBlock: suspend CoroutineScope.() -> Unit,
                           handleCancellationExceptionManually: Boolean = false
    ) {
        launchOnUI {
            tryCatch(tryBlock, {}, {}, handleCancellationExceptionManually)
        }
    }
    suspend fun executeResponse(response: BaseResponse<Any>, successBlock: suspend CoroutineScope.() -> Unit,
                                errorBlock: suspend CoroutineScope.() -> Unit) {
        coroutineScope {
            if (!response.success) errorBlock()
            else successBlock()
        }
    }

    fun Activity.onNetError(e: Throwable, func: (e: Throwable) -> Unit) {
        if (e is HttpException) {
            func(e)
        }
    }


    private suspend fun tryCatch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit,
        handleCancellationExceptionManually: Boolean = false) {
        coroutineScope {
            try {
                tryBlock()
            } catch (e: Throwable) {
                if (e !is CancellationException || handleCancellationExceptionManually) {
                    mException.value = e
                    catchBlock(e)
                } else {
                    throw e
                }
            } finally {
                finallyBlock()
            }
        }
    }

}