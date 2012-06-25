package os.bson.migration;

import os.bson.BsonModel.Info;

abstract public class MigrationAlgorithm  {
	
	abstract public byte[] migrate(byte[] document, Info info);
	
}
