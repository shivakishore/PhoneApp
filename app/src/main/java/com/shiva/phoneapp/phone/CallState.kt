package com.shiva.phoneapp.phone

import androidx.lifecycle.MutableLiveData

/**
 * Created by Shiva Kishore on 3/1/2022.
 */
object CallState {

    var state: Int?
        get() = currentState.value
        set(value) {
            currentState.postValue(value)
        }

    val currentState = MutableLiveData<Int>()
}