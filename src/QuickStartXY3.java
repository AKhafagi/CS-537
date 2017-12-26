// This program extends QuickStartXY3.java for the purpose of adding a help menu.
// The following comments are from QuickStartXY3.java
// This code gives the ability to easily add a point theme from
// a file of points with lines in the form longitude,latitude,name
// in world coordinates.  We also give the user a choice of creating
// a new shape file.  Letting the user browse to a folder would prevent
// a LOT of user errors.  This should really be done......maybe later.

import com.esri.mo2.cs.geom.*;
import com.esri.mo2.data.feat.*;
import com.esri.mo2.file.shp.*;
import com.esri.mo2.map.dpy.*;
import com.esri.mo2.map.draw.*;
import com.esri.mo2.ui.bean.*;
import com.esri.mo2.ui.bean.Layer;
import com.esri.mo2.ui.bean.Map;
import com.esri.mo2.ui.dlg.*;
import com.esri.mo2.ui.ren.*;
import com.esri.mo2.ui.tb.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.*;
import java.sql.*;
import java.util.*;

// TocEvent,Legend(a legend is part of a toc),ActateLayer

class QuickStartHelp extends JFrame {
    static Map map = new Map();
    static boolean fullMap = true;  // Map not zoomed
    Legend legend;
    Legend legend2;
    Layer layer = new Layer();
    Layer layer2 = new Layer();
    Layer layer3 = null;
    static AcetateLayer acetLayer;
    static com.esri.mo2.map.dpy.Layer layer4;
    com.esri.mo2.map.dpy.Layer activeLayer;
    int activeLayerIndex;
    com.esri.mo2.cs.geom.Point initPoint, endPoint;
    double distance;
    JMenuBar menu = new JMenuBar();
    JMenu file = new JMenu("File");
    JMenu theme = new JMenu("Theme");
    JMenu layercontrol = new JMenu("LayerControl");
    JMenu help = new JMenu("Help");
    JMenuItem attribitem = new JMenuItem("open attribute table",
            new ImageIcon("tableview.gif"));
    JMenuItem createlayeritem = new JMenuItem("create layer from selection",
            new ImageIcon("Icon0915b.jpg"));
    static JMenuItem promoteitem = new JMenuItem("promote selected layer",
            new ImageIcon("promote.jpg"));
    JMenuItem demoteitem = new JMenuItem("demote selected layer",
            new ImageIcon("demote.jpg"));
    JMenuItem printitem = new JMenuItem("print", new ImageIcon("print.gif"));
    JMenuItem addlyritem = new JMenuItem("add layer", new ImageIcon("addtheme.gif"));
    JMenuItem remlyritem = new JMenuItem("remove layer", new ImageIcon("delete.gif"));
    JMenuItem propsitem = new JMenuItem("Legend Editor", new ImageIcon("properties.gif"));
    JMenu helptopics = new JMenu("Help Topics");
    JMenuItem tocitem = new JMenuItem("Table of Contents", new ImageIcon("helptopic.gif"));
    JMenuItem legenditem = new JMenuItem("Legend Editor", new ImageIcon("helptopic.gif"));
    JMenuItem layercontrolitem = new JMenuItem("Layer Control", new ImageIcon("helptopic.gif"));
    JMenuItem helptoolitem = new JMenuItem("Help Tool", new ImageIcon("help2.gif"));
    JMenuItem contactitem = new JMenuItem("Contact us");
    JMenuItem aboutitem = new JMenuItem("About MOJO...");
    Toc toc = new Toc();
    String s1 = "./esri/MOJ20/Samples/Data/USA/Washtenaw.shp";
    String datapathname = "";
    String legendname = "";
    boolean showDialog = false;
    String path;
    ZoomPanToolBar zptb = new ZoomPanToolBar();
    static SelectionToolBar stb = new SelectionToolBar();
    JToolBar jtb = new JToolBar();
    ComponentListener complistener;
    JLabel statusLabel = new JLabel("status bar    LOC");
    static JLabel milesLabel = new JLabel("   DIST:  0 mi    ");
    static JLabel kmLabel = new JLabel("  0 km    ");
    java.text.DecimalFormat df = new java.text.DecimalFormat("0.000");
    JPanel jpanel = new JPanel();
    JPanel jpanel2 = new JPanel();
    JButton printB = new JButton(new ImageIcon("print.gif"));
    JButton addLayerB = new JButton(new ImageIcon("addtheme.gif"));
    JButton pointerB = new JButton(new ImageIcon("pointer.gif"));
    JButton distB = new JButton(new ImageIcon("measure_1.gif"));
    JButton csvB = new JButton("XY");
    //Arrow arrow = new Arrow();
    //DistanceTool distanceTool= new DistanceTool();
    ActionListener lis;
    ActionListener LayerListener;
    ActionListener layerControlListener;
    ActionListener helplis;
    TocAdapter mytocadapter;
    static Envelope env;

