package os.bson.migration;

import os.bson.BsonModel.Info;



public class ModelMigrationAlgorithm extends MigrationAlgorithm {
	
	public static String migrationsPackage;
	
	public static void init(String string) {
		migrationsPackage = string;
	}
	
	
	@Override
	public byte[] migrate(byte[] document, Info info) {
		byte[] newDocument = document;
		
		while(info.getMinVersion()<info.getMaxVersion()){
			try {
				System.out.println(info.toString());
				String scriptName = migrationsPackage+"."+info.getModelType().getSimpleName()+"Migration_v"+(info.getMinVersion());
				Class<?> scriptClass = Class.forName(scriptName);
				MigrationAlgorithm script = (MigrationAlgorithm) scriptClass.newInstance();
				newDocument = script.migrate(newDocument, info);
				info.reset(newDocument);
			} catch (Exception e) {
				info.migrate(info.getMinVersion()+1);
				e.printStackTrace();
			}	
		}
		System.out.println("FINNALLY \n"+info.toString());
		return newDocument;
	}
	
}
