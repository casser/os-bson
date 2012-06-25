import java.util.Arrays;
import java.util.Comparator;
import java.util.UUID;

import os.utils.Base32;
import os.utils.BytesUtil;
import os.utils.TimeUtil;



public class BsonTest {
	
	private static byte[] BSIC_V1 =new byte[]{
		(byte)0x77, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x03, (byte)0x5f, (byte)0x69, (byte)0x00, (byte)0x47, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x10, (byte)0x6d, (byte)0x00, (byte)0x01, 
		(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x10, (byte)0x76, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x10, (byte)0x72, (byte)0x00, (byte)0x03, (byte)0x00, (byte)0x00, 
		(byte)0x00, (byte)0x05, (byte)0x68, (byte)0x00, (byte)0x10, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x05, (byte)0x47, (byte)0xc4, (byte)0x16, (byte)0xd2, (byte)0x3f, (byte)0x22, (byte)0xa4, 
		(byte)0x44, (byte)0x05, (byte)0x42, (byte)0xa6, (byte)0x4b, (byte)0x07, (byte)0xde, (byte)0xe9, (byte)0x08, (byte)0x12, (byte)0x63, (byte)0x00, (byte)0xc3, (byte)0xf1, (byte)0x82, (byte)0xcc, 
		(byte)0x36, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x12, (byte)0x75, (byte)0x00, (byte)0xd2, (byte)0xf1, (byte)0x82, (byte)0xcc, (byte)0x36, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, 
		(byte)0x02, (byte)0x5f, (byte)0x69, (byte)0x64, (byte)0x00, (byte)0x03, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x42, (byte)0x33, (byte)0x00, (byte)0x02, (byte)0x65, (byte)0x6d, (byte)0x61, 
		(byte)0x69, (byte)0x6c, (byte)0x00, (byte)0x0f, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x67, (byte)0x72, (byte)0x69, (byte)0x73, (byte)0x68, (byte)0x61, (byte)0x40, (byte)0x6a, (byte)0x61, 
		(byte)0x6e, (byte)0x2e, (byte)0x63, (byte)0x6f, (byte)0x6d, (byte)0x00, (byte)0x00
	};
	
	public static void main(String[] args) throws Exception{
		/*
		ModelMigrationAlgorithm.init("model.migrations");
		
		MigrationFactory.registerAlgorithm(BsonModel.class,ModelMigrationAlgorithm.class);
		*/
		/*
		BsonModel.Info mi1 = new BsonModel.Info(Basic.class);
		byte[] mib = mi1.encodeBson();
		BsonModel.Info mi2 = new BsonModel.Info(mib,Basic.class);
		
		ByteArray.printHexString(mib);
		print(mib.length);
		print(mi1);
		print(mi2);
		print(mi2.equals(mi2));
		*/
		/*
		print(BSON.decode(BSIC_V1));
		Basic b1    = BSON.decode(BSIC_V1,Basic.class);
		print(b1);*/
		/*for(int i=0;i<100;i++){
			print("--------------------------------------");
			
			print(System.currentTimeMillis()+"."+Long.toHexString(index));
			while(time==System.currentTimeMillis()){
				index++;
			}
			print(Long.toHexString(System.currentTimeMillis())+"."+Long.toHexString(index));
		}*/
		//print(new UUID(1L, 1L).toString().replace("-", ""));
		Task[] tasks = new Task[500];
		long[] bytes = new long[Task.ITERATIONS*tasks.length];
		
		for(int t=0;t<tasks.length;t++){
			tasks[t] = new Task(t);
			tasks[t].start();
		}
			
		for(int t=0;t<tasks.length;t++){
			System.arraycopy(tasks[t].get(), 0, bytes, t*Task.ITERATIONS, Task.ITERATIONS);
		}
		for(int b=0;b<tasks.length;b++){
			try {
				tasks[b].join();
				System.arraycopy(tasks[b].get(), 0, bytes, b*Task.ITERATIONS, Task.ITERATIONS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		/*bytes = new long[]{
			0x329009dE94d0dL,
			0x329009d394d0dL,
			0x32A009d394d0dL,
			0x329009d394d0dL,
			0x329009dB94d0dL,
			0x329f09d394d0dL,
			0x329009d394d0dL
		};*/
		Arrays.sort(bytes);
		
		for(int b=0;b<bytes.length-1;b++){
			if(bytes[b]==bytes[b+1]){
				print(Long.toHexString(bytes[b]));	
			}
		}
		
		print(TimeUtil.max);
		
		print(BytesUtil.toHexBE(System.currentTimeMillis())+" "+System.currentTimeMillis());
		print(BytesUtil.toHexBE(System.nanoTime())+" "+System.currentTimeMillis());
		print(BytesUtil.toHexBE(TimeUtil.getSuperNanoTime())+" "+TimeUtil.getSuperNanoTime());
		print(Base32.encode(BytesUtil.toBytesBE(TimeUtil.getSuperNanoTime((byte)0xFF)))+" "+BytesUtil.toHexBE(TimeUtil.getSuperNanoTime((byte)0xFF))+" "+TimeUtil.getSuperNanoTime((byte)0x7F));
	}
	
	
	private static class Task extends Thread{
		public static final int ITERATIONS = 10000;
		private final long[] res = new long[ITERATIONS];
		private final int index ;
		public Task(int i){
			super("T"+i);
			index = i;
		}
		public long[] get(){
			return res;
		}
		
		@Override
		public void run() {
			for(int i=0;i<res.length;i++){
				res[i]=TimeUtil.getSuperNanoTime((byte)index);
			}
		}
	}
	
	private static void print(Object o) {
		System.out.println(o);
	}
}
