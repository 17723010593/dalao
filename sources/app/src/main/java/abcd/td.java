package abcd;
import com.s1243808733.aide.util.AdvancedSetting;

public class td {

	private void DW(Ia var1) {
		if (AdvancedSetting.isEnableAdrt()) {
			DW_SOURCE(var1);
		}
	}

	private void DW_SOURCE(Ia var1) {}

	private void FH(Ia param1) {
		if (AdvancedSetting.isEnableAdrt()) {
			FH_SOURCE(param1);
		} else {
			FH_NEW(param1);
		}
	}

	private void FH_SOURCE(Ia param1) {}

	private void FH_NEW(Ia param1) {}

	private void j6(Sa param1, boolean param2, boolean param3, boolean param4) {
		if (AdvancedSetting.isEnableAdrt()) {
			j6_SOURCE(param1, param2, param3, param4);
		} else {
			j6_NEW(param1, param2, param3, param4);
		}
	}

	private void j6_SOURCE(Sa param1, boolean param2, boolean param3, boolean param4) {}

	private void j6_NEW(Sa param1, boolean param2, boolean param3, boolean param4) {}

}