    public QuickStartHelp() {

        super("Washtenaw County");
        //distanceTool.setMeasureUnit(com.esri.mo2.util.Units.MILES);
        //map.setMapUnit(com.esri.mo2.util.Units.MILES);
        this.setBounds(50, 50, 750, 500);
        zptb.setMap(map);
        stb.setMap(map);
        setJMenuBar(menu);
        ActionListener lisZoom = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                fullMap = false;
            }
        }; // can change a boolean here
        ActionListener lisFullExt = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                fullMap = true;
            }
        };
        // next line gets ahold of a reference to the zoomin button
        JButton zoomInButton = (JButton) zptb.getActionComponent("ZoomIn");
        JButton zoomFullExtentButton =
                (JButton) zptb.getActionComponent("ZoomToFullExtent");
        JButton zoomToSelectedLayerButton =
                (JButton) zptb.getActionComponent("ZoomToSelectedLayer");
        zoomInButton.addActionListener(lisZoom);
        zoomFullExtentButton.addActionListener(lisFullExt);
        zoomToSelectedLayerButton.addActionListener(lisZoom);
        complistener = new ComponentAdapter() {
            public void componentResized(ComponentEvent ce) {
                if (fullMap) {
                    map.setExtent(env);
                    map.zoom(1.0);    //scale is scale factor in pixels
                    map.redraw();
                }
            }
        };
        addComponentListener(complistener);
        lis = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Object source = ae.getSource();
                if (source == printB || source instanceof JMenuItem) {
                    com.esri.mo2.ui.bean.Print mapPrint = new com.esri.mo2.ui.bean.Print();
                    mapPrint.setMap(map);
                    mapPrint.doPrint();// prints the map
                } else if (source == pointerB) {
                    Arrow arrow = new Arrow();
                    map.setSelectedTool(arrow);
                } else if (source == distB) {
                    DistanceTool distanceTool = new DistanceTool();
                    map.setSelectedTool(distanceTool);
                } else if (source == csvB) {
                    try {
                        AddXYtheme addXYtheme = new AddXYtheme();
                        addXYtheme.setMap(map);
                        addXYtheme.setVisible(false);// the file chooser needs a parent
                        // but the parent can stay behind the scenes
                        map.redraw();
                    } catch (IOException e) {
                    }
                } else {
                    try {
                        AddLyrDialog aldlg = new AddLyrDialog();
                        aldlg.setMap(map);
                        aldlg.setVisible(true);
                    } catch (IOException e) {
                    }
                }
            }
        };
        layerControlListener = new ActionListener() {
            public void
            actionPerformed(ActionEvent ae) {
                String source = ae.getActionCommand();
                System.out.println(activeLayerIndex + " active index");
                if (source == "promote selected layer")
                    map.getLayerset().moveLayer(activeLayerIndex, ++activeLayerIndex);
                else
                    map.getLayerset().moveLayer(activeLayerIndex, --activeLayerIndex);
                enableDisableButtons();
                map.redraw();
            }
        };
        helplis = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Object source = ae.getSource();
                if (source instanceof JMenuItem) {
                    String arg = ae.getActionCommand();
                    if (arg == "About MOJO...") {
                        AboutBox aboutbox = new AboutBox();
                        aboutbox.setProductName("MOJO");
                        aboutbox.setProductVersion("2.0");
                        aboutbox.setVisible(true);
                        aboutbox.setLocation(100, 100);
                    } else if (arg == "Contact us") {
                        try {
                            String s = "\n\n\n\n        Any enquiries should be addressed to " +
                                    "\n\n\n                         eckberg@edoras.sdsu.edu";
                            HelpDialog helpdialog = new HelpDialog(s);
                            helpdialog.setVisible(true);
                        } catch (IOException e) {
                        }
                    } else if (arg == "Table of Contents") {
                        try {
                            HelpDialog helpdialog = new HelpDialog((String) helpText.get(0));
                            helpdialog.setVisible(true);
                        } catch (IOException e) {
                        }
                    } else if (arg == "` Editor") {
                        try {
                            HelpDialog helpdialog = new HelpDialog((String) helpText.get(1));
                            helpdialog.setVisible(true);
                        } catch (IOException e) {
                        }
                    } else if (arg == "Layer Control") {
                        try {
                            HelpDialog helpdialog = new HelpDialog((String) helpText.get(2));
                            helpdialog.setVisible(true);
                        } catch (IOException e) {
                        }
                    }
                }
            }
        };
        LayerListener = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Object source = ae.getSource();
                if (source instanceof JMenuItem) {
                    String arg = ae.getActionCommand();
                    if (arg == "add layer") {
                        try {
                            AddLyrDialog aldlg = new AddLyrDialog();
                            aldlg.setMap(map);
                            aldlg.setVisible(true);
                        } catch (IOException e) {
                        }
                    } else if (arg == "remove layer") {
                        try {
                            com.esri.mo2.map.dpy.Layer dpylayer =
                                    legend.getLayer();
                            map.getLayerset().removeLayer(dpylayer);
                            map.redraw();
                            remlyritem.setEnabled(false);
                            propsitem.setEnabled(false);
                            attribitem.setEnabled(false);
                            promoteitem.setEnabled(false);
                            demoteitem.setEnabled(false);
                            stb.setSelectedLayer(null);
                            zptb.setSelectedLayer(null);
                        } catch (Exception e) {
                        }
                    } else if (arg == "Legend Editor") {
                        LayerProperties lp = new LayerProperties();
                        lp.setLegend(legend);
                        lp.setSelectedTabIndex(0);
                        lp.setVisible(true);
                    } else if (arg == "open attribute table") {
                        try {
                            layer4 = legend.getLayer();
                            AttrTab attrtab = new AttrTab();
                            attrtab.setVisible(true);
                        } catch (IOException ioe) {
                        }
                    } else if (arg == "create layer from selection") {
                        com.esri.mo2.map.draw.BaseSimpleRenderer sbr = new
                                com.esri.mo2.map.draw.BaseSimpleRenderer();
                        com.esri.mo2.map.draw.SimpleFillSymbol sfs = new
                                com.esri.mo2.map.draw.SimpleFillSymbol();// for polygons
                        sfs.setSymbolColor(new Color(255, 255, 0)); // mellow yellow
                        sfs.setType(com.esri.mo2.map.draw.SimpleFillSymbol.FILLTYPE_SOLID);
                        sfs.setBoundary(true);
                        layer4 = legend.getLayer();
                        FeatureLayer flayer2 = (FeatureLayer) layer4;
                        // select, e.g., Montana and then click the
                        // create layer menuitem; next line verifies a selection was made
                        System.out.println("has selected" + flayer2.hasSelection());
                        //next line creates the 'set' of selections
                        if (flayer2.hasSelection()) {
                            SelectionSet selectset = flayer2.getSelectionSet();
                            // next line makes a new feature layer of the selections
                            FeatureLayer selectedlayer = flayer2.createSelectionLayer(selectset);
                            sbr.setLayer(selectedlayer);
                            sbr.setSymbol(sfs);
                            selectedlayer.setRenderer(sbr);
                            Layerset layerset = map.getLayerset();
                            // next line places a new visible layer, e.g. Montana, on the map
                            layerset.addLayer(selectedlayer);
                            //selectedlayer.setVisible(true);
                            if (stb.getSelectedLayers() != null)
                                promoteitem.setEnabled(true);
                            try {
                                legend2 = toc.findLegend(selectedlayer);
                            } catch (Exception e) {
                            }

                            CreateShapeDialog csd = new CreateShapeDialog(selectedlayer);
                            csd.setVisible(true);
                            Flash flash = new Flash(legend2);
                            flash.start();
                            map.redraw(); // necessary to see color immediately

                        }
                    }
                }
            }
        };
        toc.setMap(map);
        mytocadapter = new TocAdapter() {
            public void click(TocEvent e) {
                System.out.println(activeLayerIndex + "dex");
                legend = e.getLegend();
                activeLayer = legend.getLayer();
                stb.setSelectedLayer(activeLayer);
                zptb.setSelectedLayer(activeLayer);
                // get acive layer index for promote and demote
                activeLayerIndex = map.getLayerset().indexOf(activeLayer);
                // layer indices are in order added, not toc order.
                System.out.println(activeLayerIndex + "active ndex");
                remlyritem.setEnabled(true);
                propsitem.setEnabled(true);
                attribitem.setEnabled(true);
                enableDisableButtons();
            }
        };
        map.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent me) {
                com.esri.mo2.cs.geom.Point worldPoint = null;
                if (map.getLayerCount() > 0) {
                    worldPoint = map.transformPixelToWorld(me.getX(), me.getY());
                    String s = "X:" + df.format(worldPoint.getX()) + " " +
                            "Y:" + df.format(worldPoint.getY());
                    statusLabel.setText(s);
                } else
                    statusLabel.setText("X:0.000 Y:0.000");
            }
        });

        toc.addTocListener(mytocadapter);
        remlyritem.setEnabled(false); // assume no layer initially selected
        propsitem.setEnabled(false);
        attribitem.setEnabled(false);
        promoteitem.setEnabled(false);
        demoteitem.setEnabled(false);
        printitem.addActionListener(lis);
        addlyritem.addActionListener(LayerListener);
        remlyritem.addActionListener(LayerListener);
        propsitem.addActionListener(LayerListener);
        attribitem.addActionListener(LayerListener);
        createlayeritem.addActionListener(LayerListener);
        promoteitem.addActionListener(layerControlListener);
        demoteitem.addActionListener(layerControlListener);
        tocitem.addActionListener(helplis);
        legenditem.addActionListener(helplis);
        layercontrolitem.addActionListener(helplis);
        helptoolitem.addActionListener(helplis);
        contactitem.addActionListener(helplis);
        aboutitem.addActionListener(helplis);
        file.add(addlyritem);
        file.add(printitem);
        file.add(remlyritem);
        file.add(propsitem);
        theme.add(attribitem);
        theme.add(createlayeritem);
        layercontrol.add(promoteitem);
        layercontrol.add(demoteitem);
        help.add(helptopics);
        helptopics.add(tocitem);
        helptopics.add(legenditem);
        helptopics.add(layercontrolitem);
        help.add(helptoolitem);
        help.add(contactitem);
        help.add(aboutitem);
        menu.add(file);
        menu.add(theme);
        menu.add(layercontrol);
        menu.add(help);
        printB.addActionListener(lis);
        printB.setToolTipText("print map");
        addLayerB.addActionListener(lis);
        addLayerB.setToolTipText("add layer");
        pointerB.addActionListener(lis);
        distB.addActionListener(lis);
        csvB.addActionListener(lis);
        csvB.setToolTipText("add a layer of points from a file");
        pointerB.setToolTipText("pointer");
        distB.setToolTipText("press-drag-release to measure a distance");
        jtb.add(printB);
        jtb.add(addLayerB);
        jtb.add(pointerB);
        jtb.add(distB);
        jtb.add(csvB);
        jpanel.add(jtb);
        jpanel.add(zptb);
        jpanel.add(stb);
        jpanel2.add(statusLabel);
        jpanel2.add(milesLabel);
        jpanel2.add(kmLabel);
        setuphelpText();
        getContentPane().add(map, BorderLayout.CENTER);
        getContentPane().add(jpanel, BorderLayout.NORTH);
        getContentPane().add(jpanel2, BorderLayout.SOUTH);
        addShapefileToMap(layer, s1);
        getContentPane().add(toc, BorderLayout.WEST);
    }

    private void addShapefileToMap(Layer layer, String s) {
        String datapath = s;
        layer.setDataset("0;" + datapath);
        map.add(layer);
    }

    private void setuphelpText() {
        String s0 =
                "    The toc, or table of contents, is to the left of the map. \n" +
                        "    Each entry is called a 'legend' and represents a map 'layer' or \n" +
                        "    'theme'.  If you click on a legend, that layer is called the \n" +
                        "    active layer, or selected layer.  Its display (rendering) properties \n" +
                        "    can be controlled using the Legend Editor, and the legends can be \n" +
                        "    reordered using Layer Control.  Both Legend Editor and Layer Control \n" +
                        "    are separate Help Topics.  This line is e... x... t... e... n... t... e... d" +
                        "    to test the scrollpane.";
        helpText.add(s0);
        String s1 = "  Under construction";
        helpText.add(s1);
        String s2 = "  Under construction";
        helpText.add(s2);
    }

    public static void main(String[] args) {
        QuickStartHelp qstart = new QuickStartHelp();
        qstart.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.out.println("Thanks, Quick Start exits");
                System.exit(0);
            }
        });
        qstart.setVisible(true);
        env = map.getExtent();
    }

    private void enableDisableButtons() {
        int layerCount = map.getLayerset().getSize();
        if (layerCount < 2) {
            promoteitem.setEnabled(false);
            demoteitem.setEnabled(false);
        } else if (activeLayerIndex == 0) {
            demoteitem.setEnabled(false);
            promoteitem.setEnabled(true);
        } else if (activeLayerIndex == layerCount - 1) {
            promoteitem.setEnabled(false);
            demoteitem.setEnabled(true);
        } else {
            promoteitem.setEnabled(true);
            demoteitem.setEnabled(true);
        }
    }

    private ArrayList helpText = new ArrayList(3);
}

