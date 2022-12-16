package com.example.simple_kotlin_app_firestore

import ItemClickType
import PersonRecyclerAdapter
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), PersonRecyclerAdapter.ContentListener {
    private lateinit var recyclerAdapter: PersonRecyclerAdapter
    val db = Firebase.firestore
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recylerList = findViewById<RecyclerView>(R.id.personList)
        db.collection("persons").get().addOnSuccessListener {
            val list: ArrayList<Person> = ArrayList()
            for(data in it.documents){
                val person = data.toObject(Person::class.java)
                if(person != null) {
                    person.id = data.id
                    list.add(person)
                }
            }
            recyclerAdapter = PersonRecyclerAdapter(list, this@MainActivity)
            recylerList.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = recyclerAdapter
            }
        }

            .addOnFailureListener{
                Log.e("MainActivity", it.message.toString())
            }

        val addButton = findViewById<Button>(R.id.button)
        val addImageURL = findViewById<EditText>(R.id.ed1)
        val addName = findViewById<EditText>(R.id.ed1)
        val addDesc = findViewById<EditText>(R.id.ed3)

        addButton.setOnClickListener {
            val person = Person("", addImageURL.text.toString(), addName.text.toString(), addDesc.text.toString())
            db.collection("persons").add(person).addOnSuccessListener {
                person.id=it.id
                recyclerAdapter.addItem(person)
            }
        }
    }

    override fun onItemButtonClick(index: Int, person: Person, clickType: ItemClickType) {
        if(clickType == ItemClickType.REMOVE){
            recyclerAdapter.removeItem(index)
            db.collection("persons").document(person.id).delete()
        }
        else if(clickType == ItemClickType.EDIT){
            db.collection("persons").document(person.id).set(person)
        }
    }
}