package com.invest.honduras.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Service;

import com.invest.honduras.dao.ProjectDao;
import com.invest.honduras.dao.SolicitudeDao;
import com.invest.honduras.dao.StorageDao;
import com.invest.honduras.domain.map.FileMap;
import com.invest.honduras.domain.map.SolicitudeMap;
import com.invest.honduras.domain.model.Project;
import com.invest.honduras.domain.model.Storage;
import com.invest.honduras.domain.model.UserSession;
import com.invest.honduras.domain.model.process.FlowFile;
import com.invest.honduras.domain.model.solicitude.Solicitude;
import com.invest.honduras.http.response.solicitude.SolicitudeItemResponse;
import com.invest.honduras.service.SolicitudeStorageService;
import com.invest.honduras.util.UtilDocumentFilePart;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class SolicitudeStorageServiceImpl implements SolicitudeStorageService {

	@Autowired
	StorageDao solicitudeStorageDao;

	@Autowired
	SolicitudeDao solicitudeDao;


	@Autowired
	ProjectDao projectDao;


	@Override
	public Mono<Storage> findById(String id) {
		return solicitudeStorageDao.findById(id);
	}

	@Override
	public Mono<SolicitudeItemResponse> create(String idSolicitude, List<Part> part, UserSession userSession) {

		log.error("create be:{}" ,   Runtime.getRuntime().freeMemory()/1024);

		FlowFile flowFile = UtilDocumentFilePart.getParaneter(part);
		
		log.error("create af:{}" ,   Runtime.getRuntime().freeMemory()/1024);

		//return FilePartUtils.getByteArray(flowFile.getFilePart()).flatMap(bytes -> {
			Storage storage = new Storage();

			new FileMap().mapping( flowFile, storage);


		return saveFile(idSolicitude, storage,flowFile.getFilePart(), userSession);

//		});
	}
	
	private Mono<SolicitudeItemResponse> saveFile(String idSolicitude, Storage storage,  FilePart filePart, UserSession userSession){
		return solicitudeStorageDao.saveStorage( storage,filePart).flatMap(findStorage -> {
			return solicitudeDao.updateSolicitudeStorage(idSolicitude, findStorage, userSession).flatMap(item -> {

				SolicitudeItemResponse destiny = new SolicitudeItemResponse();

				new SolicitudeMap().mapping(item, destiny);
				updateProject(item, userSession);
				
				return Mono.just(destiny);
			});
		});
		
	}
	
	

	@Override
	public Mono<SolicitudeItemResponse> delete(String idSolicitude, String id, UserSession userSession) {


		return solicitudeStorageDao.delete(id).flatMap(flag -> {

			return solicitudeDao.deleteSolicitudeStorage(idSolicitude, id, userSession).flatMap(item -> {

				SolicitudeItemResponse destiny = new SolicitudeItemResponse();

				new SolicitudeMap().mapping(item, destiny);

				updateProject(item, userSession);
				return Mono.just(destiny);
			});

		});

	}
	
	private void updateProject(Solicitude solicitude, UserSession userSession) {
		Project project = new Project();
		project.setSolicitude(solicitude);
		projectDao.update(solicitude.getIdProject(), project, userSession).subscribe();
	}

}
