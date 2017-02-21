package se.sthlm.jfw.mainServlet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.util.Log;

/**
 * 
 * @author JFW
 */
@UrlBinding("/about")
public class AboutActionBean implements ActionBean {

	private static final Log logger = Log.getInstance(TwitterActionBean.class);

	private ActionBeanContext context;

	public ActionBeanContext getContext() { return context; }
	public void setContext(ActionBeanContext context) { this.context = context; }

	@DefaultHandler
	public Resolution adminResolution() {
		return new ForwardResolution("/WEB-INF/about.jsp");   
	}
}