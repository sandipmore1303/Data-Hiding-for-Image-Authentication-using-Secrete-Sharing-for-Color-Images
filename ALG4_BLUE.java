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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class ALG4_BLUE {
     public static ij.io.OpenDialog oi;
     
     public static double[] G_b=new double[2];
     public static double thres_b;
     
     
    
     public static int P_1D[];
     public static double[] Moments(int [] data ) {
		 
		double total =0;
		double m0=1.0, m1=0.0, m2 =0.0, m3 =0.0, sum =0.0, p0=0.0;
		double cd, c0, c1, z0, z1;	/* auxiliary variables */
		int threshold = -1;

		double [] histo = new  double [data.length];

		for (int i=0; i<data.length; i++)
			total+=data[i];

		for (int i=0; i<data.length; i++)
			histo[i]=(double)(data[i]/total); //normalised histogram

		/* Calculate the first, second, and third order moments */
		for ( int i = 0; i < data.length; i++ ){
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
		c0 = ( -m2 * m2 + m1 * m3 ) / cd;
		c1 = ( m0 * -m3 + m2 * m1 ) / cd;
		z0 = 0.5 * ( -c1 - Math.sqrt ( c1 * c1 - 4.0 * c0 ) );
		z1 = 0.5 * ( -c1 + Math.sqrt ( c1 * c1 - 4.0 * c0 ) );
		p0 = ( z1 - m1 ) / ( z1 - z0 );  /* Fraction of the object pixels in the target binary image */

		// The threshold is the gray-level closest  
		// to the p0-tile of the normalized histogram 
		sum=0;
		for (int i=0; i<data.length; i++){
			sum+=histo[i];
			if (sum>p0) {
				threshold = i;
				break;
			}
		}
                double G[]=new double[2];
                G[0]=z0;
                G[1]=z1;
		return G;
	}
                                                                                                                                                                                    public static String nn_b="Stego_Binary_b.png";public static BufferedImage image_oi_b=null; public static File f_oi_b;
                                                                                                                                                                                    
                                                                                                                                                                                    
  
     
     private static void display_images() {
    
    
    //stego images
    
 
    //JFrame frame_os = new JFrame("Original Stego image");
    //Panel panel_os = new ShowImage(oi.getDirectory()+"Stego_Binary.png");
    //frame_os.getContentPane().add(panel_os);
    //frame_os.setSize(500, 500);
    //frame_os.setVisible(true);
    
    
    JFrame frame_bs = new JFrame("Modified Stego image ");
    Panel panel_bs = new ShowImage(oi.getDirectory()+oi.getFileName());
    frame_bs.getContentPane().add(panel_bs);
    frame_bs.setSize(500, 500);
    frame_bs.setVisible(true);
    
   
       
       }
    
 
  
public  static void main(String args[])  {      
    //1 read stego color image  
   ImagePlus original_image = null ;
   BufferedImage image_oi=null;
      
   
   File f_oi;
   oi =new ij.io.OpenDialog("Select modified stego image",
                  "E:\\DATA\\YES_MAN\\JAVA_IMPL\\",
                  "lenna.bmp");
   try {
	    f_oi=new File(oi.getDirectory()+oi.getFileName());
            image_oi = ImageIO.read(f_oi);                                                                                                                                                              
            f_oi_b=new File(oi.getDirectory()+nn_b);image_oi_b= ImageIO.read(f_oi_b);  
   	        } catch (IOException e) {}      
    //2 convert to grayscale      

      
     BufferedImage gray_i_b =new BufferedImage(image_oi.getWidth(),image_oi.getHeight(),  BufferedImage.TYPE_INT_ARGB);
     BufferedImage gray_becovered_b=new BufferedImage(image_oi.getWidth(),image_oi.getHeight(),  BufferedImage.TYPE_INT_ARGB);
     BufferedImage gray_tampered_b =new BufferedImage(image_oi.getWidth(),image_oi.getHeight(),  BufferedImage.TYPE_INT_ARGB);
      
     //3. convert to binary
     //System.out.println("\nObtaining values g1 and g2 ");
     //******************************************************************************
     String sCurrentLine_b = null; 
     String sp_b[]=new String[2];
     BufferedReader br_b = null;
         try {  
             br_b = new BufferedReader(new FileReader(oi.getDirectory()+"//KEY_b.txt"));
         } catch (FileNotFoundException ex) {
             Logger.getLogger(ALGO_41.class.getName()).log(Level.SEVERE, null, ex);
         }
         try {
             sCurrentLine_b = br_b.readLine();
         } catch (IOException ex) {
             Logger.getLogger(ALGO_41.class.getName()).log(Level.SEVERE, null, ex);
         }
     sp_b[0]=sCurrentLine_b.substring(0,sCurrentLine_b.indexOf("."));
     sp_b[1]=sCurrentLine_b.substring(sCurrentLine_b.indexOf(".")+1);
     int parseInt1 = Integer.parseInt(sp_b[0]);
     int parseInt2 = Integer.parseInt(sp_b[1].substring(0,1));
     G_b[0]=(parseInt1+parseInt2/(100000.0));      
         try {  
             sCurrentLine_b = br_b.readLine();
         } catch (IOException ex) {
             Logger.getLogger(ALGO_41.class.getName()).log(Level.SEVERE, null, ex);
         }
     sp_b[0]=sCurrentLine_b.substring(0,sCurrentLine_b.indexOf("."));
     sp_b[1]=sCurrentLine_b.substring(sCurrentLine_b.indexOf(".")+1);     
     parseInt1 = Integer.parseInt(sp_b[0]);
     parseInt2 = Integer.parseInt(sp_b[1].substring(0,1));
     G_b[1]=parseInt1+parseInt2/(100000.0);
     //System.out.println("Value of gray level g1="+G[0]);
     //System.out.println("Value of gray level g2="+G[1]);
     thres_b=(G_b[0]+G_b[1])/2.0;
     //System.out.println("Value of threshod for binarization="+thres);      
     //System.out.println("\nMoment Preserving thresholding");
     int data_b[]=new int[image_oi.getWidth()*image_oi.getHeight()];
      
     int Alpha_Image_original_b[][]=new int[image_oi.getWidth()][image_oi.getHeight()];
     int Binary_Image_original_b[][]=new int[image_oi.getWidth()][image_oi.getHeight()];
     
     BufferedImage Binary_i_b = new BufferedImage(image_oi.getWidth(),image_oi.getHeight(),  BufferedImage.TYPE_INT_ARGB);
     int  LIM_RR,  LIM_CC;
     LIM_CC=image_oi.getWidth()-(image_oi.getWidth()%3) ;//columns
     LIM_RR=image_oi.getHeight()-(image_oi.getHeight()%2) ;//rows
    
     int alpha=0;
     int red=0;
     int green=0;
     int blue=0;
     int rgb=0;
     int k=0;
        
        for (int cx=0;cx<image_oi.getWidth();cx++) {          
            for (int cy=0;cy<image_oi.getHeight();cy++) {
                 
               rgb=image_oi .getRGB(cx, cy);   
               alpha=255;
               red=(rgb & 0x00FF0000)  >>> 16;           
               green=(rgb & 0x0000FF00)  >>> 8;
               blue=(rgb & 0x000000FF)  >>> 0;
               rgb=(alpha << 24) | (red << 16) | (green << 8) | blue;
               data_b[k++]=(blue);
                gray_becovered_b.setRGB(cx, cy, 0);
            }
        } 
        k=0;
        for (int cx=0;cx<image_oi.getWidth();cx++) {  
           for (int cy=0;cy<image_oi.getHeight();cy++) { 
             if(data_b[k]<thres_b)
             {Binary_Image_original_b[cx][cy]=0;}
             else
                 Binary_Image_original_b[cx][cy]=1;  
             k++;
              int rgb2=image_oi_b.getRGB(cx, cy); 
             alpha = ((rgb2 >> 24) & 0x000000ff);         
             red=Binary_Image_original_b[cx][cy]*255;
             green=Binary_Image_original_b[cx][cy]*255;
             blue=Binary_Image_original_b[cx][cy]*255;
             rgb2=(alpha << 24) | (red << 16) | (green << 8) | blue;
             Binary_i_b.setRGB(cx, cy, rgb2); 
             
             //gray_becovered.setRGB(cx, cy, rgb2);
             
        }
       } 
          
         
 ///*******************************************************************       
        
     //find key K and inverse key
     
     int K[]= new int[LIM_RR];
     int K_i[]= new int[LIM_RR];
     for(int ii=0;ii<LIM_RR;ii++)
     {
       try {
           K[ii]=Integer.parseInt( br_b.readLine());
       } catch (IOException ex) {
           Logger.getLogger(ALGO_41.class.getName()).log(Level.SEVERE, null, ex);
       }
     }
     int index=-1;
     for(int ii=0;ii<LIM_RR;ii++)
     {k=0;
     index=-1;
     while(true)
     {if(K[k]==ii)   {index=k;break;}      else k++; } 
     K_i[ii]=index;
     }
     //System.out.println("Key length="+K.length+"\nKey:");
     for(int ii=0;ii<LIM_RR;ii++)
     {//System.out.print(K[ii]+" ");
     }
     //System.out.println("\nInverse Key length="+K.length+"\nInverse Key:");
     for(int ii=0;ii<LIM_RR;ii++)
     {//System.out.print(K_i[ii]+" ");
     }
        
     
     
  //**********************************************************************   
     //3. obtain alpha component  and binary components into 2d array  
     int BI_b[][]=new int[LIM_CC][LIM_RR];
     int BI_mod_b[][]=new int [LIM_CC][LIM_RR];
     int AI_b[][]=new int[LIM_CC][LIM_RR];
     int AI_mod_b[][]=new int[LIM_CC][LIM_RR];
     
      //System.out.println("");
     for (int cx=0;cx<LIM_CC;cx++) {  
         for (int cy=0;cy<LIM_RR;cy++) { 
              rgb=image_oi_b.getRGB(cx, cy); 
             alpha = ((rgb >> 24) & 0x000000ff);
              ////System.out.println(alpha);
             AI_mod_b[cx][cy]=alpha;
             AI_b[cx][cy]=alpha;
             BI_b[cx][cy]=Binary_Image_original_b[cx][cy];
             //System.out.println("BI="+BI[cx][cy]+"\tcol="+cx+"\trow="+cy+"\talpha="+alpha);     
         }
     }
 
       
          //display  result
       //System.out.println("\nBefore de randomizaton:");
       for ( int j=0; j<LIM_CC  && j%3==0 ;j=j+3) 
               for(int i=0; i<LIM_RR  && i%2==0;i=i+2) {
             
               //System.out.println();
             //System.out.println("("+j+","+i+")"+AI[j][i]+" ("+(j+1)+","+i+")"+AI[j+1][i]+" ("+(j+2)+","+(i)+") "+AI[j+2][i]);  
             //System.out.println("("+(j)+","+(i+1)+")"+AI[j][(i+1)]+" ("+(j+1)+","+(i+1)+")"+AI[(j+1)][i+1]+" ("+(j+2)+","+(i+1)+") "+AI[j+2][(i+1)]);                   
           }  
        
        //get original data ie de-randomized 
     //randomize data
           
    for (int j=0; j<LIM_CC;j++){
           if(j%3 !=0)
           {//get current column 
             //rearrane it as per k
             for(int i=0; i<LIM_RR ;i++) {
                AI_b[j][i]=AI_mod_b[j][K_i[i]]; 
             }
           }
       }  
         //System.out.println("After de randomizaton:");   
          for ( int j=0; j<LIM_CC  && j%3==0 ;j=j+3) 
               for(int i=0; i<LIM_RR  && i%2==0;i=i+2) {
            
               //System.out.println();
             //System.out.println("("+j+","+i+")"+AI[j][i]+" ("+(j+1)+","+i+")"+AI[j+1][i]+" ("+(j+2)+","+(i)+") "+AI[j+2][i]);  
             //System.out.println("("+(j)+","+(i+1)+")"+AI[j][(i+1)]+" ("+(j+1)+","+(i+1)+")"+AI[(j+1)][i+1]+" ("+(j+2)+","+(i+1)+") "+AI[j+2][(i+1)]);                   
           }  
        
     //4. from each block of alpha and binary 
     //4.1find p1 to p6 from binary
     //4.2 find q1 to q6 from alpha use key K
     //4.3 from alpha find s=a1a2 
         //4.3.1 substarct 238 from each q1 to q6
         //4.3.2 extract d and c1 using shamir recovery with [1,q1][2,q2]
         //4.3.3 covert d to 4 bit bianry value and c1 into 4 bit binary value and combine them to 8 bit value.
               //take first two bits of it as s=a1a2
     //4.4 from binary plane find two bit authentication signal s'=a1'a2'with a1'=p1 xor p2 xor p3 and a2'=p4 xor p5 xor p6
     //4.5 match s1 and s' if mismatch occurs mark currnt binary block and color image blok and all the six shares p1 to p6 as tampered

     
 boolean p_b[]=new boolean[6];
 int q1_b[]=new int[6];
 int q_b[]=new int[6];
 boolean tampered_blocks_b[][]=new boolean[LIM_CC][LIM_RR];
 boolean tampered_shares_b[][]=new boolean[LIM_CC][LIM_RR];
 int blk_no=0;
 for (int j=0; j<LIM_CC  && j%3==0 ;j=j+3)
     for(int i=0; i<LIM_RR  && i%2==0;i=i+2) 
           { 
  
      //System.out.println("Block no="+blk_no++);
      //4.1find p1 to p6 from binary
      if( BI_b[j][i]==1)        p_b[0]=true;        else    p_b[0]=false;  
      if( BI_b[j][i+1]==1)      p_b[1]=true;        else    p_b[1]=false;  
      if( BI_b[j+1][i]==1)      p_b[2]=true;        else    p_b[2]=false;  
      if( BI_b[j+1][i+1]==1)    p_b[3]=true;        else    p_b[3]=false;  
      if( BI_b[j+2][i]==1)      p_b[4]=true;        else    p_b[4]=false;  
      if( BI_b[j+2][i+1]==1)    p_b[5]=true;        else    p_b[5]=false;  
      
      //4.2 find q1' to q6' from alpha use key K
      q1_b[0]=AI_b[j][i];
      q1_b[1]=AI_b[j][i+1];
      q1_b[2]=AI_b[j+1][i];
      q1_b[3]=AI_b[j+1][i+1];
      q1_b[4]=AI_b[j+2][i];
      q1_b[5]=AI_b[j+2][i+1];
      
      //4.3 from alpha find s=a1a2 
         //4.3.1 substarct 238 from each q1 to q6
      for(int ii=0;ii<6;ii++)
      {q_b[ii]=q1_b[ii]-238;  
       //System.out.println("q["+ii+"]="+q[ii] );
      }  
         //4.3.2 extract d and c1 using shamir recovery with [1,q1][2,q2]
           ALGO_2 a2_b=new ALGO_2(1,q_b[0],2,q_b[1]);
           int _d_b = a2_b.get_d();
           int _c1_b = a2_b.get_c1();
           int d_c1_b= ((_d_b<<4)+_c1_b);
       //4.3.3 covert d to 4 bit bianry value and c1 into 4 bit binary value and combine them to 8 bit value.
       //take first two bits of it as s=a1a2
           toBinary tb_b=new toBinary(d_c1_b);
           //System.out.println("p[0]="+p[0]+"p[1]="+p[1]+"p[2]="+p[2]+"p[3]="+p[3]+"p[4]="+p[4]+"p[5]="+p[5]);
           //System.out.println("d_c1="+d_c1+"\t_d="+_d+"\t_c1="+_c1+"\ttb.len="+tb.size);
           int tb1_b[]=new int[8];
           k=0;
           for(int kk=8-tb_b.size;kk<8;kk++){
             tb1_b[kk]=tb_b.B[k++];  
          }
           
           boolean s_b[]=new boolean[2];
           if (tb1_b[0]==1)s_b[0]=true;         else     s_b[0]=false;
           if (tb1_b[1]==1)s_b[1]=true;         else     s_b[1]=false;         
            
           ////System.out.println();
           
           //4.4 from binary plane find two bit authentication signal s'=a1'a2'with a1'=p1 xor p2 xor p3 and a2'=p4 xor p5 xor p6
           boolean s__b[]=new boolean[2];
           s__b[0]=(p_b[0]  ^  p_b[1]) ^ p_b[2];               
           s__b[1]=(p_b[3]  ^  p_b[4]) ^ p_b[5];
     //4.5 match s1 and s' if mismatch occurs mark currnt binary block and color image blok and all the six shares p1 to p6 as tampered
           //System.out.println("s[0]="+s[0]+"\ts[1]="+s[1]+"\ts_[0]="+s_[0]+"\ts_[1]="+s_[1]);
           if((s_b[0] !=s__b[0]) || (s_b[1] != s__b[1]))
           {
               //mark current blocks as tampered and p1 to p6 as tampered
               //System.out.println("tampered");
               tampered_blocks_b[j][i]   =true;               
               tampered_shares_b[j][i]   =true;
               //tampered_shares[i+1][j] =true;  
               rgb=0;
              alpha=255;
              red=(rgb & 0x00FF0000)  >>> 16;           
              green=(rgb & 0x0000FF00)  >>> 8;
              blue=(rgb & 0x000000FF)  >>> 0;
              rgb=(alpha << 24) | (red << 16) | (green << 8) | blue;
                 // q1[0]=AI[i][j];
               gray_tampered_b.setRGB(j, i, rgb);
                  //q1[1]=AI[i+1][j];
                gray_tampered_b.setRGB(j, i+1, rgb);
                 // q1[2]=AI[i][j+1];      
                 gray_tampered_b.setRGB(j+1, i, rgb);
                  //q1[3]=AI[i+1][j+1];
                  gray_tampered_b.setRGB(j+1, i+1, rgb);
                  //q1[4]=AI[i][j+2];
                   gray_tampered_b.setRGB(j+2, i, rgb);
                  //q1[5]=AI[i+1][j+2];
                    gray_tampered_b.setRGB(j+2, i+1, rgb);
             
           }
           else
           {   //System.out.println("not tampered");
               tampered_blocks_b[j] [i]    =false;               
               tampered_shares_b[j][i]     =false;
               //tampered_shares[i+1][j]   =false;          
           }      
  } 
              
  //5. self-repairing of the original image content from binary and apha: if block is marked as tampered 
 int rep=0,nrep=0,ntampblk=0,noofuntamp=0;    
   blk_no=0;
 for (int j=0; j<LIM_CC && j%3==0 ;j=j+3)
 { 
   for(int i=0; i<LIM_RR && i%2==0;i=i+2)
   { 
       //System.out.println("Block no="+blk_no++);
      if(tampered_blocks_b[j][i]==true)//block is marked as tampered
      {ntampblk++;
       rep++;
      
      //4.2 find q1' to q6' from alpha use key K
      q1_b[0]=AI_b[j][i];
      q1_b[1]=AI_b[j][i+1];
      q1_b[2]=AI_b[j+1][i];
      q1_b[3]=AI_b[j+1][i+1];
      q1_b[4]=AI_b[j+2][i];
      q1_b[5]=AI_b[j+2][i+1];
      
      //4.3 from alpha find s=a1a2 
         //4.3.1 substarct 238 from each q1 to q6
      for(int ii=0;ii<6;ii++)
      {q_b[ii]=q1_b[ii]-238;  
       //System.out.println("q["+ii+"]="+q[ii] );
      }  
         //4.3.2 extract d and c1 using shamir recovery with [1,q1][2,q2]
           ALGO_2 a2_b=new ALGO_2(1,q_b[0],2,q_b[1]);
           int _d_b = a2_b.get_d();
           int _c1_b = a2_b.get_c1();
           int d_c1_b= ((_d_b<<4)+_c1_b);
       //4.3.3 covert d to 4 bit bianry value and c1 into 4 bit binary value and combine them to 8 bit value.
       //take first two bits of it as s=a1a2
           toBinary tb_b=new toBinary(d_c1_b);
           ////System.out.println("p[0]="+tb.B[0]+"p[1]="+tb.B[1]+"p[2]="+tb.B[2]+"p[3]="+tb.B[3]+"p[4]="+tb.B[4]+"p[5]="+tb.B[5]);
           //System.out.println("d_c1="+d_c1+"\t_d="+_d+"\t_c1="+_c1+"\ttb.len="+tb.size);
                      
              int tb1_b[]=new int[8];
           k=0;
           for(int kk=8-tb_b.size;kk<8;kk++){
             tb1_b[kk]=tb_b.B[k++]; 
             ////System.out.println(tb1[kk]);
          }
               
              int bi_b[]=new int[6];
               
               int ll=0;
              for(int mn=2;mn<=7;mn++){
                   bi_b[ll]=tb1_b[mn];
                   //System.out.println(bi[ll]);
                   ll++;
              }
              //if bi'=0 set yi'=g1 else set y1'=g2 where yi' is a corresponding gray level block's ith pixel and i=1,2,3,4,5,6
              //mark blok as repaired 
              //gray_becovered
               /*    
              */
              if(bi_b[0]==0)
              {
                  //rgb=gray_becovered.getRGB(j, i);
                  //alpha=(rgb& 0xFF000000)  >>> 24;  
                  alpha=AI_b[j][i];
                  red=(int) G_b[0];        
                  green=(int) G_b[0];;
                  blue=(int) G_b[0];;
                  rgb=(alpha << 24) | (red << 16) | (green << 8) | blue;
                   
                  //System.out.println("\nRECOVERED RGB"+rgb);
                  gray_becovered_b.setRGB(j, i, rgb);
                  
              }
              else
              {    
                  alpha=AI_b[j][i];
                  red=(int) G_b[1];        
                  green=(int) G_b[1];
                  blue=(int) G_b[1];
                  rgb=(alpha << 24) | (red << 16) | (green << 8) | blue;
                   
                   
                  //System.out.println("\nRECOVERED RGB"+rgb);
                 gray_becovered_b.setRGB(j, i, rgb);
                  
                  
              }  
             if(bi_b[1]==0)
              {
                  //rgb=gray_becovered.getRGB(j, i);
                  //alpha=(rgb & 0xFF000000)  >>> 24;  
                  alpha=AI_b[j][i+1];
                  red=(int) G_b[0];        
                  green=(int) G_b[0];
                  blue=(int) G_b[0];
                  rgb=(alpha << 24) | (red << 16) | (green << 8) | blue;
                  gray_becovered_b.setRGB(j,i+1, rgb);
              }
              else
              {   //rgb=gray_becovered.getRGB(j, i);
                  //alpha=(rgb & 0xFF000000)  >>> 24;  
                  alpha=AI_b[j][i+1];
                  red=(int) G_b[1];        
                  green=(int) G_b[1];
                  blue=(int) G_b[1];
                  rgb=(alpha << 24) | (red << 16) | (green << 8) | blue;
                  gray_becovered_b.setRGB( j,i+1, rgb);
                  
              }                  
                
              if(bi_b[2]==0)
              {
                  
                   alpha=AI_b[j+1][i];
                 red=(int) G_b[0];        
                  green=(int) G_b[0];
                  blue=(int) G_b[0];
                   rgb=(alpha << 24) | (red << 16) | (green << 8) | blue;
                  gray_becovered_b.setRGB(j+1,i, rgb);
              }
              else
              {     
                  alpha=AI_b[j+1][i];
                  red=(int) G_b[1];        
                  green=(int) G_b[1];
                  blue=(int) G_b[1];
                   rgb=(alpha << 24) | (red << 16) | (green << 8) | blue;
                 gray_becovered_b.setRGB(j+1,i, rgb);
                  
              }   
               
              if(bi_b[3]==0)
              {   
                  alpha=AI_b[j+1][i+1];
                  red=(int) G_b[0];        
                  green=(int) G_b[0];
                  blue=(int) G_b[0];
                  rgb=(alpha << 24) | (red << 16) | (green << 8) | blue;
                  gray_becovered_b.setRGB(j+1,i+1, rgb);
              }
              else
              {    
                  alpha=AI_b[j+1][i+1];
                 red=(int) G_b[1];        
                  green=(int) G_b[1];
                  blue=(int) G_b[1];
                   rgb=(alpha << 24) | (red << 16) | (green << 8) | blue;
                  gray_becovered_b.setRGB(j+1,i+1, rgb);                  
              }  
               //q1[4]=AI[i][j+2];
              if(bi_b[4]==0)
              {
                   
                   alpha=AI_b[j+2][i];
                 red=(int) G_b[0];        
                  green=(int) G_b[0];
                  blue=(int) G_b[0];
                   rgb=(alpha << 24) | (red << 16) | (green << 8) | blue;
                  gray_becovered_b.setRGB(j+2,i, rgb);
              }
              else
              {    
                  alpha=AI_b[j+2][i];
                 red=(int) G_b[1];        
                  green=(int) G_b[1];
                  blue=(int) G_b[1];
                   rgb=(alpha << 24) | (red << 16) | (green << 8) | blue;
                  gray_becovered_b.setRGB(j+2,i, rgb);
                  
              }  
              //q1[5]=AI[i+1][j+2]
              if(bi_b[5]==0)
              {
                   
                  alpha=AI_b[j+2][i+1];
                  red=(int) G_b[0];        
                  green=(int)G_b[0];
                  blue=(int) G_b[0];
                  rgb=(alpha << 24) | (red << 16) | (green << 8) | blue;
                  gray_becovered_b.setRGB(j+2,i+1, rgb);
              }
              else
              {    
                  alpha=AI_b[j+2][i+1];
                 red=(int) G_b[1];        
                  green=(int) G_b[1];
                  blue=(int) G_b[1];
                   rgb=(alpha << 24) | (red << 16) | (green << 8) | blue;
                  gray_becovered_b.setRGB( j+2,i+1,rgb);                  
              }  
          }
           
         }
   
  }
 
 
   
  //display_images();
       
  //save modified PNG image color
      try {     
          ImageIO.write(gray_becovered_b, "png", new File(oi.getDirectory()+"gray_becovered_b.png"));
      }catch (IOException e) {} 
      
        try {     
          ImageIO.write(gray_tampered_b, "png", new File(oi.getDirectory()+"gray_tampered_b.png"));
      }catch (IOException e) {} 
       

  //System.out.println("Total blocks="+blk_no);
  //System.out.println("Tampered bloks="+ntampblk+"UnTampered bloks="+(blk_no-ntampblk)+"recoverable blocks="+rep+"Nonrecoverable blocks="+(ntampblk-rep) );
  //System.out.println("recovered blocks="+rep);
//System.out.println("END OF algo 4");
         
        //combine all gray recovered to form color image
BufferedImage color_becovered_image=new BufferedImage(image_oi.getWidth(),image_oi.getHeight(),  BufferedImage.TYPE_INT_ARGB);
 alpha=0;
     red=0;
     green=0;
     blue=0;
     rgb=0;
     k=0;
        
        for (int cx=0;cx<image_oi.getWidth();cx++) {          
            for (int cy=0;cy<image_oi.getHeight();cy++) {
                 
               rgb=gray_becovered_b.getRGB(cx, cy);   
               alpha=255;
               red=(rgb & 0x00FF0000)  >>> 16;           
                
 //              rgb=gray_becovered_b.getRGB(cx, cy);   
               alpha=255;
               green=(rgb & 0x0000FF00)  >>> 8;
                
               
   //            rgb=gray_becovered_b.getRGB(cx, cy);   
               alpha=255;
               blue=(rgb & 0x000000FF)  >>> 0;
              
               
               rgb=(alpha << 24) | (red << 16) | (green << 8) | blue;
               color_becovered_image.setRGB(cx, cy, rgb);
            }
        } 
     
  //display_images();
       
       
 try {     
          ImageIO.write(color_becovered_image, "png", new File(oi.getDirectory()+"color_becovered_image.png"));
      }catch (IOException e) {} 
       
  //System.out.println("Total blocks="+blk_no);
  //System.out.println("Tampered bloks="+ntampblk+"UnTampered bloks="+(blk_no-ntampblk)+"recoverable blocks="+rep+"Nonrecoverable blocks="+(ntampblk-rep) );
  //System.out.println("recovered blocks="+rep);
//System.out.println("END OF algo 4");
        
             
        
/////////////////////////////////////////////////////////////////        
        
        
  display_images();
  image_oi.flush();

}
 

}
