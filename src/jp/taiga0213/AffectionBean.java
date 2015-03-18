package jp.taiga0213;

import java.util.Date;

public class AffectionBean {

	private int id;
	private String appName;
	private String appPackage;
	private String affections;
	private Date date;
	private byte[] appIcon;



	public byte[] getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(byte[] image) {
		this.appIcon = image;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppPackage() {
		return appPackage;
	}

	public void setAppPackage(String appPackage) {
		this.appPackage = appPackage;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getAffections() {
		return affections;
	}

	public void setAffections(String affections) {
		this.affections = affections;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


}
