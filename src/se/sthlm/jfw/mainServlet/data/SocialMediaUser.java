package se.sthlm.jfw.mainServlet.data;

import java.io.Serializable;

import org.instagram4j.entity.User;

public class SocialMediaUser implements Serializable {

	private static final long serialVersionUID = 8186554185219342497L;
	String id;
	String screenName;
	String realName;
	String description;	
	String imageURL;
	int followers;
	int friends;
	int postCount;
	String activationDate;

	public SocialMediaUser() {
		
	}

	public SocialMediaUser(User instagramUser) {
		this.setId(instagramUser.getId());
		this.setScreenName(instagramUser.getUsername());
		this.setRealName(instagramUser.getFullName());
		this.setDescription(instagramUser.getBio());
		this.setImageURL(instagramUser.getProfilePicture());
		this.setFollowers(instagramUser.getCounts().getFollowedBy());
		this.setFriends(instagramUser.getCounts().getFollows());
		this.setPostCount(instagramUser.getCounts().getMedia());
		this.setActivationDate("N/A");
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public int getFollowers() {
		return followers;
	}

	public void setFollowers(int followers) {
		this.followers = followers;
	}

	public int getFriends() {
		return friends;
	}

	public void setFriends(int friends) {
		this.friends = friends;
	}

	public String getActivationDate() {
		return activationDate;
	}

	public void setActivationDate(String activationDate) {
		this.activationDate = activationDate;
	}
	
	public int getPostCount() {
		return postCount;
	}

	public void setPostCount(int postCount) {
		this.postCount = postCount;
	}
}
