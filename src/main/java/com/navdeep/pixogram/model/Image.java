package com.navdeep.pixogram.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "image_model")
public class Image {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "type")
	private String type;

	@Column(name = "description")
	private String description;

	@Column(name = "caption")
	private String caption;
	
	@Column(name = "showImage")
	private String showImage;
	
	@Column(name = "no_of_likes")
	private Long no_of_likes;
	
	@Lob
	@Column(name = "pic")
	private byte[] pic;

	@ManyToMany(mappedBy = "image")
	List<User> users;
	
	@OneToMany(mappedBy="image", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comments> comments;

	public Image() {
	}
	
	public Image(Long id, String caption, String description, String name, byte[] pic, String showImage ,String type) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.description = description;
		this.caption = caption;
		this.showImage = showImage;
		this.pic = pic;
	}
	
	public Image(String name, String type, byte[] pic) {
		this.name = name;
		this.type = type;
		this.pic = pic;
	}

	public Image(String name, String type, byte[] pic, String showImage) {
		this.name = name;
		this.type = type;
		this.pic = pic;
		this.showImage = showImage;
	}

	public Long getId() {
		return this.id;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}	
	
	public String getShowImage() {
		return showImage;
	}

	public void setShowImage(String showImage) {
		this.showImage = showImage;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public byte[] getPic() {
		return this.pic;
	}

	public void setPic(byte[] pic) {
		this.pic = pic;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public Long getNo_of_likes() {
		return no_of_likes;
	}

	public void setNo_of_likes(Long no_of_likes) {
		this.no_of_likes = no_of_likes;
	}

	public List<Comments> getComments() {
		return comments;
	}

	public void setComments(List<Comments> comments) {
		this.comments = comments;
	}
	
	public void addComment(Comments comment) {
		this.comments.add(comment);
		comment.setImage(this);
	}
	
	public void removeComment(Comments comment) {
		this.comments.remove(comment);
		comment.setImage(null);
	}

}