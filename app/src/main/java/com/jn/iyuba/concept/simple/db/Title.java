package com.jn.iyuba.concept.simple.db;

import android.graphics.Color;
import android.os.Environment;

import androidx.room.Ignore;

import com.google.gson.annotations.SerializedName;
import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.MyApplication;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.entity.DownloadRefresh;
import com.jn.iyuba.concept.simple.util.BookUtil;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;
import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 课程
 */
public class Title extends LitePalSupport implements Serializable {
    @SerializedName(value = "listenPercentage", alternate = "listenpercentage")
    private String listenPercentage;
    @SerializedName(value = "text_num", alternate = "textnum")
    private String textNum;
    @SerializedName(value = "totalTime", alternate = "totaltime")
    private int totalTime;
    @SerializedName("titleid")
    private String titleid;
    @SerializedName(value = "end_time", alternate = "endtime")
    private String endTime;
    @SerializedName("packageid")
    private int packageid;
    @SerializedName(value = "title", alternate = "Title")
    private String title;
    @SerializedName("ownerid")
    private int ownerid;
    @SerializedName("price")
    private String price;
    @SerializedName(value = "voa_id", alternate = "voaid")
    private String voaId;
    @SerializedName("percentage")
    private String percentage;
    @SerializedName(value = "title_cn", alternate = {"titlecn", "Title_cn"})
    private String titleCn;
    @SerializedName(value = "choice_num", alternate = "choicenum")
    private String choiceNum;
    @SerializedName("name")
    private String name;
    @SerializedName(value = "wordNum", alternate = "wordnum")
    private int wordNum;
    @SerializedName("categoryid")
    private int categoryid;
    @SerializedName("desc")
    private String desc;


    /**
     * 代表英音或者美音
     * 只对新概念英语有有用
     */
    @Column(defaultValue = "US")
    private String language;

    /**
     * 图片
     */
    @SerializedName(value = "pic", alternate = "Pic")
    private String pic;

    /**
     * 是否通过
     */
    @Column(ignore = true)
    private boolean pass;

    /**
     * 正确个数
     */
    @Column(ignore = true)
    private int tNum;

    /**
     * 课本id
     */
    private int bookid;


    //***************青少年
    @SerializedName("CreatTime")
    private String creatTime;
    @SerializedName(value = "Category", alternate = "creattime")
    private String category;
    @SerializedName(value = "havePractice", alternate = "havepractice")
    private String havePractice;
    @SerializedName(value = "Texts", alternate = "texts")
    private String texts;
    @SerializedName("video")
    private String video;
    @SerializedName("Pagetitle")
    private String pagetitle;
    @SerializedName("Url")
    private String url;
    @SerializedName(value = "PublishTime", alternate = "publishtime")
    private String publishTime;
    @SerializedName(value = "HotFlg", alternate = "hotflg")
    private String hotFlg;
    @SerializedName(value = "clickRead", alternate = "clickread")
    private String clickRead;
    @SerializedName(value = "IntroDesc", alternate = "introdesc")
    private String introDesc;
    @SerializedName(value = "Keyword", alternate = "keyword")
    private String keyword;
    @SerializedName(value = "Sound", alternate = "sound")
    private String sound;
    @SerializedName(value = "Flag", alternate = "flag")
    private String flag;
    @SerializedName(value = "DescCn", alternate = "desccn")
    private String descCn;
    @SerializedName("classid")
    private String classid;
    @SerializedName("outlineid")
    private String outlineid;
    @SerializedName("series")
    private String series;
    @SerializedName(value = "CategoryName", alternate = "categoryname")
    private String categoryName;
    @SerializedName(value = "Id", alternate = "id")
    private int id;
    @SerializedName(value = "ReadCount", alternate = "readCount")
    private String readCount;


    /**
     * 首页进度
     * 读到第几句
     */
    @Column(defaultValue = "0")
    private int testNumber;

    /**
     * 是否播放完成
     */
    @Column(defaultValue = "0")
    private int endFlg;


    /**
     * 数据同步
     * 更新的时间，用来存储最新的数据
     */
    private String syncUpdateTime;


    /**
     * 下载进度
     */
    @Column(ignore = true)
    private int downloadProgress = -1;


    /**
     * 是否下载
     * 1 已下载  0 未下载
     */
    @Column(defaultValue = "0")
    private int upload = 0;


    public int getUpload() {
        return upload;
    }

    public void setUpload(int upload) {
        this.upload = upload;
    }

    public int getDownloadProgress() {
        return downloadProgress;
    }

    public void setDownloadProgress(int downloadProgress) {
        this.downloadProgress = downloadProgress;
    }

    public String getSyncUpdateTime() {
        return syncUpdateTime;
    }

    public void setSyncUpdateTime(String syncUpdateTime) {
        this.syncUpdateTime = syncUpdateTime;
    }

    public int getEndFlg() {
        return endFlg;
    }

    public void setEndFlg(int endFlg) {
        this.endFlg = endFlg;
    }

    public int getTestNumber() {
        return testNumber;
    }

