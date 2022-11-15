package kr.or.mrhi.apps

import android.app.AlertDialog
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import kr.or.mrhi.apps.databinding.CustomDialogNoteBinding
import kr.or.mrhi.apps.databinding.CustomDialogShowNoteBinding
import kr.or.mrhi.apps.databinding.ItemNoteBinding

class CustomAdapterNote(val dataList: MutableList<DataNote>) : RecyclerView.Adapter<CustomAdapterNote.CustomViewHolder>() {
    lateinit var dialog : AlertDialog
    lateinit var customDialog : AlertDialog
    lateinit var date: String
    lateinit var content: String
    var image = R.drawable.ic_star_blank
    var click = false

    override fun getItemCount(): Int {
        return dataList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val customViewHolder = CustomViewHolder(binding)

        customViewHolder.itemView.setOnClickListener{
            val showNoteBinding = CustomDialogShowNoteBinding.inflate(LayoutInflater.from(parent.context))
            val builderShow = AlertDialog.Builder(parent.context)
            val itemPosition = customViewHolder.adapterPosition
            var dataNote = dataList.get(itemPosition)
            showNoteBinding.tvDateTime.setText(dataNote.dateTime)
            showNoteBinding.tvContent.setText(dataNote.content)
            showNoteBinding.ivStar.setImageResource(dataNote.image)
            click = dataNote.image == R.drawable.ic_star_full
            builderShow.setView(showNoteBinding.root)
            dialog = builderShow.create()
            dialog.setCanceledOnTouchOutside(false)
            dialog.setCancelable(false)
            dialog.show()

            showNoteBinding.ivClose.setOnClickListener {
                if(click) {
                    image = R.drawable.ic_star_full
                } else {
                    if(dataNote.image == R.drawable.ic_star_full) {
                        image = R.drawable.ic_star_full
                    }
                    image = R.drawable.ic_star_blank
                }
                dataNote = DataNote(showNoteBinding.tvDateTime.text.toString(), showNoteBinding.tvContent.text.toString(), image)
                dataList.set(itemPosition, dataNote)
                notifyItemChanged(itemPosition)
                click = false
                dialog.dismiss()
            }

            showNoteBinding.btnDelete.setOnClickListener {
                (parent.context as NoteActivity).fragmentNote.refreshRecyclerViewDrop(dataNote)
                dialog.dismiss()
            }

            showNoteBinding.btnRewrite.setOnClickListener {
                val customDialogNoteBinding = CustomDialogNoteBinding.inflate(LayoutInflater.from(parent.context))
                val builderRewrite = AlertDialog.Builder(parent.context)
                customDialogNoteBinding.edtContent.setText(dataNote.content)
                builderRewrite.setView(customDialogNoteBinding.root)
                customDialog = builderRewrite.create()
                customDialog.show()

                customDialogNoteBinding.ivClose.setOnClickListener {
                    customDialogNoteBinding.edtContent.setText(binding.tvContent.text.toString())
                    customDialog.dismiss()
                }

                customDialogNoteBinding.btnSave.setOnClickListener {
                    date = binding.tvDateTime.text.toString()
                    content = customDialogNoteBinding.edtContent.text.toString()
                    showNoteBinding.ivStar.setImageResource(image)
                    dataNote = DataNote(date, content, image)
                    dataList.set(itemPosition, dataNote)
                    notifyItemChanged(itemPosition)
                    showNoteBinding.tvContent.setText(content)
                    customDialog.dismiss()
                }
            }

            showNoteBinding.ivShare.setOnClickListener {
                val dateTime = dataNote.dateTime
                var important = ""
                content = dataNote.content
                if(click) {
                    important = "[중요] "
                }
                (parent.context as NoteActivity).fragmentNote.sendDataToMailActivity(dateTime, content, important)
            }

            showNoteBinding.ivStar.setOnClickListener {
                if(click) {
                    showNoteBinding.ivStar.setImageResource(R.drawable.ic_star_blank)
                    click = false
                } else {
                    showNoteBinding.ivStar.setImageResource(R.drawable.ic_star_full)
                    click = true
                }
            }
        }
        return customViewHolder
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val binding = (holder as CustomViewHolder).binding
        val dataNote = dataList.get(position)
        binding.tvDateTime.setText(dataNote.dateTime)
        binding.tvContent.setText(dataNote.content)
        binding.ivStar.setImageResource(dataNote.image)
    }

    class CustomViewHolder(val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root)
    }