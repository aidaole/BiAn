package com.aidaole.bian.data.entity

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Serializable
data class FeedTab (
    @SerialName("tab_id") val id: Int,
    @SerialName("tab_name") val tabName: String,
    @SerialName("contents") val contents: List<FeedPost>
)

@Serializable
data class FeedPost(
    @SerialName("publisher_name") val publisherName: String,
    @SerialName("publisher_avatar") val publisherAvatar: String,
    @SerialName("published_at") @Serializable(with = LocalDateTimeSerializer::class) val publishedAt: LocalDateTime,
    @SerialName("is_followed") val isFollowed: Boolean,
    @SerialName("content") val content: String
)

@Serializable
object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    override val descriptor = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.toString())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun deserialize(decoder: Decoder): LocalDateTime {
        val string = decoder.decodeString()
        return LocalDateTime.ofInstant(Instant.parse(string), ZoneId.systemDefault())
    }
}