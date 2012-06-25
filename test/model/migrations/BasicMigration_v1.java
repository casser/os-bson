package model.migrations;

import os.bson.BSON;
import os.bson.BsonModel.Info;
import os.bson.migration.MigrationAlgorithm;
import model.migrations.old.basic.Basic_v1;
import model.migrations.old.basic.Basic_v2;

public class BasicMigration_v1 extends MigrationAlgorithm{
	
	@Override
	public byte[] migrate(byte[] document, Info info) {
		Basic_v1 v1 = BSON.decode(document,Basic_v1.class);
		Basic_v2 v2 = new Basic_v2();
		
		v2.setId(v1.getId());
		v2.setEmail(v1.getEmail());
		v2.setName(getNamefromEmail(v1.getEmail()));
		
		return BSON.encode(v2);
	}

	private String getNamefromEmail(String email) {
		return email.substring(0,email.indexOf('@'));
	}
	
}
