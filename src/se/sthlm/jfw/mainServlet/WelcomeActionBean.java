package se.sthlm.jfw.mainServlet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.instagram4j.DefaultInstagramClient;
import org.instagram4j.InstagramClient;
import org.instagram4j.Result;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.common.collect.ImmutableMap;

import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import se.sthlm.jfw.mainServlet.data.SocialMediaUser;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * http://rosebud-scomer.rhcloud.com/Rosebud/se/sthlm/jfw/mainServlet/Twitter.action
 * @author JFW
 */
@UrlBinding("/welcome")
public class WelcomeActionBean implements ActionBean {

	private ActionBeanContext context;
	private String authLink;
	private String facebookLink;
	private String googleLink;
	private String instagramLink;

	private String username;
	private String userId;
	private String oauth_token;
	private String oauth_verifier;

	private String message;

	private String timelineRetweeterMin;
	private boolean timelineRetweeter;
	private boolean receiveDMOnUnfollow;

	private String serverName;

	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }

	public ActionBeanContext getContext() { return context; }
	public void setContext(ActionBeanContext context) { this.context = context; }

	public String getAuthLink() { return authLink; }
	public void setAuthLink(String authLink) { this.authLink = authLink; }

	public String getFacebookLink() { return facebookLink; }
	public void setFacebookLink(String facebookLink) { this.facebookLink = facebookLink; }

	public String getInstagramLink() { return instagramLink; }
	public void setInstagramLink(String instagramLink) { this.instagramLink = instagramLink; }

	public String getGoogleLink() { return googleLink; }
	public void setGoogleLink(String googleLink) { this.googleLink = googleLink; }

	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }

	public String getOauth_token() { return oauth_token; }
	public void setOauth_token(String oauth_token) { this.oauth_token = oauth_token; }

	public String getOauth_verifier() { return oauth_verifier; }
	public void setOauth_verifier(String oauth_verifier) { this.oauth_verifier = oauth_verifier; }

	public String getTimelineRetweeterMin() { return timelineRetweeterMin; }
	public void setTimelineRetweeterMin(String timelineRetweeterMin) { this.timelineRetweeterMin = timelineRetweeterMin; }

	public String getServerName() { return serverName; }
	public void setServerName(String serverName) { this.serverName = serverName; }


	public boolean isTimelineRetweeter() {
		return timelineRetweeter;
	}
	public void setTimelineRetweeter(boolean timelineRetweeter) {
		this.timelineRetweeter = timelineRetweeter;
	}
	public boolean isReceiveDMOnUnfollow() {
		return receiveDMOnUnfollow;
	}
	public void setReceiveDMOnUnfollow(boolean receiveDMOnUnfollow) {
		this.receiveDMOnUnfollow = receiveDMOnUnfollow;
	}
	@DefaultHandler
	public Resolution addition() {

		boolean twitterAccountLoggedIn = false;
		String openshift_data_dir = System.getenv().get("OPENSHIFT_DATA_DIR");
		String account_folder = openshift_data_dir + File.separator + "admin";
		File setupFile = new File(account_folder + File.separator + "setup.txt");
		try {
			if(!setupFile.exists())
				setupFile.createNewFile();
			BufferedReader setupFileReader = new BufferedReader(new FileReader(setupFile));
			String localServerName = setupFileReader.readLine();
			if(localServerName == null) {
				localServerName = "http://rosenknopp-scomer.rhcloud.com/welcome/";
			}
			setServerName(localServerName);
			setupFileReader.close();
		} catch(Exception e) {

		}

		try {

			Twitter twitter = new TwitterFactory().getInstance();
			//PromoBooster
			twitter.setOAuthConsumer("c6oK4XyCSMIwMb9am8g10938B", "yVM8gD4AyZxVh29IMYSsPnBPxJsh9VpQhyByxQBl559XpntqHc");
			//twitter.setOAuthConsumer("ytjvV2T8q8P8lDvgangbdYylO", "BUXaaJvNylWN9Fr03k5BAxr8Ni6dhIJQRMiVKEcaaHQBvlTHyP");
			//twitter.setOAuthConsumer("6RVGBdbDFl6RzN3PVPEXvLhDP", "AfgyRQkeW9q9byg2Wjwk79OTtRT7vljZKMX2jz4sc9vdmSLKjl");
			//twitter.setOAuthConsumer("FvJHU8pK0bA9RuRDZOK5otymZ", "FZYUXWxBoWegT245dpMssr1RX00Dm8dMhREVOnntFreE5Xmdbn");
			String verifier = oauth_verifier;

			String tokenCopy = (String) getContext().getRequest().getSession().getAttribute("requestToken_token");
			String tokenSecretCopy = (String) getContext().getRequest().getSession().getAttribute("requestToken_secret");

			String code = (String) getContext().getRequest().getParameter("code");
			String state = (String) getContext().getRequest().getParameter("state");

			if(verifier != null && !verifier.equals("")) {

				RequestToken requestToken = new RequestToken(tokenCopy, tokenSecretCopy);
				AccessToken accessToken = twitter.getOAuthAccessToken(requestToken,verifier);
				twitter.setOAuthAccessToken(accessToken);

				ConfigurationBuilder cb = new ConfigurationBuilder();
				cb.setDebugEnabled(true)
				.setOAuthConsumerKey("c6oK4XyCSMIwMb9am8g10938B")
				//					.setOAuthConsumerKey("ytjvV2T8q8P8lDvgangbdYylO")
				//					.setOAuthConsumerKey("6RVGBdbDFl6RzN3PVPEXvLhDP")
				//					.setOAuthConsumerKey("FvJHU8pK0bA9RuRDZOK5otymZ")
				.setOAuthConsumerSecret("yVM8gD4AyZxVh29IMYSsPnBPxJsh9VpQhyByxQBl559XpntqHc")
				//					.setOAuthConsumerSecret("BUXaaJvNylWN9Fr03k5BAxr8Ni6dhIJQRMiVKEcaaHQBvlTHyP")
				//					.setOAuthConsumerSecret("AfgyRQkeW9q9byg2Wjwk79OTtRT7vljZKMX2jz4sc9vdmSLKjl")
				//					.setOAuthConsumerSecret("FZYUXWxBoWegT245dpMssr1RX00Dm8dMhREVOnntFreE5Xmdbn")
				.setOAuthAccessToken(accessToken.getToken())
				.setOAuthAccessTokenSecret(accessToken.getTokenSecret());
				TwitterFactory tf = new TwitterFactory(cb.build());
				Twitter twitter2 = tf.getInstance();
				User twitterUser = twitter2.verifyCredentials();
				username = twitterUser.getScreenName();
				userId = "" + twitterUser.getId();

				String twitterAccountFolder = openshift_data_dir + File.separator + "twitter" + File.separator + userId;
				File accountFolderFile = new File(twitterAccountFolder);
				if(!accountFolderFile.exists()) {
					//accountFolderFile.createNewFile();
					accountFolderFile.mkdirs();
				}

				//String openshift_data_dir = System.getenv().get("OPENSHIFT_DATA_DIR");
				BufferedWriter newInput = new BufferedWriter(new FileWriter(accountFolderFile + File.separator + "account.txt"));

				newInput.write("c6oK4XyCSMIwMb9am8g10938B" + "\n");
				//		    	newInput.write("6RVGBdbDFl6RzN3PVPEXvLhDP" + "\n");
				//newInput.write("FvJHU8pK0bA9RuRDZOK5otymZ" + "\n");
				newInput.write("yVM8gD4AyZxVh29IMYSsPnBPxJsh9VpQhyByxQBl559XpntqHc" + "\n");
				//		    	newInput.write("AfgyRQkeW9q9byg2Wjwk79OTtRT7vljZKMX2jz4sc9vdmSLKjl" + "\n");
				//newInput.write("FZYUXWxBoWegT245dpMssr1RX00Dm8dMhREVOnntFreE5Xmdbn" + "\n");
				newInput.write(accessToken.getToken() + "\n");
				newInput.write(accessToken.getTokenSecret() + "\n");
				newInput.close();

				createDashboardPage(twitterUser);
				twitterAccountLoggedIn = true;

			} else if(code != null && !code.equals("")) {

				if(state != null && state.equals("google")) {

					// get the access token by post to Google
					String body = post("https://accounts.google.com/o/oauth2/token", ImmutableMap.<String,String>builder()
							.put("code", code)
							.put("client_id", "1007371847340-3chkveepi4aqeaf48i4jfpgqu9d8q87o.apps.googleusercontent.com")
							.put("client_secret", "mDG_mZRHWm3KThoGCHwYbsQq")
							.put("redirect_uri", "http://rosebud-scomer.rhcloud.com/welcome/")
							.put("grant_type", "authorization_code").build());

					JSONObject jsonObject = null;

					// get the access token from json and request info from Google
					try {
						jsonObject = (JSONObject) new JSONParser().parse(body);
					} catch (ParseException e) {
						throw new RuntimeException("Unable to parse json " + body);
					}

					username = jsonObject.toString();
					// google tokens expire after an hour, but since we requested offline access we can get a new token without user involvement via the refresh token
					String accessToken = (String) jsonObject.get("access_token");

					//String openshift_data_dir = System.getenv().get("OPENSHIFT_DATA_DIR");
					BufferedWriter newInput = new BufferedWriter(new FileWriter(openshift_data_dir + File.separator + "ScomerService" + File.separator + accessToken + " google token.txt"));
					newInput.write(accessToken + "\n");
					newInput.close();

					//username = "Google login";

				} else if(state != null && state.equals("instagram")) {




					// get the access token by post to Instagram
					String body = post("https://api.instagram.com/oauth/access_token", ImmutableMap.<String,String>builder()
							.put("code", code)
							.put("client_id", "21be52cec5e8428eb6a551cb83706709")
							.put("client_secret", "e5d3775698cc412984feea1820dce20c")
							.put("redirect_uri", "http://rosenknopp-scomer.rhcloud.com/welcome/")
							.put("grant_type", "authorization_code").build());

					JSONObject jsonObject = null;

					// get the access token from json and request info from Google
					try {
						jsonObject = (JSONObject) new JSONParser().parse(body);
					} catch (ParseException e) {
						throw new RuntimeException("Unable to parse json " + body);
					}

					//username = jsonObject.toString();
					// google tokens expire after an hour, but since we requested offline access we can get a new token without user involvement via the refresh token
					String accessToken = (String) jsonObject.get("access_token");
					
					
				    InstagramClient client = new DefaultInstagramClient("21be52cec5e8428eb6a551cb83706709", "e5d3775698cc412984feea1820dce20c", 
				    		accessToken);
				    
			    	Result<org.instagram4j.entity.User> result = client.getCurrentUser();
			    	org.instagram4j.entity.User instagramUser = result.getData();
					//JSONObject user = (JSONObject) jsonObject.get("user");
					//String instagramUsername = (String) user.get("username");
					username = instagramUser.getUsername();
					userId = instagramUser.getId();
					
					String instagramAccountFolder = openshift_data_dir + File.separator + "instagram" + File.separator + userId;
					File accountFolderFile = new File(instagramAccountFolder);
					if(!accountFolderFile.exists()) {
						//accountFolderFile.createNewFile();
						accountFolderFile.mkdirs();
					}
					
					BufferedWriter newInput = new BufferedWriter(new FileWriter(accountFolderFile + File.separator + "account.txt"));
					newInput.write(accessToken + "\n");
					newInput.close();
					
					createDashboardPage(instagramUser);
			
					
					return new RedirectResolution("/instagram/" + instagramUser.getUsername());

					//username = "Google login";
				} else {
					Facebook facebook = (Facebook) getContext().getRequest().getSession().getAttribute("facebook");
					facebook.getOAuthAccessToken(code);            
					facebook4j.auth.AccessToken token = facebook.getOAuthAccessToken();

					//String openshift_data_dir = System.getenv().get("OPENSHIFT_DATA_DIR");
					BufferedWriter newInput = new BufferedWriter(new FileWriter(openshift_data_dir + File.separator + "ScomerService" + File.separator + facebook.getName() + " facebook token.txt"));
					newInput.write(token.getToken() + "\n");
					newInput.close();

					username = facebook.getName();
				}

			} else {

				RequestToken requestToken;
				String callbackURL = getServerName();
				//		    	String callbackURL = "http://rosenknopp-scomer.rhcloud.com/welcome/";
				//		    	String callbackURL = "http://instagram-rosebud.rhcloud.com/welcome/";
				try {

					requestToken = twitter.getOAuthRequestToken(callbackURL);
					//String authUrl = requestToken.getAuthorizationURL();
					String authUrl = requestToken.getAuthenticationURL();

					authLink = authUrl;

					getContext().getRequest().getSession().setAttribute("requestToken_token", requestToken.getToken());
					getContext().getRequest().getSession().setAttribute("requestToken_secret", requestToken.getTokenSecret());

				} catch(Exception e) {
					authLink = e.getMessage();
					//Ignored?
				}


				Facebook facebook = new FacebookFactory().getInstance();
				facebook.setOAuthAppId("716297481817313", "d484c37342f4677cf02bd6b392abc4bc");
				facebook.setOAuthPermissions("publish_actions");

				getContext().getRequest().getSession().setAttribute("facebook", facebook);
				facebookLink = facebook.getOAuthAuthorizationURL(callbackURL);



				StringBuilder oauthUrl = new StringBuilder().append("https://accounts.google.com/o/oauth2/auth")
						.append("?client_id=").append("1007371847340-3chkveepi4aqeaf48i4jfpgqu9d8q87o.apps.googleusercontent.com") // the client id from the api console registration
						.append("&response_type=code")
						.append("&scope=openid%20email") // scope is the api permissions we are requesting
						.append("&state=google")
						.append("&redirect_uri=http://rosebud-scomer.rhcloud.com/welcome/") // the servlet that google redirects to after authorization
						.append("&access_type=offline") // here we are asking to access to user's data while they are not signed in
						.append("&approval_prompt=force"); // this requires them to verify which account to use, if they are already signed in
				googleLink = oauthUrl.toString();




				instagramLink = "https://api.instagram.com/oauth/authorize/?client_id=21be52cec5e8428eb6a551cb83706709&redirect_uri=http://rosenknopp-scomer.rhcloud.com/welcome/&response_type=code&state=instagram&scope=public_content+follower_list";


				return new ForwardResolution("/WEB-INF/index.jsp");


			}

		} catch(Exception e) {
			username = e.getMessage();
		}

		timelineRetweeterMin = "50";
		timelineRetweeter = false;
		receiveDMOnUnfollow = true;

		//return new ForwardResolution("/WEB-INF/welcome.jsp");



		if(twitterAccountLoggedIn) {
			getContext().getRequest().getSession().setAttribute("accountIdentifier", userId);
			return new RedirectResolution("/twitter");
		} else {
			return new ForwardResolution("/WEB-INF/welcome.jsp");   
		}
	}

	//	public Resolution save() {
	//		return new ForwardResolution("/WEB-INF/welcome.jsp");
	//	}

	private void createDashboardPage(org.instagram4j.entity.User instagramUser) throws Exception {
		String openshift_data_dir = System.getenv().get("OPENSHIFT_DATA_DIR");
		String instagramAccountFolder = openshift_data_dir + File.separator + "instagram" + File.separator + instagramUser.getId();
		String userFileName = instagramAccountFolder + File.separator + "user.instagram";
		createFileIfItDoesNotExist(userFileName);
		FileOutputStream f = new FileOutputStream(new File(userFileName));
		ObjectOutputStream o = new ObjectOutputStream(f);

		SocialMediaUser socialMediaUser = new SocialMediaUser(instagramUser);
		o.writeObject(socialMediaUser);

		o.close();
		f.close();
		
		boolean nameIsFoundInMapFile = false;
		String mapFileName = openshift_data_dir + File.separator + "instagram-map.txt";
		BufferedReader input = new BufferedReader(new FileReader(mapFileName));
		String line = input.readLine();
		while(line != null) {
			if(line.toLowerCase().startsWith(instagramUser.getUsername().toLowerCase()))
				nameIsFoundInMapFile = true;
			line = input.readLine();
		}
		if(!nameIsFoundInMapFile) {
			BufferedWriter newInput = new BufferedWriter(new FileWriter(mapFileName, true));
			newInput.write(instagramUser.getUsername() + ":" + instagramUser.getId() + "\n");
			newInput.close();
		}
		input.close();
	}
	
	
	
	private void createDashboardPage(User twitterUser) throws Exception {
		String openshift_data_dir = System.getenv().get("OPENSHIFT_DATA_DIR");
		String twitterAccountFolder = openshift_data_dir + File.separator + "twitter" + File.separator + twitterUser.getId();
		String userFileName = twitterAccountFolder + File.separator + "user.twitter";
		createFileIfItDoesNotExist(userFileName);
		FileOutputStream f = new FileOutputStream(new File(userFileName));
		ObjectOutputStream o = new ObjectOutputStream(f);

		o.writeObject(twitterUser);

		o.close();
		f.close();
		
		boolean nameIsFoundInMapFile = false;
		String mapFileName = openshift_data_dir + File.separator + "map.txt";
		BufferedReader input = new BufferedReader(new FileReader(mapFileName));
		String line = input.readLine();
		while(line != null) {
			if(line.toLowerCase().startsWith(twitterUser.getScreenName().toLowerCase()))
				nameIsFoundInMapFile = true;
			line = input.readLine();
		}
		if(!nameIsFoundInMapFile) {
			BufferedWriter newInput = new BufferedWriter(new FileWriter(mapFileName, true));
			newInput.write(twitterUser.getScreenName() + ":" + twitterUser.getId() + "\n");
			newInput.close();
		}
		input.close();
	}
	
	// makes a POST request to url with form parameters and returns body as a string
	public String post(String url, Map<String,String> formParameters) throws ClientProtocolException, IOException { 
		HttpPost request = new HttpPost(url);

		List <NameValuePair> nvps = new ArrayList <NameValuePair>();

		for (String key : formParameters.keySet()) {
			nvps.add(new BasicNameValuePair(key, formParameters.get(key))); 
		}

		request.setEntity(new UrlEncodedFormEntity(nvps));

		return execute(request);
	}

	// makes request and checks response code for 200
	private String execute(HttpRequestBase request) throws ClientProtocolException, IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(request);

		HttpEntity entity = response.getEntity();
		String body = EntityUtils.toString(entity);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Expected 200 but got " + response.getStatusLine().getStatusCode() + ", with body " + body);
		}

		return body;
	}
	
	private static boolean createFileIfItDoesNotExist(String filename) throws Exception {
		File file = new File(filename);
		if(!file.exists())
			file.createNewFile();
		return true;
	}

}