// following is an Add Layer dialog window
class AddLyrDialog extends JDialog {
    Map map;
    ActionListener lis;
    JButton ok = new JButton("OK");
    JButton cancel = new JButton("Cancel");
    JPanel panel1 = new JPanel();
    com.esri.mo2.ui.bean.CustomDatasetEditor cus = new com.esri.mo2.ui.bean.CustomDatasetEditor();

    AddLyrDialog() throws IOException {
        setBounds(50, 50, 520, 430);
        setTitle("Select a theme/layer");
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                setVisible(false);
            }
        });
        lis = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Object source = ae.getSource();
                if (source == cancel)
                    setVisible(false);
                else {
                    try {
                        setVisible(false);
                        map.getLayerset().addLayer(cus.getLayer());
                        map.redraw();
                        if (QuickStartHelp.stb.getSelectedLayers() != null)
                            QuickStartHelp.promoteitem.setEnabled(true);
                    } catch (IOException e) {
                    }
                }
            }
        };
        ok.addActionListener(lis);
        cancel.addActionListener(lis);
        getContentPane().add(cus, BorderLayout.CENTER);
        panel1.add(ok);
        panel1.add(cancel);
        getContentPane().add(panel1, BorderLayout.SOUTH);
    }

    public void setMap(com.esri.mo2.ui.bean.Map map1) {
        map = map1;
    }
}

