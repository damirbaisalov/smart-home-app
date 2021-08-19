package kz.bfgroup.smarthomeapp.registration.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class StreetApiData(
    @JsonProperty("id")
    val id: String?,
    @JsonProperty("street")
    val street: String?,
    @JsonProperty("nomer")
    val nomer: String?
)