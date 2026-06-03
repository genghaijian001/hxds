<template>
    <view class="page">
        <view class="back-btn" @tap="goBack">‹ 返回</view>
        <image src="../../static/register/top.jpg" mode="widthFix" class="hero-img" />
        <view class="hero-overlay">
            <text class="hero-title">成为代驾司机</text>
            <text class="hero-sub">灵活接单 · 高额收益 · 自由时间</text>
        </view>

        <view class="card">
            <view class="card-title-bar"><text class="card-title">为什么选择我们</text></view>
            <view class="benefit-grid">
                <view class="benefit-item">
                    <text class="benefit-icon">💰</text>
                    <text class="benefit-name">高额收益</text>
                    <text class="benefit-desc">日均收入丰厚</text>
                </view>
                <view class="benefit-item">
                    <text class="benefit-icon">⏰</text>
                    <text class="benefit-name">自由时间</text>
                    <text class="benefit-desc">弹性灵活接单</text>
                </view>
                <view class="benefit-item">
                    <text class="benefit-icon">🛡️</text>
                    <text class="benefit-name">安全保障</text>
                    <text class="benefit-desc">全程保险覆盖</text>
                </view>
                <view class="benefit-item">
                    <text class="benefit-icon">📱</text>
                    <text class="benefit-name">便捷操作</text>
                    <text class="benefit-desc">手机轻松接单</text>
                </view>
            </view>
        </view>

        <view class="card">
            <view class="card-title-bar"><text class="card-title">基本要求</text></view>
            <view class="req-item"><text class="req-dot">✓</text><text class="req-text">年龄23~55周岁</text></view>
            <view class="req-item"><text class="req-dot">✓</text><text class="req-text">三年以上安全驾驶经验</text></view>
            <view class="req-item"><text class="req-dot">✓</text><text class="req-text">持有有效C1及以上驾驶证</text></view>
            <view class="req-item"><text class="req-dot">✓</text><text class="req-text">无重大违章及事故记录</text></view>
            <view class="req-item"><text class="req-dot">✓</text><text class="req-text">提供本人身份证与驾驶证</text></view>
        </view>

        <view class="card">
            <view class="card-title-bar"><text class="card-title">注册流程</text></view>
            <view class="step-item">
                <view class="step-num">01</view>
                <view class="step-info">
                    <text class="step-title">在线注册</text>
                    <text class="step-desc">填写昵称完成小程序注册</text>
                </view>
            </view>
            <view class="step-item">
                <view class="step-num">02</view>
                <view class="step-info">
                    <text class="step-title">提交资料</text>
                    <text class="step-desc">上传身份证与驾驶证照片</text>
                </view>
            </view>
            <view class="step-item">
                <view class="step-num">03</view>
                <view class="step-info">
                    <text class="step-title">审核通过</text>
                    <text class="step-desc">1~3个工作日完成信息审核</text>
                </view>
            </view>
            <view class="step-item last">
                <view class="step-num">04</view>
                <view class="step-info">
                    <text class="step-title">开始接单</text>
                    <text class="step-desc">签署合同后即可正式上岗</text>
                </view>
            </view>
        </view>

        <view class="card">
            <view class="card-title-bar"><text class="card-title">完成注册</text></view>
            <text class="auth-hint">请使用微信信息完成注册</text>
            <button class="avatar-btn" open-type="chooseAvatar" @chooseavatar="onChooseAvatar">
                <view class="avatar-circle">
                    <image v-if="photo" class="avatar-img" :src="photo" mode="aspectFill"></image>
                    <text v-else class="avatar-placeholder">📷</text>
                </view>
                <text class="avatar-tip">{{ photo ? '点击更换头像' : '点击选择头像' }}</text>
            </button>
            <input class="form-input nickname-input" type="nickname" @input="onNicknameInput" placeholder="点击填入微信昵称" :value="nickname" />
            <button class="btn" @tap="register">立即注册成为司机</button>
        </view>

        <view class="dev-area" v-if="showDev">
            <text class="dev-title">开发测试模式</text>
            <input class="dev-input" v-model="devId" placeholder="输入测试ID（如：driver1）" />
            <button class="dev-btn" size="mini" @tap="devRegister">测试注册</button>
        </view>
        <text class="dev-toggle" @tap="toggleDev">· · ·</text>
        <u-toast ref="uToast" />
    </view>
</template>

<script>
export default {
    data() {
        return {
            nickname: '',
            photo: '',
            devId: '',
            showDev: false
        };
    },
    methods: {
        onChooseAvatar(e) { this.photo = e.detail.avatarUrl; },
        onNicknameInput(e) { this.nickname = e.detail.value; },
        goBack() { uni.navigateBack({ delta: 1 }); },
        toggleDev() { this.showDev = !this.showDev; },
        register() {
            let that = this;
            if (!that.nickname) {
                that.$refs.uToast.show({ title: '请输入您的昵称', type: 'warning' });
                return;
            }
            uni.login({
                provider: 'weixin',
                success: function(resp) {
                    let data = { code: resp.code, nickname: that.nickname, photo: that.photo };
                    that.ajax(that.url.registerNewDriver, 'POST', data, function(resp) {
                        let token = resp.data.token;
                        uni.setStorageSync('token', token);
                        uni.setStorageSync('realAuth', 1);
                        that.$refs.uToast.show({
                            title: '注册成功',
                            type: 'success',
                            callback: function() {
                                uni.redirectTo({ url: '../../identity/filling/filling?mode=create' });
                            }
                        });
                    });
                }
            });
        },
        devRegister() {
            let that = this;
            if (!that.devId) {
                that.$refs.uToast.show({ title: '请输入测试ID', type: 'warning' });
                return;
            }
            let data = {
                code: 'dev_driver_' + that.devId,
                nickname: that.nickname || ('测试司机' + that.devId),
                photo: that.photo
            };
            that.ajax(that.url.registerNewDriver, 'POST', data, function(resp) {
                let token = resp.data.token;
                uni.setStorageSync('token', token);
                uni.setStorageSync('realAuth', 1);
                that.$refs.uToast.show({
                    title: '测试注册成功',
                    type: 'success',
                    callback: function() {
                        uni.redirectTo({ url: '../../identity/filling/filling?mode=create' });
                    }
                });
            });
        }
    }
};
</script>

<style lang="less">
@import url('register.less');
.back-btn {
    position: fixed;
    top: 88rpx;
    left: 30rpx;
    z-index: 100;
    background: rgba(0, 0, 0, 0.35);
    color: #fff;
    font-size: 28rpx;
    padding: 10rpx 24rpx;
    border-radius: 30rpx;
}
</style>
