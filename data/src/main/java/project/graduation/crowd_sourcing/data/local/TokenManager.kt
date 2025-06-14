package project.graduation.crowd_sourcing.data.local

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

    fun getAccessToken(): String? {
        // 임시로 하드코딩
        return "3cc3c08e-dd85-4264-893c-0f5ddfaddaea"
        
        // 원래 코드 (로그인 기능 활성화 시 주석 해제)
        // return prefs.getString("ACCESS_TOKEN", null)
    }

    fun getRefreshToken(): String? = prefs.getString("REFRESH_TOKEN", null)

    fun getUserId(): Int = prefs.getInt("USER_ID", -1)

    fun clear() {
        prefs.edit().clear().apply()
    }
}
