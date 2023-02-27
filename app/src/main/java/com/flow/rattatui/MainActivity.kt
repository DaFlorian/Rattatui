package com.flow.rattatui


import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.flow.rattatui.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.opencsv.CSVReader
import com.opencsv.CSVWriter
import java.io.*
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab?.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val navHostFragment =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment?)!!
        val navController = navHostFragment.navController

        binding.navView?.let {
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.nav_transform, R.id.nav_reflow, R.id.nav_slideshow, R.id.nav_settings
                ),
                binding.drawerLayout
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            it.setupWithNavController(navController)
        }

        binding.appBarMain.contentMain.bottomNavView?.let {
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.nav_transform, R.id.nav_reflow, R.id.nav_slideshow
                )
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            it.setupWithNavController(navController)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val result = super.onCreateOptionsMenu(menu)
        // Using findViewById because NavigationView exists in different layout files
        // between w600dp and w1240dp
        val navView: NavigationView? = findViewById(R.id.nav_view)
        if (navView == null) {
            // The navigation drawer already has the items including the items in the overflow menu
            // We only inflate the overflow menu if the navigation drawer isn't visible
            menuInflater.inflate(R.menu.overflow, menu)
        }
        return result
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_settings -> {
                val navController = findNavController(R.id.nav_host_fragment_content_main)
                navController.navigate(R.id.nav_settings)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    fun createFile(buffer: ByteBuffer): File {
        // Function to create a file system object from the buffer of a captured image
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US)
        val simpleDateString = simpleDateFormat.format(Date())
        val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + "/rattatui_$simpleDateString.jpeg")
        Log.d("Info", "Saving file... Path: $file")

        try {
            if (file.exists()) {file.delete()}
            val bytes = ByteArray(buffer.capacity())
            buffer.get(bytes)
            var output: OutputStream? = null
            try {
                if (!file.exists()) {
                    output = FileOutputStream(file)
                    output.write(bytes)
                }
            } finally {
                output?.close()
                Log.d("Info", "File saved successfully...")
            }
        } catch (e: IOException) {
            // Log the exception
            Log.d("Info", "Error saving file... $e")
        }

        // return File(filesDir, "VID_${simpleDateFormat.format(Date())}.mp4")
        return file
    }


    fun getFileList(): Array<out File>? {
        // Function to get the list of files (CSV-Files of menus, receipts etc.)
        // val filesPath = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString()
        val filesPath = Environment.getExternalStorageDirectory().absolutePath.toString()  // +"/rattatui";
        Log.d("Info", "Searching for files in path $filesPath")

        val directory: File = File(filesPath)
        val fileItems = directory.listFiles()

        // val fileItems = File("$filesPath/*.rattatui.csv")
        if (fileItems != null) {
            Log.d("Info", "Found files: " + fileItems.size)
        }

        if (fileItems != null) {
            for (i in fileItems.indices) {
                Log.d("Info", "FileName:" + fileItems[i].name)
            }
        }

        return fileItems
    }


    fun getMenuObjectData(csvContent: MutableList<Array<String>>?): Array<String>? {
        // Function to return a Menu-Object from the content of a CSV-File
        // var csvData: MutableList<Array<String>>

        // Loop through the CSV-Content (one header row and one data row)
        if (csvContent != null) {
            //var ratMenu: MutableList<Array<String>>? = null
            var firstRow = true
            for (row in csvContent) {
                // Loop through the cells if not the header row
                if (!firstRow){
                    // This is a data row
                    for ((i, cell) in row.withIndex()) {
                        // Log cell content for debugging purposes
                        Log.d("Info", "RatMenu-cell: $i: $cell")
                    }
                    return row      // val row: Array<String>
                } else {
                    // This is the header row
                    firstRow = false
                }
            }
            return null
        } else {
            return null
        }
    }


    fun getCSVFileContent(filePath: String): MutableList<Array<String>>? {
        // Function to get the content of a file (e.g. CSV-File with menu or receipt)
        // val filesPath = Environment.getExternalStorageDirectory().absolutePath.toString()
        lateinit var csvReader: CSVReader
        var csvData: MutableList<Array<String>> = ArrayList()

        return try {
            csvReader = CSVReader(FileReader(filePath))
            csvData = csvReader.readAll()      // Returns: A List of String[], with each String[] representing a line of the file.
            Log.d("Info", "CSV-File-Content: $csvData")

            csvData
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("Error", "getCSVFileContent: $e")

            null
        }
    }


    fun writeMenuCsvFile(fileName: String, menuName: String, menuDescription: String, menuType: String, menuTime: String, menuCosts: String, menuComment: String): Boolean {
        // Function to write CSV-File to storage
        val filesPath = Environment.getExternalStorageDirectory().toString()
        var writer: CSVWriter? = null

        try {
            writer = CSVWriter(FileWriter("$filesPath/$fileName"))
            val data: MutableList<Array<String>> = ArrayList()
            data.add(arrayOf("menuName", menuName))
            data.add(arrayOf("menuDescription", menuDescription))
            data.add(arrayOf("menuType", menuType))
            data.add(arrayOf("menuTime", menuTime))
            data.add(arrayOf("menuCosts", menuCosts))
            data.add(arrayOf("menuComment", menuComment))
            writer.writeAll(data)                                   // Write data to CSV-File
            writer.close()

            return true
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("Error", "writeMenuCsvFile: $e")

            return false
        }

    }


}


