package com.example.googlemapexecapp

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment

class AllListDelConfirmDialogFragment: DialogFragment() {

    public var testd: Int = 1

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //ダイアログビルダを生成
        val builder = AlertDialog.Builder(activity)
        //ダイアログのタイトルを生成
        builder.setTitle("確認")
        //ダイアログのメッセージを生成
        builder.setMessage("メッセージ")
        //ボタンを設定
        builder.setPositiveButton("はい", DialogButtonClickListener())
        builder.setNegativeButton("いいえ", DialogButtonClickListener())

        val dialog = builder.create()
        return dialog
    }

    private inner class DialogButtonClickListener: DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface?, which: Int) {

            //タップされたアクションボタンで分岐
            when(which) {
                //ポジティブボタン
                DialogInterface.BUTTON_POSITIVE -> {
                    testd = 2
                    Log.d("DialogInterface", "onClick BUTTON_POSITIVE")
                }
                //ネガティブボタン
                DialogInterface.BUTTON_NEGATIVE -> {
                    testd = 1
                    Log.d("DialogInterface", "onClick BUTTON_NEGATIVE")
                }
            }
        }
    }
}