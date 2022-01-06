package com.example.massigym_android.model

data class Workout(
    var userMail: String?,
    var userName: String?,
    var category: String?,
    var name: String?,
    var description: String?,
    var duration: Int?,
    var imageUrl: String?,
    var videoUrl: String?,
    var searchKeyList: ArrayList<String>?,
    var favourites: ArrayList<String>?,
    var likes: ArrayList<String>?
) {
    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        null,
        "",
        "",
        null,
        null,
        null,
    )
}
