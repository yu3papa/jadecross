package com.jadecross.jennifer5;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.aries.extension.data.EventData;
import com.aries.extension.handler.EventHandler;
import com.aries.extension.util.LogUtil;
import com.aries.extension.util.PropertyUtil;

/**
 * 제니퍼5에서 AGENT_STOP/AGETN_START 이벤트가 발생했을 때 SMS 전송할수 있는 DB 테이블에 insert하는 어댑터</br></br>
 * <font color="red">주의사항</font>
 * <ul>
 * <li>sysout으로 로그를 남길수는 없다.</li>
 * <li>LogUtil.debug() 메소드도 로그를 남길수 없다.</li>
 * <li>LogUtil.info() 메소드를 이용한 로그만 adapter.view.2019-03-01.log 파일에 기록됨</li>
 * </ul>
 * <pre>
 * => AGENT_STOP 이벤트 정보
 *     domainId=1000
 *     domainName=JADECROSS
 *     time=1552444284511
 *     instanceId=10001
 *     instanceName=tomcat11
 *     instanceDescription=10001
 *     errorType=AGENT_STOP
 *     metricsName=
 *     eventLevel=FATAL
 *     message=
 *     value=-1.0
 *     otype=SYSTEM
 *     detailMessage=Disconnection is detected while read/write
 *     serviceName=
 *     txid=0
 *     customMessage=
 * </pre>
 * <pre>
 * => AGENT_START 이벤트 정보
 *     domainId=1000
 *     domainName=JADECROSS
 *     time=1552444168993
 *     instanceId=10001
 *     instanceName=tomcat11
 *     instanceDescription=10001
 *     errorType=AGENT_START
 *     metricsName=
 *     eventLevel=FATAL
 *     message=
 *     value=-1.0
 *     otype=SYSTEM
 *     detailMessage=version=5.2.3.3, connection=Local:172.21.22.51:5000-Remote:172.21.112.42:39370
 *     serviceName=
 *     txid=0
 *     customMessage=
 * </pre>
 * @author jadecross
 */
public class SmsAdapter implements EventHandler {
	
	private String sql;
	private DataSource nmcDS;
	private Connection conn;
	private PreparedStatement pstmt;
	
	/**
	 * 메세지 템플릿
	 * 	- property name : sms.message.template
	 *  - property value : 발생일시: <EVENT_TIME><CRLF>이벤트 : <CONTAINER> <EVENT_TYPE>
	 *  <pre>
	 *  ### 컨테이너 다운 메세지 샘플 ###
	 *  발생일시: 2019-03-13 11:31:24
	 *  이벤트 : W11 AGENT_STOP
	 *  
	 *  ### 컨테이너 시작 메세지 샘플 ###
	 *  발생일시: 2019-03-13 11:31:24
	 *  이벤트 : W11 AGENT_START
	 *  </pre>
	 */
	private String msgTemplate;
	
	/**
	 * 문자 수신자 목록 : csv형태로 값을 저장해야 함(한번에 한사람에게만 문자 전송이 가능함)
	 * 	- property name : sms.message.recipient
	 *  - property value : 01036474472,01011112222,01033334444
	 */
	private String[] msgRecipients;
	
	/**
	 * 문자 보낸사람 : 
	 * 	- property name : sms.message.sender
	 *  - property value : 0222767093
	 */
	private String msgSender; 
	
