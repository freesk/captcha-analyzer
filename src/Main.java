import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.ArrayList;

public class Main {
	
	public static void main(String[] args) {
		
		String dest = "";
		
		// number of digits in a row 
		final int NUM = 5;
		
		try {
			dest = args[0];
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Error: The file has not been specified");
			return;
		}
		
		BufferedImage image = readImage(dest);
		
		ArrayList<BufferedImage> images = breakIntoPieces(image, NUM);
		
		int i;
		
		ArrayList<Character> collection = new ArrayList<Character>();
		
		for(i = 0; i < images.size(); i++) {
			collection.add('_');
		}
		  
		int f = 256;
		
		while(collection.indexOf('_') >= 0 && f >= 124) {
			
			for(i = 0; i < images.size(); i++) {
				
				String res = analyzeImage(images.get(i), f);
				
				char out;
				
				if(res instanceof String && res.length() > 0) {
					out = res.charAt(0);
				} else {
					out = '_';
				}
				
				if(collection.get(i).equals('_')) {
					collection.set(i, out);
				}
				
				collection.add(out);
			}
			
			for(i = 1; i < images.size() + 1; i++) {	
				System.out.print(collection.get(i - 1) + " ");
				if(i%5 == 0) System.out.print("\n");
			}
			
			f -= 32;
			
			System.out.print("\n");
		}

	}
	
	public static ArrayList<BufferedImage> breakIntoPieces(BufferedImage image, int num) {
		ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
		BufferedImage img;
		
		final int WIDTH = image.getWidth();
		final int HEIGHT = image.getHeight();
		
		for (int i = 0; i < num; i++) {
		    for (int j = 0; j < num; j++) {
		    	img = cropImage(
		    			WIDTH/num, 
		    			j,
		    			HEIGHT/num, 
		    			i,
		    			image
		    	);
		    	images.add(img);
		    }
		}
		
		return images;
	}
	
	public static BufferedImage cropImage(int WIDTH, int x_pos, int HEIGHT,int y_pos, BufferedImage image) {
			
		int c;
		
		BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		
		for (int i = 0; i < HEIGHT; i++) {
		    for (int j = 0; j < WIDTH; j++) {
		    	c = image.getRGB(j + (x_pos * WIDTH), i + (y_pos * HEIGHT));
		    	img.setRGB(j, i, c);
		    }
		}
		
		return img;
	}
	
	public static String analyzeImage(BufferedImage image, int f) {
		
		ArrayList<ArrayList<Integer>> image_as_list = turnImageIntoArray(image, f);
		
		ArrayList<ArrayList<Integer>> zero_one_list = getZeroOneArray(image_as_list);
			
		zero_one_list = cleanZeroOneArray(zero_one_list);
		
//		printArrayList(zero_one_list);
//		System.out.print("\n");
				
		BufferedImage new_image = turnArrayIntoImage(image_as_list, zero_one_list);
				
		saveImage(new_image);
				
		String res = runTerminalCommands();
		
		return res;
	}
	
	public static ArrayList<ArrayList<Integer>> getZeroOneArray(ArrayList<ArrayList<Integer>> l) {
		
		ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> foo;
		
		for(int i = 0; i < l.size(); i++) {
			
			foo = new ArrayList<Integer>();
			
			for(int j = 0; j < l.get(i).size(); j++) {
				if(l.get(i).get(j) == -1) {
					foo.add(0);
				} else {
					foo.add(1);
				}
			}
			
			list.add(foo);
		}
		
		return list;
	}
	
	public static BufferedImage turnArrayIntoImage(ArrayList<ArrayList<Integer>> image_as_list, ArrayList<ArrayList<Integer>> zero_one_list) {
		BufferedImage img = new BufferedImage(image_as_list.get(0).size(), image_as_list.size(), BufferedImage.TYPE_INT_ARGB);
		
		int clr;
		
		for (int i = 0; i < image_as_list.size(); i++) {
		    for (int j = 0; j < image_as_list.get(i).size(); j++) {
		    	
		    	if(zero_one_list.get(i).get(j) != 0)
		    		clr = image_as_list.get(i).get(j);  
		        else
		        	clr = -1;
		    	
		    	try {
		    		img.setRGB(j, i, clr);
		    	} catch (ArrayIndexOutOfBoundsException e) {
		    		System.out.println("Error: " + e.getMessage());
		    	}
		    	
		    }
		}
		
		return img;
	}
	
	public static BufferedImage turnArrayIntoImage(ArrayList<ArrayList<Integer>> image_as_list) {
		BufferedImage img = new BufferedImage(image_as_list.get(0).size(), image_as_list.size(), BufferedImage.TYPE_INT_ARGB);
		
		for (int i = 0; i < image_as_list.size(); i++) {
		    for (int j = 0; j < image_as_list.get(i).size(); j++) {
		    	
		    	int clr = image_as_list.get(i).get(j);  
		    	
		    	try {
		    		img.setRGB(j, i, clr);
		    	} catch (ArrayIndexOutOfBoundsException e) {
		    		System.out.println("Error: " + e.getMessage());
		    	}
		    	
		    }
		}
		
		return img;
	}
	
	public static ArrayList<ArrayList<Integer>> turnImageIntoArray(BufferedImage image, int f) {
		// Define a two-dimensional list 
		ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();
		// Define an inner list 
		ArrayList<Integer> foo;
		
		final int WIDTH = image.getWidth();
		final int HEIGHT = image.getHeight();
		
		int clr;
		
		for (int i = 0; i < HEIGHT; i++) {
			foo = new ArrayList<Integer>();
		    for (int j = 0; j < WIDTH; j++) {
		    	
		    	try {
		    		clr = image.getRGB(j, i);
		    	} catch (ArrayIndexOutOfBoundsException e) {
		    		System.out.println("Error: " + e.getMessage());
		    		clr = -1;
		    	}
		    	
		    	clr = filterColorBrightness(clr, f);
		    	
		        foo.add(clr);
		    }
		    
		    list.add(foo);
		    
		}
		
		return list;
		
	}
	