    public void setTestNumber(int testNumber) {
        this.testNumber = testNumber;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getBookid() {
        return bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }

    public int gettNum() {
        return tNum;
    }

    public void settNum(int tNum) {
        this.tNum = tNum;
    }

    public boolean isPass() {
        return pass;
    }

    public void setPass(boolean pass) {
        this.pass = pass;
    }

    public String getListenPercentage() {
        return listenPercentage;
    }

    public void setListenPercentage(String listenPercentage) {
        this.listenPercentage = listenPercentage;
    }

    public String getTextNum() {
        return textNum;
    }

    public void setTextNum(String textNum) {
        this.textNum = textNum;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public String getTitleid() {
        return titleid;
    }

    public void setTitleid(String titleid) {
        this.titleid = titleid;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getPackageid() {
        return packageid;
    }

    public void setPackageid(int packageid) {
        this.packageid = packageid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(int ownerid) {
        this.ownerid = ownerid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVoaId() {
        return voaId;
    }

    public void setVoaId(String voaId) {
        this.voaId = voaId;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getTitleCn() {
        return titleCn;
    }

    public void setTitleCn(String titleCn) {
        this.titleCn = titleCn;
    }

    public String getChoiceNum() {
        return choiceNum;
    }

    public void setChoiceNum(String choiceNum) {
        this.choiceNum = choiceNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWordNum() {
        return wordNum;
    }

    public void setWordNum(int wordNum) {
        this.wordNum = wordNum;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getHavePractice() {
        return havePractice;
    }

    public void setHavePractice(String havePractice) {
        this.havePractice = havePractice;
    }


    public String getTexts() {
        return texts;
    }

    public void setTexts(String texts) {
        this.texts = texts;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getPagetitle() {
        return pagetitle;
    }

    public void setPagetitle(String pagetitle) {
        this.pagetitle = pagetitle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getHotFlg() {
        return hotFlg;
    }

    public void setHotFlg(String hotFlg) {
        this.hotFlg = hotFlg;
    }


    public String getClickRead() {
        return clickRead;
    }

    public void setClickRead(String clickRead) {
        this.clickRead = clickRead;
    }

    public String getIntroDesc() {
        return introDesc;
    }

    public void setIntroDesc(String introDesc) {
        this.introDesc = introDesc;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getDescCn() {
        return descCn;
    }

    public void setDescCn(String descCn) {
        this.descCn = descCn;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getOutlineid() {
        return outlineid;
    }

    public void setOutlineid(String outlineid) {
        this.outlineid = outlineid;
    }


    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReadCount() {
        return readCount;
    }

    public void setReadCount(String readCount) {
        this.readCount = readCount;
    }


    /**
     * 获取下载文件的名字
     *
     * @return
     */
    public String getDownloadName() {


        return "concept" + bookid + voaId + "US" + ".mp3";
    }

    /**
     * 下载音频文件
     */
    public void download() {

        File file = new File(MyApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC), getDownloadName());
        if (!file.exists()) {

            new Thread(new DownloadRunnable(voaId, bookid + "", sound)).start();
        }
    }

    /**
     * 下载音频的线程
     */
    public class DownloadRunnable implements Runnable {

        //文件命名："concept" + bookid + voaid + "US"    "concept" + bookid + voaid + "UK"
        private String voaid;

        private String bookid;

        private String sound;

        private String url;

        private int preProgress = 0;

        public DownloadRunnable(String voaId, String bookid, String sound) {
            this.voaid = voaId;
            this.bookid = bookid;
            this.sound = sound;

            //生成url
            if (BookUtil.isYouthBook(Integer.parseInt(bookid))) {

                url = sound.replace("voa", "voa/sentence").replace(voaid + ".mp3", voaid + "/" + voaid + ".mp3");
            } else {

                if (Constant.LANGUAGE.equalsIgnoreCase("US")) {

                    int a = Integer.parseInt(voaid) / 1000;
                    int b = Integer.parseInt(voaid) - Integer.parseInt(voaid) / 1000 * 1000;
                    url = "http://static2.iyuba.cn/newconcept/" + a + "_" + b + ".mp3";
                } else {

                    int a = Integer.parseInt(voaid) / 1000;
                    int b = Integer.parseInt(voaid) - Integer.parseInt(voaid) / 1000 * 1000;
                    url = "http://static2.iyuba.cn/newconcept/british/" + a + "/" + a + "_" + b + ".mp3";
                }
            }

        }

        @Override
        public void run() {

            String name = "concept" + bookid + voaid + "US" + ".mp3";
            File file = new File(MyApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC), name);

            try {
                OkHttpClient client = new OkHttpClient();
                //设置要访问的网络地址
                Request request = new Request.Builder()
                        .url(url)//http、https
                        .build();
                //发送请求并获取数据
                Response response = client.newCall(request).execute();
                long length = response.body().contentLength();
                InputStream inputStream = response.body().byteStream();
                FileOutputStream fileOutputStream = new FileOutputStream(file);

                byte[] bytes = new byte[2048];
                int total = 0;
                int size = inputStream.read(bytes);
                while (size != -1) {

                    fileOutputStream.write(bytes, 0, size);
                    total = total + size;
                    downloadProgress = (int) (total * 100.0 / length);
                    if (preProgress != downloadProgress) {

                        EventBus.getDefault().post(new DownloadRefresh(Integer.parseInt(bookid), Integer.parseInt(voaid)));
                        preProgress = downloadProgress;
                    }

                    size = inputStream.read(bytes);
                }
                fileOutputStream.flush();
                inputStream.close();
                fileOutputStream.close();

                //更新下载的状态
                Title t = new Title();
                t.setUpload(1);
                t.updateAll("bookid = ? and voaid = ? and language = ?", bookid, voaid, Constant.LANGUAGE);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}