package com.northis.speechvotingapp.authentication

interface IOAuthSettingsProvider {
    val authUrl: String
    val tokenUrl: String
    val clientId: String
    val clientSecret: String
    val grantType: String
    val grantTypeRefresh: String
    val redirectUri: String
    val responseType: String
    val scope: String

    /**
     * PKCE
     */
//TODO Генераторы.
    val codeChallenge: String
    val codeVerifier: String

    companion object {
        val instance: IOAuthSettingsProvider by lazy {
            object : IOAuthSettingsProvider {
                override val authUrl: String
                    get() = "http://192.168.100.8:5001/connect/authorize"
                override val tokenUrl: String
                    get() = "http://192.168.100.8:5001/connect/token"
                override val clientId: String
                    get() = "SpeechVotingAndroid"
                override val clientSecret: String
                    get() = "androidSecret"
                override val grantType: String
                    get() = "authorization_code"
                override val grantTypeRefresh: String
                    get() = "refresh_token"
                override val redirectUri: String
                    get() = "https://com.northis.speechvotingapp/signin-oidc"
                override val responseType: String
                    get() = "code"
                override val scope: String
                    get() = "SpeechVotingApi openid profile offline_access"
                override val codeChallenge: String
                    get() = "oTsyb5tdGTz6jcjq6eh5CJdMMZrnVdEoGOkiy0GnJek"
                override val codeVerifier: String
                    get() = "NRaxVF6qEMcF-Kc_aL3VQfxup5cmc43xL7N8tUs313w"

            }
        }
    }
}