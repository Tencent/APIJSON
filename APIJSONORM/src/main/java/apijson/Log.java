/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson;

/**测试用Log
 * @modifier Lemon
 */
public class Log {

	public static boolean DEBUG = true;
	
	/**
	 * @param TAG
	 * @param msg
	 */
	public static void d(String TAG, String msg) {
		if (DEBUG) {
			System.err.println(TAG + ".DEBUG: " + msg);
		}
	}

	/**
	 * Forced debug
	 * @param TAG tag
	 * @param msg debug messages
	 */
	public static void fd(String TAG, String msg) {
			System.err.println(TAG + ".DEBUG: " + msg);
	}

	/**
	 * Generate separation line
	 * @param pre prefix
	 * @param symbol used for generating separation line
	 * @param post postfix
	 */
	public static void sl(String pre,char symbol ,String post) {
		System.err.println(pre+new String(new char[48]).replace('\u0000', symbol)+post);
	}

	/**
	 * @param TAG
	 * @param msg
	 */
	public static void v(String TAG, String msg) {
		if (DEBUG) {
			System.out.println(TAG + ".VERBOSE: " + msg);
		}
	}

	/**
	 * @param TAG
	 * @param msg
	 */
	public static void i(String TAG, String msg) {
		if (DEBUG) {
			System.out.println(TAG + ".INFO: " + msg);
		}
	}

	/**
	 * @param TAG
	 * @param msg
	 */
	public static void e(String TAG, String msg) {
		if (DEBUG) {
			System.err.println(TAG + ".ERROR: " + msg);
		}
	}

	/**
	 * @param TAG
	 * @param msg
	 */
	public static void w(String TAG, String msg) {
		if (DEBUG) {
			System.err.println(TAG + ".WARN: " + msg);
		}
	}

}
