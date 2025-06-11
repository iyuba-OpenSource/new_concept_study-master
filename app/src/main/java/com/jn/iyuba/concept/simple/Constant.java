package com.jn.iyuba.concept.simple;

import com.jn.iyuba.concept.simple.db.Book;
import com.jn.iyuba.concept.simple.entity.WordBook;
import com.jn.iyuba.concept.simple.model.bean.UserBean;

public class Constant {


    /**
     * 阅读速度
     */
    public final static int NORMAL_WPM = 600;

    //英音（UK）和美音（US）
    public static String LANGUAGE = "US";
    //初始化
    public static final String ACTION_INIT = "com.jn.iyuba.succinct.init";
    public final static String UMENG_KEY = "541b94aefd98c52eb3014729";

    /**
     * 是否调试支付
     */
    public final static boolean IS_PAY_DEBUG = false;
    /**
     * 用户信息
     */
    public static UserBean.UserinfoDTO userinfo;


    //记录选择的课本
    public static Book book;

    /**
     * 单词闯关记录选择的课本
     */
    public static WordBook wordBook;

    /**
     * appid
     */
    public final static int APPID = 222;


    public final static int OWERID = 21;


    /**
     * topic
     */
    public final static String TYPE = "concept";

    /**
     * 简称
     */
    public final static String NAME = "concept";

    public static String DOMAIN = "qomolama.cn";

    public static String DOMAIN_LONG = "qomolama.com.cn";

    public static String DOMAIN2 = "iyuba.cn";

    public static String DOMAIN2_LONG = "iyuba.com.cn";


    public static String API_URL = "http://api." + DOMAIN;

    public static String IUSERSPEECH_URL = "http://iuserspeech." + DOMAIN2 + ":9001";


    public static String API_COM_CN_URL = "http://api." + DOMAIN2_LONG;


    public static String STATIC1_URL = "http://static1." + DOMAIN;


    public static String DAXUE_URL = "http://daxue." + DOMAIN2;


    public static String URL_DAXUE_I = "http://daxue." + DOMAIN;

    public static String VOA_URL = "http://voa." + DOMAIN;

    public static String URL_VIP = "http://vip." + DOMAIN;

    public static String URL_Q_API = "http://api." + DOMAIN;

    public static String URL_API = "http://api." + DOMAIN2;

    public static String URL_APPS = "http://apps." + DOMAIN2;

    public static String URL_APPS2 = "https://apps." + DOMAIN2;

    public static String URL_APP = "http://app." + DOMAIN2;

    public static String URL_M = "http://m." + DOMAIN;

    public static String URL_M_QOMOLAMA = "http://m." + DOMAIN2;

    public static String URL_AI = "http://ai." + DOMAIN2;

    public static String URL_VOA = "http://voa." + DOMAIN2;

    public static String URL_DEV = "http://dev." + DOMAIN;

    public static String URL_STATIC0 = "http://static0." + DOMAIN2;

    public static String URL_STATICVIP = "http://staticvip." + DOMAIN2;

    public static String URL_WORD = "http://word." + DOMAIN2;


//    public static String get_Register_All = "http://api.qomolama.cn/getRegisterAll.jsp?appId=%@&appVersion=%@";


    /**
     * 我的钱包记录
     */
    public static String GET_USER_ACTION_RECORD = URL_API + "/credits/getuseractionrecord.jsp ";

    /**
     * 上传阅读记录
     */
    public static String URL_UPDATE_NEWS_STUDY_RECORD = URL_DAXUE_I + "/ecollege/updateNewsStudyRecord.jsp";
    /**
     * 版本更新
     */
    public static String IS_LATEST = URL_API + "/mobile/android/newconcept/islatest.plain";

    /**
     * 获取收藏单词的pdf
     */
    public static String GET_WORD_TO_PDF = URL_AI + "/management/getWordToPDF.jsp";
    /**
     * 获取单词列表
     */
    public static String WORD_LIST_SERVICE = URL_APPS2 + "/words/wordListService.jsp";

