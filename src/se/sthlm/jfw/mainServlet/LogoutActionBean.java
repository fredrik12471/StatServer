package se.sthlm.jfw.mainServlet;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/logout")
public class LogoutActionBean implements ActionBean {

	private ActionBeanContext context;
	
    public ActionBeanContext getContext() { return context; }
    public void setContext(ActionBeanContext context) { this.context = context; }

	@DefaultHandler
    public Resolution logout() {
	
		
		return new RedirectResolution("/welcome");
	}
	
	

}