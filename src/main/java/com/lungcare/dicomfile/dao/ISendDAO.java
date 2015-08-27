package com.lungcare.dicomfile.dao;

import java.util.List;

import com.lungcare.dicomfile.entity.SendEntity;

public interface ISendDAO {
	public List<SendEntity> getAllSendEntity();
}
