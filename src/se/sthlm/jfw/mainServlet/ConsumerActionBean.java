package se.sthlm.jfw.mainServlet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.util.Log;

@UrlBinding("/twitter")
public class ConsumerActionBean implements ActionBean {
    private ActionBeanContext context;
    private String accountName;
    
    private String retweet;
    private String retweetKeyword;
    private ArrayList<String> retweetKeywords;
    private String retweetKeywordsTiming;
    private String keyword;
    
	public ActionBeanContext getContext() { return context; }
    public void setContext(ActionBeanContext context) { this.context = context; }

    public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getRetweetKeyword() {
		return retweetKeyword;
	}
	public void setRetweetKeyword(String retweetKeyword) {
		this.retweetKeyword = retweetKeyword;
	}
	public String getRetweet() {
		return retweet;
	}
	public void setRetweet(String retweet) {
		this.retweet = retweet;
	}
	
	public ArrayList<String> getRetweetKeywords() {
		return retweetKeywords;
	}
	public void setRetweetKeywords(ArrayList<String> retweetKeywords) {
		this.retweetKeywords = retweetKeywords;
	}
	public String getRetweetKeywordsTiming() {
		return retweetKeywordsTiming;
	}
	public void setRetweetKeywordsTiming(String retweetKeywordsTiming) {
		this.retweetKeywordsTiming = retweetKeywordsTiming;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	@DefaultHandler
    public Resolution setupData() {
        
    	try {
    		String openshift_data_dir = System.getenv().get("OPENSHIFT_DATA_DIR");
    		accountName = getContext().getRequest().getSession().getAttribute("accountName").toString();
    		String account_folder = openshift_data_dir + File.separator + accountName;
    		File retweetFile = new File(account_folder + File.separator + "retweets.txt");
    		if(retweetFile != null && retweetFile.exists())
    			retweet = "true";
    		else
    			retweet = "false";
    		retweetKeyword = "";
    		retweetKeywords = new ArrayList<String>();
    		if(retweet.equals("true")) {
    			BufferedReader input = new BufferedReader(new FileReader(retweetFile));
    			int currentCounter = Integer.valueOf(input.readLine());
    			int resetCounter = Integer.valueOf(input.readLine());
    			String aKeyword = input.readLine();
    			while(aKeyword != null) {
            		retweetKeywords.add(aKeyword);
            		aKeyword = input.readLine();
    			}

    			input.close();
    		}

    	} catch(Exception e) {
    		log(e.getMessage());
    	}


    	keyword = "AIK";
    	return new ForwardResolution("/WEB-INF/twitter.jsp");
    }
    
    public Resolution save() {
    	
//    	try {
//	    	String openshiftDataDir = System.getenv().get("OPENSHIFT_DATA_DIR");
//			accountName = getContext().getRequest().getSession().getAttribute("accountName").toString();
//			String accountFolder = openshiftDataDir + File.separator + accountName;
//			File retweetFile = new File(accountFolder + File.separator + "retweets.txt");
//			File retweetedTweetsFile = new File(accountFolder + File.separator + "retweetedTweets.txt");
//			if(retweet != null && retweetKeywords != null && retweetKeywords.size() > 0) {
//				BufferedWriter tmpInput = new BufferedWriter(new FileWriter(retweetFile));
//		    	tmpInput.write(retweetKeywordsTiming + "\n");
//		    	tmpInput.write(retweetKeywordsTiming + "\n");
//		    	for(int i = 0; i < retweetKeywords.size(); i ++)
//		    		tmpInput.write(retweetKeywords.get(i) + "\n");
//		    	tmpInput.close();
//		    	retweetedTweetsFile.createNewFile();
//	    	} else {
//	    		retweetFile.delete();
//	    	}
//    	} catch(Exception e) {
//    		//getContext().getRequest().getSession().setAttribute("accountName", e.getMessage());
//    		log("Exception: " + e.getMessage());
//    	}
    	try {
    		accountName = getContext().getRequest().getSession().getAttribute("accountName").toString();
    		log("getRetweetKeyword: " + getRetweetKeyword());
    		log("getRetweet: " + getRetweet());
    		log("getRetweetKeywords: " + (getRetweetKeywords() != null));
    		log("getRetweetKeywordsTiming: " + getRetweetKeywordsTiming());
    		for(int i = 0; i < getRetweetKeywords().size(); i ++) {
    			log("getRetweetKeywords(" + i + "):" + getRetweetKeywords().get(i));
    		}
    		log("getKeyword: " + getKeyword());
    	} catch(Exception e) {
    		//ignored
    	}

    	return new ForwardResolution("/WEB-INF/twitter.jsp");
    }

    public Resolution logout() {
    	return new RedirectResolution("/welcome");
    }
    
    private boolean log(String message) {
    	try {
	    	String openshiftDataDir = System.getenv().get("OPENSHIFT_DATA_DIR");
	    	String logFilename = openshiftDataDir + File.separator + "frejov" + File.separator + "jboss.txt";
			File logFile = new File(logFilename);
			BufferedWriter tmpInput = new BufferedWriter(new FileWriter(logFile,true));
	    	tmpInput.write(message + "\n");
	    	tmpInput.close();
    	} catch(IOException e) {
    		//Ignored
    	}
    	return true;
    }
    
    
}