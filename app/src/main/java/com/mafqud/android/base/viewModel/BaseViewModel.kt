package com.mafqud.android.base.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch


open class BaseViewModel<I, S>(initialState: S) : ViewModel() {

    /**
     * Intent channel
     */
    val intentChannel = Channel<I>(Channel.CONFLATED)

    /**
     * State flow
     */

    protected val _stateChannel = MutableStateFlow(initialState)
    val stateChannel: StateFlow<S> = _stateChannel

    /**
     * To get current state
     */
    protected fun getCurrentState() = _stateChannel.value

    /**
     *
     */
    protected fun getIODispatcher() = Dispatchers.IO

    protected fun launchViewModelScope(yourCode: suspend () -> Unit) {
        viewModelScope.launch(getIODispatcher()) {
            yourCode()
        }
    }

    protected fun handleIntents(handle: (intent: I) -> Unit) {
        viewModelScope.launch(getIODispatcher()) {
            intentChannel.consumeAsFlow().collect {
                handle(it)
            }
        }
        //...
    }

    override fun onCleared() {
        super.onCleared()
        // Release memory
        intentChannel.close()
    }
}