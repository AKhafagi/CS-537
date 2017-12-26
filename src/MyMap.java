//// Just adds a simple AcetateLayer to place a line & points on the
//// map. Mercifully this works if the drawing is done in Java terms,
//// using frame coordinates (pixels).  Using an AcetateLayer has the
//// advantage that an AcetateLayer is transparent, and this solves
//// a problem that transparent layers in Java are somewhat tricky
//// to do, e.g. a canvas can not be made transparent. In addition,
//// the line is drawn in stages by using a thread.  The thread is
//// wise and necessary.  In this  version, the acetate layers
//// are removed, within the loop, so they do not accumulate
//// and slow things down. The loop should work with two arbitrary
//// points!  Those points are parameters to the thread code.
//
//import com.esri.mo2.cs.geom.Point;
//import com.esri.mo2.ui.bean.*;
//import com.esri.mo2.ui.tb.*;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;
//import java.awt.geom.*;
//
//public class MyMap extends JFrame {
//    private java.awt.Point start;
//    private java.awt.Point end;
//    private java.awt.Point mid;
//    private boolean drawTriangle = false;
//    static Map map = new Map();
//    private Layer layer = new Layer();
//    private Layer layer2 = new Layer();
//    private Toc toc = new Toc();
//    private String s1 = "./esri/MOJ20/Samples/Data/World/country.shp";
//    private String s2 = "./esri/MOJ20/Samples/Data/World/cities.shp";
//    private ZoomPanToolBar zptb = new ZoomPanToolBar();
//    private SelectionToolBar stb = new SelectionToolBar();
//    private JPanel jpanel = new JPanel();
//    private JButton getCoordinates = new JButton("Input Coordinates");
//    private JButton draw = new JButton("Draw");
//    private JButton help = new JButton("Help");
//    private ActionListener getCoordinatesListener;
//    private ActionListener drawListener;
//    private ActionListener helpPopup;
//
//
//    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//   /*
//   * Helper method to convert a World Point to a pixel on the screen
//   * input: worldPoint: a Point that holds the world point to convert to a pixel on the screen.
//   * returns: a Java.awt.Point that has the x, and y pixel on the screen.
//   * */
//    private java.awt.Point transformWorldPointToPixel(Point worldPoint) {
//        return map.transformWorldToPixel(worldPoint.x, worldPoint.y);
//    }
//
//    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    private MyMap() {
//        super("World Map");
//        this.setBounds(80, 80, 1024, 1024);
//        zptb.setMap(map);
//        stb.setMap(map);
//        // listens to changes on the get coordinates button.
//        getCoordinatesListener = ae -> {
//            Point startPoint, endPoint, midPoint;
//            double x1, x2, x3, y1, y2, y3;
//            x1 = x2 = x3 = y1 = y2 = y3 = 0.0;
//            String prompt = "Please enter a set of coordinates separated by a comma for example \"37 N,65 W, 21 N,42 W\"\n"
//                    + "will be 2 sets of coordinates \'37 N\' is one part of the set and \'65 W\' is the second part\n" +
//                    "you can also enter 3 sets of coordinates to draw a triangle";
//            String input;
//            String[] inputs;
//            do {
//                input = JOptionPane.showInputDialog(this, prompt, "Please Enter Coordinates", JOptionPane.QUESTION_MESSAGE);
//                if (input == null) {
//                    JOptionPane.showMessageDialog(this, "Input is required, please try again");
//                    continue;
//                }
//                inputs = input.trim().split(",");
//                if(inputs.length ==1 && inputs[0].isEmpty()){
//                    JOptionPane.showMessageDialog(this, "No coordinates have been entered the program will now exit!");
//                    try {
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } finally {
//                        System.exit(0);
//                    }
//                }
//                else if (inputs.length < 4) {
//                    JOptionPane.showMessageDialog(this, "Incorrect number of coordinates please try again");
//                   continue;
//                }
//                boolean error = false;
//                // converts S,and W to negative for all inputs.
//                for (int i = 0; i < inputs.length; i++) {
//                    String[] coordinate = inputs[i].trim().split(" ", -1);
//                    if (coordinate.length != 2) {
//                        JOptionPane.showMessageDialog(this, "Coordinates entered in wrong format please try again");
//                        error = true;
//                        break;
//                    }
//                    switch (i) {
//
//                        case 0:
//                            x1 = Double.parseDouble(coordinate[0]);
//                            if (coordinate[1].toUpperCase().equals("S")) {
//                                x1 *= -1;
//                            }
//                            break;
//
//                        case 1:
//                            y1 = Double.parseDouble(coordinate[0]);
//                            if (coordinate[1].toUpperCase().equals("W")) {
//                                y1 *= -1;
//                            }
//                            break;
//                        case 2:
//                            x2 = Double.parseDouble(coordinate[0]);
//                            if (coordinate[1].toUpperCase().equals("S")) {
//                                x2 *= -1;
//                            }
//                            break;
//                        case 3:
//                            y2 = Double.parseDouble(coordinate[0]);
//                            if (coordinate[1].toUpperCase().equals("W")) {
//                                y2 *= -1;
//                            }
//                            break;
//                        case 4:
//                            x3 = Double.parseDouble(coordinate[0]);
//                            if (coordinate[1].toUpperCase().equals("S")) {
//                                x3 *= -1;
//                            }
//                            break;
//                        case 5:
//                            y3 = Double.parseDouble(coordinate[0]);
//                            if (coordinate[1].toUpperCase().equals("W")) {
//                                y3 *= -1;
//                            }
//                            break;
//
//                    }
//                }
//                if (!error)
//                    break;
//            } while (true);
//            if (inputs.length > 4) {
//                startPoint = new Point(y1, x1);
//                midPoint = new Point(y2, x2);
//                endPoint = new Point(y3, x3);
//                start = transformWorldPointToPixel(startPoint);
//                end = transformWorldPointToPixel(endPoint);
//                mid = transformWorldPointToPixel(midPoint);
//                drawTriangle = true;
//                draw.setText("Draw Triangle");
//                JOptionPane.showMessageDialog(this, "Three Coordinates entered successfully you can now draw a triangle");
//            } else {
//                startPoint = new Point(y1, x1);
//                endPoint = new Point(y2, x2);
//                start = transformWorldPointToPixel(startPoint);
//                end = transformWorldPointToPixel(endPoint);
//                drawTriangle = false;
//                draw.setText("Draw Line");
//                JOptionPane.showMessageDialog(this, "Two Coordinates entered successfully");
//            }
//
//            getCoordinates.setEnabled(false);
//            draw.setEnabled(true);
//        };
//        drawListener = ae -> {
//            Flash flash;
//            if (drawTriangle) {
//                flash = new Flash(start.x, start.y, mid.x, mid.y, end.x, end.y, this);
//            } else
//                flash = new Flash(start.x, start.y, end.x, end.y, this);
//            flash.start();
//        };
//
//
//        toc.setMap(map);
//        helpPopup = ae -> {
//            String message = "To use this program first click the button \"Enter Coordinates\" \n then click the button \"Draw\"\n" +
//                    "1) You can draw a line by inputting two coordinates.\n"+
//                    "The form of the coordinates should be latitude degree with direction separated by a space and \n"+
//                    "and the longitude degree with direction sperated by a space. Every coordinate should be delimted by a comma\n"+
//                    "2) You can draw a triangle by inputting three world coordinates";
//            JOptionPane.showMessageDialog(this.getComponent(0), message);
//        };
//        draw.addActionListener(drawListener);
//        draw.setEnabled(false);
//        getCoordinates.addActionListener(getCoordinatesListener);
//        help.addActionListener(helpPopup);
//        jpanel.add(zptb);
//        jpanel.add(stb);
//        jpanel.add(getCoordinates);
//        jpanel.add(draw);
//        jpanel.add(help);
//        getContentPane().add(map, BorderLayout.CENTER);
//        getContentPane().add(jpanel, BorderLayout.NORTH);
//        addShapefileToMap(layer, s1);
//        addShapefileToMap(layer2, s2);
//        getContentPane().add(toc, BorderLayout.WEST);
//    }
//
//    private void addShapefileToMap(Layer layer, String s) {
//        layer.setDataset("0;" + s);
//        map.add(layer);
//
//    }
//
//    public static void main(String[] args) {
//        MyMap qstart = new MyMap();
//        qstart.addWindowListener(new WindowAdapter() {
//            public void windowClosing(WindowEvent e) {
//                JOptionPane.showMessageDialog(qstart, "Thanks for using this program Good Bye!");
//                System.exit(0);
//            }
//        });
//        qstart.setVisible(true);
//    }
//}
//
//class Flash extends Thread {
//    private AcetateLayer acetLayer = new AcetateLayer();
//    private double x1;
//    private double y1;
//    private double x2;
//    private double y2;
//    private double x3;
//    private double y3;
//    private boolean drawTriangle;
//    private JFrame frame;
//
//
//    Flash(double x11, double y11, double x22, double y22, JFrame f) {
//        x1 = x11;
//        y1 = y11;
//        x2 = x22;
//        y2 = y22;
//        this.drawTriangle = false;
//        this.frame = f;
//    }
//
//    Flash(double x11, double y11, double x22, double y22, double x33, double y33, JFrame f) {
//        x1 = x11;
//        y1 = y11;
//        x2 = x22;
//        y2 = y22;
//        x3 = x33;
//        y3 = y33;
//        this.drawTriangle = true;
//        this.frame = f;
//    }
//
//
//    public void run() {
//        if (drawTriangle) {
//            for (int i = 0; i < 21; i++) {
//                try {
//                    Thread.sleep(300);
//                    final int j = i;
//                    if (acetLayer != null) MyMap.map.remove(acetLayer);
//                    acetLayer = new AcetateLayer() {
//                        public void paintComponent(java.awt.Graphics g) {
//                            java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
//                            // Line2D.Double line = new Line2D.Double(x1,y1,x2,y2);
//                            Line2D.Double line = new Line2D.Double(x1, y1, x1 + j * (x2 - x1) / 20.0, y1 + j * (y2 - y1) / 20.0);
//                            Line2D.Double line2 = new Line2D.Double(x2, y2, x2 + j * (x3 - x2) / 20.0, y2 + j * (y3 - y2) / 20.0);
//                            Line2D.Double line3 = new Line2D.Double(x3, y3, x3 + j * (x1 - x3) / 20.0, y3 + j * (y1 - y3) / 20.0);
//                            g2d.setColor(new Color(250, 23, 0));
//                            g2d.draw(line);
//                            g2d.draw(line2);
//                            g2d.draw(line3);
//                            g2d.setColor(new Color(0, 42, 250));
//                            g2d.fillOval((int) x1, (int) y1, 5, 5);
//                            g2d.fillOval((int) x2, (int) y2, 5, 5);
//                            g2d.fillOval((int) x3, (int) y3, 5, 5);
//                        }
//                    };
//                    acetLayer.setMap(MyMap.map);
//                    MyMap.map.add(acetLayer);
//                } catch (Exception ignored) {
//
//                }
//            }
//            JOptionPane.showMessageDialog(frame, "The Triangle has been drawn");
//            System.exit(0);
//        } else {
//            for (int i = 0; i < 21; i++) {
//                try {
//                    Thread.sleep(300);
//                    final int j = i;
//                    if (acetLayer != null) MyMap.map.remove(acetLayer);
//                    acetLayer = new AcetateLayer() {
//                        public void paintComponent(java.awt.Graphics g) {
//                            java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
//                            // Line2D.Double line = new Line2D.Double(x1,y1,x2,y2);
//                            Line2D.Double line = new Line2D.Double(x1, y1, x1 + j * (x2 - x1) / 20.0, y1 + j * (y2 - y1) / 20.0);
//                            g2d.setColor(new Color(250, 23, 0));
//                            g2d.draw(line);
//                            g2d.setColor(new Color(41, 0, 250));
//                            g2d.fillOval((int) x1, (int) y1, 5, 5);
//                            g2d.fillOval((int) x2, (int) y2, 5, 5);
//                        }
//                    };
//                    acetLayer.setMap(MyMap.map);
//                    MyMap.map.add(acetLayer);
//                } catch (Exception ignored) {
//                }
//            }
//            JOptionPane.showMessageDialog(frame, "The Line has been drawn");
//            System.exit(0);
//        }
//    }
//}
//
