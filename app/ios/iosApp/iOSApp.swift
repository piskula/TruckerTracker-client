import Shared
import SwiftUI

@main
struct iOSApp: App {
    init() {
        #if DEBUG
        IosAppInitializerKt.bootstrapIosApp(isDebug: true)
        #else
        IosAppInitializerKt.bootstrapIosApp(isDebug: false)
        #endif
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
