//package randomgraph;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.geom.GeneralPath;
import java.awt.GraphicsEnvironment.*;
import com.sun.image.codec.jpeg.*;

/**
 * Title:        RandomGraph
 * Description:  Demo Servlet for generating random graphs.
 * Copyright:    Copyright (c) 2001
 * Company:      Sun Microsystems
 * @author Kevin Rabito
 * @version 1.0
 */

public class RandomGraph extends HttpServlet implements SingleThreadModel {
  private static final String CONTENT_TYPE = "text/html";
  /**Initialize global variables*/
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }
  //Service the request
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    int graphSeed = 1;
    final int x0 = 20;
    final int y0 = 80;
    int xOld = 0;
    int yOld = 0;
    int xNew = 0;
    int yNew = 0;
    int [] xPlot = new int [4];

    String hostName = InetAddress.getLocalHost().getHostName();
    try {
      graphSeed = Integer.parseInt(request.getParameter("Seed"));
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    response.setContentType("image/jpeg");
    ServletOutputStream out = response.getOutputStream();

    BufferedImage image = new BufferedImage(250,100,BufferedImage.TYPE_INT_RGB);

    if( image == null) log("Buffered Image is null");
    Graphics2D plot = image.createGraphics();

    plot.setPaint(Color.yellow);
    plot.setStroke(new BasicStroke(1.0f));
    // X Axis
    plot.drawLine(20,80,250,80);
    // Y Axis
    plot.drawLine(20,2,20,80);
    // X Axis Label
    plot.drawString(hostName, 125,95);
    plot.setPaint(Color.green);

    Random xSeeds = new java.util.Random();
    for(int a = 0; a < 4; a++){
      xPlot[a] = (int) (xSeeds.nextDouble() * 80);
      //System.out.println("xPlot= " + xPlot[a] + ", a= " + a);
    }

    for(int xx = 0; xx < xPlot.length; xx++){
      xNew =  x0 + xPlot[xx];
      plot.fillRect(x0, y0 - 50 + xx*10,xNew,4);
    }

    JPEGEncodeParam param = JPEGCodec.getDefaultJPEGEncodeParam(image);
    param.setQuality(1,false);

    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out,param);

    try{
      encoder.encode(image);
    }
    catch(IOException e){
    }
    xPlot = null;
    xSeeds = null;
    out.close();
    image.flush();
    plot.dispose();
    System.gc();
  }



  /**Clean up resources*/
  public void destroy() {
    System.gc();
  }
}