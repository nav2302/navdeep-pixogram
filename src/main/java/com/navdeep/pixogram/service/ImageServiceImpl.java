package com.navdeep.pixogram.service;
import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.navdeep.pixogram.model.Image;
import com.navdeep.pixogram.model.User;
import com.navdeep.pixogram.repository.ImageRepository;

@Service
public class ImageServiceImpl implements ImageService {

	@Autowired
	ImageRepository imageRepository;
	
	@Autowired
	EntityManager em;

	@Override
	public Image findByName(String name) {
		Image image = imageRepository.findByName(name);
		return image;
	}

	@Override
	public Image save(Image image) {
		return imageRepository.save(image);
	}

	@Override
	public List<Image> getAllImages(User user) {
		return imageRepository.findAllByUsers(user);
	}

	@Override
	public Image findById(Long id) {
		return imageRepository.findById(id);
	}

	@Override
	public void updateHideOrShow(String showValue, Long id) {
		imageRepository.updateShow(showValue,id);
	}

	@Override
	public List<Image> getAllImagesWithShow(Long id) {
		Query query = em.createNativeQuery("SELECT image0_.id as id, image0_.caption as caption, image0_.description as description, image0_.name as name, image0_.no_of_likes as no_of_likes, image0_.pic as pic, image0_.show_image as show_image, image0_.type as type from image_model image0_ left outer join users_images users1_ on image0_.id=users1_.image_id left outer join user user2_ on users1_.user_id=user2_.id where user2_.id=?1 AND image0_.show_image='yes'",Image.class);
		query.setParameter(1, id);
		@SuppressWarnings("unchecked")
		List<Image> resultList = query.getResultList();
		return resultList;
	}

	@Override
	public List<Image> getAllImagesWithLimitShow(Long id) {
		Query query = em.createNativeQuery("SELECT image0_.id as id, image0_.caption as caption, image0_.description as description, image0_.name as name, image0_.no_of_likes as no_of_likes, image0_.pic as pic, image0_.show_image as show_image, image0_.type as type from image_model image0_ left outer join users_images users1_ on image0_.id=users1_.image_id left outer join user user2_ on users1_.user_id=user2_.id where user2_.id=?1 AND image0_.show_image='yes' LIMIT 5",Image.class);
		query.setParameter(1, id);
		@SuppressWarnings("unchecked")
		List<Image> resultList = query.getResultList();
		return resultList;
	}

	@Override
	public Long findPreviousImageId(Long userId, Long imageId) {
		Query query = em.createNativeQuery("select image0_.image_id as id1_0_ from users_images image0_ where image0_.user_id=?1 and image0_.image_id<?2 ORDER BY image0_.image_id DESC LIMIT 1;");
		query.setParameter(1, userId);
		query.setParameter(2, imageId);
		try {
			BigInteger biid = (BigInteger) query.getSingleResult();
			long previousImageId = biid.longValue();
			return previousImageId;
		}
		catch (NoResultException nre) {
			int i = -1;
			Long l = new Long(i);
			return Long.valueOf(l);
		}
	}
	
	@Override
	public Long findNextImageId(Long userId, Long imageId) {
		Query query = em.createNativeQuery("select image0_.image_id as id1_0_ from users_images image0_ where image0_.user_id=?1 and image0_.image_id>?2 ORDER BY image0_.image_id LIMIT 1;");
		query.setParameter(1, userId);
		query.setParameter(2, imageId);
		try {
			BigInteger biid = (BigInteger) query.getSingleResult();
			long nextImageId = biid.longValue();
			return nextImageId;
		}
		catch (NoResultException nre) {
			int i = -1;
			Long l = new Long(i);
			return Long.valueOf(l);
		}
	}

}
