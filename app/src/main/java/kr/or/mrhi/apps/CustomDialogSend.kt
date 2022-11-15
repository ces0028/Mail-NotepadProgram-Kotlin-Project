package kr.or.mrhi.apps

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import kr.or.mrhi.apps.databinding.CustomDialogSendBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CustomDialogSend(val context: Context) {
    val dialog = Dialog(context)

    @RequiresApi(Build.VERSION_CODES.O)
    fun showDialog(dateTime: String, content: String) {
        val binding = CustomDialogSendBinding.inflate(LayoutInflater.from(context))
        binding.tvFromAddress.setText(R.string.myEmail)
        if(!(dateTime.equals("") && content.equals(""))) {
            binding.edtContent.setText("${dateTime}에 작성된 메모입니다.\n${content}")
        }
        dialog.setContentView(binding.root)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()

        binding.ivClose.setOnClickListener {
            dialog.dismiss()
        }

        binding.btnSend.setOnClickListener {
            val address = binding.edtToAddress.text.toString()
            val subject = binding.edtSubject.text.toString()
            val content = binding.edtContent.text.toString()
            val today = LocalDate.now()
            val date = today.format(DateTimeFormatter.ofPattern("MM월 dd일"))

            if(address != "" && subject != "" && content != "") {
                var image = R.drawable.img_profile_01
                var randomNumber = (Math.random() * 8).toInt().toString()
                when(randomNumber) {
                    "1" -> image = R.drawable.img_profile_01
                    "2" -> image = R.drawable.img_profile_02
                    "3" -> image = R.drawable.img_profile_03
                    "4" -> image = R.drawable.img_profile_04
                    "5" -> image = R.drawable.img_profile_05
                    "6" -> image = R.drawable.img_profile_06
                    "7" -> image = R.drawable.img_profile_07
                }
                var dataSent : DataSent
                dataSent = DataSent(address, subject, content, date, image)
                (context as MailActivity).fragmentSent.refreshRecyclerViewAdd(dataSent)
                dialog.dismiss()
            } else {
                Snackbar.make(binding.root,"모든 항목을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }
}