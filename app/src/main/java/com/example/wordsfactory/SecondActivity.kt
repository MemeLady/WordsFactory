package com.example.wordsfactory

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.wordsfactory.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySecondBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)

        binding.backBtn.setOnClickListener {
            val intent = Intent(this, FirstActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.signupBtn.setOnClickListener {
            isEmptyText()
        }

    }

    private fun dialogCreate() {
        var dialog= Dialog(this)
        dialog.setContentView(R.layout.dialog)
        dialog.show()

        val okBtn = dialog.findViewById(R.id.okBtn) as Button
        okBtn.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun isEmptyText()
    {
        if (binding.editTextName.getText().toString().equals("") ||
            binding.editTextTextEmail.getText().toString().equals("") ||
            binding.editTextTextPassword.getText().toString().equals("")) {
            dialogCreate()
        }
        else{
            val intent = Intent(this, ThirdActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}