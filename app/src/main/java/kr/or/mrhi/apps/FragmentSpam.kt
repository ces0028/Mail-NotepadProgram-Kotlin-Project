package kr.or.mrhi.apps

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kr.or.mrhi.apps.databinding.FragmentSpamBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FragmentSpam : Fragment() {
    lateinit var binding : FragmentSpamBinding
    lateinit var customAdapterSpam: CustomAdapterSpam
    var dataList = mutableListOf<DataSpam>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSpamBinding.inflate(inflater, container, false)
        val today = LocalDate.now()
        for(i in 20 downTo 1) {
            dataList.add(DataSpam("spam" + i + "@gmail.com", "[광고] subjectSpam" + i, "contentSpam" + i, "${today.format(DateTimeFormatter.ofPattern("MM월 dd일"))}", R.drawable.img_profile_08))
        }
        val linearLayoutManager = LinearLayoutManager(container?.context)
        customAdapterSpam = CustomAdapterSpam(dataList)
        binding.spamRecyclerView.layoutManager = linearLayoutManager
        binding.spamRecyclerView.adapter = customAdapterSpam
        binding.spamRecyclerView.addItemDecoration(MyDecoration(binding.root.context))

        return binding.root
    }

    fun refreshRecyclerViewDrop(dataSpam: DataSpam) {
        Snackbar.make(binding.root, "${dataSpam.subject} 삭제하였습니다", Toast.LENGTH_SHORT).show()
        dataList.remove(dataSpam)
        customAdapterSpam.notifyDataSetChanged()
    }
}