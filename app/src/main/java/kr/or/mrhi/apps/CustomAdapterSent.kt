package kr.or.mrhi.apps

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.or.mrhi.apps.databinding.CustomDialogShowEmailBinding
import kr.or.mrhi.apps.databinding.ItemEmailBinding

class CustomAdapterSent(val dataList: MutableList<DataSent>) : RecyclerView.Adapter<CustomAdapterSent.CustomViewHolder>() {
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
            val itemPosition = customViewHolder.adapterPosition
            val dataSent = dataList.get(itemPosition)
            showEmailBinding.tvFromAddress.setText(R.string.myEmail)
            showEmailBinding.tvToAddress.setText(dataSent.address)
            showEmailBinding.tvSubject.setText(dataSent.subject)
            showEmailBinding.tvContent.setText(dataSent.content)
            showEmailBinding.tvDateWhen.setText("2022년 ${dataSent.date}")
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
                (parent.context as MailActivity).fragmentSent.refreshRecyclerViewDrop(dataSent)
                customDialog.dismiss()
            }
        }
        return customViewHolder
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val binding = (holder as CustomViewHolder).binding
        val dataSent = dataList.get(position)
        binding.ivImage.setImageResource(dataSent.image)
        binding.tvFrom.setText(dataSent.address)
        binding.tvSubject.setText(dataSent.subject)
        binding.tvContent.setText(dataSent.content)
        binding.tvDate.setText(dataSent.date)
    }

    class CustomViewHolder(val binding: ItemEmailBinding) : RecyclerView.ViewHolder(binding.root)
}