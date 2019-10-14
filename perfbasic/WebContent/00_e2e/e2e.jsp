<%@ page session="true" import="java.io.InputStream,java.net.HttpURLConnection,java.net.URL"%>
<html>
<body>

	<%
		String me = request.getRequestURL().toString();
// kosta 데모환경에서는 Port Forwarding 문제로 yuyusvr 리퀘스트 문자열을 localhost:8080으로 정적 대체해야 함
// 좋은 방법은 아님
// me = me.replace("yuyusvr", "localhost:8080");
		String next1 = me.replace("e2e.jsp", "e2end.jsp");
		String next2 = me.replace("e2e.jsp", "e2test.jsp");

		for (int i = 0; i < 3; i++) {
			InputStream in = null;
			HttpURLConnection uc = null;
			try {
				URL u = null;
				if (i % 2 == 0) {
					u = new URL(next1 + "?" + i);
				} else {
					u = new URL(next2 + "?" + i);
				}
				uc = (HttpURLConnection) u.openConnection();
				uc.setRequestMethod("POST");

				in = uc.getInputStream();

				byte[] buff = new byte[4096];
				int n = in.read(buff);
				while (n >= 0) {
					n = in.read(buff);
				}
			} catch (Throwable t) {
			} finally {
				if (uc != null) { uc.disconnect(); }
				if (in != null) { in.close(); }
			}
		}
	%>
	This is a test page!!
</body>
</html>
