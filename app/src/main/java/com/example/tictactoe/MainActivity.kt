package com.example.tictactoe

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.PowerManager
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        doBindService()
        val music = Intent()
        music.setClass(this, MusicService::class.java)
        startService(music)
        setContentView(R.layout.activity_main)
    }

    fun btnClick(view: View)
    {
        val buSelected: Button = view as Button
        var cellId = 0
        when(buSelected.id)
        {
            R.id.bu1 -> cellId = 1
            R.id.bu2 -> cellId = 2
            R.id.bu3 -> cellId = 3
            R.id.bu4 -> cellId = 4
            R.id.bu5 -> cellId = 5
            R.id.bu6 -> cellId = 6
            R.id.bu7 -> cellId = 7
            R.id.bu8 -> cellId = 8
            R.id.bu9 -> cellId = 9
        }
//        Toast.makeText(this,"cellId"+cellId,Toast.LENGTH_LONG).show()
        playGame(cellId,buSelected)
    }
    //  create array list to keep track of player one hits cellid
    val player1 : ArrayList<Int> = ArrayList()
    val player2 : ArrayList<Int> = ArrayList()
    var activeplayer = 1//player 1 is "x" and player 2 is "0"
    fun playGame(cellId: Int, buSelected: Button) {
        if (activeplayer == 1)
        {
            buSelected.setText("X")
            buSelected.setBackgroundColor(Color.GREEN)
            player1.add(cellId)
            activeplayer = 2
        }
        else
        {
            buSelected.setText("O")
            buSelected.setBackgroundColor(Color.RED)
            player2.add(cellId)
            activeplayer = 1
        }
        buSelected.isEnabled = false
        checkWinner()
    }
    private fun checkWinner()
    {
        // R
        if(player1.contains(1) && player1.contains(2) && player1.contains(3))
            Toast.makeText(this,"Player1 is Winner",Toast.LENGTH_LONG).show()
        if(player2.contains(1) && player2.contains(2) && player2.contains(3))
            Toast.makeText(this,"Player2 is Winner",Toast.LENGTH_LONG).show()
        if(player1.contains(4) && player1.contains(5) && player1.contains(6))
            Toast.makeText(this,"Player1 is Winner",Toast.LENGTH_LONG).show()
        if(player2.contains(4) && player2.contains(5) && player2.contains(6))
            Toast.makeText(this,"Player2 is Winner",Toast.LENGTH_LONG).show()
        if(player1.contains(7) && player1.contains(8) && player1.contains(9))
            Toast.makeText(this,"Player1 is Winner",Toast.LENGTH_LONG).show()
        if(player2.contains(7) && player2.contains(8) && player2.contains(9))
            Toast.makeText(this,"Player2 is Winner",Toast.LENGTH_LONG).show()
        //     C
        if(player1.contains(1) && player1.contains(4) && player1.contains(7))
            Toast.makeText(this,"Player1 is Winner",Toast.LENGTH_LONG).show()
        if(player2.contains(1) && player2.contains(4) && player2.contains(7))
            Toast.makeText(this,"Player2 is Winner",Toast.LENGTH_LONG).show()
        if(player1.contains(2) && player1.contains(5) && player1.contains(8))
            Toast.makeText(this,"Player1 is Winner",Toast.LENGTH_LONG).show()
        if(player2.contains(2) && player2.contains(5) && player2.contains(8))
            Toast.makeText(this,"Player2 is Winner",Toast.LENGTH_LONG).show()
        if(player1.contains(3) && player1.contains(6) && player1.contains(9))
            Toast.makeText(this,"Player1 is Winner",Toast.LENGTH_LONG).show()
        if(player2.contains(3) && player2.contains(6) && player2.contains(9))
            Toast.makeText(this,"Player2 is Winner",Toast.LENGTH_LONG).show()
        //DIAGONAL.
        if(player1.contains(1) && player1.contains(5) && player1.contains(9))
            Toast.makeText(this,"Player1 is Winner",Toast.LENGTH_LONG).show()
        if(player2.contains(1) && player2.contains(5) && player2.contains(9))
            Toast.makeText(this,"Player2 is Winner",Toast.LENGTH_LONG).show()
        if(player1.contains(3) && player1.contains(5) && player1.contains(7))
            Toast.makeText(this,"Player1 is Winner",Toast.LENGTH_LONG).show()
        if(player2.contains(3) && player2.contains(5) && player2.contains(9))
            Toast.makeText(this,"Player2 is Winner",Toast.LENGTH_LONG).show()

    }
    private var mIsBound = false
    private var mServ: MusicService? = null
    private val Scon: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, binder: IBinder) {
            mServ = (binder as MusicService.ServiceBinder).getService()
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mServ = null
        }
    }

    fun doBindService() {
        bindService(
            Intent(this, MusicService::class.java),
            Scon, Context.BIND_AUTO_CREATE
        )
        mIsBound = true
    }

    fun doUnbindService() {
        if (mIsBound) {
            unbindService(Scon)
            mIsBound = false
        }
    }

    override fun onResume() {
        super.onResume()
        mServ?.resumeMusic()
    }

    override fun onPause() {
        super.onPause()

        //Detect idle screen
        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        var isScreenOn = false
        isScreenOn = pm.isScreenOn
        if (!isScreenOn) {
            mServ?.pauseMusic()
        }
    }


    override fun onDestroy() {
        super.onDestroy()

        //UNBIND music service
        doUnbindService()
        val music = Intent()
        music.setClass(this, MusicService::class.java)
        stopService(music)
    }

}

