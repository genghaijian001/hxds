<view style="width:750rpx;height:100vh">
    <!-- 这是背景遮罩 -->
    <canvas wx:if='{{isShowImage}}' canvas-id="image-canvas" style='position:fixed;top:0rpx;width:{{windowWidth}}px; height:{{windowHeight}}px;'></canvas>
    <camera wx:if="{{cameraAuth}}" flash="{{flash ? 'on' : 'off'}}" bindinitdone="showTool" mode="normal" device-position="back" binderror="error" style="position:fixed;top:0rpx;width: 100%; height: 100vh;">
        <cover-view wx:if="{{showCamera}}" style="display: flex; height: 100vh; flex-direction: column;">
            <cover-view style="height:{{top}}rpx;" class="header bg-cover">
            </cover-view>
            <cover-view class="main-container">
                <cover-view style="height: {{height}}rpx;" class="container-center">
                </cover-view>
                <cover-view style="flex: 0 0 {{left}}px;" class="container-left bg-cover"></cover-view>
                <cover-view style="flex: 0 0 {{left}}px;" class="container-right bg-cover"></cover-view>
            </cover-view>
            <cover-view class="footer bg-cover">
            </cover-view>
        </cover-view>
        <cover-view wx:if="{{showCamera}}" style="position:fixed; top:0rpx; display: flex; height: 100vh; flex-direction: column;">
            <cover-view style="height:{{top}}rpx;" class="header">
                <!-- <cover-view wx:if="{{!cameraAuth}}"><Button bindopensetting='handleSetting' style="color:#000" open-type="openSetting">开启摄像头授权</Button></cover-view> -->
                <cover-view style="padding-top:{{globalStatusBarHeight}}rpx" class="title"> {{title}}</cover-view>
                <cover-image wx:if="{{cameraAuth}}" bindtap="changeFlash" style="margin-top:60rpx" class="flash btn-click" src="../../resources/{{flash ? 'ScanCode_Flash@3x' : 'ScanCode_FlashClose@3x'}}.png"> </cover-image>
            </cover-view>
            <cover-view class="main-container">
                <cover-view wx:if="{{cameraAuth}}" style="height: {{height}}rpx;" class="container-center">
                    <cover-view class="column">
                        <cover-image class="frame" src="../../resources/ScanCode_Frame.png"> </cover-image>
                        <cover-image style="transform: rotate(90deg);" class="frame" src="../../resources/ScanCode_Frame.png"> </cover-image>
                    </cover-view>
                    <cover-view class="bottom-column">
                        <cover-image style="transform: rotate(270deg);" class="frame" src="../../resources/ScanCode_Frame.png"> </cover-image>
                        <cover-image style="transform: rotate(180deg);" class="frame" src="../../resources/ScanCode_Frame.png"> </cover-image>
                    </cover-view>
                </cover-view>
                <cover-view wx:else style="height: {{height}}rpx;" class="no-authwording bg-cover">
                    <!-- <button style="line-height: 45px;height:45px; padding-left: 14px;padding-right: 14px" type="default" open-type="openSetting" bindopensetting='handleSetting' >点击授权 </button> -->
                    <cover-view style="position:relative; top:-45px;" class="touch-wording">暂无权限访问摄像头，请去小程序设置中授权打开摄像头</cover-view>
                </cover-view>
                <cover-view style="flex: 0 0 {{left}}px;" class="container-left"></cover-view>
                <cover-view style="flex: 0 0 {{left}}px;" class="container-right"></cover-view>
            </cover-view>
            <cover-view class="footer">
                <cover-view wx:if="{{cameraAuth}}" class="frame-wording">拍摄要求：清晰完整，避免缺边、模糊、反光。</cover-view>
                <!-- <cover-view class="touch-wording">轻触拍照</cover-view> -->
                <cover-view class="tool-container">
                    <!-- <cover-view wx:if="{{cameraAuth}}" style="margin-bottom: {{touchPadding}}px" class="touch-wording">轻触拍照</cover-view> -->
                    <cover-view class="img-container">
                        <cover-image bindtap="goPre" class="close tool-icon btn-click" src="../../resources/ScanCode_Close@3x.png"> </cover-image>
                        <cover-image  bindtap="takePhotoAction" class="cam tool-icon btn-click" src="../../resources/ScanCode_Cam@3x.png"> </cover-image>
                        <!-- <button style="line-height: 45px;height:45px; padding-left: 14px;padding-right: 14px" type="default" open-type="openSetting" bindopensetting='handleSetting' >点击授权 </button> -->
                        <cover-image bindtap="chooseImg" class="pic tool-icon btn-click" src="../../resources/ScanCode_Pic@3x.png"> </cover-image>
                    </cover-view>
                    <cover-view style="height: {{bottom}}px" class="tool-blank"></cover-view>
                </cover-view>
            </cover-view>
            <!-- <cover-view ></cover-view> -->
        </cover-view>
    </camera>
    <cover-view wx:if='{{!isShowImage}}' class="show-img">
        <cover-view style="display: flex; height: 100vh; flex-direction: column;">
            <cover-view style="height:{{top}}rpx;" class="header">
            </cover-view>
            <cover-view class="main-container">
                <cover-view style="height: {{height}}rpx;" class="container-center">
                    <cover-image class="photo" src="{{image}}"></cover-image>
                </cover-view>
                <cover-view style="flex: 0 0 {{left}}px;" class="container-left"></cover-view>
                <cover-view style="flex: 0 0 {{left}}px;" class="container-right"></cover-view>
            </cover-view>
            <cover-view class="footer">
            </cover-view>
            <!-- <cover-view ></cover-view> -->
        </cover-view>
    </cover-view>
    <view wx:if="{{!cameraAuth}}" style="position:fixed;top:0rpx;width: 100%; height: 100vh;background-color: #7f7f7f">
        <view style="display: flex; height: 100vh; flex-direction: column;">
            <view style="height:{{top}}rpx;" class="header">
            </view>
            <view class="main-container">
                <view style="height: {{height}}rpx;" class="container-center">
                </view>
                <view style="flex: 0 0 {{left}}px;" class="container-left"></view>
                <view style="flex: 0 0 {{left}}px;" class="container-right"></view>
            </view>
            <view class="footer">
            </view>
        </view>
        <view style="position:fixed; top:0rpx; display: flex; height: 100vh; flex-direction: column;">
            <view style="height:{{top}}rpx;" class="header">
                <!-- <view wx:if="{{!cameraAuth}}"><Button bindopensetting='handleSetting' style="color:#000" open-type="openSetting">开启摄像头授权</Button></view> -->
                <view style="padding-top:{{globalStatusBarHeight}}rpx" class="title"> {{title}}</view>
            </view>
            <view class="main-container">
                <view style="position:relative; top:-45px;" class="touch-wording">暂无权限访问摄像头，请点击下方按钮设置权限</view>
            </view>
            <view style="text-align: center; width: 750rpx; position:relative; top:-25px;">
                <button type="primary" style="width: 90px" open-type="openSetting">设置</button>
            </view>
            <view class="footer">
                <view class="tool-container">
                    <!-- <view wx:if="{{cameraAuth}}" style="margin-bottom: {{touchPadding}}px" class="touch-wording">轻触拍照</view> -->
                    <view class="img-container">
                        <image bindtap="goPre" class="close tool-icon btn-click" src="/static/resources/ScanCode_Close@3x.png"> </image>
                        <image  bindtap="takePhotoAction" class="cam tool-icon btn-click" src="/static/resources/ScanCode_Cam@3x.png"> </image>
                        <!-- <button style="line-height: 45px;height:45px; padding-left: 14px;padding-right: 14px" type="default" open-type="openSetting" bindopensetting='handleSetting' >点击授权 </button> -->
                        <image bindtap="chooseImg" class="pic tool-icon btn-click" src="/static/resources/ScanCode_Pic@3x.png"> </image>
                    </view>
                    <view style="height: {{bottom}}px" class="tool-blank"></view>
                </view>
            </view>
            <!-- <view ></view> -->
        </view>
    </view>
</view>
