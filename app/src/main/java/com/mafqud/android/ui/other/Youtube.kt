package com.mafqud.android.ui.other


/*
@Composable
fun ComposeYoutube(
    modifier: Modifier,
    supportFragmentManager: FragmentManager,
    onError: (String) -> Unit
) {

    val apiKey = stringResource(id = R.string.google_console_api)
    AndroidView(
        modifier = modifier,
        factory = {
            var player: YouTubePlayer? = null

            val onPlaylistChangeListener = object : YouTubePlayer.PlaylistEventListener {
                override fun onPlaylistEnded() {}
                override fun onPrevious() {}
                override fun onNext() {}
            }

            val youtubeApiInitializedListener = object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, p1: YouTubePlayer?, p2: Boolean) {
                    player = p1
                    player?.setPlaylistEventListener(onPlaylistChangeListener)
                    player?.loadVideo("https://youtu.be/y3xAC0fqwOc")
                }

                override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
                    onError("TODO")
                }
            }

            FrameLayout(it).apply {
                // select any R.id.X from your project, it does not matter what it is, but container must have one for transaction below.
                id = R.id.helpFragment

                val youtubeView = YouTubePlayerSupportFragment()

                supportFragmentManager
                    .beginTransaction()
                    .add(
                        R.id.helpFragment,
                        youtubeView,
                        null
                    )
                    .commit()

                youtubeView.initialize(apiKey, youtubeApiInitializedListener)
            }
        },
        update = { }
    )
}*/
