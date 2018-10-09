package com.bugsir.share.qq;

import android.app.Activity;
import android.os.Bundle;

import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * @author:
 * @date: 2017/12/16 01:13
 * @description:
 */

public class QQShareUtil {
    private Activity mContext;
    private String mQQId;
    private boolean mIsQzone=false;
    private String mAppName;
    private QQShareUtil(Activity context)
    {
        this.mContext=context;
    }

    public static QQShareUtil with(Activity context)
    {
        return new QQShareUtil(context);
    }
    public  QQShareUtil setQQId(String qqId)
    {
        this.mQQId=qqId;
        return this;
    }

    public QQShareUtil setIsQzone(boolean isQzone)
    {
        this.mIsQzone=isQzone;
        return this;
    }
    public QQShareUtil setAppName(String appName)
    {
        this.mAppName=appName;
        return this;
    }

    public  void shareUrl(String Url, String title, String iconSdPath, String content) {

        Tencent mTencent = Tencent.createInstance(mQQId, mContext);
        Bundle bundle = new Bundle();
//这条分享消息被好友点击后的跳转URL。
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, Url);
//分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_ SUMMARY不能全为空，最少必须有一个是有值的。
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, title);
//分享的图片URL
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, iconSdPath);
//分享的消息摘要，最长50个字
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, content);
//手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
//        bundle.putString(SocialConstants.PARAM_APPNAME, "??我在测试");
//标识该消息的来源应用，值为应用名称+AppId。
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, mAppName);
        bundle.putInt(QQShare.SHARE_TO_QQ_EXT_INT, mIsQzone ? QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN : QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);

        mTencent.shareToQQ(mContext, bundle, new IUiListener() {
            @Override
            public void onComplete(Object o) {

            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        });
    }

    public  void shareImage(String localpath) {
        Tencent mTencent = Tencent.createInstance(mQQId,mContext);
        Bundle params = new Bundle();
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, localpath);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, mAppName);
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, mIsQzone ? QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN : QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        mTencent.shareToQQ(mContext, params, new IUiListener() {
            @Override
            public void onComplete(Object o) {

            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        });
    }
}
