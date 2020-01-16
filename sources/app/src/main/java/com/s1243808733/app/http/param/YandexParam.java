package com.s1243808733.app.http.param;
import org.xutils.http.RequestParams;
import com.s1243808733.translate.YandexTranslate;

public class YandexParam extends RequestParams {

    public static class Builder {
        private String key;
        private String targetLang;
        private String text;

        public Builder() {
            setKey(YandexTranslate.getKey());
        }

        public Builder setKey(String key) {
            this.key = key;
            return this;
        }

        public Builder setTargetLang(String targetLang) {
            this.targetLang = targetLang;
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public YandexParam create() {
            YandexParam param=new YandexParam();
            param.setUri("https://translate.yandex.net/api/v1.5/tr.json/translate");
            param.addQueryStringParameter("key", key);
            param.addQueryStringParameter("lang", targetLang);
            param.addQueryStringParameter("text", text);
            return param;
        }

    }

}
