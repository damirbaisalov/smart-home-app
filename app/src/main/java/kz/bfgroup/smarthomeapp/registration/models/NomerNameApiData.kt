package kz.bfgroup.smarthomeapp.registration.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class NomerNameApiData(
    @JsonProperty("nomer")
    val nomer: String?
)