package os.bson;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import os.bson.binary.MD5;
import os.utils.BytesUtil;


public interface BsonModel {
	
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface Entity {
		int version() default 1;
		String name() default "";
		byte model()  default 0;
	}
		
	public static class Info implements BsonDecodable, BsonEncodable{
		
		public static final int LENGTH = 68;
		
		public Integer version;
		public Integer revision;
		public Integer flags;
		public Integer createdAt;
		public Integer updatedAt;
		public MD5 hash;
		
		private Boolean isValid;
		private Boolean isModified;
		private Boolean isMigrated;
		private Boolean isFresh;
		
		private Class<? extends BsonModel> model;
		
		public Boolean isValid(){
			return isValid!=null && isValid;
		}
		
		public Boolean isFresh(){
			return isFresh==null && isFresh;
		}
		
		public Boolean isNew(){
			return isModified==null;
		}
		
		public Boolean isModified(){
			return isModified!=null && isModified;
		}
		
		public Boolean isMigrated(){
			return isMigrated!=null && isMigrated;
		}
		
		public Boolean isMigratable(){
			return getMaxVersion()>getMinVersion();
		}
		
		public Integer getMaxVersion() {
			return model.getAnnotation(Entity.class).version();
		}
		
		public Integer getMinVersion() {
			return version;
		}
		
		@SuppressWarnings("unchecked")
		public Info(Class<?> cls) throws Exception {
			if(BsonModel.class.isAssignableFrom(cls) && cls.isAnnotationPresent(Entity.class)){
				this.model	   = (Class<? extends BsonModel>) cls;
				this.version   = 1;
				this.revision  = 0;
				this.flags	   = 0;
				this.createdAt = Long.valueOf(System.currentTimeMillis()/1000).intValue();
				this.updatedAt = 0;
				this.hash 	   = null;
			}else{
				throw new Exception("Invalid Bson Model");
			}
		}
		
		public Info(byte[] bytes, Class<?> cls) throws Exception {
			this(cls);
			reset(bytes);
		}
		
		public void reset(byte[] bytes){
			if(bytes.length==LENGTH){
				decodeBson(bytes);	
			}else if(bytes.length>=LENGTH+5){
				decodeBson(BytesUtil.readBytes(bytes, 4, LENGTH));
				validate(bytes);
			}
		}
		
		@Override
		public byte[] encodeBson() {
			BsonByteArray bson = new BsonByteArray(LENGTH);
			
			bson.writeByte(BSON.DOCUMENT);
			bson.writeCString("__");
			
			bson.writeInt(LENGTH-4);
			
			bson.writeByte(BSON.INT32);
			bson.writeCString("f");
			bson.writeInt(flags);
			
			bson.writeByte(BSON.INT32);
			bson.writeCString("v");
			bson.writeInt(version);
			
			bson.writeByte(BSON.INT32);
			bson.writeCString("r");
			bson.writeInt(revision);
						
			bson.writeByte(BSON.BINARY);
			bson.writeCString("h");
			bson.writeBinary(hash);
			
			bson.writeByte(BSON.INT32);
			bson.writeCString("c");
			bson.writeInt(createdAt);
			
			bson.writeByte(BSON.INT32);
			bson.writeCString("u");
			bson.writeInt(updatedAt);
			
			bson.writeByte(BSON.TERMINATOR);
			
			return bson.array();
		}

		@Override
		public void decodeBson(byte[] bytes) {
			BsonByteArray bson = new BsonByteArray(bytes.length);
			bson.write(0, bytes);
			
			bson.readByte();
			bson.readCString();
			
			bson.readInt();
			
			bson.readByte();
			bson.readCString();
			this.flags = bson.readInt();
			
			bson.readByte();
			bson.readCString();
			this.version = bson.readInt();
						
			bson.readByte();
			bson.readCString();
			this.revision = bson.readInt();
			
			bson.readByte();
			bson.readCString();
			this.hash = new MD5(bson.readBinary());
			
			bson.readByte();
			bson.readCString();
			this.createdAt = bson.readInt();
			
			bson.readByte();
			bson.readCString();
			this.updatedAt = bson.readInt();
			
			bson.readByte();
		}
		
		@Override
		public String toString() {
			return "BMI["+model.getSimpleName()+"] "+version+"."+revision+" "+hash+" "+createdAt+" "+updatedAt+" "+
				(isValid()			? "V  ":"v  ")+
				(isNew()			? "N  ":"n  ")+
				(isModified()		? "M  ":"m  ")+
				(isMigratable()		? "MN ":"mn ")+
				(isMigrated()		? "MD" :"md" );
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj!=null && obj.getClass().equals(Info.class)){
				Info target = (Info)obj;
				return 
					this.version.equals(target.version)     &&
					this.revision.equals(target.revision)   &&
					this.createdAt.equals(target.createdAt) &&
					this.updatedAt.equals(target.updatedAt) &&
					this.hash.equals(target.hash);
			}else{
				return false;
			}
		}
		
		public Boolean validate(byte[] buffer) {
			byte[] hash     = BytesUtil.readBytes(buffer, 41, 16);
			this.isValid 	= this.hash.equals(hash);
			return isValid;
		}
		
		public void commit(byte[] buffer) {
			int length  = BytesUtil.readInt(buffer, 0);
			MD5 hash    = new MD5(buffer,LENGTH+4,length-LENGTH-5);
			if(this.hash==null || !this.hash.equals(hash)){
				this.version	= getMaxVersion();
				upgrade(hash);
			}else{
				this.isModified	= false;
			}
			BytesUtil.write(buffer, 4, encodeBson());
		}

		public void migrate(Integer version) {
			this.version 	= version;
			this.isMigrated = true;
		}
		
		private void upgrade(MD5 hash) {
			this.hash		=  hash;
			this.isModified =  true;
			this.isValid	=  true;
			this.revision	+= 1;
			this.updatedAt	=  Long.valueOf(System.currentTimeMillis()/1000).intValue();
		}

		public Class<? extends BsonModel> getModelType() {
			return model;
		}

		public void fresh(Boolean isFresh) {
			this.isFresh = isFresh;
		}
		
	}
	
	public Object id();
	public void id(Object value);
	
	public BsonModel.Info info();
	public void info(BsonModel.Info info);
	
}
