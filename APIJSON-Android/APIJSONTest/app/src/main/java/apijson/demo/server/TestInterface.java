package apijson.demo.server;

import java.io.Serializable;

public interface TestInterface extends Serializable {

	void setData(Object data);
	Object getData();

	void setId(Long id);
	Long getId();

	Boolean sort();

	default void minusAsId(long a, long b) {
		setId(a - b);
	}
}
