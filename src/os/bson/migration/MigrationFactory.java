package os.bson.migration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import os.bson.BsonModel;


public class MigrationFactory {
	
	private static final Map<Class<? extends BsonModel>, Class<? extends MigrationAlgorithm>> algorithms =
	   new ConcurrentHashMap<Class<? extends BsonModel>, Class<? extends MigrationAlgorithm>>();
	
	public static <T extends BsonModel> void registerAlgorithm(Class<T> modelType, Class<? extends MigrationAlgorithm> algorithmType){
		algorithms.put(modelType, algorithmType);
	}
	
	public static <T extends BsonModel> MigrationAlgorithm createAlgorithm(Class<T> type) {
		try {
			Class<? extends MigrationAlgorithm> clazz = algorithms.get(BsonModel.class);
			if(algorithms.containsKey(type)){
				clazz = algorithms.get(type);
			}
			if(clazz!=null){
				return clazz.newInstance();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
