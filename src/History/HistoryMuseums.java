//package History;//===========================================================================
//// IMPORTS
////===========================================================================
//import com.esri.mo2.cs.geom.*;
//import com.esri.mo2.data.feat.*;
//import com.esri.mo2.file.shp.*;
//import com.esri.mo2.map.dpy.FeatureLayer;
//import com.esri.mo2.map.dpy.BaseFeatureLayer;
//import com.esri.mo2.map.dpy.Layerset;
//import com.esri.mo2.map.draw.* ;
//import com.esri.mo2.ui.bean.*;
//import com.esri.mo2.ui.bean.Map;
//import com.esri.mo2.ui.dlg.*;
//import com.esri.mo2.ui.ren.LayerProperties;
//import com.esri.mo2.ui.tb.ZoomPanToolBar;
//import com.esri.mo2.ui.tb.SelectionToolBar;
//import java.awt.*;
//import java.awt.event.*;
//import java.awt.geom.*;
//import java.io.*;
//import java.sql.*;
//import java.util.*;
//import javax.swing.*;
//import javax.swing.table.AbstractTableModel;
//import javax.swing.table.TableColumn;
//
//
////===========================================================================
//// CLASS: History.HistoryMeusems
//// DESCRIPTION: Main class for application.
////===========================================================================
//public class HistoryMuseums extends JFrame {
//
//	//Global variables to use throughout the program.
//	static boolean fullMap = true;
//	static boolean helpToolOn = true;
//	int activeLayerIndex;
//	String s1 = "./esri/MOJ20/Samples/Data/USA/San Diego.shp";
//	String s2 = "./Resources/data/History Museum.shp";
//	private ArrayList<String> helpText = new ArrayList<String>(3);
//	static HelpTool helptool = new HelpTool() ;
//
//	//Action listener setup
//	ActionListener lis;
//	ActionListener LayerListener;
//	ActionListener layerControlListener;
//	ComponentListener complistener;
//
//	// Object Variables
//	static Map map = new Map();
//	Legend legend;
//	Legend legend2;
//	Layer layer2 = new Layer();
//	Layer layer = new Layer();
//	Layer layer3 = new Layer();
//	static AcetateLayer acetLayer;
//	static com.esri.mo2.map.dpy.Layer layer4;
//	com.esri.mo2.map.dpy.Layer activeLayer;
//	Toc toc = new Toc();
//	TocAdapter mytocadapter;
//	static Envelope env;
//
//	// Setup the Menu items here...
//	JMenuBar menu = new JMenuBar();
//	JMenu file = new JMenu("File");
//	JMenu theme = new JMenu("Theme");
//	JMenu layercontrol = new JMenu("Layer Control");
//	JMenu help = new JMenu("Help");
//	JMenu help2 = new JMenu("Help Topics");
//
//	// Setup ImageIcons for use later (mostly for code readabilty)
//	ImageIcon cplyIco = new ImageIcon("./Resources//icons/add_blue.png");
//	ImageIcon attrIco = new ImageIcon("./Resources/icons/table_view.png") ;
//	ImageIcon demoteIco = new ImageIcon("./Resources/icons/demote.png") ;
//	ImageIcon printIco = new ImageIcon("./Resources/icons/print-icon.png") ;
//	ImageIcon addThemeIco = new ImageIcon("./Resources/icons/addTheme.jpg") ;
//	ImageIcon deleteIco = new ImageIcon("./Resources/icons/delete.png") ;
//	ImageIcon legendEIco = new ImageIcon("./Resources/icons/properties.png") ;
//	ImageIcon helpIco = new ImageIcon("./Resources/icons/help.png");
//	static ImageIcon promoteIco = new ImageIcon("./Resources/icons/promote.png") ;
//
//	// Associate the above ImageIcons to JMenuIcons now...
//	JMenuItem attribitem = new JMenuItem("Open Attribute Table", attrIco);
//	JMenuItem cpolylayer  = new JMenuItem("Create Polygon Layer From Selection",
//			cplyIco);
//	JMenuItem cpointlayer  = new JMenuItem("Create Point Layer From Selection",
//			cplyIco);
//	JMenuItem demoteitem = new JMenuItem("Demote Selected Layer", demoteIco);
//	JMenuItem printitem = new JMenuItem("Print", printIco);
//	JMenuItem alitem = new JMenuItem("Add Layer", addThemeIco);
//	JMenuItem remlyritem = new JMenuItem("Remove Layer", deleteIco );
//	JMenuItem propsitem = new JMenuItem("Legend Editor", legendEIco);
//	static JMenuItem pitem = new JMenuItem("Promote Selected Layer", promoteIco);
//	JMenuItem aboutitem = new JMenuItem("About", helpIco);
//	JMenuItem contactusitem = new JMenuItem("Contact Us", helpIco);
//	JMenuItem helptoolitem = new JMenuItem("Help Tool", helpIco);
//	JMenuItem h2toc = new JMenuItem("Table of Contents Help", helpIco);
//	JMenuItem h2le = new JMenuItem("Legend Editor Help", helpIco);
//	JMenuItem h2lc = new JMenuItem("Layer Control Help", helpIco);
//
//	// Setup the Toolbars next...
//	ZoomPanToolBar zptb = new ZoomPanToolBar();
//	static SelectionToolBar stb = new SelectionToolBar();
//	JToolBar jtb = new JToolBar();
//
//	// Setup the Statusbar next...
//	JLabel statusLabel = new JLabel("status bar    LOC");
//	static JLabel milesLabel = new JLabel("   DIST:  0 mi    ");
//	static JLabel kmLabel = new JLabel("  0 km    ");
//	java.text.DecimalFormat df = new java.text.DecimalFormat("0.000");
//
//	// Setup the CustomToolbar here...
//	JPanel jpanel = new JPanel();
//	JPanel jpanel2 = new JPanel();
//	JButton printB = new JButton(new ImageIcon("./Resources/icons/print.png"));
//	JButton addLayerB = new JButton(addThemeIco);
//	JButton pointerB = new JButton(new ImageIcon("./Resources/icons/pointer2.png"));
//	JButton distB = new JButton(new ImageIcon("./Resources/icons/distance.png"));
//	JButton csvB = new JButton(new ImageIcon("./Resources/icons/csv.png"));
//	JButton hotLinkB = new JButton(new ImageIcon("./Resources/icons/hotlink.png"));
//	JButton helpB = new JButton(new ImageIcon("./Resources/icons/help2.png"));
//
//	// Setup the hotlink tool here...
//	Toolkit tk = Toolkit.getDefaultToolkit();
//	Image bolt = tk.getImage("./Resources/icons/pinpoint.png");
//	java.awt.Cursor boltCursor = tk.createCustomCursor(bolt, new java.awt.Point(6,30),"bolt");
//
//	// PickAdapter for the hotlink tool...
//	MyPickAdapter pickListener = new MyPickAdapter();
//	Identify hotlink = new Identify();
//
//	//===========================================================================
//	// CLASS: MyPickAdapter
//	// DESCRIPTION: hotlink tool listener implemented by Indentity class.
//	//===========================================================================
//	class MyPickAdapter implements PickListener {
//		MouseEvent MouseEvent ;
//		int x ;
//		int y ;
//
//		public void beginPick(PickEvent pe){
//			System.out.println("hotLink start");
//			MouseEvent = pe.getMouseEvent() ;
//			x = MouseEvent.getX() ;
//			y = MouseEvent.getY() ;
//		}
//
//
//		//=========================================================================
//		// FUNCTION: foundData()
//		// DESCRIPTION: This code runs when the user has clicked near a feature on
//		//              the active layer.
//		//=========================================================================
//		public void foundData(PickEvent pe){
//			String fsvalue ;
//			int fivalue = 0;
//
//			System.out.println("Coordinates on screen " + x + "," + y);
//			com.esri.mo2.cs.geom.Point point = map.transformPixelToWorld(x,y);
//			com.esri.mo2.data.feat.Cursor cursor = pe.getCursor();
//
//			// Pull feature attribute information and pass to hotlink tool...
//			Row featureRow = null ;
//			while (cursor.hasMore()) {
//				featureRow = (com.esri.mo2.data.feat.Row)cursor.next();
//				fsvalue = featureRow.getDisplayValue(1) ;
//				fivalue = Integer.parseInt(fsvalue);
//			}
//
//			// Skip the sdcounty base layer which has GIS data...
//			String actlayername = map.getLayer(activeLayerIndex).getName();
//			if ( ! new String("San Diego").equals(actlayername) ) {
//
//				// Lookup this point in the array of points in our current layer...
//				// If True, call function to invoke JPanel Window Create...
//				try {
//					HotPick hotpick = new HotPick(fivalue, featureRow, activeLayerIndex, map);
//					hotpick.setVisible(true);
//				} catch(Exception e){
//					System.err.println(e);
//					e.printStackTrace();
//				}
//			}
//		}
//
//
//		//=========================================================================
//		// FUNCTION: endPick()
//		// DESCRIPTION: This code runs after the user has clicked.  It resets the
//		//              hotlink tool to null and forces the user to click it again.
//		//=========================================================================
//		public void endPick(PickEvent pe){
//			System.out.println("end pick");
//			hotlink.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
//			map.setSelectedTool(null);
//		}
//	} ;
//
//
//	//=========================================================================
//	// FUNCTION: History.HistoryMeusems()
//	// DESCRIPTION: Main Application Constructor
//	//=========================================================================
//	public HistoryMuseums() {
//
//		// Top level variables...
//		super("History Museums");
//		helpToolOn = false ;
//		this.setBounds(50,50,900,900);
//		zptb.setMap(map);
//		stb.setMap(map);
//		setJMenuBar(menu);
//
//
//		// A special zoom listener to handle setting full map bool to false...
//		ActionListener lisZoom = ae -> fullMap = false;
//
//		// A special zoom listener to handle setting full map bool to true...
//		ActionListener lisFullExt = ae -> fullMap = true;
//
//		// Zoom tool bar button configuration (and add listeners from above) here...
//		JButton zoomInButton = (JButton)zptb.getActionComponent("ZoomIn");
//		JButton zoomFullExtentButton = (JButton)zptb.getActionComponent("ZoomToFullExtent");
//		JButton zoomToSelectedLayerButton = (JButton)zptb.getActionComponent("ZoomToSelectedLayer");
//		zoomInButton.addActionListener(lisZoom);
//		zoomFullExtentButton.addActionListener(lisFullExt);
//		zoomToSelectedLayerButton.addActionListener(lisZoom);
//
//		// Create & add resize component listener...
//		complistener = new ComponentAdapter () {
//			public void componentResized(ComponentEvent ce) {
//				if(fullMap) {
//					map.setExtent(env);
//					map.zoom(1.0);
//					map.redraw();
//				}
//			}
//		};
//		addComponentListener(complistener);
//		// MouseAdapter to add the help tool messages for the buttons
//	    MouseAdapter lisHelpTool = new MouseAdapter() {
//	    	public void mousePressed(MouseEvent me) {
//	    		if (SwingUtilities.isRightMouseButton(me) && helpToolOn) {
//	    			try {
//						HelpDialog hd = null;
//	    				if(me.getSource() == printB){
//							hd = new HelpDialog(helpText.get(9)) ;
//						}
//						else if(me.getSource() == addLayerB ){
//							hd = new HelpDialog(helpText.get(10)) ;
//						}
//						else if(me.getSource() == pointerB){
//							hd = new HelpDialog(helpText.get(11)) ;
//						}
//						else if(me.getSource() == distB){
//							hd = new HelpDialog(helpText.get(5)) ;
//						}
//						else if(me.getSource() == csvB){
//							hd = new HelpDialog(helpText.get(7)) ;
//						}
//						else if(me.getSource() == hotLinkB){
//							hd = new HelpDialog(helpText.get(8)) ;
//						}
//						else if(me.getSource() ==helpB){
//							hd = new HelpDialog(helpText.get(3)) ;
//						}
//	    				hd.setVisible(true);
//	    			} catch(IOException e) {
//	    				System.err.println(e);
//						e.printStackTrace();
//	    			}
//	    		}
//	    	}
//	    };
//	    printB.addMouseListener(lisHelpTool);
//	    addLayerB.addMouseListener(lisHelpTool);
//	    pointerB.addMouseListener(lisHelpTool);
//	    distB.addMouseListener(lisHelpTool);
//	    csvB.addMouseListener(lisHelpTool);
//	    hotLinkB.addMouseListener(lisHelpTool);
//	    helpB.addMouseListener(lisHelpTool);
//
//		// Custom Tool Bar case statement next...
//		lis = new ActionListener() {
//			public void actionPerformed(ActionEvent ae) {
//				Object source = ae.getSource();
//				if (source == printB || source instanceof JMenuItem ) {
//					com.esri.mo2.ui.bean.Print mapPrint = new com.esri.mo2.ui.bean.Print();
//					mapPrint.setMap(map);
//					mapPrint.doPrint();
//				} else if (source == pointerB) {
//					Arrow arrow = new Arrow();
//					map.setSelectedTool(arrow);
//				} else if (source == distB) {
//					DistanceTool distanceTool = new DistanceTool();
//					map.setSelectedTool(distanceTool);
//				} else if (source == csvB) {
//					try {
//						AddXYtheme addXYtheme = new AddXYtheme();
//						addXYtheme.setMap(map);
//						addXYtheme.setVisible(false);
//						map.redraw();
//					} catch (IOException e) { }
//				} else if (source == hotLinkB) {
//					hotlink.setCursor(boltCursor);
//					map.setSelectedTool(hotlink);
//				} else if (source == helpB){
//					helpToolOn = true ;
//					map.setSelectedTool(helptool);
//				} else {
//					try {
//						AddLyrDialog aldlg = new AddLyrDialog();
//						aldlg.setMap(map);
//						aldlg.setVisible(true);
//					} catch(IOException e) { }
//				}
//			}
//		};
//	    hotlink.setPickWidth(60);
//		hotlink.addPickListener(pickListener);
//	    csvB.addActionListener(lis);
//	    hotLinkB.addActionListener(lis);
//	    printB.addActionListener(lis);
//	    pointerB.addActionListener(lis);
//	    distB.addActionListener(lis);
//	    addLayerB.addActionListener(lis);
//	    printitem.addActionListener(lis);
//	    helpB.addActionListener(lis);
//
//		// Create and configure the promote and demote listener now...
//		layerControlListener = ae -> {
//            String source = ae.getActionCommand();
//            System.out.println(activeLayerIndex+" active index");
//            if (source == "Promote Selected Layer") {
//                map.getLayerset().moveLayer(activeLayerIndex,++activeLayerIndex);
//            } else {
//                map.getLayerset().moveLayer(activeLayerIndex,--activeLayerIndex);
//                enableDisableButtons();
//                map.redraw();
//            }
//        };
//	    pitem.addActionListener(layerControlListener);
//	    demoteitem.addActionListener(layerControlListener);
//
//
//
//		// Main program listener is created and attached to the
//	    // buttons in the toolbar now...
//		LayerListener = ae -> {
//            Object source = ae.getSource();
//            if (source instanceof JMenuItem) {
//                String arg = ae.getActionCommand();
//                if(arg == "Add Layer") {
//                    try {
//                        AddLyrDialog aldlg = new AddLyrDialog();
//                        aldlg.setMap(map);
//                        aldlg.setVisible(true);
//                    } catch(IOException e){}
//                } else if(arg == "About") {
//                    AboutBox ab = new AboutBox();
//                    ab.setProductName("History Meusems");
//                    ab.setProductVersion("1.0");
//                    ab.setVisible(true) ;
//                } else if (arg == "Contact Us"){
//                    try {
//                        String s = "\n\nInquiries should be addressed to: " ;
//                        s = s + "anas.khafagi@gmail.com." ;
//                        HelpDialog hd = new HelpDialog(s) ;
//                        hd.setVisible(true);
//                    } catch (IOException e) {}
//                } else if (arg == "Help Tool"){
//                    try {
//                        HelpDialog hd = new HelpDialog(helpText.get(3)) ;
//                        hd.setVisible(true);
//                    } catch (IOException e) {}
//                } else if (arg == "Table of Contents Help"){
//                    try {
//                        HelpDialog hd = new HelpDialog(helpText.get(0)) ;
//                        hd.setVisible(true);
//                    } catch (IOException e) {}
//                } else if (arg == "Legend Editor Help"){
//                    try {
//                        HelpDialog hd = new HelpDialog(helpText.get(1)) ;
//                        hd.setVisible(true);
//                    } catch (IOException e) {}
//                } else if (arg == "Layer Control Help"){
//                    try {
//                        HelpDialog hd = new HelpDialog(helpText.get(2)) ;
//                        hd.setVisible(true);
//                    } catch (IOException e) {}
//                } else if(arg == "Remove Layer") {
//                    try {
//                        com.esri.mo2.map.dpy.Layer dpylayer = legend.getLayer();
//                        map.getLayerset().removeLayer(dpylayer);
//                        map.redraw();
//                        remlyritem.setEnabled(false);
//                        propsitem.setEnabled(false);
//                        attribitem.setEnabled(false);
//                        pitem.setEnabled(false);
//                        demoteitem.setEnabled(false);
//                        stb.setSelectedLayer(null);
//                        stb.setSelectedLayers(null);
//                        zptb.setSelectedLayer(null);
//                    } catch(Exception e) {}
//                } else if(arg == "Legend Editor") {
//                    LayerProperties lp = new LayerProperties();
//                    lp.setLegend(legend);
//                    lp.setSelectedTabIndex(0);
//                    lp.setVisible(true);
//                } else if (arg == "Open Attribute Table") {
//                    try {
//                        layer4 = legend.getLayer();
//                        AttrTab attrtab = new AttrTab();
//                        attrtab.setVisible(true);
//                    } catch(IOException ioe){}
//                } else if (arg=="Create Polygon Layer From Selection" || arg=="Create Point Layer From Selection" ) {
//					int layertype ;
//                    BaseSimpleRenderer sbr = new BaseSimpleRenderer();
//
//                    // For Polygons
//                    SimpleFillSymbol simplepolysymbol = new SimpleFillSymbol();
//                    simplepolysymbol.setSymbolColor(new Color(255,255,0));
//                    simplepolysymbol.setType(SimpleFillSymbol.FILLTYPE_SOLID);
//                    simplepolysymbol.setBoundary(true);
//
//                    // For Points
//                    SimpleMarkerSymbol simplepointsymbol = new SimpleMarkerSymbol();
//                    simplepointsymbol.setType( SimpleMarkerSymbol.STAR_MARKER );
//                    simplepointsymbol.setSymbolColor(new Color(255,255,0));
//                    simplepointsymbol.setWidth(12);
//
//                    if ( arg =="Create Polygon Layer From Selection") {
//                        layertype = 2;
//                    } else {
//                        layertype = 0;
//                    }
//
//                    layer4 = legend.getLayer();
//                    FeatureLayer flayer2 = (FeatureLayer)layer4;
//                    System.out.println("has selected " + flayer2.hasSelection());
//                    if (flayer2.hasSelection()) {
//                        SelectionSet selectset = flayer2.getSelectionSet();
//                        FeatureLayer selectedlayer =
//                                flayer2.createSelectionLayer(selectset);
//                        sbr.setLayer(selectedlayer);
//                        if ( layertype == 2 ) {
//                            sbr.setSymbol(simplepolysymbol);
//                        } else {
//                            sbr.setSymbol(simplepointsymbol);
//                        }
//                        selectedlayer.setRenderer(sbr);
//                        Layerset layerset = map.getLayerset();
//
//                        layerset.addLayer(selectedlayer);
//
//                        if(stb.getSelectedLayers() != null) {
//                            pitem.setEnabled(true);
//                        }
//                        try {
//                            legend2 = toc.findLegend(selectedlayer);
//                        } catch (Exception e) {}
//
//                        CreateShapeDialog csd = new CreateShapeDialog(selectedlayer,layertype);
//                        csd.setVisible(true);
//                        Flash flash = new Flash(legend2);
//                        flash.start();
//                        map.redraw();
//                    }
//                }
//            }
//        };
//	    alitem.addActionListener(LayerListener);
//	    propsitem.addActionListener(LayerListener);
//	    attribitem.addActionListener(LayerListener);
//	    cpolylayer.addActionListener(LayerListener);
//	    remlyritem.addActionListener(LayerListener);
//	    cpointlayer.addActionListener(LayerListener);
//	    aboutitem.addActionListener(LayerListener);
//	    contactusitem.addActionListener(LayerListener);
//	    helptoolitem.addActionListener(LayerListener);
//	    h2toc.addActionListener(LayerListener);
//	    h2le.addActionListener(LayerListener);
//	    h2lc.addActionListener(LayerListener);
//
//
//		// TOC Listener to set active layer here...
//		toc.setMap(map);
//		mytocadapter = new TocAdapter() {
//			public void click(TocEvent e) {
//				System.out.println(activeLayerIndex + " active layer index");
//				legend = e.getLegend();
//				activeLayer = legend.getLayer();
//				stb.setSelectedLayer(activeLayer);
//				zptb.setSelectedLayer(activeLayer);
//				activeLayerIndex = map.getLayerset().indexOf(activeLayer);
//				com.esri.mo2.map.dpy.Layer[] layers = {activeLayer};
//				hotlink.setSelectedLayers(layers);
//
//				System.out.println(activeLayerIndex + " active index");
//				remlyritem.setEnabled(true);
//				propsitem.setEnabled(true);
//				attribitem.setEnabled(true);
//				enableDisableButtons();
//			}
//		};
//		toc.addTocListener(mytocadapter);
//
//		map.addMouseMotionListener(
//			new MouseMotionAdapter() {
//				public void mouseMoved(MouseEvent me) {
//					com.esri.mo2.cs.geom.Point worldPoint = null;
//					if (map.getLayerCount() > 0) {
//						worldPoint = map.transformPixelToWorld(me.getX(),me.getY());
//						String s = "X:"+df.format(worldPoint.getX())+" "+
//							"Y:"+df.format(worldPoint.getY());
//						statusLabel.setText(s);
//					} else {
//						statusLabel.setText("X:0.000 Y:0.000");
//					}
//				}
//			}
//		);
//
//		// Default application properties for default application behaviors...
//		remlyritem.setEnabled(false);
//		propsitem.setEnabled(false);
//		attribitem.setEnabled(false);
//		pitem.setEnabled(false);
//	    demoteitem.setEnabled(false);
//
//	    // Build the File menu now...
//	    file.add(alitem);
//	    file.add(printitem);
//	    file.add(remlyritem);
//	    file.add(propsitem);
//
//	    // Build the Theme menu now...
//	    theme.add(attribitem);
//	    theme.add(cpolylayer);
//	    theme.add(cpointlayer);
//
//	    // Build the Layer Control menu now...
//	    layercontrol.add(pitem);
//	    layercontrol.add(demoteitem);
//
//	    // Build the Help menu now...
//	    setuphelpText();
//	    help2.add(h2toc);
//	    help2.add(h2le);
//	    help2.add(h2lc);
//	    help.add(help2);
//	    help.add(helptoolitem);
//	    help.add(contactusitem);
//	    help.add(aboutitem);
//
//	    // Now add those menus to the menubar itself...
//	    menu.add(file);
//	    menu.add(theme);
//	    menu.add(layercontrol);
//	    menu.add(help);
//
//	    // Add some helpful tool tips next...
//	    printB.setToolTipText("print map");
//	    addLayerB.setToolTipText("add layer");
//	    pointerB.setToolTipText("arrow tool");
//	    distB.setToolTipText("press-drag-release to measure a distance");
//	    csvB.setToolTipText("add a layer of points from a file");
//	    hotLinkB.setToolTipText("Click on a point to see more information");
//	    helpB.setToolTipText("click this button, "
//	    		+ "then right click any toolbar item for help");
//
//	    // Attach those buttons to the tool bar now...
//	    jtb.add(printB);
//	    jtb.add(addLayerB);
//	    jtb.add(pointerB);
//	    jtb.add(distB);
//	    jtb.add(csvB);
//	    jtb.add(hotLinkB);
//	    jtb.add(helpB);
//
//	    // Add these various objects to the jpanels now...
//	    jpanel.add(jtb);
//	    jpanel.add(zptb);
//	    jpanel.add(stb);
//	    jpanel2.add(statusLabel);
//	    jpanel2.add(milesLabel);
//	    jpanel2.add(kmLabel);
//
//	    // Add a window splitter for usability...
//	    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,toc,map) ;
//	    splitPane.setOneTouchExpandable(true) ;
//
//	    // Configure the layout for the jpanel and then  add the maps...
//	    getContentPane().add(map, BorderLayout.CENTER);
//	    getContentPane().add(jpanel,BorderLayout.NORTH);
//	    getContentPane().add(jpanel2,BorderLayout.SOUTH);
//	    addShapefileToMap(layer,s1);
//	    addShapefileToMap(layer2,s2);
////
//	    // Set base map color to red
//	    java.util.List list = toc.getAllLegends();
//	    com.esri.mo2.map.dpy.Layer lay1 = ((Legend)list.get(1)).getLayer();
//	    FeatureLayer flayer1 = (FeatureLayer)lay1;
//	    BaseSimpleRenderer bsr1 = (BaseSimpleRenderer)flayer1.getRenderer();
//		SimplePolygonSymbol sym = (SimplePolygonSymbol) bsr1.getSymbol();
//		sym.setPaint(AoFillStyle.getPaint(com.esri.mo2.map.draw.AoFillStyle.SOLID_FILL, new java.awt.Color(176, 86, 255)));
//		bsr1.setSymbol(sym);
//
//
//	    // Set base polygon for sdcounty to grey...
//	    com.esri.mo2.map.dpy.Layer lay2 = ((Legend)list.get(0)).getLayer();
//	    FeatureLayer flayer2 = (FeatureLayer)lay2;
//	    BaseSimpleRenderer bsr2 = (BaseSimpleRenderer)flayer2.getRenderer();
//		com.esri.mo2.map.draw.RasterMarkerSymbol sms = null; // for points
//		sms = new com.esri.mo2.map.draw.RasterMarkerSymbol();
//		sms.setSizeX(30);
//		sms.setSizeY(30);
//		sms.setImageString("./Resources/icons/meusem4.png");
//		bsr2.setSymbol(sms);
//
//		// Display the rest now...
//	    getContentPane().add(splitPane, BorderLayout.WEST);
//		com.esri.mo2.map.dpy.Layer[] layers = {map.getLayerset().layerAt(1)};
//		hotlink.setSelectedLayers(layers);
//		activeLayerIndex = 1;
//	    map.setExtent(env);
//		map.zoom(1.0);
//		map.redraw();
//	}
//
//
//	//=========================================================================
//	// FUNCTION: addShapefileToMap()
//	// DESCRIPTION: Adds a shp file to the map
//	//=========================================================================
//	private void addShapefileToMap(Layer layer, String s) {
//		String datapath = s;
//		layer.setDataset("0;"+datapath);
//		map.add(layer);
//	}
//
//
//	//=========================================================================
//	// FUNCTION: enableDisableButtons()
//	// DESCRIPTION: Depending on what's happening in the application these
//	//              global variables need to be set/reset accordingly.
//	//=========================================================================
//	private void enableDisableButtons() {
//		int layerCount = map.getLayerset().getSize();
//		if (layerCount < 2) {
//			pitem.setEnabled(false);
//			demoteitem.setEnabled(false);
//		} else if (activeLayerIndex == 0) {
//			demoteitem.setEnabled(false);
//			pitem.setEnabled(true);
//		} else if (activeLayerIndex == layerCount - 1) {
//			pitem.setEnabled(false);
//			demoteitem.setEnabled(true);
//		} else {
//			pitem.setEnabled(true);
//			demoteitem.setEnabled(true);
//		}
//	}
//
//
//	//=========================================================================
//	// FUNCTION: setuphelpText()
//	// DESCRIPTION: Just a helper function to handle strings and help for the
//	//              user
//	//=========================================================================
//	private void setuphelpText() {
//		String s0 = "The table of contents is to the left of the map.\n"
//	    + "Each entry is called a 'legend' and represents a map 'layer' or\n"
//	    + "'theme'.  If you click on a legend, that layer is called the\n"
//	    + "active layer, or selected layer.  Its display (rendering) properties\n"
//	    + "can be controlled using the Legend Editor, and the legends can be\n"
//	    + "reordered using Layer Control.  Both Legend Editor and Layer Control\n"
//	    + "are separate Help Topics.";
//		helpText.add(s0);
//		String s1 = "The Legend Editor is a menu item found under the File menu.\n"
//	    + "Given that a layer is selected by clicking on its legend in the table"
//	    + " of contents, clicking on Legend Editor will open a window giving you"
//	    + " choices about how to display that layer.  For example you can control"
//	    + " the color used to display the layer on the map, or whether to use"
//	    + " multiple colors";
//		helpText.add(s1);
//		String s2 = "Layer Control is a Menu on the menu bar.  If you have selected"
//		+ " a layer by clicking on a legend in the toc (table of contents) to the"
//		+ " left of the map, then the promote and demote tools will become"
//		+ " usable.  Clicking on promote will raise the selected legend one"
//		+ " position higher in the toc, and clicking on demote will lower that"
//		+ " legend one position in the toc.";
//		helpText.add(s2);
//		String s3 = "This tool will allow you to learn about certain other tools.\n"
//	    + "You begin with a standard left mouse button click on the Help Tool"
//	    + " itself.\nRIGHT click on another tool and a window may give you"
//	    + "information about the intended usage of the tool.\nClick on the"
//	    + " arrow tool to stop using the help tool.";
//		helpText.add(s3);
//		String s4 = "If you click on the Zoom In tool, and then click on the map,"
//		+ "you\nwill see a part of the map in greater detail.  You can zoom in"
//		+ " multiple times. You can also sketch a rectangular part of the map,"
//		+ " and zoom to that.  You can\nundo a Zoom In with a Zoom Out or"
//		+ " with a Zoom to Full Extent";
//		helpText.add(s4);
//		String s5 = "You must have a selected layer to use the Zoom to Active"
//		+ "Layer tool.\n"
//	    + "If you then click on Zoom to Active Layer, you will be shown enough of\n"
//	    + "the full map to see all of the features in the layer you select.  If"
//	    + " you\nselect a layer that shows where glaciers are, then you do not"
//	    + " need to\nsee Hawaii, or any southern states, so you will see"
//	    + "Alaska, and northern\nmainland states.";
//		helpText.add(s5);
//		String s6 = "If you click on the MOJO MeasureTool Measuring Tool and then.\n"
//		+ "you click anywhere on the map, and drag and release.\n"
//		+ "This will give the distance between the mouse click and release.";
//	    helpText.add(s6);
//	    String s7 = "If you click on the XY icon tool it will allow us to add a CSV"
//	    + "file.\n If you click on the XY, it will let us to move to the "
//	    + "path where csv file is stored.  Once we add the csv file, the "
//	    + "points considered in that file will be displayed on the map";
//		helpText.add(s7);
//		String s8 = "If you click on Bolt also called Hotlink Tool\n "
//		+ "is a pointer tool, if you click on any points on \n map you can see\n"
//		+ "information about that point" ;
//		helpText.add(s8);
//		String s9 = "If you click on the printer button the native dialog box "
//		+ "for print will open and you can print your map." ;
//		helpText.add(s9);
//		String s10 = "If you click on the add layer button a dialog box will appear"
//		+ "which you are expected to browse to a shp file on your hard drive"
//		+ " and load it into the application." ;
//		helpText.add(s10);
//		String s11 = "If you click on the History.Arrow Tool it will reset the mouse cursor"
//		+ " and handlers to default values, clearing any user selections." ;
//		helpText.add(s11);
//	}
//
//
//	//=========================================================================
//	// FUNCTION: main()
//	// DESCRIPTION: main function
//	//=========================================================================
//	public static void main(String[] args) {
//		System.out.println("Showing history Meusems in San Diego County");
//		HistoryMuseums qstart = new HistoryMuseums();
//		qstart.addWindowListener(
//			new WindowAdapter() {
//				public void windowClosing(WindowEvent e) {
//					System.exit(0);
//				}
//			}
//		);
//		qstart.setVisible(true);
//		env = map.getExtent();
//	}
//}
//
//
////===========================================================================
//// CLASS: History.AddLyrDialog
//// DESCRIPTION: This class handles when the user selects the AddLayer option
////              to add a layer to the map.
////===========================================================================
//class AddLyrDialog extends JDialog {
//	Map map;
//	ActionListener lis;
//	JButton ok = new JButton("OK");
//	JButton cancel = new JButton("Cancel");
//	JPanel panel1 = new JPanel();
//	com.esri.mo2.ui.bean.CustomDatasetEditor cus =
//			new com.esri.mo2.ui.bean.CustomDatasetEditor();
//
//	AddLyrDialog() throws IOException {
//
//		setBounds(50,50,520,430);
//		setTitle("Select a theme/layer");
//		addWindowListener(
//			new WindowAdapter() {
//				public void windowClosing(WindowEvent e) {
//					setVisible(false);
//				}
//			}
//		);
//
//		lis = ae -> {
//            Object source = ae.getSource();
//            if (source == cancel) {
//                setVisible(false);
//            } else {
//                try {
//                    setVisible(false);
//                    map.getLayerset().addLayer(cus.getLayer());
//                    map.redraw();
//                    if (HistoryMuseums.stb.getSelectedLayers() != null) {
//                        HistoryMuseums.pitem.setEnabled(true);
//                    }
//                } catch(IOException e){}
//            }
//        };
//
//		ok.addActionListener(lis);
//		cancel.addActionListener(lis);
//		getContentPane().add(cus,BorderLayout.CENTER);
//		panel1.add(ok);
//		panel1.add(cancel);
//		getContentPane().add(panel1,BorderLayout.SOUTH);
//	}
//
//	//=========================================================================
//	// FUNCTION: setMap()
//	// DESCRIPTION: This function will set the map to the map passed in.
//	//=========================================================================
//	public void setMap(com.esri.mo2.ui.bean.Map map1){
//		map = map1;
//	}
//}
//
//
////===========================================================================
//// CLASS: History.AddXYtheme
//// DESCRIPTION: This function parses a CSV file and creates a feature layer.
////===========================================================================
//class AddXYtheme extends JDialog {
//	Map map;
//	Vector<String> fieldNames = new Vector<>();
//	Vector fieldValues = new Vector();
//	JFileChooser jfc = new JFileChooser();
//	BasePointsArray bpa = new BasePointsArray();
//	AddXYtheme() throws IOException {
//		setBounds(50,50,520,430);
//		jfc.setCurrentDirectory(new File("./"));
//		jfc.showOpenDialog(this);
//		try {
//			File file = jfc.getSelectedFile();
//
//			FileReader fred = new FileReader(file);
//			BufferedReader in = new BufferedReader(fred);
//			String s;
//			double x=0.0, y=0.0;
//			int n = 0;
//			int i = 0;
//			boolean first =true;
//			while ((s = in.readLine()) != null) {
//				StringTokenizer st = new StringTokenizer(s, ",");
//				i =0;
//				while (st.hasMoreTokens()) {
//					if (first) {
//						fieldNames.add(st.nextToken());
//					} else {
//						if (i == 0) {
//							x = Double.parseDouble(st.nextToken());
//							fieldValues.add(x);
//						} else if (i == 1) {
//							y = Double.parseDouble(st.nextToken());
//							fieldValues.add(y);
//						} else
//							fieldValues.add(st.nextToken());
//						i++;
//					}
//				}
//				if(!first) {
//					bpa.insertPoint(n, new com.esri.mo2.cs.geom.Point(x, y));
//					n++;
//				}
//				else{
//					first  = false;
//				}
//
//			}
//		} catch (IOException e){}
//		XYfeatureLayer xyfl = new XYfeatureLayer(bpa,map,fieldNames,fieldValues);
//		xyfl.setVisible(true);
//		map = HistoryMuseums.map;
//		map.getLayerset().addLayer(xyfl);
//		map.redraw();
//	}
//
//	//=========================================================================
//	// FUNCTION: setMap()
//	// DESCRIPTION: This function will set the map to the map passed in.
//	//=========================================================================
//	public void setMap(com.esri.mo2.ui.bean.Map map1){
//		map = map1;
//	}
//}
//
//
////===========================================================================
//// CLASS: History.XYfeatureLayer
//// DESCRIPTION: This is the nuts and bolts class for the create feature layer
////              function from CSV (XYTool).
////===========================================================================
//class XYfeatureLayer extends BaseFeatureLayer {
//	BaseFields fields;
//	private java.util.Vector featureVector;
//
//	//=========================================================================
//	// FUNCTION: XYFeatureLayer()
//	// DESCRIPTION: Class constructor
//	//=========================================================================
//	public XYfeatureLayer(BasePointsArray bpa, Map map, Vector fieldNames, Vector fieldValues) {
//
//		createFeaturesAndFields(bpa,map,fieldNames,fieldValues);
//		BaseFeatureClass bfc = getFeatureClass("MyPoints",bpa);
//		setFeatureClass(bfc);
//		BaseSimpleRenderer srd = new BaseSimpleRenderer();
//        SimpleMarkerSymbol sms = new SimpleMarkerSymbol();
//        sms.setType(SimpleMarkerSymbol.CIRCLE_MARKER);
//        sms.setSymbolColor(new java.awt.Color(250, 23, 0));
//        sms.setWidth(5);
//        srd.setSymbol(sms);
//        setRenderer(srd);
//        // without setting layer capabilities, the points will not
//        // display (but the toc entry will still appear)
//        XYLayerCapabilities lc = new XYLayerCapabilities();
//        setCapabilities(lc);
//
////		com.esri.mo2.map.draw.RasterMarkerSymbol sms = null; // for points
////		sms = new com.esri.mo2.map.draw.RasterMarkerSymbol();
////		sms.setSizeX(30);
////		sms.setSizeY(30);
////		sms.setImageString("./icons/meusem4.png");
////		srd.setSymbol(sms);
////        // without setting layer capabilities, the points will not
////        // display (but the toc entry will still appear)
////        XYLayerCapabilities lc = new XYLayerCapabilities();
////        setCapabilities(lc);
//	}
//
//	//=========================================================================
//	// FUNCTION: createFeaturesAndFields()
//	// DESCRIPTION: This creates the main data structure used by the XYTool to
//	//              create the features in the map and attribute table.
//	//=========================================================================
//	private void createFeaturesAndFields(BasePointsArray bpa,Map map,Vector fieldNames, Vector fieldValues) {
//		featureVector = new java.util.Vector();
//		fields = new BaseFields();
//		createDbfFields(fieldNames);
//		int j = 0;
//		for(int i=0;i<bpa.size();i++) {
//			BaseFeature feature = new BaseFeature();
//			feature.setFields(fields);
//			com.esri.mo2.cs.geom.Point p = new com.esri.mo2.cs.geom.Point(bpa.getPoint(i));
//			feature.setValue(0,p);
//			feature.setValue(1,new Integer(i));
//			feature.setValue(2, (double) fieldValues.elementAt(j));
//            feature.setValue(3, (double) fieldValues.elementAt(++j));
//            feature.setValue(4, (String) fieldValues.elementAt(++j));
//            feature.setValue(5, (String) fieldValues.elementAt(++j));
//            feature.setValue(6, (String) fieldValues.elementAt(++j));
//			feature.setValue(7, (String) fieldValues.elementAt(++j));
//			feature.setValue(8, (String) fieldValues.elementAt(++j));
//			feature.setValue(9, (String) fieldValues.elementAt(++j));
//			feature.setDataID(new BaseDataID("Meusems",i));
//			featureVector.addElement(feature);
//			j++;
//		}
//	}
//
//	//=========================================================================
//	// FUNCTION: createDbfFields()
//	// DESCRIPTION: This setups the attribute table, names the columns and
//	//              defines the type of each.
//	//=========================================================================
//	private void createDbfFields(Vector<String> fieldNames) {
//		fields.addField(new BaseField("#SHAPE#",Field.ESRI_SHAPE,0,0));
//		fields.addField(new BaseField("ID",java.sql.Types.INTEGER,9,0));
//		fields.addField(new BaseField(fieldNames.get(0), Types.DOUBLE, 20, 5));
//		fields.addField(new BaseField(fieldNames.get(1), Types.DOUBLE, 20, 5));
//		fields.addField(new BaseField(fieldNames.get(2),java.sql.Types.VARCHAR,64,0));
//		fields.addField(new BaseField(fieldNames.get(3),java.sql.Types.VARCHAR,64,0));
//		fields.addField(new BaseField(fieldNames.get(4),java.sql.Types.VARCHAR,20,0));
//		fields.addField(new BaseField(fieldNames.get(5),java.sql.Types.VARCHAR,10,0));
//		fields.addField(new BaseField(fieldNames.get(6),java.sql.Types.VARCHAR,100,0));
//		fields.addField(new BaseField(fieldNames.get(7),java.sql.Types.VARCHAR,200,0));
//
//	}
//
//	//=========================================================================
//	// FUNCTION: BaseFeatureClass()
//	// DESCRIPTION: This function returns the BaseFeature class.
//	//=========================================================================
//	public BaseFeatureClass getFeatureClass(String name,BasePointsArray bpa){
//		com.esri.mo2.map.mem.MemoryFeatureClass featClass = null;
//		try {
//			featClass = new com.esri.mo2.map.mem.MemoryFeatureClass(MapDataset.POINT,fields);
//		} catch (IllegalArgumentException iae) {}
//		featClass.setName(name);
//		for (int i=0;i<bpa.size();i++) {
//			featClass.addFeature((Feature) featureVector.elementAt(i));
//		}
//		return featClass;
//	}
//
//	//=========================================================================
//	// FUNCTION: XYLayerCapabilities()
//	// DESCRIPTION: This function sets some properties of the new feature layer
//	//=========================================================================
//	private final class XYLayerCapabilities
//		extends com.esri.mo2.map.dpy.LayerCapabilities {
//
//		XYLayerCapabilities() {
//			for (int i=0;i<this.size(); i++) {
//				setAvailable(this.getCapabilityName(i),true);
//				setEnablingAllowed(this.getCapabilityName(i),true);
//				getCapability(i).setEnabled(true);
//			}
//		}
//	}
//}
//
//
////===========================================================================
//// CLASS: History.AttrTab
//// DESCRIPTION: This class displays the attribute table for the active layer
////===========================================================================
//class AttrTab extends JDialog {
//	JPanel panel1 = new JPanel();
//	com.esri.mo2.map.dpy.Layer layer = HistoryMuseums.layer4;
//	JTable jtable = new JTable(new MyTableModel());
//	JScrollPane scroll = new JScrollPane(jtable);
//
//	//=========================================================================
//	// FUNCTION: History.AttrTab()
//	// DESCRIPTION: Class constructor
//	//=========================================================================
//	public AttrTab() throws IOException {
//		setBounds(70,70,450,350);
//		setTitle("Attribute Table");
//		addWindowListener(
//			new WindowAdapter() {
//				public void windowClosing(WindowEvent e) {
//					setVisible(false);
//				}
//			}
//		);
//
//		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//		jtable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//
//		TableColumn tc = null;
//		int numCols = jtable.getColumnCount();
//		for (int j=0;j<numCols;j++) {
//			tc = jtable.getColumnModel().getColumn(j);
//			tc.setMinWidth(50);
//		}
//		getContentPane().add(scroll,BorderLayout.CENTER);
//	}
//}
//
//
////===========================================================================
////CLASS: History.MyTableModel
////DESCRIPTION: This class creates the attribute table
////===========================================================================
//class MyTableModel extends AbstractTableModel {
//	com.esri.mo2.map.dpy.Layer layer = HistoryMuseums.layer4;
//	MyTableModel() {
//		qfilter.setSubFields(fields);
//		com.esri.mo2.data.feat.Cursor cursor = flayer.search(qfilter);
//		while (cursor.hasMore()) {
//			ArrayList inner = new ArrayList();
//			Feature f = (com.esri.mo2.data.feat.Feature)cursor.next();
//			inner.add(0,String.valueOf(row));
//			for (int j=1;j<fields.getNumFields();j++) {
//				inner.add(f.getValue(j).toString());
//			}
//			data.add(inner);
//			row++;
//		}
//	}
//	FeatureLayer flayer = (FeatureLayer) layer;
//	FeatureClass fclass = flayer.getFeatureClass();
//	String columnNames [] = fclass.getFields().getNames();
//	ArrayList data = new ArrayList();
//	int row = 0;
//	int col = 0;
//	BaseQueryFilter qfilter = new BaseQueryFilter();
//	Fields fields = fclass.getFields();
//
//	//=========================================================================
//	// FUNCTION: getColumnCount()
//	// DESCRIPTION: Returns the # of columns
//	//=========================================================================
//	public int getColumnCount() {
//		return fclass.getFields().getNumFields();
//	}
//
//	//=========================================================================
//	// FUNCTION: getRowCount()
//	// DESCRIPTION: Returns the # of rows
//	//=========================================================================
//	public int getRowCount() {
//		return data.size();
//	}
//
//	//=========================================================================
//	// FUNCTION: getColumnName()
//	// DESCRIPTION: Returns the name of column
//	//=========================================================================
//	public String getColumnName(int colIndx) {
//		return columnNames[colIndx];
//	}
//
//	//=========================================================================
//	// FUNCTION: getValueAt()
//	// DESCRIPTION: Returns the value in the cell in the table
//	//=========================================================================
//	public Object getValueAt(int row, int col) {
//		ArrayList temp = new ArrayList();
//		temp =(ArrayList) data.get(row);
//		return temp.get(col);
//	}
//}
//
//
////===========================================================================
////CLASS: History.CreateShapeDialog
////DESCRIPTION: This class writes the new shapefile to disk
////===========================================================================
//class CreateShapeDialog extends JDialog {
//	String name = "";
//	String path = "";
//	int ltype  ;
//	JButton ok = new JButton("OK");
//	JButton cancel = new JButton("Cancel");
//	JTextField nameField = new JTextField("enter layer name here, then hit ENTER",25);
//	com.esri.mo2.map.dpy.FeatureLayer selectedlayer;
//	JPanel panel1 = new JPanel();
//	JLabel centerlabel = new JLabel();
//
//	ActionListener lis = new ActionListener() {
//		public void actionPerformed(ActionEvent ae) {
//			Object o = ae.getSource();
//			if (o == nameField) {
//				name = nameField.getText().trim();
//				try {
//					//path = ((ShapefileFolder)(History.HistoryMuseums.layer4.getLayerSource())).getPath();
//					path = "./data/";
//				} catch ( Exception e ) {
//					path = "C:/Temp" ;
//				}
//				System.out.println(path+"    " + name);
//			} else if (o == cancel) {
//				setVisible(false);
//			} else {
//				try {
//					ShapefileWriter.writeFeatureLayer(selectedlayer,path,name,ltype);
//				} catch(Exception e) {
//				      e.printStackTrace();
//				}
//				setVisible(false);
//			}
//		}
//	};
//
//	//=========================================================================
//	// FUNCTION: History.CreateShapeDialog()
//	// DESCRIPTION: Constructor
//	//=========================================================================
//	public CreateShapeDialog (com.esri.mo2.map.dpy.FeatureLayer layer5,
//					          int layertype) {
//		selectedlayer = layer5;
//		ltype = layertype;
//		setBounds(40,350,450,150);
//	    setTitle("Create new shapefile?");
//	    addWindowListener(
//	    	new WindowAdapter() {
//	    		public void windowClosing(WindowEvent e) {
//	    			setVisible(false);
//	    		}
//	    	}
//	    );
//	    nameField.addActionListener(lis);
//	    ok.addActionListener(lis);
//	    cancel.addActionListener(lis);
//	    String s = "<HTML> To make a new shapefile from the new layer, enter<BR>" +
//	      "the new name you want for the layer and click OK.<BR>" +
//	      "You can then add it to the map in the usual way.<BR>"+
//	      "Click ENTER after replacing the text with your layer name";
//	    centerlabel.setHorizontalAlignment(JLabel.CENTER);
//	    centerlabel.setText(s);
//	    getContentPane().add(centerlabel,BorderLayout.CENTER);
//	    panel1.add(nameField);
//	    panel1.add(ok);
//	    panel1.add(cancel);
//	    getContentPane().add(panel1,BorderLayout.SOUTH);
//	}
//}
//
//
////===========================================================================
////CLASS: History.Arrow
////DESCRIPTION: This class handles the mouse cursor behavior.
////===========================================================================
//class Arrow extends Tool {
//
//	//=========================================================================
//	// FUNCTION: History.Arrow()
//	// DESCRIPTION: Class constructor
//	//=========================================================================
//	public Arrow() {
//		HistoryMuseums.milesLabel.setText("DIST   0 mi   ");
//		HistoryMuseums.kmLabel.setText("   0 km    ");
//		HistoryMuseums.map.repaint();
//		HistoryMuseums.helpToolOn = false ;
//	}
//}
//
//
////===========================================================================
////CLASS: History.Flash
////DESCRIPTION: When a new layer is added this class will flash the new layer
////             in a new thread to help show the user what they have done.
////             It works on polygon layers only.
////===========================================================================
//class Flash extends Thread {
//	Legend legend;
//	Flash(Legend legendin) {
//		legend = legendin;
//	}
//
//	//=========================================================================
//	// FUNCTION: run()
//	// DESCRIPTION: This is the function that will be executed when the child
//	//              thread is invoked.
//	//=========================================================================
//	public void run() {
//		for (int i=0;i<12;i++) {
//			try {
//				Thread.sleep(500);
//				legend.toggleSelected();
//			} catch (Exception e) {}
//		}
//	}
//}
//
//
////===========================================================================
////CLASS: History.DistanceTool
////DESCRIPTION: A class to help calculate as the crow flies distance
////             interactively with the user.
////===========================================================================
//class DistanceTool extends DragTool  {
//	int startx,starty,endx,endy,currx,curry;
//	com.esri.mo2.cs.geom.Point initPoint, endPoint, currPoint;
//	double distance;
//
//	//=========================================================================
//	// FUNCTION: mousePressed()
//	// DESCRIPTION: Collects the starting point for the user.
//	//=========================================================================
//	public void mousePressed(MouseEvent me) {
//		startx = me.getX(); starty = me.getY();
//		initPoint = HistoryMuseums.map.transformPixelToWorld(me.getX(),me.getY());
//	}
//
//	//=========================================================================
//	// FUNCTION: mouseReleased()
//	// DESCRIPTION: Collects the end point and calculates as the crow flies
//	//              distance.
//	//=========================================================================
//	public void mouseReleased(MouseEvent me) {
//		endx = me.getX(); endy = me.getY();
//		endPoint = HistoryMuseums.map.transformPixelToWorld(me.getX(),me.getY());
//		distance = (69.44 / (2*Math.PI)) * 360 * Math.acos(
//				 Math.sin(initPoint.y * 2 * Math.PI / 360)
//				 * Math.sin(endPoint.y * 2 * Math.PI / 360)
//				 + Math.cos(initPoint.y * 2 * Math.PI / 360)
//				 * Math.cos(endPoint.y * 2 * Math.PI / 360)
//				 * (Math.abs(initPoint.x - endPoint.x) < 180 ?
//                    Math.cos((initPoint.x - endPoint.x)*2*Math.PI/360):
//                    Math.cos((360 -
//                    		Math.abs(initPoint.x - endPoint.x))*2*Math.PI/360)));
//		System.out.println( distance );
//		HistoryMuseums.milesLabel.setText("DIST: " +
//									 new Float((float)distance).toString()
//									 + " mi  ");
//		HistoryMuseums.kmLabel.setText(new Float((float)(distance*1.6093)).toString()
//								  + " km");
//
//		if (HistoryMuseums.acetLayer != null) {
//			HistoryMuseums.map.remove(HistoryMuseums.acetLayer);
//		}
//
//		HistoryMuseums.acetLayer = new AcetateLayer() {
//			public void paintComponent(java.awt.Graphics g) {
//				java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
//				Line2D.Double line = new Line2D.Double(startx,
//							starty,
//							endx,
//							endy);
//				g2d.setColor(new Color(0,0,250));
//				g2d.draw(line);
//			}
//		};
//
//		Graphics g = super.getGraphics();
//		HistoryMuseums.map.add(HistoryMuseums.acetLayer);
//		HistoryMuseums.map.redraw();
//	}
//
//	//=========================================================================
//	// FUNCTION: cancel()
//	// DESCRIPTION: Null function to handle cancel event
//	//=========================================================================
//	public void cancel() {};
//}
//
//
////===========================================================================
////CLASS: History.HotPick
////DESCRIPTION: Displays a dialog window with attribute data information as
////             well as a button to click to take the user to the website.
////===========================================================================
//class HotPick extends JDialog {
//
//	Hashtable<Integer,String[]> pictures = new Hashtable<>();
//	//=========================================================================
//	// FUNCTION: History.HotPick()
//	// DESCRIPTION: Class constructor
//	//=========================================================================
//	public HotPick(int id, Row row, int activeLayerIndex, Map map) throws IOException {
//
//		pictures.put(0,new String[]{"./Resources/photos/MuesemOfMan1.jpg","./Resources/photos/MuesemOfMan2.jpg"});
//		pictures.put(1,new String[]{"./Resources/photos/whaleyHouse.jpg","./Resources/photos/whaleyHouse1.jpg"});
//		pictures.put(2,new String[]{"./Resources/photos/wellsFargo.jpg","./Resources/photos/wellsFargo1.jpg"});
//		pictures.put(3,new String[]{"./Resources/photos/SDHC.jpg","./Resources/photos/SDHC1.jpg"});
//		pictures.put(4,new String[]{"./Resources/photos/san-diego-chinese-historical.jpg","./Resources/photos/san-diego-chinese-historical1.jpg"});
//		pictures.put(5,new String[]{"./Resources/photos/Veterans Museum.jpg","./Resources/photos/Veterans Museum1.jpg"});
//		pictures.put(6,new String[]{"./Resources/photos/Junipero-Serra-Museum.jpg","./Resources/photos/Junipero-Serra-Museum1.jpg"});
//		// Main variables...
//		ActionListener buttonListener ;
//		final String os = System.getProperty("os.name");
//		final JButton jb = new JButton("Go to Website", new ImageIcon("./Resources/icons/globe.png"));
//		// Get the main container for all panels here...
//		Container container = getContentPane();
//		// Create new top layer panel for all subsequent panels...
//		JPanel top = new JPanel(new BorderLayout());
//		// Title information:
//		setTitle(row.getDisplayValue(4));
//
//
//		JPanel info = new JPanel(new GridLayout(1,2));
//
//		JPanel infotitlePanel = new JPanel() ;
//		infotitlePanel.setLayout( new FlowLayout( FlowLayout.LEADING ) );
//		JLabel infotitle = new JLabel(row.getDisplayValue(4) + ": ") ;
//		infotitlePanel.add(infotitle);
//		info.add(infotitlePanel) ;
//
//		String meusemInformation = "<HTML>";
//		JPanel infodetailsPanel = new JPanel() ;
//		infodetailsPanel.setLayout( new FlowLayout( FlowLayout.LEADING ) );
//
//		// Store off the url field for use later...
//		final String url = row.getDisplayValue(8) ;
//
//		// Loop through the features and add each item to the text display...
//		int numfieldsinrow = row.getFields().size() ;
//		String[] fieldnames = row.getFields().getNames() ;
//		for ( int i=2; i<numfieldsinrow; ++i ) {
//			// Skip URL field since we have a JButton to handle that
//			// in the South...
//			if ( i != 8 ) {
//				meusemInformation += fieldnames[i] + ": " + row.getDisplayValue(i) + "<BR>" ;
//			}
//		}
//		JLabel infodetails = new JLabel(meusemInformation) ;
//		infodetailsPanel.add(infodetails) ;
//		info.add(infodetailsPanel);
//		top.add(info, BorderLayout.NORTH);
//
//		// Create a grid layout for pictures...
//		JPanel pics = new JPanel(new GridLayout(1,10,10,10));
//		String curLayerName = map.getLayer(activeLayerIndex).getName();
//		System.out.println("LayerName = [" + curLayerName + "]" );
//		if ( !new String("San Diego").equals(curLayerName) ) {
//
//			// We have pictures so make the window size larger than normal...
//			setBounds(20,20,1250,750);
//
//			for ( String pic :pictures.get(id)) {
//				JLabel label = new JLabel(new ImageIcon(pic));
//				pics.add(label) ;
//			}
//			//The pictures will likely need a horizontal scroll bar...
//			JScrollPane jp = new JScrollPane( pics, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//			top.add(jp, BorderLayout.CENTER);
//		} else {
//			// We do not have pictures so make the window size smaller...
//			setBounds(50,50,500,275);
//		}
//
//
//		buttonListener = new ActionListener() {
//			public void actionPerformed(ActionEvent ae) {
//				Object source = ae.getSource();
//
//				// Default to windows...
//				String b = "C:\\Program Files\\Internet Explorer\\IEXPLORE.EXE";
//				b = b + " " + url ;
//
//				if (source == jb) {
//					try {
//						if (os.indexOf("Windows") != -1) {
//							Runtime.getRuntime().exec(b);
//						} else if (os.indexOf("Mac") != -1) {
//							b = "open " + "-a" + " Safari " + url ;
//							Runtime.getRuntime().exec(b);
//							}
//					} catch (Exception ex) {
//						System.out.println("cannot execute command. " + ex);
//						ex.printStackTrace();
//					}
//				}
//			}
//		} ;
//
//
//		jb.addActionListener(buttonListener);
//		top.add(jb, BorderLayout.SOUTH);
//
//		container.add(top) ;
//
//		// Close window handler...
//		addWindowListener(
//			new WindowAdapter() {
//				public void windowClosing(WindowEvent e) {
//					setVisible(false);
//				}
//			}
//		);
//	}
//}
//
//
////===========================================================================
////CLASS: History.HelpDialog
////DESCRIPTION: A Help Menu function to display a new window and display
////             helpful text to the user to help show them how to use the app.
////===========================================================================
//class HelpDialog extends JDialog {
//	JTextArea ha ;
//
//	//=========================================================================
//	// FUNCTION: History.HelpDialog()
//	// DESCRIPTION: Class constructor
//	//=========================================================================
//	public HelpDialog(String input) throws IOException {
//		setBounds(70,70,450,250) ;
//		setTitle("Help");
//		addWindowListener(
//			new WindowAdapter() {
//				public void windowClosing(WindowEvent e) {
//					setVisible(false);
//				}
//			}
//		) ;
//		ha = new JTextArea(input,7,40);
//		JScrollPane sp = new JScrollPane(ha);
//		ha.setEditable(false);
//		getContentPane().add(sp,"Center");
//	}
//}
//
//
//class HelpTool extends Tool {
//}