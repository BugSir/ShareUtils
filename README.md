# ShareUtils
[![](https://jitpack.io/v/BugSir/ShareUtils.svg)](https://jitpack.io/#BugSir/ShareUtils)
# 引用方法:<br/>
<pre><code>
工程目录gradle
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
项目gradle
	dependencies {
          //微信分享
          implementation 'com.github.BugSir.ShareUtils:wxshare:1.0.0'
          implementation 'com.tencent.mm.opensdk:wechat-sdk-android-without-mta:+'
          //qq分享
          implementation 'com.github.BugSir.ShareUtils:qqshare:1.0.0'
          implementation files('libs/open_sdk_r5923_lite.jar')//这里是qq的sdk
	}
</code></pre>
### 分享使用
```java
        QQShareUtil.with(this)
                .setQQId("")
                .setAppName("")
                .setIsQzone(false)
                .shareUrl();
        WxShareUtil.with(this)
                .setWxId("")
                .setIsShareCycle(false)
                .shareUrl();
```
