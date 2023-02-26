package com.flow.rattatui.ui.menus


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File
import java.nio.ByteBuffer


class MenusViewModel : ViewModel() {

    private fun loadMenus() {
        // Function to load the menu CSV-Files and create the live data


    }



    private val _texts = MutableLiveData<List<String>>().apply {
        value = (1..5).mapIndexed { _, i ->
            "This is menu item # $i"
        }
    }

    private val _name = MutableLiveData<List<String>>().apply {
        value = (1..5).mapIndexed { _, i ->
            "This is menu # $i"
        }
    }

    val texts: LiveData<List<String>> = _texts
    val name: LiveData<List<String>> = _name


}