package kr.or.mrhi.apps

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import kr.or.mrhi.apps.databinding.CustomDialogNoteBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CustomDialogNote(val context: Context) {
    val dialogNote = Dialog(context)

    @RequiresApi(Build.VERSION_CODES.O)
    fun showDialog() {
        val binding = CustomDialogNoteBinding.inflate(LayoutInflater.from(context))
        dialogNote.setContentView(binding.root)
        dialogNote.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialogNote.setCanceledOnTouchOutside(false)
        dialogNote.setCancelable(false)
        dialogNote.show()

        binding.ivClose.setOnClickListener {
            dialogNote.dismiss()
        }
        binding.btnSave.setOnClickListener {
            val content = binding.edtContent.text.toString()
            if(content != "") {
                val today = LocalDateTime.now()
                val dateTime = today.format(DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm"))
                val image = R.drawable.ic_star_blank
                var dataNote = DataNote(dateTime, content, image)
                (context as NoteActivity).fragmentNote.refreshRecyclerViewAdd(dataNote)
                dialogNote.dismiss()
            } else {
                Snackbar.make(binding.root,"내용을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }
}