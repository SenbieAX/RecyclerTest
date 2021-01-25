package com.strelkovax.testrecycler

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.strelkovax.testrecycler.fragments.RecyclerFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        if (fragment == null) {
            val fm = supportFragmentManager
            fm.beginTransaction()
                .replace(R.id.container, RecyclerFragment())
                .commit()
        }
    }
}