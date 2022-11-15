package kr.or.mrhi.apps

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.or.mrhi.apps.databinding.CustomDialogShowEmailBinding
import kr.or.mrhi.apps.databinding.ItemEmailBinding

class CustomAdapterSpam(val dataList: MutableList<DataSpam>) : RecyclerView.Adapter<CustomAdapterSpam.CustomViewHolder>() {
    lateinit var customDialog : AlertDialog

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding = ItemEmailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val customViewHolder = CustomViewHolder(binding)

        customViewHolder.itemView.setOnClickListener{
            val showEmailBinding = CustomDialogShowEmailBinding.inflate(LayoutInflater.from(parent.context))
            val builder = AlertDialog.Builder(parent.context)
            val itemPostion = customViewHolder.adapterPosition
            val dataSend = dataList.get(itemPostion)
            showEmailBinding.tvFromAddress.setText(dataSend.address)
            showEmailBinding.tvToAddress.setText(R.string.myEmail)
            showEmailBinding.tvSubject.setText(dataSend.subject)
            showEmailBinding.tvContent.setText(dataSend.content)
            showEmailBinding.tvDateWhen.setText("2022년 ${dataSend.date}")
            builder.setView(showEmailBinding.root)
            customDialog = builder.create()
            customDialog.setCanceledOnTouchOutside(false)
            customDialog.setCancelable(false)
            customDialog.show()

            showEmailBinding.ivClose.setOnClickListener {
                customDialog.dismiss()
            }
            showEmailBinding.btnDelete.setOnClickListener {
                val position: Int = customViewHolder.bindingAdapterPosition
                val dataSpam = dataList.get(position)
                (parent.context as MailActivity).fragmentSpam.refreshRecyclerViewDrop(dataSpam)
                customDialog.dismiss()
            }
        }
        return customViewHolder
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val binding = (holder as CustomViewHolder).binding
        val dataSpam = dataList.get(position)
        binding.ivImage.setImageResource(dataSpam.image)
        binding.tvFrom.setText(dataSpam.address)
        binding.tvSubject.setText(dataSpam.subject)
        binding.tvContent.setText(dataSpam.content)
        binding.tvDate.setText(dataSpam.date)
    }

    class CustomViewHolder(val binding: ItemEmailBinding) : RecyclerView.ViewHolder(binding.root)
}