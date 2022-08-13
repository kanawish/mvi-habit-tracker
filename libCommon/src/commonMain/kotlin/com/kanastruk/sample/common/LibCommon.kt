package com.kanastruk.sample.common

import com.kanastruk.sample.common.rest.auth.AnonymousResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

/**
 * Basically some hardcoded junk, to help with quickly testing
 * interops between platforms, testing some concepts for 'slide code',
 * etc.
 */
class LibCommon {
    companion object {
        const val COMMON_HELLO = "libCommon says Hello"
        val SC_BAR:SCFoo = SCFoo.SCBar
        val SC_BAZ:SCFoo = SCFoo.SCBaz()
    }
}

sealed class SCFoo {
    object SCBar:SCFoo() {
        val name = "wikiwiki"
    }
    data class SCBaz(val darth: String = "Vador") : SCFoo()
}


object SampleData {

    /**
     * Used this to avoid having to think too hard about storing
     * session/refresh credentials on iOS and Web. Also allows me
     * to develop the apis from a unit test environment.
     *
     * Use 'toCredentials()' to get credentials, implies we have to refresh every time.
     *
     * FIXME: Nuke account once the sample has proper multiplatform session management
     */
    val credentials = AnonymousResponse(
        expiresIn = "3600",
        idToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6ImJmMWMyNzQzYTJhZmY3YmZmZDBmODRhODY0ZTljMjc4ZjMxYmM2NTQiLCJ0eXAiOiJKV1QifQ.eyJwcm92aWRlcl9pZCI6ImFub255bW91cyIsImlzcyI6Imh0dHBzOi8vc2VjdXJldG9rZW4uZ29vZ2xlLmNvbS9tdmloYWJpdHRyYWNrZXIiLCJhdWQiOiJtdmloYWJpdHRyYWNrZXIiLCJhdXRoX3RpbWUiOjE2NTg2Nzk2NTcsInVzZXJfaWQiOiIxT0JLU2dXNkpkV2tEV2tzTkJhY2xHbW5BVGwxIiwic3ViIjoiMU9CS1NnVzZKZFdrRFdrc05CYWNsR21uQVRsMSIsImlhdCI6MTY1ODY3OTY1NywiZXhwIjoxNjU4NjgzMjU3LCJmaXJlYmFzZSI6eyJpZGVudGl0aWVzIjp7fSwic2lnbl9pbl9wcm92aWRlciI6ImFub255bW91cyJ9fQ.EwnmYUWaLyihmEiikECX6WBZ3BEqIdyHI6weGgzJ_ACARHJdMf_Vktk5WF2WDIKuy08Wpwthynv14DajUPFFFLAqyxYQeHwYHof5Plkmkodf-yTnB_9BeZC2q59dMRs1dCqWxVxLnH63WuPdaTuPXmVEgbdHlH6IeyFcwO31j2jKqSeyfN2F6ze1wXbo2NDrOQl8MFj-aqg7JNABzS6bbvQamMVVvd90E2tBAgebRDWrASjyDKa1nhANkq9FXM446Ne9KGpGJipd48q_kBd7sZFrzLp0lu7v9UPQPqxcILn03_QhcNPBWY_3jPUVwhACcKyabSflvSy4k8jYPoERKw",
        kind = "identitytoolkit#SignupNewUserResponse",
        localId = "1OBKSgW6JdWkDWksNBaclGmnATl1",
        refreshToken = "AOEOulYbgZp5FGYFDtjKFyLldZ7bCjRsZgzhuZAY7CWfrncL_6z1MQvZrMFJGGemRmXpuaOkTTCYJcQIJ9yZgyjgtv0of3U3O2nxwbc3ru2obd66WkCHBRp05gZpQ7mP066xiJX2dxwgKrqsBS9o11aw-yE2cAS100OO8rZXtOzSQ9y_LNgCHtvtZK_KOr5N2L22p9ciMjBX"
    ).toCredentials()

}