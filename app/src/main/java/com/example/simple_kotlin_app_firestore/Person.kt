package com.example.simple_kotlin_app_firestore

data class Person(
    var id: String="",
    var imageUrl: String? = null,
    var name: String? = null,
    var description: String? = null
)