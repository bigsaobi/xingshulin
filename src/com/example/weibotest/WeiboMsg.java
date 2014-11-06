package com.example.weibotest;

import java.util.Date;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "WeiboMsg")
public class WeiboMsg extends Model {
	// @Column(name = "Name")
	// public String name;
	// @Column(name = "_id")
	// public int _id;
	// @Column(name = "time")
	// public Date time;

	@Column(name = "msgid")
	public String msgid;
	@Column(name = "username")
	public String username;
	@Column(name = "userimage")
	public String userimage;
	@Column(name = "createtime")
	public long createtime;
	@Column(name = "content")
	public String content;

	public WeiboMsg() {
		super();
	}

	public WeiboMsg(String msgid, String username, String userimage,
			long createtime, String content) {
		this.msgid = msgid;
		this.username = username;
		this.userimage = userimage;
		this.createtime = createtime;
		this.content = content;
	}

	public String toString() {
		return getId() + "-------" + msgid + "--" + username + "--" + userimage
				+ "--" + new Date(createtime).toString() + "---" + content;
	}
}
