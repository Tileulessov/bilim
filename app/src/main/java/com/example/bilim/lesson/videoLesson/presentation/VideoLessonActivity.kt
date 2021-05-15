package com.example.bilim.lesson.videoLesson.presentation

import android.os.Bundle
import android.util.SparseArray
import android.view.View
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.example.bilim.R
import com.example.bilim.common.Constants
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource

private lateinit var toolbar: Toolbar
private var simpleExoPlayer: SimpleExoPlayer? = null
private lateinit var playerView: PlayerView
private var playWhenReady = true
private var currentWindow = 0
private var playBackPosition: Long = 0

class VideoLessonActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_lesson)
        initViews()
    }

    private fun initViews() {
        toolbar = findViewById(R.id.activity_video_lesson_toolbar)
        playerView = findViewById(R.id.activity_video_lesson_video_view)
        val title = intent.getStringExtra(Constants.LESSON_TITLE)
        setToolbarTitle(title)
        onBack()
    }

    private fun initPlayer() {
        simpleExoPlayer = SimpleExoPlayer.Builder(this).build()
        playerView.player = simpleExoPlayer
        val videoUrl = intent.getStringExtra(Constants.COURSE_CONTENT_PDF_URL)
        object : YouTubeExtractor(this) {
            override fun onExtractionComplete(ytFiles: SparseArray<YtFile>?, videoMeta: VideoMeta?) {
                if (ytFiles != null) {
                    val videoUrl = ytFiles[Constants.VIDEO_TAG].url
                    val audioUrl = ytFiles[Constants.AUDIO_TAG].url

                    val audioSource: MediaSource = ProgressiveMediaSource
                            .Factory(DefaultHttpDataSource.Factory())
                            .createMediaSource(MediaItem.fromUri(audioUrl))
                    val videoSource: MediaSource = ProgressiveMediaSource
                            .Factory(DefaultHttpDataSource.Factory())
                            .createMediaSource(MediaItem.fromUri(videoUrl))
                    simpleExoPlayer!!.setMediaSource(MergingMediaSource(
                            true,
                            videoSource,
                            audioSource,
                    ),
                            true)
                    simpleExoPlayer!!.prepare()
                    simpleExoPlayer!!.playWhenReady = playWhenReady
                    simpleExoPlayer!!.seekTo(currentWindow, playBackPosition)
                }
            }

        }.extract(videoUrl, false, true)
    }

    override fun onStart() {
        super.onStart()
        initPlayer()
    }

    override fun onResume() {
        super.onResume()
        initPlayer()
        hideSystemUI()
    }

    private fun hideSystemUI() {
        playerView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                )
    }

    override fun onPause() {

        releasePlayer()
        super.onPause()
    }

    override fun onStop() {

        releasePlayer()
        super.onStop()
    }

    private fun releasePlayer() {
        if (simpleExoPlayer != null) {
            playWhenReady = simpleExoPlayer!!.playWhenReady
            playBackPosition = simpleExoPlayer!!.currentPosition
            simpleExoPlayer!!.release()
            simpleExoPlayer = null
        }
    }


    private fun setToolbarTitle(title: String?) {
        toolbar.title = title
    }

    private fun onBack() {
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}