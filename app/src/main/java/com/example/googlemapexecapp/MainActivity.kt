package com.example.googlemapexecapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), AllListDelConfirmDialogFragment.NoticeDialogLister  {

    //キーワードエディットテキストオブジェクト
    private lateinit var etKeyword: EditText

    //キーワードリストクラス
    private val keylist = KeywordList(this@MainActivity)

    //キーワード用アダプター
    private lateinit var adapter: RecyclerListAdapter

    /**
     * MainActivityのonCreate
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {

        Log.d("MainActivity", "onCreate in")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Map表示ボタンのオブジェクト取得
        val mapBtn = findViewById<Button>(R.id.btMapKeyword)
        //初期化ではクリックを無効にする
        mapBtn.isEnabled = false

        //-----------------------
        // リストにデータをロードする
        keylist.startList()

        //キーワードエディットテキストオブジェクト取得
        etKeyword = findViewById(R.id.etInputKeyword)

        //TextWatcherでキーワード入力状況を監視する
        etKeyword.addTextChangedListener(object : TextWatcher {

            //静的関数の実装
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            //文字列入力中に呼び出される
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                Log.d("MainActivity", "onTextChanged in count = $count")

                //エディットボックスが空欄の場合は、ボタンを無効とする
                when(count) {
                    0 -> {
                        //ボタンをを無効にする
                        mapBtn.isEnabled = false
                    }
                    else -> {
                        //ボタンを有効にする
                        mapBtn.isEnabled = true
                    }
                }
                Log.d("MainActivity", "onTextChanged out")
            }
        })


        //RecyclerView設定
        val lvMenu = findViewById<RecyclerView>(R.id.recycler_view)
        //LinearLayoutManagerオブジェクトを生成。
        val layout = LinearLayoutManager(applicationContext)
        //レイアウトマネージャーを登録
        lvMenu.layoutManager = layout

        //アダプタオブジェクトを生成。
        adapter = RecyclerListAdapter(keylist.get())
        //RecyclerViewにアダプタオブジェクトを設定。
        lvMenu.adapter = adapter

        //区切り専用のオブジェクトを生成。
        val decorator = DividerItemDecoration(applicationContext, layout.orientation)
        //RecyclerViewに区切り線オブジェクトを設定。
        lvMenu.addItemDecoration(decorator)

        //GPSのパーミッション確認
        if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //許可を求めるダイアログを表示する
            val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            ActivityCompat.requestPermissions(this@MainActivity, permissions, 1000)
            return
        }

        Log.d("MainActivity", "onCreate out")
    }

    /**
     * Acrtivityから制御が移動したら呼ばれる
     * Databaseの全ての情報を再登録する
     *
     */
    override fun onPause() {

        Log.d("MainActivity", "onPause in")

        //Databaseの全ての情報を再登録
        keylist.saveData()

        super.onPause()

        Log.d("MainActivity", "onPause out")
    }

    /**
     * 終了時に呼ばれる
     *　データベース終了処理
     */
    override fun onDestroy() {

        Log.d("MainActivity", "onDestroy in")

        //キーリスト・データベース終了処理
        keylist.close()
        super.onDestroy()

        Log.d("MainActivity", "onDestroy out")
    }

    /**
     * RecyclerViewのビューホルダクラス。
     */
    private inner class RecyclerListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /**
         * リスト1行分中でキーワードを表示する画面部品。
         */
        var tvKeyword: TextView = itemView.findViewById(R.id.tvKeyword)

        /**
         * リスト1行分中で日時を表示する画面部品。
         */
        var tvDaytime: TextView = itemView.findViewById(R.id.tvDaytime)

    }

    /**
     * RecyclerViewのアダプタクラス。
     */
    private inner class RecyclerListAdapter(private val _listData: MutableList<MutableMap<String, String>>): RecyclerView.Adapter<RecyclerListViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerListViewHolder {

            Log.d("RecyclerListAdapter", "onCreateViewHolder in")

            //レイアウトインフレータを取得。
            val inflater = LayoutInflater.from(applicationContext)
            //lwlist.xmlをインフレートし、1行分の画面部品とする。
            val view = inflater.inflate(R.layout.kwlist, parent, false)
            //インフレートされた1行分の画面部品にリスナを設定。
            view.setOnClickListener(ItemClickListener())
            //ビューホルダオブジェクトを生成。
            val holder = RecyclerListViewHolder(view)

            Log.d("RecyclerListAdapter", "onCreateViewHolder out")

            //生成したビューホルダをリターン。
            return holder
        }

        override fun onBindViewHolder(holder: RecyclerListViewHolder, position: Int) {

            Log.d("RecyclerListAdapter", "onBindViewHolder in")

            //リストデータから該当1行分のデータを取得。
            val item = _listData[position]
            //キーワード文字列を取得。
            val keyword = item["keyword"] as String
            //日時を取得。
            val daytime = item["daytime"] as String

            //メニュー名と金額をビューホルダ中のTextViewに設定。
            holder.tvKeyword.text = keyword
            holder.tvDaytime.text = daytime

            Log.d("RecyclerListAdapter", "onBindViewHolder out")

        }

        override fun getItemCount(): Int {

            Log.d("RecyclerListAdapter", "getItemCount in / out")

            //リストデータ中の件数をリターン。
            return _listData.size
        }
    }

    /**
     * パーミッションダイアログの結果取得
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        Log.d("RecyclerListAdapter", "onRequestPermissionsResult in")

        //ACCESS_LOCATIONに対するパーミッションで許可が選択されなかったら終了
        if(requestCode == 1000 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Log.d("RecyclerListAdapter", "onRequestPermissionsResult finish")
            finish()
        }

        Log.d("RecyclerListAdapter", "onRequestPermissionsResult out")

        return
    }

    /**
     * リストをタップした時のリスナクラス。
     */
    private inner class ItemClickListener : View.OnClickListener {
        override fun onClick(view: View) {

            Log.d("ItemClickListener", "onClick in")

            //タップされたLinearLayout内にあるキーワードTextViewを取得。
            val tvKeyword = view.findViewById<TextView>(R.id.tvKeyword)
            //メニュー名表示TextViewから表示されているメニュー名文字列を取得。
            val keyword = tvKeyword.text.toString()

            //キーワードでGoogleMapを起動する
            val mapIntent = Gglmap.makeIntent(keyword)
            startActivity(mapIntent)

            //-----------------------------
            //キーワードと時刻をリストに登録
            keylist.add(keyword, "")

            //-----------------------------
            //アダプターにリストの再描画を指示する
            adapter.notifyDataSetChanged()

            Log.d("ItemClickListener", "onClick out")
        }
    }

    /**
     * onCreateOptionsMenu
     *
     * <p>メニュー初期化処理</p>
     *
     * @param menu
     * @return
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        Log.d("MainActivity", "onCreateOptionsMenu in")

        //オプションメニュー用のxmlをインフレイト
        menuInflater.inflate(R.menu.menu_options_list, menu)

        Log.d("MainActivity", "onCreateOptionsMenu out")

        return super.onCreateOptionsMenu(menu)
    }

    /**
     * オプションアイテムがタップされたときのリスナ
     *
     * @param item
     * @return
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        Log.d("MainActivity", "onOptionsItemSelected in")

        //メニューで選択された場合
        when(item.itemId) {

            //ゴミ箱が選択された場合
            R.id.opMenuDelete -> {
                //リストが空でなければメッセージでリストの削除を確認するメッセージを表示する
                if (!keylist.IsListEmpty()) {
                    //ダイアログメッセージを表示
                    val dialogFragment = AllListDelConfirmDialogFragment()
                    dialogFragment.show(supportFragmentManager, "AllListDelConfirmDialogFragment")
                    Log.d("MainActivity", "R.id.opMenuDelete end")
                }
            }

            //現在地が選択された場合、GPSActivityに遷移する
            R.id.opMenuOptionGPS -> {
                //インテントオブジェクトを生成
                val intent = Intent(applicationContext, GPSActivity::class.java)
                //オフジェクトを起動
                startActivityForResult(intent, resCodeGPS)
                Log.d("MainActivity", "R.id.opMenuOptionGPS end")
            }

            //終了が選択された場合、終了処理を行う
            R.id.opMenuOptionExit -> {
                //finishAndRemoveTask()はAPILevel21以降
                //finish()の場合、excludeFromRecents="true"を設定しないとタスク画面の履歴に残る
                Log.d("MainActivity", "R.id.opMenuOptionExit finish()")
                finish()
            }
        }

        Log.d("MainActivity", "onOptionsItemSelected out")

        return super.onOptionsItemSelected(item)
    }

    /**
     * GPSアクティビティからの戻り情報を受け取る
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("MainActivity", "onActivityResult in")

        //GPSActivityからのリザルトコードの場合
        if (requestCode == resCodeGPS) {
            Log.d("MainActivity", "resCodeGPS in")
            //GPSで検索したURIを受け取る
            val uri = data?.getStringExtra("ruiStr")
            if (uri != null) {
                if (uri != "") {
                    //リストに追加
                    keylist.add(uri, "")

                    //アダプターにリストの再描画を指示する
                    adapter.notifyDataSetChanged()
                }
            }
            Log.d("MainActivity", "resCodeGPS out")
        }
        Log.d("MainActivity", "onActivityResult out")
    }

    /**
     * onMapShowButtonClick
     * Map表示ボタン押下時の処理。layoutのxmlに定義する
     *
     * @param view
     */
    fun onMapShowButtonClick(@Suppress("UNUSED_PARAMETER")  view: View) {
        Log.d("MainActivity", "onMapShowButtonClick in")

        //EditTextのキーワードでGoogleMapを起動する
        val keywordstr = etKeyword.text.toString()
        val mapIntent = Gglmap.makeIntent(keywordstr)
        startActivity(mapIntent)

        //　Editbox空欄化
        etKeyword.text = null

        //キーワードと時刻をリストに登録
        keylist.add(keywordstr, "")

        //アダプターにリストの再描画を指示する
        adapter.notifyDataSetChanged()


        Log.d("MainActivity", "onMapShowButtonClick out")
    }

    /**
     * 削除ダイアログからのコールバック
     * 削除が選択された場合
     *
     * @param dialog　AllListDelConfirmDialogFragmentクラス
     */
    override fun onDialogPositiveClick(dialog: DialogFragment) {
        //リストを全削除
        this.keylist.removeListAll()

        //アダプターにリストの再描画を指示する
        adapter.notifyDataSetChanged()
    }

    /**
    * 削除ダイアログからのコールバック
    * 削除が選択された場合は処理なし
    *
    * @param dialog　AllListDelConfirmDialogFragmentクラス
    */
    override fun onDialogNegativeClick(dialog: DialogFragment) {
    }

    companion object {
        private const val resCodeGPS = 200
    }

}

