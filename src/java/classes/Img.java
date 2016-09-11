/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package classes;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author pj
 */
public class Img {
    BufferedImage source;
    BufferedImage destination;
    File sourceFile;
    File destinationFile;
    private int width;
    private int height;
    String extension;

     public boolean resizeWidth(File sourceFile, File destinationFile, int width) throws IOException {
           String format;
            this.sourceFile = sourceFile;
            this.destinationFile=destinationFile;
            this.extension=(sourceFile.getName().substring(sourceFile.getName().lastIndexOf("."))).toLowerCase();
            this.extension=this.extension.substring(1);
            if(this.extension.equals("jpg"))
                format="JPEG";
            else
                format=this.extension.toUpperCase();
            this.source = ImageIO.read(this.sourceFile);
            this.width = width;
            int height0 = this.source.getHeight();
            int width0 = this.source.getWidth();
            this.height = (int) (((double)height0*(double)this.getWidth())/((double)width0));
            this.destination = new BufferedImage(this.getWidth(), this.getHeight(),BufferedImage.TYPE_INT_RGB);
            Graphics2D g = this.destination.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(this.source, 0, 0, this.getWidth(), this.getHeight(), null);
            g.dispose();
            ImageIO.write(this.destination, format, this.destinationFile);
            return true;
   }
    public boolean resizeHeight(File sourceFile, File destinationFile, int height) throws IOException {
            String format;
            this.sourceFile = sourceFile;
            this.destinationFile=destinationFile;
            this.extension=(sourceFile.getName().substring(sourceFile.getName().lastIndexOf("."))).toLowerCase();
            this.extension=this.extension.substring(1);
            if(this.extension.equals("jpg"))
                format="JPEG";
            else
                format=this.extension.toUpperCase();
            this.source = ImageIO.read(this.sourceFile);
            this.height = height;
            int height0 = this.source.getHeight();
            int width0 = this.source.getWidth();
            this.width = (int) (((double)width0*(double)this.getHeight())/((double)height0));
            this.destination = new BufferedImage(this.getWidth(), this.getHeight(),BufferedImage.TYPE_INT_RGB);
            Graphics2D g = this.destination.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(this.source, 0, 0, this.getWidth(), this.getHeight(), null);
            g.dispose();
            ImageIO.write(this.destination, format, this.destinationFile);
            return true;
   }
   public boolean getSize(File sourceFile) throws IOException {
            this.sourceFile = sourceFile;
            this.source = ImageIO.read(this.sourceFile);
            this.width=this.source.getWidth();
            this.height=this.source.getHeight();
            return true;

   }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }
}
