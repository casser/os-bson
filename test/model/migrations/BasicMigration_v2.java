package model.migrations;

import os.bson.BSON;
import os.bson.BsonModel.Info;
import os.bson.migration.MigrationAlgorithm;
import model.Basic;
import model.migrations.old.basic.Basic_v2;

public class BasicMigration_v2 extends MigrationAlgorithm{
	
	@Override
	public byte[] migrate(byte[] document, Info info) {
		Basic_v2 	vO = BSON.decode(document,Basic_v2.class);
		Basic 		vN 	= new Basic();
		
		vN.setId(vO.getId());
		vN.setEmail(vO.getEmail());
		
		vN.setFirstName(getFirstNameFromName(vO.getName()));
		vN.setLastName(getLastNameFromName(vO.getName()));
		
		return BSON.encode(vN);
	}

	private String getFirstNameFromName(String name) {
		return "First "+name;
	}

	private String getLastNameFromName(String name) {
		return "Last "+name;
	}
	
}
