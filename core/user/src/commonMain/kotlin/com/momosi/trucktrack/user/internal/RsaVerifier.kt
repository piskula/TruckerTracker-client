package com.momosi.trucktrack.user.internal

/**
 * Verifies an RS256 (RSA-SHA256, PKCS#1 v1.5) signature.
 *
 * @param signedData the exact bytes that were signed (JWT's `header.payload` ASCII substring)
 * @param signature the raw signature bytes
 * @param publicKeyDer the RSA public key, X.509 SubjectPublicKeyInfo, DER-encoded
 */
expect fun verifyRs256(
    signedData: ByteArray,
    signature: ByteArray,
    publicKeyDer: ByteArray,
): Boolean
