package kr.or.mrhi.apps

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import kr.or.mrhi.apps.databinding.FragmentNoteBinding
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FragmentNote : Fragment() {
    lateinit var binding: FragmentNoteBinding
    var dataList = mutableListOf<DataNote>()
    var customAdapterNote = CustomAdapterNote(dataList)
    lateinit var noteActivity : NoteActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        noteActivity = context as NoteActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNoteBinding.inflate(inflater, container, false)
        val today = LocalDateTime.now()
        for(i in 1 .. 30){
            dataList.add(DataNote("${today.minusDays((i).toLong()).format(DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm"))}", "NOTE ${31 - i}", R.drawable.ic_star_blank))
        }
        val gridLayoutManager = GridLayoutManager(container?.context, 2)
        binding.noteRecyclerView.layoutManager = gridLayoutManager
        binding.noteRecyclerView.adapter = customAdapterNote
        binding.noteRecyclerView.addItemDecoration(MyDecoration(binding.root.context))

        return binding.root
    }

    fun refreshRecyclerViewAdd(dataNote: DataNote) {
        dataList.add(0, dataNote)
        customAdapterNote.notifyDataSetChanged()
    }

    fun refreshRecyclerViewDrop(dataNote: DataNote) {
        val noteMaxLength = 8
        if(dataNote.content.length > 8) {
            val content = "${dataNote.content.subSequence(0, noteMaxLength - 2)}..."
            Snackbar.make(binding.root,"${content} 삭제하였습니다", Toast.LENGTH_SHORT).show()
        } else {
            Snackbar.make(binding.root,"${dataNote.content} 삭제하였습니다", Toast.LENGTH_SHORT).show()
        }
        dataList.remove(dataNote)
        customAdapterNote.notifyDataSetChanged()
    }

    fun sendDataToMailActivity(dateTime: String, content: String, important: String) {
        val intent = Intent(noteActivity, MailActivity::class.java)
        val noteParcel = NoteParcel(dateTime, content, important)
        val id = noteActivity.id
        val name = noteActivity.name
        val sendParcel = SendParcel(id, name, "1")
        intent.putExtra("sendParcel", sendParcel)

        val image = noteActivity.image
        val stream = ByteArrayOutputStream()
        image!!.compress(Bitmap.CompressFormat.JPEG, 50, stream)
        val byteArray: ByteArray = stream.toByteArray()
        intent.putExtra("profileImage", byteArray)

        intent.putExtra("noteParcel", noteParcel)
        startActivity(intent)
        System.exit(0)
    }
}