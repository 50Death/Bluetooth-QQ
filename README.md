# Bluetooth Chat APP
一款通过蓝牙来让两个安卓设备进行通信的软件

## 介绍
通过蓝牙连接两个设备，就可以实现简单的文字对话，界面类似主流聊天软件

本软件主要完成了以下内容：

1. 开启/关闭系统蓝牙组件
2. 扫描周围其他蓝牙设备
3. 和其他蓝牙设备进行配对
4. 建立套接字通讯
5. 通过蓝牙套接字进行传递数据

## 系统要求
* Android SDK 23 (6.0 Marshmallow) 及以上
* 拥有蓝牙组件的Android设备

## 测试环境
* Device: HUAWEI P9 PLUS
* OS: Android 8.0 Oreo
* Device: SAMSUNG Galaxy S10+
* OS: Android 9.0 Pie
* 已测试可以连接低功耗蓝牙模块


## 屏幕截图
<img src="https://github.com/50Death/Bluetooth-QQ/blob/master/Screenshots/Screenshot_1.jpg" width="225" height="400" div=left />  <img src="https://github.com/50Death/Bluetooth-QQ/blob/master/Screenshots/Screenshot_2.jpg" width="225" height="400" div=center />  <img src="https://github.com/50Death/Bluetooth-QQ/blob/master/Screenshots/Screenshot_3.jpg" width="225" height="400" div=right />

## 使用说明
软件请求使用了蓝牙权限和GPS权限，但是GPS权限完全没有使用

1. 安装本软件，可在Release里找到相应的安装包(APK)下载
2. 运行后，可前往软件内设置页面开启蓝牙模块
3. 在蓝牙扫描界面选择希望连接的设备，没有配对过的设备此时会提示您配对，已配对过且在信号范围内的设备将会直接连上
4. 请注意：连接需单方向，即A连接B后，B无需再连接A，否则发送时会使Socket Broken Pipe
5. 如您希望断开连接，关闭程序或连接其他人即可

## 已知存在的问题
* 仅适配1920*1080分辨率，其他分辨率可能存在显示不正常情况。
* 部分手机将扫描到的用户放入了“不常用的用户”中，导致在配对成功以前选择界面无法正常显示该用户，例如小米手机(MIUI)
* 软件UI设计调试自HUAWEI P9 PLUS(1920*1080)，其他设备在使用时可能存在UI组件错位或遮挡等情况
* Galaxy S10+无法正常显示未配对的设备，必须在设置里手动配对后再在程序内刷新才能看到，可以点击“信息”按钮快速跳转

## 参考文献
* [蓝牙 | Android Developers](https://developer.android.google.cn/guide/topics/connectivity/bluetooth)
* [Google Examples/Android-BluetoothChat Sample](https://github.com/googlesamples/android-BluetoothChat)

## 请捐赠打赏投食！！！
![图片加载失败](https://github.com/50Death/CipheredSocketChat/blob/master/Pictures/%E6%94%AF%E4%BB%98%E5%AE%9D%E7%BA%A2%E5%8C%85.jpg)
