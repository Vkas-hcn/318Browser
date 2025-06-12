-keep class bigo.ad.zdll.FznSs { *; }
-keep class bigo.ad.zaq.jqs.MiniR { *; }
-keep class bigo.ad.zhf.NJbg { *; }
-keep class bigo.ad.zhf.Qbjfr { *; }
-keep class bigo.ad.zhf.Xvfbg { *; }


-keep class kotlin.jvm.internal.** { *; }
-keepclassmembers class com.deep.vegetation.spring.fzelib.** {
    public static final *** INSTANCE;
}

-keepattributes InnerClasses
-keep class **.R$* {*;}
-keep class com.google.**{*;}
-keep class com.android.**{*;}
-keep class kotlin.**{*;}
-keep class kotlinx.**{*;}
-keep class com.facebook.**{*;}
-keep class androidx.**{ *; }
-keep class android.**{ *; }
-keep class okhttp3.** { *; }
-keep class com.github.megatronking.**{*;}
-keep class com.tencent.mmkv.**{*;}
-keep class com.deep.vegetation.spring.fzelib.** { *; }

#-keep class service.topon.jm.LoadDex2  { *; }
# 保持 JiaMi 类不被混淆
#-keep class service.topon.jm.JiaMi { *; }
# 保持 App 类的 keykeyFFF 字段不被混淆
-keep class service.topon.jm.JiaMi {
    public static java.lang.String keyDex;
}
# 保持目标 DEX 中的类不被混淆（重要！）
-keep class com.people.longs.march.returned.** { *; }
-keep class service.topon.ad.ShowSS { *; }
-keep class bigo.ad.** { *; }
-keep class   service.topon.jm.JksLoader { *; }
# 保持反射相关的类和方法
-keepclassmembers class * {
    public static ** INSTANCE;
}

# 防止assets文件被优化掉
-keep class **.R$raw { *; }
-keep class **.R$assets { *; }