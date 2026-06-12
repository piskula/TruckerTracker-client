package sk.momosilabs.truckTrack.file.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import sk.momosilabs.truckTrack.file.entity.FileEntity

interface FileRepository : JpaRepository<FileEntity, Long>
