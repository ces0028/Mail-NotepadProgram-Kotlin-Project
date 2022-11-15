package kr.or.mrhi.apps

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kr.or.mrhi.apps.databinding.ActivityMailBinding
import kr.or.mrhi.apps.databinding.CustomDialogModifyNameBinding
import kr.or.mrhi.apps.databinding.CustomDialogModifyProfileBinding
import java.io.ByteArrayOutputStream

class MailActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var binding: ActivityMailBinding
    lateinit var fragmentInbox: FragmentInbox
    lateinit var fragmentSent: FragmentSent
    lateinit var fragmentSpam: FragmentSpam
    lateinit var id: String
    lateinit var name: String
    lateinit var image: Any
    lateinit var dialogBinding: CustomDialogModifyProfileBinding
    var newBitmap: Bitmap? = null
    var requestGalleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){}

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.drawerOpen, R.string.drawerClose)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toggle.syncState()

        val pagerAdapter = PagerAdapterMail(this)
        val titleList = mutableListOf<String>("INBOX", "SENT", "SPAM")
        fragmentInbox = FragmentInbox()
        fragmentSent = FragmentSent()
        fragmentSpam = FragmentSpam()

        pagerAdapter.addFragment(fragmentInbox, titleList[0])
        pagerAdapter.addFragment(fragmentSent, titleList[1])
        pagerAdapter.addFragment(fragmentSpam, titleList[2])

        binding.viewpager.adapter = pagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewpager){ tab, position ->
            tab.text = titleList.get(position)
        }.attach()

        val navigationView: NavigationView = findViewById(R.id.navigationView)
        val header: View = navigationView.getHeaderView(0)

        if(intent.hasExtra("profileImage")) {
            val byteArray = intent.getByteArrayExtra("profileImage")
            newBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
            val ivHeaderImage: ImageView = header.findViewById(R.id.ivHeaderImage)
            ivHeaderImage.setImageBitmap(newBitmap)
        }

        if(intent.hasExtra("sendParcel")) {
            val sendParcel = intent.getParcelableExtra<SendParcel>("sendParcel")
            when(sendParcel!!.send) {
                "0" -> {
                    binding.viewpager.currentItem = 0
                    Snackbar.make(binding.root, "받은메일함을 누르셨습니다", Toast.LENGTH_SHORT).show()
                }
                "1" -> {
                    binding.viewpager.currentItem = 1
                    Snackbar.make(binding.root, "보낸메일함을 누르셨습니다", Toast.LENGTH_SHORT).show()
                }
                "2" -> {
                    binding.viewpager.currentItem = 2
                    Snackbar.make(binding.root, "스팸메일함을 누르셨습니다", Toast.LENGTH_SHORT).show()
                }
            }
            id = sendParcel!!.email
            name = sendParcel!!.profileName
            val tvHeaderAddress: TextView = header.findViewById(R.id.tvHeaderAddress)
            val tvHeaderName: TextView = header.findViewById(R.id.tvHeaderName)
            tvHeaderAddress.setText(id)
            tvHeaderName.setText(name)
        }

        if(intent.hasExtra("noteParcel")) {
            val noteParcel = intent.getParcelableExtra<NoteParcel>("noteParcel")
            val dialog = CustomDialogSend(binding.root.context)
            dialog.showDialog("${noteParcel?.dateTime}", "${noteParcel?.important}${noteParcel?.content}")
        }

        if(intent.hasExtra("id")) {
            id = intent.getStringExtra("id").toString()
            name = id.substring(0, id.indexOf("@"))
            val tvHeaderAddress: TextView = header.findViewById(R.id.tvHeaderAddress)
            val tvHeaderName: TextView = header.findViewById(R.id.tvHeaderName)
            tvHeaderAddress.setText(id)
            tvHeaderName.setText(name)
        }

        binding.mainEfab.setOnClickListener {
            when(binding.mainEfab.isExtended) {
                true -> binding.mainEfab.shrink()
                false -> binding.mainEfab.extend()
            }
            val dialog = CustomDialogSend(binding.root.context)
            dialog.showDialog("", "")
        }

        binding.navigationView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.menuMailInbox -> {
                    Snackbar.make(binding.root, "받은메일함을 누르셨습니다", Toast.LENGTH_SHORT).show()
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    binding.viewpager.currentItem = 0
                }
                R.id.menuMailSent -> {
                    Snackbar.make(binding.root, "보낸메일함을 누르셨습니다", Toast.LENGTH_SHORT).show()
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    binding.viewpager.currentItem = 1
                }
                R.id.menuMailSpam -> {
                    Snackbar.make(binding.root, "스팸메일함을 누르셨습니다", Toast.LENGTH_SHORT).show()
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    binding.viewpager.currentItem = 2
                }
                R.id.menuNote -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    val intent = Intent(this, NoteActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

                    val sendParcel = SendParcel(id, name, "snackbar")
                    intent.putExtra("sendParcel", sendParcel)

                    val stream = ByteArrayOutputStream()
                    if(newBitmap == null) {
                        newBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_main)
                    }
                    newBitmap!!.compress(Bitmap.CompressFormat.JPEG, 50, stream)
                    val byteArray: ByteArray = stream.toByteArray()
                    intent.putExtra("profileImage", byteArray)

                    startActivity(intent)
                    finish()
                }
            }
            true
        }

        requestGalleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val calRatio = calculateInSampleSize(
                it.data!!.data!!,
                resources.getDimensionPixelSize(R.dimen.imageSize),
                resources.getDimensionPixelSize(R.dimen.imageSize)
            )
            val options = BitmapFactory.Options()
            options.inSampleSize = calRatio
            try {
                val inputStream = contentResolver.openInputStream(it.data!!.data!!)
                val bitmap = BitmapFactory.decodeStream(inputStream, null, options)
                val orientation = getOrientationOfImage(it.data!!.data!!).toFloat()
                newBitmap = getRotatedBitmap(bitmap, orientation)
                val ivHeaderImage: ImageView = header.findViewById(R.id.ivHeaderImage)
                ivHeaderImage.setImageBitmap(newBitmap)
            } catch (e: Exception) {
                Log.d("kr.or.mrhi.apps", "${e.printStackTrace()}")
            }
        }
    }

    fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int{
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true

        try {
            var inputStream = contentResolver.openInputStream(fileUri)
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream?.close()
            inputStream = null
        } catch (e: Exception) {
            Log.d("kr.or.mrhi.apps", "${e.printStackTrace()}")
        }
        val weight = options.outWidth
        val height = options.outHeight
        var insampleSize = 1

        if(weight > reqWidth || height > reqHeight) {
            val halfWidth = weight / 2
            val halfHeight = height / 2
            while(halfWidth/insampleSize >= reqWidth && halfHeight/insampleSize >= reqHeight) {
                insampleSize *= 2
            }
        }
        return insampleSize
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getOrientationOfImage(fileUri: Uri): Int {
        val inputStream = contentResolver.openInputStream(fileUri)
        val exif: ExifInterface? = try {
            ExifInterface(inputStream!!)
        } catch (e: Exception) {
            e.printStackTrace()
            return -1
        }
        inputStream.close()

        val orientation = exif?.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        if (orientation != -1) {
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> return 90
                ExifInterface.ORIENTATION_ROTATE_180 -> return 180
                ExifInterface.ORIENTATION_ROTATE_270 -> return 270
            }
        }
        return 0
    }

    fun getRotatedBitmap(bitmap: Bitmap?, degrees: Float): Bitmap? {
        if (bitmap == null) return null
        if (degrees == 0F) return bitmap
        val matrix = Matrix()
        matrix.setRotate(degrees, bitmap.width.toFloat() / 2, bitmap.height.toFloat() / 2)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_overflow, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menuLogout -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.menuCustomiseProfile -> {
                dialogBinding = CustomDialogModifyProfileBinding.inflate(layoutInflater)
                val builder = android.app.AlertDialog.Builder(this)
                builder.setView(dialogBinding.root)
                val customDialog = builder.create()
                customDialog.setCanceledOnTouchOutside(false)
                customDialog.setCancelable(false)
                customDialog.show()

                val navigationView: NavigationView = findViewById(R.id.navigationView)
                val header: View = navigationView.getHeaderView(0)

                dialogBinding.btnModifyName.setOnClickListener {
                    val dialogModifyNameBinding = CustomDialogModifyNameBinding.inflate(layoutInflater)
                    builder.setView(dialogModifyNameBinding.root)
                    val customDialogInDialog = builder.create()
                    customDialogInDialog.setCanceledOnTouchOutside(false)
                    customDialogInDialog.setCancelable(false)
                    customDialogInDialog.show()

                    dialogModifyNameBinding.btnSave.setOnClickListener {
                        val tvHeaderName: TextView = header.findViewById(R.id.tvHeaderName)
                        val profileName = dialogModifyNameBinding.edtProfileName.text.toString()
                        tvHeaderName.text = profileName
                        name = profileName
                        customDialogInDialog.dismiss()
                    }
                    dialogModifyNameBinding.ivClose.setOnClickListener {
                        customDialogInDialog.dismiss()
                    }
                }

                dialogBinding.btnModifyImage.setOnClickListener {
                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    intent.type = "image/*"
                    requestGalleryLauncher.launch(intent)
                }

                dialogBinding.ivClose.setOnClickListener {
                    customDialog.dismiss()
                }
            }
        }

        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawers()
        } else {
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