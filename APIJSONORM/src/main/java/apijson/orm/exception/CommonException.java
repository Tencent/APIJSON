/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson.orm.exception;

import java.io.UnsupportedEncodingException;
import java.net.HttpRetryException;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

import apijson.JSONResponse;
import apijson.Log;
import apijson.StringUtil;
import apijson.orm.AbstractSQLConfig;
import apijson.orm.SQLConfig;

/**异常包装器，主要用来包装构造器没有 Throwable 参数的 Exception
 * @author Lemon
 */
public class CommonException extends Exception {
  private static final long serialVersionUID = 1L;
  private Integer code;
  private String environment;

  public CommonException setCode(Integer code) {
    this.code = code;
    return this;
  }
  public Integer getCode() {
    if (code == null) {
      code = getCode(getCause());
    }
    return code;
  }


  public static int getCode(Throwable e) {
    boolean isCommon = Log.DEBUG && e instanceof CommonException;

    Integer code = isCommon ? ((CommonException) e).getCode() : null;
    if (code != null) {
      return code;
    }

    Throwable t = isCommon ? e.getCause() : e;
    if (t == null) {
      return JSONResponse.CODE_SERVER_ERROR;
    }

    if (t instanceof HttpRetryException) {
      return ((HttpRetryException) t).responseCode();
    }

    if (t instanceof UnsupportedEncodingException) {
      return JSONResponse.CODE_UNSUPPORTED_ENCODING;
    }

    if (t instanceof IllegalAccessException) {
      return JSONResponse.CODE_ILLEGAL_ACCESS;
    }

    if (t instanceof UnsupportedOperationException) {
      return JSONResponse.CODE_UNSUPPORTED_OPERATION;
    }

    if (t instanceof NotExistException) {
      return JSONResponse.CODE_NOT_FOUND;
    }

    if (t instanceof IllegalArgumentException) {
      return JSONResponse.CODE_ILLEGAL_ARGUMENT;
    }

    if (t instanceof NotLoggedInException) {
      return JSONResponse.CODE_NOT_LOGGED_IN;
    }

    if (t instanceof TimeoutException) {
      return JSONResponse.CODE_TIME_OUT;
    }

    if (t instanceof ConflictException) {
      return JSONResponse.CODE_CONFLICT;
    }

    if (t instanceof ConditionErrorException) {
      return JSONResponse.CODE_CONDITION_ERROR;
    }

    if (t instanceof UnsupportedDataTypeException) {
      return JSONResponse.CODE_UNSUPPORTED_TYPE;
    }

    if (t instanceof OutOfRangeException) {
      return JSONResponse.CODE_OUT_OF_RANGE;
    }

    if (t instanceof NullPointerException) {
      return JSONResponse.CODE_NULL_POINTER;
    }

    return JSONResponse.CODE_SERVER_ERROR;
  }

  public static String getMsg(Throwable e) {
    if (e == null) {
      return null;
    }

    String msg = e.getMessage();
    if (msg != null) {
      return msg;
    }

    Throwable t = e.getCause();
    return t == null ? null : t.getMessage();
  }

  public CommonException setEnvironment(String environment) {
    this.environment = environment;
    return this;
  }

  public String getEnvironment() {
    return environment;
  }

  public CommonException(Throwable t) {
    this(null, t);
  }
  public CommonException(String msg, Throwable t) {
    super(msg == null && t != null ? t.getMessage() : null, t);
  }

  public CommonException(int code, String msg) {
    this(code, msg, null);
  }
  public CommonException(int code, String msg, Throwable t) {
    this(msg, t);
    setCode(code);
  }
  public CommonException(Throwable t, String environment) {
    this(null, t);
    setEnvironment(environment);
  }


  public static Exception wrap(Exception e, SQLConfig config) {
    if (Log.DEBUG == false && e instanceof SQLException) {
      return new SQLException("数据库驱动执行异常SQLException，非 Log.DEBUG 模式下不显示详情，避免泄漏真实模式名、表名等隐私信息", e);
    }

    //			String msg = e.getMessage();

    if (Log.DEBUG && (e instanceof CommonException == false || ((CommonException) e).getEnvironment() == null)) {
      // msg != null && msg.contains(Log.KEY_SYSTEM_INFO_DIVIDER) == false) {
      try {
        String db = config == null ? AbstractSQLConfig.DEFAULT_DATABASE : (config instanceof AbstractSQLConfig
          ? ((AbstractSQLConfig) config).getSQLDatabase() : config.getDatabase()
        );

        String dbVersion = config.getDBVersion();
        if (StringUtil.isEmpty(dbVersion)) {
          dbVersion = "<!-- 请填写版本号，例如 8.0 -->";
        }

        if (db != null) {
          db += " " + dbVersion;
        }
        else if (config.isMySQL()) {
          db = SQLConfig.DATABASE_MYSQL + " " + dbVersion;
        }
        else if (config.isPostgreSQL()) {
          db = SQLConfig.DATABASE_POSTGRESQL + " " + dbVersion;
        }
        else if (config.isSQLServer()) {
          db = SQLConfig.DATABASE_SQLSERVER + " " + dbVersion;
        }
        else if (config.isOracle()) {
          db = SQLConfig.DATABASE_ORACLE + " " + dbVersion;
        }
        else if (config.isDb2()) {
          db = SQLConfig.DATABASE_DB2 + " " + dbVersion;
        }
        else if (config.isDameng()) {
          db = SQLConfig.DATABASE_DAMENG + " " + dbVersion;
        }
        else if (config.isClickHouse()) {
          db = SQLConfig.DATABASE_CLICKHOUSE + " " + dbVersion;
        }
        else if (config.isTDengine()) {
          db = SQLConfig.DATABASE_TDENGINE + " " + dbVersion;
        }
        else {
          db = "<!-- 请填写，例如 MySQL 5.7。获取到的默认数据库为 " + AbstractSQLConfig.DEFAULT_DATABASE + " -->";
        }

//					Class<? extends Exception> clazz = e.getClass();
//          msg = msg
//                      + "       " + Log.KEY_SYSTEM_INFO_DIVIDER + "       **环境信息** "
        String env = " **环境信息** "
          + " \n 系统: " + Log.OS_NAME + " " + Log.OS_VERSION
          + " \n 数据库: " + db
          + " \n JDK: " + Log.JAVA_VERSION + " " + Log.OS_ARCH
          + " \n APIJSON: " + Log.VERSION;

        if (e instanceof CommonException) {
          ((CommonException) e).setEnvironment(env);
          return e;
        }

//          try {
//            e = clazz.getConstructor(String.class, Throwable.class).newInstance(msg, e);
//          }
//          catch (Throwable e2) {
        return new CommonException(e, env);  // e = clazz.getConstructor(String.class).newInstance(msg);
//          }
      } catch (Throwable e2) {}
    }

    return e;
  }

}

