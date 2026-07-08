package com.momosi.trucktrack.user.internal

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.usePinned
import platform.CoreFoundation.CFDataCreate
import platform.CoreFoundation.CFDataRef
import platform.CoreFoundation.CFDictionaryCreateMutable
import platform.CoreFoundation.CFDictionarySetValue
import platform.CoreFoundation.CFErrorRefVar
import platform.CoreFoundation.CFRelease
import platform.CoreFoundation.kCFAllocatorDefault
import platform.CoreFoundation.kCFTypeDictionaryKeyCallBacks
import platform.CoreFoundation.kCFTypeDictionaryValueCallBacks
import platform.Security.SecKeyCreateWithData
import platform.Security.SecKeyVerifySignature
import platform.Security.kSecAttrKeyClass
import platform.Security.kSecAttrKeyClassPublic
import platform.Security.kSecAttrKeyType
import platform.Security.kSecAttrKeyTypeRSA
import platform.Security.kSecKeyAlgorithmRSASignatureMessagePKCS1v15SHA256

@OptIn(ExperimentalForeignApi::class)
actual fun verifyRs256(
    signedData: ByteArray,
    signature: ByteArray,
    publicKeyDer: ByteArray,
): Boolean = memScoped {
    val error = alloc<CFErrorRefVar>()

    val attributes = CFDictionaryCreateMutable(kCFAllocatorDefault, 2, kCFTypeDictionaryKeyCallBacks.ptr, kCFTypeDictionaryValueCallBacks.ptr)
    CFDictionarySetValue(attributes, kSecAttrKeyType, kSecAttrKeyTypeRSA)
    CFDictionarySetValue(attributes, kSecAttrKeyClass, kSecAttrKeyClassPublic)

    val keyData = publicKeyDer.toCFData()
    val secKey = SecKeyCreateWithData(keyData, attributes, error.ptr)
    CFRelease(keyData)
    CFRelease(attributes)

    if (secKey == null) return@memScoped false

    val signedDataCf = signedData.toCFData()
    val signatureCf = signature.toCFData()

    val verified = SecKeyVerifySignature(
        secKey,
        kSecKeyAlgorithmRSASignatureMessagePKCS1v15SHA256,
        signedDataCf,
        signatureCf,
        error.ptr,
    )

    CFRelease(signedDataCf)
    CFRelease(signatureCf)
    CFRelease(secKey)

    verified
}

@OptIn(ExperimentalForeignApi::class)
private fun ByteArray.toCFData(): CFDataRef? = usePinned { pinned ->
    CFDataCreate(kCFAllocatorDefault, pinned.addressOf(0).reinterpret(), size.toLong())
}
