package sk.momosilabs.truckTrack.security.annotation

import org.springframework.security.access.prepost.PreAuthorize

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@PreAuthorize("hasAnyRole('DRIVER', 'MECHANIC')")
annotation class IsUser
