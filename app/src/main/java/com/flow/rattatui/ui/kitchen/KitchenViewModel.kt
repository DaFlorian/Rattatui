package com.flow.rattatui.ui.kitchen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class KitchenViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the kitchen fragment"
    }
    val text: LiveData<String> = _text
}