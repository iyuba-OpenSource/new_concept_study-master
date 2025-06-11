package com.jn.iyuba.concept.simple.model;


import com.jn.iyuba.concept.simple.db.ConceptWord;
import com.jn.iyuba.concept.simple.entity.DubbingPublish;
import com.jn.iyuba.concept.simple.entity.ExamRecordPost;
import com.jn.iyuba.concept.simple.model.bean.AdEntryBean;
import com.jn.iyuba.concept.simple.model.bean.AlipayOrderBean;
import com.jn.iyuba.concept.simple.model.bean.AudioRankingBean;
import com.jn.iyuba.concept.simple.model.bean.BookBean;
import com.jn.iyuba.concept.simple.model.bean.ClockInLogBean;
import com.jn.iyuba.concept.simple.model.bean.ConceptExerciseBean;
import com.jn.iyuba.concept.simple.model.bean.DelEvalBean;
import com.jn.iyuba.concept.simple.model.bean.DubbingPublishBean;
import com.jn.iyuba.concept.simple.model.bean.DubbingRankBean;
import com.jn.iyuba.concept.simple.model.bean.EvalBean;
import com.jn.iyuba.concept.simple.model.bean.JpQQBean;
import com.jn.iyuba.concept.simple.model.bean.JpQQBean2;
import com.jn.iyuba.concept.simple.model.bean.LessonDetailBean;
import com.jn.iyuba.concept.simple.model.bean.LogoffBean;
import com.jn.iyuba.concept.simple.model.bean.MergeBean;
import com.jn.iyuba.concept.simple.model.bean.MoreInfoBean;
import com.jn.iyuba.concept.simple.model.bean.MyDubbingBean;
import com.jn.iyuba.concept.simple.model.bean.MyTimeBean;
import com.jn.iyuba.concept.simple.model.bean.PayResultBean;
import com.jn.iyuba.concept.simple.model.bean.PdfBean;
import com.jn.iyuba.concept.simple.model.bean.PointReadingBean;
import com.jn.iyuba.concept.simple.model.bean.ProcessBean;
import com.jn.iyuba.concept.simple.model.bean.PublishEvalBean;
import com.jn.iyuba.concept.simple.model.bean.RankingDetailsBean;
import com.jn.iyuba.concept.simple.model.bean.ScoreBean;
import com.jn.iyuba.concept.simple.model.bean.TitleBean;
import com.jn.iyuba.concept.simple.model.bean.UpdateBTBean;
import com.jn.iyuba.concept.simple.model.bean.UpdateTestRecordBean;
import com.jn.iyuba.concept.simple.model.bean.UserBean;
import com.jn.iyuba.concept.simple.model.bean.WXOrderBean;
import com.jn.iyuba.concept.simple.model.bean.WordBean;
import com.jn.iyuba.concept.simple.model.bean.WxLoginBean;
import com.jn.iyuba.concept.simple.model.bean.home.ReadSubmitBean;
import com.jn.iyuba.concept.simple.model.bean.home.WordCollectBean;
import com.jn.iyuba.concept.simple.model.bean.me.RewardBean;
import com.jn.iyuba.concept.simple.model.bean.me.SuggestBean;
import com.jn.iyuba.concept.simple.model.bean.me.WordCollectListBean;
import com.jn.iyuba.concept.simple.model.bean.me.WordPdfBean;
import com.jn.iyuba.concept.simple.model.bean.sync.SyncEvalBean;
import com.jn.iyuba.concept.simple.model.bean.sync.SyncExerciseBean;
import com.jn.iyuba.concept.simple.model.bean.sync.SyncListenBean;
import com.jn.iyuba.concept.simple.model.bean.sync.SyncWordBean;
import com.jn.iyuba.novel.model.bean.NovelBookBean;
import com.jn.iyuba.novel.model.bean.NovelSentenceBean;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiServer {


    /**
     * @param language
     * @param book
     * @param flg
     * @return
     */
    @GET("/concept/getConceptTitle.jsp")
    Observable<TitleBean> getConceptTitle(@Query("language") String language, @Query("book") int book, @Query("flg") int flg);

    //http://api.qomolama.cn/concept/getConceptSentence.jsp?voaid=1001

    /**
     * 获取课文句子
     *
     * @param voaid
     * @return
     */
    @GET("/concept/getConceptSentence.jsp")
    Observable<LessonDetailBean> getConceptSentence(@Query("voaid") int voaid);


    /**
     * 获取某个课本的单词
     *
     * @param url
     * @param book
     * @return
     */
    @GET
    Observable<WordBean> getConceptWord(@Url String url, @Query("book") int book);


    /**
     * 评测
     *
     * @param requestBody
     * @return
     */
    @POST
    Observable<EvalBean> eval(@Url String url, @Body RequestBody requestBody);


    /**
     * 注册
     *
     * @param protocol
     * @param mobile
     * @param username
     * @param password
     * @param platform
     * @param appid
     * @param app
     * @param format
     * @param sign
     * @return
     */
    @GET
    Observable<UserBean.UserinfoDTO> register(@Url String url, @Query("protocol") int protocol, @Query("mobile") String mobile, @Query("username") String username,
                                              @Query("password") String password, @Query("platform") String platform, @Query("appid") int appid,
                                              @Query("app") String app, @Query("format") String format, @Query("sign") String sign);


    /**
     * 根据手机号码和密码登录
     *
     * @param protocol 10010
     * @param appid    241
     * @param username 手机号码或者用户名
     * @param password 密码
     * @param x
     * @param y
     * @return
     */
    @GET
    Observable<UserBean.UserinfoDTO> login(@Url String url, @Query("protocol") int protocol, @Query("appid") int appid, @Query("username") String username,
                                           @Query("password") String password, @Query("x") int x,
                                           @Query("y") int y, @Query("sign") String sign);


    /**
     * 获取音频排行榜
     *
     * @param topic
     * @param topicid
     * @param uid
     * @param type
     * @param start
     * @param total
     * @param sign
     * @return
     */
    @GET
    Observable<AudioRankingBean> getTopicRanking(@Url String url, @Query("topic") String topic, @Query("topicid") int topicid, @Query("uid") int uid,
                                                 @Query("type") String type, @Query("start") int start, @Query("total") int total,
                                                 @Query("sign") String sign);


    /**
     * 获取某个指定用户的已发布音频评测接口(单句+合成)
     *
     * @param uid
     * @param topic
     * @param topicId
     * @param shuoshuoType
     * @param sign
     * @return
     */
    @GET
    Observable<RankingDetailsBean> getWorksByUserId(@Url String url, @Query("uid") int uid, @Query("topic") String topic, @Query("topicId") int topicId,
                                                    @Query("shuoshuoType") String shuoshuoType, @Query("sign") String sign);


    /**
     * 获取支付宝订单
     *
     * @param app_id
     * @param userId
     * @param code
     * @param WIDtotal_fee
     * @param amount
     * @param product_id
     * @param WIDbody
     * @param WIDsubject
     * @return
     */
    @GET
    Observable<AlipayOrderBean> alipayOrder(@Url String url, @Query("app_id") int app_id, @Query("userId") int userId, @Query("code") String code,
                                            @Query("WIDtotal_fee") String WIDtotal_fee, @Query("amount") int amount,
                                            @Query("product_id") int product_id, @Query("WIDbody") String WIDbody,
                                            @Query("WIDsubject") String WIDsubject);


    /**
     * 获取微信支付订单
     *
     * @param url
     * @param wxkey
     * @param appid
     * @param weixinApp
     * @param uid
     * @param money
     * @param amount
     * @param productid
     * @param sign
     * @param body
     * @return
     */
    @GET
    Observable<WXOrderBean> weixinPay(@Url String url, @Query("wxkey") String wxkey, @Query("appid") String appid,
                                      @Query("weixinApp") String weixinApp, @Query("uid") String uid, @Query("money") String money,
                                      @Query("amount") String amount, @Query("productid") String productid,
                                      @Query("sign") String sign, @Query("body") String body, @Query("format") String format);


    /**
     * 更新订单状态
     *
     * @param data
     * @return
     */
    @GET
    Observable<PayResultBean> notifyAliNew(@Url String url, @Query("data") String data);


    /**
     * 获取更多的用户数据
     *
     * @param platform
     * @param protocol
     * @param id
     * @param myid
     * @param appid
     * @param sign
     * @return
     */
    @GET
    Observable<MoreInfoBean> getMoreInfo(@Url String url, @Query("platform") String platform, @Query("protocol") int protocol,
                                         @Query("id") int id, @Query("myid") int myid,
                                         @Query("appid") int appid, @Query("sign") String sign);


    /**
     * 注销
     *
     * @param protocol
     * @param username
     * @param password
     * @param format
     * @param sign
     * @return
     */
    @GET
    Observable<LogoffBean> logoff(@Url String url, @Query("protocol") int protocol, @Query("username") String username,
                                  @Query("password") String password, @Query("format") String format,
                                  @Query("sign") String sign);


    /**
     * 获取pdf文件
     *
     * @param voaid
     * @param type
     * @return
     */
    @GET("/iyuba/getConceptPdfFile.jsp")
    Observable<PdfBean> getConceptPdfFile(@Query("voaid") int voaid, @Query("type") int type);


    /**
     * 获取QQ群
     *
     * @param type
     * @return
     */
    @GET
    Observable<JpQQBean2> getQQGroup(@Url String url, @Query("type") String type);


    /**
     * 获取QQ
     *
     * @param appid 自定义的appid
     * @return
     */
    @GET
    Observable<JpQQBean> getJpQQ(@Url String url, @Query("appid") int appid);


    /**
     * 发布音频
     *
     * @param topic
     * @param content
     * @param format
     * @param idIndex
     * @param paraid
     * @param platform
     * @param protocol
     * @param score
     * @param shuoshuotype
     * @param userid
     * @param name
     * @param voaid
     * @return
     */
    @POST
    @FormUrlEncoded
    Observable<PublishEvalBean> publishEval(@Url String url, @Field("topic") String topic, @Field("content") String content, @Field("format") String format,
                                            @Field("idIndex") int idIndex, @Field("paraid") int paraid, @Field("platform") String platform,
                                            @Field("protocol") int protocol, @Field("score") int score, @Field("shuoshuotype") int shuoshuotype,
                                            @Field("userid") String userid, @Field("name") String name, @Field("voaid") String voaid,
                                            @Field("rewardVersion") int rewardVersion, @Field("appid") int appid);


    /**
     * 获取青少年课本
     *
     * @param URL
     * @param type
     * @param category
     * @param appid
     * @param uid
     * @param sign
     * @return
     */
    @GET
    Observable<BookBean> getTitleBySeries(@Url String URL, @Query("type") String type, @Query("category") int category
            , @Query("appid") int appid, @Query("uid") int uid, @Query("sign") String sign);


    /**
     * 青少版获取标题列表
     *
     * @param URL
     * @param type
     * @param appid
     * @param uid
     * @param sign
     * @param seriesid
     * @return
     */
    @GET
    Observable<TitleBean> getTitleBySeriesid(@Url String URL, @Query("type") String type, @Query("appid") int appid
            , @Query("uid") int uid, @Query("sign") String sign, @Query("seriesid") int seriesid);


    /**
     * 青少版 获取句子
     *
     * @param url
     * @param voaid
     * @return
     */
    @GET
    Observable<LessonDetailBean> textExamApi(@Url String url, @Query("voaid") int voaid);


    /**
     * 青少版  获取pdf文件
     *
     * @param url
     * @param type
     * @param voaid
     * @param isenglish
     * @return
     */
    @GET
    Observable<PdfBean> getVoaPdfFile_new(@Url String url, @Query("type") String type, @Query("voaid") int voaid, @Query("isenglish") int isenglish);


    /**
     * 获取微信小程序的token
     *
     * @param platform
     * @param format
     * @param protocol
     * @param appid
     * @param sign
     * @return
     */
    @GET
    Observable<WxLoginBean> getWxAppletToken(@Url String url, @Query("platform") String platform, @Query("format") String format,
                                             @Query("protocol") String protocol, @Query("appid") String appid,
                                             @Query("sign") String sign);


    /**
     * 通过获取token获取uid
     *
     * @param platform
     * @param format
     * @param protocol
     * @param token
     * @return
     */
    @GET
    Observable<WxLoginBean> getUidByToken(@Url String url, @Query("platform") String platform, @Query("format") String format,
                                          @Query("protocol") String protocol, @Query("token") String token, @Query("sign") String sign,
                                          @Query("appid") String appid);


    /**
     * 合成
     * /test/merge/  最后的/不能省略，省略后不
     *
     * @param audios
     * @param type
     * @return
     */
    @POST
    @FormUrlEncoded
    Observable<MergeBean> merge(@Url String url, @Field("audios") String audios, @Field("type") String type);


    /**
     * 获取广告
     *
     * @param appId
     * @param flag  2 广告顺序  5自家广告内容
     * @param uid
     * @return
     */
    @GET
    Observable<List<AdEntryBean>> getAdEntryAll(@Url String url, @Query("appId") String appId, @Query("flag") int flag, @Query("uid") String uid);


    /**
     * 上传学习记录（语音）
     *
     * @param format
     * @param uid
     * @param BeginTime
     * @param EndTime
     * @param Lesson
     * @param TestMode
     * @param TestWords
     * @param platform
     * @param appId
     * @param DeviceId
     * @param LessonId
     * @param sign
     * @return
     */
    @GET
    Observable<ResponseBody> updateStudyRecordNew(@Url String url, @Query("format") String format, @Query("uid") String uid,
                                                  @Query("BeginTime") String BeginTime, @Query("EndTime") String EndTime,
                                                  @Query("Lesson") String Lesson, @Query("TestMode") String TestMode,
                                                  @Query("TestWords") String TestWords, @Query("platform") String platform,
                                                  @Query("appId") String appId, @Query("DeviceId") String DeviceId,
                                                  @Query("LessonId") String LessonId, @Query("sign") String sign,
                                                  @Query("EndFlg") int EndFlg, @Query("TestNumber") int TestNumber,
                                                  @Query("rewardVersion") int rewardVersion);


    /**
     * 获取打卡界面的信息
     *
     * @param uid
     * @param day
     * @param flg
     * @return
     */
    @GET
    Observable<MyTimeBean> getMyTime(@Url String url, @Query("uid") String uid, @Query("day") int day, @Query("flg") int flg);


    /**
     * 获取积分
     *
     * @param srid
     * @param mobile
     * @param flag
     * @param uid
     * @param appid
     * @return
     */
    @GET
    Observable<ScoreBean> updateScore(@Url String url, @Query("srid") int srid, @Query("mobile") int mobile, @Query("flag") String flag,
                                      @Query("uid") String uid, @Query("appid") int appid);


    /**
     * 获取积分(分享使用)
     *
     * @param srid
     * @param mobile
     * @param flag
     * @param uid
     * @param appid
     * @return
     */
    @GET
    Observable<ScoreBean> updateScore(@Url String url, @Query("srid") int srid, @Query("mobile") int mobile, @Query("flag") String flag,
                                      @Query("uid") String uid, @Query("appid") int appid, @Query("idindex") String idindex);


    /**
     * 获取打卡记录
     *
     * @param uid
     * @param appId
     * @param time
     * @return
     */
    @GET
    Observable<ClockInLogBean> getShareInfoShow(@Url String url, @Query("uid") String uid, @Query("appId") int appId, @Query("time") String time);


    /**
     * 练习题提交接口
     *
     * @param format
     * @param appName
     * @param sign
     * @param uid
     * @param appId
     * @param TestMode
     * @param DeviceId
     * @return
     */
    @GET
    Observable<UpdateTestRecordBean> updateTestRecordNew(@Url String url, @Query("format") String format, @Query("appName") String appName, @Query("sign") String sign,
                                                         @Query("uid") String uid, @Query("appId") String appId, @Query("TestMode") int TestMode,
                                                         @Query("DeviceId") String DeviceId, @Query("jsonStr") String jsonStr);


    /**
     * 获取
     *
     * @param bookNum
     * @return
     */
    @GET("/concept/getConceptExercise.jsp")
    Observable<ConceptExerciseBean> getConceptExercise(@Query("bookNum") int bookNum);


    /**
     * 书虫和剑桥
     * 获取句子
     *
     * @param types
     * @param level
     * @param from
     * @return
     */
    @GET
    Observable<NovelSentenceBean> getStroryInfo(@Url String url, @Query("types") String types, @Query("level") int level,
                                                @Query("orderNumber") String orderNumber, @Query("chapterOrder") String chapterOrder,
                                                @Query("from") String from);


    /**
     * 获取剑桥和书虫的pdf
     *
     * @param url
     * @param voaid
     * @param type
     * @param language
     * @return
     */
    @GET
    Observable<PdfBean> getBookWormPdf(@Url String url, @Query("voaid") String voaid, @Query("type") String type, @Query("language") String language);


    /**
     * 获取是否在审核状态
     *
     * @param appId
     * @param appVersion
     * @return
     */
    @GET("/getRegisterAll.jsp")
    Observable<ProcessBean> getRegisterAll(@Query("appId") String appId, @Query("appVersion") String appVersion);


    /**
     * 获取配音排行榜
     *
     * @param platform
     * @param format
     * @param protocol
     * @param voaid
     * @param pageNumber
     * @param pageCounts
     * @param sort
     * @param topic
     * @param selectType
     * @return
     */
    @GET
    Observable<DubbingRankBean> getDubbingRank(@Url String url, @Query("platform") String platform, @Query("format") String format,
                                               @Query("protocol") int protocol, @Query("voaid") String voaid,
                                               @Query("pageNumber") int pageNumber, @Query("pageCounts") int pageCounts,
                                               @Query("sort") int sort, @Query("topic") String topic,
                                               @Query("selectType") int selectType);


    /**
     * 发布配音
     *
     * @param protocol       60002
     * @param content        3
     * @param userid
     * @param dubbingPublish 整合参数
     * @return
     */
    @POST
    Observable<DubbingPublishBean> publishDubbing(@Url String url, @Query("protocol") int protocol, @Query("content") int content
            , @Query("userid") String userid, @Body DubbingPublish dubbingPublish);


    /**
     * 获取我的配音列表
     *
     * @param appid
     * @param uid
     * @param appname
     * @return
     */
    @GET
    Observable<MyDubbingBean> getTalkShowOtherWorks(@Url String url, @Query("appid") int appid, @Query("uid") String uid, @Query("appname") String appname);


    /**
     * 删除
     *
     * @param protocol
     * @param id
     * @return
     */
    @GET
    Observable<DelEvalBean> delEvalAndDubbing(@Url String url, @Query("protocol") int protocol, @Query("id") int id);


    /**
     * 数据同步
     * 获取上传的听力进度接口
     *
     * @param format
     * @param uid
     * @param Pageth
     * @param NumPerPage
     * @param TestMode
     * @param sign
     * @param Lesson     biaori
     * @return
     */
    @GET
    Observable<SyncListenBean> getStudyRecordByTestMode(@Url String url, @Query("format") String format, @Query("uid") int uid, @Query("Pageth") int Pageth,
                                                        @Query("NumPerPage") int NumPerPage, @Query("TestMode") int TestMode, @Query("sign") String sign,
                                                        @Query("Lesson") String Lesson);

    /**
     * 获取评测句子的同步数据
     *
     * @param userId
     * @param newstype
     * @return
     */
    @GET
    Observable<SyncEvalBean> getTestRecord(@Url String url, @Query("userId") String userId, @Query("newstype") String newstype);


    /**
     * 数据同步
     * 获取单词数据
     *
     * @param appId
     * @param uid
     * @param lesson
     * @param TestMode
     * @param mode
     * @param sign
     * @param format
     * @return
     */
    @GET
    Observable<SyncWordBean> getExamDetail(@Url String url, @Query("appId") int appId, @Query("uid") String uid, @Query("lesson") String lesson,
                                           @Query("TestMode") String TestMode, @Query("mode") int mode, @Query("sign") String sign,
                                           @Query("format") String format);


    /**
     * 上传单词做题记录
     *
     * @param bean
     * @return
     */
    @POST
    Observable<UpdateBTBean> updateExamRecord(@Url String url, @Body ExamRecordPost bean);


    /**
     * 获取青少版单词
     *
     * @param bookid
     * @return
     */
    @GET("/iyuba/getWordByUnit.jsp")
    Observable<WordBean> getWordByUnit(@Query("bookid") String bookid);


    /**
     * 获取练习的数据
     *
     * @param appId
     * @param uid
     * @param TestMode
     * @param sign
     * @param format
     * @param Pageth
     * @param NumPerPage
     * @return
     */
    @GET
    Observable<SyncExerciseBean> getTestRecordDetail(@Url String url, @Query("appId") String appId, @Query("uid") String uid, @Query("TestMode") String TestMode,
                                                     @Query("sign") String sign, @Query("format") String format, @Query("Pageth") String Pageth,
                                                     @Query("NumPerPage") String NumPerPage);

//PointReadingBean

    /**
     * 获取点读的数据
     *
     * @param format
     * @param voaid
     * @return
     */
    @GET
    Observable<PointReadingBean> textExamApi(@Url String url, @Query("format") String format, @Query("voaid") String voaid);


    /**
     * 获取单词信息
     *
     * @param q
     * @return
     */
    @GET
    Observable<ResponseBody> apiWord(@Url String url, @Query("q") String q);


    /**
     * 收藏单词
     *
     * @param groupName Iyuba
     * @param mod       insert
     * @param word
     * @param userId
     * @param format    Iyuba
     * @return
     */
    @GET
    Observable<WordCollectBean> updateWord(@Url String url, @Query("groupName") String groupName, @Query("mod") String mod,
                                           @Query("word") String word, @Query("userId") String userId, @Query("format") String format);


    /**
     * 获取收藏单词的列表
     *
     * @param url
     * @param u
     * @param pageNumber
     * @param pageCounts
     * @param format
     * @return
     */
    @GET
    Observable<WordCollectListBean> wordListService(@Url String url, @Query("u") String u, @Query("pageNumber") int pageNumber
            , @Query("pageCounts") int pageCounts, @Query("format") String format);


    /**
     * 获取单词pdf   链接地址
     *
     * @param u
     * @param pageNumber
     * @param pageCounts
     * @return
     */
    @GET
    Observable<WordPdfBean> getWordToPDF(@Url String url, @Query("u") int u, @Query("pageNumber") int pageNumber, @Query("pageCounts") int pageCounts);


    /**
     * 版本更新
     *
     * @param url
     * @param currver
     * @param packageStr
     * @return
     */
    @GET
    Observable<ResponseBody> islatest(@Url String url, @Query("currver") int currver, @Query("package") String packageStr);


    /**
     * 提交阅读记录
     *
     * @param url
     * @param format
     * @param uid
     * @param BeginTime
     * @param EndTime
     * @param appName
     * @param Lesson
     * @param LessonId
     * @param appId
     * @param Device
     * @param DeviceId
     * @param EndFlg
     * @param wordcount
     * @param categoryid
     * @param platform
     * @return
     */
    @GET
    Observable<ReadSubmitBean> updateNewsStudyRecord(@Url String url, @Query("format") String format, @Query("uid") int uid, @Query("BeginTime") String BeginTime,
                                                     @Query("EndTime") String EndTime, @Query("appName") String appName, @Query("Lesson") String Lesson, @Query("LessonId") int LessonId,
                                                     @Query("appId") int appId, @Query("Device") String Device, @Query("DeviceId") String DeviceId, @Query("EndFlg") int EndFlg,
                                                     @Query("wordcount") int wordcount, @Query("categoryid") int categoryid, @Query("platform") String platform,
                                                     @Query("rewardVersion") int rewardVersion);


    /**
     * 意见反馈
     *
     * @param url
     * @param protocol
     * @param uid
     * @param content
     * @param email
     * @param app
     * @param platform
     * @return
     */
    @GET
    Observable<SuggestBean> suggest(@Url String url, @Query("protocol") int protocol, @Query("uid") int uid,
                                    @Query("content") String content, @Query("email") String email,
                                    @Query("app") String app, @Query("platform") String platform);


    /**
     * 我的钱包
     *
     * @param uid
     * @param pages
     * @param pageCount
     * @param sign
     * @return
     */
    @GET
    Observable<RewardBean> getUserActionRecord(@Url String url, @Query("uid") int uid, @Query("pages") int pages, @Query("pageCount") int pageCount, @Query("sign") String sign);


}
