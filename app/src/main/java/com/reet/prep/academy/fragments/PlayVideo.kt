package com.reet.prep.academy.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.util.Util
import com.reet.prep.academy.R
import com.reet.prep.academy.databinding.FragmentPlayVideoBinding

private const val ARG_PARAM1 = "param1"

class PlayVideo : Fragment() {
    private lateinit var binding: FragmentPlayVideoBinding
    private var param1: String? = null
    lateinit var playerView: PlayerView
    lateinit var player: SimpleExoPlayer
    var currentWindow = 0
    var playbackPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString("VIDEO_URL")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPlayVideoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playerView = binding.vvPlayVideo
        initPlayer()
    }

    private fun initPlayer() {
        player = SimpleExoPlayer.Builder(requireContext()).build()
        playerView.player = player
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24)
            initPlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun releasePlayer() {

    }
}