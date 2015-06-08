/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 * 
 *  提示：如何获取安全校验码和合作身份者id
 *  1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *  2.点击“商家服务”(https://b.alipay.com/order/myorder.htm)
 *  3.点击“查询合作者身份(pid)”、“查询安全校验码(key)”
 */

package com.alipay.android.msp;

//
//请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
//这里签名时，只需要使用生成的RSA私钥。
//Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys
{
	// 合作身份者id，以2088开头的16位纯数字
	public static final String DEFAULT_PARTNER = "2088211062632388";
	
	// 收款支付宝账号
	public static final String DEFAULT_SELLER = "2880265591@qq.com";
	
	// 商户私钥，自助生成
	/*
	 * public static final String PRIVATE = "MIICXAIBAAKBgQCXRUzeW3" +
	 * "3n0dGjrHcFVCLaUmjqubSOo79usPvNjRZYieZjmjYcuW6B4qTYZYacNcuePPALrLKKRKaiW6f0cS1be1u1YLuYQfVIq" +
	 * "/PLfFC+yR7Yu0Ptuy/TC1Xy1FjW0tOT2YzVTWS5sgXUBa1+2R+xabaOdhus/J8Wv9x7v8HC0QIDAQAB" +
	 * "AoGAI3lr+mVIVHn/CLwkOUqu1bYnxowATMcX2JSGqICisea88bSzZe8SfEEzWVAN" +
	 * "zkpLUvOFc6dvQz+wODj4XTnBYetx9rhvILXx+jt7QPsNFv29qvy+yn7bWbhE2g+f" +
	 * "vk7sO6Y7cgGXc2YO4hjwI1zIO/CTra9XXcgMmepLdSkDTNUCQQDGNvbGCajU7Yyb" +
	 * "NK2mpYmy4yFADSzLQQh9lejAVcPqpwlBMDbXQ1+6rs0j7SqchzA8ikiKpnWKzN2y" +
	 * "7AfzAOWDAkEAw17XUQYqtOSvf5tYsfBEJcNgBpG6y/TYQITrI2xDatrld63q2XKj" +
	 * "Jey0CIqSfCHDG45pSDPi2pp5CA12CmjaGwJBAKWSVCCpAPacV89t+YAy1wsu3x95" +
	 * "4ekGsej1yC6hdb4VOY3SkHzQMq7mRUW1XtrRsmFfwajP02/WIXrL3vgFz7ECQC7L" +
	 * "/uNHiN8/j9IPEdW9aXVqMQwhyA6mnU4HCJO5kQR9Xk2meQSQ8wdYhV8uYhwdsjBd" +
	 * "klZbJ1GZgrgtEUM5DlUCQFX55xcppT1cOLVhi3ZC4m3e7sHz4l6OKeAjc8yKEZKX" +
	 * "uui3saB2qzuDMTypBe+mHO0cVRWxd9qpPHXvi+EMmTo=";
	 */
	
	public static final String PRIVATE = "MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBANlirYMMg5X2Bg+x"
			+ "U3axfeCIR4XlGlYIBEewTqWCS98+27AQfGe1zsH704SGKmRsK09QizjJijxenHzz"
			+ "DXs8cdsxdEBRjERCyRcP5ZElANaIWbaTF1A5Jii3XSBmz+OITFtfzfWI4WSKhQbD"
			+ "AlN/TcbO//qk4ai49YEaxT9XX7lVAgMBAAECgYEAyYskZYC/qtQ7UGdOg5gRpY+U"
			+ "CD6dyS+LkE+Uyvxfja5Ajh7qyFiWReFNfOvK9+oVyWZafZ7VbB0npuzNDfm/cA0Y"
			+ "rOy2T1i5+IHGjG5mQ5E8Iee4+baTk69mWWMYPPJC2C3P+ft8QR3utxJJEaWftfr2"
			+ "1JSbaX1WivX+N5AS8S0CQQDuRaXrlG0VUSnzH/4MZjRA407MbE2pOxwG7prC8m8e"
			+ "OVCJcqing6vrC7wb0W40KZIDyrgA+3mWfXnjNkLgi40jAkEA6Y80NYLDWZnHTc8v"
			+ "IQk25hEK/RyifnhPQiACaKZTEIXo8pOBcmSS1W3aSUk0z6M2IoyQhi3XoZLLJ7qL"
			+ "XxfzJwJBAMbV9EoNSKJ7YshysCxktcJYbg+FaaGJpC/cueVpLnVIWv74Yem4paS1"
			+ "3DpSoOaCskjbyIV5VdPRzf9f7ps3mYUCQQDOk/kLs2Lxp1BfA/edcrb5+u0LPv6C"
			+ "egswY3t98E7b+HU3yTvfYBF3cTh3RabOWH+TQ1y5MWbd5CaINzj6nQZVAkEAzds+"
			+ "NOP1F4Z8Z8Ritjpt1tI+kZGOq5u2rhw/kqj5WAE34Oq2vkPXmNeJbkbu3aPB8mZh" + "RXyQrv0ZU6EMH6FvPQ==";
	
	public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCukY06avjshtdG8DO2SDVIJZDRDQIqsxD8H+7KbAaPCvTgtdnzX035nlQSFu/KDvuEefNzj2T4GE8Hezkj0McRdJ+zanrssPLuy0Yo1TW5fAi8FsWcH3WOZQr+2B3NHDUkaED+Cn6HmxdKKK7siV4JBCwWueSAICW9nMuC6tRRZwIDAQAB";
}
