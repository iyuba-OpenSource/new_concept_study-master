package com.jn.iyuba.novel;

import com.jn.iyuba.novel.db.NovelBook;

public class NovelConstant {


    /**
     * 默认非会员
     */
    public static int VIP_STATE = 0;

    public static NovelBook novelBook;

    /**
     * SP文件名
     */
    public static String SP_BOOK = "NOVEL_DATA";

    /**
     * 选择的那一本书
     */
    public static String SP_KEY_BOOK = "BOOK";

    public static String DOMAIN_QOMOLAMA = "qomolama.cn";

    public static String DOMAIN_QOMOLAMA_LONG = "qomolama.com.cn";

    public static String DOMAIN_IYUBA = "iyuba.cn";

    public static String DOMAIN_IYUBA_LONG = "iyuba.com.cn";


    public static String API_URL = "http://api." + DOMAIN_QOMOLAMA;

    public static String IUSERSPEECH_URL = "http://iuserspeech." + DOMAIN_IYUBA + ":9001";


    public static String API_COM_CN_URL = "http://api." + DOMAIN_IYUBA_LONG;


    public static String STATIC1_URL = "http://static1." + DOMAIN_QOMOLAMA;


    public static String STATIC2_URL = "http://static2." + DOMAIN_IYUBA;


    public static String STATICVIP2_URL = "http://staticvip2." + DOMAIN_IYUBA;


    public static String DAXUE_URL = "http://daxue." + DOMAIN_QOMOLAMA;


    public static String URL_DAXUE_I = "http://daxue." + DOMAIN_QOMOLAMA;

    public static String VOA_URL = "http://voa." + DOMAIN_QOMOLAMA;

    public static String URL_VIP = "http://vip." + DOMAIN_QOMOLAMA;

    public static String URL_Q_API = "http://api." + DOMAIN_QOMOLAMA;

    public static String URL_API = "http://api." + DOMAIN_IYUBA;

    public static String URL_APPS = "http://apps." + DOMAIN_IYUBA;

    public static String URL_APP = "http://app." + DOMAIN_IYUBA;

    public static String URL_M = "http://m." + DOMAIN_QOMOLAMA;

    public static String URL_M_QOMOLAMA = "http://m." + DOMAIN_IYUBA;

    public static String URL_AI = "http://ai." + DOMAIN_IYUBA;

    public static String URL_VOA = "http://voa." + DOMAIN_IYUBA;


    public static String URL_DEV = "http://dev." + DOMAIN_QOMOLAMA;
}
