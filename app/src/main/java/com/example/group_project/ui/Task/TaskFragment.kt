package com.example.group_project.ui.Task

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.group_project.databinding.FragmentTaskBinding

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.group_project.R
import java.util.*
import com.example.group_project.MainActivity
import com.example.group_project.databinding.FragmentChatBinding
import java.util.zip.Inflater

class TaskFragment : Fragment() {


    companion object {
        var tasks = ArrayList<String?>()
        var taskyear = ArrayList<String?>()
        var taskmonth = ArrayList<String?>()
        var taskday = ArrayList<String?>()
        var taskhour = ArrayList<String?>()
        var taskminute = ArrayList<String?>()
        var taskout = ArrayList<String?>()

        var arrayAdapter: ArrayAdapter<*>? = null
        var set: MutableSet<String?>? = null
        var yset: MutableSet<String?>? = null
        var mset: MutableSet<String?>? = null
        var dset: MutableSet<String?>? = null
        var hset: MutableSet<String?>? = null
        var miset: MutableSet<String?>? = null
        var outset: MutableSet<String?>? = null
        val ca = Calendar.getInstance()
    }
    private var _binding: FragmentTaskBinding? = null
    private val handler: Handler = Handler(Looper.getMainLooper())
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_addtask, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.add) {
            tasks.add("Fight For International!")
            taskyear.add(ca[Calendar.YEAR].toString())
            taskmonth.add(ca[Calendar.MONTH].toString())
            taskday.add(ca[Calendar.DAY_OF_MONTH].toString())
            taskhour.add(ca[Calendar.HOUR_OF_DAY].toString())
            taskminute.add(ca[Calendar.MINUTE].toString())
            taskout.add("Fight For International!")
            val sharedPreferences =
                this.getActivity()!!.getSharedPreferences("com.example.group_project", Context.MODE_PRIVATE)
            if (set == null) {
                set = HashSet()
            } else {
                set!!.clear()
            }
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
            set!!.addAll(tasks)
            yset!!.addAll(taskyear)
            mset!!.addAll(taskmonth)
            dset!!.addAll(taskday)
            hset!!.addAll(taskhour)
            miset!!.addAll(taskminute)
            outset!!.addAll(taskout)
            arrayAdapter!!.notifyDataSetChanged()
            sharedPreferences.edit().remove("tasks").apply()
            sharedPreferences.edit().putStringSet("tasks", set).apply()
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
            val i = Intent(getActivity(), AddTaskActivity::class.java)
            i.putExtra("taskId", tasks.size - 1)
            startActivity(i)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {




        val homeViewModel =
            ViewModelProvider(this).get(MoreViewModel::class.java)

        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val sharedPreferences =
            this.getActivity()!!.getSharedPreferences("com.example.group_project", Context.MODE_PRIVATE)
        set = sharedPreferences.getStringSet("tasks", null)
        yset = sharedPreferences.getStringSet("taskyear", null)
        mset = sharedPreferences.getStringSet("taskmonth", null)
        dset = sharedPreferences.getStringSet("taskday", null)
        hset = sharedPreferences.getStringSet("taskhour", null)
        miset = sharedPreferences.getStringSet("taskminute", null)
        outset = sharedPreferences.getStringSet("taskout", null)

        tasks.clear()
        if (set != null) {
            tasks.addAll(set!!)

        } else {
            tasks.add("Fight for international!")
            set = HashSet()
            (set as HashSet<String?>).addAll(tasks)
            sharedPreferences.edit().putStringSet("tasks", set).apply()
        }

        taskyear.clear()
        if (yset != null) {
            taskyear.addAll(yset!!)

        } else {
            taskyear.add("1949")
            yset = HashSet()
            (yset as HashSet<String?>).addAll(taskyear)
            sharedPreferences.edit().putStringSet("taskyear", yset).apply()
        }
        taskmonth.clear()
        if (mset != null) {
            taskmonth.addAll(mset!!)

        } else {
            taskmonth.add("10")
            mset = HashSet()
            (mset as HashSet<String?>).addAll(taskmonth)
            sharedPreferences.edit().putStringSet("taskmonth", mset).apply()
        }
        taskday.clear()
        if (dset != null) {
            taskday.addAll(dset!!)

        } else {
            taskday.add("1")
            dset = HashSet()
            (dset as HashSet<String?>).addAll(taskday)
            sharedPreferences.edit().putStringSet("taskday", dset).apply()
        }
        taskhour.clear()
        if (hset != null) {
            taskhour.addAll(hset!!)

        } else {
            taskhour.add("10")
            hset = HashSet()
            (hset as HashSet<String?>).addAll(taskhour)
            sharedPreferences.edit().putStringSet("taskhour", hset).apply()
        }
        taskminute.clear()
        if (miset != null) {
            taskminute.addAll(miset!!)

        } else {
            taskminute.add("10")
            miset = HashSet()
            (miset as HashSet<String?>).addAll(taskminute)
            sharedPreferences.edit().putStringSet("taskminute", miset).apply()
        }
        taskout.clear()
        if (outset != null) {
            taskout.addAll(outset!!)

        } else {
            taskout.add("Fight for international! \n 1949/10/1 10:10")
            outset = HashSet()
            (outset as HashSet<String?>).addAll(taskout)
            sharedPreferences.edit().putStringSet("taskout", outset).apply()
        }

        val listView =binding.listView

        arrayAdapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_list_item_1,
            taskout
        )
        listView.adapter = arrayAdapter
        listView.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            val i = Intent(this@TaskFragment.context, AddTaskActivity::class.java)
            i.putExtra("taskId", position)
            startActivity(i)
        }
        listView.onItemLongClickListener = OnItemLongClickListener { parent, view,
                                                                     position, id ->
            AlertDialog.Builder(this@TaskFragment.context!!)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are you sure?")
                .setMessage("Do you want to delete this task?")
                .setPositiveButton("Yes") { dialog, which ->
                    tasks.removeAt(position)
                    val sharedPreferences =
                        this.getActivity()!!.getSharedPreferences(
                            "com.example.todolist",
                            Context.MODE_PRIVATE
                        )
                    if (set == null) {
                        set = HashSet()
                    } else {
                        set!!.clear()
                    }
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
                    set!!.addAll(tasks)
                    yset!!.addAll(taskyear)
                    mset!!.addAll(taskmonth)
                    hset!!.addAll(taskhour)
                    dset!!.addAll(taskday)
                    miset!!.addAll(taskminute)
                    outset!!.addAll(taskout)
                    sharedPreferences.edit().remove("tasks").apply()
                    sharedPreferences.edit().remove("taskyear").apply()
                    sharedPreferences.edit().remove("taskmonth").apply()
                    sharedPreferences.edit().remove("taskday").apply()
                    sharedPreferences.edit().remove("taskhour").apply()
                    sharedPreferences.edit().remove("taskminute").apply()
                    sharedPreferences.edit().remove("taskout").apply()
                    sharedPreferences.edit()
                        .putStringSet("tasks", set).apply()
                    sharedPreferences.edit()
                        .putStringSet("taskyear", yset).apply()
                    sharedPreferences.edit()
                        .putStringSet("taskmonth", mset).apply()
                    sharedPreferences.edit()
                        .putStringSet("taskday", dset).apply()
                    sharedPreferences.edit()
                        .putStringSet("taskhour", hset).apply()
                    sharedPreferences.edit()
                        .putStringSet("taskminute", miset).apply()
                    sharedPreferences.edit()
                        .putStringSet("taskout", outset).apply()
                }
                .setNegativeButton("No", null)
                .show()
            true
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}