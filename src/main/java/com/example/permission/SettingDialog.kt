package com.alan.administrator.test.permission

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.permission.R


class SettingDialog : DialogFragment() {
    var onClickListener: OnClickListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_setting, container, false)

        view.findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
            with(onClickListener) {
                this?.onCancel()
            }
            dismiss()
        }
        view.findViewById<TextView>(R.id.tv_positive).setOnClickListener {
            with(onClickListener) {
                this?.onPositive()
            }
            dismiss()
        }
        view.findViewById<TextView>(R.id.tv_content).text = arguments?.getString("content")

        return view
    }

    fun setContent(conetent: String): SettingDialog {
        val bundle = Bundle()
        bundle.putString("content", conetent)
        arguments = bundle
        return this
    }


    fun setOnClickListener(onClickListener: OnClickListener?): SettingDialog {
        this.onClickListener = onClickListener
        return this
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var dialog = super.onCreateDialog(savedInstanceState)
        dialog.window.setBackgroundDrawableResource(R.drawable.bg_round4)


        return dialog
    }


    fun show() {
        show(activity?.supportFragmentManager, "SettingDialog")
    }

    companion object {
        fun getInstance() = SettingDialog()
    }

    interface OnClickListener {
        fun onPositive()
        fun onCancel() {

        }
    }

}