package com.visellico.util;

import java.util.ArrayList;
import java.util.List;

public class BinaryWriter {

	private List<Byte> buffer;
	
	public BinaryWriter() {
		buffer = new ArrayList<Byte>();
	}
	
	public BinaryWriter(int size) {
		buffer = new ArrayList<Byte>(size);
	}
	
	public byte[] getBuffer() {

		Byte[] array = new Byte[buffer.size()];
		buffer.toArray(array);
		
		byte[] result = new byte[buffer.size()];
		
		for (int i = 0; i < result.length; i++) {
			result[i] = array[i];
		}
		
		return result;
		//Wouldn't the below work? Or does a get method do the inefficiency thing 
//		byte[] b = new byte[buffer.size()];
//		for (int i = 0; i < buffer.size(); i++) {
//			b[i] = buffer.get(i);
//		}
//		
//		return b;
		
	}

	public void write(byte[] data) {
		
		for (int i = 0; i < data.length; i++) {

			buffer.add(data[i]);
			
		}
	}
	
	public void write(byte data) {
		byte[] b = new byte[] {data};//ByteBuffer.allocate(4).putInt(data).array();
		write(b);
	}
	
}
