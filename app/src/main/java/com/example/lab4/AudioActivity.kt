package com.example.lab4

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.SeekBar
import com.example.lab4.databinding.ActivityAudioBinding

class AudioActivity : AppCompatActivity() {
    lateinit var binding: ActivityAudioBinding
    var mediaPlayer = MediaPlayer()
    private var paths = arrayListOf<String>()
    private var pos  = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        paths = intent.getStringArrayListExtra("paths") as ArrayList<String>
        pos = intent.getIntExtra("pos", 0)

        val path = paths[pos]

        Log.e("path", path)

        mediaPlayer.setDataSource(path)
        mediaPlayer.prepare()
        mediaPlayer.start()

        musicControl()
        initialiseSeekBar()
    }

    private fun musicControl() {
        binding.buttonStartStop.setOnClickListener{
            if (mediaPlayer.isPlaying){
                binding.buttonStartStop.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24)
                mediaPlayer.pause()
            } else {
                binding.buttonStartStop.setBackgroundResource(R.drawable.ic_baseline_pause_24)
                mediaPlayer.start()
            }
        }

        binding.buttonTimePlus.setOnClickListener{
            mediaPlayer.seekTo(mediaPlayer.currentPosition + 3000)
        }

        binding.buttonTimeMinus.setOnClickListener {
            mediaPlayer.seekTo(mediaPlayer.currentPosition - 3000)
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) mediaPlayer.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        mediaPlayer.setOnCompletionListener {
            binding.buttonStartStop.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24)
            mediaPlayer.seekTo(0)
            mediaPlayer.pause()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mediaPlayer.stop()
    }

    private fun initialiseSeekBar() {

        binding.seekBar.max = mediaPlayer.duration

        val handler = Handler()
        handler.postDelayed(object: Runnable {
            override fun run() {
                try {
                    binding.seekBar.progress = mediaPlayer.currentPosition
                    handler.postDelayed(this, 1000)
                } catch (e: Exception) {
                    binding.seekBar.progress = 0
                }
            }
        }, 0)
    }
}