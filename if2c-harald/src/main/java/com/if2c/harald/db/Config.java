package com.if2c.harald.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.if2c.harald.job.DeletePreview;
import com.if2c.harald.job.JobBase;
import com.if2c.harald.mail.Mailer;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class Config {
	static private Logger logger = LoggerFactory.getLogger(JobBase.class);
	static private Config shedulerCfg;
	static private Configuration freeMarkerCfg;
	private Map<String, JobInfo> jobInfos = new HashMap<String, JobInfo>();
	private Mailer mailer;
	private List<DatasourceElement> datasource;
	private List<MailHost> mailhost;
	private Host homePageHost;
	private String routerAddr;
	
	public static Configuration getTemplateConf() throws IOException {
		if (freeMarkerCfg == null) {
			freeMarkerCfg = new Configuration();
			freeMarkerCfg.setDirectoryForTemplateLoading(new File(
					"data/template"));
			freeMarkerCfg.setObjectWrapper(new DefaultObjectWrapper());
		}
		return freeMarkerCfg;
	}

	public static String getTemplate(String templateFile,
			Map<String, String> data) {
		Writer out = new StringWriter();
		try {
			Template template = getTemplateConf().getTemplate(templateFile);
			try {
				template.process(data, out);
			} catch (TemplateException | IOException e) {
				logger.error(e.getMessage(), e);
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return out.toString().replaceAll("\\'", "\\\\'");
	}

	public List<MailHost> getMailhost() {
		return mailhost;
	}

	public void setMailhost(List<MailHost> mailhost) {
		this.mailhost = mailhost;
	}

	public List<DatasourceElement> getDatasourceElement() {
		return datasource;
	}

	public Mailer getMailer() {
		if (mailer == null) {
			Properties props = new Properties();
			props.setProperty("java.net.preferIPv4Stack", "true");// 可以实现指定获取IPv4的地址而不是IPv6
			props.setProperty("mail.smtp.auth", "true"); // 设置smtp身份认证，必须为true才能通过验证
			props.setProperty("mail.smtp.quitwait", "false"); // false命令立即发送，连接也会立即关闭
			props.setProperty("EMAIL_USERNAME", getMailhost().get(0)
					.getUserName());
			props.setProperty("EMAIL_PASSWORD", getMailhost().get(0)
					.getPassWord());
			props.setProperty("mail.smtp.host", getMailhost().get(0).getHost());
			props.setProperty("mail.smtp.port",
					String.valueOf(getMailhost().get(0).getPort()));
			mailer = new Mailer(props);
		}
		return mailer;

	}

	public List<Class<JobBase>> getClasses() throws ClassNotFoundException {
		List<Class<JobBase>> result = new ArrayList<Class<JobBase>>();
		Collection<JobInfo> jobInfoList = jobInfos.values();
		for (JobInfo jobInfo : jobInfoList) {
			@SuppressWarnings("unchecked")
			Class<JobBase> c = (Class<JobBase>) Class.forName(jobInfo
					.getClassName());
			result.add(c);
		}
		return result;
	}

	public static void init() {
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(new File("datasource.xml"));
			shedulerCfg = parseXml(fs);
		} catch (FileNotFoundException e) {
			logger.info("");
			System.exit(0);
			e.printStackTrace();
		} finally {
			try {
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static Config getConf() {
		if (shedulerCfg == null) {
			init();
		}
		return shedulerCfg;
	}

	public Map<String, JobInfo> getJobInfos() {
		return jobInfos;
	}

	public void setJobInfos(Map<String, JobInfo> jobInfos) {
		this.jobInfos = jobInfos;
	}
	
	

	public Host getHomePageHost() {
		return homePageHost;
	}

	public void setHomePageHost(Host homePageHost) {
		this.homePageHost = homePageHost;
	}

	public static Config parseXml(InputStream is) {
		if (is == null) {
			System.out.println("File is not exist!");
			return null;
		}
		// System.out.println(xmlFile.getPath());
		SAXReader reader = new SAXReader();
		Config conf = new Config();
		try {
			// 获取根节点
			Element root = reader.read(is).getRootElement();
			List<DatasourceElement> datasources = new ArrayList<DatasourceElement>();
			List<MailHost> mailhosts = new ArrayList<MailHost>();
			Host host = new Host();
			// 解析datasource节点
			@SuppressWarnings("unchecked")
			Iterator<Element> datasourceIter = root
					.elementIterator("datasource");
			Map<String, JobInfo> jobInfos = new HashMap<String, JobInfo>();
			@SuppressWarnings("unchecked")
			Iterator<Element> jobIter = root.elementIterator("job");
			@SuppressWarnings("unchecked")
			Iterator<Element> emailhostIter = root.elementIterator("mailhost");
			@SuppressWarnings("unchecked")
			Iterator<Element> hostIter = root.elementIterator("homePageHost");
			Element routerElem=root.element("router");
			String routerAddr=routerElem.attributeValue("address");
			conf.setRouterAddr(routerAddr);
			while (hostIter.hasNext()) {
				Element webHost = (Element) hostIter.next();
				host.setHost(webHost.attributeValue("host"));
				/*Integer port = 80;
				try {
					port = Integer.valueOf(webHost.attributeValue("port"));
				} catch (Exception e) {
					e.printStackTrace();
				}
				host.setPort(port);*/
			}
			while (emailhostIter.hasNext()) {
				MailHost mailhost = new MailHost();
				Element emailhost = (Element) emailhostIter.next();
				mailhost.setHost(emailhost.attributeValue("host"));
				mailhost.setPassWord(emailhost.attributeValue("passWord"));
				mailhost.setUserName(emailhost.attributeValue("userName"));
				mailhost.setHost(emailhost.attributeValue("host"));
				Integer port = 0;
				try {
					port = Integer.valueOf(emailhost.attributeValue("port"));
				} catch (Exception e) {
					e.printStackTrace();
				}
				mailhost.setPort(port);
				mailhosts.add(mailhost);

			}

			while (jobIter.hasNext()) {
				JobInfo job = new JobInfo();
				Element eJob = (Element) jobIter.next();
				job.setName(eJob.attributeValue("name"));
				Integer repeatCount = 0;
				try {
				    if(eJob.attributeValue("repeatCount")!=null){
				        repeatCount = Integer.valueOf(eJob
	                            .attributeValue("repeatCount"));
				    }
				} catch (Exception e) {
					e.printStackTrace();
				}
				job.setRepeatCount(repeatCount);
				job.setStartDate(eJob.attributeValue("startDate"));
				String intervalStr = eJob.attributeValue("interval");

				Integer interval = 0;
				try {
				    if(intervalStr!=null){
				        interval = Integer.valueOf(intervalStr);
				    }
				} catch (Exception e) {
					e.printStackTrace();
				}
				job.setInterval(interval);
				job.setCronExpression(eJob.attributeValue("cronExpression"));
				job.setClassName(eJob.attributeValue("class"));
				jobInfos.put(eJob.attributeValue("class"), job);
			}
			while (datasourceIter.hasNext()) {
				Element edatasource = (Element) datasourceIter.next();
				DatasourceElement datasource = new DatasourceElement();
				datasource.setName(edatasource.attributeValue("name"));
				datasources.add(datasource);
				Map<String, String> proMap = new HashMap<String, String>();

				@SuppressWarnings("unchecked")
				Iterator<Element> propertyIter = edatasource
						.elementIterator("property");

				while (propertyIter.hasNext()) {
					Element eProperty = propertyIter.next();
					proMap.put(eProperty.attributeValue("name"),
							eProperty.attributeValue("value"));
				}

				datasource.setProMap(proMap);
			}
			conf.setDatasource(datasources);
			conf.setMailhost(mailhosts);
			conf.setJobInfos(jobInfos);
			conf.setHomePageHost(host);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return conf;
	}

	public void setDatasource(List<DatasourceElement> datasource) {
		this.datasource = datasource;
	}

	public String getRouterAddr() {
        return routerAddr;
    }

    public void setRouterAddr(String routerAddr) {
        this.routerAddr = routerAddr;
    }

    public static void main(String[] args) {
		// Map<String, String> dataMap = new HashMap<String, String>();
		// dataMap.put("user", "lzy");
		// System.out.println(Config.getTemplate("couponEmail.html", dataMap));
	}
}
