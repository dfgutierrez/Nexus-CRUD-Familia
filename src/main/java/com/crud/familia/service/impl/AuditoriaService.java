package com.crud.familia.service.impl;

import java.util.List;
import java.util.Optional;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crud.familia.dao.IAuditoriaDao;
import com.crud.familia.dao.IPersonaDao;
import com.crud.familia.entity.Auditoria;
import com.crud.familia.entity.Persona;
import com.crud.familia.service.IAuditoriaService;
import com.crud.familia.service.IPersonaService;

@Service
public class AuditoriaService implements IAuditoriaService {
	
	@Autowired
	private IAuditoriaDao uditoriaDao;

	@Override
	public Auditoria save(Auditoria a) {
		return uditoriaDao.save(a);
	}



}
