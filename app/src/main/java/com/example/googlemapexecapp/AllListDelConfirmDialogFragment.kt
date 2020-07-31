package com.example.googlemapexecapp

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment


class AllListDelConfirmDialogFragment: DialogFragment() {

    //コールバック用インターフェース(呼び出し側に実装する)
    interface NoticeDialogLister {
        fun onDialogPositiveClick(dialog:DialogFragment)
        fun onDialogNegativeClick(dialog:DialogFragment)
    }

    var mLister:NoticeDialogLister? = null


    override fun onAttach(context: Context) {

        Log.d("DialogFragment", "onAttach in")

        super.onAttach(context)
        try {
            mLister = context as NoticeDialogLister
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement NoticeDialogListener")
        }
        Log.d("DialogFragment", "onAttach out")
    }

    /**
     * ダイアログ起動時に呼び出される
     *
     * @param savedInstanceState
     * @return
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        Log.d("DialogFragment", "onCreateDialog in")

        //ダイアログビルダを生成
        val builder = AlertDialog.Builder(activity)
        //ダイアログのタイトルを生成
        builder.setTitle("確認")
        //ダイアログのメッセージを生成
        builder.setMessage("全ての履歴を削除してよろしいですか？")
        //ボタンを設定
        builder.setPositiveButton("はい", DialogButtonClickListener())
        builder.setNegativeButton("いいえ", DialogButtonClickListener())

        val dialog = builder.create()

        Log.d("DialogFragment", "onCreateDialog out")

        return dialog
    }

    /**
     * ダイアログ上のボタンが選択された時のリスナクラス
     */
    private inner class DialogButtonClickListener: DialogInterface.OnClickListener {

        /**
         * ボタンを選択されたときのリスナ
         * 親ActivityのNoticeDialogListerインターフェースメソッドを呼び出す＝コールバックで結果を伝える
         *
         * @param dialog
         * @param which
         */
        override fun onClick(dialog: DialogInterface?, which: Int) {

            Log.d("DialogFragment", "onClick in")

            //タップされたアクションボタンで分岐
            when(which) {
                //ポジティブボタン
                DialogInterface.BUTTON_POSITIVE -> {
                    mLister?.onDialogPositiveClick(this@AllListDelConfirmDialogFragment)
                    Log.d("DialogInterface", "onClick BUTTON_POSITIVE")
                }
                //ネガティブボタン
                DialogInterface.BUTTON_NEGATIVE -> {
                    mLister?.onDialogNegativeClick(this@AllListDelConfirmDialogFragment)
                    Log.d("DialogInterface", "onClick BUTTON_NEGATIVE")
                }
            }

            Log.d("DialogFragment", "onClick out")
        }
    }
}