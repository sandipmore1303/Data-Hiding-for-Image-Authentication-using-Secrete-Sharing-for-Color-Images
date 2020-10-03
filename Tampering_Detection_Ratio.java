/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sam
 */
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
public class Tampering_Detection_Ratio {
    public static void main(String args[]) throws IOException {
        ImagePlus original_image = null;
        BufferedImage image_si = null;
        BufferedImage image_mi = null;
        File f_oi;
        ij.io.OpenDialog si = new ij.io.OpenDialog("Select stego image", "E:\\DATA\\YES_MAN\\JAVA_IMPL\\", "lenna.bmp");
        try {
            f_oi = new File(si.getDirectory() + si.getFileName());
            image_si = ImageIO.read(f_oi);
        } catch (IOException e) {
        }
        try {
            //ImageIO.write(image_oi, "png", new File(oi.getDirectory() + "Original_Gray.png"));
        } catch (Exception e) {
        }
        ij.io.OpenDialog mi = new ij.io.OpenDialog("Select tampered stego image", "E:\\DATA\\YES_MAN\\JAVA_IMPL\\", "lenna.bmp");
        try {
            f_oi = new File(mi.getDirectory() + mi.getFileName());
            image_mi = ImageIO.read(f_oi);
        } catch (IOException e) {
        }
        try {
            //ImageIO.write(image_oi, "png", new File(oi.getDirectory() + "Original_Gray.png"));
        } catch (Exception e) {
        }

        int lim_r, lim_c;
        lim_c = image_si.getWidth() - (image_si.getWidth() % 3);//columns
        lim_r = image_si.getHeight() - (image_si.getHeight() % 2);//rows

        int tampered_blocks=0;
        int R_s[]=new int[6];
        int G_s[]=new int[6];
        int B_s[]=new int[6];
        
        int R_m[]=new int[6];
        int G_m[]=new int[6];
        int B_m[]=new int[6];
        
         int alpha = 0;
        int red = 0;
        int green = 0;
        int blue = 0;
        int rgb = 0;
        int blk_no = 0;
        
        boolean tampered_block[][]=new boolean[lim_c][lim_r];
         
        
         blk_no = 0;
        for (int j = 0; j < lim_c && j % 3 == 0; j = j + 3) {
            for (int i = 0; i < lim_r && i % 2 == 0; i = i + 2) {
                //System.out.println();
                blk_no++;
                rgb=image_si.getRGB(j, i);
                R_s[0]=(rgb & 0x00FF0000)  >>> 16;           
                G_s[0]=(rgb & 0x0000FF00)  >>> 8;
                B_s[0]=(rgb & 0x000000FF)  >>> 0; 
                rgb=image_si.getRGB(j, i+1);
                R_s[1]=(rgb & 0x00FF0000)  >>> 16;           
                G_s[1]=(rgb & 0x0000FF00)  >>> 8;
                B_s[1]=(rgb & 0x000000FF)  >>> 0; 
                rgb=image_si.getRGB(j+1, i);
                R_s[2]=(rgb & 0x00FF0000)  >>> 16;           
                G_s[2]=(rgb & 0x0000FF00)  >>> 8;
                B_s[2]=(rgb & 0x000000FF)  >>> 0;  
                rgb=image_si.getRGB(j+1, i+1);
                R_s[3]=(rgb & 0x00FF0000)  >>> 16;           
                G_s[3]=(rgb & 0x0000FF00)  >>> 8;
                B_s[3]=(rgb & 0x000000FF)  >>> 0; 
                rgb=image_si.getRGB(j+2, i);
                R_s[4]=(rgb & 0x00FF0000)  >>> 16;           
                G_s[4]=(rgb & 0x0000FF00)  >>> 8;
                B_s[4]=(rgb & 0x000000FF)  >>> 0; 
                rgb=image_si.getRGB(j+2, i+1);
                R_s[5]=(rgb & 0x00FF0000)  >>> 16;           
                G_s[5]=(rgb & 0x0000FF00)  >>> 8;
                B_s[5]=(rgb & 0x000000FF)  >>> 0; 
                
                rgb=image_mi.getRGB(j, i);
                R_m[0]=(rgb & 0x00FF0000)  >>> 16;           
                G_m[0]=(rgb & 0x0000FF00)  >>> 8;
                B_m[0]=(rgb & 0x000000FF)  >>> 0; 
                rgb=image_mi.getRGB(j, i+1);
                R_m[1]=(rgb & 0x00FF0000)  >>> 16;           
                G_m[1]=(rgb & 0x0000FF00)  >>> 8;
                B_m[1]=(rgb & 0x000000FF)  >>> 0; 
                rgb=image_mi.getRGB(j+1, i);
                R_m[2]=(rgb & 0x00FF0000)  >>> 16;           
                G_m[2]=(rgb & 0x0000FF00)  >>> 8;
                B_m[2]=(rgb & 0x000000FF)  >>> 0;  
                rgb=image_mi.getRGB(j+1, i+1);
                R_m[3]=(rgb & 0x00FF0000)  >>> 16;           
                G_m[3]=(rgb & 0x0000FF00)  >>> 8;
                B_m[3]=(rgb & 0x000000FF)  >>> 0; 
                rgb=image_mi.getRGB(j+2, i);
                R_m[4]=(rgb & 0x00FF0000)  >>> 16;           
                G_m[4]=(rgb & 0x0000FF00)  >>> 8;
                B_m[4]=(rgb & 0x000000FF)  >>> 0; 
                rgb=image_mi.getRGB(j+2, i+1);
                R_m[5]=(rgb & 0x00FF0000)  >>> 16;           
                G_m[5]=(rgb & 0x0000FF00)  >>> 8;
                B_m[5]=(rgb & 0x000000FF)  >>> 0; 
                
                boolean flag=false;
                for(int pk=0;pk<6;pk++){
                    if(R_s[pk] != R_m[pk] || G_s[pk] != G_m[pk] || B_s[pk] != B_m[pk] ){
                    flag=true;break;
                    }
                }
                if(flag){tampered_blocks++;tampered_block[j][i]=true;}
            }
        }

       

        //ImageIO.write(gray_i, "png", new File(oi.getDirectory() +Integer.toString(k)+".png" )); 
        System.out.println("\tTotal blocks="+blk_no+"\tTampered Blocks="+tampered_blocks);
    }
}

    