	public static int filterColorBrightness(int clr, int max) {
		
		int c = clr;
		
		int a = (clr>>24)&0xff;
		int r = (clr>>16)&0xff;
		int g = (clr>>8)&0xff;
		int b = clr&0xff;
		
		double l = 0.2126*(double)r + 0.7152*(double)g + 0.0722*(double)b;
		
		// 128 <=> 256
		if(l > max) c = -1;
		
		return c;
	}
	
	public static BufferedImage readImage(String dest) {
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		try {
			img = ImageIO.read(new File(dest));
		} catch (IOException e) {
			System.out.println("Error: Cannot read the file");
		}
		return img;
	}
	
	public static void saveImage(BufferedImage image) {
		try {
			
			File f = new File(".workspace");
			if (!f.exists()) {
				File dir = new File(".workspace");
				dir.mkdir();
			}
			
			File outputfile = new File(".workspace/output.png");
		    ImageIO.write(image, "png", outputfile);
			
		} catch (IOException e) {
		    System.out.println("Error: " + "Cannot save a new image");
		}
	}
	
	public static String runTerminalCommands() {
		String[] cmd_1 = {"/usr/local/bin/convert", ".workspace/output.png", ".workspace/output.ppm"};
		
		String cmd_2_res = "";
		
		try {
			execCmd(cmd_1);
		} catch (IOException e) {
			System.out.println("Error: " + "Cannot convert the image to ppm");
		}
		
		String[] cmd_2 = {"/usr/local/bin/ocrad", "--filter=numbers_only", ".workspace/output.ppm"};
		
		try {
			cmd_2_res = execCmd(cmd_2);
		} catch (IOException e) {
			System.out.println("Error: " + "Cannot start ocrad");
		}
		
		return cmd_2_res;
	}
	
	public static String execCmd(String[] cmd) throws java.io.IOException {
	    Process proc = Runtime.getRuntime().exec(cmd);
	    java.io.InputStream is = proc.getInputStream();
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    String val = "";
	    if (s.hasNext()) {
	        val = s.next();
	    }
	    else {
	        val = "";
	    }
	    return val;
	}
	
	private static void printArrayList(ArrayList<ArrayList<Integer>> list) {
		for(int i = 0; i < list.size(); i++) {
			for(int j = 0; j < list.get(i).size(); j++) {
				System.out.print(list.get(i).get(j));
			}
			System.out.print("\n");
		}
	}
	
	private static ArrayList<ArrayList<Integer>> clean_h(ArrayList<ArrayList<Integer>> list) {
		
		ArrayList<ArrayList<Integer>> current_list = list;
		ArrayList<Integer> current_sub_list;
		
		int i, j;
		
		for(i = 0; i < list.size(); i++) {
			
			current_sub_list = current_list.get(i); 
			
			for(j = 0; j < current_sub_list.size() - 4; j++) {
				
				int pix_1 = current_sub_list.get(j);
				int pix_2 = current_sub_list.get(j+1);
				int pix_3 = current_sub_list.get(j+2);
				int pix_4 = current_sub_list.get(j+3);
				
				if (pix_1 == 0 && pix_2 != 0 && pix_3 != 0 && pix_4 == 0) {
					pix_2 = 0;
					pix_3 = 0;
				} else if (pix_1 == 0 && pix_2 == 0 && pix_3 != 0 && pix_4 == 0) {
					pix_3 = 0;
				} else if (pix_1 == 0 && pix_2 != 0 && pix_3 == 0 && pix_4 == 0) {
					pix_2 = 0;
				} 
 				
				current_sub_list.set(j+1, pix_2);
				current_sub_list.set(j+2, pix_3);
				
			}
			
			current_list.set(i, current_sub_list);
			
		}
		
		return current_list;
		
	}
	
	private static ArrayList<ArrayList<Integer>> clean_v(ArrayList<ArrayList<Integer>> list) {
	
		ArrayList<ArrayList<Integer>> current_list = list;
		ArrayList<Integer> current_sub_list;
		
		int i, j;
		
		for(i = 1; i < list.size()-1; i++) {
			
			current_sub_list = current_list.get(i); 
			
			for(j = 0; j < current_sub_list.size(); j++) {
				
				if(list.get(i-1).get(j) == 0 && list.get(i).get(j) != 0 && list.get(i+1).get(j) == 0) {
					current_sub_list.set(j, 0);
				}
				
			}
		
			current_list.set(i, current_sub_list);
			
		}
		
		return current_list;
	}
	
	private static ArrayList<ArrayList<Integer>> cleanZeroOneArray(ArrayList<ArrayList<Integer>> l) {
		
		ArrayList<ArrayList<Integer>> list = l;
		
		list = clean_h(list);
		list = clean_v(list);
		
		return list;
	}
	
	
	private static BufferedImage rotateImage(BufferedImage image) {
		// The required drawing location
//		int drawLocationX = 300;
//		int drawLocationY = 300;

		// Rotation information

//		double rotationRequired = Math.toRadians (45);
//		double locationX = image.getWidth() / 2;
//		double locationY = image.getHeight() / 2;
//		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
//		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

		// Drawing the rotated image at the required drawing locations
//		g2d.drawImage(op.filter(image, null), drawLocationX, drawLocationY, null);
		
		return image;
		
	}
}

