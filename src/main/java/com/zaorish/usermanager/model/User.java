package com.zaorish.usermanager.model;

import java.util.Objects;
import java.util.UUID;

import static java.util.UUID.randomUUID;

public class User {

	private UUID id = randomUUID();

	private String firstname;

	private String lastname;

	private String nickname;

	private String password;

	private String email;

	private String country;

	public User(String nickname, String password, String email) {
		this.nickname = nickname;
		this.password = password;
		this.email = email;
	}

	public UUID getId() {
		return id;
	}

	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public User withFirstname(String firstname) {
		this.firstname = firstname;
		return this;
	}

	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public User withLastname(String lastname) {
		this.lastname = lastname;
		return this;
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
	public User from(String country) {
		this.country = country;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getFirstname(), getLastname(), getNickname(), getPassword(), getEmail(), getCountry());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return Objects.equals(getId(), user.getId()) &&
				Objects.equals(getFirstname(), user.getFirstname()) &&
				Objects.equals(getLastname(), user.getLastname()) &&
				Objects.equals(getNickname(), user.getNickname()) &&
				Objects.equals(getPassword(), user.getPassword()) &&
				Objects.equals(getEmail(), user.getEmail()) &&
				Objects.equals(getCountry(), user.getCountry());
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + getId() +
				", firstname='" + getFirstname() + '\'' +
				", lastname='" + getLastname() + '\'' +
				", nickname='" + getNickname() + '\'' +
				", email='" + getEmail() + '\'' +
				", country='" + getCountry() + '\'' +
				'}';
	}

}
