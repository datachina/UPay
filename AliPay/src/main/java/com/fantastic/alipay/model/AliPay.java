package com.fantastic.alipay.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.alipay.sdk.app.PayTask;
import com.fantastic.alipay.config.Constant;
import com.fantastic.alipay.interfaces.AliPayListener;

import java.util.Map;

/**
 * @author jidaojiuyou
 * @see <a href="https://docs.open.alipay.com/204/105301/">支付宝回调结果</a>
 * @since 2021-05-20
 */
@SuppressWarnings({"unused", "AlibabaClassNamingShouldBeCamel"})
public class AliPay {
    /**
     * 实例
     */
    @SuppressLint("StaticFieldLeak")
    private static AliPay aliPay;
    /**
     * 应用上下文
     */
    private final Activity context;
    /**
     * 支付监听器
     */
    private AliPayListener aliPayListener;

    /**
     * 私有化构造方法
     *
     * @param context ctx
     */
    public AliPay(Activity context) {
        this.context = context;
    }

    /**
     * 获取实例(懒汉式)
     *
     * @param context 应用上下文
     * @return UPay
     */
    public static synchronized AliPay getInstance(Activity context) {
        if (aliPay == null) {
            aliPay = new AliPay(context);
        }
        return aliPay;
    }

    /**
     * 启动支付宝并付款
     */
    @SuppressWarnings("AlibabaAvoidManuallyCreateThread")
    public void startPay(String orderInfo, AliPayListener aliPayListener) {
        this.aliPayListener = aliPayListener;
        new Thread(() -> {
            PayTask payTask = new PayTask(context);
            Map<String, String> result = payTask.payV2(orderInfo, true);
            Message msg = new Message();
            msg.obj = result;
            handler.sendMessage(msg);
        }).start();
    }

    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            // 监听器为空直接返回
            if (aliPayListener == null) {
                return;
            }
            // 结果解析失败
            if (payResult.getResultStatus() == null) {
                aliPayListener.onPayError(Constant.ErrCode.PARSE_FAILED, Constant.ErrMsg.PARSE_FAILED);
                return;
            }
            // 结果解析正常
            String resultStatus = payResult.getResultStatus();
            // 支付成功
            if (TextUtils.equals(resultStatus, String.valueOf(Constant.ErrCode.PAY_SUCCESS))) {
                aliPayListener.onPaySuccess();
                // 正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
            } else if (TextUtils.equals(resultStatus, String.valueOf(Constant.ErrCode.PAY_DEALING))) {
                aliPayListener.onPayError(Constant.ErrCode.PAY_DEALING, Constant.ErrMsg.PAY_DEALING);
                // 支付错误
            } else if (TextUtils.equals(resultStatus, String.valueOf(Constant.ErrCode.PAY_FAILED))) {
                aliPayListener.onPayError(Constant.ErrCode.PAY_FAILED, Constant.ErrMsg.PAY_FAILED);
                // 重复请求
            } else if (TextUtils.equals(resultStatus, String.valueOf(Constant.ErrCode.REPEAT_REQUEST))) {
                aliPayListener.onPayError(Constant.ErrCode.REPEAT_REQUEST, Constant.ErrMsg.REPEAT_REQUEST);
                // 支付取消
            } else if (TextUtils.equals(resultStatus, String.valueOf(Constant.ErrCode.PAY_CANCEL))) {
                aliPayListener.onPayCancel();
                // 网络连接出错
            } else if (TextUtils.equals(resultStatus, String.valueOf(Constant.ErrCode.PAY_NETWORK_ERROR))) {
                aliPayListener.onPayError(Constant.ErrCode.PAY_NETWORK_ERROR, Constant.ErrMsg.PAY_NETWORK_ERROR);
                // 支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
            } else if (TextUtils.equals(resultStatus, String.valueOf(Constant.ErrCode.PAY_UNKNOWN_ERROR))) {
                aliPayListener.onPayError(Constant.ErrCode.PAY_UNKNOWN_ERROR, Constant.ErrMsg.PAY_UNKNOWN_ERROR);
            } else {
                aliPayListener.onPayError(Constant.ErrCode.PAY_OTHER_ERROR, Constant.ErrMsg.PAY_OTHER_ERROR);
            }
        }
    };
}