package com.github.soyanga.springBootDemo1.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @program: springBootDemo1
 * @Description: 第三方支付的配置实例
 * @Author: SOYANGA
 * @Create: 2019-05-01 16:02
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "app.pay")
public class PayProperties {

    private Alipay alipay = new Alipay();

    private Wxpay wxpay = new Wxpay();

    /**
     * 微信支付配置
     */
    @Data
    public static class Wxpay {
        private String appId;

        /**
         * 商户平台设置的密钥
         */
        private String apiKey;

        /**
         * 商户号
         */
        private String mchId;

        /**
         * 交易类型
         */
        private String tradeType = "App";

        /**
         * 签名类型            
         */
        private String signType = "MD5";

        /**
         * 支付通知地址            
         */
        private String payNotifyUrl;


        /**
         * 退款通知接口            
         */
        private String refundNotifyUrl;
    }

    /**
     * 支付宝可配置参数
     */
    @Data
    public static class Alipay {
        private String gateway = "https//openapi.alipay.com/gateway.do";

        private String privateKey;

        private String publicKey;

        private String alipublicKey;

        private String appId;

        private String method = "alipay.trade.app.pay";

        private String format = "json";

        private String charset = "utf-8";

        private String version = "1.0";

        private String notifyUrl;
    }
}
