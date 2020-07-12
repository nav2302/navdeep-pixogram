package com.navdeep.pixogram.model;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String firstName;
	private String lastName;
	private String email;
	
	private String about;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dob;
	
	private String address;
	private String phone;
	
	private String password;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private Collection<Role> roles;

	@ManyToMany(cascade = {CascadeType.ALL})
	@JoinTable(name = "users_images", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "image_id", referencedColumnName = "id"))
	List<Image> image;
	

	@ManyToMany
	@JoinTable(name = "users_videos", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "video_id", referencedColumnName = "id"))
	List<Video> video;
	
	@ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "USER_RELATIONS", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "FOLLOWER_ID"))
	private Set<User> followers;
	
	@ManyToMany(mappedBy = "followers")
    private Set<User> following;
	
	@OneToMany(mappedBy="user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comments> comments;

	@OneToMany(mappedBy="user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CommentReplies> commentReplies;
	
	public User() {
	}

	public User(String firstName, String lastName, String email, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}

	public User(String firstName, String lastName, String email, String password, Collection<Role> roles) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.roles = roles;
	}

	public User(String firstName, String lastName, String email, String password, List<Image> image) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.image = image;
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}

	public List<Image> getImage() {
		return image;
	}

	public void addImage(Image image) {
		this.image.add(image);
	}
	
	public void removeImage(Image image) {
		this.image.remove(image);
	}
	
	public List<Video> getVideo() {
		return video;
	}

	public void addVideo(Video video) {
		this.video.add(video);
	}
	
	public void removeVideo(Video video) {
		this.video.remove(video);
	}
	
	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
    public Set<User> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<User> followers) {
        this.followers = followers;
    }

    public void addFollower(User follower) {
        followers.add(follower);
        follower.following.add(this);
    }
    
    public void removeFollower(User follower) {
    	followers.remove(follower);
    	follower.following.remove(this);
    }

    public Set<User> getFollowing() {
        return following;
    }

    public void setFollowing(Set<User> following) {
        this.following = following;
    }

    public void addFollowing(User followed) {
        followed.addFollower(this);
    }

	public List<Comments> getComments() {
		return comments;
	}

	public void setComments(List<Comments> comments) {
		this.comments = comments;
	}
	
	public void addComment(Comments comment) {
		this.comments.add(comment);
		comment.setUser(this);
	}
	
	public void removeComment(Comments comment) {
		this.comments.remove(comment);
		comment.setUser(null);
	}

	public List<CommentReplies> getCommentReplies() {
		return commentReplies;
	}

	public void setCommentReplies(List<CommentReplies> commentReplies) {
		this.commentReplies = commentReplies;
	}
	
	public void addCommentReply(CommentReplies commentReply) {
		this.commentReplies.add(commentReply);
		commentReply.setUser(this);
	}
	
	public void removeCommentReply(CommentReplies commentReply) {
		this.commentReplies.remove(commentReply);
		commentReply.setUser(null);
	}

	@Override
	public String toString() {
		return "User{" + "id=" + id + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\''
				+ ", email='" + email + '\'' + ", password='" + "*********" + '\'' + ", roles=" + roles + '}';
	}
}
