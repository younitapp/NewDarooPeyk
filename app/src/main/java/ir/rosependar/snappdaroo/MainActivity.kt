package ir.rosependar.snappdaroo


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.bottomnavigation.BottomNavigationView
import ir.rosependar.snappdaroo.ui.login.LoginActivity
import ir.rosependar.snappdaroo.utils.Constants
import ir.rosependar.snappdaroo.utils.Prefs
import ir.rosependar.snappdaroo.utils.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var currentNavController: LiveData<NavController>? = null
    var paidOrderId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Prefs.getInstance()!!.getToken().isEmpty()) {
            Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(this)
            }
        }
        Glide.with(this).asBitmap().load(Constants.FILE_URL + Prefs.getInstance()!!.getBackground())
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {

                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    try {
                        setContentView(R.layout.activity_main)
                        lyt_main.setBackgroundResource(R.drawable.background_default)
                        initMainActivity(savedInstanceState)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    try {
                        setContentView(R.layout.activity_main)
                        lyt_main.background = BitmapDrawable(resources, resource)
                        initMainActivity(savedInstanceState)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })


    }


    fun initMainActivity(savedInstanceState: Bundle?) {
        if (intent.getStringExtra("paid_id")!!.isNotEmpty()) {
            paidOrderId = intent.getStringExtra("paid_id")!!
        }
        if (intent.getStringExtra("order_id")!!.isNotEmpty()) {
            paidOrderId = intent.getStringExtra("order_id")!!
        }


        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }

    }

    fun setMainBackground(image_url: String) {

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)

        val navGraphIds =
            listOf(R.navigation.nav_home, R.navigation.nav_profile, R.navigation.nav_orders)

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, Observer { navController ->

        })
        currentNavController = controller
        val view: View = bottomNavigationView.findViewById(R.id.nav_home)
        view.performClick()
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

}