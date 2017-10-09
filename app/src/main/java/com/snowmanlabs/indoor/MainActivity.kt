package com.snowmanlabs.indoor

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import com.snowmanlabs.com.R

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var mContent:Fragment? = null

    private val  MAP_FRAGMENT = "mapFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        if (savedInstanceState != null) {
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, MAP_FRAGMENT)
        }else{
            mContent = MapFragment.newInstance()
        }

        val ft = getFragmentTransaction()

        ft.replace(container.id,mContent, MapFragment::class.java.simpleName)
        ft.commit()

    }

    private fun getFragmentTransaction(): FragmentTransaction {
        return supportFragmentManager.beginTransaction()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        supportFragmentManager.putFragment(outState, MAP_FRAGMENT, mContent)
    }

}
