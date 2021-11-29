package com.example.wallet

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class HomeActivity : AppCompatActivity() {
    private lateinit var button: Button
    private lateinit var etTitle: TextView
    @Throws(IOException::class, JSONException::class)
    private suspend fun httpPost(myUrl: String): String {

        val result = withContext(Dispatchers.IO) {
            val url = URL(myUrl)
            // 1. create HttpURLConnection
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8")

            // 2. build JSON object
            val jsonObject = buildJsonObject()

            // 3. add JSON content to POST request body
            setPostRequestContent(conn, jsonObject)

            // 4. make POST request to the given URL
            conn.connect()

            // 5. return response message
            conn.responseMessage + ""


        }
        return result
    }

    @SuppressLint("SetTextI18n")
    private fun checkNetworkConnection(): Boolean {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkInfo = connMgr.activeNetworkInfo
        val isConnected: Boolean = networkInfo?.isConnected ?: false
        /*if (networkInfo != null && isConnected) {
            // show "Connected" & type of network "WIFI or MOBILE"
            tvIsConnected.text = "Connected " + networkInfo.typeName
            // change background color to red
            tvIsConnected.setBackgroundColor(-0x8333da)
        } else {
            // show "Not Connected"
            tvIsConnected.text = "Not Connected"
            // change background color to green
            tvIsConnected.setBackgroundColor(-0x10000)
        }*/
        return isConnected
    }

    @Throws(JSONException::class)
    private fun buildJsonObject(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.accumulate("id", "1")
        jsonObject.accumulate("title", "Municipality of birth")
        jsonObject.accumulate("name", "birth-place")
        jsonObject.accumulate("validity", "∞")
        jsonObject.accumulate("format", "barcelona")
        jsonObject.accumulate("signature", "0x4DACEFBEAD8925A84D852A3E8D477FE75CF17D741F5F2C8105AB8691B824B7A4")
        Log.i("MESSAGE",jsonObject.toString())
        return jsonObject
    }

    @Throws(IOException::class)
    private fun setPostRequestContent(conn: HttpURLConnection, jsonObject: JSONObject) {

        val os = conn.outputStream
        val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
        writer.write(jsonObject.toString())
        Log.i(MainActivity::class.java.toString(), jsonObject.toString())
        writer.flush()
        writer.close()
        os.close()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        button = findViewById(R.id.button)
        etTitle = findViewById(R.id.etTitle)
        button.setOnClickListener {
            if (checkNetworkConnection()){
                Toast.makeText(this, "Posted", Toast.LENGTH_SHORT).show()
                lifecycleScope.launch {
                    val myUrl = etTitle.text.toString()
                    val result = httpPost(myUrl)

                }
            }
            else
                Toast.makeText(this, "Not Connected!", Toast.LENGTH_SHORT).show()
        }

        val btn1: ImageButton = findViewById(R.id.btncred)
        btn1.setOnClickListener {
            val intent = Intent(this, CredentialsActivity::class.java)
            startActivity(intent)
        }
       
        val btn3: ImageButton = findViewById(R.id.btnpend)
        btn3.setOnClickListener {
            val intent = Intent(this, PendingActivity::class.java)
            startActivity(intent)
        }
        val btn4: FloatingActionButton = findViewById(R.id.floatingActionButton4)
        btn4.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        /*val btn5: Button = findViewById(R.id.button3)
        btn5.setOnClickListener {
            val newFragment = CuadreDialeg()
            newFragment.show(supportFragmentManager, "peticions")
        }*/
    }
}