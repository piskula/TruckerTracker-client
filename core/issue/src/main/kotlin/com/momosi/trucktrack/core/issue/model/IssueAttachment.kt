package com.momosi.trucktrack.core.issue.model

import java.time.Instant

data class IssueAttachment(val id: Long, val filename: String, val contentType: String, val sizeBytes: Long, val uploadedBy: Account?, val uploadedAt: Instant)
