package com.petoi.kotlin.android.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.petoi.kotlin.android.app.properties.PetoiPreferences

class LegalInfoActivity: AppCompatActivity() {

    private lateinit var preferences: PetoiPreferences

    private fun setProperties() {
        PetoiPreferences(this).also { preferences = it }
        preferences.setFirstTime(this)
    }

    private fun bindAgreeBtn() {
        val btn = findViewById<Button>(R.id.btn_legal_agree)

        btn.setOnClickListener {
            setProperties()

            intent = Intent(this@LegalInfoActivity, ConnectionActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun bindDisagreeBtn() {
        val btn = findViewById<Button>(R.id.btn_legal_disagree)

        btn.setOnClickListener {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_legal)

        bindAgreeBtn()
        bindDisagreeBtn()
    }
}