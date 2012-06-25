package model.migrations.old.basic;

import os.bson.BsonModel;
import os.bson.BsonModel.Entity;

@Entity(version=2)
public class Basic_v2 implements BsonModel {
	
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
	
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String email) {
		this.name = email;
	}
	
	public Basic_v2() {
		
	}
	
	public Basic_v2(String id,String email) {
		this.setId(id);
		this.setEmail(email);
	}
	
	@Override
	public String toString() {
		return getId()+"<"+getEmail()+" "+getName()+"> "+info();
	}

	
}
