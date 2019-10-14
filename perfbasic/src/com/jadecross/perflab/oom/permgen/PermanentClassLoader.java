package com.jadecross.perflab.oom.permgen;

/**
   * 동적으로 로딩하고 자신은 메모리에서 해제 되지 않는 ClassLoader
 *
 */
public class PermanentClassLoader extends ClassLoader {
	private static PermanentClassLoader _instance = new PermanentClassLoader();

	private PermanentClassLoader() {
	}

	public static PermanentClassLoader getInstance() {
		return _instance;
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