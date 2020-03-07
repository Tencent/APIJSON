package apijson.framework;

import apijson.JSONResponse;
import apijson.server.JSONRequest;
import apijson.server.Visitor;
import apijson.server.model.Access;
import apijson.server.model.Column;
import apijson.server.model.Document;
import apijson.server.model.ExtendedProperty;
import apijson.server.model.Function;
import apijson.server.model.PgAttribute;
import apijson.server.model.PgClass;
import apijson.server.model.Request;
import apijson.server.model.Response;
import apijson.server.model.SysColumn;
import apijson.server.model.SysTable;
import apijson.server.model.Table;
import apijson.server.model.Test;
import apijson.server.model.TestRecord;

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
