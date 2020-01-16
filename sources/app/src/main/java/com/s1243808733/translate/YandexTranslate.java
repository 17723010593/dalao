package com.s1243808733.translate;

import android.content.SharedPreferences;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.s1243808733.aide.util.AIDEUtils;
import com.s1243808733.app.bean.BeanYandex;
import com.s1243808733.app.http.param.YandexParam;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.x;

public class YandexTranslate {

    public static Callback.Cancelable translate(YandexParam params, final Callback.CommonCallback<BeanYandex> callback) {
        return x.http().get(params, new Callback.CommonCallback<BeanYandex>(){

			@Override
			public void onSuccess(BeanYandex bean) {
				callback.onSuccess(bean);
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				if (ex instanceof HttpException) {
					try {
						HttpException httpEx=(HttpException)ex;
						String result=httpEx.getResult();

						if (TextUtils.isEmpty(result)) {
							callback.onError(httpEx, isOnCallback);
						} else {
							BeanYandex bean=new Gson().fromJson(result, BeanYandex.class);
							callback.onSuccess(bean);
						}
					} catch (Throwable e) {
						callback.onError(e, isOnCallback);
					}
				} else {
					callback.onError(ex, isOnCallback);
				}
			}

			@Override
			public void onCancelled(Callback.CancelledException cex) {
				callback.onCancelled(cex);
			}

			@Override
			public void onFinished() {
				callback.onFinished();
			}
		});
    }

    public static String getDefaultKey() {
        return "trnsl.1.1.20190930T205816Z.a9f88a3c4b7e0c95.920cb9f62edfa90f9253d2a8b1980bad01f5aba4";
    }

    public static String getKey() {
        String pref_key="key_yandex";
        SharedPreferences sp=AIDEUtils.getSp();
        String data=sp.getString(pref_key, "");
        if (data.trim().length() == 0) {
            sp.edit().putString(pref_key, getDefaultKey()).commit();
            return getDefaultKey();
        }
        return data;
    }

	public static String[][] getSupportedLanguage() {
		String[][] array={
			{"en","英语"},
			{"zh","中文"},
			{"ja","日本"},
			{"ko","韩国"},
			
			{"am","阿姆哈拉语"},
			{"ar","阿拉伯文"},
			{"az","阿塞拜疆"},
			{"ba","巴什基尔"},
			{"be","白俄罗斯"},
			{"bg","保加利亚"},
			{"bn","孟加拉"},
			{"bs","波斯尼亚"},
			{"ca","加泰罗尼亚语"},
			{"cs","捷克"},
			{"cy","威尔士"},
			{"da","丹麦"},
			{"de","德语"},
			{"el","希腊"},
			{"eo","世界"},
			{"es","西班牙语"},
			{"et","爱沙尼亚"},
			{"eu","巴斯克"},
			{"fa","波斯"},
			{"fi","芬兰"},
			{"fr","法语"},
			{"ga","爱尔兰"},
			{"gd","苏格兰盖尔语"},
			{"gl","加利西亚"},
			{"gu","古吉拉特"},
			{"he","希伯来语"},
			{"hr","克罗地亚"},
			{"ht","海地"},
			{"hu","匈牙利"},
			{"hy","亚美尼亚"},
			{"id","印度尼西亚"},
			{"is","冰岛"},
			{"it","意大利语"},
			{"jv","爪哇"},
			{"ka","格鲁吉亚"},
			{"kk","哈萨克斯坦"},
			{"km","高棉"},
			{"kn","卡纳达语"},
			{"ky","吉尔吉斯斯坦"},
			{"la","拉丁"},
			{"lb","卢森堡的"},
			{"lo","老挝"},
			{"lt","立陶宛"},
			{"lv","拉脱维亚"},
			{"mg","英语"},
			{"mhr","丈夫"},
			{"mi","毛利人的"},
			{"mk","马其顿"},
			{"ml","马拉雅拉姆语"},
			{"mn","蒙古"},
			{"mr","马拉"},
			{"mrj","山里"},
			{"ms","马来"},
			{"mt","马耳他"},
			{"my","缅甸"},
			{"ne","尼泊尔"},
			{"nl","荷兰"},
			{"no","挪威"},
			{"pa","旁遮普语"},
			{"pap","帕皮阿门托语"},
			{"pl","波兰"},
			{"pt","葡萄牙"},
			{"ro","罗马尼亚"},
			{"ru","俄罗斯"},
			{"si","僧伽罗语"},
			{"sk","斯洛伐克"},
			{"sl","斯洛文尼亚"},
			{"sq","阿尔巴尼亚"},
			{"sr","塞尔维亚"},
			{"su","巽"},
			{"sv","瑞典"},
			{"sw","斯瓦希里语"},
			{"ta","泰米尔"},
			{"te","泰卢固语"},
			{"tg","塔吉克斯坦"},
			{"th","泰国"},
			{"tl","他加禄语"},
			{"tr","土耳其"},
			{"tt","Tatar"},
			{"udm","乌德穆尔特"},
			{"uk","乌克兰"},
			{"ur","乌尔都语"},
			{"uz","乌兹别克斯坦"},
			{"vi","越南语"},
			{"xh","科萨语"},
			{"yi","犹太语"},
		};
		return array;
	}


}