class AddXYtheme extends JDialog {
    Map map;
    Vector<String> fieldNames = new Vector<>();
    Vector fieldValues = new Vector();
    JFileChooser jfc = new JFileChooser();
    BasePointsArray bpa = new BasePointsArray();
    AddXYtheme() throws IOException {
        setBounds(50,50,520,430);
        jfc.setCurrentDirectory(new File("./"));
        jfc.showOpenDialog(this);
        try {
            File file = jfc.getSelectedFile();

            FileReader fred = new FileReader(file);
            BufferedReader in = new BufferedReader(fred);
            String s;
            double x=0.0, y=0.0;
            int n = 0;
            int i = 0;
            boolean first =true;
            while ((s = in.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(s, ",");
                i =0;
                while (st.hasMoreTokens()) {
                    if (first) {
                        fieldNames.add(st.nextToken());
                    } else {
                        if (i == 0) {
                            x = Double.parseDouble(st.nextToken());
                            fieldValues.add(x);
                        } else if (i == 1) {
                            y = Double.parseDouble(st.nextToken());
                            fieldValues.add(y);
                        } else
                            fieldValues.add(st.nextToken());
                        i++;
                    }
                }
                if(!first) {
                    bpa.insertPoint(n, new com.esri.mo2.cs.geom.Point(x, y));
                    n++;
                }
                else{
                    first  = false;
                }

            }
        } catch (IOException e){}
        XYfeatureLayer xyfl =
                new XYfeatureLayer(bpa,map,fieldNames,fieldValues);
        xyfl.setVisible(true);
        map = QuickStartHelp.map;
//        map.getLayerset().addLayer(xyfl);
//        map.redraw();
        CreateXYShapeDialog xydialog = new CreateXYShapeDialog(xyfl, map);
        xydialog.setVisible(true);
    }

    //=========================================================================
    // FUNCTION: setMap()
    // DESCRIPTION: This function will set the map to the map passed in.
    //=========================================================================
    public void setMap(com.esri.mo2.ui.bean.Map map1){
        map = map1;
    }
}


//===========================================================================
// CLASS: XYfeatureLayer
// DESCRIPTION: This is the nuts and bolts class for the create feature layer
//              function from CSV (XYTool).
//===========================================================================
class XYfeatureLayer extends BaseFeatureLayer {
    BaseFields fields;
    private java.util.Vector featureVector;

