package com.jadecross;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {
	private static PropertyReader _instance = new PropertyReader();
	Properties props;

	private PropertyReader() {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("setting.properties");
		props = new Properties();
		try {
			props.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static PropertyReader getInstance() {
		return _instance;
	}

	public String getProperty(String key) {
		return props.getProperty(key);
	}
}