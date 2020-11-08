package com.crud.familia.service.impl;

import java.util.List;
import java.util.Optional;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crud.familia.dao.IPersonaDao;
import com.crud.familia.entity.Persona;
import com.crud.familia.service.IPersonaService;

@Service
public class PersonaService implements IPersonaService {
	
	@Autowired
	private IPersonaDao personaDao;

	@Override
	@Transactional(readOnly = true)
	public List<Persona> fineAll() {
		return (List<Persona>) personaDao.findAll();
	}

	@Override
	public Persona fineById(Long id) {
		return personaDao.findById(id).orElse(null);
	}

	@Override
	public Persona save(Persona p) {
		return personaDao.save(p);
	}

	@Override
	public void delete(Long id) {
		personaDao.deleteById(id);
		
	}

}