    /**
     * 收藏单词
     */
    public static String UPDATE_WORD = URL_WORD + "/words/updateWord.jsp";
    /**
     * 随机播放和顺序播放
     * 切换数据源(剑桥书虫)
     */
    public static String BROADCAST_CHAPTER = "com.jn.iyuba.succinct.chapter_switch_data";

    /**
     * 随机播放和顺序播放
     * 切换数据源(新概念)
     */
    public static String BROADCAST_TITLE = "com.jn.iyuba.succinct.title_switch_data";


    /**
     * 获取单词的释义和例句
     */
    public static String API_WORD = URL_WORD + "/words/apiWord.jsp";
    /**
     * 获取点读数据
     */
    public static String POINT_READING = URL_APPS + "/iyuba/textExamApi.jsp";

    /**
     * 同步数据
     * 获取练习数据
     */
    public static String GET_TEST_RECORD_DETAIL = DAXUE_URL + "/ecollege/getTestRecordDetail.jsp";
    /**
     * 上传做题数据
     */
    public static String UPDATE_EXAM_RECORD = DAXUE_URL + "/ecollege/updateExamRecordNew.jsp";
    /**
     * 数据同步
     * 获取单词数据
     */
    public static String GET_EXAM_DETAIL = DAXUE_URL + "/ecollege/getExamDetailNew.jsp";
    /**
     * 数据同步
     * 获取评测句子的数据
     */
    public static String GET_TEST_RECORD = Constant.URL_AI + "/management/getTestRecord.jsp";
    /**
     * 数据同步
     * 获取听力数据
     */
    public static String GET_STUDY_RECORD_BY_TEST_MODE = DAXUE_URL + "/ecollege/getStudyRecordByTestMode.jsp";
    /**
     * 获取我的配音
     */
    public static String GET_TALK_SHOW_OTHER_WORKS = Constant.URL_VOA + "/voa/getTalkShowOtherWorks.jsp";
    /**
     * 书虫或者剑桥小说馆
     * 获取pdf
     */
    public static String GET_BOOK_WORM_PDF = URL_APPS + "/book/getBookWormPdf.jsp";

    /**
     * 书虫或者剑桥小说馆
     * 获取句子
     */
    public static String GET_STRORY_INFO = URL_APPS + "/book/getStroryInfo.jsp";

    /**
     * 练习题提交
     */
    public static String UPDATE_TEST_RECORD_NEW = URL_DAXUE_I + "/ecollege/updateTestRecordNew.jsp";
    /**
     * 获取打卡记录
     */
    public static String GET_SHARE_INFO_SHOW = URL_APP + "/getShareInfoShow.jsp";
    /**
     * 获取积分
     */
    public static String UPDATE_SCORE = URL_API + "/credits/updateScore.jsp";
    /**
     * 获取打卡界面的信息
     */
    public static String GET_MY_TIME = DAXUE_URL + "/ecollege/getMyTime.jsp";
    /**
     * 上传听力记录（学习）
     */
    public static String UPDATE_STUDY_RECORD_NEW = DAXUE_URL + "/ecollege/updateStudyRecordNew.jsp";
    /**
     * 广告接口
     */
    public static String GET_AD_ENTRY_ALL = URL_DEV + "/getAdEntryAll.jsp";

    /**
     * 合成
     */
    public static String MERGE = IUSERSPEECH_URL + "/test/merge/";
    /**
     * 青少版-获取pdf
     */
    public static String GET_VOA_PDF_FILE_NEW = URL_APPS + "/iyuba/getVoapdfFile_new.jsp";
    /**
     * 青少版-获取某课的句子
     */
    public static String TEXT_EXAM_API = URL_APPS + "/iyuba/textExamApi.jsp";

    /**
     * 获取青少年课本
     * http://apps.iyuba.cn/iyuba/getTitleBySeries.jsp
     */
    public static String GET_TITLE_BY_SERIES = URL_APPS + "/iyuba/getTitleBySeries.jsp";

