package com.jadecross.perflab.oom;

import java.io.Serializable;
import java.util.UUID;

public class Member implements Serializable {
	private static final long serialVersionUID = 1L;

	public String name;
	public String age;
	public String sex;
	public String address;

	public Member() {
		this.name = "name_" + UUID.randomUUID().toString();
		this.age = "age_" + UUID.randomUUID().toString();
		this.sex = "sex_" + UUID.randomUUID().toString();
		this.address = "address_" + UUID.randomUUID().toString();
	}
}