    //=========================================================================
    // FUNCTION: XYFeatureLayer()
    // DESCRIPTION: Class constructor
    //=========================================================================
    public XYfeatureLayer(BasePointsArray bpa, Map map, Vector fieldNames, Vector fieldValues) {

        createFeaturesAndFields(bpa,map,fieldNames,fieldValues);
        BaseFeatureClass bfc = getFeatureClass("MyPoints",bpa);
        setFeatureClass(bfc);
        BaseSimpleRenderer srd = new BaseSimpleRenderer();
        com.esri.mo2.map.draw.RasterMarkerSymbol sms = null; // for points
        sms = new com.esri.mo2.map.draw.RasterMarkerSymbol();
        sms.setSizeX(30);
        sms.setSizeY(30);
        sms.setImageString("./icons/meusem4.png");
        srd.setSymbol(sms);
        // without setting layer capabilities, the points will not
        // display (but the toc entry will still appear)
        XYLayerCapabilities lc = new XYLayerCapabilities();
        setCapabilities(lc);
    }

    //=========================================================================
    // FUNCTION: createFeaturesAndFields()
    // DESCRIPTION: This creates the main data structure used by the XYTool to
    //              create the features in the map and attribute table.
    //=========================================================================
    private void createFeaturesAndFields(BasePointsArray bpa,Map map,Vector fieldNames, Vector fieldValues) {
        featureVector = new java.util.Vector();
        fields = new BaseFields();
        createDbfFields(fieldNames);
        int j = 0;
        for(int i=0;i<bpa.size();i++) {
            BaseFeature feature = new BaseFeature();
            feature.setFields(fields);
            com.esri.mo2.cs.geom.Point p = new com.esri.mo2.cs.geom.Point(bpa.getPoint(i));
            feature.setValue(0,p);
            feature.setValue(1,new Integer(i));
            feature.setValue(2, (double) fieldValues.elementAt(j));
            feature.setValue(3, (double) fieldValues.elementAt(++j));
            feature.setValue(4, (String) fieldValues.elementAt(++j));
            feature.setValue(5, (String) fieldValues.elementAt(++j));
            feature.setValue(6, (String) fieldValues.elementAt(++j));
            feature.setValue(7, (String) fieldValues.elementAt(++j));
            feature.setValue(8, (String) fieldValues.elementAt(++j));
            feature.setValue(9, (String) fieldValues.elementAt(++j));
            feature.setDataID(new BaseDataID("MyPoints",i));
            featureVector.addElement(feature);
            j++;
        }
    }

