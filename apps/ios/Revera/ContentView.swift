import ReveraShared
import SwiftUI
import UIKit

/// Hosts the shared Compose UI. All screens, navigation and adaptive layout live in Kotlin.
struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        // Compose draws edge to edge and applies safe-area and keyboard insets itself.
        ComposeView()
            .ignoresSafeArea()
    }
}
