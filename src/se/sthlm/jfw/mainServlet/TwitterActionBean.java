package se.sthlm.jfw.mainServlet;

import java.io.File;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/twitter")
public class TwitterActionBean implements ActionBean {
    private ActionBeanContext context;
    private String accountIdentifier;
      
	public ActionBeanContext getContext() { return context; }
    public void setContext(ActionBeanContext context) { this.context = context; }

    public String getAccountIdentifier() {
		return accountIdentifier;
	}
	public void setAccountIdentifier(String accountIdentifier) {
		this.accountIdentifier = accountIdentifier;
	}

	@DefaultHandler
    public Resolution setupData() {
        
    	try {
    		String openshift_data_dir = System.getenv().get("OPENSHIFT_DATA_DIR");
    		accountIdentifier = getContext().getRequest().getSession().getAttribute("accountIdentifier").toString();
    		String account_folder = openshift_data_dir + File.separator + "twitter" + File.separator + accountIdentifier;
    		File accountFolderFile = new File(account_folder);
			if(!accountFolderFile.exists())
				accountFolderFile.mkdirs();
    	} catch(Exception e) {
    		//Ignored for now
    	}

    	return new ForwardResolution("/WEB-INF/twitter.jsp");
    }
}