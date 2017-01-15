package se.sthlm.jfw.mainServlet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

/**
 * 
 * @author JFW
 */
@UrlBinding("/admin")
public class AdminActionBean implements ActionBean {

	private ActionBeanContext context;

	private String serverName;

	public ActionBeanContext getContext() { return context; }
	public void setContext(ActionBeanContext context) { this.context = context; }
	public String getServerName() { return serverName; }
	public void setServerName(String serverName) { this.serverName = serverName; }


	@DefaultHandler
	public Resolution adminResolution() {
		String openshift_data_dir = System.getenv().get("OPENSHIFT_DATA_DIR");
		String account_folder = openshift_data_dir + File.separator + "admin";
		File setupFile = new File(account_folder + File.separator + "setup.txt");
		try {
			if(!setupFile.exists())
				setupFile.createNewFile();
			BufferedReader setupFileReader = new BufferedReader(new FileReader(setupFile));
			String localServerName = setupFileReader.readLine();
			if(localServerName == null) {
				localServerName = "http://rosenknopp-scomer.rhcloud.com/welcome/";
			}
			setServerName(localServerName);
			setupFileReader.close();
		} catch(Exception e) {
			//Ignored for now
		}
		return new ForwardResolution("/WEB-INF/admin.jsp");   
	}

	public Resolution save() {
		String openshift_data_dir = System.getenv().get("OPENSHIFT_DATA_DIR");
		String account_folder = openshift_data_dir + File.separator + "admin";
		File setupFile = new File(account_folder + File.separator + "setup.txt");
		try {
			if(!setupFile.exists())
				setupFile.createNewFile();
			BufferedWriter setupFileWriter = new BufferedWriter(new FileWriter(setupFile, false));

			setupFileWriter.write(getServerName() + "\n");
			setupFileWriter.close();
		} catch(Exception e) {
			//ignored
		}

		return new ForwardResolution("/WEB-INF/admin.jsp");
	}


}