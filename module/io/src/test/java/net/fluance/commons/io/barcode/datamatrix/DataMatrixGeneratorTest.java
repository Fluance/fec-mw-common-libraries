package net.fluance.commons.io.barcode.datamatrix;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Test;

public class DataMatrixGeneratorTest{

	
	@Test
	public void testGenerate() throws IOException{
		DataMatrixGenerator dmGenerator = new DataMatrixGenerator();
		dmGenerator.generate("out.png", "CAM-654654");
	}
	
	@After
	public void deleteImage() {
		File file = new File("out.png");
		file.delete();
	}
}
