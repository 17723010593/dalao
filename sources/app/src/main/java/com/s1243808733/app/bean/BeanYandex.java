package com.s1243808733.app.bean;
import com.s1243808733.app.http.response.JsonResponseParser;
import java.util.List;
import org.xutils.http.annotation.HttpResponse;

@HttpResponse(parser = JsonResponseParser.class)
public class BeanYandex {

    private int code;
    
    private String lang;
    
    private List<String> text;
    
    private String message;

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getLang() {
        return lang;
    }

    public void setText(List<String> text) {
        this.text = text;
    }

    public List<String> getText() {
        return text;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getMessage(int code) {

        if (code == 401) {
            return "无效的API密钥";
        } else if (code == 402) {
            return "封锁的API密钥";
        } else if (code == 404) {
            return "超出每日翻译量限制";
        } else if (code == 413) {
            return "超过最大文字大小";
        } else if (code == 422) {
            return "文字无法翻译";
        } else if (code == 501) {
            return "不支持指定的翻译方向";
        }

        return message;
    }

    public boolean isSuccess() {
        return 200 == getCode();
    }
    
    public String getErrorMessage() {
        return getMessage(getCode());
    }
    

}
