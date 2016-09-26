package com.zaorish.usermanager.model;

public class UserResource {

	private String firstname;

	private String lastname;

	private String nickname;

	private String password;

	private String email;

	private String country;

	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "UserResource{" +
				"firstname='" + getFirstname() + '\'' +
				", lastname='" + getLastname() + '\'' +
				", nickname='" + getNickname() + '\'' +
				", email='" + getEmail() + '\'' +
				", country='" + getCountry() + '\'' +
				'}';
	}

}
