package com.example.group_project.ui.Calendar

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.group_project.R
import com.example.group_project.databinding.FragmentCalendarBinding


class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var mroot: LinearLayout

    //星期几
    private var day: RelativeLayout? = null

    //SQLite Helper类
    var databaseHelper: DatabaseHelper? = DatabaseHelper(this@CalendarFragment.context, "database.db", null, 1)

    //被点击的View
    var ClickedView: View? = null
    var currentCoursesNumber = 0
    var maxCoursesNumber = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val calendarViewModel =
            ViewModelProvider(this).get(CalendarViewModel::class.java)

        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mroot=binding.leftViewLayout

        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);

        //从数据库读取数据
//        loadData()

    }

    private fun loadData() {
        val coursesList = ArrayList<Course>() //课程列表
        val sqLiteDatabase = databaseHelper!!.writableDatabase
        val cursor = sqLiteDatabase.rawQuery("select * from courses", null)
        if (cursor.moveToFirst()) {
            do {
                coursesList.add(
                    Course(
                        cursor.getString(cursor.getColumnIndex("course_name")),
                        cursor.getString(cursor.getColumnIndex("teacher")),
                        cursor.getString(cursor.getColumnIndex("class_room")),
                        cursor.getInt(cursor.getColumnIndex("day")),
                        cursor.getInt(cursor.getColumnIndex("class_start")),
                        cursor.getInt(cursor.getColumnIndex("class_end"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()

        //使用从数据库读取出来的课程信息来加载课程表视图
        for (course in coursesList) {
            createLeftView(course)
            createItemCourseView(course)
        }
    }

    //保存数据到数据库
    fun saveData(course: Course) {
        val sqLiteDatabase = databaseHelper!!.writableDatabase
        sqLiteDatabase.execSQL(
            "insert into courses(course_name, teacher, class_room, day, class_start, class_end) " + "values(?, ?, ?, ?, ?, ?)",
            arrayOf<String>(
                course.courseName,
                course.teacher,
                course.classRoom,
                course.day.toString(),
                course.start.toString(),
                course.end.toString()
            )
        )
    }

    //创建"第几节数"视图
    @SuppressLint("MissingInflatedId")
    private fun createLeftView(course: Course) {
        val endNumber: Int = course.end
        if (endNumber > maxCoursesNumber) {
            for (i in 0 until endNumber - maxCoursesNumber) {
                val view: View = LayoutInflater.from(this@CalendarFragment.context).inflate(com.example.group_project.R.layout.left_view, null)
                val params = LinearLayout.LayoutParams(110, 180)
                view.layoutParams = params
                val text = view.findViewById<TextView>(com.example.group_project.R.id.class_number_text)
                text.text = (++currentCoursesNumber).toString()
//                val view1: View = LayoutInflater.from(this@CalendarFragment.context).inflate(com.example.group_project.R.layout.fragment_calendar, null)
                val leftViewLayout: LinearLayout = mroot
                leftViewLayout.addView(view)
            }
            maxCoursesNumber = endNumber
        }
    }



    //获得控件里面的星期几控件
    private fun getViewDay(day: Int): RelativeLayout? {
        var dayId = 0
        when (day) {
            1 -> dayId = com.example.group_project.R.id.monday
            2 -> dayId = com.example.group_project.R.id.tuesday
            3 -> dayId = com.example.group_project.R.id.wednesday
            4 -> dayId = com.example.group_project.R.id.thursday
            5 -> dayId = com.example.group_project.R.id.friday
            6 -> dayId = com.example.group_project.R.id.saturday
            7 -> dayId = com.example.group_project.R.id.weekday
        }
        return view?.findViewById<RelativeLayout>(dayId)
    }

    //创建单个课程视图
    private fun createItemCourseView(course: Course) {
        val getDay: Int = course.day
        if (getDay < 1 || getDay > 7 || course.start > course.end) Toast.makeText(
            this@CalendarFragment.context,
            R.string.time_warning,
            Toast.LENGTH_LONG
        ).show() else {
            day = getViewDay(getDay)
            val height = 180
            val v = LayoutInflater.from(this@CalendarFragment.context).inflate(R.layout.course_card, null) //加载单个课程布局
            v.y = (height * (course.start - 1)).toFloat() //设置开始高度,即第几节课开始
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (course.end - course.start + 1) * height - 8
            ) //设置布局高度,即跨多少节课
            v.layoutParams = params
            val text = v.findViewById<TextView>(R.id.text_view)
            text.setText((course.courseName + "\n" + course.teacher).toString() + "\n" + course.classRoom) //显示课程名
            day!!.addView(v)

            //查看课程
            v.setOnClickListener { view ->
                ClickedView = view
                val intent = Intent(this@CalendarFragment.context, SeeCourseActivity::class.java)
                intent.putExtra("seeCourse", course)
                startActivityForResult(intent, 1)
            }
        }
    }

override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.toolbar, menu)
    super.onCreateOptionsMenu(menu!!, inflater)
}

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.add_courses -> {
                val intent = Intent(this@CalendarFragment.context, AddCourseActivity::class.java)
                startActivityForResult(intent, 0)
                return true
            }
            R.id.menu_about -> {

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            val course = data.getSerializableExtra("course") as Course?
            //创建课程表左边视图(节数)
            createLeftView(course!!)
            //创建课程表视图
            createItemCourseView(course)
//            存储数据到数据库
//            saveData(course)
        }
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            val PreCourse = data.getSerializableExtra("PreCourse") as Course?
            val isDelete = data.getBooleanExtra("isDelete", true)
            if (isDelete) {
                ClickedView!!.visibility = View.GONE //先隐藏
                day = PreCourse?.let { getViewDay(it.day) }
                day!!.removeView(ClickedView) //再移除课程视图

//                val sqLiteDatabase = databaseHelper!!.writableDatabase
//                sqLiteDatabase.execSQL(
//                    "delete from courses where course_name = ? and day =? and class_start=? and class_end=?",
//                    arrayOf<String>(
//                        PreCourse.courseName,
//                        java.lang.String.valueOf(PreCourse.day),
//                        java.lang.String.valueOf(PreCourse.start),
//                        java.lang.String.valueOf(PreCourse.end)
//                    )
//                )
//                Toast.makeText(this@CalendarFragment.context, R.string.delsucc, Toast.LENGTH_SHORT).show()

            } else {
                val intent = Intent(this@CalendarFragment.context, AddCourseActivity::class.java)
                intent.putExtra("ReviseCourse", PreCourse)
                intent.putExtra("isRevise", true)
                startActivityForResult(intent, 2)
            }
        }

        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            val PreCourse = data.getSerializableExtra("PreCourse") as Course?
            val newCourse = data.getSerializableExtra("newCourse") as Course?
            ClickedView!!.visibility = View.GONE //先隐藏
            day = PreCourse?.let { getViewDay(it.day) }
            day!!.removeView(ClickedView) //再移除课程视图

            //创建课程表左边视图(节数)
            createLeftView(newCourse!!)
            //创建课程表视图
            createItemCourseView(newCourse!!)
//            val sqLiteDatabase = databaseHelper!!.writableDatabase
//            sqLiteDatabase.execSQL(
//                "update courses set " +
//                        "course_name = ?,teacher = ?,class_room=? ,day=? ,class_start=? ,class_end =?" +
//                        "where course_name = ? and day =? and class_start=? and class_end=?",
//                arrayOf<String>(
//                    newCourse.courseName,
//                    newCourse.teacher,
//                    newCourse.classRoom,
//                    java.lang.String.valueOf(newCourse.day),
//                    java.lang.String.valueOf(newCourse.start),
//                    java.lang.String.valueOf(newCourse.end),
//                    PreCourse.courseName,
//                    java.lang.String.valueOf(PreCourse.day),
//                    java.lang.String.valueOf(PreCourse.start),
//                    java.lang.String.valueOf(PreCourse.end)
//                )
//            )
        }

    }


}