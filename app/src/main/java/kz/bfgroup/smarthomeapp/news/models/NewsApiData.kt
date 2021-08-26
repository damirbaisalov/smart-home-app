package kz.bfgroup.smarthomeapp.news.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class NewsApiData(
    @JsonProperty("id")
    val id: String?,
    @JsonProperty("title")
    val title: String?,
    @JsonProperty("description")
    val description: String?,
    @JsonProperty("img")
    val img: String?,
    @JsonProperty("data")
    val data: String?
)