    /**
     * 发布配音
     */
    public static String PUBLISH_DUBBING = URL_VOA + "/voa/UnicomApi2";
    /**
     * 发布音频
     */
    public static String PUBLISH = URL_VOA + "/voa/UnicomApi";
    /**
     * 获取qq客服
     */
    public static String GET_JP_QQ = URL_AI + "/japanapi/getJpQQ.jsp";
    /**
     * 获取QQ群
     */
    public static String GET_QQ_GROUP = URL_M + "/m_login/getQQGroup.jsp";

    /**
     * 获取pdf文件
     */
    //http://api.qomolama.cn/iyuba/getConceptPdfFile.jsp?voaid=1003&type=0
    public static String GET_CONCEPT_PDF_FILE = URL_Q_API + "/iyuba/getConceptPdfFile.jsp";

    /**
     * 更新订单
     */
    public static String NOTIFY_ALI_NEW = URL_VIP + "/notifyAliNew.jsp";
    /**
     * 支付接口
     */
    public static String ALIPAY = URL_VIP + "/alipay.jsp";


    /**
     * 微信支付
     */
    public static String WEIXINPAY = URL_VIP + "/weixinPay.jsp";

    /**
     * 获取某个指定用户的已发布音频评测接口(单句+合成)
     */
    public static String GET_WORKS_BY_USER_ID = VOA_URL + "/voa/getWorksByUserId.jsp";
    /**
     * 获取音频排行榜
     */
    public static String GET_TOPIC_RANKING = DAXUE_URL + "/ecollege/getTopicRanking.jsp";
    public static String PROTOCOL = API_COM_CN_URL + "/v2/api.iyuba";

    /**
     * 获取单词数据
     */
    public static String GET_CONCEPT_WORD = "http://apps.iyuba.cn/concept/getConceptWord.jsp";


    /**
     * 评测
     */
    public static String EVAL_URL = IUSERSPEECH_URL + "/test/ai/";


    /**
     * 课文首页设置
     */
    public final static String SP_LESSON = "LESSON_HOME_SETTING";


    /**
     * 课文首页key
     */
    public final static String SP_KEY_LESSON = "LESSON_HOME";


    /**
     * 存储登录的用户信息
     */
    public final static String SP_USER = "USER";

    public final static String SP_KEY_USER_INFO = "USER_INFO";

    //保存选择的课本
    public final static String SP_KEY_BOOk = "CHOOSE_BOOK";

    /**
     * 保存单词闯关的课本
     */
    public final static String SP_KEY_WORD_BOOK = "CHOOSE_WORD_BOOK";

    //保存使用的是英音还是美音
    public final static String SP_KEY_LANGUAGE = "LANGUAGE";


    /**
     * 隐私协议的确认状态
     */
    public final static String SP_PRIVACY = "PRIVACY";

    public final static String SP_KEY_PRIVACY_STATE = "PRIVACY_STATE";

    /**
     * 引导页
     * 0: 未进入
     * 1：已进入
     */
    public final static String SP_KEY_GUIDE_STATE = "GUIDE_STATE";


    /**
     * 权限
     */
    public final static String SP_PERMISSIONS = "PERMISSIONS";


    /**
     * 录音
     * 1 拒绝
     * 0 未申请过此权限
     */
    public final static String SP_KEY_RECORD = "RECORD";


    /**
     * 通知的权限
     * 1 拒绝
     * 0 未申请过此权限
     */
    public final static String SP_KEY_NOTIFICATIONS = "NOTIFICATIONS";


    /**
     * 用户协议
     */
    public static String URL_PROTOCOLUSE;


    /**
     * 隐私政策
     */
    public static String URL_PROTOCOLPRI;

    //广告
    public static final String AD_ADS1 = "ads1";//倍孜
    public static final String AD_ADS2 = "ads2";//创见
    public static final String AD_ADS3 = "ads3";//头条穿山甲
    public static final String AD_ADS4 = "ads4";//广点通优量汇
    public static final String AD_ADS5 = "ads5";//快手
}
