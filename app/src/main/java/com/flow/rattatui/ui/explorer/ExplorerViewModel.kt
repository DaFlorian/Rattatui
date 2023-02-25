package com.flow.rattatui.ui.explorer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExplorerViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the explorer fragment"
    }
    val text: LiveData<String> = _text
}