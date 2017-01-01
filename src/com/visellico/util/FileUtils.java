package com.visellico.util;

import java.io.File;
import java.util.List;

public class FileUtils {

	private FileUtils() {
	}
	
	private int fileNameIncrement = 0;
	public static String availableName (String path, String desiredName, String extension) {
		
		
		return availableNameInc(path, desiredName, extension, -1);
		
	}
	
	private static String availableNameInc (String path, String desiredName, String extension, int increment) {
		
		File file;
		if (increment == -1) {
			file = new File(path + File.separator + desiredName + extension);
		} else {
			file = new File(path + File.separator + desiredName + Integer.toString(increment) + extension);
		}
		
		if (!file.exists()) {
			return desiredName + (increment == -1 ? "" : Integer.toString(increment));
		} else {
			return availableNameInc(path, desiredName, extension, ++increment);
		}
		
		
	}
	
	public static String stripExtension(String name) {
		if (name.indexOf(".") > 0) name = name.substring(0, name.lastIndexOf("."));
		return name;
	}
	
	public static String stripPath(String name) {
		if (name.lastIndexOf(File.separator) > 0) name = name.substring(name.lastIndexOf(File.separator) + 1);
		if (name.lastIndexOf("/") > 0) name = name.substring(name.lastIndexOf("/") + 1);
		return name;
	}
	
	/**
	 * Stuffs all of the file/directory names inside a directory into the given List<String>
	 * @param dirPath
	 * @param loc
	 */
	public static void loadNames(String dirPath, List<String> loc) {
		File parentDirectory = new File(dirPath);
		String[] fileNames = parentDirectory.list();
		
		for (int i = 0; i < fileNames.length; i++) {
			String name = fileNames[i];
//			if (name.indexOf(".") > 0) name = name.substring(0, name.lastIndexOf("."));
			name = stripExtension(name);
			loc.add(name);
		}
	}
	
}
