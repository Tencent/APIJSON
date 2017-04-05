package zuo.biao.library.interfaces;

import android.view.ViewGroup;

public interface AdapterViewPresenter<T, V> {

	/**生成新的BV
	 * @param position
	 * @param parent
	 * @return
	 */
	public abstract V createView(int position, ViewGroup parent);
	
	/**设置BV显示
	 * @param position
	 * @param bv
	 * @return
	 */
	public abstract void bindView(int position, V bv);
	
}
