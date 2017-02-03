package se.sthlm.jfw.mainServlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
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
    private String followerData;
    private String friendData;
    private String csvFile;
    private String userId;
    

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
	public String getFollowerData() {
		return followerData;
	}
	public void setFollowerData(String followerData) {
		this.followerData = followerData;
	}
	
	public String getFriendData() {
		return friendData;
	}
	public void setFriendData(String friendData) {
		this.friendData = friendData;
	}
	public String getCsvFile() {
		return csvFile;
	}
	public void setCsvFile(String csvFile) {
		this.csvFile = csvFile;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
			
			String totalFollowerFileName = account_folder + File.separator + "followers.txt";
			BufferedReader input = new BufferedReader(new FileReader(totalFollowerFileName));

			String line = input.readLine();
			if(line != null) {
				followerData = line;
				line = input.readLine();
				while(line != null) {
					followerData += ", " + line;
					line = input.readLine();
				}
			}
				
			input.close();
			
			String totalFriendFileName = account_folder + File.separator + "friends.txt";
			BufferedReader totalFriendInput = new BufferedReader(new FileReader(totalFriendFileName));

			String totalFriendLine = totalFriendInput.readLine();
			if(totalFriendLine != null) {
				friendData = totalFriendLine;
				totalFriendLine = totalFriendInput.readLine();
				while(totalFriendLine != null) {
					friendData += ", " + totalFriendLine;
					totalFriendLine = totalFriendInput.readLine();
				}
			}
				
			totalFriendInput.close();

			//data = "12, 13, 14, 2, 8, 10, 12";
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
	
	private String getLatestCSVFile(String fullPath) {
		File accountFolderFile = new File(fullPath);

		String[] files = accountFolderFile.list(new FilenameFilter() {
		  @Override
		  public boolean accept(File current, String name) {
		    return new File(current, name).isFile();
		  }
		});
		
		Arrays.sort(files, Collections.reverseOrder());

		for(String file : files)
			if(file.endsWith(".csv"))
				return fullPath + File.separator + file;
		return null;
	}
	

    public Resolution downloadCsvFile() {
    	String openshift_data_dir = System.getenv().get("OPENSHIFT_DATA_DIR");
    	String account_folder = openshift_data_dir + File.separator + "twitter" + File.separator + getUserId();
    	setCsvFile(getLatestCSVFile(account_folder));
        InputStream is = null; 
        try { 
            is = new FileInputStream(new File(getCsvFile())); 
        } catch (FileNotFoundException ex) { 
           
        } 
        return new StreamingResolution("text/plain", is).setFilename("followers.csv"); 
    } 

}