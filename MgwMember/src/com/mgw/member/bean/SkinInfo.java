package com.mgw.member.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 皮肤bean
 * Created by huyan .
 */
public class SkinInfo implements Serializable {

	private long id;// 皮肤的id
	/** 皮肤的名字 */
	private String skinName;
	
	/** 皮肤的icon */
	private String skinIconUrl;
	
	/** 皮肤的zip包名 */
	private String skinPackageName;
	/** 皮肤下载路径 */
	private String skinDownloadUrl;
	/** 是否本地有 0 无 ，1有 */
	private String isLocalExist;
	/** 皮肤包的大小 */
	private long size;
	/** 皮肤包的本地保存路径 */
	private String localExistUrl;

	
	
	
	public SkinInfo(long id, String skinName, String skinPackageName, String skinDownloadUrl) {
		super();
		this.id = id;
		this.skinName = skinName;
		this.skinPackageName = skinPackageName;
		this.skinDownloadUrl = skinDownloadUrl;
	}
	public SkinInfo(long id, String skinName, String skinPackageName, String skinDownloadUrl,String isLocalExist) {
		super();
		this.id = id;
		this.skinName = skinName;
		this.skinPackageName = skinPackageName;
		this.skinDownloadUrl = skinDownloadUrl;
		this.isLocalExist = isLocalExist;
	}

	public String getSkinIconUrl() {
		return skinIconUrl;
	}

	public void setSkinIconUrl(String skinIconUrl) {
		this.skinIconUrl = skinIconUrl;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSkinName() {
		return skinName;
	}

	public void setSkinName(String skinName) {
		this.skinName = skinName;
	}

	public String getSkinPackageName() {
		return skinPackageName;
	}

	public void setSkinPackageName(String skinPackageName) {
		this.skinPackageName = skinPackageName;
	}

	public String getSkinDownloadUrl() {
		return skinDownloadUrl;
	}

	public void setSkinDownloadUrl(String skinDownloadUrl) {
		skinDownloadUrl = skinDownloadUrl;
	}

	public String getIsLocalExist() {
		return isLocalExist;
	}

	public void setIsLocalExist(String isLocalExist) {
		this.isLocalExist = isLocalExist;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getLocalExistUrl() {
		return localExistUrl;
	}

	public void setLocalExistUrl(String localExistUrl) {
		this.localExistUrl = localExistUrl;
	}

	@Override
	public String toString() {
		return "SkinInfo [id=" + id + ", skinName=" + skinName + ", skinPackageName=" + skinPackageName + ", SkinDownloadUrl=" + skinDownloadUrl + ", isLocalExist=" + isLocalExist + ", size=" + size
				+ ", localExistUrl=" + localExistUrl + "]";
	}

}
