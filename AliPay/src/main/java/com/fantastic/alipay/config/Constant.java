package com.fantastic.alipay.config;

/**
 * 常量类
 *
 * @author jidaojiuyou
 * @since 2021-05-20
 */
@SuppressWarnings("unused")
public class Constant {
    /**
     * 错误码
     */
    public static class ErrCode {
        public static final int PAY_SUCCESS = 9000;
        public static final int PAY_DEALING = 8000;
        public static final int PAY_FAILED = 4000;
        public static final int REPEAT_REQUEST = 5000;
        public static final int PAY_CANCEL = 6001;
        public static final int PAY_NETWORK_ERROR = 6002;
        public static final int PAY_UNKNOWN_ERROR = 6004;
        public static final int PARSE_FAILED = 0;
        public static final int PAY_PARAMETERS_ERROR = -1;
        public static final int PAY_OTHER_ERROR = 1;
    }

    /**
     * 错误说明
     */
    public static class ErrMsg {
        public static final String PAY_SUCCESS = "支付成功";
        public static final String PAY_DEALING = "正在处理中";
        public static final String PAY_FAILED = "支付失败";
        public static final String REPEAT_REQUEST = "重复请求";
        public static final String PAY_CANCEL = "支付取消";
        public static final String PAY_NETWORK_ERROR = "网络连接异常";
        public static final String PAY_UNKNOWN_ERROR = "支付结果未知";
        public static final String PARSE_FAILED = "解析失败";
        public static final String PAY_PARAMETERS_ERROR = "支付参数异常";
        public static final String PAY_OTHER_ERROR = "其他错误";
    }
}