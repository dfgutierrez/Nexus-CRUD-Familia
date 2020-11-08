package com.crud.familia.service;

import java.util.List;
import java.util.Optional;

import com.crud.familia.entity.Persona;

public interface IPersonaService {

	public List<Persona> fineAll();

	public Persona fineById(Long id);

	public Persona save(Persona p);

	public void delete(Long id);

}
