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
import com.example.lab4.databinding.FragmentAudioBinding
import java.io.File


class AudioFragment : Fragment() {
    lateinit var binding : FragmentAudioBinding
    private var audioPaths: ArrayList<String> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAudioBinding.inflate(inflater)

        displayAudio()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val listView = binding.listViewAudio

        listView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(context, AudioActivity::class.java)
            intent.putExtra("pos", position)
            intent.putExtra("paths", audioPaths)
            startActivity(intent)
        }
    }

    private fun findAudio(rootPath: String?): ArrayList<HashMap<String, String>>? {
        val fileList: ArrayList<HashMap<String, String>> = ArrayList()
        return try {
            val rootFolder = File(rootPath)
            val files =
                rootFolder.listFiles()
            for (file in files) {
                if (file.isDirectory) {
                    if (findAudio(file.absolutePath) != null) {
                        fileList.addAll(findAudio(file.absolutePath)!!)
                    } else {
                        break
                    }
                } else if (file.name.endsWith(".mp3")) {
                    val song: HashMap<String, String> = HashMap()
                    song["file_path"] = file.absolutePath
                    song["file_name"] = file.name
                    fileList.add(song)
                    audioPaths.add(file.absolutePath)
                }
            }
            fileList
        } catch (e: Exception) {
            null
        }
    }

    private fun displayAudio(){
        val audioList = findAudio(Environment.getExternalStorageDirectory().absolutePath)
        val audioNames : ArrayList<String> = arrayListOf()
        if (audioList != null) {
            for (i in 0 until audioList.size) {
                val fileName = audioList[i]["file_name"]
                if (fileName != null) {
                    audioNames.add(fileName)
                }
                Log.e("file details ", " name =$fileName")
            }
        }
        val arrayAdapter: ArrayAdapter<*> = context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_list_item_1, audioNames)
        }!!
        binding.listViewAudio.adapter = arrayAdapter
    }

    companion object {

        @JvmStatic
        fun newInstance() = AudioFragment()
    }
}