    //=========================================================================
    // FUNCTION: createDbfFields()
    // DESCRIPTION: This setups the attribute table, names the columns and
    //              defines the type of each.
    //=========================================================================
    private void createDbfFields(Vector<String> fieldNames) {
        fields.addField(new BaseField("#SHAPE#",Field.ESRI_SHAPE,0,0));
        fields.addField(new BaseField("ID",java.sql.Types.INTEGER,9,0));
        fields.addField(new BaseField(fieldNames.get(0), Types.DOUBLE, 20, 5));
        fields.addField(new BaseField(fieldNames.get(1), Types.DOUBLE, 20, 5));
        fields.addField(new BaseField(fieldNames.get(2),java.sql.Types.VARCHAR,64,0));
        fields.addField(new BaseField(fieldNames.get(3),java.sql.Types.VARCHAR,64,0));
        fields.addField(new BaseField(fieldNames.get(4),java.sql.Types.VARCHAR,20,0));
        fields.addField(new BaseField(fieldNames.get(5),java.sql.Types.VARCHAR,10,0));
        fields.addField(new BaseField(fieldNames.get(6),java.sql.Types.VARCHAR,100,0));
        fields.addField(new BaseField(fieldNames.get(7),java.sql.Types.VARCHAR,200,0));

    }

    //=========================================================================
    // FUNCTION: BaseFeatureClass()
    // DESCRIPTION: This function returns the BaseFeature class.
    //=========================================================================
    public BaseFeatureClass getFeatureClass(String name,BasePointsArray bpa){
        com.esri.mo2.map.mem.MemoryFeatureClass featClass = null;
        try {
            featClass = new com.esri.mo2.map.mem.MemoryFeatureClass(MapDataset.POINT,fields);
        } catch (IllegalArgumentException iae) {}
        featClass.setName(name);
        for (int i=0;i<bpa.size();i++) {
            featClass.addFeature((Feature) featureVector.elementAt(i));
        }
        return featClass;
    }

    //=========================================================================
    // FUNCTION: XYLayerCapabilities()
    // DESCRIPTION: This function sets some properties of the new feature layer
    //=========================================================================
    private final class XYLayerCapabilities
            extends com.esri.mo2.map.dpy.LayerCapabilities {

        XYLayerCapabilities() {
            for (int i=0;i<this.size(); i++) {
                setAvailable(this.getCapabilityName(i),true);
                setEnablingAllowed(this.getCapabilityName(i),true);
                getCapability(i).setEnabled(true);
            }
        }
    }
}

class AttrTab extends JDialog {
    JPanel panel1 = new JPanel();
    com.esri.mo2.map.dpy.Layer layer = QuickStartHelp.layer4;
    JTable jtable = new JTable(new MyTableModel());
    JScrollPane scroll = new JScrollPane(jtable);

    public AttrTab() throws IOException {
        setBounds(70, 70, 450, 350);
        setTitle("Attribute Table");
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                setVisible(false);
            }
        });
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        // next line necessary for horiz scrollbar to work
        jtable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        TableColumn tc = null;
        int numCols = jtable.getColumnCount();
        //jtable.setPreferredScrollableViewportSize(
        //new java.awt.Dimension(440,340));
        for (int j = 0; j < numCols; j++) {
            tc = jtable.getColumnModel().getColumn(j);
            tc.setMinWidth(50);
        }
        getContentPane().add(scroll, BorderLayout.CENTER);
    }
}

class MyTableModel extends AbstractTableModel {
    // the required methods to implement are getRowCount,
// getColumnCount, getValueAt
    com.esri.mo2.map.dpy.Layer layer = QuickStartHelp.layer4;

    MyTableModel() {
        qfilter.setSubFields(fields);
        com.esri.mo2.data.feat.Cursor cursor = flayer.search(qfilter);
        while (cursor.hasMore()) {
            ArrayList inner = new ArrayList();
            Feature f = (com.esri.mo2.data.feat.Feature) cursor.next();
            inner.add(0, String.valueOf(row));
            for (int j = 1; j < fields.getNumFields(); j++) {
                inner.add(f.getValue(j).toString());
            }
            data.add(inner);
            row++;
        }
    }

