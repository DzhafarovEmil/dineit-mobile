package ua.nure.dzhafarov.dineit.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokenData(
        @SerializedName("access_token")
        @Expose
        var accessToken: String,
        @SerializedName("refresh_token")
        @Expose
        var refreshToken: String,
        @SerializedName("expires_in")
        @Expose
        var expiresIn: Int,
        @SerializedName("scope")
        @Expose
        var scope: String,
        @SerializedName("token_type")
        @Expose
        var tokenType: String
)