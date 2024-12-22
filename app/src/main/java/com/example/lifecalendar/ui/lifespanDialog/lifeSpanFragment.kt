package com.example.lifecalendar.ui.lifespanDialog

import android.app.Dialog
import android.content.ContentValues
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog

import androidx.fragment.app.DialogFragment
import com.example.lifecalendar.LifeCalendarProvider
import com.example.lifecalendar.R



/**
 * A simple [Fragment] subclass.
 * Use the [lifeSpanFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class lifeSpanFragment : DialogFragment() {

    private lateinit var spinner: Spinner

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("选择预期寿命")

        val view = layoutInflater.inflate(R.layout.fragment_life_span, null)
        spinner = view.findViewById(R.id.lifespan_spinner)
        builder.setView(view)

        val lifeSpans = arrayOf("60年", "70年", "80年", "90年")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, lifeSpans)
        spinner.adapter = adapter

        builder.setPositiveButton("确定") { _, _ ->
            val selectedLifeSpan = spinner.selectedItem.toString()
            val weeks = when (selectedLifeSpan) {
                "60年" -> 3128
                "70年" -> 3650
                "80年" -> 4171
                "90年" -> 4693
                else -> 0 // 默认值，可以根据需要修改
            }
            // 将选择的周数存储到内容提供器中
            val values = ContentValues().apply {
                put(LifeCalendarProvider.LIFESPAN_COLUMN_WEEKS, weeks)
            }
            context?.contentResolver?.insert(LifeCalendarProvider.CONTENT_URI, values)


        }

        builder.setNegativeButton("取消") { dialog, _ ->
            dialog.dismiss()
        }

        return builder.create()
    }
}