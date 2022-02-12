package com.nahlasamir244.flickrsearchapp.presentation.photo_preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nahlasamir244.flickrsearchapp.R
import com.nahlasamir244.flickrsearchapp.databinding.PhotoPreviewFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoPreviewFragment : Fragment() {
    private lateinit var binding:PhotoPreviewFragmentBinding
    private val photoPreviewViewModel: PhotoPreviewViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.photo_preview_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = PhotoPreviewFragmentBinding.bind(view)
        binding.apply {
            viewmodel = photoPreviewViewModel
        }
    }

}