package kr.or.mrhi.apps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kr.or.mrhi.apps.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adminId = "ceshin0028@gmail.com"
        val adminPw = "123456"

        binding.btnLogin.setOnClickListener {
            if (binding.tvId.text.toString().equals(adminId) && binding.tvPw.text.toString().equals(adminPw)) {
                val intent = Intent(this, MailActivity::class.java)
                intent.putExtra("id", binding.tvId.text.toString())
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "아이디 또는 비밀번호가 틀립니다.\n확인 후 다시 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}