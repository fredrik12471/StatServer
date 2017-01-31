package se.sthlm.jfw.scripts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

public class UserCollector {

	public static void main(String[] args) {
		try {
			
    		//String openshift_data_dir = System.getenv().get("OPENSHIFT_DATA_DIR");
    		String openshift_data_dir = ".";


    		String account_folder = openshift_data_dir + File.separator + "twitter";
    		File accountFolderFile = new File(account_folder);
    		//File file = new File("/path/to/directory");
    		String[] directories = accountFolderFile.list(new FilenameFilter() {
    		  @Override
    		  public boolean accept(File current, String name) {
    		    return new File(current, name).isDirectory();
    		  }
    		});
    		//System.out.println(Arrays.toString(directories));
    		ArrayList<Twitter> twitterList = new ArrayList<Twitter>();
    		for(String directory : directories) {
    			String fullPath = account_folder + File.separator + directory;
    			Twitter twitter = getTwitter(fullPath);
    			twitterList.add(twitter);
    		}

    		String order_folder = openshift_data_dir + File.separator + "orders";
    		String orderFile = order_folder + File.separator + "followers.csv";
    		createFileIfItDoesNotExist(orderFile);
    		
    		BufferedReader orderInput = new BufferedReader(new FileReader(order_folder + File.separator + "order.txt"));
    		boolean continueLoop = true;
    		while(continueLoop) {
    			for(Twitter aTwitter : twitterList) {
	    			String accountId = orderInput.readLine();
	    			if(accountId == null) {
	    				continueLoop = false;
	    				break;
	    			}
	    			User twitterUser = aTwitter.showUser(Long.valueOf(accountId));
	    			BufferedWriter userOutput = new BufferedWriter(new FileWriter(orderFile, true));
	    			
	    			userOutput.write("@" + cleanUpString(twitterUser.getScreenName()) + ";" + 
	    								   cleanUpString(twitterUser.getDescription()) + ";" +
	    								   cleanUpString(twitterUser.getLocation()) + ";" +
	    								   twitterUser.getFollowersCount() + ";" +
	    								   twitterUser.getFriendsCount() + ";" +
	    								   userIsActive(twitterUser) + ";" +
	    			"\n");
	    			// inactive/active, and each accounts bio, location, # followers,
	    			userOutput.close();
    			}
    			Thread.sleep(1000);
    		}
    		orderInput.close();
    		
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
	}
	
	private static boolean userIsActive(User twitterUser) {
		if(twitterUser.getStatus() == null)
			return false;
		Date referenceDate = new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(referenceDate); 
		c.add(Calendar.MONTH, -1);
		
		return twitterUser.getStatus().getCreatedAt().after(c.getTime());
	}

	private static String cleanUpString(String string) {
		
		return string.replace("\n", ",").replace("\r", "");
	}

	private static Twitter getTwitter(String accountFolder) throws Exception {
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
	
	private static boolean createFileIfItDoesNotExist(String filename) throws Exception {
		File file = new File(filename);
		if(!file.exists())
			file.createNewFile();
		return true;
	}

}
