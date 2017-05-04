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

package apijson.demo.client.model;

/**评论类
 * @author Lemon
 */
public class Comment extends apijson.demo.server.model.Comment {
	private static final long serialVersionUID = -1011007127735372824L;

	public Comment() {
		super();
	}
	public Comment(long id) {
		this();
		setId(id);
	}
	public Comment(String content) {
		this();
		setContent(content);
	}

	@Override
	public Long getId() {
		return value(super.getId());
	}
	@Override
	public Long getDate() {
		return value(super.getDate());
	}
	@Override
	public Long getMomentId() {
		return value(super.getMomentId());
	}
	@Override
	public Long getToId() {
		return value(super.getToId());
	}
	@Override
	public Long getUserId() {
		return value(super.getUserId());
	}

}