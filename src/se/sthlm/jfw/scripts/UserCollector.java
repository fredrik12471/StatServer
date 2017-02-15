package se.sthlm.jfw.scripts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

public class UserCollector {
	public static void main(String[] args) {
		try {
			
    		String openshift_data_dir = System.getenv().get("OPENSHIFT_DATA_DIR");
    		//String openshift_data_dir = ".";

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
    		
    		for(String directory : directories) {
    			String fullPath = account_folder + File.separator + directory;
    			String latestFollowerFile = getLatestFollowerFile(fullPath);
    			if(latestFollowerFile == null)
    				continue;
	    		String orderFile = latestFollowerFile.replace(".txt", ".csv");
	    		createFileIfItDoesNotExist(orderFile);
	    		BufferedReader orderInput = new BufferedReader(new FileReader(getLatestFollowerFile(fullPath)));
	    		boolean continueLoop = true;
	    		while(continueLoop) {
	    			for(Twitter aTwitter : twitterList) {
		    			String accountId = orderInput.readLine();
		    			if(accountId == null) {
		    				continueLoop = false;
		    				break;
		    			}
		    			User twitterUser = null;
		    			try {
		    				twitterUser = aTwitter.showUser(Long.valueOf(accountId));
		    			} catch(Exception e) {
		    				//Ignored for now, users may have been suspended
		    				//e.printStackTrace();
		    			}
		    			if(twitterUser != null) {
			    			BufferedWriter userOutput = new BufferedWriter(new FileWriter(orderFile, true));
			    			
			    			userOutput.write(cleanUpString(twitterUser.getName()) + ";" +
			    					         "@" + cleanUpString(twitterUser.getScreenName()) + ";" + 
			    					         cleanUpString(twitterUser.getURL()) + ";" +
			    								   cleanUpString(twitterUser.getDescription()) + ";" +
			    								   cleanUpString(twitterUser.getLocation()) + ";" +
			    								   twitterUser.getFollowersCount() + ";" +
			    								   twitterUser.getFriendsCount() + ";" +
			    								   userIsActive(twitterUser) + ";" +
			    			"\n");
			    			// inactive/active, and each accounts bio, location, # followers,
			    			userOutput.close();
		    			}
	    			}
	    			Thread.sleep(1000);
	    		}
	    		orderInput.close();
    		}
	    		
    	} catch(Exception e) {
    	}
	}
	
	private static String getLatestFollowerFile(String fullPath) {
		File accountFolderFile = new File(fullPath);

		String[] files = accountFolderFile.list(new FilenameFilter() {
		  @Override
		  public boolean accept(File current, String name) {
		    return new File(current, name).isFile();
		  }
		});
		
		Arrays.sort(files, Collections.reverseOrder());

		for(String file : files)
			if(file.startsWith("followers-"))
				return fullPath + File.separator + file;
		return null;
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
		if(string == null)
			string = "";
		return string.replace("\n", " ").replace("\r", "").replace(",", "");
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
