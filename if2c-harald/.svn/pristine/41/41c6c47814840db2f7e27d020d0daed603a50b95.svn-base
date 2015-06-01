package com.if2c.harald.mail;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.StringUtils;

import com.if2c.harald.tools.BackupLog;

public class Mailer {
	private MailAuthenticator authenticator;
	private Properties props;

	public Mailer(Properties props) {
		this.props = props;
		String userName = props.getProperty("EMAIL_USERNAME");
		String password = props.getProperty("EMAIL_PASSWORD");
		authenticator = new MailAuthenticator(userName, password);// 进行邮件服务用户认证
	}

	/**
	 * 
	 * @param from
	 * @param to
	 *            对于多个接收者，要用英文分号(;)分隔
	 * @param subject
	 * @param content
	 * @return
	 */
	public boolean sendMail(String from, String to, String subject,
			String content, int id, String cc) {
		return sendMail(from, to, subject, content, id, cc, null);
	}

	public boolean sendMail(String from, String to, String subject,
			String content, int id, String cc, List<String> files) {
		// sendMail
		Session sendMailSession = Session.getDefaultInstance(props,
				authenticator);// 设置session,和邮件服务器进行通讯
		Message mailMessage = new MimeMessage(sendMailSession);
		try {
			if (StringUtils.isBlank(to)) {
				return false;
			}
			Address addressFrom = new InternetAddress(from);
			mailMessage.setFrom(addressFrom);// 设置邮件发�?者的地址
			String[] tos = to.split(";");
			Address[] addressTo = new InternetAddress[tos.length];
			for (int i = 0; i < tos.length; i++) {
				if (StringUtils.isNotBlank(tos[i])) {
					addressTo[i] = new InternetAddress(tos[i]);// 设置邮件接收者的地址
				}
			}

			if (StringUtils.isNotBlank(cc)) {
				String[] ccs = cc.split(";");
				Address[] addressCcTo = new InternetAddress[ccs.length];
				for (int i = 0; i < ccs.length; i++) {
					if (StringUtils.isNotBlank(ccs[i])) {
						addressCcTo[i] = new InternetAddress(ccs[i]);// 设置邮件接收者的地址
					}
				}
				mailMessage
						.setRecipients(Message.RecipientType.CC, addressCcTo);
			}

			mailMessage.setRecipients(Message.RecipientType.TO, addressTo); // 设置其接收类型为TO

			mailMessage.setSubject(subject);// 设置邮件主题
			mailMessage.setSentDate(new Date());// 发�?邮件时间
			Multipart mainPart = new MimeMultipart();
			BodyPart html = new MimeBodyPart();

			html.setContent(content, "text/html;charset=utf-8"); // 邮件正文
			mainPart.addBodyPart(html);

			// if (files != null && !files.isEmpty()) { // 带附�?
			// for (String path : files) {
			// BodyPart filePart = new MimeBodyPart();
			// FileDataSource fds = new FileDataSource(path);
			// filePart.setDataHandler(new DataHandler(fds));
			// filePart.setFileName(fds.getName());
			// mainPart.addBodyPart(filePart);
			// }
			// }

			mailMessage.setContent(mainPart);
			Transport.send(mailMessage);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			BackupLog.logError("send email failed! email_queue.seq:" + id, e);
		}
		return false;
	}
}
