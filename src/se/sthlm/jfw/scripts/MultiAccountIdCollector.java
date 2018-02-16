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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import twitter4j.IDs;

import twitter4j.RateLimitStatus;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

public class MultiAccountIdCollector {

	public static Logger logger = LogManager.getRootLogger();


	public static void main(String[] args) {

		String fileName = "";
		boolean getFollowers = true;
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

				boolean twitterAccountOK = false;
				try {
					twitter.verifyCredentials();
					twitter.getSuggestedUserCategories();
					twitterAccountOK = true;
					System.out.println(fullPath + " twitter is ok.");
				} catch(Exception e) {
					System.out.println(fullPath + " twitter is not ok.");
				}
				if(twitterAccountOK)
					twitterList.add(twitter);
			}

			String accountToMine = "fwsthlm";
			if(args.length > 0)
				accountToMine = args[0];

			fileName = openshift_data_dir + File.separator + accountToMine + "-followers.txt";
			createFileIfItDoesNotExist(fileName);

			boolean continueLoop = true;
			int counter = 0;
			//while(continueLoop) {

			//Long startTime = System.currentTimeMillis();

			counter ++;

			try {
				long cursor = -1;
				IDs ids = null;
				int followers = 0;

				BufferedWriter newInput = new BufferedWriter(new FileWriter(fileName));
				System.out.println("Listing followers's ids.");
				boolean doItAgain = true;
				do {
					for(Twitter aTwitter : twitterList) {
						Map<String ,RateLimitStatus> rateLimitStatus = aTwitter.getRateLimitStatus();
						RateLimitStatus status = rateLimitStatus.get("/followers/ids");
						int remainingListLimit = status.getRemaining();
						System.out.println("Reamining calls to list: " + remainingListLimit);

						if(remainingListLimit > 0) {
							if(getFollowers)
								ids = aTwitter.getFollowersIDs(accountToMine, cursor);
							else
								ids = aTwitter.getFriendsIDs(accountToMine, cursor);
							for (long id : ids.getIDs()) {

								newInput.write(id + "\n");
								followers ++;

							}
						}
						System.out.println("Accounts: " + followers);
						logger.info("Got accounts: " + followers);
						doItAgain = (cursor = ids.getNextCursor()) != 0;
					}
					if(doItAgain)
						Thread.sleep(60000);
				} while (doItAgain);
				newInput.close();
			} catch (Exception e) {
				System.out.println("Exception: " + e.getMessage());
				e.printStackTrace();
				logger.error("Exception: " + e.getMessage());
			}
			logger.info("Accounts processed: " + counter);

		} catch(Exception e) {
			logger.error("Exception: " + e.getMessage());

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

	public static void getAllFollowersFromAnAccount(Twitter twitter, String accountToGrow, String accountToGetFollowersFrom) {
		try {
			long cursor = -1;
			IDs ids = null;
			int followers = 0;
			//focusattack
			BufferedWriter newInput = new BufferedWriter(new FileWriter(accountToGrow + "-" + accountToGetFollowersFrom + ".txt"));
			System.out.println("Listing followers's ids.");
			do {

				Map<String ,RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus();
				RateLimitStatus status = rateLimitStatus.get("/followers/ids");
				int remainingListLimit = status.getRemaining();
				System.out.println("Reamining calls to list: " + remainingListLimit);

				if(remainingListLimit > 0) {
					ids = twitter.getFollowersIDs(accountToGetFollowersFrom, cursor);
					for (long id : ids.getIDs()) {
						//System.out.println(id);
						///////////
						//extra
						//											User user = twitter.showUser(id); 
						//											System.out.println("" + user.getDescription());
						//											System.out.println("" + user.getName());
						//											String banner = user.getProfileBannerURL();
						//											//System.out.println("url: " + url);
						//											if(banner != null) {
						//												//getWebImage(url, 100 + followers);
						//												//saveImage(banner, 100 + followers, "banners");
						//											}
						////											String profilePicture = user.getProfileImageURL();
						//											String profilePicture = user.getOriginalProfileImageURL();
						//
						//											//System.out.println("url: " + url);
						//											if(profilePicture != null) {
						//												//getWebImage(url, 100 + followers);
						//												saveImage(profilePicture, 100 + followers, "profilePictures");
						//											}
						////////////
						newInput.write(id + "\n");
						followers ++;
						//					if(followers > 200)
						//						break;
					}
				}
				System.out.println("Followers: " + followers);
				Thread.sleep(60000);
			} while ((cursor = ids.getNextCursor()) != 0);
			newInput.close();
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}


}
