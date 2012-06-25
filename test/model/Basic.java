package model;

import os.bson.BsonModel;
import os.bson.BsonModel.Entity;

@Entity(version=3)
public class Basic implements BsonModel {
	
	private String id;
	@Override
	public Object id() {
		return getId();
	}
	@Override
	public void id(Object value) {
		setId((String)value);
	}
	
	private BsonModel.Info info;
	@Override
	public BsonModel.Info info() {
		return info;
	}
	@Override
	public void info(BsonModel.Info info) {
		this.info = info;
	}
		
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	private String email;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	private String firstName;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	private String lastName;
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public Basic() {
	}
	
	public Basic(String id,String email) {
		this.setId(id);
		this.setEmail(email);
	}
	
	@Override
	public String toString() {
		return getId()+"<"+getEmail()+" "+getFirstName()+" "+getLastName()+">";
	}

	
}
