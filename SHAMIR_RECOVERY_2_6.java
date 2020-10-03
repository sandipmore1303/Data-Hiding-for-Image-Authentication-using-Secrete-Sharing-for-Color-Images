/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sam
 */
 

import java.io.IOException;
import java.math.BigInteger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sam
 */
public class SHAMIR_RECOVERY_2_6 {
    int k1;int q1;int k2;int q2;
    double A,B,C,D;
    SHAMIR_RECOVERY_2_6(int k1,int q1,int k2,int q2)
    {
       this.k1=k1;
       this.k2=k2;
       this.q1=q1;
       this.q2=q2;
       this.A=this.q1/(this.k1-this.k2);
       this.B=this.q2/(this.k2-this.k1);
        this.D=-(this.A*this.k2+this.B*this.k1);
        this.C=(this.A+this.B);
    }
  int get_d()
    { 
        shamirKey[] sk=new shamirKey[2];
            
        sk[0]=new shamirKey();
        sk[0].setP(BigInteger.valueOf(17)); 
        sk[0].setX(BigInteger.valueOf(this.k1));    
        sk[0].setF(BigInteger.valueOf(this.q1));  
        
        
        
        sk[1]=new shamirKey();
        sk[1].setP(BigInteger.valueOf(17)); 
        sk[1].setX(BigInteger.valueOf(this.k2));    
        sk[1].setF(BigInteger.valueOf(this.q2)); 
        
        shamir ss=new shamir();
        
        byte[] calculateLagrange = shamir.calculateLagrange(sk);
        //System.out.println(calculateLagrange.length);
        return  calculateLagrange[0];
         
    }
  int get_d_new()
    { 
        shamirKey[] sk=new shamirKey[2];
            
        sk[0]=new shamirKey();
        sk[0].setP(BigInteger.valueOf(17)); 
        sk[0].setX(BigInteger.valueOf(this.k1));    
        sk[0].setF(BigInteger.valueOf(this.q1));  
        
        
        
        sk[1]=new shamirKey();
        sk[1].setP(BigInteger.valueOf(17)); 
        sk[1].setX(BigInteger.valueOf(this.k2));    
        sk[1].setF(BigInteger.valueOf(this.q2)); 
        
        shamir ss=new shamir();
        
        byte[] calculateLagrange = shamir.calculateLagrange(sk);
        //System.out.println(calculateLagrange.length);
        return  (int)Math.ceil(this.D);
         
    }
     
  int get_c1()
    {  
        if(this.C <0)   
        return (int) ((this.C+17)%17);
        else   
        return (int) ((this.C)%17);
        
    }
     public static void main (String args[])   {
         
        //ALGO_2 a2=new ALGO_2(0,15,1,13); 
        SHAMIR_RECOVERY_2_6 a2=new SHAMIR_RECOVERY_2_6(-1,15,1,13); 
       // SHAMIR_RECOVERY_2_6 a2=new SHAMIR_RECOVERY_2_6(0,15,1,13); 
           int _d = a2.get_d();
           int _c1 = a2.get_c1();
           int  s= ((_d<<4)+_c1);
           String ss="123.123";
        
         System.out.println("d="+Math.ceil((double)a2.get_d_new())+"   c1="+a2.get_c1()+" s="+s);//+"strin s="+String.valueOf(s)+"    ");
         System.out.println("d="+a2.get_d()+"   c1="+a2.get_c1()+" s="+s);//+"s
     }
     
    
}
