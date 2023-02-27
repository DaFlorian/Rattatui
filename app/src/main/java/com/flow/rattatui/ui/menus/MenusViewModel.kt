package com.flow.rattatui.ui.menus


// import androidx.lifecycle.LiveData
// import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flow.rattatui.MainActivity
import java.io.File


class MenusViewModel : ViewModel() {

    private fun loadMenuNames(): Array<String?> {
        // Function to load the menu CSV-Files and create the live data
        val mainActivity = MainActivity()

        val fileList: Array<out File>? = mainActivity.getFileList()
        // var menuNames = arrayOfNulls<String>(0)
        var menuNames = arrayOfNulls<String>(0)  // Array<String>?      // val ratMenuObjectData: Array<String>?

        if (fileList != null) {
            for (element in fileList) {
                // Load the current element in the loop >> var csvData: MutableList<Array<String>>
                val csvContent = mainActivity.getCSVFileContent(element.absolutePath)
                val ratMenuObjectData = mainActivity.getMenuObjectData(csvContent)  // Array<String>
                if (ratMenuObjectData != null) {
                    menuNames += ratMenuObjectData[0]
                }
            }
        }

        return menuNames
    }


    private val _names = loadMenuNames()


    // val texts: LiveData<List<String>> = _texts
    val name: Array<String?> = _names

}