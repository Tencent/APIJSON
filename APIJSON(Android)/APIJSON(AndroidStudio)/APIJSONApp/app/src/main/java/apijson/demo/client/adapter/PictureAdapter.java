/*Copyright ©2016 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package apijson.demo.client.adapter;

import zuo.biao.library.base.BaseAdapter;
import zuo.biao.library.util.ImageLoaderUtil;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import apijson.demo.client.R;

/**图片Adapter
 * @author Lemon
 * @use new PictureAdapter(...)
 */
public class PictureAdapter extends BaseAdapter<String> {
//	private static final String TAG = "PictureAdapter";

	public PictureAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = convertView == null ? null : (ViewHolder) convertView.getTag();
		if (holder == null) {
			convertView = inflater.inflate(R.layout.picture_item, parent, false);

			holder = new ViewHolder();
			holder.ivHead = (ImageView) convertView.findViewById(R.id.ivGridItemHead);

			convertView.setTag(holder);
		}

		ImageLoaderUtil.loadImage(holder.ivHead, getItem(position));

		return convertView;
	}

	public class ViewHolder {
		public ImageView ivHead;
	}

}
