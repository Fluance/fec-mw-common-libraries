package net.fluance.commons.io.barcode;

import java.io.IOException;

public interface FluanceBarcodeGenerator {

	public void generate(String filePath, String code) throws IOException;
}
