package os.bson;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface BsonEncodable {
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface Ignore {
	}
	public byte[] encodeBson(); 
}
