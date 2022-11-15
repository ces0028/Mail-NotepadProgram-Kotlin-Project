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
import kr.or.mrhi.apps.databinding.FragmentInboxBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FragmentInbox : Fragment() {
    lateinit var binding : FragmentInboxBinding
    lateinit var customAdapterInbox : CustomAdapterInbox
    var dataList = mutableListOf<DataInbox>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentInboxBinding.inflate(inflater, container, false)
        val today = LocalDate.now()
        dataList.add(DataInbox("mango@gmail.com", "subjectMango", "contentMango","${today.minusDays(0).format(DateTimeFormatter.ofPattern("MM월 dd일"))}", R.drawable.img_profile_01))
        dataList.add(DataInbox("lemon@gmail.com", "subjectLemon", "contentLemon","${today.minusDays(1).format(DateTimeFormatter.ofPattern("MM월 dd일"))}", R.drawable.img_profile_02))
        dataList.add(DataInbox("kiwi@gmail.com", "subjectKiwi", "contentKiwi","${today.minusDays(2).format(DateTimeFormatter.ofPattern("MM월 dd일"))}", R.drawable.img_profile_03))
        dataList.add(DataInbox("jackfruit@gmail.com", "subjectJackfruit", "contentJackfruit","${today.minusDays(3).format(DateTimeFormatter.ofPattern("MM월 dd일"))}", R.drawable.img_profile_04))
        dataList.add(DataInbox("icaco@gmail.com", "subjectIcaco", "contentIcaco","${today.minusDays(4).format(DateTimeFormatter.ofPattern("MM월 dd일"))}", R.drawable.img_profile_05))
        dataList.add(DataInbox("huckleberry@gmail.com", "subjectHuckleberry", "contentHuckleberry","${today.minusDays(5).format(DateTimeFormatter.ofPattern("MM월 dd일"))}", R.drawable.img_profile_06))
        dataList.add(DataInbox("grape@gmail.com", "subjectGrape", "contentGrape","${today.minusDays(6).format(DateTimeFormatter.ofPattern("MM월 dd일"))}", R.drawable.img_profile_07))
        dataList.add(DataInbox("fennel@gmail.com", "subjectFennel", "contentFennel","${today.minusDays(7).format(DateTimeFormatter.ofPattern("MM월 dd일"))}", R.drawable.img_profile_01))
        dataList.add(DataInbox("eggplant@gmail.com", "subejectEggplant", "contentEggplant","${today.minusDays(8).format(DateTimeFormatter.ofPattern("MM월 dd일"))}", R.drawable.img_profile_02))
        dataList.add(DataInbox("dewberry@gmail.com", "subjectDewberry", "contentDewberry","${today.minusDays(9).format(DateTimeFormatter.ofPattern("MM월 dd일"))}", R.drawable.img_profile_03))
        dataList.add(DataInbox("cacao@gmail.com", "subjectCacao", "contentCacao","${today.minusDays(10).format(DateTimeFormatter.ofPattern("MM월 dd일"))}", R.drawable.img_profile_04))
        dataList.add(DataInbox("banana@gmail.com", "sujectBanana", "contentBanana","${today.minusDays(11).format(DateTimeFormatter.ofPattern("MM월 dd일"))}", R.drawable.img_profile_05))
        dataList.add(DataInbox("apple@gmail.com", "subjectApple", "contentApple","${today.minusDays(12).format(DateTimeFormatter.ofPattern("MM월 dd일"))}", R.drawable.img_profile_06))

        val linearLayoutManager = LinearLayoutManager(container?.context)
        customAdapterInbox = CustomAdapterInbox(dataList)
        binding.inboxRecyclerView.layoutManager = linearLayoutManager
        binding.inboxRecyclerView.adapter = customAdapterInbox
        binding.inboxRecyclerView.addItemDecoration(MyDecoration(binding.root.context))

        return binding.root
    }
    fun refreshRecyclerViewDrop(dataReceive: DataInbox) {
        Snackbar.make(binding.root,"${dataReceive.subject} 삭제하였습니다", Toast.LENGTH_SHORT).show()
        dataList.remove(dataReceive)
        customAdapterInbox.notifyDataSetChanged()
    }
}