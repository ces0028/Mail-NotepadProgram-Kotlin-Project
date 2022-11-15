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
import kr.or.mrhi.apps.databinding.FragmentSentBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FragmentSent : Fragment() {
    lateinit var binding: FragmentSentBinding
    var dataList = mutableListOf<DataSent>()
    var customAdapterSent = CustomAdapterSent(dataList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSentBinding.inflate(inflater, container, false)
        val today = LocalDate.now()

        dataList.add(DataSent("zenobia@gmail.com", "subjectZenobia", "contentZenobia","${today.minusDays(18).format(DateTimeFormatter.ofPattern("MM월 dd일"))}", R.drawable.img_profile_07))
        dataList.add(DataSent("yarrow@gmail.com", "subjectYarrow", "contentYarrow","${today.minusDays(19).format(DateTimeFormatter.ofPattern("MM월 dd일"))}", R.drawable.img_profile_06))
        dataList.add(DataSent("xanthisma@gmail.com", "subjectXanthisma", "contentXanthisma","${today.minusDays(20).format(DateTimeFormatter.ofPattern("MM월 dd일"))}", R.drawable.img_profile_05))
        dataList.add(DataSent("wallflower@gmail.com", "subjectWallflower", "contentWallflower","${today.minusDays(21).format(DateTimeFormatter.ofPattern("MM월 dd일"))}", R.drawable.img_profile_04))
        dataList.add(DataSent("varcissus@gmail.com", "subjectNarcissus", "contentNarcissus","${today.minusDays(22).format(DateTimeFormatter.ofPattern("MM월 dd일"))}", R.drawable.img_profile_03))
        dataList.add(DataSent("ulex@gmail.com", "subejectUlex", "contentUlex","${today.minusDays(23).format(DateTimeFormatter.ofPattern("MM월 dd일"))}", R.drawable.img_profile_02))
        dataList.add(DataSent("trillium@gmail.com", "subjectcTrillium", "contentTrillium","${today.minusDays(24).format(DateTimeFormatter.ofPattern("MM월 dd일"))}", R.drawable.img_profile_01))
        dataList.add(DataSent("saponaria@gmail.com", "subjectSaponaria", "contentSaponaria","${today.minusDays(25).format(DateTimeFormatter.ofPattern("MM월 dd일"))}", R.drawable.img_profile_07))
        dataList.add(DataSent("rose@gmail.com", "sujectRose", "contentRose","${today.minusDays(26).format(DateTimeFormatter.ofPattern("MM월 dd일"))}", R.drawable.img_profile_06))
        dataList.add(DataSent("quesnelia@gmail.com", "subjectQuesnelia", "contentQuesnelia","${today.minusDays(27).format(DateTimeFormatter.ofPattern("MM월 dd일"))}", R.drawable.img_profile_05))
        dataList.add(DataSent("petunia@gmail.com", "subjectPetunia", "contentPetunia","${today.minusDays(28).format(DateTimeFormatter.ofPattern("MM월 dd일"))}", R.drawable.img_profile_04))
        dataList.add(DataSent("orchid@gmail.com", "subjectOrchid", "contentOrchid","${today.minusDays(29).format(DateTimeFormatter.ofPattern("MM월 dd일"))}", R.drawable.img_profile_03))
        dataList.add(DataSent("narcissus@gmail.com", "subjectNarcissus", "contentNarcissus","${today.minusDays(30).format(DateTimeFormatter.ofPattern("MM월 dd일"))}", R.drawable.img_profile_02))

        val linearLayoutManager = LinearLayoutManager(container?.context)
        binding.sentRecyclerView.layoutManager = linearLayoutManager
        binding.sentRecyclerView.adapter = customAdapterSent
        binding.sentRecyclerView.addItemDecoration(MyDecoration(binding.root.context))

        return binding.root
    }

    fun refreshRecyclerViewAdd(dataSent: DataSent) {
        dataList.add(0, dataSent)
        customAdapterSent.notifyDataSetChanged()
    }

    fun refreshRecyclerViewDrop(dataSent: DataSent) {
        Snackbar.make(binding.root,"${dataSent.subject} 삭제하였습니다", Toast.LENGTH_SHORT).show()
        dataList.remove(dataSent)
        customAdapterSent.notifyDataSetChanged()
    }
}