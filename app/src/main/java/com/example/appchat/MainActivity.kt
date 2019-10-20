package com.example.appchat

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.appchat.Register_Login.Login
import com.example.appchat.Register_Login.Register
import kotlinx.android.synthetic.main.activity_main.*




class MainActivity : AppCompatActivity() {
    companion object{
        lateinit var contextOfApplication: Context
//        fun getContextOfApplication(): Context {
//            return contextOfApplication
//        }
    }
    var dem=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        contextOfApplication = applicationContext
        peform()
    }

    private fun peform() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.frmMain, Login())
            .commit()
        messagebox.setText("Đăng kí tài khoản")

        messagebox.setOnClickListener {
            if(dem%2==0) {
                showFragment(Register())
                messagebox.setText("Bạn đã có tài khoản! Hãy đăng nhập")
                dem++
            }
            else{
                showFragment(Login())
                messagebox.setText("Đăng kí tài khoản")
                dem++
            }
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frmMain, fragment)
            .commit()
    }

}