    FeatureLayer flayer = (FeatureLayer) layer;
    FeatureClass fclass = flayer.getFeatureClass();
    String columnNames[] = fclass.getFields().getNames();
    ArrayList data = new ArrayList();
    int row = 0;
    int col = 0;
    BaseQueryFilter qfilter = new BaseQueryFilter();
    Fields fields = fclass.getFields();

    public int getColumnCount() {
        return fclass.getFields().getNumFields();
    }

    public int getRowCount() {
        return data.size();
    }

    public String getColumnName(int colIndx) {
        return columnNames[colIndx];
    }

    public Object getValueAt(int row, int col) {
        ArrayList temp = new ArrayList();
        temp = (ArrayList) data.get(row);
        return temp.get(col);
    }
}

class CreateShapeDialog extends JDialog {
    String name = "";
    String path = "";
    JButton ok = new JButton("OK");
    JButton cancel = new JButton("Cancel");
    JTextField nameField = new JTextField("enter layer name here, then hit ENTER", 25);
    com.esri.mo2.map.dpy.FeatureLayer selectedlayer;
    ActionListener lis = new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
            Object o = ae.getSource();
            if (o == nameField) {
                name = nameField.getText().trim();
                path = ((ShapefileFolder) (QuickStartHelp.layer4.getLayerSource())).getPath();
                System.out.println(path + "    " + name);
            } else if (o == cancel)
                setVisible(false);
            else {
                try {
                    ShapefileWriter.writeFeatureLayer(selectedlayer, path, name, 2);
                } catch (Exception e) {
                    System.out.println("write error");
                }
                setVisible(false);
            }
        }
    };

    JPanel panel1 = new JPanel();
    JLabel centerlabel = new JLabel();

    //centerlabel;
    CreateShapeDialog(com.esri.mo2.map.dpy.FeatureLayer layer5) {
        selectedlayer = layer5;
        setBounds(40, 350, 450, 150);
        setTitle("Create new shapefile?");
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                setVisible(false);
            }
        });
        nameField.addActionListener(lis);
        ok.addActionListener(lis);
        cancel.addActionListener(lis);
        String s = "<HTML> To make a new shapefile from the new layer, enter<BR>" +
                "the new name you want for the layer and click OK.<BR>" +
                "You can then add it to the map in the usual way.<BR>" +
                "Click ENTER after replacing the text with your layer name";
        centerlabel.setHorizontalAlignment(JLabel.CENTER);
        centerlabel.setText(s);
        getContentPane().add(centerlabel, BorderLayout.CENTER);
        panel1.add(nameField);
        panel1.add(ok);
        panel1.add(cancel);
        getContentPane().add(panel1, BorderLayout.SOUTH);
    }
}

class CreateXYShapeDialog extends JDialog {
    String name = "";
    Map map;
    String path = "";
    JFileChooser jfc = new JFileChooser();
    JButton ok = new JButton("OK");
    JButton cancel = new JButton("Cancel");
    JButton choose = new JButton("browse");
    JTextField nameField = new JTextField("enter layer name here, then hit ENTER", 35);
    com.esri.mo2.map.dpy.FeatureLayer XYlayer;
    ActionListener lis = new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
            Object o = ae.getSource();
            if (o == choose) {
                jfc.setDialogTitle("Please choose Folder!");
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal;
                do {
                    returnVal = jfc.showOpenDialog((Component) ae.getSource());
                } while (returnVal != JFileChooser.APPROVE_OPTION);
                File file = jfc.getSelectedFile();
                try {
                    path = file.toString();
                } catch (Exception ex) {
                    System.out.println("problem accessing file" + file.getAbsolutePath());
                }
            } else if (o == nameField) {
                name = nameField.getText().trim();
                //path = ((ShapefileFolder)(QuickStartHelp.layer4.getLayerSource())).getPath();
                System.out.println(path + "    " + name);
            } else if (o == cancel)
                setVisible(false);
            else {  // ok button clicked
                boolean shouldCreate = false;
                try {
                    ShapefileWriter.writeFeatureLayer(XYlayer, path, name, 0);
                    JOptionPane.showMessageDialog(null, "ShapeFile created successfully");
                    setVisible(false);
                    shouldCreate = true;
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Failed to create Shape File");
                }
                if (shouldCreate) {
                    String prompt = "Would like to add the new shape file to the map?";
                    int dialogResult = JOptionPane.showConfirmDialog(null, prompt, "Warning", JOptionPane.YES_NO_OPTION);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        Layer layer1 = new Layer();
                        layer1.setRendererDetails("0;255;0;0");
                        String dataPath = path + "/" + name;
                        layer1.setDataset("0;" + dataPath);
                        map.add(layer1);
                    }
                }
            }
        }
    };

    JPanel panel1 = new JPanel();
    JPanel panel2 = new JPanel();
    JLabel centerlabel = new JLabel();

    //centerlabel;
    CreateXYShapeDialog(com.esri.mo2.map.dpy.FeatureLayer layer5, Map map) {
        XYlayer = layer5;
        this.map = map;
        setBounds(40, 250, 600, 300);
        setTitle("Create new shapefile?");
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                setVisible(false);
            }
        });
        nameField.addActionListener(lis);
        //pathField.addActionListener(lis);
        choose.addActionListener(lis);
        ok.addActionListener(lis);
        cancel.addActionListener(lis);
        String s = "<HTML> To make a new shapefile from the new layer, enter<BR>" +
                "the new name you want for the layer and hit ENTER.<BR>" +
                "then browse to the folder you want to use <BR>" +
                "and click choose <BR>" +
                "You will be prompted to add it to the map immediately .<BR>" +
                "Click ENTER after replacing the text with your layer name";
        centerlabel.setHorizontalAlignment(JLabel.CENTER);
        centerlabel.setText(s);
        //getContentPane().add(centerlabel,BorderLayout.CENTER);
        panel1.add(centerlabel);
        panel1.add(nameField);
        panel1.add(choose);
        panel2.add(ok);
        panel2.add(cancel);
        getContentPane().add(panel2, BorderLayout.SOUTH);
        getContentPane().add(panel1, BorderLayout.CENTER);
    }
}

