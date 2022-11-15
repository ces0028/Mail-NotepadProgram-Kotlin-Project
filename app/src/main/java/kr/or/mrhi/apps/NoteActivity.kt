package kr.or.mrhi.apps

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kr.or.mrhi.apps.databinding.ActivityMailBinding
import kr.or.mrhi.apps.databinding.ActivityNoteBinding
import java.io.ByteArrayOutputStream

class NoteActivity : AppCompatActivity() {
    lateinit var toggle : ActionBarDrawerToggle
    lateinit var binding : ActivityNoteBinding
    lateinit var fragmentNote: FragmentNote
    lateinit var id: String
    lateinit var name: String
    var image: Bitmap? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.drawerOpen, R.string.drawerClose)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toggle.syncState()

        val pagerAdapter = PagerAdapterNote(this@NoteActivity)
        val titleList = mutableListOf<String>("NOTE")
        fragmentNote = FragmentNote()

        pagerAdapter.addFragment(fragmentNote, titleList[0])

        binding.noteViewpager.adapter = pagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.noteViewpager){ tab, position ->
            tab.text = titleList.get(position)
        }.attach()

        val navigationView: NavigationView = findViewById(R.id.navigationView)
        val header: View = navigationView.getHeaderView(0)

        if(intent.hasExtra("profileImage")) {
            val byteArray = intent.getByteArrayExtra("profileImage")
            image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
            val ivHeaderImage: ImageView = header.findViewById(R.id.ivHeaderImage)
            ivHeaderImage.setImageBitmap(image)
        }

        if(intent.hasExtra("sendParcel")) {
            val sendParcel = intent.getParcelableExtra<SendParcel>("sendParcel")
            if(sendParcel?.send == "snackbar") {
                Snackbar.make(binding.root, "메모장을 누르셨습니다", Toast.LENGTH_SHORT).show()
            }
            id = sendParcel!!.email
            name = sendParcel!!.profileName
            val tvHeaderAddress: TextView = header.findViewById(R.id.tvHeaderAddress)
            val tvHeaderName: TextView = header.findViewById(R.id.tvHeaderName)
            tvHeaderAddress.setText(id)
            tvHeaderName.setText(name)
        }

        binding.noteEfab.setOnClickListener {
            when(binding.noteEfab.isExtended) {
                true -> binding.noteEfab.shrink()
                false -> binding.noteEfab.extend()
            }
            val dialog = CustomDialogNote(binding.root.context)
            dialog.showDialog()
        }

        binding.navigationView.setNavigationItemSelectedListener {
            val mailBinding = ActivityMailBinding.inflate(layoutInflater)
            when(it.itemId) {
                R.id.menuMailInbox -> intentToMailActivity("0")
                R.id.menuMailSent -> intentToMailActivity("1")
                R.id.menuMailSpam -> intentToMailActivity("2")
                R.id.menuNote -> {
                    Snackbar.make(binding.root, "메모장을 누르셨습니다", Toast.LENGTH_SHORT).show()
                    mailBinding.drawerLayout.closeDrawer(GravityCompat.START)
                }
            }
            true
        }
    }

    fun intentToMailActivity(where: String) {
        val intent = Intent(this, MailActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val sendParcel = SendParcel(id, name, where)
        intent.putExtra("sendParcel", sendParcel)

        val stream = ByteArrayOutputStream()
        image!!.compress(Bitmap.CompressFormat.JPEG, 50, stream)
        val byteArray: ByteArray = stream.toByteArray()
        intent.putExtra("profileImage", byteArray)

        startActivity(intent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawers()
        }else{
            val eventHandler = object: DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, answer: Int) {
                    if(answer == DialogInterface.BUTTON_POSITIVE){
                        System.exit(0)
                    }
                }
            }
            AlertDialog.Builder(this).run {
                setMessage("프로그램을 종료하시겠습니까?")
                setPositiveButton("종료", eventHandler)
                setNegativeButton("닫기", null)
                show()
            }
        }
    }
}