package com.mafqud.android.imageSlider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mafqud.android.ui.theme.*
import com.mafqud.android.R
import com.mafqud.android.home.HomeActivity
import com.mafqud.android.ui.compose.IconBack
import com.mafqud.android.ui.compose.IconShare
import com.mafqud.android.ui.compose.LoadImageAsync
import com.mafqud.android.util.other.shareText


class ImageSliderFragment : Fragment() {


    private val args: ImageSliderFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        controlHomeNavigationBar(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //requireActivity().statusBarColor(resources.getColor(R))
        return ComposeView(requireContext()).apply {
            setContent {
                MafQudTheme {
                    val actionsVisible = remember {
                        mutableStateOf(true)
                    }
                    val image = args.imageUrl
                    DisplayImages(image, actionsVisible)

                }
            }
        }
    }

    @Composable
    private fun DisplayImages(
        image: String?,
        actionsVisible: MutableState<Boolean>,
    ) {
        BoxUi(
            Modifier
                .fillMaxSize()
        ) {
            ColumnUi {
                TopActions(image)

                BoxUi(Modifier
                    .fillMaxSize()
                    .clickable(indication = null,
                        interactionSource = remember { MutableInteractionSource() }) {
                        //actionsVisible.value = !actionsVisible.value
                    }) {
                    LoadImageAsync(
                        modifier = Modifier.fillMaxSize(),
                        imageUrl = image,
                        contentScale = ContentScale.Fit
                    )
                }

            }

        }
    }

    @Composable
    private fun TopActions(imageUrl: String?) {
        BoxUi(
            modifier = Modifier
                .fillMaxWidth()
                .background(androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant)
        ) {
            RowUi(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconBack(onClick = {
                    findNavController().popBackStack()
                })

                IconShare(onClick = {
                    imageUrl?.let {
                        requireContext().shareText(imageUrl)
                    }
                })
            }
        }
    }


    override fun onDestroy() {
        //requireActivity().statusBarColor(resources.getColor(R.color.white))
        controlHomeNavigationBar(true)
        super.onDestroy()
    }

    private fun controlHomeNavigationBar(isVisible: Boolean) {
        if (activity is HomeActivity) {
            val homeActivity = activity as HomeActivity
            homeActivity.bottomBarNavigationVisibility(isVisible)
        }
    }

}