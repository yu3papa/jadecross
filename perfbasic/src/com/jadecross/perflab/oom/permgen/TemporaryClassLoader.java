package com.jadecross.perflab.oom.permgen;

public class TemporaryClassLoader extends ClassLoader {

	public TemporaryClassLoader() {
	}

	@Override
	protected Class<?> findClass(String className) throws ClassNotFoundException {
		byte[] classBytes;
		try {
			classBytes = HelloWorldClassCreator.dump(className);
			return defineClass(className, classBytes, 0, classBytes.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.findClass(className);
	}
}