package com.bugsir.share.wx;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


/**
 *@author:
 *@date: 2017/12/16 00:03
 *@description: 
 */

public    class WxShareUtil {

    private Context mContext;
    private String mWxId;
    private boolean mIsShareCycle=false;

    private WxShareUtil(Context context)
    {
        this.mContext=context;
    }

    public static WxShareUtil with(Context context)
    {
        return new WxShareUtil(context);
    }
    public  WxShareUtil setWxId(String wxId)
    {
        this.mWxId=wxId;
        return this;
    }


    public WxShareUtil setIsShareCycle(boolean isShareCycle)
    {
        this.mIsShareCycle=isShareCycle;
        return this;
    }

    public void shareUrl(String url, String title, String desc, byte[] thumbData)
    {
        shareUrl(url, title, desc, thumbData, "webpage" + System.currentTimeMillis());
    }

    public  void shareUrl(String url, String title, String desc, byte[] thumbData,
                                   String transaction)
    {
        if (!isWXAppInstalledAndSupported())
        {
            Toast.makeText(this.mContext,"您未安装微信，请先下载微信",Toast.LENGTH_SHORT).show();
            return;
        }
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = desc;
        msg.thumbData = thumbData;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = transaction;
        req.message = msg;
        req.scene = mIsShareCycle ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        WXAPIFactory.createWXAPI(mContext, mWxId).sendReq(req);
    }

    public  void shareImage(byte[] bitmapData)
    {
        if (!isWXAppInstalledAndSupported())
        {
            Toast.makeText(this.mContext,"您未安装微信，请先下载微信",Toast.LENGTH_SHORT).show();
            return;
        }
        Bitmap thumbBmp = BitmapUtil.getBitmap(bitmapData);
        WXImageObject imgObj = new WXImageObject(thumbBmp);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        msg.thumbData = bitmapData;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "image" + System.currentTimeMillis();
        req.message = msg;
        req.scene = mIsShareCycle ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        WXAPIFactory.createWXAPI(mContext, mWxId).sendReq(req);

    }

    public  void shareImage(Bitmap bitmapData)
    {
        shareImage(bitmapData, Bitmap.CompressFormat.JPEG);
    }

    public  void shareImage(Bitmap bitmapData,Bitmap.CompressFormat compressFormat)
    {
        if (!isWXAppInstalledAndSupported())
        {
            Toast.makeText(this.mContext,"您未安装微信，请先下载微信",Toast.LENGTH_SHORT).show();
            return;
        }
        WXImageObject imgObj = new WXImageObject(bitmapData);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        //缩略图大小不能大于32kb
        msg.thumbData = BitmapUtil.compress2Array(bitmapData,30,false, compressFormat);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "image" + System.currentTimeMillis();
        req.message = msg;
        req.scene = mIsShareCycle ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        WXAPIFactory.createWXAPI(mContext, mWxId).sendReq(req);

    }

    /**
     *  微信是否已安装且可用
     * @return
     */
    public  boolean isWXAppInstalledAndSupported()
    {
        IWXAPI msgApi = WXAPIFactory.createWXAPI(mContext, mWxId);
        boolean sIsWXAppInstalledAndSupported = msgApi.isWXAppInstalled();
        return sIsWXAppInstalledAndSupported;
    }
}