	/**
	 * 뷰서버 시작될때 Adapter 인스턴스가 생성된다.
	 * 1. Adapter에 등록된 파라미터 정보를 가져올 수 있다.
	 * 		- 파라미터값 가져오는 방법 : PropertyUtil.getValue("SmsAdapter", "sms.db.jdbc.url");
	 * 2. 생성자에서는 JNDI Lookup이 안된다.
	 * 		- jetty에 등록된 DataSource를 Lookup할 수 없으므로 on(EventData[] events)메소드에서 LookUp해야 한다.
	 */
	public SmsAdapter() {
		
		this.msgTemplate = PropertyUtil.getValue("SmsAdapter", "sms.message.template");
		this.msgRecipients = PropertyUtil.getValue("SmsAdapter", "sms.message.recipient").split(",");
		this.msgSender = PropertyUtil.getValue("SmsAdapter", "sms.message.sender");
		
		// INSERT SQL
		StringBuffer sb = new StringBuffer();
		sb.append("INSERT INTO inf.sm_data (seq, 				");
		sb.append("                         dest, 				");
		sb.append("                         callback, 			");
		sb.append("                         msg_flag, 			");
		sb.append("                         msg_text, 			");
		sb.append("                         reservation_time) 	");
		sb.append("VALUES (  inf.sms_seq.NEXTVAL, 				");
		sb.append("          ?, 								");
		sb.append("          ?, 								");
		sb.append("          '1', 								");
		sb.append("          ?, 								");
		sb.append("          DECODE (NULL, NULL, SYSDATE, TO_DATE (NULL || '0130', 'YYYYMMDDHH24MI'))) ");
		
		this.sql = sb.toString();
		LogUtil.info("##### SmsAdapter Loaded");
		LogUtil.info("     msgTemplate=" + msgTemplate);
		LogUtil.info("     msgRecipients=" + msgRecipients);
		LogUtil.info("     msgSender=" + msgSender);
		LogUtil.info("     SQL=" + sql);
	}

	/**
	 * Jennifer5에서 이벤트가 발생할 때 마다 호출되는 메소드
	 */
	@Override
	public void on(EventData[] events) {
		LogUtil.info("=> 이벤트 발생");
		String smsMessage = "";
		for(EventData data : events) {
			if(data.errorType.equals("AGENT_STOP") || data.errorType.equals("AGENT_START")) {
				// 1. 메세지 조립
				smsMessage = msgTemplate.replaceAll("<EVENT_TIME>", this.formatDate(data.time));
				smsMessage = smsMessage.replaceAll("<CRLF>", "\n");
				smsMessage = smsMessage.replaceAll("<CONTAINER>", data.instanceName);
				smsMessage = smsMessage.replaceAll("<EVENT_TYPE>", data.errorType);
				
				LogUtil.info(smsMessage);
				// 2. insert : batch 형태로 INSERT 수행
				insertSmsData(smsMessage);
			}
        }		
	}
	
	/**
	 * SMS 메세지를 DB테이블에 Batch 형태로 INSERT하다.
	 * @param smsMessage
	 */
	private void insertSmsData(String smsMessage) {
		try {
			this.lookupSmsDataSource();
			conn = nmcDS.getConnection();
			pstmt = conn.prepareStatement(sql);
			
			for(String eachRecepiant : msgRecipients) {
				pstmt.setString(1, eachRecepiant); // 수신자
				pstmt.setString(2, this.msgSender); // 발신자
				pstmt.setString(3, smsMessage); // SMS 메세지
				
				pstmt.addBatch();
			}
			
			pstmt.executeBatch(); // 배치 형태로 한번에 INSERT
			
			LogUtil.info("SmsAdapter SQL Insert Success");
		} catch (SQLException ex) {
			LogUtil.info("SmsAdapter SQL Exception Occured");
			LogUtil.info("  => " +  ex.getMessage());
		} finally {
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) { }
			if (conn != null) try { conn.close(); } catch (Exception e) { }
		}
	}
	
	/**
	 * 제니퍼 view-server(jetty)에 등록한 SMS DB에 대한 DataSource를 LookUp하여 멤버 변수로 설정한다.
	 *   - 생성자에서 LookUp하게 되면 아직 JNDI에 오브젝트가 등록되지 않아서 LookUp할 수 없다.
	 */
	private void lookupSmsDataSource() {
		Context jndiCntx = null;
		try {
			jndiCntx = new InitialContext();
			nmcDS = (DataSource) jndiCntx.lookup("java:comp/env/jdbc/nmcDS");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 제니퍼 Event Time을 한국 날자 형식의 문자열로 반환한다.
	 * @param eventTime - 제니퍼 이벤트 데이터의 time값
	 * @return
	 */
	private String formatDate(long eventTime) {
		Date eventDate = new Date(eventTime);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(eventDate);
	}
}
