package com.s1243808733.aide.completion.translate;

import com.s1243808733.app.bean.BeanYandex;
import com.s1243808733.app.http.param.YandexParam;
import com.s1243808733.translate.YandexTranslate;
import java.util.List;
import org.xutils.common.Callback;

public class Translate {

    public static void translate(String lang, String targetLang, final String content) {
		yandexTranslate(targetLang, content);
		//...
	}

    public static void yandexTranslate(String targetLang, final String content) {

        YandexParam param=new YandexParam.Builder()
			.setKey(YandexTranslate.getKey())
			.setTargetLang(targetLang)
			.setText(TranslateUtils.wordSegmentation(content))
			.create();

        YandexTranslate.translate(param, new Callback.CommonCallback<BeanYandex>(){

				@Override
				public void onSuccess(BeanYandex bean) {
					Table items=TranslateUtils.query(content);
					if (items == null || items.getState() == TranslateUtils.STATE_SUCCESS) {
						return;
					}
					if (bean.isSuccess()) {
						List<String> text=bean.getText();
						if (text.size() == 0) {
							items.setState(TranslateUtils.STATE_FAILED);
						} else {
							items.setTranslation(text.get(0));
							items.setState(TranslateUtils.STATE_SUCCESS);
						}
					} else {
						items.setTranslation(bean.getErrorMessage());
						items.setState(TranslateUtils.STATE_FAILED);
					}

					TranslateUtils.update(items);

				}

				@Override
				public void onError(Throwable p1, boolean p2) {
					Table items=TranslateUtils.query(content);
					if (items == null || items.getState() == TranslateUtils.STATE_SUCCESS) {
						return;
					}
					items.setState(TranslateUtils.STATE_FAILED);
					TranslateUtils.update(items);
				}

				@Override
				public void onCancelled(Callback.CancelledException p1) {
				}

				@Override
				public void onFinished() {
				}
			});


    }


}
