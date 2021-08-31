package kz.bfgroup.smarthomeapp.registration.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class StreetNameApiData(
    @JsonProperty("street")
    val street: String?
)