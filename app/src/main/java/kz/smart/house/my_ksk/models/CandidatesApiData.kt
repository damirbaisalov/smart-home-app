package kz.smart.house.my_ksk.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class CandidatesApiData(
    @JsonProperty("id")
    val id: String?,
    @JsonProperty("candidates_id")
    val candidatesId: String?,
    @JsonProperty("golosa")
    val golosa: String?,
    @JsonProperty("dom_id")
    val dom_id: String?,
    @JsonProperty("fio")
    val fio: String?,
    @JsonProperty("max_golos")
    val max_golos: String?
)