package com.jikabao.android.common.util;

public class LocalPathResolver {

    private static final String TAG = LocalPathResolver.class.getSimpleName();

	private static final String BASE_DIR = "/jikabao";
	private static String base;

	public static void init(String base) {
        LocalPathResolver.base = base;
	}

	public static String getBaseDir() {
		return base + BASE_DIR;
	}

}
