package os.bson;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface BsonModel {
	
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface Entity {
		int version() default 1;
		String collection() default "";
		byte model()  default 0;
	}
		
	
	public Object id();
	public void id(Object value);
	
}
