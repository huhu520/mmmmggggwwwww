
package com.hx.hxchat.domain;


/**
 * 用于封装搜索时返回的 用户
 * @author huyan
 *
 */
public class UserFriend
{
	/**
	 * 环信userid
	 */
	private String m_id;
	/**
	 * nick
	 */
	private String m_name;
	/**
	 * 头像路径
	 */
	private String m_imagePath;
	
	public UserFriend(String id, String name, String imagePath)
	{
		m_id = id;
		m_name = name;
		m_imagePath = imagePath;
	}
	
	public String getM_id()
	{
		return m_id;
	}

	public String getM_name()
	{
		return m_name;
	}
	
	public String getM_imagePath()
	{
		return m_imagePath;
	}
}
