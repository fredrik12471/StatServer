package se.sthlm.jfw.mainServlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

@UrlBinding("/twitter")
public class TwitterActionBean implements ActionBean {
    private ActionBeanContext context;
    private String accountIdentifier;
    private User twitterUser;
    
	public ActionBeanContext getContext() { return context; }
    public void setContext(ActionBeanContext context) { this.context = context; }

    public String getAccountIdentifier() {
		return accountIdentifier;
	}
	public void setAccountIdentifier(String accountIdentifier) {
		this.accountIdentifier = accountIdentifier;
	}
	public User getTwitterUser() {
		return twitterUser;
	}
	public void setTwitterUser(User twitterUser) {
		this.twitterUser = twitterUser;
	}
	@DefaultHandler
    public Resolution setupData() {
        String account_folder = "";
    	try {
       		String openshift_data_dir = System.getenv().get("OPENSHIFT_DATA_DIR");
    		accountIdentifier = getContext().getRequest().getSession().getAttribute("accountIdentifier").toString();
    		account_folder = openshift_data_dir + File.separator + "twitter" + File.separator + accountIdentifier;
    		File accountFolderFile = new File(account_folder);
			if(!accountFolderFile.exists())
				accountFolderFile.mkdirs();
			Twitter twitter = getTwitter(account_folder);
			twitterUser = twitter.showUser(Long.valueOf(accountIdentifier));
			//twitterUser.getstat
    	} catch(Exception e) {
    		//Ignored for now
    		setAccountIdentifier("" + e.getMessage() + ", " + account_folder);
    	}

    	return new ForwardResolution("/WEB-INF/twitter.jsp");
    }
	
	private Twitter getTwitter(String accountFolder) throws Exception {
		BufferedReader input = new BufferedReader(new FileReader(accountFolder + File.separator + "account.txt"));
		String oAuthConsumerKey = input.readLine();
		String oAuthConsumerSecret = input.readLine();
		String oAuthAccessToken = input.readLine();
		String oAuthAccessTokenSecret = input.readLine();
		input.close();
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
			.setOAuthConsumerKey(oAuthConsumerKey)
			.setOAuthConsumerSecret(oAuthConsumerSecret)
			.setOAuthAccessToken(oAuthAccessToken)
			.setOAuthAccessTokenSecret(oAuthAccessTokenSecret);
		TwitterFactory tf = new TwitterFactory(cb.build());
		return tf.getInstance();
	}
}