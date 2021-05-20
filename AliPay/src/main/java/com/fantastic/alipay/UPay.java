package com.fantastic.alipay;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.fantastic.alipay.config.Constant;
import com.fantastic.alipay.interfaces.AliPayListener;
import com.fantastic.alipay.model.AliPay;

/**
 * 公共类
 *
 * @author jidaojiuyou
 * @since 2021-05-20
 */
@SuppressWarnings({"unused", "AlibabaClassNamingShouldBeCamel"})
public class UPay {
    /**
     * 实例
     */
    @SuppressLint("StaticFieldLeak")
    private static UPay uPay;
    /**
     * 应用上下文
     */
    private final Activity context;

    /**
     * 获取实例(懒汉式)
     *
     * @param context 应用上下文
     * @return UPay
     */
    public static synchronized UPay getInstance(Activity context) {
        if (uPay == null) {
            uPay = new UPay(context);
        }
        return uPay;
    }

    /**
     * 私有构造方法
     *
     * @param context ctx
     */
    private UPay(Activity context) {
        this.context = context;
    }

    /**
     * 调用支付宝实现方法
     *
     * @param payParameters  支付参数,此处是指orderInfo
     * @param aliPayListener 支付结果监听器
     */
    public void toAliPay(String payParameters, AliPayListener aliPayListener) {
        if (payParameters != null) {
            if (aliPayListener != null) {
                AliPay.getInstance(context).startPay(payParameters, aliPayListener);
            }
        } else {
            if (aliPayListener != null) {
                aliPayListener.onPayError(Constant.ErrCode.PAY_PARAMETERS_ERROR, Constant.ErrMsg.PAY_PARAMETERS_ERROR);
            }
        }
    }

}