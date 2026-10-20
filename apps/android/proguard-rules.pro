# Revera R8 rules. Libraries in use (kotlinx.serialization, Ktor, Koin, Compose) ship their own
# consumer rules; add app-specific keeps here.

# Keep @Serializable navigation keys and models readable in crash reports / saved state.
-keepattributes *Annotation*, InnerClasses, Signature, SourceFile, LineNumberTable
-renamesourcefileattribute SourceFile

# Ktor references optional JVM classes that don't exist on Android.
-dontwarn java.lang.management.**
-dontwarn org.slf4j.**
