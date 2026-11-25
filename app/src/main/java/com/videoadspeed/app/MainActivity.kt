package com.videoadspeed.app

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.view.ViewGroup
import android.widget.*
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackParameters
import androidx.media3.ui.PlayerView

class MainActivity : Activity() {
    private var player: ExoPlayer? = null
    private lateinit var playerView: PlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(20, 20, 20, 20)
            setBackgroundColor(-1)
        }

        val title = TextView(this).apply {
            text = "VideoAdSpeed"
            textSize = 26f
            setTextColor(0xff222222.toInt())
        }
        layout.addView(title)

        playerView = PlayerView(this)
        layout.addView(playerView, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f
        ))

        val open = Button(this).apply {
            text = "動画を選択"
            setOnClickListener {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    type = "video/*"
                    addCategory(Intent.CATEGORY_OPENABLE)
                }
                startActivityForResult(intent, 1)
            }
        }
        layout.addView(open)

        val speeds = LinearLayout(this)
        for (speed in listOf(0.5f, 1f, 1.5f, 2f, 3f, 4f)) {
            val button = Button(this).apply {
                text = "${speed}x"
                setOnClickListener {
                    player?.setPlaybackParameters(PlaybackParameters(speed))
                }
            }
            speeds.addView(button, LinearLayout.LayoutParams(0, -2, 1f))
        }
        layout.addView(speeds)

        setContentView(layout)
    }

    override fun onStart() {
        super.onStart()
        player = ExoPlayer.Builder(this).build()
        playerView.player = player
    }

    override fun onStop() {
        playerView.player = null
        player?.release()
        player = null
        super.onStop()
    }

    @Deprecated("Deprecated")
    override fun onActivityResult(
        requestCode: Int, resultCode: Int, data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            data?.data?.let {
                player?.setMediaItem(MediaItem.fromUri(it))
                player?.prepare()
                player?.play()
            }
        }
    }
}
