package com.nahlasamir244.flickrsearchapp.domain

import com.nahlasamir244.flickrsearchapp.data.model.Photo

typealias GetUrl = GeneratePhotoUrlUseCase
class GeneratePhotoUrlUseCase {

    operator fun invoke(photo:Photo):String {
        val url ="https://farm${photo.farm}.static.flickr.com/${photo.serverId}/" +
                "${photo.id}_${photo.secret}.jpg"
        url.filterNot { it.isWhitespace() }
        return url
    }
}