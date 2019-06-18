package com.example.workflow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.workflow.test.ExecutionTest

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val executionTest = ExecutionTest()
        executionTest.start()
    }
}
