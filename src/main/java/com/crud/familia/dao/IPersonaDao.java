package com.crud.familia.dao;

import org.springframework.data.repository.CrudRepository;

import com.crud.familia.entity.Persona;

public interface IPersonaDao extends CrudRepository<Persona, Long>{

}
