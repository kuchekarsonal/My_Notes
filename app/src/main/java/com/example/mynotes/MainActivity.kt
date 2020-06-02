package com.example.mynotes

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.row.*
import kotlinx.android.synthetic.main.row.view.*
import kotlinx.android.synthetic.main.row.view.shareBtn

class MainActivity : AppCompatActivity() {

  var  listNotes = ArrayList<Note>()
 lateinit var add :Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        add = findViewById<Button>(R.id.btnAddNew)
        LoadQuery("%")

        add.setOnClickListener {
           var intent = Intent(this, AddTask::class.java)
            startActivity(intent)


        }


    }
    override fun onResume() {
        super.onResume()
        LoadQuery("%")
    }

    private fun LoadQuery(title: String) {
        var dbManager = DbManager(this)
        val projections = arrayOf("ID", "Title","Description")
        val selectionArgs = arrayOf(title)
        val cursor = dbManager.Query(projections, "Title like ?", selectionArgs, "Title")
        listNotes.clear()
        if(cursor.moveToFirst()){
            do{
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Description = cursor.getString(cursor.getColumnIndex("Description"))

                listNotes.add(Note(ID, Title, Description))

            }while (cursor.moveToNext())
        }

        var myNotesAdapter = MyNotesAdapter(this, listNotes)
        notesLv.adapter = myNotesAdapter
    }

  inner class MyNotesAdapter : BaseAdapter{
      var listNotesAdapter = ArrayList<Note>()
      var context : Context?= null

      constructor(context: Context, listNotesAdapter: ArrayList<Note>) : super() {
          this.listNotesAdapter = listNotesAdapter
          this.context = context
      }


      override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var myView = layoutInflater.inflate(R.layout.row, null)
          var myNote = listNotesAdapter[position]
          myView.titledoes.text = myNote.noteName
          myView.descdoes.text = myNote.noteDescription
          //delete listener

          myView.deleteBtn.setOnClickListener {
              var dbManager = DbManager(this.context!!)
              val selectionArgs = arrayOf(myNote.noteId.toString())
              dbManager.delete("ID=?", selectionArgs)
              LoadQuery("%")
          }
          myView.editBtn.setOnClickListener {
              GoToUpdateFun(myNote)
          }
          myView.shareBtn.setOnClickListener {
              val title = myView.titledoes.text.toString()
              val desc = myView.descdoes.text.toString()

              val s = title+ "\n"+ desc

              val shareIntent = Intent()
              shareIntent.action = Intent.ACTION_SEND
              shareIntent.type= "text/plain"
              shareIntent.putExtra(Intent.EXTRA_TEXT, s)
              startActivity(Intent.createChooser(shareIntent, s))

          }

          return myView
      }

      override fun getItem(position: Int): Any {

          return listNotesAdapter[position]
      }

      override fun getItemId(position: Int): Long {
          return position.toLong()
      }

      override fun getCount(): Int {
          return listNotesAdapter.size
      }

  }

    private fun GoToUpdateFun(myNote: Note) {
        var intent = Intent(this, AddTask::class.java)
        intent.putExtra("ID",myNote.noteId)
        intent.putExtra("name", myNote.noteName)
        intent.putExtra("des",myNote.noteDescription)
        startActivity(intent)

    }

}
