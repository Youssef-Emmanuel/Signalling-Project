package com.example.signallingproject2

import User
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.example.signallingproject2.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()

        val uid = auth.currentUser?.uid ?: return

        val userRef = db.getReference("users").child(uid)
        userRef.get().addOnSuccessListener {
            val user = it.getValue(User::class.java)
            binding.etName.setText(user?.name)
            binding.etPhone.setText(user?.phone)
            binding.etAddress.setText(user?.address)
            binding.etEmail.setText(user?.email)
        }

        binding.btnSave.setOnClickListener {
            val updatedUser = User(
                binding.etName.text.toString(),
                binding.etPhone.text.toString(),
                binding.etAddress.text.toString(),
                binding.etEmail.text.toString()
            )
            userRef.setValue(updatedUser).addOnSuccessListener {
                Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
