package net.fluance.commons.io.barcode.datamatrix;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.krysalis.barcode4j.impl.datamatrix.DataMatrixBean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

import net.fluance.commons.io.barcode.FluanceBarcodeGenerator;

public class DataMatrixGenerator implements FluanceBarcodeGenerator {

	@Override
	public void generate(String filePath, String code) throws IOException {
		if (code == null || code.isEmpty()) {
			throw new IllegalArgumentException("Code Must not be null");
		}
		DataMatrixBean bean = new DataMatrixBean();
		final int dpi = 600;
		bean.setModuleWidth(UnitConv.in2mm(2.0f / dpi));
		bean.doQuietZone(false);
		File outputFile = new File(filePath);
		OutputStream out = new FileOutputStream(outputFile);
		try {
			BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, "image/x-png", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);
			bean.generateBarcode(canvas, code);
			canvas.finish();
		} finally {
			out.close();
		}
	}
}
