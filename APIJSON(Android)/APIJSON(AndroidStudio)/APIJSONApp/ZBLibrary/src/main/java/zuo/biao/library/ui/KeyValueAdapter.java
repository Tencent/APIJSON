/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package zuo.biao.library.ui;

import zuo.biao.library.R;
import zuo.biao.library.base.BaseAdapter;
import zuo.biao.library.model.Entry;
import zuo.biao.library.util.StringUtil;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**key-value型(两个都是String类型)Adapter，
 * @author Lemon
 * @use new KeyValueAdapter(...); 具体参考.DemoAdapter
 */
public class KeyValueAdapter extends BaseAdapter<Entry<String, String>> {

	private int layoutRes;//布局id
	public KeyValueAdapter(Activity context) {     
		this(context, R.layout.key_value_item);
	}   
	public KeyValueAdapter(Activity context, int layoutRes) {     
		super(context);
		
		this.layoutRes = layoutRes;
	}   

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = convertView == null ? null : (ViewHolder) convertView.getTag();
		if (holder == null) {
			convertView = inflater.inflate(layoutRes, parent, false);

			holder = new ViewHolder();
			holder.tvKey = (TextView) convertView.findViewById(R.id.tvKeyValueItemKey);
			holder.tvValue = (TextView) convertView.findViewById(R.id.tvKeyValueItemValue);

			convertView.setTag(holder);
		}

		final Entry<String, String> data = getItem(position);
		
		holder.tvKey.setText(StringUtil.getTrimedString(data.getKey()));
		holder.tvValue.setText(StringUtil.getTrimedString(data.getValue()));

		return convertView;
	}

	static class ViewHolder {  
		public TextView tvKey;
		public TextView tvValue;
	}


}
