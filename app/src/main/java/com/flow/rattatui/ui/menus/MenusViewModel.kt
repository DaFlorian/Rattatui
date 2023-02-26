package com.flow.rattatui.ui.menus


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flow.rattatui.MainActivity
import java.io.File


class MenusViewModel : ViewModel() {

    private fun loadMenus() {
        // Function to load the menu CSV-Files and create the live data
        val mainActivity: MainActivity = MainActivity()

        val fileList: Array<out File>? = mainActivity.getFileList()
        var fileItem: File? = null

        if (fileList != null) {
            for (element in fileList) {
                // Load the current element in the loop
                var csvContent = mainActivity.getCSVFileContent(element.absolutePath)


            }
        }

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