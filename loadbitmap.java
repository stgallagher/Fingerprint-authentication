import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class loadbitmap {

	private static final int IMAGE_START = 14400;
	private static final int IMAGE_END = 71999;
	private static final int IMAGE_WIDTH = 240;
	private static final int IMAGE_HEIGHT = 360;
	private static final int NORMALIZATION_END = 71519;
	
	public static void processImages() {
		try {
				// read image into buffer
				BufferedImage img = ImageIO.read(new File("sample_fingerprint_grayscale.bmp"));
				final int w = img.getWidth();
				final int h = img.getHeight();
				
				// put image data in a writable raster then place values in an int array
				final WritableRaster wr = (WritableRaster) img.getData();
				int[] imageValues = null;
				imageValues = wr.getPixels(0, 0, w, h, imageValues);
					
				// binarize image
				binarizationSampler( 15, 60, imageValues);
			
				wr.setPixels(0, 0, w, h, imageValues);
				img.setData(wr);
				ImageIO.write(img, "bmp", new File("binarization.bmp"));
				
				// normalize image
				normalizationSampler(imageValues);
				
				wr.setPixels(0, 0, w, h, imageValues);
				img.setData(wr);
				ImageIO.write(img, "bmp", new File("normalization.bmp"));
			
		} catch (IOException e) {
			System.out.println("Image load error");
			e.printStackTrace();
		}
	}
	
	// ---------------------------------------------------------------------------------------------------
	// Binarization filter 
	// ---------------------------------------------------------------------------------------------------
	
	
	// Full-image scanner for binarization
	private static void binarizationSampler(int partitionLength, int partitionWidth, int[] image)
	{
		int currentAverage = 0;
		int sum = 0;
		
		// Get average then binarize based on local average
		for(int i = IMAGE_START; i < (IMAGE_END - partitionLength * partitionWidth); i += partitionWidth)
		{
			currentAverage = sampleAverager(i, partitionLength, partitionWidth, image);
			sum += currentAverage;
			
			binarizationFilter(i, partitionLength, partitionWidth, currentAverage, image);
		}
	}
	
	// Determines average of a bounded sample
	private static int sampleAverager(int start, int height, int width, int[] image)
	{
		int sampleLength = height * width;
		int sampleSum = 0;
		int sampleAverage = 0;
		
		for(int i = start; i < (start + sampleLength); i++)
		{
			sampleSum += image[i];
		}
		sampleAverage = sampleSum/sampleLength;
		return sampleAverage;
	}
	
	// Binarize using local area average as threshold value
	private static void binarizationFilter(int start, int height, int width, int average, int[] image)
	{
		int sampleLength = height * width;
		
		for(int i = start; i < (start + sampleLength); i++)
		{
			if(image[i] < average)
			{
				image[i] = 0;
			}
			else
			{
				image[i] = 255;
			}
		}
	}
	
	//-----------------------------------------------------------------------------------------------------
	

	// ----------------------------------------------------------------------------------------------------
	// Normalization filter
	//-----------------------------------------------------------------------------------------------------
	
	// Full image scanner for normalization
	private static void normalizationSampler(int[] image)
	{
		int partitionWidth = 3;
		
		for(int i = IMAGE_START; i < NORMALIZATION_END; i++)
		{
			sampleNormalizer(i, partitionWidth, image);	
		}
	}
	
	// Eliminates lone pixels, gets a 3 X 3 sample, if center pixel is black 
	// and surrounding pixels are white, changes center pixel to white
	private static void sampleNormalizer(int start, int width, int[] image)
	{	
		int [] normalSample;
		normalSample = new int[9];
		
		for(int i = 0; i < 3; i++)
		{
			normalSample[i] = image[start + i];
			normalSample[i + width] = image[start + i + IMAGE_WIDTH];
			normalSample[i + (2 * width)] = image[start + i + (2 * IMAGE_WIDTH)];
		}
		
		if(normalSample[4] == 0 && areSurroundingPixelsAllWhite(normalSample))
		{
			image[start + IMAGE_WIDTH + 1] = 255;
		}
	}
	
	// Determines if surrounding pixels are all white
	private static boolean areSurroundingPixelsAllWhite(int [] sample)
	{
		boolean pixelsAllWhite = true;
		
		for(int i = 0; i < sample.length; i++)
		{
			if(i != 4)
			{
				if(sample[i] == 0)
				{
					return pixelsAllWhite = false;
				}
			}
		}
		return pixelsAllWhite;
	}
	//------------------------------------------------------------------------------------------------------
	
	
	// -----------------------------------------------------------------------------------------------------
	// Development helpers
	//------------------------------------------------------------------------------------------------------
	
	private static void displayArrayContents(int [] sample)
	{
		for(int i = 0; i < sample.length; i++)
		{
			System.out.println("At index " + i + " value is " + sample[i]);
		}
	}
}

