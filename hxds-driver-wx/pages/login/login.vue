<template>
    <view>
        <image src="../../static/login/top.png" mode="widthFix" class="top"></image>
        <button class="btn" open-type="getPhoneNumber" @getphonenumber="login">立即进入（司机版）</button>
        <view class="register-container">
            没有账号?
            <text class="link" @tap="toRegisterPage()">立即注册</text>
        </view>
        <view class="dev-area" v-if="showDev">
            <text class="dev-title">开发测试模式</text>
            <input class="dev-input" @input="onDevIdInput" placeholder="测试ID (如: driver1)" :value="devId" />
            <button class="dev-btn" size="mini" @tap="devLogin">测试登录</button>
        </view>
        <text @tap="toggleDev" class="dev-toggle">· · ·</text>
        <u-toast ref="uToast" />
    </view>
</template>

<script>
export default {
    data() {
        return { devId: '', showDev: false };
    },
    methods: {
        onDevIdInput: function(e) { this.devId = e.detail.value; },
        toggleDev: function() { this.showDev = !this.showDev; },
        _handleLoginSuccess: function(resp) {
            let that = this;
            let token = resp.data.token;
            let realAuth = resp.data.realAuth;
            let archive = resp.data.archive;
            uni.setStorageSync('token', token);
            uni.setStorageSync('realAuth', realAuth);
            uni.removeStorageSync('executeOrder');
            that.$refs.uToast.show({
                title: '登陆成功',
                type: 'success',
                callback: function() {
                    uni.setStorageSync('workStatus', '停止接单');
                    if (realAuth == 1) {
                        uni.redirectTo({ url: '../../identity/filling/filling?mode=create' });
                    } else if (archive == false) {
                        uni.showModal({
                            title: '提示消息',
                            content: '您还没有录入用于核实身份的面部特征信息，如果不录入将无法接单',
                            confirmText: '录入',
                            cancelText: '取消',
                            success: function(r) {
                                if (r.confirm) {
                                    uni.redirectTo({ url: '../../identity/face_camera/face_camera?mode=create' });
                                } else {
                                    uni.switchTab({ url: '../workbench/workbench' });
                                }
                            }
                        });
                    } else {
                        uni.switchTab({ url: '../workbench/workbench' });
                    }
                }
            });
        },
        login: function(e) {
            let that = this;
            let phoneCode = e.detail.code;
            uni.login({
                provider: 'weixin',
                success: function(resp) {
                    let data = { code: resp.code, phoneCode: phoneCode };
                    that.ajax(that.url.login, 'POST', data, function(resp) {
                        if (!resp.data.hasOwnProperty('token')) {
                            that.$refs.uToast.show({ title: '请先注册', type: 'error' });
                        } else {
                            that._handleLoginSuccess(resp);
                        }
                    });
                },
                fail: function(err) {
                    that.$refs.uToast.show({ title: '微信授权失败，请重试', type: 'error' });
                    console.error('uni.login failed:', err);
                }
            });
        },
        devLogin: function() {
            let that = this;
            if (!that.devId) { that.$refs.uToast.show({ title: '请输入测试ID', type: 'warning' }); return; }
            function enterWorkbench(token) {
                uni.setStorageSync('token', token);
                uni.setStorageSync('realAuth', 0);
                uni.removeStorageSync('executeOrder');
                uni.setStorageSync('workStatus', '停止接单');
                that.$refs.uToast.show({
                    title: '登录成功', type: 'success',
                    callback: function() { uni.switchTab({ url: '../workbench/workbench' }); }
                });
            }
            let loginData = { code: 'dev_driver_' + that.devId, phoneCode: 'dev_phone_' + that.devId };
            that.ajax(that.url.login, 'POST', loginData, function(resp) {
                if (resp.data.hasOwnProperty('token')) {
                    enterWorkbench(resp.data.token);
                } else {
                    // 账号不存在，自动注册后再进工作台
                    let regData = { code: 'dev_driver_' + that.devId, nickname: '测试司机' + that.devId, photo: '' };
                    that.ajax(that.url.registerNewDriver, 'POST', regData, function(regResp) {
                        enterWorkbench(regResp.data.token);
                    });
                }
            });
        },
        toRegisterPage: function() {
            uni.navigateTo({ url: '../register/register' });
        }
    },
    onLoad: function() {}
};
</script>

<style lang="less">
@import url('login.less');
</style>
