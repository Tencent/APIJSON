package zuo.biao.apijson;

public class Column {

	private int count;//获得所有列的数目及实际列数
	private String name;//获得指定列的列名
	private String value;//获得指定列的列值
	private int type;//获得指定列的数据类型
	private String typeName;//获得指定列的数据类型名
	private String catalogName;//所在的Catalog名字
	private String className;//对应数据类型的类
	private int displaySize;//在数据库中类型的最大字符个数
	private String label;//默认的列的标题
	private String schemaName;//获得列的模式
	private int precision;//某列类型的精确度(类型的长度)
	private int scale;//小数点后的位数
	private String table;//获取某列对应的表名
	private boolean autoInctement;// 是否自动递增
	private boolean isCurrency;//在数据库中是否为货币型
	private int nullable;//是否为空
	private boolean readOnly;//是否为只读
	private boolean searchable;//能否出现在where中

	public Column() {
		super();
	}
	public Column(String name) {
		this();
		setName(name);
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getCatalogName() {
		return catalogName;
	}
	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public int getDisplaySize() {
		return displaySize;
	}
	public void setDisplaySize(int displaySize) {
		this.displaySize = displaySize;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getSchemaName() {
		return schemaName;
	}
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	public int getPrecision() {
		return precision;
	}
	public void setPrecision(int precision) {
		this.precision = precision;
	}
	public int getScale() {
		return scale;
	}
	public void setScale(int scale) {
		this.scale = scale;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public boolean isAutoInctement() {
		return autoInctement;
	}
	public void setAutoInctement(boolean autoInctement) {
		this.autoInctement = autoInctement;
	}
	public boolean getIsCurrency() {
		return isCurrency;
	}
	public void setIsCurrency(boolean isCurrency) {
		this.isCurrency = isCurrency;
	}
	public int getNullable() {
		return nullable;
	}
	public void setNullable(int nullable) {
		this.nullable = nullable;
	}
	public boolean getReadOnly() {
		return readOnly;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	public boolean getSearchable() {
		return searchable;
	}
	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}

}
