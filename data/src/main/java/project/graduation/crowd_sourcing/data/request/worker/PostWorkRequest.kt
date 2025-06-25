package project.graduation.crowd_sourcing.data.request.worker

import com.google.gson.annotations.SerializedName

data class PostWorkRequest(
    @SerializedName("work") val work: String,
    @SerializedName("workCount") val workCount: Int,
    @SerializedName("workpoint") val workpoint: Int,
    @SerializedName("martName") val martName: String,
    @SerializedName("sigungu") val sigungu: String,
    @SerializedName("item") val item: String,
    @SerializedName("workDate") val workDate: String,
    @SerializedName("memberId") val memberId: Int,
    @SerializedName("category") val category: String,
    @SerializedName("workhour") val workhour: Int,
    @SerializedName("expirationDate") val expirationDate: String
)