class HelpDialog extends JDialog {
    JTextArea helptextarea;

    public HelpDialog(String inputText) throws IOException {
        setBounds(70, 70, 450, 250);
        setTitle("Help");
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                setVisible(false);
            }
        });
        helptextarea = new JTextArea(inputText, 7, 40);
        JScrollPane scrollpane = new JScrollPane(helptextarea);
        helptextarea.setEditable(false);
        getContentPane().add(scrollpane, "Center");
    }
}

class Arrow extends Tool {
    Arrow() { // undo measure tool residue
        QuickStartHelp.milesLabel.setText("DIST   0 mi   ");
        QuickStartHelp.kmLabel.setText("   0 km    ");
        QuickStartHelp.map.remove(QuickStartHelp.acetLayer);
        QuickStartHelp.acetLayer = null;
        QuickStartHelp.map.repaint();
    }
}

class Flash extends Thread {
    Legend legend;

    Flash(Legend legendin) {
        legend = legendin;
    }

    public void run() {
        for (int i = 0; i < 12; i++) {
            try {
                Thread.sleep(500);
                legend.toggleSelected();
            } catch (Exception e) {
            }
        }
    }
}

class DistanceTool extends DragTool {
    int startx, starty, endx, endy, currx, curry;
    com.esri.mo2.cs.geom.Point initPoint, endPoint, currPoint;
    double distance;

    public void mousePressed(MouseEvent me) {
        startx = me.getX();
        starty = me.getY();
        initPoint = QuickStartHelp.map.transformPixelToWorld(me.getX(), me.getY());
    }

    public void mouseReleased(MouseEvent me) {
        // now we create an acetatelayer instance and draw a line on it
        endx = me.getX();
        endy = me.getY();
        endPoint = QuickStartHelp.map.transformPixelToWorld(me.getX(), me.getY());
        distance = (69.44 / (2 * Math.PI)) * 360 * Math.acos(
                Math.sin(initPoint.y * 2 * Math.PI / 360)
                        * Math.sin(endPoint.y * 2 * Math.PI / 360)
                        + Math.cos(initPoint.y * 2 * Math.PI / 360)
                        * Math.cos(endPoint.y * 2 * Math.PI / 360)
                        * (Math.abs(initPoint.x - endPoint.x) < 180 ?
                        Math.cos((initPoint.x - endPoint.x) * 2 * Math.PI / 360) :
                        Math.cos((360 - Math.abs(initPoint.x - endPoint.x)) * 2 * Math.PI / 360)));
        System.out.println(distance);
        QuickStartHelp.milesLabel.setText("DIST: " + new Float((float) distance).toString() + " mi  ");
        QuickStartHelp.kmLabel.setText(new Float((float) (distance * 1.6093)).toString() + " km");
        if (QuickStartHelp.acetLayer != null)
            QuickStartHelp.map.remove(QuickStartHelp.acetLayer);
        QuickStartHelp.acetLayer = new AcetateLayer() {
            public void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
                Line2D.Double line = new Line2D.Double(startx, starty, endx, endy);
                g2d.setColor(new Color(0, 0, 250));
                g2d.draw(line);
            }
        };
        Graphics g = super.getGraphics();
        QuickStartHelp.map.add(QuickStartHelp.acetLayer);
        QuickStartHelp.map.redraw();
    }

    public void cancel() {
    }

    ;
}