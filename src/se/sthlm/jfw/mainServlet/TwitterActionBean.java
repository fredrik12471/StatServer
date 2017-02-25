package se.sthlm.jfw.mainServlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import java.util.TimeZone;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.util.Log;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

@UrlBinding("/twitter/{username}")
public class TwitterActionBean implements ActionBean {
    private ActionBeanContext context;
    private String accountIdentifier;
    private User twitterUser;
    private String followerData;
    private String friendData;
    private String csvFile;
    private String userId;
    private String dataDate;
    
    private String username;
	private static final Log logger = Log.getInstance(TwitterActionBean.class);

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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDataDate() {
		return dataDate;
	}
	public void setDataDate(String dataDate) {
		this.dataDate = dataDate;
	}
	
	@DefaultHandler
    public Resolution setupData() {
        String account_folder = "";
    	try {
    		
    		logger.info("In setupData");
    		//logger.info("I am alive!");
       		String openshift_data_dir = System.getenv().get("OPENSHIFT_DATA_DIR");
    		//String un = getContext().getRequest().getParameter("un");
       		
       		if(username != null) {
       			logger.info("username:" + username);
       			accountIdentifier = getAccountIdentifierFromUsername(username);
       		} else {
       			accountIdentifier = getContext().getRequest().getSession().getAttribute("accountIdentifier").toString();
       		}
   			logger.info("accountIdentifier:" + accountIdentifier);
    		account_folder = openshift_data_dir + File.separator + "twitter" + File.separator + accountIdentifier;
    		File accountFolderFile = new File(account_folder);
			if(!accountFolderFile.exists())
				accountFolderFile.mkdirs();
			//Twitter twitter = getTwitter(account_folder);
			//twitterUser = twitter.showUser(Long.valueOf(accountIdentifier));
			String userFileName = account_folder + File.separator + "user.twitter";
			
			Path file = Paths.get(userFileName);
			BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS",Locale.ENGLISH);
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(attr.lastModifiedTime().toMillis());
			sdf.setTimeZone(TimeZone.getTimeZone("CET"));
			dataDate = sdf.format(calendar.getTime());
			
			File objectFile = new File(userFileName);
			FileInputStream fi = new FileInputStream(objectFile);
			ObjectInputStream oi = new ObjectInputStream(fi);

			// Read objects
			twitterUser = (User) oi.readObject();

			oi.close();
			fi.close();

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
	
	private String getAccountIdentifierFromUsername(String username) throws Exception {
		String openshift_data_dir = System.getenv().get("OPENSHIFT_DATA_DIR");
		BufferedReader mapInput = new BufferedReader(new FileReader(openshift_data_dir + File.separator + "map.txt"));
		String mapInputLine = mapInput.readLine();
		while(mapInputLine != null) {
			if((mapInputLine.split(":")[0]).toLowerCase().equals(username.toLowerCase())) {
				mapInput.close();
				return mapInputLine.split(":")[1];
			}
			mapInputLine = mapInput.readLine();
		}
		mapInput.close();
		return null;
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