package apijson.framework;

import apijson.JSONResponse;
import apijson.orm.JSONRequest;
import apijson.orm.Visitor;
import apijson.orm.model.Access;
import apijson.orm.model.Column;
import apijson.orm.model.Document;
import apijson.orm.model.ExtendedProperty;
import apijson.orm.model.Function;
import apijson.orm.model.PgAttribute;
import apijson.orm.model.PgClass;
import apijson.orm.model.Request;
import apijson.orm.model.Response;
import apijson.orm.model.SysColumn;
import apijson.orm.model.SysTable;
import apijson.orm.model.Table;
import apijson.orm.model.Test;
import apijson.orm.model.TestRecord;

public class APIJSONConstant {

	public static final String DEFAULTS = "defaults";
	public static final String USER_ = "User";
	public static final String PRIVACY_ = "Privacy";
	public static final String VISITOR_ID = "visitorId";
	
	public static final String ID = JSONRequest.KEY_ID;
	public static final String USER_ID = JSONRequest.KEY_USER_ID;
	public static final String TAG = JSONRequest.KEY_TAG;
	public static final String VERSION = JSONRequest.KEY_VERSION;
	public static final String FORMAT = JSONRequest.KEY_FORMAT;
	
	public static final String CODE = JSONResponse.KEY_CODE;
	public static final String MSG = JSONResponse.KEY_MSG;
	public static final String COUNT = JSONResponse.KEY_COUNT;
	public static final String TOTAL = JSONResponse.KEY_TOTAL;
	
	public static final String ACCESS_;
	public static final String COLUMN_;
	public static final String DOCUMENT_;
	public static final String EXTENDED_PROPERTY_;
	public static final String FUNCTION_;
	public static final String PG_ATTRIBUTE_;
	public static final String PG_CLASS_;
	public static final String RESPONSE_;
	public static final String REQUEST_;
	public static final String SYS_COLUMN_;
	public static final String SYS_TABLE_;
	public static final String TABLE_;
	public static final String TEST_;
	public static final String TEST_RECORD_;
	
	public static final String VISITOR_;

	static {
		ACCESS_ = Access.class.getSimpleName();
		COLUMN_ = Column.class.getSimpleName();
		DOCUMENT_ = Document.class.getSimpleName();
		EXTENDED_PROPERTY_ = ExtendedProperty.class.getSimpleName();
		FUNCTION_ = Function.class.getSimpleName();
		PG_ATTRIBUTE_ = PgAttribute.class.getSimpleName();
		PG_CLASS_ = PgClass.class.getSimpleName();
		REQUEST_ = Request.class.getSimpleName();
		RESPONSE_ = Response.class.getSimpleName();
		SYS_COLUMN_ = SysColumn.class.getSimpleName();
		SYS_TABLE_ = SysTable.class.getSimpleName();
		TABLE_ = Table.class.getSimpleName();
		TEST_ = Test.class.getSimpleName();
		TEST_RECORD_ = TestRecord.class.getSimpleName();
		
		VISITOR_ = Visitor.class.getSimpleName();
	}
	


}
