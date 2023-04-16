package com.example.group_project.ui.Calendar

import java.io.Serializable


class Course(
    var courseName: String,
    var teacher: String,
    var classRoom: String,
    var day: Int,
    val start: Int,
    var end: Int
) :
    Serializable
