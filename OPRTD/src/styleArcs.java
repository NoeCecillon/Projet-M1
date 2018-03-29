import java.util.Hashtable;

import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

public class styleArcs {

	 public static final String MY_CUSTOM_VERTEX_STYLE = "MY_CUSTOM_VERTEX_STYLE";
	    public static final String MY_CUSTOM_EDGE_STYLE = "MY_CUSTOM_EDGE_STYLE";

	    private static void setStyleSheet(mxGraph graph) {

	        Hashtable<String, Object> style;
	        mxStylesheet stylesheet = graph.getStylesheet();

	        // base style
	        Hashtable<String, Object> baseStyle = new Hashtable<String, Object>();
	        baseStyle.put(mxConstants.STYLE_STROKECOLOR, "#FF0000");

	        // custom vertex style
	        style = new Hashtable<String, Object>(baseStyle);
	        style.put(mxConstants.STYLE_FILLCOLOR, "#FFFF00");
	        stylesheet.putCellStyle(MY_CUSTOM_VERTEX_STYLE, style);

	        // custom edge style
	        style = new Hashtable<String, Object>(baseStyle);
	        style.put(mxConstants.STYLE_STROKEWIDTH, 3);
	        stylesheet.putCellStyle(MY_CUSTOM_EDGE_STYLE, style);

	    }
}
