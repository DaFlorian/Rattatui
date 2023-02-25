package com.flow.rattatui.ui.menus

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MenusViewModel : ViewModel() {

    private val _texts = MutableLiveData<List<String>>().apply {
        value = (1..5).mapIndexed { _, i ->
            "This is menu item # $i"
        }
    }

    val texts: LiveData<List<String>> = _texts
}