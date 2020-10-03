
import javax.swing.*;
import java.awt.*;
import javax.imageio.*;
import ij.IJ;
import ij.*;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.filter.PlugInFilter;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import ij.process.ByteProcessor;
import ij.process.ColorProcessor;
import java.awt.Color;
import java.awt.*;
import java.awt.color.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.image.renderable.*;
import java.util.*;
import javax.media.jai.*;
import javax.media.jai.operator.*;
import javax.media.jai.widget.*;
import java.awt.Frame;
import java.awt.image.renderable.ParameterBlock;
import java.io.IOException;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import com.sun.media.jai.codec.FileSeekableStream;
import javax.media.jai.widget.ScrollingImagePanel;
import ij.*;
import ij.process.ImageConverter;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import ij.process.*;
import ij.gui.*;
import java.awt.Rectangle;
import ij.*;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import java.awt.*;
import java.awt.color.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.image.renderable.*;
import java.util.*;
import javax.media.jai.*;
import javax.media.jai.operator.*;
import javax.media.jai.widget.*;
import java.awt.Frame;
import java.awt.image.renderable.ParameterBlock;
import java.io.IOException;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import com.sun.media.jai.codec.FileSeekableStream;
import ij.*;
import ij.process.ImageConverter;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import ij.process.*;
import ij.gui.Roi.*;
import jxl.CellReferenceHelper;
import jxl.CellView;
import jxl.HeaderFooter;
import jxl.Range;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.Orientation;
import jxl.format.PageOrder;
import jxl.format.PageOrientation;
import jxl.format.PaperSize;
import jxl.format.ScriptStyle;
import jxl.format.UnderlineStyle;
import jxl.write.Blank;
import jxl.write.Boolean;
import jxl.write.DateFormat;
import jxl.write.DateFormats;
import jxl.write.DateTime;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormat;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFeatures;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableHyperlink;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class ALG3_COLOR_FINAL {

    public static ij.io.OpenDialog oi;
    public static double[] G_r;
    public static double[] G_g;
    public static double[] G_b;
    public static double thres_r;
    public static double thres_g;
    public static double thres_b;
    public static int P_1D[];

    public static double[] Moments(int[] data) {

        double total = 0;
        double m0 = 1.0, m1 = 0.0, m2 = 0.0, m3 = 0.0, sum = 0.0, p0 = 0.0;
        double cd, c0, c1, z0, z1;	/* auxiliary variables */
        int threshold = -1;

        double[] histo = new double[data.length];

        for (int i = 0; i < data.length; i++) {
            total += data[i];
        }

        for (int i = 0; i < data.length; i++) {
            histo[i] = (double) (data[i] / total); //normalised histogram
        }
        /* Calculate the first, second, and third order moments */
        for (int i = 0; i < data.length; i++) {
            m1 += i * histo[i];
            m2 += i * i * histo[i];
            m3 += i * i * i * histo[i];
        }
        /* 
         First 4 moments of the gray-level image should match the first 4 moments
         of the target binary image. This leads to 4 equalities whose solutions 
         are given in the Appendix of Ref. 1 
         */
        cd = m0 * m2 - m1 * m1;
        c0 = (-m2 * m2 + m1 * m3) / cd;
        c1 = (m0 * -m3 + m2 * m1) / cd;
        z0 = 0.5 * (-c1 - Math.sqrt(c1 * c1 - 4.0 * c0));
        z1 = 0.5 * (-c1 + Math.sqrt(c1 * c1 - 4.0 * c0));
        p0 = (z1 - m1) / (z1 - z0);  /* Fraction of the object pixels in the target binary image */

        // The threshold is the gray-level closest  
        // to the p0-tile of the normalized histogram 
        sum = 0;
        for (int i = 0; i < data.length; i++) {
            sum += histo[i];
            if (sum > p0) {
                threshold = i;
                break;
            }
        }
        double G[] = new double[2];
        G[0] = z0;
        G[1] = z1;
        return G;
    }

    
    private static void display_images() {
        //JFrame frame_oi = new JFrame("Original Color image");
        //Panel panel_oi = new ShowImage(oi.getDirectory()+"Original_Color.png");
        //frame_oi.getContentPane().add(panel_oi);
        //frame_oi.setSize(500, 500);
        //frame_oi.setVisible(true);


        JFrame frame_gi = new JFrame("Original Color Image");
        Panel panel_gi = new ShowImage(oi.getDirectory() + "Original_Color.png");
        frame_gi.getContentPane().add(panel_gi);
        frame_gi.setSize(500, 500);
        frame_gi.setVisible(true);


         
                  

        JFrame frame_bs = new JFrame("Stego Binarized red image with alpha channel");
        Panel panel_bs = new ShowImage(oi.getDirectory() + "Stego_Binary_r.png");
        frame_bs.getContentPane().add(panel_bs);
        frame_bs.setSize(500, 500);
        frame_bs.setVisible(true);
        
        JFrame frame_bsg = new JFrame("Stego Binarized green image with alpha channel");
        Panel panel_bsg = new ShowImage(oi.getDirectory() + "Stego_Binary_g.png");
        frame_bsg.getContentPane().add(panel_bsg);
        frame_bsg.setSize(500, 500);
        frame_bsg.setVisible(true);
        
        JFrame frame_bsb = new JFrame("Stego Binarized blue image with alpha channel");
        Panel panel_bsb = new ShowImage(oi.getDirectory() + "Stego_Binary_b.png");
        frame_bsb.getContentPane().add(panel_bsb);
        frame_bsb.setSize(500, 500);
        frame_bsb.setVisible(true);
        

        JFrame frame_bs1 = new JFrame("Authentication signal");
        Panel panel_bs1 = new ShowImage(oi.getDirectory() + "Authentication_data_color.png");
        frame_bs1.getContentPane().add(panel_bs1);
        frame_bs1.setSize(500, 500);
        frame_bs1.setVisible(true);
        
         

    }

    

       public static void main(String args[]) throws IOException {
           //1.1 read image
        ImagePlus original_image = null;
        BufferedImage image_oi = null;
        File f_oi;
        oi = new ij.io.OpenDialog("Select image", "E:\\DATA\\YES_MAN\\JAVA_IMPL\\", "lenna.bmp");
        try {
            f_oi = new File(oi.getDirectory() + oi.getFileName());
            image_oi = ImageIO.read(f_oi);
        } catch (IOException e) {
        }
        try {
            ImageIO.write(image_oi, "png", new File(oi.getDirectory() + "Original_Color.png"));
        } catch (IOException e) {
        }



        BufferedImage gray_i_r = new BufferedImage(image_oi.getWidth(), image_oi.getHeight(), BufferedImage.TYPE_INT_ARGB);

        BufferedImage gray_with_alpha_r = new BufferedImage(image_oi.getWidth(), image_oi.getHeight(), BufferedImage.TYPE_INT_ARGB);

         
        int alpha = 0;
        int red = 0;
        int green = 0;
        int blue = 0;
        int rgb = 0;
        int k = 0;
        for (int i = 0; i < image_oi.getWidth(); i++) {
            for (int j = 0; j < image_oi.getHeight(); j++) {
                rgb = image_oi.getRGB(i, j);
                alpha = 255;
                red = (rgb & 0x00FF0000) >>> 16;
                green = 0;
                blue =  0;
                rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                 
                gray_with_alpha_r.setRGB(i, j, rgb);
                gray_i_r.setRGB(i, j, rgb);
                
             }
        }

        //1.3 save graysacle image
        try {
            ImageIO.write(gray_with_alpha_r, "png", new File(oi.getDirectory() + "gray_with_alpha_r.png"));
           } catch (IOException e) {
        }

        //1.4 convert to binary     
        int data_r[] = new int[image_oi.getWidth() * image_oi.getHeight()];
        int Gray_Image_original_r[][] = new int[image_oi.getWidth()][image_oi.getHeight()];
        int Alpha_Image_original_r[][] = new int[image_oi.getWidth()][image_oi.getHeight()];
        int Binary_Image_original_r[][] = new int[image_oi.getWidth()][image_oi.getHeight()];
         
         
         
        k = 0;
        for (int cx = 0; cx < image_oi.getWidth(); cx++) {
            for (int cy = 0; cy < image_oi.getHeight(); cy++) {
                rgb = gray_i_r.getRGB(cx, cy);
                red = (rgb & 0x00FF0000) >>> 16;
         
                data_r[k] = (red ) ;
                Gray_Image_original_r[cx][cy] = (red );
                Alpha_Image_original_r[cx][cy] = 255;
                
                
                k++;
            }
        }
        //1.4.1 compute g1 and g2
        int histogram_r[] = new int[256];//histogram og gray image
         
        for (int i = 0; i < data_r.length; i++) {
            histogram_r[data_r[i]] = histogram_r[data_r[i]] + 1;
        }

        G_r = Moments(histogram_r);
        // System.out.println("Stage I—generation of authentication signals");
       // System.out.println("\nStep 1.Input image binarization");
        //System.out.println("\nMoment Preserving thresholding");
        //System.out.println("Value of gray level g1="+G[0]);
        //System.out.println("Value of gray level g2="+G[1]);
        //1.4.2 compute threshold for binarization  
        thres_r = (G_r[0] + G_r[1]) / 2.0;
          
        BufferedImage Binary_i_r = new BufferedImage(gray_i_r.getWidth(), gray_i_r.getHeight(), BufferedImage.TYPE_INT_ARGB);
         
        //1.4.3 binarize gray image
        k = 0;
        for (int cx = 0; cx < image_oi.getWidth(); cx++) {
            for (int cy = 0; cy < image_oi.getHeight(); cy++) {
                if (data_r[k] < thres_r) {
                    Binary_Image_original_r[cx][cy] = 0;
                } 
                else {
                    Binary_Image_original_r[cx][cy] = 1;
                }
          
                k++;
                alpha = 255;
                red   = Binary_Image_original_r[cx][cy] * 255;
                green = 0;
                blue  = 0;
                rgb   = (alpha << 24) | (red << 16) | (green << 8) | blue;
                Binary_i_r.setRGB(cx, cy, rgb);
                
                 
            }
        }

         
        try {
            ImageIO.write(Binary_i_r, "png", new File(oi.getDirectory() + "Binary_i_r.png"));
             }   catch (IOException e) {
        }

        //get Binary and alpha into 2D array
        int LIM_RR, LIM_C;
        LIM_C = image_oi.getWidth() - (image_oi.getWidth() % 3);//columns
        LIM_RR = image_oi.getHeight() - (image_oi.getHeight() % 2);//rows
        int BI_r[][] = new int[LIM_C][LIM_RR];
        int AI_r[][] = new int[LIM_C][LIM_RR];
        int AI_mod_r[][] = new int[LIM_C][LIM_RR];

         
        //System.out.println("\n\nContents of Binarized image");
        for (int cx = 0; cx < LIM_C; cx++) {
            for (int cy = 0; cy < LIM_RR; cy++) {
                BI_r[cx][cy] = Binary_Image_original_r[cx][cy];
                 //System.out.println("BI="+BI[cx][cy]+"\tcol="+cx+"\trow="+cy);         
            }
        }

//////////////////////////////////////////////////////////////////////////////////////////////////
        
        BufferedImage gray_i_g = new BufferedImage(image_oi.getWidth(), image_oi.getHeight(), BufferedImage.TYPE_INT_ARGB);

        BufferedImage gray_with_alpha_g = new BufferedImage(image_oi.getWidth(), image_oi.getHeight(), BufferedImage.TYPE_INT_ARGB);

         
         alpha = 0;
         red = 0;
         green = 0;
         blue = 0;
         rgb = 0;
         k = 0;
        for (int i = 0; i < image_oi.getWidth(); i++) {
            for (int j = 0; j < image_oi.getHeight(); j++) {
                rgb = image_oi.getRGB(i, j);
                alpha = 255;
                red = 0;
                green =(rgb & 0x0000FF00) >>> 8;
                blue =  0;
                rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                 
                gray_with_alpha_g.setRGB(i, j, rgb);
                gray_i_g.setRGB(i, j, rgb);
                
             }
        }

        //1.3 save graysacle image
        try {
            ImageIO.write(gray_with_alpha_g, "png", new File(oi.getDirectory() + "gray_with_alpha_g.png"));
           } catch (IOException e) {
        }

        //1.4 convert to binary     
        int data_g[] = new int[image_oi.getWidth() * image_oi.getHeight()];
        int Gray_Image_original_g[][] = new int[image_oi.getWidth()][image_oi.getHeight()];
        int Alpha_Image_original_g[][] = new int[image_oi.getWidth()][image_oi.getHeight()];
        int Binary_Image_original_g[][] = new int[image_oi.getWidth()][image_oi.getHeight()];
         
         
         
        k = 0;
        for (int cx = 0; cx < image_oi.getWidth(); cx++) {
            for (int cy = 0; cy < image_oi.getHeight(); cy++) {
                rgb = gray_i_g.getRGB(cx, cy);
                green = (rgb & 0x0000FF00) >>> 8;
         
                data_g[k] = (green ) ;
                Gray_Image_original_g[cx][cy] = (green );
                Alpha_Image_original_g[cx][cy] = 255;
                
                
                k++;
            }
        }
        //1.4.1 compute g1 and g2
        int histogram_g[] = new int[256];//histogram og gray image
         
        for (int i = 0; i < data_g.length; i++) {
            histogram_g[data_g[i]] = histogram_g[data_g[i]] + 1;
        }

        G_g = Moments(histogram_g);
         //System.out.println("Stage I—generation of authentication signals");
        //System.out.println("\nStep 1.Input image binarization");
        //System.out.println("\nMoment Preserving thresholding");
        //System.out.println("Value of gray level g1="+G[0]);
        //System.out.println("Value of gray level g2="+G[1]);
        //1.4.2 compute threshold for binarization  
        thres_g = (G_g[0] + G_g[1]) / 2.0;
          
        BufferedImage Binary_i_g = new BufferedImage(gray_i_g.getWidth(), gray_i_g.getHeight(), BufferedImage.TYPE_INT_ARGB);
         
        //1.4.3 binarize gray image
        k = 0;
        for (int cx = 0; cx < image_oi.getWidth(); cx++) {
            for (int cy = 0; cy < image_oi.getHeight(); cy++) {
                if (data_g[k] < thres_g) {
                    Binary_Image_original_g[cx][cy] = 0;
                } 
                else {
                    Binary_Image_original_g[cx][cy] = 1;
                }
          
                k++;
                alpha = 255;
                red   = 0;
                green = Binary_Image_original_g[cx][cy] * 255;
                blue  = 0;
                rgb   = (alpha << 24) | (red << 16) | (green << 8) | blue;
                Binary_i_g.setRGB(cx, cy, rgb);
                
                 
            }
        }

         
        try {
            ImageIO.write(Binary_i_g, "png", new File(oi.getDirectory() + "Binary_i_g.png"));
             }   catch (IOException e) {
        }

        //get Binary and alpha into 2D array
         
        LIM_C = image_oi.getWidth() - (image_oi.getWidth() % 3);//columns
        LIM_RR = image_oi.getHeight() - (image_oi.getHeight() % 2);//rows
        int BI_g[][] = new int[LIM_C][LIM_RR];
        int AI_g[][] = new int[LIM_C][LIM_RR];
        int AI_mod_g[][] = new int[LIM_C][LIM_RR];

         
        //System.out.println("\n\nContents of Binarized image");
        for (int cx = 0; cx < LIM_C; cx++) {
            for (int cy = 0; cy < LIM_RR; cy++) {
                BI_g[cx][cy] = Binary_Image_original_g[cx][cy];
                 //System.out.println("BI="+BI[cx][cy]+"\tcol="+cx+"\trow="+cy);         
            }
        }




        
        
        
        
//////////////////////////////////////////////////////////////////////////////////////////////////        

//**************************************************************************************************



        BufferedImage gray_i_b = new BufferedImage(image_oi.getWidth(), image_oi.getHeight(), BufferedImage.TYPE_INT_ARGB);

        BufferedImage gray_with_alpha_b = new BufferedImage(image_oi.getWidth(), image_oi.getHeight(), BufferedImage.TYPE_INT_ARGB);

         
         alpha = 0;
         red = 0;
         green = 0;
         blue = 0;
         rgb = 0;
         k = 0;
        for (int i = 0; i < image_oi.getWidth(); i++) {
            for (int j = 0; j < image_oi.getHeight(); j++) {
                rgb = image_oi.getRGB(i, j);
                alpha = 255;
                red = 0;
                green =0;
                blue =  (rgb & 0x000000FF) >>> 0;
                rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                 
                gray_with_alpha_b.setRGB(i, j, rgb);
                gray_i_b.setRGB(i, j, rgb);
                
             }
        }

        //1.3 save graysacle image
        try {
            ImageIO.write(gray_with_alpha_b, "png", new File(oi.getDirectory() + "gray_with_alpha_b.png"));
           } catch (IOException e) {
        }

        //1.4 convert to binary     
        int data_b[] = new int[image_oi.getWidth() * image_oi.getHeight()];
        int Gray_Image_original_b[][] = new int[image_oi.getWidth()][image_oi.getHeight()];
        int Alpha_Image_original_b[][] = new int[image_oi.getWidth()][image_oi.getHeight()];
        int Binary_Image_original_b[][] = new int[image_oi.getWidth()][image_oi.getHeight()];
         
         
         
        k = 0;
        for (int cx = 0; cx < image_oi.getWidth(); cx++) {
            for (int cy = 0; cy < image_oi.getHeight(); cy++) {
                rgb = gray_i_b.getRGB(cx, cy);
                blue = (rgb & 0x000000FF) >>> 0;
         
                data_b[k] = (blue ) ;
                Gray_Image_original_b[cx][cy] = (blue );
                Alpha_Image_original_b[cx][cy] = 255;
                
                
                k++;
            }
        }
        //1.4.1 compute g1 and g2
        int histogram_b[] = new int[256];//histogram og gray image
         
        for (int i = 0; i < data_b.length; i++) {
            histogram_b[data_b[i]] = histogram_b[data_b[i]] + 1;
        }

        G_b = Moments(histogram_b);
         System.out.println("Stage I—generation of authentication signals");
        System.out.println("\nStep 1.Input image binarization");
        System.out.println("\nMoment Preserving thresholding");
        System.out.println("Value of gray level(red)\tg1 ="+G_r[0]+"\tg2="+G_r[1]);
        System.out.println("Value of gray level(green)\tg1 ="+G_g[0]+"\tg2="+G_g[1]);
        System.out.println("Value of gray level(blue)\tg1 ="+G_b[0]+"\tg2="+G_b[1]);
        System.out.println("\nBinarizing red green blue planes separately.");
          
        //1.4.2 compute threshold for binarization  
        thres_b = (G_b[0] + G_b[1]) / 2.0;
          
        BufferedImage Binary_i_b = new BufferedImage(gray_i_b.getWidth(), gray_i_b.getHeight(), BufferedImage.TYPE_INT_ARGB);
         
        //1.4.3 binarize gray image
        k = 0;
        for (int cx = 0; cx < image_oi.getWidth(); cx++) {
            for (int cy = 0; cy < image_oi.getHeight(); cy++) {
                if (data_b[k] < thres_b) {
                    Binary_Image_original_b[cx][cy] = 0;
                } 
                else {
                    Binary_Image_original_b[cx][cy] = 1;
                }
          
                k++;
                alpha = 255;
                red   = 0;
                green = 0;
                blue  = Binary_Image_original_b[cx][cy] * 255;
                rgb   = (alpha << 24) | (red << 16) | (green << 8) | blue;
                Binary_i_b.setRGB(cx, cy, rgb); 
                 
            }
        }

         
        try {
            ImageIO.write(Binary_i_b, "png", new File(oi.getDirectory() + "Binary_i_b.png"));
             }   catch (IOException e) {
        }

        //get Binary and alpha into 2D array
         LIM_C = image_oi.getWidth() - (image_oi.getWidth() % 3);//columns
        LIM_RR = image_oi.getHeight() - (image_oi.getHeight() % 2);//rows
        int BI_b[][] = new int[LIM_C][LIM_RR];
        int AI_b[][] = new int[LIM_C][LIM_RR];
        int AI_mod_b[][] = new int[LIM_C][LIM_RR];

         
        //System.out.println("\n\nContents of Binarized image");
        for (int cx = 0; cx < LIM_C; cx++) {
            for (int cy = 0; cy < LIM_RR; cy++) {
                BI_b[cx][cy] = Binary_Image_original_b[cx][cy];
                 //System.out.println("BI="+BI[cx][cy]+"\tcol="+cx+"\trow="+cy);         
            }
        }

        
        
        
//***************************************************************************************************        
        //find key K and inverse
        int K[] = new int[LIM_RR];
        int K_i[] = new int[LIM_RR];
        RandomPermutation rp = new RandomPermutation();
        K = rp.GenerateRandomPermutation(LIM_RR);
       System.out.println("\nGenerating Key K for partial share mapping:\nKey Length="+K.length);
        for (k = 0; k < K.length; k++)
           System.out.print("  " + K[k])
           ;

        int index = -1;
        for (int ii = 0; ii < LIM_RR; ii++) {
            k = 0;
            index = -1;
            while (true) {
                if (K[k] == ii) {
                    index = k;
                    break;
                } 
                else {
                    k++;
                }
            }
            K_i[ii] = index;
        }

       System.out.println("\nInverse Key of K has length="+K.length+"\nInverse Key of K:")
               ;
        for (int ii = 0; ii < LIM_RR; ii++) {
            System.out.print(K_i[ii]+" ")
            ;
        }      
        
        
        
//*********************************************************************************************************************        
        //2. for every block of binary and alpha 

        boolean p_r[] = new boolean[6];
        int SDATA_mod_r[][] = new int[LIM_C][LIM_RR];
        int SDATA_r[][] = new int[LIM_C][LIM_RR];
        
         ////System.out.println("\nbefore:");
        int blk_no = 0;
        for (int j = 0; j < LIM_C && j % 3 == 0; j = j + 3) {
            for (int i = 0; i < LIM_RR && i % 2 == 0; i = i + 2) {
                System.out.println("\n\n\tProcessing Red Gray Block no="+blk_no++);
                if (BI_r[j][i] == 1) {
                    p_r[0] = true;
                } else {
                    p_r[0] = false;
                }
                if (BI_r[j][i + 1] == 1) {
                    p_r[1] = true;
                } else {
                    p_r[1] = false;
                }
                if (BI_r[j + 1][i] == 1) {
                    p_r[2] = true;
                } else {
                    p_r[2] = false;
                }
                if (BI_r[j + 1][i + 1] == 1) {
                    p_r[3] = true;
                } else {
                    p_r[3] = false;
                }
                if (BI_r[j + 2][i] == 1) {
                    p_r[4] = true;
                } else {
                    p_r[4] = false;
                }
                if (BI_r[j + 2][i + 1] == 1) {
                    p_r[5] = true;
                } else {
                    p_r[5] = false;
                }

                boolean a1_r, a2_r;
                a1_r = (p_r[0] ^ p_r[1]) ^ p_r[2];
                a2_r = (p_r[3] ^ p_r[4]) ^ p_r[5];
                System.out.println("Six binary pixels of current Binary block of corresponding Red gray block:");
                System.out.println("p[0]="+p_r[0]+"\tp[1]="+p_r[1]+"\tp[2]="+p_r[2]+"\tp[3]="+p_r[3]+"\tp[4]="+p_r[4]+"\tp[5]="+p_r[5]);
                System.out.println("Two Authentication signals computed from six binary pixels of current Binary block (to be embedded in alpha plane of stego image and authentication image):");
                System.out.println("s[0]=a1=p[0] XOR p[1] XOR p[2]="+a1_r+"\ts[1]=a2=p[3] XOR p[4] XOR [5]="+a2_r);
             
                //System.out.println("p[0]="+p[0]+"\tp[1]="+p[1]+"\tp[2]="+p[2]+"\tp[3]="+p[3]+"\tp[4]="+p[4]+"\tp[5]="+p[5]);
                ////System.out.println("s[0]="+a1+"\ts[1]="+a2);
                //System.out.println("a1="+a1+"\ta2="+a2);

                int m1_r[] = new int[4];
                if (a1_r == true) {
                    m1_r[0] = 1;
                } else {
                    m1_r[0] = 0;
                }
                if (a2_r == true) {
                    m1_r[1] = 1;
                } else {
                    m1_r[1] = 0;
                }
                if (p_r[0] == true) {
                    m1_r[2] = 1;
                } else {
                    m1_r[2] = 0;
                }
                if (p_r[1] == true) {
                    m1_r[3] = 1;
                } else {
                    m1_r[3] = 0;
                }

                int m2_r[] = new int[4];
                if (p_r[2] == true) {
                    m2_r[0] = 1;
                } else {
                    m2_r[0] = 0;
                }
                if (p_r[3] == true) {
                    m2_r[1] = 1;
                } else {
                    m2_r[1] = 0;
                }
                if (p_r[4] == true) {
                    m2_r[2] = 1;
                } else {
                    m2_r[2] = 0;
                }
                if (p_r[5] == true) {
                    m2_r[3] = 1;
                } else {
                    m2_r[3] = 0;
                }

                int d_r = 0, c1_r = 0;
                d_r = (m1_r[0] * 2 * 2 * 2 + m1_r[1] * 2 * 2 + m1_r[2] * 2 + m1_r[3] * 1);
                c1_r = (m2_r[0] * 2 * 2 * 2 + m2_r[1] * 2 * 2 + m2_r[2] * 2 + m2_r[3] * 1);
                int x_r[] = new int[6];
                int q_r[] = new int[6];
                int q1_r[] = new int[6];
                int F_r[] = new int[6];
               System.out.println(("d=m1=(a1 * 2 * 2 * 2 + a2 * 2 * 2 + p[0] * 2 + p[1] * 1)="+d_r+"\tc1=m2=(p[2] * 2 * 2 * 2 + p[3] * 2 * 2 + p[4] * 2 + p[5] * 1)="+c1_r));
               System.out.println("six partial shares from current Binary block computed using Shamir secrete share generation by using eqn:q[i] = ((d + c1 * (i+1)) % 17),i=0,1,2,..5;\n(to be embedded in alpha plane and authentication image):");
              
                for (int mmm = 0; mmm < 6; mmm++) {
                    x_r[mmm] = mmm + 1;
                    F_r[mmm] = ((d_r + c1_r * x_r[mmm]) % 17);
                    q_r[mmm] = ((d_r + c1_r * x_r[mmm]) % 17);
                    q1_r[mmm] = (q_r[mmm] + 238);
                    System.out.println("q["+mmm+"]="+q_r[mmm] );
                }
                 System.out.println("six partial shares from current binary block  by using eqn: q1[i]=q[i]+238,i=0,1,2,..5;\n(first two shares will be retained,remaining four shares will be mapped to other blocks using key K ):");
                for (int mmm = 0; mmm < 6; mmm++) {
                     
                    System.out.println("q1["+mmm+"]="+q1_r[mmm] );
                }
                ////System.out.println("Step 7. (Mapping of the partial shares)");
                //System.out.println(("d="+d+"\tc1="+c1));

                ////System.out.println("Step 8. (Embedding two partial shares in the current block)");
                AI_r[j][i] = q1_r[0];
                AI_r[j][i + 1] = q1_r[1];
                AI_r[j + 1][i] = q1_r[2];
                AI_r[j + 1][i + 1] = q1_r[3];
                AI_r[j + 2][i] = q1_r[4];
                AI_r[j + 2][i + 1] = q1_r[5];

                AI_mod_r[j][i] = q1_r[0];
                AI_mod_r[j][i + 1] = q1_r[1];
                AI_mod_r[j + 1][i] = q1_r[2];
                AI_mod_r[j + 1][i + 1] = q1_r[3];
                AI_mod_r[j + 2][i] = q1_r[4];
                AI_mod_r[j + 2][i + 1] = q1_r[5];

                ////System.out.println();
                //for(int iii=0;iii<6;iii++)
                ////System.out.print(" "+q1[iii]+" ");    


                SDATA_mod_r[j][i] = q_r[0];
                SDATA_mod_r[j][i + 1] = q_r[1];
                SDATA_mod_r[j + 1][i] = q_r[2];
                SDATA_mod_r[j + 1][i + 1] = q_r[3];
                SDATA_mod_r[j + 2][i] = q_r[4];
                SDATA_mod_r[j + 2][i + 1] = q_r[5];

                SDATA_r[j][i] = q_r[0];
                SDATA_r[j][i + 1] = q_r[1];
                SDATA_r[j + 1][i] = q_r[2];
                SDATA_r[j + 1][i + 1] = q_r[3];
                SDATA_r[j + 2][i] = q_r[4];
                SDATA_r[j + 2][i + 1] = q_r[5];
            }
        }

        //display randomize result
         System.out.println("\n\nStep 7. (Mapping of the partial shares in alpha plane)");
         System.out.println("Step 8. (Embedding two partial shares in the current block of alpha plane)");
        System.out.println("\n\nBefore mapping remaining four partial shares of each block of alpha plane to other blocks using key K:");        
         for (int j = 0; j < LIM_C && j % 3 == 0; j = j + 3) {               
             System.out.println();

            for (int i = 0; i < LIM_RR && i % 2 == 0; i = i + 2) {
                System.out.println("("+j+","+i+")"+AI_mod_r[j][i]+" ("+(j+1)+","+i+")"+AI_mod_r[j+1][i]+" ("+(j+2)+","+(i)+") "+AI_mod_r[j+2][i]);  
                System.out.println("("+(j)+","+(i+1)+")"+AI_mod_r[j][(i+1)]+" ("+(j+1)+","+(i+1)+")"+AI_mod_r[(j+1)][i+1]+" ("+(j+2)+","+(i+1)+") "+AI_mod_r[j+2][(i+1)]);                   
            }
        }
        //randomize data
        // //System.out.println("doing ...");
  System.out.println("Mapping remaining four partial shares of each block of alpha plane to other blocks using key K ...");

        for (int j = 0; j < LIM_C; j++) {
            //System.out.println("Randomizing alpha palne ...");
            if (j % 3 != 0) {//get current column 
                //rearrane it as per k

                for (int i = 0; i < LIM_RR; i++) {
                    //System.out.println("*****************************");
                    //System.out.println("\n("+j+","+i+")"+AI_mod[j][i]+"\tK["+i+"]="+K[i]);
                    AI_mod_r[j][i] = AI_r[j][K[i]];
                    //System.out.println("("+j+","+i+")"+AI_mod[j][i]+"\tK["+i+"]="+K[i]);
                    //System.out.println("*****************************");
                    SDATA_mod_r[j][i] = SDATA_r[j][K[i]];
                }
            }
        }

        System.out.println("\n\nAfter Mapping remaining four partial shares of each block of alpha plane to other blocks using key K ...:");        
             for (int j = 0; j < LIM_C && j % 3 == 0; j = j + 3) {
                 System.out.println();
            for (int i = 0; i < LIM_RR && i % 2 == 0; i = i + 2) {
                //
                System.out.println("("+j+","+i+")"+AI_mod_r[j][i]+" ("+(j+1)+","+i+")"+AI_mod_r[j+1][i]+" ("+(j+2)+","+(i)+") "+AI_mod_r[j+2][i]);  
                System.out.println("("+(j)+","+(i+1)+")"+AI_mod_r[j][(i+1)]+" ("+(j+1)+","+(i+1)+")"+AI_mod_r[(j+1)][i+1]+" ("+(j+2)+","+(i+1)+") "+AI_mod_r[j+2][(i+1)]);                   
            }
        }


        // BufferedImage stego_color=new BufferedImage(gray_i.getWidth(), gray_i.getHeight(),  BufferedImage.TYPE_INT_ARGB);
        BufferedImage stego_gray_r = new BufferedImage(gray_i_r.getWidth(), gray_i_r.getHeight(), BufferedImage.TYPE_INT_ARGB);
        BufferedImage stego_binary_r = new BufferedImage(gray_i_r.getWidth(), gray_i_r.getHeight(), BufferedImage.TYPE_INT_ARGB);
        //stego_binary=Binary_i;
        BufferedImage Authentication_data_r = new BufferedImage(gray_i_r.getWidth(), gray_i_r.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int cy = 0; cy < LIM_C; cy++) {
            for (int cx = 0; cx < LIM_RR; cx++) {
                  
                rgb = gray_with_alpha_r.getRGB(cy, cx);
                alpha = AI_mod_r[cy][cx];
                
                red = (rgb & 0x00FF0000) >>> 16;
                green = 0;
                blue = 0;
                rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                stego_gray_r.setRGB(cy, cx, rgb);
                   
               
                
                
                rgb = Binary_i_r.getRGB(cy, cx);
                alpha = AI_mod_r[cy][cx];
                red = (rgb & 0x00FF0000) >>> 16;
                green = 0;
                blue =   0;
                rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                stego_binary_r.setRGB(cy, cx, rgb);


                alpha = 255;
                red = SDATA_r[cy][cx]+238;
                green = 0;
                blue = 0;
                rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;

                Authentication_data_r.setRGB(cy, cx, rgb);
               }
        }

        
         try {
            ImageIO.write(Authentication_data_r, "png", new File(oi.getDirectory() + "Authentication_data_r.png"));
         } catch (IOException e) {}

        try {
            ImageIO.write(stego_gray_r, "png", new File(oi.getDirectory() + "Stego_Gray_r.png"));
        } catch (IOException e) {
        }
        try {
            ImageIO.write(stego_binary_r, "png", new File(oi.getDirectory() + "Stego_Binary_r.png"));
        } catch (IOException e) {
        }

        try {
            ImageIO.write(Authentication_data_r, "png", new File(oi.getDirectory() + "Authentication_data_r.png"));
        } catch (IOException e) {
        }


        //save values key and g1 g2
        FileWriter fw2_r = new FileWriter(oi.getDirectory() + "KEY_r.txt");
        BufferedWriter bw2_r = new BufferedWriter(fw2_r);

        String content = String.valueOf((G_r[0]));
        if(content.indexOf(".")==-1)
          {content=content+".00000";}
        else{
            if(content.substring(content.indexOf(".")+1).length()<5)
            {for(int i=content.substring(content.indexOf(".")+1).length();i<5;i++){
                content=content+"0";
                System.out.println(content);
            }
            }              
        }               
        bw2_r.write(content + "\n");
        

        content = String.valueOf((G_r[1]));
        
         if(content.indexOf(".")==-1)
          {content=content+".00000";}
        else{
            if(content.substring(content.indexOf(".")+1).length()<5)
            {for(int i=content.substring(content.indexOf(".")+1).length();i<5;i++){
                content=content+"0";
                System.out.println(content);
            }
            }              
        }
        
        bw2_r.write(content + "\n");

        for (int i = 0; i < K.length; i++) {
            content = String.valueOf(K[i]);
            bw2_r.write(content + "\n");
        }

        bw2_r.close();      


//*********************************************************************************************************************
        
        //2. for every block of binary and alpha 

        boolean p_g[] = new boolean[6];
        int SDATA_mod_g[][] = new int[LIM_C][LIM_RR];
        int SDATA_g[][] = new int[LIM_C][LIM_RR];
        
         ////System.out.println("\nbefore:");
        blk_no = 0;
        for (int j = 0; j < LIM_C && j % 3 == 0; j = j + 3) {
            for (int i = 0; i < LIM_RR && i % 2 == 0; i = i + 2) {
                 System.out.println("\n\n\tProcessing Green gray Block no="+blk_no++);
                if (BI_g[j][i] == 1) {
                    p_g[0] = true;
                } else {
                    p_g[0] = false;
                }
                if (BI_g[j][i + 1] == 1) {
                    p_g[1] = true;
                } else {
                    p_g[1] = false;
                }
                if (BI_g[j + 1][i] == 1) {
                    p_g[2] = true;
                } else {
                    p_g[2] = false;
                }
                if (BI_g[j + 1][i + 1] == 1) {
                    p_g[3] = true;
                } else {
                    p_g[3] = false;
                }
                if (BI_g[j + 2][i] == 1) {
                    p_g[4] = true;
                } else {
                    p_g[4] = false;
                }
                if (BI_g[j + 2][i + 1] == 1) {
                    p_g[5] = true;
                } else {
                    p_g[5] = false;
                }

                boolean a1_g, a2_g;
                a1_g = (p_g[0] ^ p_g[1]) ^ p_g[2];
                a2_g = (p_g[3] ^ p_g[4]) ^ p_g[5];
                //System.out.println("p[0]="+p[0]+"\tp[1]="+p[1]+"\tp[2]="+p[2]+"\tp[3]="+p[3]+"\tp[4]="+p[4]+"\tp[5]="+p[5]);
                ////System.out.println("s[0]="+a1+"\ts[1]="+a2);
                //System.out.println("a1="+a1+"\ta2="+a2);
                System.out.println("Six binary pixels of current Binary block of corresponding Green gray block:");
                System.out.println("p[0]="+p_g[0]+"\tp[1]="+p_g[1]+"\tp[2]="+p_g[2]+"\tp[3]="+p_g[3]+"\tp[4]="+p_g[4]+"\tp[5]="+p_g[5]);
                System.out.println("Two Authentication signals computed from six binary pixels of current Binary block (to be embedded in authentication image):");
                System.out.println("s[0]=a1=p[0] XOR p[1] XOR p[2]="+a1_g+"\ts[1]=a2=p[3] XOR p[4] XOR [5]="+a2_g);
 

                int m1_g[] = new int[4];
                if (a1_g == true) {
                    m1_g[0] = 1;
                } else {
                    m1_g[0] = 0;
                }
                if (a2_g == true) {
                    m1_g[1] = 1;
                } else {
                    m1_g[1] = 0;
                }
                if (p_g[0] == true) {
                    m1_g[2] = 1;
                } else {
                    m1_g[2] = 0;
                }
                if (p_g[1] == true) {
                    m1_g[3] = 1;
                } else {
                    m1_g[3] = 0;
                }

                int m2_g[] = new int[4];
                if (p_g[2] == true) {
                    m2_g[0] = 1;
                } else {
                    m2_g[0] = 0;
                }
                if (p_g[3] == true) {
                    m2_g[1] = 1;
                } else {
                    m2_g[1] = 0;
                }
                if (p_g[4] == true) {
                    m2_g[2] = 1;
                } else {
                    m2_g[2] = 0;
                }
                if (p_g[5] == true) {
                    m2_g[3] = 1;
                } else {
                    m2_g[3] = 0;
                }

                int d_g = 0, c1_g = 0;
                d_g = (m1_g[0] * 2 * 2 * 2 + m1_g[1] * 2 * 2 + m1_g[2] * 2 + m1_g[3] * 1);
                c1_g = (m2_g[0] * 2 * 2 * 2 + m2_g[1] * 2 * 2 + m2_g[2] * 2 + m2_g[3] * 1);
                int x_g[] = new int[6];
                int q_g[] = new int[6];
                int q1_g[] = new int[6];
                int F_g[] = new int[6];
                System.out.println(("d=m1=(a1 * 2 * 2 * 2 + a2 * 2 * 2 + p[0] * 2 + p[1] * 1)="+d_g+"\tc1=m2=(p[2] * 2 * 2 * 2 + p[3] * 2 * 2 + p[4] * 2 + p[5] * 1)="+c1_g));
               System.out.println("six partial shares from current Binary block computed using Shamir secrete share generation by using eqn:q[i] = ((d + c1 * (i+1)) % 17),i=0,1,2,..5;\n(to be embedded in authentication image):");

                               for (int mmm = 0; mmm < 6; mmm++) {
                    x_g[mmm] = mmm + 1;
                    F_g[mmm] = ((d_g + c1_g * x_g[mmm]) % 17);
                    q_g[mmm] = ((d_g + c1_g * x_g[mmm]) % 17);
                    q1_g[mmm] = (q_g[mmm] + 238);
                    System.out.println("q["+mmm+"]="+q_g[mmm] );
                }
                 System.out.println("six partial shares from current Binary block  by using eqn: q1[i]=q[i]+238,i=0,1,2,..5;\n(first two shares will be retained,remaining four shares will be mapped to other blocks using key K ):");
                for (int mmm = 0; mmm < 6; mmm++) {
                     
                    System.out.println("q1["+mmm+"]="+q1_g[mmm] );
                }              

                ////System.out.println("Step 7. (Mapping of the partial shares)");
                //System.out.println(("d="+d+"\tc1="+c1));

                ////System.out.println("Step 8. (Embedding two partial shares in the current block)");
                
                AI_g[j][i] = q1_g[0];
                AI_g[j][i + 1] = q1_g[1];
                AI_g[j + 1][i] = q1_g[2];
                AI_g[j + 1][i + 1] = q1_g[3];
                AI_g[j + 2][i] = q1_g[4];
                AI_g[j + 2][i + 1] = q1_g[5];

                AI_mod_g[j][i] = q1_g[0];
                AI_mod_g[j][i + 1] = q1_g[1];
                AI_mod_g[j + 1][i] = q1_g[2];
                AI_mod_g[j + 1][i + 1] = q1_g[3];
                AI_mod_g[j + 2][i] = q1_g[4];
                AI_mod_g[j + 2][i + 1] = q1_g[5];

                ////System.out.println();
                //for(int iii=0;iii<6;iii++)
                ////System.out.print(" "+q1[iii]+" ");    


                SDATA_mod_g[j][i] = q_g[0];
                SDATA_mod_g[j][i + 1] = q_g[1];
                SDATA_mod_g[j + 1][i] = q_g[2];
                SDATA_mod_g[j + 1][i + 1] = q_g[3];
                SDATA_mod_g[j + 2][i] = q_g[4];
                SDATA_mod_g[j + 2][i + 1] = q_g[5];

                SDATA_g[j][i] = q_g[0];
                SDATA_g[j][i + 1] = q_g[1];
                SDATA_g[j + 1][i] = q_g[2];
                SDATA_g[j + 1][i + 1] = q_g[3];
                SDATA_g[j + 2][i] = q_g[4];
                SDATA_g[j + 2][i + 1] = q_g[5];
            }
        }

       //display randomize result
        System.out.println("\n\nStep 7. (Mapping of the partial shares )");
        System.out.println("Step 8. (Embedding two partial shares in the current block of plane)");
        System.out.println("\n\nBefore mapping remaining four partial shares of each block to other blocks using key K:");        

        //System.out.println("\n\nBefore randomizaton:");        
        for (int j = 0; j < LIM_C && j % 3 == 0; j = j + 3) {
            System.out.println();
            for (int i = 0; i < LIM_RR && i % 2 == 0; i = i + 2) {
                System.out.println("("+j+","+i+")"+AI_mod_g[j][i]+" ("+(j+1)+","+i+")"+AI_mod_g[j+1][i]+" ("+(j+2)+","+(i)+") "+AI_mod_g[j+2][i]);  
                System.out.println("("+(j)+","+(i+1)+")"+AI_mod_g[j][(i+1)]+" ("+(j+1)+","+(i+1)+")"+AI_mod_g[(j+1)][i+1]+" ("+(j+2)+","+(i+1)+") "+AI_mod_g[j+2][(i+1)]);                   

              }
        }
        //randomize data
     System.out.println("Mapping remaining four partial shares of each block to other blocks using key K ...");

        for (int j = 0; j < LIM_C; j++) {
            //System.out.println("Randomizing alpha palne ...");
            if (j % 3 != 0) {//get current column 
                //rearrane it as per k

                for (int i = 0; i < LIM_RR; i++) {
                    //System.out.println("*****************************");
                    //System.out.println("\n("+j+","+i+")"+AI_mod[j][i]+"\tK["+i+"]="+K[i]);
                    AI_mod_g[j][i] = AI_g[j][K[i]];
                    //System.out.println("("+j+","+i+")"+AI_mod[j][i]+"\tK["+i+"]="+K[i]);
                    //System.out.println("*****************************");
                    SDATA_mod_g[j][i] = SDATA_g[j][K[i]];
                }
            }
        }





    System.out.println("\n\nAfter Mapping remaining four partial shares of each block to other blocks using key K ...:");            
        for (int j = 0; j < LIM_C && j % 3 == 0; j = j + 3) {
            System.out.println();
            for (int i = 0; i < LIM_RR && i % 2 == 0; i = i + 2) {
                //System.out.println();
              System.out.println("("+j+","+i+")"+AI_mod_g[j][i]+" ("+(j+1)+","+i+")"+AI_mod_g[j+1][i]+" ("+(j+2)+","+(i)+") "+AI_mod_g[j+2][i]);  
              System.out.println("("+(j)+","+(i+1)+")"+AI_mod_g[j][(i+1)]+" ("+(j+1)+","+(i+1)+")"+AI_mod_g[(j+1)][i+1]+" ("+(j+2)+","+(i+1)+") "+AI_mod_g[j+2][(i+1)]);                        }
        }


        // BufferedImage stego_color=new BufferedImage(gray_i.getWidth(), gray_i.getHeight(),  BufferedImage.TYPE_INT_ARGB);
        BufferedImage stego_Gray_g = new BufferedImage(gray_i_g.getWidth(), gray_i_g.getHeight(), BufferedImage.TYPE_INT_ARGB);
        BufferedImage stego_binary_g = new BufferedImage(gray_i_g.getWidth(), gray_i_g.getHeight(), BufferedImage.TYPE_INT_ARGB);
        //stego_binary=Binary_i;
        BufferedImage Authentication_data_g = new BufferedImage(gray_i_g.getWidth(), gray_i_g.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int cy = 0; cy < LIM_C; cy++) {
            for (int cx = 0; cx < LIM_RR; cx++) {
                  
                rgb = gray_with_alpha_g.getRGB(cy, cx);
                      
                alpha = AI_mod_g[cy][cx];
                red = 0;
                green = (rgb & 0x0000FF00) >>> 8;
                blue = 0;
                rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                stego_Gray_g.setRGB(cy, cx, rgb);
                   
               
                
                
                rgb = Binary_i_g.getRGB(cy, cx);
                alpha = AI_mod_g[cy][cx];
                red = 0;
                green = (rgb & 0x0000FF00) >>> 8;
                blue =   0;
                rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                stego_binary_g.setRGB(cy, cx, rgb);


                alpha = 255;
                red = 0;
                green =SDATA_g[cy][cx]+238;
                blue = 0;
                rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;

                Authentication_data_g.setRGB(cy, cx, rgb);
               }
        }

        
         try {
            ImageIO.write(Authentication_data_g, "png", new File(oi.getDirectory() + "Authentication_data_g.png"));
         } catch (IOException e) {}

        try {
            ImageIO.write(stego_Gray_g, "png", new File(oi.getDirectory() + "Stego_Gray_g.png"));
        } catch (IOException e) {
        }
        try {
            ImageIO.write(stego_binary_g, "png", new File(oi.getDirectory() + "Stego_Binary_g.png"));
        } catch (IOException e) {
        }

        try {
            ImageIO.write(Authentication_data_g, "png", new File(oi.getDirectory() + "Authentication_data_g.png"));
        } catch (IOException e) {
        }


  FileWriter fw2_g = new FileWriter(oi.getDirectory() + "KEY_g.txt");
        BufferedWriter bw2_g = new BufferedWriter(fw2_g);

         content = String.valueOf((G_g[0]));
        if(content.indexOf(".")==-1)
          {content=content+".00000";}
        else{
            if(content.substring(content.indexOf(".")+1).length()<5)
            {for(int i=content.substring(content.indexOf(".")+1).length();i<5;i++){
                content=content+"0";
                System.out.println(content);
            }
            }              
        }               
        bw2_g.write(content + "\n");
        

        content = String.valueOf((G_g[1]));
        
         if(content.indexOf(".")==-1)
          {content=content+".00000";}
        else{
            if(content.substring(content.indexOf(".")+1).length()<5)
            {for(int i=content.substring(content.indexOf(".")+1).length();i<5;i++){
                content=content+"0";
                System.out.println(content);
            }
            }              
        }
        
        bw2_g.write(content + "\n");

        for (int i = 0; i < K.length; i++) {
            content = String.valueOf(K[i]);
            bw2_g.write(content + "\n");
        }

        bw2_g.close();      



        
       
        
        
//*********************************************************************************************************************        
        
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //2. for every block of binary and alpha 

        boolean p_b[] = new boolean[6];
        int SDATA_mod_b[][] = new int[LIM_C][LIM_RR];
        int SDATA_b[][] = new int[LIM_C][LIM_RR];
        
         ////System.out.println("\nbefore:");
         blk_no = 0;
        for (int j = 0; j < LIM_C && j % 3 == 0; j = j + 3) {
            for (int i = 0; i < LIM_RR && i % 2 == 0; i = i + 2) {
                System.out.println("\n\n\tProcessing Blue gray Block no="+blk_no++);
                if (BI_b[j][i] == 1) {
                    p_b[0] = true;
                } else {
                    p_b[0] = false;
                }
                if (BI_b[j][i + 1] == 1) {
                    p_b[1] = true;
                } else {
                    p_b[1] = false;
                }
                if (BI_b[j + 1][i] == 1) {
                    p_b[2] = true;
                } else {
                    p_b[2] = false;
                }
                if (BI_b[j + 1][i + 1] == 1) {
                    p_b[3] = true;
                } else {
                    p_b[3] = false;
                }
                if (BI_b[j + 2][i] == 1) {
                    p_b[4] = true;
                } else {
                    p_b[4] = false;
                }
                if (BI_b[j + 2][i + 1] == 1) {
                    p_b[5] = true;
                } else {
                    p_b[5] = false;
                }

                boolean a1_b, a2_b;
                a1_b = (p_b[0] ^ p_b[1]) ^ p_b[2];
                a2_b = (p_b[3] ^ p_b[4]) ^ p_b[5];
                //System.out.println("p[0]="+p[0]+"\tp[1]="+p[1]+"\tp[2]="+p[2]+"\tp[3]="+p[3]+"\tp[4]="+p[4]+"\tp[5]="+p[5]);
                ////System.out.println("s[0]="+a1+"\ts[1]="+a2);
                //System.out.println("a1="+a1+"\ta2="+a2);
                System.out.println("Six binary pixels of current Binary block of corresponding Red gray block:");
                System.out.println("p[0]="+p_b[0]+"\tp[1]="+p_b[1]+"\tp[2]="+p_b[2]+"\tp[3]="+p_b[3]+"\tp[4]="+p_b[4]+"\tp[5]="+p_b[5]);
                System.out.println("Two Authentication signals computed from six binary pixels of current Binary block (to be embedded in authentication image):");
                System.out.println("s[0]=a1=p[0] XOR p[1] XOR p[2]="+a1_b+"\ts[1]=a2=p[3] XOR p[4] XOR [5]="+a2_b);
   
                int m1_b[] = new int[4];
                if (a1_b == true) {
                    m1_b[0] = 1;
                } else {
                    m1_b[0] = 0;
                }
                if (a2_b == true) {
                    m1_b[1] = 1;
                } else {
                    m1_b[1] = 0;
                }
                if (p_b[0] == true) {
                    m1_b[2] = 1;
                } else {
                    m1_b[2] = 0;
                }
                if (p_b[1] == true) {
                    m1_b[3] = 1;
                } else {
                    m1_b[3] = 0;
                }

                int m2_b[] = new int[4];
                if (p_b[2] == true) {
                    m2_b[0] = 1;
                } else {
                    m2_b[0] = 0;
                }
                if (p_b[3] == true) {
                    m2_b[1] = 1;
                } else {
                    m2_b[1] = 0;
                }
                if (p_b[4] == true) {
                    m2_b[2] = 1;
                } else {
                    m2_b[2] = 0;
                }
                if (p_b[5] == true) {
                    m2_b[3] = 1;
                } else {
                    m2_b[3] = 0;
                }

                int d_b = 0, c1_b = 0;
                d_b = (m1_b[0] * 2 * 2 * 2 + m1_b[1] * 2 * 2 + m1_b[2] * 2 + m1_b[3] * 1);
                c1_b = (m2_b[0] * 2 * 2 * 2 + m2_b[1] * 2 * 2 + m2_b[2] * 2 + m2_b[3] * 1);
                int x_b[] = new int[6];
                int q_b[] = new int[6];
                int q1_b[] = new int[6];
                int F_b[] = new int[6];
                
               System.out.println(("d=m1=(a1 * 2 * 2 * 2 + a2 * 2 * 2 + p[0] * 2 + p[1] * 1)="+d_b+"\tc1=m2=(p[2] * 2 * 2 * 2 + p[3] * 2 * 2 + p[4] * 2 + p[5] * 1)="+c1_b));
               System.out.println("six partial shares from current Binary block computed using Shamir secrete share generation by using eqn:q[i] = ((d + c1 * (i+1)) % 17),i=0,1,2,..5;\n(to be embedded in authentication image):");
              
                for (int mmm = 0; mmm < 6; mmm++) {
                    x_b[mmm] = mmm + 1;
                    F_b[mmm] = ((d_b + c1_b * x_b[mmm]) % 17);
                    q_b[mmm] = ((d_b + c1_b * x_b[mmm]) % 17);
                    q1_b[mmm] = (q_b[mmm] + 238);
                    System.out.println("q["+mmm+"]="+q_b[mmm] );
                }
              System.out.println("six partial shares from current Binary block  by using eqn: q1[i]=q[i]+238,i=0,1,2,..5;\n(first two shares will be retained,remaining four shares will be mapped to other blocks using key K ):");
                for (int mmm = 0; mmm < 6; mmm++) {                     
                    System.out.println("q1["+mmm+"]="+q1_b[mmm] );
                }              

                ////System.out.println("Step 7. (Mapping of the partial shares)");
                //System.out.println(("d="+d+"\tc1="+c1));

                ////System.out.println("Step 8. (Embedding two partial shares in the current block)");
                AI_b[j][i] = q1_b[0];
                AI_b[j][i + 1] = q1_b[1];
                AI_b[j + 1][i] = q1_b[2];
                AI_b[j + 1][i + 1] = q1_b[3];
                AI_b[j + 2][i] = q1_b[4];
                AI_b[j + 2][i + 1] = q1_b[5];

                AI_mod_b[j][i] = q1_b[0];
                AI_mod_b[j][i + 1] = q1_b[1];
                AI_mod_b[j + 1][i] = q1_b[2];
                AI_mod_b[j + 1][i + 1] = q1_b[3];
                AI_mod_b[j + 2][i] = q1_b[4];
                AI_mod_b[j + 2][i + 1] = q1_b[5];

                ////System.out.println();
                //for(int iii=0;iii<6;iii++)
                ////System.out.print(" "+q1[iii]+" ");    


                SDATA_mod_b[j][i] = q_b[0];
                SDATA_mod_b[j][i + 1] = q_b[1];
                SDATA_mod_b[j + 1][i] = q_b[2];
                SDATA_mod_b[j + 1][i + 1] = q_b[3];
                SDATA_mod_b[j + 2][i] = q_b[4];
                SDATA_mod_b[j + 2][i + 1] = q_b[5];

                SDATA_b[j][i] = q_b[0];
                SDATA_b[j][i + 1] = q_b[1];
                SDATA_b[j + 1][i] = q_b[2];
                SDATA_b[j + 1][i + 1] = q_b[3];
                SDATA_b[j + 2][i] = q_b[4];
                SDATA_b[j + 2][i + 1] = q_b[5];
            }
        }

       //display randomize result
System.out.println("\n\nStep 7. (Mapping of the partial shares )");
        System.out.println("Step 8. (Embedding two partial shares in the current block of plane)");
        System.out.println("\n\nBefore mapping remaining four partial shares of each block to other blocks using key K:");        
       
        for (int j = 0; j < LIM_C && j % 3 == 0; j = j + 3) {
            System.out.println();
            for (int i = 0; i < LIM_RR && i % 2 == 0; i = i + 2) {
                //System.out.println();
                System.out.println("("+j+","+i+")"+AI_mod_b[j][i]+" ("+(j+1)+","+i+")"+AI_mod_b[j+1][i]+" ("+(j+2)+","+(i)+") "+AI_mod_b[j+2][i]);  
                System.out.println("("+(j)+","+(i+1)+")"+AI_mod_b[j][(i+1)]+" ("+(j+1)+","+(i+1)+")"+AI_mod_b[(j+1)][i+1]+" ("+(j+2)+","+(i+1)+") "+AI_mod_b[j+2][(i+1)]);                   
          
            }
        }
        //randomize data
        // //System.out.println("doing ...");
System.out.println("Mapping remaining four partial shares of each block to other blocks using key K ...");
        for (int j = 0; j < LIM_C; j++) {
            //System.out.println("Randomizing alpha palne ...");
            if (j % 3 != 0) {//get current column 
                //rearrane it as per k

                for (int i = 0; i < LIM_RR; i++) {
                    //System.out.println("*****************************");
                    //System.out.println("\n("+j+","+i+")"+AI_mod[j][i]+"\tK["+i+"]="+K[i]);
                    AI_mod_b[j][i] = AI_b[j][K[i]];
                    //System.out.println("("+j+","+i+")"+AI_mod[j][i]+"\tK["+i+"]="+K[i]);
                    //System.out.println("*****************************");
                    SDATA_mod_b[j][i] = SDATA_b[j][K[i]];
                }
            }
        }





        //System.out.println("\n\nAfter randomizaton:");  
        System.out.println("\n\nAfter Mapping remaining four partial shares of each block to other blocks using key K ...:");        
        for (int j = 0; j < LIM_C && j % 3 == 0; j = j + 3) {
            System.out.println();
            for (int i = 0; i < LIM_RR && i % 2 == 0; i = i + 2) {
                //
       System.out.println("("+j+","+i+")"+AI_mod_b[j][i]+" ("+(j+1)+","+i+")"+AI_mod_b[j+1][i]+" ("+(j+2)+","+(i)+") "+AI_mod_b[j+2][i]);  
        System.out.println("("+(j)+","+(i+1)+")"+AI_mod_b[j][(i+1)]+" ("+(j+1)+","+(i+1)+")"+AI_mod_b[(j+1)][i+1]+" ("+(j+2)+","+(i+1)+") "+AI_mod_b[j+2][(i+1)]);                   
          
   }
        }


        // BufferedImage stego_color=new BufferedImage(gray_i.getWidth(), gray_i.getHeight(),  BufferedImage.TYPE_INT_ARGB);
        BufferedImage stego_Gray_b = new BufferedImage(gray_i_b.getWidth(), gray_i_b.getHeight(), BufferedImage.TYPE_INT_ARGB);
        BufferedImage stego_binary_b = new BufferedImage(gray_i_b.getWidth(), gray_i_b.getHeight(), BufferedImage.TYPE_INT_ARGB);
        //stego_binary=Binary_i;
        BufferedImage Authentication_data_b = new BufferedImage(gray_i_b.getWidth(), gray_i_b.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int cy = 0; cy < LIM_C; cy++) {
            for (int cx = 0; cx < LIM_RR; cx++) {
                  
                rgb = gray_with_alpha_b.getRGB(cy, cx);
                      
                alpha = AI_mod_b[cy][cx];
                red = 0;
                green =0;
                blue =  (rgb & 0x000000FF) >>> 0;
                rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                stego_Gray_b.setRGB(cy, cx, rgb);
                   
               
                
                
                rgb = Binary_i_b.getRGB(cy, cx);
                alpha = AI_mod_b[cy][cx];
                red = 0;
                green =0;
                blue =  (rgb & 0x000000FF) >>> 0;
                rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                stego_binary_b.setRGB(cy, cx, rgb);


                alpha = 255;
                red = 0;
                green =0;
                blue = SDATA_b[cy][cx]+238;
                rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;

                Authentication_data_b.setRGB(cy, cx, rgb);
               }
        }

        
         try {
            ImageIO.write(Authentication_data_b, "png", new File(oi.getDirectory() + "Authentication_data_b.png"));
         } catch (IOException e) {}

        try {
            ImageIO.write(stego_Gray_b, "png", new File(oi.getDirectory() + "Stego_Gray_b.png"));
        } catch (IOException e) {
        }
        try {
            ImageIO.write(stego_binary_b, "png", new File(oi.getDirectory() + "Stego_Binary_b.png"));
        } catch (IOException e) {
        }

        try {
            ImageIO.write(Authentication_data_b, "png", new File(oi.getDirectory() + "Authentication_data_b.png"));
        } catch (IOException e) {
        }

       FileWriter fw2_b = new FileWriter(oi.getDirectory() + "KEY_b.txt");
        BufferedWriter bw2_b = new BufferedWriter(fw2_b);

         content = String.valueOf((G_b[0]));
        if(content.indexOf(".")==-1)
          {content=content+".00000";}
        else{
            if(content.substring(content.indexOf(".")+1).length()<5)
            {for(int i=content.substring(content.indexOf(".")+1).length();i<5;i++){
                content=content+"0";
                System.out.println(content);
            }
            }              
        }               
        bw2_b.write(content + "\n");
        

        content = String.valueOf((G_b[1]));
        
         if(content.indexOf(".")==-1)
          {content=content+".00000";}
        else{
            if(content.substring(content.indexOf(".")+1).length()<5)
            {for(int i=content.substring(content.indexOf(".")+1).length();i<5;i++){
                content=content+"0";
                System.out.println(content);
            }
            }              
        }
        
        bw2_b.write(content + "\n");

        for (int i = 0; i < K.length; i++) {
            content = String.valueOf(K[i]);
            bw2_b.write(content + "\n");
        }

        bw2_b.close();      


        
        
        
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
///save authentication signal in color format
        BufferedImage Authentication_data_color = new BufferedImage(gray_i_r.getWidth(), gray_i_r.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int cy = 0; cy < LIM_C; cy++) {
            for (int cx = 0; cx < LIM_RR; cx++) {
                alpha = 255;
                                
                rgb = Authentication_data_r.getRGB(cy, cx);
                red = (rgb & 0x00FF0000) >>> 16;
                 
                rgb = Authentication_data_g.getRGB(cy, cx);
                green = (rgb & 0x0000FF00) >>> 8;
                 
                rgb = Authentication_data_b.getRGB(cy, cx);
                blue = (rgb & 0x000000FF) >>> 0;
                
                //red=red-238;
                //green=green-238;
                //blue=blue-238;
                rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                Authentication_data_color.setRGB(cy, cx, rgb);
                
            }
        }
          try {
            ImageIO.write(Authentication_data_color, "png", new File(oi.getDirectory() + "Authentication_data_color.png"));
         } catch (IOException e) {}

        display_images();
        image_oi.flush();

        System.out.println("End of alg 3"); 
    }
        public static void MAIN(String args) throws IOException {
           //1.1 read image
        ImagePlus original_image = null;
        BufferedImage image_oi = null;
        File f_oi;
        oi = new ij.io.OpenDialog("Select image", "E:\\DATA\\YES_MAN\\JAVA_IMPL\\", "lenna.bmp");
        try {
            f_oi = new File(oi.getDirectory() + oi.getFileName());
            image_oi = ImageIO.read(f_oi);
        } catch (IOException e) {
        }
        try {
            ImageIO.write(image_oi, "png", new File(oi.getDirectory() + "Original_Color.png"));
        } catch (IOException e) {
        }



        BufferedImage gray_i_r = new BufferedImage(image_oi.getWidth(), image_oi.getHeight(), BufferedImage.TYPE_INT_ARGB);

        BufferedImage gray_with_alpha_r = new BufferedImage(image_oi.getWidth(), image_oi.getHeight(), BufferedImage.TYPE_INT_ARGB);

         
        int alpha = 0;
        int red = 0;
        int green = 0;
        int blue = 0;
        int rgb = 0;
        int k = 0;
        for (int i = 0; i < image_oi.getWidth(); i++) {
            for (int j = 0; j < image_oi.getHeight(); j++) {
                rgb = image_oi.getRGB(i, j);
                alpha = 255;
                red = (rgb & 0x00FF0000) >>> 16;
                green = 0;
                blue =  0;
                rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                 
                gray_with_alpha_r.setRGB(i, j, rgb);
                gray_i_r.setRGB(i, j, rgb);
                
             }
        }

        //1.3 save graysacle image
        try {
            ImageIO.write(gray_with_alpha_r, "png", new File(oi.getDirectory() + "gray_with_alpha_r.png"));
           } catch (IOException e) {
        }

        //1.4 convert to binary     
        int data_r[] = new int[image_oi.getWidth() * image_oi.getHeight()];
        int Gray_Image_original_r[][] = new int[image_oi.getWidth()][image_oi.getHeight()];
        int Alpha_Image_original_r[][] = new int[image_oi.getWidth()][image_oi.getHeight()];
        int Binary_Image_original_r[][] = new int[image_oi.getWidth()][image_oi.getHeight()];
         
         
         
        k = 0;
        for (int cx = 0; cx < image_oi.getWidth(); cx++) {
            for (int cy = 0; cy < image_oi.getHeight(); cy++) {
                rgb = gray_i_r.getRGB(cx, cy);
                red = (rgb & 0x00FF0000) >>> 16;
         
                data_r[k] = (red ) ;
                Gray_Image_original_r[cx][cy] = (red );
                Alpha_Image_original_r[cx][cy] = 255;
                
                
                k++;
            }
        }
        //1.4.1 compute g1 and g2
        int histogram_r[] = new int[256];//histogram og gray image
         
        for (int i = 0; i < data_r.length; i++) {
            histogram_r[data_r[i]] = histogram_r[data_r[i]] + 1;
        }

        G_r = Moments(histogram_r);
         //System.out.println("Stage I—generation of authentication signals");
        //System.out.println("\nStep 1.Input image binarization");
        //System.out.println("\nMoment Preserving thresholding");
        //System.out.println("Value of gray level g1="+G[0]);
        //System.out.println("Value of gray level g2="+G[1]);
        //1.4.2 compute threshold for binarization  
        thres_r = (G_r[0] + G_r[1]) / 2.0;
          
        BufferedImage Binary_i_r = new BufferedImage(gray_i_r.getWidth(), gray_i_r.getHeight(), BufferedImage.TYPE_INT_ARGB);
         
        //1.4.3 binarize gray image
        k = 0;
        for (int cx = 0; cx < image_oi.getWidth(); cx++) {
            for (int cy = 0; cy < image_oi.getHeight(); cy++) {
                if (data_r[k] < thres_r) {
                    Binary_Image_original_r[cx][cy] = 0;
                } 
                else {
                    Binary_Image_original_r[cx][cy] = 1;
                }
          
                k++;
                alpha = 255;
                red   = Binary_Image_original_r[cx][cy] * 255;
                green = 0;
                blue  = 0;
                rgb   = (alpha << 24) | (red << 16) | (green << 8) | blue;
                Binary_i_r.setRGB(cx, cy, rgb);
                
                 
            }
        }

         
        try {
            ImageIO.write(Binary_i_r, "png", new File(oi.getDirectory() + "Binary_i_r.png"));
             }   catch (IOException e) {
        }

        //get Binary and alpha into 2D array
        int LIM_RR, LIM_C;
        LIM_C = image_oi.getWidth() - (image_oi.getWidth() % 3);//columns
        LIM_RR = image_oi.getHeight() - (image_oi.getHeight() % 2);//rows
        int BI_r[][] = new int[LIM_C][LIM_RR];
        int AI_r[][] = new int[LIM_C][LIM_RR];
        int AI_mod_r[][] = new int[LIM_C][LIM_RR];

         
        //System.out.println("\n\nContents of Binarized image");
        for (int cx = 0; cx < LIM_C; cx++) {
            for (int cy = 0; cy < LIM_RR; cy++) {
                BI_r[cx][cy] = Binary_Image_original_r[cx][cy];
                 //System.out.println("BI="+BI[cx][cy]+"\tcol="+cx+"\trow="+cy);         
            }
        }

//////////////////////////////////////////////////////////////////////////////////////////////////
        
        BufferedImage gray_i_g = new BufferedImage(image_oi.getWidth(), image_oi.getHeight(), BufferedImage.TYPE_INT_ARGB);

        BufferedImage gray_with_alpha_g = new BufferedImage(image_oi.getWidth(), image_oi.getHeight(), BufferedImage.TYPE_INT_ARGB);

         
         alpha = 0;
         red = 0;
         green = 0;
         blue = 0;
         rgb = 0;
         k = 0;
        for (int i = 0; i < image_oi.getWidth(); i++) {
            for (int j = 0; j < image_oi.getHeight(); j++) {
                rgb = image_oi.getRGB(i, j);
                alpha = 255;
                red = 0;
                green =(rgb & 0x0000FF00) >>> 8;
                blue =  0;
                rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                 
                gray_with_alpha_g.setRGB(i, j, rgb);
                gray_i_g.setRGB(i, j, rgb);
                
             }
        }

        //1.3 save graysacle image
        try {
            ImageIO.write(gray_with_alpha_g, "png", new File(oi.getDirectory() + "gray_with_alpha_g.png"));
           } catch (IOException e) {
        }

        //1.4 convert to binary     
        int data_g[] = new int[image_oi.getWidth() * image_oi.getHeight()];
        int Gray_Image_original_g[][] = new int[image_oi.getWidth()][image_oi.getHeight()];
        int Alpha_Image_original_g[][] = new int[image_oi.getWidth()][image_oi.getHeight()];
        int Binary_Image_original_g[][] = new int[image_oi.getWidth()][image_oi.getHeight()];
         
         
         
        k = 0;
        for (int cx = 0; cx < image_oi.getWidth(); cx++) {
            for (int cy = 0; cy < image_oi.getHeight(); cy++) {
                rgb = gray_i_g.getRGB(cx, cy);
                green = (rgb & 0x0000FF00) >>> 8;
         
                data_g[k] = (green ) ;
                Gray_Image_original_g[cx][cy] = (green );
                Alpha_Image_original_g[cx][cy] = 255;
                
                
                k++;
            }
        }
        //1.4.1 compute g1 and g2
        int histogram_g[] = new int[256];//histogram og gray image
         
        for (int i = 0; i < data_g.length; i++) {
            histogram_g[data_g[i]] = histogram_g[data_g[i]] + 1;
        }

        G_g = Moments(histogram_g);
         //System.out.println("Stage I—generation of authentication signals");
        //System.out.println("\nStep 1.Input image binarization");
        //System.out.println("\nMoment Preserving thresholding");
        //System.out.println("Value of gray level g1="+G[0]);
        //System.out.println("Value of gray level g2="+G[1]);
        //1.4.2 compute threshold for binarization  
        thres_g = (G_g[0] + G_g[1]) / 2.0;
          
        BufferedImage Binary_i_g = new BufferedImage(gray_i_g.getWidth(), gray_i_g.getHeight(), BufferedImage.TYPE_INT_ARGB);
         
        //1.4.3 binarize gray image
        k = 0;
        for (int cx = 0; cx < image_oi.getWidth(); cx++) {
            for (int cy = 0; cy < image_oi.getHeight(); cy++) {
                if (data_g[k] < thres_g) {
                    Binary_Image_original_g[cx][cy] = 0;
                } 
                else {
                    Binary_Image_original_g[cx][cy] = 1;
                }
          
                k++;
                alpha = 255;
                red   = 0;
                green = Binary_Image_original_g[cx][cy] * 255;
                blue  = 0;
                rgb   = (alpha << 24) | (red << 16) | (green << 8) | blue;
                Binary_i_g.setRGB(cx, cy, rgb);
                
                 
            }
        }

         
        try {
            ImageIO.write(Binary_i_g, "png", new File(oi.getDirectory() + "Binary_i_g.png"));
             }   catch (IOException e) {
        }

        //get Binary and alpha into 2D array
         
        LIM_C = image_oi.getWidth() - (image_oi.getWidth() % 3);//columns
        LIM_RR = image_oi.getHeight() - (image_oi.getHeight() % 2);//rows
        int BI_g[][] = new int[LIM_C][LIM_RR];
        int AI_g[][] = new int[LIM_C][LIM_RR];
        int AI_mod_g[][] = new int[LIM_C][LIM_RR];

         
        //System.out.println("\n\nContents of Binarized image");
        for (int cx = 0; cx < LIM_C; cx++) {
            for (int cy = 0; cy < LIM_RR; cy++) {
                BI_g[cx][cy] = Binary_Image_original_g[cx][cy];
                 //System.out.println("BI="+BI[cx][cy]+"\tcol="+cx+"\trow="+cy);         
            }
        }




        
        
        
        
//////////////////////////////////////////////////////////////////////////////////////////////////        

//**************************************************************************************************



        BufferedImage gray_i_b = new BufferedImage(image_oi.getWidth(), image_oi.getHeight(), BufferedImage.TYPE_INT_ARGB);

        BufferedImage gray_with_alpha_b = new BufferedImage(image_oi.getWidth(), image_oi.getHeight(), BufferedImage.TYPE_INT_ARGB);

         
         alpha = 0;
         red = 0;
         green = 0;
         blue = 0;
         rgb = 0;
         k = 0;
        for (int i = 0; i < image_oi.getWidth(); i++) {
            for (int j = 0; j < image_oi.getHeight(); j++) {
                rgb = image_oi.getRGB(i, j);
                alpha = 255;
                red = 0;
                green =0;
                blue =  (rgb & 0x000000FF) >>> 0;
                rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                 
                gray_with_alpha_b.setRGB(i, j, rgb);
                gray_i_b.setRGB(i, j, rgb);
                
             }
        }

        //1.3 save graysacle image
        try {
            ImageIO.write(gray_with_alpha_b, "png", new File(oi.getDirectory() + "gray_with_alpha_b.png"));
           } catch (IOException e) {
        }

        //1.4 convert to binary     
        int data_b[] = new int[image_oi.getWidth() * image_oi.getHeight()];
        int Gray_Image_original_b[][] = new int[image_oi.getWidth()][image_oi.getHeight()];
        int Alpha_Image_original_b[][] = new int[image_oi.getWidth()][image_oi.getHeight()];
        int Binary_Image_original_b[][] = new int[image_oi.getWidth()][image_oi.getHeight()];
         
         
         
        k = 0;
        for (int cx = 0; cx < image_oi.getWidth(); cx++) {
            for (int cy = 0; cy < image_oi.getHeight(); cy++) {
                rgb = gray_i_b.getRGB(cx, cy);
                blue = (rgb & 0x000000FF) >>> 0;
         
                data_b[k] = (blue ) ;
                Gray_Image_original_b[cx][cy] = (blue );
                Alpha_Image_original_b[cx][cy] = 255;
                
                
                k++;
            }
        }
        //1.4.1 compute g1 and g2
        int histogram_b[] = new int[256];//histogram og gray image
         
        for (int i = 0; i < data_b.length; i++) {
            histogram_b[data_b[i]] = histogram_b[data_b[i]] + 1;
        }

        G_b = Moments(histogram_b);
         //System.out.println("Stage I—generation of authentication signals");
        //System.out.println("\nStep 1.Input image binarization");
        //System.out.println("\nMoment Preserving thresholding");
        //System.out.println("Value of gray level g1="+G[0]);
        //System.out.println("Value of gray level g2="+G[1]);
        //1.4.2 compute threshold for binarization  
        thres_b = (G_b[0] + G_b[1]) / 2.0;
          
        BufferedImage Binary_i_b = new BufferedImage(gray_i_b.getWidth(), gray_i_b.getHeight(), BufferedImage.TYPE_INT_ARGB);
         
        //1.4.3 binarize gray image
        k = 0;
        for (int cx = 0; cx < image_oi.getWidth(); cx++) {
            for (int cy = 0; cy < image_oi.getHeight(); cy++) {
                if (data_b[k] < thres_b) {
                    Binary_Image_original_b[cx][cy] = 0;
                } 
                else {
                    Binary_Image_original_b[cx][cy] = 1;
                }
          
                k++;
                alpha = 255;
                red   = 0;
                green = 0;
                blue  = Binary_Image_original_b[cx][cy] * 255;
                rgb   = (alpha << 24) | (red << 16) | (green << 8) | blue;
                Binary_i_b.setRGB(cx, cy, rgb);
                
                 
            }
        }

         
        try {
            ImageIO.write(Binary_i_b, "png", new File(oi.getDirectory() + "Binary_i_b.png"));
             }   catch (IOException e) {
        }

        //get Binary and alpha into 2D array
         LIM_C = image_oi.getWidth() - (image_oi.getWidth() % 3);//columns
        LIM_RR = image_oi.getHeight() - (image_oi.getHeight() % 2);//rows
        int BI_b[][] = new int[LIM_C][LIM_RR];
        int AI_b[][] = new int[LIM_C][LIM_RR];
        int AI_mod_b[][] = new int[LIM_C][LIM_RR];

         
        //System.out.println("\n\nContents of Binarized image");
        for (int cx = 0; cx < LIM_C; cx++) {
            for (int cy = 0; cy < LIM_RR; cy++) {
                BI_b[cx][cy] = Binary_Image_original_b[cx][cy];
                 //System.out.println("BI="+BI[cx][cy]+"\tcol="+cx+"\trow="+cy);         
            }
        }

        
        
        
//***************************************************************************************************        
        //find key K and inverse
        int K[] = new int[LIM_RR];
        int K_i[] = new int[LIM_RR];
        RandomPermutation rp = new RandomPermutation();
        K = rp.GenerateRandomPermutation(LIM_RR);
        //System.out.println("\nGenerating Key value for authentication:\nKey Length="+K.length);
        for (k = 0; k < K.length; k++)
           //System.out.print("  " + K[k])
           ;

        int index = -1;
        for (int ii = 0; ii < LIM_RR; ii++) {
            k = 0;
            index = -1;
            while (true) {
                if (K[k] == ii) {
                    index = k;
                    break;
                } 
                else {
                    k++;
                }
            }
            K_i[ii] = index;
        }

        //System.out.println("\nInverse Key length="+K.length+"\nInverse Key:");
        for (int ii = 0; ii < LIM_RR; ii++) {//System.out.print(K_i[ii]+" ");
        }
        
        
        
        
        
        
        
        
        
        
        
        
//*********************************************************************************************************************        
        //2. for every block of binary and alpha 

        boolean p_r[] = new boolean[6];
        int SDATA_mod_r[][] = new int[LIM_C][LIM_RR];
        int SDATA_r[][] = new int[LIM_C][LIM_RR];
        
         ////System.out.println("\nbefore:");
        int blk_no = 0;
        for (int j = 0; j < LIM_C && j % 3 == 0; j = j + 3) {
            for (int i = 0; i < LIM_RR && i % 2 == 0; i = i + 2) {
                //System.out.println("\n\nBlock no="+blk_no++);
                if (BI_r[j][i] == 1) {
                    p_r[0] = true;
                } else {
                    p_r[0] = false;
                }
                if (BI_r[j][i + 1] == 1) {
                    p_r[1] = true;
                } else {
                    p_r[1] = false;
                }
                if (BI_r[j + 1][i] == 1) {
                    p_r[2] = true;
                } else {
                    p_r[2] = false;
                }
                if (BI_r[j + 1][i + 1] == 1) {
                    p_r[3] = true;
                } else {
                    p_r[3] = false;
                }
                if (BI_r[j + 2][i] == 1) {
                    p_r[4] = true;
                } else {
                    p_r[4] = false;
                }
                if (BI_r[j + 2][i + 1] == 1) {
                    p_r[5] = true;
                } else {
                    p_r[5] = false;
                }

                boolean a1_r, a2_r;
                a1_r = (p_r[0] ^ p_r[1]) ^ p_r[2];
                a2_r = (p_r[3] ^ p_r[4]) ^ p_r[5];
                //System.out.println("p[0]="+p[0]+"\tp[1]="+p[1]+"\tp[2]="+p[2]+"\tp[3]="+p[3]+"\tp[4]="+p[4]+"\tp[5]="+p[5]);
                ////System.out.println("s[0]="+a1+"\ts[1]="+a2);
                //System.out.println("a1="+a1+"\ta2="+a2);

                int m1_r[] = new int[4];
                if (a1_r == true) {
                    m1_r[0] = 1;
                } else {
                    m1_r[0] = 0;
                }
                if (a2_r == true) {
                    m1_r[1] = 1;
                } else {
                    m1_r[1] = 0;
                }
                if (p_r[0] == true) {
                    m1_r[2] = 1;
                } else {
                    m1_r[2] = 0;
                }
                if (p_r[1] == true) {
                    m1_r[3] = 1;
                } else {
                    m1_r[3] = 0;
                }

                int m2_r[] = new int[4];
                if (p_r[2] == true) {
                    m2_r[0] = 1;
                } else {
                    m2_r[0] = 0;
                }
                if (p_r[3] == true) {
                    m2_r[1] = 1;
                } else {
                    m2_r[1] = 0;
                }
                if (p_r[4] == true) {
                    m2_r[2] = 1;
                } else {
                    m2_r[2] = 0;
                }
                if (p_r[5] == true) {
                    m2_r[3] = 1;
                } else {
                    m2_r[3] = 0;
                }

                int d_r = 0, c1_r = 0;
                d_r = (m1_r[0] * 2 * 2 * 2 + m1_r[1] * 2 * 2 + m1_r[2] * 2 + m1_r[3] * 1);
                c1_r = (m2_r[0] * 2 * 2 * 2 + m2_r[1] * 2 * 2 + m2_r[2] * 2 + m2_r[3] * 1);
                int x_r[] = new int[6];
                int q_r[] = new int[6];
                int q1_r[] = new int[6];
                int F_r[] = new int[6];
                for (int mmm = 0; mmm < 6; mmm++) {
                    x_r[mmm] = mmm + 1;
                    F_r[mmm] = ((d_r + c1_r * x_r[mmm]) % 17);
                    q_r[mmm] = ((d_r + c1_r * x_r[mmm]) % 17);
                    q1_r[mmm] = (q_r[mmm] + 238);
                    //System.out.println("q["+mmm+"]="+q[mmm] );
                }
                ////System.out.println("Step 7. (Mapping of the partial shares)");
                //System.out.println(("d="+d+"\tc1="+c1));

                ////System.out.println("Step 8. (Embedding two partial shares in the current block)");
                AI_r[j][i] = q1_r[0];
                AI_r[j][i + 1] = q1_r[1];
                AI_r[j + 1][i] = q1_r[2];
                AI_r[j + 1][i + 1] = q1_r[3];
                AI_r[j + 2][i] = q1_r[4];
                AI_r[j + 2][i + 1] = q1_r[5];

                AI_mod_r[j][i] = q1_r[0];
                AI_mod_r[j][i + 1] = q1_r[1];
                AI_mod_r[j + 1][i] = q1_r[2];
                AI_mod_r[j + 1][i + 1] = q1_r[3];
                AI_mod_r[j + 2][i] = q1_r[4];
                AI_mod_r[j + 2][i + 1] = q1_r[5];

                ////System.out.println();
                //for(int iii=0;iii<6;iii++)
                ////System.out.print(" "+q1[iii]+" ");    


                SDATA_mod_r[j][i] = q_r[0];
                SDATA_mod_r[j][i + 1] = q_r[1];
                SDATA_mod_r[j + 1][i] = q_r[2];
                SDATA_mod_r[j + 1][i + 1] = q_r[3];
                SDATA_mod_r[j + 2][i] = q_r[4];
                SDATA_mod_r[j + 2][i + 1] = q_r[5];

                SDATA_r[j][i] = q_r[0];
                SDATA_r[j][i + 1] = q_r[1];
                SDATA_r[j + 1][i] = q_r[2];
                SDATA_r[j + 1][i + 1] = q_r[3];
                SDATA_r[j + 2][i] = q_r[4];
                SDATA_r[j + 2][i + 1] = q_r[5];
            }
        }

        //display randomize result
        //System.out.println("\n\nBefore randomizaton:");        
        for (int j = 0; j < LIM_C && j % 3 == 0; j = j + 3) {
            for (int i = 0; i < LIM_RR && i % 2 == 0; i = i + 2) {
                //System.out.println();
                //System.out.println("("+j+","+i+")"+AI_mod[j][i]+" ("+(j+1)+","+i+")"+AI_mod[j+1][i]+" ("+(j+2)+","+(i)+") "+AI_mod[j+2][i]);  
                //System.out.println("("+(j)+","+(i+1)+")"+AI_mod[j][(i+1)]+" ("+(j+1)+","+(i+1)+")"+AI_mod[(j+1)][i+1]+" ("+(j+2)+","+(i+1)+") "+AI_mod[j+2][(i+1)]);                   
            }
        }
        //randomize data
        // //System.out.println("doing ...");

        for (int j = 0; j < LIM_C; j++) {
            //System.out.println("Randomizing alpha palne ...");
            if (j % 3 != 0) {//get current column 
                //rearrane it as per k

                for (int i = 0; i < LIM_RR; i++) {
                    //System.out.println("*****************************");
                    //System.out.println("\n("+j+","+i+")"+AI_mod[j][i]+"\tK["+i+"]="+K[i]);
                    AI_mod_r[j][i] = AI_r[j][K[i]];
                    //System.out.println("("+j+","+i+")"+AI_mod[j][i]+"\tK["+i+"]="+K[i]);
                    //System.out.println("*****************************");
                    SDATA_mod_r[j][i] = SDATA_r[j][K[i]];
                }
            }
        }





        //System.out.println("\n\nAfter randomizaton:");        
        for (int j = 0; j < LIM_C && j % 3 == 0; j = j + 3) {
            for (int i = 0; i < LIM_RR && i % 2 == 0; i = i + 2) {
                //System.out.println();
                //System.out.println("("+j+","+i+")"+AI_mod[j][i]+" ("+(j+1)+","+i+")"+AI_mod[j+1][i]+" ("+(j+2)+","+(i)+") "+AI_mod[j+2][i]);  
                //System.out.println("("+(j)+","+(i+1)+")"+AI_mod[j][(i+1)]+" ("+(j+1)+","+(i+1)+")"+AI_mod[(j+1)][i+1]+" ("+(j+2)+","+(i+1)+") "+AI_mod[j+2][(i+1)]);                   
            }
        }


        // BufferedImage stego_color=new BufferedImage(gray_i.getWidth(), gray_i.getHeight(),  BufferedImage.TYPE_INT_ARGB);
        BufferedImage stego_gray_r = new BufferedImage(gray_i_r.getWidth(), gray_i_r.getHeight(), BufferedImage.TYPE_INT_ARGB);
        BufferedImage stego_binary_r = new BufferedImage(gray_i_r.getWidth(), gray_i_r.getHeight(), BufferedImage.TYPE_INT_ARGB);
        //stego_binary=Binary_i;
        BufferedImage Authentication_data_r = new BufferedImage(gray_i_r.getWidth(), gray_i_r.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int cy = 0; cy < LIM_C; cy++) {
            for (int cx = 0; cx < LIM_RR; cx++) {
                  
                rgb = gray_with_alpha_r.getRGB(cy, cx);
                alpha = AI_mod_r[cy][cx];
                
                red = (rgb & 0x00FF0000) >>> 16;
                green = 0;
                blue = 0;
                rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                stego_gray_r.setRGB(cy, cx, rgb);
                   
               
                
                
                rgb = Binary_i_r.getRGB(cy, cx);
                alpha = AI_mod_r[cy][cx];
                red = (rgb & 0x00FF0000) >>> 16;
                green = 0;
                blue =   0;
                rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                stego_binary_r.setRGB(cy, cx, rgb);


                alpha = 255;
                red = SDATA_r[cy][cx]+238;
                green = 0;
                blue = 0;
                rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;

                Authentication_data_r.setRGB(cy, cx, rgb);
               }
        }

        
         try {
            ImageIO.write(Authentication_data_r, "png", new File(oi.getDirectory() + "Authentication_data_r.png"));
         } catch (IOException e) {}

        try {
            ImageIO.write(stego_gray_r, "png", new File(oi.getDirectory() + "Stego_Gray_r.png"));
        } catch (IOException e) {
        }
        try {
            ImageIO.write(stego_binary_r, "png", new File(oi.getDirectory() + "Stego_Binary_r.png"));
        } catch (IOException e) {
        }

        try {
            ImageIO.write(Authentication_data_r, "png", new File(oi.getDirectory() + "Authentication_data_r.png"));
        } catch (IOException e) {
        }


        //save values key and g1 g2
        FileWriter fw2_r = new FileWriter(oi.getDirectory() + "KEY_r.txt");
        BufferedWriter bw2_r = new BufferedWriter(fw2_r);

        String content = String.valueOf((G_r[0]));
        bw2_r.write(content + "\n");

        content = String.valueOf((G_r[1]));
        bw2_r.write(content + "\n");

        for (int i = 0; i < K.length; i++) {
            content = String.valueOf(K[i]);
            bw2_r.write(content + "\n");
        }

        bw2_r.close();

//*********************************************************************************************************************
        
        //2. for every block of binary and alpha 

        boolean p_g[] = new boolean[6];
        int SDATA_mod_g[][] = new int[LIM_C][LIM_RR];
        int SDATA_g[][] = new int[LIM_C][LIM_RR];
        
         ////System.out.println("\nbefore:");
        blk_no = 0;
        for (int j = 0; j < LIM_C && j % 3 == 0; j = j + 3) {
            for (int i = 0; i < LIM_RR && i % 2 == 0; i = i + 2) {
                //System.out.println("\n\nBlock no="+blk_no++);
                if (BI_g[j][i] == 1) {
                    p_g[0] = true;
                } else {
                    p_g[0] = false;
                }
                if (BI_g[j][i + 1] == 1) {
                    p_g[1] = true;
                } else {
                    p_g[1] = false;
                }
                if (BI_g[j + 1][i] == 1) {
                    p_g[2] = true;
                } else {
                    p_g[2] = false;
                }
                if (BI_g[j + 1][i + 1] == 1) {
                    p_g[3] = true;
                } else {
                    p_g[3] = false;
                }
                if (BI_g[j + 2][i] == 1) {
                    p_g[4] = true;
                } else {
                    p_g[4] = false;
                }
                if (BI_g[j + 2][i + 1] == 1) {
                    p_g[5] = true;
                } else {
                    p_g[5] = false;
                }

                boolean a1_g, a2_g;
                a1_g = (p_g[0] ^ p_g[1]) ^ p_g[2];
                a2_g = (p_g[3] ^ p_g[4]) ^ p_g[5];
                //System.out.println("p[0]="+p[0]+"\tp[1]="+p[1]+"\tp[2]="+p[2]+"\tp[3]="+p[3]+"\tp[4]="+p[4]+"\tp[5]="+p[5]);
                ////System.out.println("s[0]="+a1+"\ts[1]="+a2);
                //System.out.println("a1="+a1+"\ta2="+a2);

                int m1_g[] = new int[4];
                if (a1_g == true) {
                    m1_g[0] = 1;
                } else {
                    m1_g[0] = 0;
                }
                if (a2_g == true) {
                    m1_g[1] = 1;
                } else {
                    m1_g[1] = 0;
                }
                if (p_g[0] == true) {
                    m1_g[2] = 1;
                } else {
                    m1_g[2] = 0;
                }
                if (p_g[1] == true) {
                    m1_g[3] = 1;
                } else {
                    m1_g[3] = 0;
                }

                int m2_g[] = new int[4];
                if (p_g[2] == true) {
                    m2_g[0] = 1;
                } else {
                    m2_g[0] = 0;
                }
                if (p_g[3] == true) {
                    m2_g[1] = 1;
                } else {
                    m2_g[1] = 0;
                }
                if (p_g[4] == true) {
                    m2_g[2] = 1;
                } else {
                    m2_g[2] = 0;
                }
                if (p_g[5] == true) {
                    m2_g[3] = 1;
                } else {
                    m2_g[3] = 0;
                }

                int d_g = 0, c1_g = 0;
                d_g = (m1_g[0] * 2 * 2 * 2 + m1_g[1] * 2 * 2 + m1_g[2] * 2 + m1_g[3] * 1);
                c1_g = (m2_g[0] * 2 * 2 * 2 + m2_g[1] * 2 * 2 + m2_g[2] * 2 + m2_g[3] * 1);
                int x_g[] = new int[6];
                int q_g[] = new int[6];
                int q1_g[] = new int[6];
                int F_g[] = new int[6];
                for (int mmm = 0; mmm < 6; mmm++) {
                    x_g[mmm] = mmm + 1;
                    F_g[mmm] = ((d_g + c1_g * x_g[mmm]) % 17);
                    q_g[mmm] = ((d_g + c1_g * x_g[mmm]) % 17);
                    q1_g[mmm] = (q_g[mmm] + 238);
                    //System.out.println("q["+mmm+"]="+q[mmm] );
                }
                ////System.out.println("Step 7. (Mapping of the partial shares)");
                //System.out.println(("d="+d+"\tc1="+c1));

                ////System.out.println("Step 8. (Embedding two partial shares in the current block)");
                AI_g[j][i] = q1_g[0];
                AI_g[j][i + 1] = q1_g[1];
                AI_g[j + 1][i] = q1_g[2];
                AI_g[j + 1][i + 1] = q1_g[3];
                AI_g[j + 2][i] = q1_g[4];
                AI_g[j + 2][i + 1] = q1_g[5];

                AI_mod_g[j][i] = q1_g[0];
                AI_mod_g[j][i + 1] = q1_g[1];
                AI_mod_g[j + 1][i] = q1_g[2];
                AI_mod_g[j + 1][i + 1] = q1_g[3];
                AI_mod_g[j + 2][i] = q1_g[4];
                AI_mod_g[j + 2][i + 1] = q1_g[5];

                ////System.out.println();
                //for(int iii=0;iii<6;iii++)
                ////System.out.print(" "+q1[iii]+" ");    


                SDATA_mod_g[j][i] = q_g[0];
                SDATA_mod_g[j][i + 1] = q_g[1];
                SDATA_mod_g[j + 1][i] = q_g[2];
                SDATA_mod_g[j + 1][i + 1] = q_g[3];
                SDATA_mod_g[j + 2][i] = q_g[4];
                SDATA_mod_g[j + 2][i + 1] = q_g[5];

                SDATA_g[j][i] = q_g[0];
                SDATA_g[j][i + 1] = q_g[1];
                SDATA_g[j + 1][i] = q_g[2];
                SDATA_g[j + 1][i + 1] = q_g[3];
                SDATA_g[j + 2][i] = q_g[4];
                SDATA_g[j + 2][i + 1] = q_g[5];
            }
        }

        //display randomize result
        //System.out.println("\n\nBefore randomizaton:");        
        for (int j = 0; j < LIM_C && j % 3 == 0; j = j + 3) {
            for (int i = 0; i < LIM_RR && i % 2 == 0; i = i + 2) {
                //System.out.println();
                //System.out.println("("+j+","+i+")"+AI_mod[j][i]+" ("+(j+1)+","+i+")"+AI_mod[j+1][i]+" ("+(j+2)+","+(i)+") "+AI_mod[j+2][i]);  
                //System.out.println("("+(j)+","+(i+1)+")"+AI_mod[j][(i+1)]+" ("+(j+1)+","+(i+1)+")"+AI_mod[(j+1)][i+1]+" ("+(j+2)+","+(i+1)+") "+AI_mod[j+2][(i+1)]);                   
            }
        }
        //randomize data
        // //System.out.println("doing ...");

        for (int j = 0; j < LIM_C; j++) {
            //System.out.println("Randomizing alpha palne ...");
            if (j % 3 != 0) {//get current column 
                //rearrane it as per k

                for (int i = 0; i < LIM_RR; i++) {
                    //System.out.println("*****************************");
                    //System.out.println("\n("+j+","+i+")"+AI_mod[j][i]+"\tK["+i+"]="+K[i]);
                    AI_mod_g[j][i] = AI_g[j][K[i]];
                    //System.out.println("("+j+","+i+")"+AI_mod[j][i]+"\tK["+i+"]="+K[i]);
                    //System.out.println("*****************************");
                    SDATA_mod_g[j][i] = SDATA_g[j][K[i]];
                }
            }
        }





        //System.out.println("\n\nAfter randomizaton:");        
        for (int j = 0; j < LIM_C && j % 3 == 0; j = j + 3) {
            for (int i = 0; i < LIM_RR && i % 2 == 0; i = i + 2) {
                //System.out.println();
                //System.out.println("("+j+","+i+")"+AI_mod[j][i]+" ("+(j+1)+","+i+")"+AI_mod[j+1][i]+" ("+(j+2)+","+(i)+") "+AI_mod[j+2][i]);  
                //System.out.println("("+(j)+","+(i+1)+")"+AI_mod[j][(i+1)]+" ("+(j+1)+","+(i+1)+")"+AI_mod[(j+1)][i+1]+" ("+(j+2)+","+(i+1)+") "+AI_mod[j+2][(i+1)]);                   
            }
        }


        // BufferedImage stego_color=new BufferedImage(gray_i.getWidth(), gray_i.getHeight(),  BufferedImage.TYPE_INT_ARGB);
        BufferedImage stego_Gray_g = new BufferedImage(gray_i_g.getWidth(), gray_i_g.getHeight(), BufferedImage.TYPE_INT_ARGB);
        BufferedImage stego_binary_g = new BufferedImage(gray_i_g.getWidth(), gray_i_g.getHeight(), BufferedImage.TYPE_INT_ARGB);
        //stego_binary=Binary_i;
        BufferedImage Authentication_data_g = new BufferedImage(gray_i_g.getWidth(), gray_i_g.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int cy = 0; cy < LIM_C; cy++) {
            for (int cx = 0; cx < LIM_RR; cx++) {
                  
                rgb = gray_with_alpha_g.getRGB(cy, cx);
                      
                alpha = AI_mod_g[cy][cx];
                red = 0;
                green = (rgb & 0x0000FF00) >>> 8;
                blue = 0;
                rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                stego_Gray_g.setRGB(cy, cx, rgb);
                   
               
                
                
                rgb = Binary_i_g.getRGB(cy, cx);
                alpha = AI_mod_g[cy][cx];
                red = 0;
                green = (rgb & 0x0000FF00) >>> 8;
                blue =   0;
                rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                stego_binary_g.setRGB(cy, cx, rgb);


                alpha = 255;
                red = 0;
                green =SDATA_g[cy][cx]+238;
                blue = 0;
                rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;

                Authentication_data_g.setRGB(cy, cx, rgb);
               }
        }

        
         try {
            ImageIO.write(Authentication_data_g, "png", new File(oi.getDirectory() + "Authentication_data_g.png"));
         } catch (IOException e) {}

        try {
            ImageIO.write(stego_Gray_g, "png", new File(oi.getDirectory() + "Stego_Gray_g.png"));
        } catch (IOException e) {
        }
        try {
            ImageIO.write(stego_binary_g, "png", new File(oi.getDirectory() + "Stego_Binary_g.png"));
        } catch (IOException e) {
        }

        try {
            ImageIO.write(Authentication_data_g, "png", new File(oi.getDirectory() + "Authentication_data_g.png"));
        } catch (IOException e) {
        }


        //save values key and g1 g2
        FileWriter fw2_g = new FileWriter(oi.getDirectory() + "KEY_g.txt");
        BufferedWriter bw2_g = new BufferedWriter(fw2_g);

        content = String.valueOf((G_g[0]));
        bw2_g.write(content + "\n");

        content = String.valueOf((G_g[1]));
        bw2_g.write(content + "\n");

        for (int i = 0; i < K.length; i++) {
            content = String.valueOf(K[i]);
            bw2_g.write(content + "\n");
        }

        bw2_g.close();


        
       
        
        
//*********************************************************************************************************************        
        
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //2. for every block of binary and alpha 

        boolean p_b[] = new boolean[6];
        int SDATA_mod_b[][] = new int[LIM_C][LIM_RR];
        int SDATA_b[][] = new int[LIM_C][LIM_RR];
        
         ////System.out.println("\nbefore:");
         blk_no = 0;
        for (int j = 0; j < LIM_C && j % 3 == 0; j = j + 3) {
            for (int i = 0; i < LIM_RR && i % 2 == 0; i = i + 2) {
                //System.out.println("\n\nBlock no="+blk_no++);
                if (BI_b[j][i] == 1) {
                    p_b[0] = true;
                } else {
                    p_b[0] = false;
                }
                if (BI_b[j][i + 1] == 1) {
                    p_b[1] = true;
                } else {
                    p_b[1] = false;
                }
                if (BI_b[j + 1][i] == 1) {
                    p_b[2] = true;
                } else {
                    p_b[2] = false;
                }
                if (BI_b[j + 1][i + 1] == 1) {
                    p_b[3] = true;
                } else {
                    p_b[3] = false;
                }
                if (BI_b[j + 2][i] == 1) {
                    p_b[4] = true;
                } else {
                    p_b[4] = false;
                }
                if (BI_b[j + 2][i + 1] == 1) {
                    p_b[5] = true;
                } else {
                    p_b[5] = false;
                }

                boolean a1_b, a2_b;
                a1_b = (p_b[0] ^ p_b[1]) ^ p_b[2];
                a2_b = (p_b[3] ^ p_b[4]) ^ p_b[5];
                //System.out.println("p[0]="+p[0]+"\tp[1]="+p[1]+"\tp[2]="+p[2]+"\tp[3]="+p[3]+"\tp[4]="+p[4]+"\tp[5]="+p[5]);
                ////System.out.println("s[0]="+a1+"\ts[1]="+a2);
                //System.out.println("a1="+a1+"\ta2="+a2);

                int m1_b[] = new int[4];
                if (a1_b == true) {
                    m1_b[0] = 1;
                } else {
                    m1_b[0] = 0;
                }
                if (a2_b == true) {
                    m1_b[1] = 1;
                } else {
                    m1_b[1] = 0;
                }
                if (p_b[0] == true) {
                    m1_b[2] = 1;
                } else {
                    m1_b[2] = 0;
                }
                if (p_b[1] == true) {
                    m1_b[3] = 1;
                } else {
                    m1_b[3] = 0;
                }

                int m2_b[] = new int[4];
                if (p_b[2] == true) {
                    m2_b[0] = 1;
                } else {
                    m2_b[0] = 0;
                }
                if (p_b[3] == true) {
                    m2_b[1] = 1;
                } else {
                    m2_b[1] = 0;
                }
                if (p_b[4] == true) {
                    m2_b[2] = 1;
                } else {
                    m2_b[2] = 0;
                }
                if (p_b[5] == true) {
                    m2_b[3] = 1;
                } else {
                    m2_b[3] = 0;
                }

                int d_b = 0, c1_b = 0;
                d_b = (m1_b[0] * 2 * 2 * 2 + m1_b[1] * 2 * 2 + m1_b[2] * 2 + m1_b[3] * 1);
                c1_b = (m2_b[0] * 2 * 2 * 2 + m2_b[1] * 2 * 2 + m2_b[2] * 2 + m2_b[3] * 1);
                int x_b[] = new int[6];
                int q_b[] = new int[6];
                int q1_b[] = new int[6];
                int F_b[] = new int[6];
                for (int mmm = 0; mmm < 6; mmm++) {
                    x_b[mmm] = mmm + 1;
                    F_b[mmm] = ((d_b + c1_b * x_b[mmm]) % 17);
                    q_b[mmm] = ((d_b + c1_b * x_b[mmm]) % 17);
                    q1_b[mmm] = (q_b[mmm] + 238);
                    //System.out.println("q["+mmm+"]="+q[mmm] );
                }
                ////System.out.println("Step 7. (Mapping of the partial shares)");
                //System.out.println(("d="+d+"\tc1="+c1));

                ////System.out.println("Step 8. (Embedding two partial shares in the current block)");
                AI_b[j][i] = q1_b[0];
                AI_b[j][i + 1] = q1_b[1];
                AI_b[j + 1][i] = q1_b[2];
                AI_b[j + 1][i + 1] = q1_b[3];
                AI_b[j + 2][i] = q1_b[4];
                AI_b[j + 2][i + 1] = q1_b[5];

                AI_mod_b[j][i] = q1_b[0];
                AI_mod_b[j][i + 1] = q1_b[1];
                AI_mod_b[j + 1][i] = q1_b[2];
                AI_mod_b[j + 1][i + 1] = q1_b[3];
                AI_mod_b[j + 2][i] = q1_b[4];
                AI_mod_b[j + 2][i + 1] = q1_b[5];

                ////System.out.println();
                //for(int iii=0;iii<6;iii++)
                ////System.out.print(" "+q1[iii]+" ");    


                SDATA_mod_b[j][i] = q_b[0];
                SDATA_mod_b[j][i + 1] = q_b[1];
                SDATA_mod_b[j + 1][i] = q_b[2];
                SDATA_mod_b[j + 1][i + 1] = q_b[3];
                SDATA_mod_b[j + 2][i] = q_b[4];
                SDATA_mod_b[j + 2][i + 1] = q_b[5];

                SDATA_b[j][i] = q_b[0];
                SDATA_b[j][i + 1] = q_b[1];
                SDATA_b[j + 1][i] = q_b[2];
                SDATA_b[j + 1][i + 1] = q_b[3];
                SDATA_b[j + 2][i] = q_b[4];
                SDATA_b[j + 2][i + 1] = q_b[5];
            }
        }

        //display randomize result
        //System.out.println("\n\nBefore randomizaton:");        
        for (int j = 0; j < LIM_C && j % 3 == 0; j = j + 3) {
            for (int i = 0; i < LIM_RR && i % 2 == 0; i = i + 2) {
                //System.out.println();
                //System.out.println("("+j+","+i+")"+AI_mod[j][i]+" ("+(j+1)+","+i+")"+AI_mod[j+1][i]+" ("+(j+2)+","+(i)+") "+AI_mod[j+2][i]);  
                //System.out.println("("+(j)+","+(i+1)+")"+AI_mod[j][(i+1)]+" ("+(j+1)+","+(i+1)+")"+AI_mod[(j+1)][i+1]+" ("+(j+2)+","+(i+1)+") "+AI_mod[j+2][(i+1)]);                   
            }
        }
        //randomize data
        // //System.out.println("doing ...");

        for (int j = 0; j < LIM_C; j++) {
            //System.out.println("Randomizing alpha palne ...");
            if (j % 3 != 0) {//get current column 
                //rearrane it as per k

                for (int i = 0; i < LIM_RR; i++) {
                    //System.out.println("*****************************");
                    //System.out.println("\n("+j+","+i+")"+AI_mod[j][i]+"\tK["+i+"]="+K[i]);
                    AI_mod_b[j][i] = AI_b[j][K[i]];
                    //System.out.println("("+j+","+i+")"+AI_mod[j][i]+"\tK["+i+"]="+K[i]);
                    //System.out.println("*****************************");
                    SDATA_mod_b[j][i] = SDATA_b[j][K[i]];
                }
            }
        }





        //System.out.println("\n\nAfter randomizaton:");        
        for (int j = 0; j < LIM_C && j % 3 == 0; j = j + 3) {
            for (int i = 0; i < LIM_RR && i % 2 == 0; i = i + 2) {
                //System.out.println();
                //System.out.println("("+j+","+i+")"+AI_mod[j][i]+" ("+(j+1)+","+i+")"+AI_mod[j+1][i]+" ("+(j+2)+","+(i)+") "+AI_mod[j+2][i]);  
                //System.out.println("("+(j)+","+(i+1)+")"+AI_mod[j][(i+1)]+" ("+(j+1)+","+(i+1)+")"+AI_mod[(j+1)][i+1]+" ("+(j+2)+","+(i+1)+") "+AI_mod[j+2][(i+1)]);                   
            }
        }


        // BufferedImage stego_color=new BufferedImage(gray_i.getWidth(), gray_i.getHeight(),  BufferedImage.TYPE_INT_ARGB);
        BufferedImage stego_Gray_b = new BufferedImage(gray_i_b.getWidth(), gray_i_b.getHeight(), BufferedImage.TYPE_INT_ARGB);
        BufferedImage stego_binary_b = new BufferedImage(gray_i_b.getWidth(), gray_i_b.getHeight(), BufferedImage.TYPE_INT_ARGB);
        //stego_binary=Binary_i;
        BufferedImage Authentication_data_b = new BufferedImage(gray_i_b.getWidth(), gray_i_b.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int cy = 0; cy < LIM_C; cy++) {
            for (int cx = 0; cx < LIM_RR; cx++) {
                  
                rgb = gray_with_alpha_b.getRGB(cy, cx);
                      
                alpha = AI_mod_b[cy][cx];
                red = 0;
                green =0;
                blue =  (rgb & 0x000000FF) >>> 0;
                rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                stego_Gray_b.setRGB(cy, cx, rgb);
                   
               
                
                
                rgb = Binary_i_b.getRGB(cy, cx);
                alpha = AI_mod_b[cy][cx];
                red = 0;
                green =0;
                blue =  (rgb & 0x000000FF) >>> 0;
                rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                stego_binary_b.setRGB(cy, cx, rgb);


                alpha = 255;
                red = 0;
                green =0;
                blue = SDATA_b[cy][cx]+238;
                rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;

                Authentication_data_b.setRGB(cy, cx, rgb);
               }
        }

        
         try {
            ImageIO.write(Authentication_data_b, "png", new File(oi.getDirectory() + "Authentication_data_b.png"));
         } catch (IOException e) {}

        try {
            ImageIO.write(stego_Gray_b, "png", new File(oi.getDirectory() + "Stego_Gray_b.png"));
        } catch (IOException e) {
        }
        try {
            ImageIO.write(stego_binary_b, "png", new File(oi.getDirectory() + "Stego_Binary_b.png"));
        } catch (IOException e) {
        }

        try {
            ImageIO.write(Authentication_data_b, "png", new File(oi.getDirectory() + "Authentication_data_b.png"));
        } catch (IOException e) {
        }


        //save values key and g1 g2
        FileWriter fw2_b = new FileWriter(oi.getDirectory() + "KEY_b.txt");
        BufferedWriter bw2_b = new BufferedWriter(fw2_b);

         content = String.valueOf((G_b[0]));
        bw2_b.write(content + "\n");

        content = String.valueOf((G_b[1]));
        bw2_b.write(content + "\n");

        for (int i = 0; i < K.length; i++) {
            content = String.valueOf(K[i]);
            bw2_b.write(content + "\n");
        }

        bw2_b.close();


        
        
        
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
///save authentication signal in color format
        BufferedImage Authentication_data_color = new BufferedImage(gray_i_r.getWidth(), gray_i_r.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int cy = 0; cy < LIM_C; cy++) {
            for (int cx = 0; cx < LIM_RR; cx++) {
                alpha = 255;
                                
                rgb = Authentication_data_r.getRGB(cy, cx);
                red = (rgb & 0x00FF0000) >>> 16;
                 
         //       rgb = Authentication_data_g.getRGB(cy, cx);
                green = (rgb & 0x0000FF00) >>> 8;
                 
           //     rgb = Authentication_data_b.getRGB(cy, cx);
                blue = (rgb & 0x000000FF) >>> 0;
                
                //red=red-238;
                //green=green-238;
                //blue=blue-238;
                rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                Authentication_data_color.setRGB(cy, cx, rgb);
                
            }
        }
          try {
            ImageIO.write(Authentication_data_color, "png", new File(oi.getDirectory() + "Authentication_data_color.png"));
         } catch (IOException e) {}

        display_images();
        image_oi.flush();

        //System.out.println("End of alg 3"); 
    }
}
