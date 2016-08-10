package com.simdezimon.jtorio.misc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


public class FileUtils {

	public static void saveFile(String path, String text) throws IOException{
		PrintWriter out;
		try {
			out = new PrintWriter(path);
			out.print(text);
			out.close();
		} catch (FileNotFoundException e){
			throw e;
		}
	}
	
	public static String readFile(String path) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, StandardCharsets.UTF_8);
	}

	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
}

