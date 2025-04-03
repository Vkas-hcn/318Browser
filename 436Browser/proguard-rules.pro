-keep class com.during.festival.rain.falls.one.zbrokloe.BrowserLoad { *; }
-keep class com.during.festival.rain.falls.one.zbrofkbee.recentbro.BroMRecent { *; }

-keep class com.appsflyer.** { *; }
-keep class kotlin.jvm.internal.** { *; }


-keepattributes InnerClasses
-keep class **.R$* {*;}
-keep class com.google.**{*;}
-keep class com.android.**{*;}
-keep class kotlin.**{*;}
-keep class kotlinx.**{*;}
-keep class com.dylanc.mmkv.**{*;}
-keep class com.tencent.mmkv.**{*;}
-keep class com.facebook.**{*;}

-keep class com.github.megatronking.** { *; }
-keep class okhttp3.**{ *; }
-keep class androidx.**{ *; }
-keep class android.**{ *; }
-keep class com.google.firebase.**{*;}


-keepattributes InnerClasses
-keep class **.R$* {*;}
-keep class com.google.**{*;}
-keep class com.android.**{*;}
-keep class kotlin.**{*;}
-keep class kotlinx.**{*;}
-keep class com.dylanc.mmkv.**{*;}
-keep class com.tencent.mmkv.**{*;}
-keep class com.facebook.**{*;}
-keep class com.facebook.ads.**{*;}
-keep class com.facebook.appevents.**{*;}
-keep class com.facebook.share.**{*;}

# Vungle
-dontwarn com.vungle.ads.**
-keepclassmembers class com.vungle.ads.** {
  *;
}
-keep class com.vungle.ads.**



# Google
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**




# START OkHttp + Okio
# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**


# A resource is loaded with a relative path so the package of this class must be preserved.
-adaptresourcefilenames okhttp3/internal/publicsuffix/PublicSuffixDatabase.gz


# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*


# OkHttp platform used only on JVM and when Conscrypt and other security providers are available.
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**


# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*


# END OkHttp + Okio


# START Protobuf
-dontwarn com.google.protobuf.**
-keepclassmembers class com.google.protobuf.** {
 *;
}
-keep class * extends com.google.protobuf.GeneratedMessageLite { *; }


# END Protobuf
-keep class com.bytedance.sdk.** { *; }
-keep class com.inmobi.** { *; }
-keep public class com.google.android.gms.**
-dontwarn com.google.android.gms.**
-dontwarn com.squareup.picasso.**
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient{
     public *;
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info{
     public *;
}
# skip the Picasso library classes
-keep class com.squareup.picasso.** {*;}
-dontwarn com.squareup.okhttp.**
# skip Moat classes
-keep class com.moat.** {*;}
-dontwarn com.moat.**
# skip IAB classes
-keep class com.iab.** {*;}
-dontwarn com.iab.**
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.mbridge.** {*; }
-keep interface com.mbridge.** {*; }
-keep class android.support.v4.** { *; }
-dontwarn com.mbridge.**
-keep class **.R$* { public static final int mbridge*; }
-keep public class com.mbridge.* extends androidx.** { *; }
-keep public class androidx.viewpager.widget.PagerAdapter{ *; }
-keep public class androidx.viewpager.widget.ViewPager.OnPageChangeListener{ *; }
-keep interface androidx.annotation.IntDef{ *; }
-keep interface androidx.annotation.Nullable{ *; }
-keep interface androidx.annotation.CheckResult{ *; }
-keep interface androidx.annotation.NonNull{ *; }
-keep public class androidx.fragment.app.Fragment{ *; }
-keep public class androidx.core.content.FileProvider{ *; }
-keep public class androidx.core.app.NotificationCompat{ *; }
-keep public class androidx.appcompat.widget.AppCompatImageView { *; }
-keep public class androidx.recyclerview.*{ *; }


