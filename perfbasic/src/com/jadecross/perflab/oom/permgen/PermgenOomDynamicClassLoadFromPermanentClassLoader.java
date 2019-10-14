package com.jadecross.perflab.oom.permgen;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PermgenOomDynamicClassLoadFromPermanentClassLoader extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public PermgenOomDynamicClassLoadFromPermanentClassLoader() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String dynamicClassName = null;
		PermanentClassLoader myClassLoader = null;
		Class<?> dynamicClass = null;

		myClassLoader = PermanentClassLoader.getInstance();
		try {
			dynamicClassName = "C_" + UUID.randomUUID().toString().replace("-", "");
			dynamicClass = myClassLoader.findClass(dynamicClassName);

			dynamicClass.getDeclaredMethod("hello").invoke(dynamicClass.newInstance());

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		response.getWriter().append("Class : ").append(dynamicClassName).append(" Loaded by PermanentClassLoader.... ");
	}
}
