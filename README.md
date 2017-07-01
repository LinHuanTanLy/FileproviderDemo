# FileproviderDemo
Android 7.0 拍照以及获取文件


**注意地方**

android 清单文件需要注册：
```xml

 <provider
            android:name="android.support.v4.content.FileProvider"
            android:authorities="applicationId.fileprovider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_paths"/>
        </provider>

```


其中注意*android:resource="@xml/file_paths"*

该目录位于：![xml目录][1]


  [1]: http://image.talkmoney.cn/1498915695118.jpg "1498915695118.jpg"



其中xml文件为：
```xml

<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <external-path name="my_images" path="Android/data/com.ly.liveanimationdemo/files/Pictures/" />
    <external-path path="" name="camera_photos" />
    <external-path name="images" path="Pictures/" />
    <external-path name="dcim" path="DCIM/" />

</paths>

```


注意 *path="Android/data/com.ly.liveanimationdemo/files/Pictures/"*

path路径里面注意 包名要与 build文件里面的包名一致
