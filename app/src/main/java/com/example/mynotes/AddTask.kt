package com.example.mynotes

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_task.*
import kotlinx.android.synthetic.main.row.*

class AddTask : AppCompatActivity() {

    var dbTable = "Notes"
    var id = 0
    lateinit var  addTask :Button
    lateinit var  cancel : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        //create button
         addTask = findViewById<Button>(R.id.btnSaveTask)

         cancel = findViewById<Button>(R.id.btnCancel)

        try {
            val bundle:Bundle = intent.extras!!
            id = bundle.getInt("ID", 0)
            if (id!=0){
                addTaskTitle.setText(bundle.getString("name"))
                addTaskDescription.setText(bundle.getString("des"))
            }
        }catch (ex:Exception){}

        addTask.setOnClickListener {

            addFunc()


        }

        cancel.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }




    }
    public fun addFunc() {
        var dbManager = DbManager(this)
        var values = ContentValues()
        values.put("Title", addTaskTitle.text.toString().trim())
        values.put("Description", addTaskDescription.text.toString().trim())

        if (id == 0) {
            val ID = dbManager.insert(values)
            if (ID > 0) {
                Toast.makeText(this, "Note is added", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error adding note...", Toast.LENGTH_SHORT).show()
            }
        } else {
            var selectionArgs = arrayOf(id.toString())
            val ID = dbManager.update(values, "ID=?", selectionArgs)
            if (ID > 0) {
                Toast.makeText(this, "Note is added", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error adding note...", Toast.LENGTH_SHORT).show()
            }
        }


    }

}
