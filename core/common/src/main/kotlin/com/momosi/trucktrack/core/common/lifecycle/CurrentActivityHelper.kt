package com.momosi.trucktrack.core.common.lifecycle

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentActivityHelper @Inject constructor(@ApplicationContext context: Context) {

    private var currentActivities: MutableMap<String, WeakReference<Activity>> = ConcurrentHashMap()

    val activity: Activity?
        get() = currentActivities.values
            .firstOrNull { it.get()?.getLifecycleOwner()?.lifecycle?.currentState?.isAtLeast(Lifecycle.State.STARTED) == true }
            ?.get()

    init {
        (context as Application).registerActivityLifecycleCallbacks(
            object : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    currentActivities[activity::class.java.simpleName] = WeakReference(activity)
                }

                override fun onActivityStarted(p0: Activity) {}
                override fun onActivityResumed(activity: Activity) {
                    currentActivities[activity::class.java.simpleName] = WeakReference(activity)
                }

                override fun onActivityPaused(p0: Activity) {}
                override fun onActivityStopped(p0: Activity) {}
                override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}
                override fun onActivityDestroyed(p0: Activity) {}
            },
        )
    }

    fun Context.getLifecycleOwner(): LifecycleOwner? = when (this) {
        is LifecycleOwner -> this
        is ContextWrapper -> this.baseContext.getLifecycleOwner()
        else -> null
    }
}
