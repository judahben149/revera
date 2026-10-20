# ProGuard rules for the release desktop build (packageRelease* tasks).
-dontwarn kotlinx.coroutines.debug.**
-dontwarn io.ktor.**
-dontwarn org.slf4j.**
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# kotlinx.serialization: keep generated serializers (navigation keys, network DTOs).
-keepattributes *Annotation*, InnerClasses, Signature
-keepclassmembers class **$$serializer { *; }
-keepclasseswithmembers class * { kotlinx.serialization.KSerializer serializer(...); }
-keep class com.jj.revera.**$Companion { *; }

# Koin resolves ViewModels reflectively by constructor.
-keep class * extends androidx.lifecycle.ViewModel { <init>(...); }
