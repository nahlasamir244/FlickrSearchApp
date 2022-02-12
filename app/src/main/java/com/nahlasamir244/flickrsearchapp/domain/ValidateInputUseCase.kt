package com.nahlasamir244.flickrsearchapp.domain

typealias IsValid = ValidateInputUseCase
class ValidateInputUseCase {
    operator fun invoke(input:String,inputType: InputType):Boolean{
        return when(inputType){
            InputType.PHOTO_SEARCH_QUERY -> isPhotoSearchQueryValid(input)
        }
    }
    private fun isPhotoSearchQueryValid(input: String):Boolean
    //we can add any validation logic here
    = input.isNotBlank() && input.isNotEmpty()

}

//each inputType has a different validation criteria
enum class InputType{
    PHOTO_SEARCH_QUERY
}