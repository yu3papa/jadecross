package com.jadecross.perflab.oom.permgen;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PermgenOomDynamicClassLoadFromTemporaryClassLoader extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public PermgenOomDynamicClassLoadFromTemporaryClassLoader() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String dynamicClassName = null;
		TemporaryClassLoader myClassLoader = null;
		Class<?> dynamicClass = null;

		myClassLoader = new TemporaryClassLoader();
		try {
			dynamicClassName = "C_" + UUID.randomUUID().toString().replace("-", "");
			dynamicClass = myClassLoader.findClass(dynamicClassName);

			dynamicClass.getDeclaredMethod("hello").invoke(dynamicClass.newInstance());

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		response.getWriter().append("Class : ").append(dynamicClassName).append(" Loaded by TemporaryClassLoader.... ");
	}

}
