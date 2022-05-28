package otsu_;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Otsu_ {

    public static void main(String[] args) throws IOException {

        String path="exp1.png";

        File file= new File(path);
        BufferedImage image = ImageIO.read(file);

        int height = image.getHeight();
        int width = image.getWidth();

        Pixel pixels [][] = new Pixel[width][height];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                int  color =  image.getRGB(j,i);
                int  red   = (color & 0x00ff0000) >> 16;
                int  green = (color & 0x0000ff00) >> 8;
                int  blue  =  color & 0x000000ff;
                pixels[j][i] = new Pixel(red,green,blue,(red+green+blue));
                Histogram.hisred[red]++;
                Histogram.hisgreen[green]++;
                Histogram.hisblue[blue]++;
                Histogram.histotal[(red+green+blue)]++;
            }
        }
        
        int total_pixels_num = width*height;
        int thresh_red   = thresh(Histogram.hisred, total_pixels_num);
        int thresh_green = thresh(Histogram.hisgreen, total_pixels_num);
        int thresh_blue  = thresh(Histogram.hisblue, total_pixels_num);
        
        for (int i = 0; i < height; i++) {
            
            for (int j = 0; j < width; j++) {
                
                if(pixels[j][i].red<=thresh_red &&
                        pixels[j][i].green<=thresh_green &&
                        pixels[j][i].blue<=thresh_blue)
                {
                    System.out.print(".");
                } 
                else{ 
                    System.out.print(" "); 
                } 
            }
            System.out.println("");
        }
        


    }
    
    
    public static int thresh(int[]data,int total_pixels_num){
        
        double wf=0;    double wb=0;
        double meanf=0; double meanb=0;
        double variancef=0; double varianceb=0;
        int thresh=999;
        int lastThresh=0;
        double maxVariance=Double.MIN_VALUE;
        
        for (int i = 0; i < 256; i++) {
            
            wf=0; double sumf=0; double pixf=0; meanf=0;
            for(int j=0;j<i;j++){
                 sumf+=data[j]*j;
                 pixf+=data[j];
            }
            meanf=sumf/pixf;
            wf=(double)pixf/total_pixels_num;
            variancef=0;
            for(int j=0;j<i;j++){
              
                variancef+= (data[j]*(Math.pow((j-meanf), 2)))/pixf; 
                thresh=j;
            }
            
            
            int k=256-i;
            wb=0; double sumb=0; double pixb=0; meanb=0;
            for(int j=i;j<256;j++){
                
                 sumb+=data[j]*j;
                 pixb+=data[j];
            }
            meanb=sumb/pixb;
            wb=(double)pixb/total_pixels_num;
            
             
            //variancef hesabı
            varianceb=0; 
            for(int j=i;j<256;j++){   
                varianceb+= (data[j]*(Math.pow((j-meanb), 2)))/pixb; 
            }
             double betweenvariance= wb*wf*((meanb-meanf)*(meanb-meanf));
             
            if(betweenvariance>maxVariance){
             
                maxVariance=betweenvariance;
                lastThresh=thresh;
             
            }
            
        }
        
      
      return lastThresh;
    }
    
    
}





class Histogram{
    
    static int histotal[]=new int[766];
    static int hisred[]= new int[256];
    static int hisgreen[]= new int[256];
    static int hisblue[]= new int[256];
    
    
}




class Pixel{

    int red;
    int green;
    int blue;
    int total;

    public Pixel(int red, int green, int blue, int total) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.total = total;
    }

    @Override
    public String toString() {
        return "Pixel{" +
                "red=" + red +
                ", green=" + green +
                ", blue=" + blue +
                ", total=" + total +
                '}';
    }
}
