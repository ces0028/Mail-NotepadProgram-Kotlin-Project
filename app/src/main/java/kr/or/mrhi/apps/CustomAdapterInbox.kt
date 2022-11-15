package kr.or.mrhi.apps

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.or.mrhi.apps.databinding.CustomDialogShowEmailBinding
import kr.or.mrhi.apps.databinding.ItemEmailBinding

class CustomAdapterInbox(val dataList: MutableList<DataInbox>) : RecyclerView.Adapter<CustomAdapterInbox.CustomViewHolder>() {
    lateinit var customDialog : AlertDialog

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding = ItemEmailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val customViewHolder = CustomViewHolder(binding)

        customViewHolder.itemView.setOnClickListener {
            val showEmailBinding = CustomDialogShowEmailBinding.inflate(LayoutInflater.from(parent.context))
            val builder = AlertDialog.Builder(parent.context)
            val itemPostion = customViewHolder.adapterPosition
            val dataInbox = dataList.get(itemPostion)
            showEmailBinding.tvFromAddress.setText(dataInbox.address)
            showEmailBinding.tvToAddress.setText(R.string.myEmail)
            showEmailBinding.tvSubject.setText(dataInbox.subject)
            showEmailBinding.tvContent.setText(dataInbox.content)
            showEmailBinding.tvDateWhen.setText("2022년 ${dataInbox.date}")
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
                (parent.context as MailActivity).fragmentInbox.refreshRecyclerViewDrop(dataInbox)
                customDialog.dismiss()
            }
        }
        return customViewHolder
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val binding = (holder as CustomViewHolder).binding
        val dataInbox = dataList.get(position)
        binding.ivImage.setImageResource(dataInbox.image)
        binding.tvFrom.setText(dataInbox.address)
        binding.tvSubject.setText(dataInbox.subject)
        binding.tvContent.setText(dataInbox.content)
        binding.tvDate.setText(dataInbox.date)
    }

    class CustomViewHolder(val binding: ItemEmailBinding) : RecyclerView.ViewHolder(binding.root)
}