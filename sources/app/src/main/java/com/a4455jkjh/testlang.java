package com.a4455jkjh;

import android.app.Activity;
import android.os.Bundle;
import com.s1243808733.app.bean.BeanYandex;
import com.s1243808733.app.http.param.YandexParam;
import com.s1243808733.translate.YandexTranslate;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import com.s1243808733.util.Utils;

public class testlang extends Activity {

	private JSONObject jsonobj;

	private Iterator<String> keySet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		String json="{\n  \"af\": \"Afrikaans\",\n  \"am\": \"Amharic\",\n  \"ar\": \"Arabic\",\n  \"az\": \"Azerbaijani\",\n  \"ba\": \"Bashkir\",\n  \"be\": \"Belarusian\",\n  \"bg\": \"Bulgarian\",\n  \"bn\": \"Bengali\",\n  \"bs\": \"Bosnian\",\n  \"ca\": \"Catalan\",\n  \"ceb\": \"Cebuano\",\n  \"cs\": \"Czech\",\n  \"cy\": \"Welsh\",\n  \"da\": \"Danish\",\n  \"de\": \"German\",\n  \"el\": \"Greek\",\n  \"en\": \"English\",\n  \"eo\": \"Esperanto\",\n  \"es\": \"Spanish\",\n  \"et\": \"Estonian\",\n  \"eu\": \"Basque\",\n  \"fa\": \"Persian\",\n  \"fi\": \"Finnish\",\n  \"fr\": \"French\",\n  \"ga\": \"Irish\",\n  \"gd\": \"Scottish Gaelic\",\n  \"gl\": \"Galician\",\n  \"gu\": \"Gujarati\",\n  \"he\": \"Hebrew\",\n  \"hi\": \"Hindi\",\n  \"hr\": \"Croatian\",\n  \"ht\": \"Haitian\",\n  \"hu\": \"Hungarian\",\n  \"hy\": \"Armenian\",\n  \"id\": \"Indonesian\",\n  \"is\": \"Icelandic\",\n  \"it\": \"Italian\",\n  \"ja\": \"Japanese\",\n  \"jv\": \"Javanese\",\n  \"ka\": \"Georgian\",\n  \"kk\": \"Kazakh\",\n  \"km\": \"Khmer\",\n  \"kn\": \"Kannada\",\n  \"ko\": \"Korean\",\n  \"ky\": \"Kyrgyz\",\n  \"la\": \"Latin\",\n  \"lb\": \"Luxembourgish\",\n  \"lo\": \"Lao\",\n  \"lt\": \"Lithuanian\",\n  \"lv\": \"Latvian\",\n  \"mg\": \"Malagasy\",\n  \"mhr\": \"Mari\",\n  \"mi\": \"Maori\",\n  \"mk\": \"Macedonian\",\n  \"ml\": \"Malayalam\",\n  \"mn\": \"Mongolian\",\n  \"mr\": \"Marathi\",\n  \"mrj\": \"Hill Mari\",\n  \"ms\": \"Malay\",\n  \"mt\": \"Maltese\",\n  \"my\": \"Burmese\",\n  \"ne\": \"Nepali\",\n  \"nl\": \"Dutch\",\n  \"no\": \"Norwegian\",\n  \"pa\": \"Punjabi\",\n  \"pap\": \"Papiamento\",\n  \"pl\": \"Polish\",\n  \"pt\": \"Portuguese\",\n  \"ro\": \"Romanian\",\n  \"ru\": \"Russian\",\n  \"si\": \"Sinhalese\",\n  \"sk\": \"Slovak\",\n  \"sl\": \"Slovenian\",\n  \"sq\": \"Albanian\",\n  \"sr\": \"Serbian\",\n  \"su\": \"Sundanese\",\n  \"sv\": \"Swedish\",\n  \"sw\": \"Swahili\",\n  \"ta\": \"Tamil\",\n  \"te\": \"Telugu\",\n  \"tg\": \"Tajik\",\n  \"th\": \"Thai\",\n  \"tl\": \"Tagalog\",\n  \"tr\": \"Turkish\",\n  \"tt\": \"Tatar\",\n  \"udm\": \"Udmurt\",\n  \"uk\": \"Ukrainian\",\n  \"ur\": \"Urdu\",\n  \"uz\": \"Uzbek\",\n  \"vi\": \"Vietnamese\",\n  \"xh\": \"Xhosa\",\n  \"yi\": \"Yiddish\",\n  \"zh\": \"Chinese\"\n}";
		try {
			jsonobj = new JSONObject(json);
		} catch (JSONException e) {}

		try {
			Utils.toast(jsonobj.getString("af"));
		} catch (JSONException e) {
			Utils.toast(e);
		}

		keySet = jsonobj.keys();
test(keySet.next());


    }

	private String result="";

	private void test(final String key) {

		YandexParam para =null;
		try {
			para = new YandexParam.Builder()
			.setText(jsonobj.getString(key))
			.setTargetLang("zh")
			.create();
		} catch (JSONException e) {}
		YandexTranslate.translate(para, new Callback.CommonCallback<BeanYandex>(){

			@Override
			public void onSuccess(BeanYandex p1) {
				result += "{\"" + key + "\",\"" + p1.getText().get(0) + "\"},\n";
			}

			@Override
			public void onError(Throwable p1, boolean p2) {
			}

			@Override
			public void onCancelled(Callback.CancelledException p1) {
			}

			@Override
			public void onFinished() {
				if (keySet.hasNext()) {
					String t=keySet.next();
					Utils.toast(t);
					test(t);

				} else {
					Utils.copyToClipboard(result);
				}
			}
		});
	}

}
