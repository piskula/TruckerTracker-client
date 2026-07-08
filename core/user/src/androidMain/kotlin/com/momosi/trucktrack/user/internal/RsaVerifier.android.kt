package com.momosi.trucktrack.user.internal

import java.security.KeyFactory
import java.security.Signature
import java.security.spec.X509EncodedKeySpec

actual fun verifyRs256(
    signedData: ByteArray,
    signature: ByteArray,
    publicKeyDer: ByteArray,
): Boolean {
    val publicKey = KeyFactory.getInstance("RSA").generatePublic(X509EncodedKeySpec(publicKeyDer))
    return Signature.getInstance("SHA256withRSA")
        .apply {
            initVerify(publicKey)
            update(signedData)
        }
        .verify(signature)
}
