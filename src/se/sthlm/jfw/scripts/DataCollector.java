package se.sthlm.jfw.scripts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.util.Map;

import twitter4j.IDs;
import twitter4j.RateLimitStatus;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class DataCollector {

	public static void main(String[] args) {
		try {
    		String openshift_data_dir = System.getenv().get("OPENSHIFT_DATA_DIR");
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
    		for(String directory : directories) {
    			String fullPath = account_folder + File.separator + directory;
    			Twitter twitter = getTwitter(fullPath);
    			String followerFileName = fullPath + File.separator + "followers-" + System.currentTimeMillis() + ".txt";
    			getAllAccountsFromAnAccount(twitter, followerFileName, twitter.getScreenName(), true);
    			String friendsFileName = fullPath + File.separator + "friends-" + System.currentTimeMillis() + ".txt";
    			getAllAccountsFromAnAccount(twitter, friendsFileName, twitter.getScreenName(), false);

    			String totalFollowerFileName = fullPath + File.separator + "followers.txt";
    			createFileIfItDoesNotExist(totalFollowerFileName);
    			BufferedWriter totalFollowerFile = new BufferedWriter(new FileWriter(totalFollowerFileName, true));
    			totalFollowerFile.write(twitter.showUser(twitter.getId()).getFollowersCount() + "\n");
    			totalFollowerFile.close();
    			
    			String totalFriendsFileName = fullPath + File.separator + "friends.txt";
    			createFileIfItDoesNotExist(totalFriendsFileName);
    			BufferedWriter totalFriendsFile = new BufferedWriter(new FileWriter(totalFriendsFileName, true));
    			totalFriendsFile.write(twitter.showUser(twitter.getId()).getFriendsCount() + "\n");
    			totalFriendsFile.close();
    		}
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
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
	
	public static void getAllAccountsFromAnAccount(Twitter twitter, String fileName, String accountToGetFollowersFrom, boolean getFollowers) {
		try {
			long cursor = -1;
			IDs ids = null;
			int followers = 0;
			//focusattack
			BufferedWriter newInput = new BufferedWriter(new FileWriter(fileName));
			System.out.println("Listing followers's ids.");
			boolean doItAgain = true;
			do {

				Map<String ,RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus();
				RateLimitStatus status = rateLimitStatus.get("/followers/ids");
				int remainingListLimit = status.getRemaining();
				System.out.println("Reamining calls to list: " + remainingListLimit);

				if(remainingListLimit > 0) {
					if(getFollowers)
						ids = twitter.getFollowersIDs(accountToGetFollowersFrom, cursor);
					else
						ids = twitter.getFriendsIDs(accountToGetFollowersFrom, cursor);
					for (long id : ids.getIDs()) {

						newInput.write(id + "\n");
						followers ++;
						//					if(followers > 200)
						//						break;
					}
				}
				System.out.println("Accounts: " + followers);
				doItAgain = (cursor = ids.getNextCursor()) != 0;
				if(doItAgain)
					Thread.sleep(60000);
			} while (doItAgain);
			newInput.close();
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private static boolean createFileIfItDoesNotExist(String filename) throws Exception {
		File file = new File(filename);
		if(!file.exists())
			file.createNewFile();
		return true;
	}


}
