package com.example.lab4

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class PageAdapter(fm : FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int {
        return 2;
    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> {
                AudioFragment()
            }
            1 -> {
                VideoFragment()
            }
            else -> {
                AudioFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> {
                "Audio"
            }
            1 -> {
                "Video"
            }
            else -> {
                "Audio"
            }
        }
    }

}