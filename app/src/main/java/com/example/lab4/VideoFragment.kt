package com.example.lab4

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.lab4.databinding.FragmentVideoBinding
import java.io.File


class VideoFragment : Fragment() {

    lateinit var binding: FragmentVideoBinding
    private var videoPaths: ArrayList<String> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoBinding.inflate(inflater)

        displayVideo()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val listView = binding.listViewVideo
        
        listView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(context, VideoActivity::class.java)
            intent.putExtra("path", videoPaths[position])
            startActivity(intent)
        }
    }

    private fun findVideo(rootPath: String?): ArrayList<HashMap<String, String>>? {
        val fileList: ArrayList<HashMap<String, String>> = ArrayList()
        return try {
            val rootFolder = File(rootPath)
            val files =
                rootFolder.listFiles()
            for (file in files) {
                if (file.isDirectory) {
                    if (findVideo(file.absolutePath) != null) {
                        fileList.addAll(findVideo(file.absolutePath)!!)
                    } else {
                        break
                    }
                } else if (file.name.endsWith(".mp4")) {
                    val video: HashMap<String, String> = HashMap()
                    video["file_path"] = file.absolutePath
                    video["file_name"] = file.name
                    fileList.add(video)
                    videoPaths.add(file.absolutePath)
                }
            }
            fileList
        } catch (e: Exception) {
            null
        }
    }

    private fun displayVideo(){
        val videoList = findVideo(Environment.getExternalStorageDirectory().absolutePath)
        val videoNames : ArrayList<String> = arrayListOf()
        if (videoList != null) {
            for (i in 0 until videoList.size) {
                val fileName = videoList[i]["file_name"]
                if (fileName != null) {
                    videoNames.add(fileName)
                }
            }
        }
        val arrayAdapter: ArrayAdapter<*> = context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_list_item_1, videoNames)
        }!!
        binding.listViewVideo.adapter = arrayAdapter
    }

    companion object {

        @JvmStatic
        fun newInstance() = VideoFragment()
    }
}