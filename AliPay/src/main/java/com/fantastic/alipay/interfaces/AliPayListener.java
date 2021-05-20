package com.fantastic.alipay.interfaces;

/**
 * @author jidaojiuyou
 * @since 2021-05-20
 */
@SuppressWarnings("unused")
public interface AliPayListener {
    /**
     * 支付成功
     */
    void onPaySuccess();

    /**
     * 支付失败
     *
     * @param errCode 错误码
     * @param message 错误消息
     */
    void onPayError(int errCode, String message);

    /**
     * 支付取消
     */
    void onPayCancel();
}