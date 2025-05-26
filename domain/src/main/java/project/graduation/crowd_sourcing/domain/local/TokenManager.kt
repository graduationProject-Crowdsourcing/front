package project.graduation.crowd_sourcing.domain.local

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun save(accessToken: String, refreshToken: String, userId: Int) {
        prefs.edit().apply {
            putString("ACCESS_TOKEN", accessToken)
            putString("REFRESH_TOKEN", refreshToken)
            putInt("USER_ID", userId)
            apply()
        }
    }
    fun saveUsername(userName:String){
        prefs.edit().putString("USER_NAME", userName)
    }

    fun getAccessToken(): String? = prefs.getString("ACCESS_TOKEN", null)

    fun getRefreshToken(): String? = prefs.getString("REFRESH_TOKEN", null)

    fun getUserId(): Int = prefs.getInt("USER_ID", -1)

    fun getUserName(): String? = prefs.getString("USER_NAME", null)


    fun clear() {
        prefs.edit().clear().apply()
    }
}
