package com.zeehail.cash.counter

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.testing.FakeReviewManager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var mInterstitialAd: InterstitialAd
    var total = 0
    //val manager = ReviewManagerFactory.create(this)
    val REQUEST_EXTERNAL_STORAGE = 1
    val PERMISSIONS_STORAGE = arrayOf<String>(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    var totalNotes = 0
    var file:File?=null
    var txt2000 = 0;
    var txt500 = 0;
    var txt200 = 0
    var txt100 = 0;
    var txt50 = 0;
    var txt20 = 0;
    var txt10 = 0;
    var txt5 = 0;
    var txt2 = 0;
    var txt1 = 0
    var note2000 = 0;
    var note500 = 0;
    var note200 = 0;
    var note100 = 0;
    var note50 = 0
    var note20 = 0
    var note10 = 0
    var note5 = 0
    var note2 = 0
    var note1 = 0

    @SuppressLint("SimpleDateFormat", "MissingPermission", "SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        verifyStoragePermissions(this@MainActivity)
        if (ConnectionManager().checkConnectivity(this@MainActivity)){
        MobileAds.initialize(this@MainActivity) {}
        mInterstitialAd = InterstitialAd(this@MainActivity)
        mInterstitialAd.adUnitId = "ca-app-pub-6449044710108986/5197909645"
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdClosed() {
                mInterstitialAd.loadAd(AdRequest.Builder().build())
            }

        }
            if (mInterstitialAd.isLoaded) {
                mInterstitialAd.show()
            } else {
                //Toast.makeText(this@MainActivity, "The interstitial wasn't loaded yet.", Toast.LENGTH_SHORT).show()
            }
            }
     /*else{
            val dialog = AlertDialog.Builder(this@MainActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection not Found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                finish()
            }
            dialog.setNegativeButton("Cancel") { text, listener ->
                ActivityCompat.finishAffinity(this@MainActivity)
                /*val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.theopen.android"))
                    startActivity(intent)*/
            }
            dialog.create()
            dialog.setCancelable(false)
            dialog.show()
        }*/

        var et2000 = findViewById<EditText>(R.id.et2000)
        var txtResult2000 = findViewById<TextView>(R.id.txtResult2000)
        var et500 = findViewById<EditText>(R.id.et500)
        var txtResult500 = findViewById<TextView>(R.id.txtResult500)
        var et200 = findViewById<EditText>(R.id.et200)
        var txtResult200 = findViewById<TextView>(R.id.txtResult200)
        var et100 = findViewById<EditText>(R.id.et100)
        var txtResult100 = findViewById<TextView>(R.id.txtResult100)
        var et50 = findViewById<EditText>(R.id.et50)
        var txtResult50 = findViewById<TextView>(R.id.txtResult50)
        var et20 = findViewById<EditText>(R.id.et20)
        var txtResult20 = findViewById<TextView>(R.id.txtResult20)
        var et10 = findViewById<EditText>(R.id.et10)
        var txtResult10 = findViewById<TextView>(R.id.txtResult10)
        var et5 = findViewById<EditText>(R.id.et5)
        var txtResult5 = findViewById<TextView>(R.id.txtResult5)
        var et2 = findViewById<EditText>(R.id.et2)
        var txtResult2 = findViewById<TextView>(R.id.txtResult2)
        var ll: LinearLayout = findViewById(R.id.ll)
        var et1 = findViewById<EditText>(R.id.et1)
        var txtResult1 = findViewById<TextView>(R.id.txtResult1)
        var txtResult = findViewById<TextView>(R.id.txtResult)
        var txtNoteResult = findViewById<TextView>(R.id.txtNoteResult)
        var btnSave: View = findViewById<View>(R.id.save)
        var btnShare: View = findViewById(R.id.share)
        var txtDate = findViewById<TextView>(R.id.txtDate)
        var btnReset: View = findViewById(R.id.reset)
        var etName=findViewById<EditText>(R.id.etName)
        val watcher: TextWatcher
        val df: DateFormat = SimpleDateFormat.getDateTimeInstance()
        //val dateTime = df.format(Calendar.getInstance().time)
        val today = Date()
        val format = SimpleDateFormat("yyyy-MM-dd hh:mm")
        val dateTime = format.format(today)
        //Toast.makeText(this@MainActivity,"$dateTime",Toast.LENGTH_LONG).show()
        txtDate.text = dateTime.toString()
        var temp: String
        btnSave.setOnClickListener {
            verifyStoragePermissions(this@MainActivity)
            if (TextUtils.isEmpty(etName.text)) {
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("Error")
                dialog.setMessage("Please Enter Name")

                dialog.setNegativeButton("OK") { _, _ ->

                }
                dialog.create()
                dialog.setCancelable(false)
                dialog.show()
                etName.error = "Please Enter Name"
            } else {

                val rootView = window.decorView.findViewById<View>(android.R.id.content)
                var bitmap: Bitmap = getScreenShot(rootView)
                store(bitmap, "${etName.text}"+".png")
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("File Saved at:")
                dialog.setMessage("$file")

                dialog.setNegativeButton("OK") { _, _ ->

                }
                dialog.create()
                dialog.setCancelable(false)
                dialog.show()
                if (ConnectionManager().checkConnectivity(this@MainActivity)) {
                    if (mInterstitialAd.isLoaded) {
                        mInterstitialAd.show()
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.")
                    }
                }
            }
        }
        btnShare.setOnClickListener {
            verifyStoragePermissions(this@MainActivity)
            if(TextUtils.isEmpty(etName.text)){
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("Error")
                dialog.setMessage("Please Enter Name")

                dialog.setNegativeButton("OK") { _, _ ->

                }
                dialog.create()
                dialog.setCancelable(false)
                dialog.show()
                etName.error = "Please Enter Name"
            }
            else{
            val rootView = window.decorView.findViewById<View>(android.R.id.content)
            var bitmap: Bitmap = getScreenShot(rootView)
            var file=store(bitmap, "${etName.text}"+".png")
                if (ConnectionManager().checkConnectivity(this)) {
                    if (mInterstitialAd.isLoaded) {
                        mInterstitialAd.show()
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.")
                    }
                }
            if (file != null) {
                shareImage(file)
            }
            }
        }

        btnReset.setOnClickListener {
            finish()
            intent = Intent(this@MainActivity, MainActivity::class.java)
            startActivity(intent)

        }


        watcher = object : TextWatcher {
            override fun afterTextChanged(mEdit: Editable) {}


            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                when (hasWindowFocus()) {
                    et2000.hasFocus() -> {
                        temp = et2000.text.toString()
                        note2000 = countNotes(temp)
                        txt2000 = calculate(temp, 2000, txtResult2000)
                    }
                    et500.hasFocus() -> {
                        temp = et500.text.toString()
                        note500 = countNotes(temp)
                        txt500 = calculate(temp, 500, txtResult500)

                    }
                    et200.hasFocus() -> {
                        temp = et200.text.toString()
                        note200 = countNotes(temp)
                        txt200 = calculate(temp, 200, txtResult200)
                    }
                    et100.hasFocus() -> {
                        temp = et100.text.toString()
                        note100 = countNotes(temp)
                        txt100 = calculate(temp, 100, txtResult100)
                    }
                    et50.hasFocus() -> {
                        temp = et50.text.toString()
                        note50 = countNotes(temp)
                        txt50 = calculate(temp, 50, txtResult50)
                    }
                    et20.hasFocus() -> {
                        temp = et20.text.toString()
                        note20 = countNotes(temp)
                        txt20 = calculate(temp, 20, txtResult20)
                    }
                    et10.hasFocus() -> {
                        temp = et10.text.toString()
                        note10 = countNotes(temp)
                        txt10 = calculate(temp, 10, txtResult10)
                    }
                    et5.hasFocus() -> {
                        temp = et5.text.toString()
                        note5 = countNotes(temp)
                        txt5 = calculate(temp, 5, txtResult5)
                    }
                    et2.hasFocus() -> {
                        temp = et2.text.toString()
                        note2 = countNotes(temp)
                        txt2 = calculate(temp, 2, txtResult2)
                    }
                    et1.hasFocus() -> {
                        temp = et1.text.toString()
                        note1 = countNotes(temp)
                        txt1 = calculate(temp, 1, txtResult1)
                    }

                }
                total =
                    txt2000 + txt500 + txt200 + txt100 + txt50 + txt20 + txt10 + txt5 + txt2 + txt1
                totalNotes =
                    note2000 + note500 + note200 + note100 + note50 + note20 + note10 + note5 + note2 + note1
                txtNoteResult.text = totalNotes.toString()
                txtResult.text = total.toString()
            }
        }




        et2000.addTextChangedListener(watcher)
        et500.addTextChangedListener(watcher)
        et200.addTextChangedListener(watcher)
        et100.addTextChangedListener(watcher)
        et50.addTextChangedListener(watcher)
        et20.addTextChangedListener(watcher)
        et10.addTextChangedListener(watcher)
        et5.addTextChangedListener(watcher)
        et2.addTextChangedListener(watcher)
        et1.addTextChangedListener(watcher)

    }

    fun calculate(temp: String, num: Int, view: TextView): Int {
        var int2000 = 0
        if (temp.isNotEmpty()) {
            int2000 = Integer.parseInt(temp)
        }
        var result = int2000 * num
        view.text = result.toString()
        if (temp.isEmpty()) {
            view.text = "0"
        }
        return result

    }

    fun countNotes(tmp: String): Int {
        var note: Int
        if (!tmp.isNullOrEmpty()) {
            note = Integer.parseInt(tmp)
        } else {
            note = 0
        }
        return note
    }

    fun verifyStoragePermissions(activity: Activity) {
        // Check if we have write permission
        val permission =
            ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }

    fun getScreenShot(view: View): Bitmap {
        val screenView = view.rootView
        screenView.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(screenView.drawingCache)
        screenView.isDrawingCacheEnabled = false
        return bitmap
    }

    fun store(bm: Bitmap, fileName: String): File? {
        var fos: OutputStream
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        val dirPath: String =
            Environment.getExternalStorageDirectory().absolutePath
                .toString() + "/Pictures/CurrencyCounter"
        val dir = File(dirPath)
        if (!dir.exists()) dir.mkdirs()
        file = File(dirPath, fileName)
        try{
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
                var resolver=contentResolver
                var contentValues= ContentValues()
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,"$fileName")
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE,"image/png")
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH,Environment.DIRECTORY_PICTURES+File.separator+"CurrencyCounter")
                var imageUri =resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues)
                fos= resolver.openOutputStream(Objects.requireNonNull(imageUri)!!)!!
                bm.compress(Bitmap.CompressFormat.PNG,100,fos)
                Objects.requireNonNull(fos)
                review()
                //Toast.makeText(this,"File saved at ${Environment.DIRECTORY_PICTURES}/CurrencyCounter",Toast.LENGTH_SHORT).show()
                return file

            }

                val fOut = FileOutputStream(file)
                bm.compress(Bitmap.CompressFormat.PNG, 100, fOut)
                fOut.flush()
                fOut.close()
                review()
                //Toast.makeText(this@MainActivity, "Saved to $dirPath", Toast.LENGTH_LONG).show()
                return file

        }
        catch(e:Exception){
            Toast.makeText(this,"$e",Toast.LENGTH_SHORT).show()
            return null
        }

    }
    private fun shareImage(file: File) {
        val uri = Uri.fromFile(file)
        val intent = Intent()
        //Toast.makeText(this,"$uri",Toast.LENGTH_LONG).show()
        intent.action = Intent.ACTION_SEND
        intent.type = "image/png"
        intent.putExtra(Intent.EXTRA_SUBJECT, "")
        intent.putExtra(Intent.EXTRA_TEXT, "")
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        try {
            startActivity(Intent.createChooser(intent, "Share Screenshot"))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this@MainActivity, "No App Available", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if (ConnectionManager().checkConnectivity(this@MainActivity)) {
                if (mInterstitialAd.isLoaded) {
                    mInterstitialAd.show()
                } else {
                    //Log.d("TAG", "The interstitial wasn't loaded yet.")
                }
            }
            val dialog = AlertDialog.Builder(this@MainActivity)
            dialog.setTitle("Confirmation")
            dialog.setMessage("Are you sure you want to exit?")
            dialog.setPositiveButton("Yes") { text, listener ->
                ActivityCompat.finishAffinity(this)
            }
            dialog.setNegativeButton("No") { text, listener ->

            }

            dialog.create()
            dialog.setCancelable(false)
            dialog.show()
        }
        return false
    }
    fun review(){
        val manager = ReviewManagerFactory.create(this)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { request ->
            if (request.isSuccessful) {
                val reviewInfo = request.result
            } else {
                //Handle the error here
            }
        }
        request.addOnCompleteListener { request ->
            if (request.isSuccessful) {
                val reviewInfo = request.result
                val flow = manager.launchReviewFlow(this, reviewInfo)
                flow.addOnCompleteListener { _ ->
                    //Continue your application process
                }
            } else {
                //Handle the error here
            }
        }
    }
}
