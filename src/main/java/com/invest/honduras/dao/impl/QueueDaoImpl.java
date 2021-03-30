package com.invest.honduras.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.invest.honduras.dao.QueueDao;
import com.invest.honduras.domain.model.QueueTransaction;

@Component
public class QueueDaoImpl implements QueueDao {
	public static final String TX_NAME_COLLECTION = "queue_tx";

	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public void addTransaction(QueueTransaction tx) {

		mongoTemplate.insert(tx, TX_NAME_COLLECTION); // return's old person object

	}

}
