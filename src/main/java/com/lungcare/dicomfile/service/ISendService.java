package com.lungcare.dicomfile.service;

import java.util.List;

import com.lungcare.dicomfile.entity.SendEntity;

public interface ISendService {
	public List<SendEntity> getAllSendEntity();
}
