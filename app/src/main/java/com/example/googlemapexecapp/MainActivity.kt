package com.example.googlemapexecapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.net.URLEncoder

class MainActivity : AppCompatActivity()   {

    //キーワードエディットテキストオブジェクト
    private lateinit var etKeyword: EditText

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

        //キーワードエディットテキストオブジェクト取得
        etKeyword = findViewById<EditText>(R.id.etInputKeyword)

        //TextWatcherでキーワード入力状況を監視する
        etKeyword.addTextChangedListener(object : TextWatcher {

            //静的関数の実装
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            //文字列入力中に呼び出される
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                Log.d("MainActivity", "onTextChanged in count = " + count)

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

        //サンプルのListオブジェクトを用意。
        val menuList: MutableList<MutableMap<String, String>> = mutableListOf()
        //「から揚げ定食」のデータを格納するMapオブジェクトの用意とmenuListへのデータ登録。
        var menus = mutableMapOf("keyword" to "東京駅", "daytime" to "800", "desc" to "若鳥のから揚げにサラダ、ご飯とお味噌汁が付きます。")
        menuList.add(menus)
        //「ハンバーグ定食」のデータを格納するMapオブジェクトの用意とmenuListへのデータ登録。
        menus = mutableMapOf("keyword" to "京都駅", "daytime" to "850", "desc" to "手ごねハンバーグにサラダ、ご飯とお味噌汁が付きます。")
        menuList.add(menus)
        //以下データ登録の繰り返し。
        menus = mutableMapOf("keyword" to "庄内空港", "daytime" to "850", "desc" to "すりおろし生姜を使った生姜焼きにサラダ、ご飯とお味噌汁が付きます。")
        menuList.add(menus)

        //アダプタオブジェクトを生成。
        val adapter = RecyclerListAdapter(menuList)
        //RecyclerViewにアダプタオブジェクトを設定。
        lvMenu.adapter = adapter

        //区切り専用のオブジェクトを生成。
        val decorator = DividerItemDecoration(applicationContext, layout.orientation)
        //RecyclerViewに区切り線オブジェクトを設定。
        lvMenu.addItemDecoration(decorator)

        Log.d("MainActivity", "onCreate out")
    }

    /**
     * RecyclerViewのビューホルダクラス。
     */
    private inner class RecyclerListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /**
         * リスト1行分中でキーワードを表示する画面部品。
         */
        var tvKeyword: TextView
        /**
         * リスト1行分中で日時を表示する画面部品。
         */
        var tvDaytime: TextView

        init {
            //引数で渡されたリスト1行分の画面部品中から表示に使われるTextViewを取得。
            tvKeyword = itemView.findViewById(R.id.tvKeyword)
            tvDaytime = itemView.findViewById(R.id.tvDaytime)
        }
    }

    /**
     * RecyclerViewのアダプタクラス。
     */
    private inner class RecyclerListAdapter(private val _listData: MutableList<MutableMap<String, String>>): RecyclerView.Adapter<RecyclerListViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerListViewHolder {
            //レイアウトインフレータを取得。
            val inflater = LayoutInflater.from(applicationContext)
            //row.xmlをインフレートし、1行分の画面部品とする。
            val view = inflater.inflate(R.layout.kwlist, parent, false)
            //インフレートされた1行分の画面部品にリスナを設定。
            view.setOnClickListener(ItemClickListener())
            //ビューホルダオブジェクトを生成。
            val holder = RecyclerListViewHolder(view)
            //生成したビューホルダをリターン。
            return holder
        }

        override fun onBindViewHolder(holder: RecyclerListViewHolder, position: Int) {
            //リストデータから該当1行分のデータを取得。
            val item = _listData[position]
            //キーワード文字列を取得。
            val keyword = item["keyword"] as String
            //日時を取得。
            val daytime = item["daytime"] as String

            //メニュー名と金額をビューホルダ中のTextViewに設定。
            holder.tvKeyword.text = keyword
            holder.tvDaytime.text = daytime
        }

        override fun getItemCount(): Int {
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
        //ACCESS_LOCATIONに対するパーミッションで許可が選択されたら
        if(requestCode == 1000 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //LocationManagerオブジェクトを取得
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            //位置情報が更新された際のリスナオブジェクトを生成
           // val locationListener = GPSLocationListener()

            if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return
            }
        }
    }

    /**
     * リストをタップした時のリスナクラス。
     */
    private inner class ItemClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            //タップされたLinearLayout内にあるメニュー名表示TextViewを取得。
            val tvMenuName = view.findViewById<TextView>(R.id.tvKeyword)
            //メニュー名表示TextViewから表示されているメニュー名文字列を取得。
            val menuName = tvMenuName.text.toString()
            //トーストに表示する文字列を生成。
            val msg = "ご注文" + menuName
            //トースト表示。
            Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
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
     * onOptionsItemSelected
     *
     * @param item
     * @return
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        Log.d("MainActivity", "onOptionsItemSelected in")

        //メニューで選択された場合
        when(item.itemId) {
            //現在地が選択された場合、GPSActivityに遷移する
            R.id.ofMenuOptionGPS -> {
                //インテントオブジェクトを生成
                val intent = Intent(applicationContext, GPSActivity::class.java)
                //オフジェクトを起動
                startActivity(intent)
                Log.d("MainActivity", "startActivity")
            }

            //終了が選択された場合、終了処理を行う
            R.id.ofMenuOptionExit -> {
                //finishAndRemoveTask()はAPILevel21以降
                //finish()の場合、excludeFromRecents="true"を設定しないとタスク画面の履歴に残る
                Log.d("MainActivity", "onOptionsItemSelected finish()")
                finish()
            }
        }

        Log.d("MainActivity", "onOptionsItemSelected out")

        return super.onOptionsItemSelected(item)
    }

    /**
     * onMapShowButtonClick
     * Map表示ボタン押下時の処理。layoutのxmlに定義する
     *
     * @param view
     */
    fun onMapShowButtonClick(view: View) {
        Log.d("MainActivity", "onMapShowButtonClick in")

        //検索するキーワードEditTextのViewを取得

        var keyword = etKeyword.text.toString()

        //キーワード文字列をURLエンコードする
        keyword = URLEncoder.encode(keyword, "UTF-8")

        //マップアプリと連携する、URIオブジェクトを作成
        // "?q="を使ってURIエンコードされたクエリ文字列を渡したインテントリクエスト
        val uriStr = "geo:0,0?q=${keyword}"
        val gmmIntentUri = Uri.parse(uriStr)
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        //mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)

        Log.d("MainActivity", "onMapShowButtonClick out")
    }


}

