package com.jikabao.android.common.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.jikabao.android.common.R;
import com.jikabao.android.common.util.ToastUtil;
import com.tencent.mm.sdk.openapi.*;

import com.jikabao.android.common.util.WeChatUtils;

import net.sourceforge.simcpux.GetFromWXActivity;
import net.sourceforge.simcpux.ShowFromWXActivity;

/**
 * Created by pjq on 1/8/15.
 */
public class WechatActivity extends BaseActivity implements IWXAPIEventHandler{
    private WeChatUtils weChatUtils;
//    private String WEIXIN_SCOPE ="snsapi_userinfo";
    private String WEIXIN_SCOPE ="snsapi_base";
    private String WEIXIN_STATE ="callback1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initWeChat();
    }

    private void initWeChat() {
        weChatUtils = WeChatUtils.getInstance(this);
        if (weChatUtils.getIWXAPI().isWXAppInstalled()) {
            weChatUtils.register();
            weChatUtils.getIWXAPI().handleIntent(getIntent(), this);
        }else {
            ToastUtil.showToast(getApplicationContext(), getString(R.string.please_install_wechat));
        }
    }

    protected void loginWithWeixin() {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = WEIXIN_SCOPE;
        req.state = WEIXIN_STATE;
        weChatUtils.getIWXAPI().sendReq(req);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                goToGetMsg();
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                goToShowMsg((ShowMessageFromWX.Req) req);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResp(BaseResp resp) {
        int result = 0;

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = R.string.errcode_success;
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = R.string.errcode_cancel;
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = R.string.errcode_deny;
                break;
            default:
                result = R.string.errcode_unknown;
                break;
        }

        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }

    private void goToGetMsg() {
        Intent intent = new Intent(this, GetFromWXActivity.class);
        intent.putExtras(getIntent());
        startActivity(intent);
        //finish();
    }

    private void goToShowMsg(ShowMessageFromWX.Req showReq) {
        WXMediaMessage wxMsg = showReq.message;
        WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;

        StringBuffer msg = new StringBuffer(); // 组织一个待显示的消息内容
        msg.append("description: ");
        msg.append(wxMsg.description);
        msg.append("\n");
        msg.append("extInfo: ");
        msg.append(obj.extInfo);
        msg.append("\n");
        msg.append("filePath: ");
        msg.append(obj.filePath);

        Intent intent = new Intent(this, ShowFromWXActivity.class);
        intent.putExtra(net.sourceforge.simcpux.Constants.ShowMsgActivity.STitle, wxMsg.title);
        intent.putExtra(net.sourceforge.simcpux.Constants.ShowMsgActivity.SMessage, msg.toString());
        intent.putExtra(net.sourceforge.simcpux.Constants.ShowMsgActivity.BAThumbData, wxMsg.thumbData);
        startActivity(intent);
        //finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        weChatUtils.getIWXAPI().handleIntent(intent, this);
    }
}
