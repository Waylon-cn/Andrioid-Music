package com.example.myapplication

import android.app.AlarmManager
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import java.util.*
import kotlin.system.exitProcess


class CustomDialog : Dialog {
    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, theme: Int) : super(context!!, theme)


    class Builder(private val context: Context) {
        private var alarmManager: AlarmManager? = null
        val timer= Timer()
        var content: String? = null
        private var positiveButtonClickListener: DialogInterface.OnClickListener? = null
        private var negativeButtonClickListener: DialogInterface.OnClickListener? = null

        fun setContent(content: String): Builder {
            this.content = content
            return this
        }

        fun setPositiveButton(listener: DialogInterface.OnClickListener): Builder {
            this.positiveButtonClickListener = listener
            return this
        }

        fun setNegativeButton(listener: DialogInterface.OnClickListener): Builder {
            this.negativeButtonClickListener = listener
            return this
        }

        fun create(): CustomDialog {

            val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            //为自定义弹框设置主题
            val customDialog = CustomDialog(context, R.style.CustomDialog)
            val view = layoutInflater.inflate(R.layout.dialog_clock, null)
            val tv=view.findViewById<EditText>(R.id.show)
            val seekbar=view.findViewById<SeekBar>(R.id.seekBar)
            var timelong:Long
            var cc:Int
            seekbar.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener{
                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        cc=progress*10
                        tv?.setText("$cc")
                    }
                    override fun onStartTrackingTouch(seekBar: SeekBar) {
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar) {
                    }
                }
            )


            customDialog.addContentView(view, LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ))
            //设置弹框内容
            content?.let {
                (view.findViewById<TextView>(R.id.dialog_content) as TextView).text = it
            }
            //设置弹框按钮
            positiveButtonClickListener?.let {
                (view.findViewById<Button>(R.id.dialog_sure) as Button).setOnClickListener {
                    positiveButtonClickListener!!.onClick(customDialog, DialogInterface.BUTTON_POSITIVE)
                    timelong=tv?.text.toString().toLong()*60*1000
                    timer.schedule(object : TimerTask() {
                        override fun run() {
                            exitProcess(-1)//退出整个程序
                        }
                    }, timelong)

                }
            } ?: run {
                (view.findViewById<Button>(R.id.dialog_sure) as Button).visibility = View.GONE
            }
            negativeButtonClickListener?.let {
                (view.findViewById<Button>(R.id.dialog_cancel) as Button).setOnClickListener {
                    negativeButtonClickListener!!.onClick(customDialog, DialogInterface.BUTTON_NEGATIVE)
                    timer.cancel()
                }
            } ?: run {
                (view.findViewById<Button>(R.id.dialog_cancel) as Button).visibility = View.GONE
            }

            customDialog.setContentView(view)
            return customDialog
        }

    }

}