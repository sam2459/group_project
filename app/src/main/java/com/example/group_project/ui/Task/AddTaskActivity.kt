package com.example.group_project.ui.Task

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.group_project.MainActivity
import com.example.group_project.R
import com.example.group_project.ui.Task.TaskFragment.Companion.arrayAdapter
import com.example.group_project.ui.Task.TaskFragment.Companion.dset
import com.example.group_project.ui.Task.TaskFragment.Companion.hset
import com.example.group_project.ui.Task.TaskFragment.Companion.miset
import com.example.group_project.ui.Task.TaskFragment.Companion.mset
import com.example.group_project.ui.Task.TaskFragment.Companion.outset
import com.example.group_project.ui.Task.TaskFragment.Companion.taskday
import com.example.group_project.ui.Task.TaskFragment.Companion.taskhour
import com.example.group_project.ui.Task.TaskFragment.Companion.taskminute
import com.example.group_project.ui.Task.TaskFragment.Companion.taskmonth
import com.example.group_project.ui.Task.TaskFragment.Companion.taskout
import com.example.group_project.ui.Task.TaskFragment.Companion.tasks
import com.example.group_project.ui.Task.TaskFragment.Companion.taskyear
import com.example.group_project.ui.Task.TaskFragment.Companion.yset
import java.util.*

class AddTaskActivity : AppCompatActivity(), TextWatcher {
    var noteId = 0
    var y: String?=null
    var m: String?=null
    var d: String?=null
    var h: String?=null
    var mi: String? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        val tasktime = findViewById<View>(R.id.tasktime) as TextView
        val taskdate = findViewById<View>(R.id.taskdate) as TextView
        val editText = findViewById<View>(R.id.editText) as EditText
        val i = intent
        noteId = i.getIntExtra("taskId", -1)
        if (noteId != -1) {
            editText.setText(TaskFragment.tasks[noteId])
            y=taskyear[noteId]
            m=taskmonth[noteId]
            d=taskday[noteId]
            h=taskhour[noteId]
            mi=taskminute[noteId]
        }
        editText.addTextChangedListener(this)
        taskdate.text = y+"/"+m+"/"+d
        tasktime.text = h+" "+mi
    }

    fun buttonFunc(view: View) {
        val tasktime = findViewById<View>(R.id.tasktime) as TextView
        val taskdate = findViewById<View>(R.id.taskdate) as TextView
        val sharedPreferences =
            getSharedPreferences("com.example.group_project", Context.MODE_PRIVATE)
        when (view.id) {
            R.id.taskdate -> {
                // 日期选择器
                val ca = Calendar.getInstance()
                var mYear = y!!.toInt()
                var mMonth = m!!.toInt()
                var mDay =d!!.toInt()

                val datePickerDialog = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        mYear = year
                        mMonth = month
                        mDay = dayOfMonth
                        val mDate = "${year}/${month + 1}/${dayOfMonth}"
                        y=mYear.toString()
                        m=(mMonth+1).toString()
                        d=mDay.toString()
                        taskdate.text = mDate
                    },
                    mYear, mMonth, mDay
                )
                datePickerDialog.show()
            }
            R.id.tasktime -> {
                // 时间选择器
                val ca = Calendar.getInstance()
                var mHour = h!!.toInt()
                var mMinute = mi!!.toInt()

                val timePickerDialog = TimePickerDialog(
                    this,
                    TimePickerDialog.OnTimeSetListener{ _, hourOfDay, minute ->
                        mHour   = hourOfDay
                        mMinute = minute
                        val mTime = "${hourOfDay}:${minute}"
                        h=hourOfDay.toString()
                        mi=minute.toString()
                        tasktime.text = mTime
                    },
                    mHour, mMinute, true
                )
                timePickerDialog.show()
            }
        }
    }
    override fun beforeTextChanged(
        s: CharSequence,
        start: Int,
        count: Int,
        after: Int
    ) {
    }

    override fun onTextChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {
        TaskFragment.tasks[noteId] = s.toString()
        TaskFragment.arrayAdapter!!.notifyDataSetChanged()
        val sharedPreferences =
            getSharedPreferences("com.example.group_project", Context.MODE_PRIVATE)
        if (TaskFragment.set == null) {
            TaskFragment.set = HashSet()
        } else {
            TaskFragment.set!!.clear()
        }
        TaskFragment.set!!.addAll(TaskFragment.tasks)
        sharedPreferences.edit().remove("tasks").apply()
        sharedPreferences.edit().putStringSet("tasks", TaskFragment.set).apply()
    }
    override fun afterTextChanged(s: Editable) {}
    fun addTask(view: View?) {
        taskyear[noteId] =y
        taskmonth[noteId] =m
        taskday[noteId] =d
        taskhour[noteId] =h
        taskminute[noteId] =mi

        taskout[noteId] = tasks[noteId]+"\n" +taskyear[noteId]+"/"+ taskmonth[noteId]+"/"+ taskday[noteId]+" "+ taskhour[noteId]+":"+ taskminute[noteId]
        arrayAdapter!!.notifyDataSetChanged()
        val sharedPreferences =
            getSharedPreferences("com.example.group_project", Context.MODE_PRIVATE)
        if (yset == null) {
            yset = HashSet()

        } else {
            yset!!.clear()
        }
        if (mset == null) {
            mset = HashSet()

        } else {
            mset!!.clear()
        }
        if (dset == null) {
            dset = HashSet()

        } else {
            dset!!.clear()
        }
        if (hset == null) {
            hset = HashSet()

        } else {
            hset!!.clear()
        }
        if (miset == null) {
            miset = HashSet()

        } else {
            miset!!.clear()
        }

        if (outset == null) {
            outset = HashSet()

        } else {
            outset!!.clear()
        }
        yset!!.addAll(taskyear)
        mset!!.addAll(taskmonth)
        dset!!.addAll(taskday)
        hset!!.addAll(taskhour)
        miset!!.addAll(taskminute)
        outset!!.addAll(taskout)

        sharedPreferences.edit().remove("taskyear").apply()
        sharedPreferences.edit().putStringSet("taskyear", yset).apply()
        sharedPreferences.edit().remove("taskmonth").apply()
        sharedPreferences.edit().putStringSet("taskmonth", mset).apply()
        sharedPreferences.edit().remove("taskday").apply()
        sharedPreferences.edit().putStringSet("taskday", dset).apply()
        sharedPreferences.edit().remove("taskhour").apply()
        sharedPreferences.edit().putStringSet("taskhour", hset).apply()
        sharedPreferences.edit().remove("taskminute").apply()
        sharedPreferences.edit().putStringSet("taskminute", miset).apply()
        sharedPreferences.edit().remove("taskout").apply()
        sharedPreferences.edit().putStringSet("taskout", outset).apply()



        val i = Intent(applicationContext, MainActivity::class.java)
        startActivity(i)
    }
}