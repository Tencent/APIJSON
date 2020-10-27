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
