# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Keep application class
-keep class com.bootreceiver.app.BootReceiverApplication { *; }

# Keep receivers
-keep class com.bootreceiver.app.receiver.** { *; }

# Keep services
-keep class com.bootreceiver.app.service.** { *; }
