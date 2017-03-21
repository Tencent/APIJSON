package apijson.demo.client.interfaces;

import android.app.Activity;
import android.view.View;

public interface TopBarMenuCallback {
	/**
	 * @param activity
	 */
	View getLeftMenu(Activity activity);

	/**
	 * @param activity
	 */
	View getRightMenu(Activity activity);
}
