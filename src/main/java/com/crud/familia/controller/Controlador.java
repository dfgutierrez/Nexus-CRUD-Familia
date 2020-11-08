package com.crud.familia.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;

import com.crud.familia.entity.Auditoria;
import com.crud.familia.entity.Persona;
import com.crud.familia.service.IAuditoriaService;
import com.crud.familia.service.IPersonaService;
import com.crud.familia.constans.Constants;
import com.crud.familia.dto.PersonaDto;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping(Constants.WS)
public class Controlador {

	@Autowired
	private IPersonaService service;

	@Autowired
	private IAuditoriaService auditoriaService;

	@GetMapping(Constants.METHOD_PERSONAS)
	public List<Persona> listar() {
		List<Persona> personas = service.fineAll();
		String endpoint = Constants.PATH + Constants.WS + Constants.METHOD_PERSONAS;

		logAuditoria("", new Gson().toJson(personas), endpoint, Constants.ACCION_LISTAR);

		return personas;
	}

	@GetMapping(Constants.METHOD_PERSONAS + "/{id}")
	public ResponseEntity<?> fineById(@PathVariable Long id) {
		ResponseEntity<?> responseEntity = null;
		String endpoint = Constants.PATH + Constants.WS + Constants.METHOD_PERSONAS + "/" + id;
		Persona persona = null;
		Map<String, Object> response = new HashMap<>();
		try {
			persona = service.fineById(id);
		} catch (DataAccessException e) {
			response.put("Mensaje", "Error consultando en la DB");
			response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			responseEntity = new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

			logAuditoria("", new Gson().toJson(responseEntity), endpoint, Constants.ACCION_BUSCAR);
			return responseEntity;
		}

		if (persona == null) {
			response.put("Mensaje", "La persona  ID: ".concat(id.toString().concat(" No existe en la DB!")));
			responseEntity = new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			logAuditoria("", new Gson().toJson(responseEntity), endpoint, Constants.ACCION_BUSCAR);
			return responseEntity;
		}

		responseEntity = new ResponseEntity<Persona>(persona, HttpStatus.OK);
		logAuditoria("", new Gson().toJson(responseEntity), endpoint, Constants.ACCION_BUSCAR);
		return responseEntity;
	}

	@PostMapping(Constants.METHOD_PERSONAS)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> create(@RequestBody List<PersonaDto> personas) {
		ResponseEntity<?> responseEntity = null;
		String endpoint = Constants.PATH + Constants.WS + Constants.METHOD_PERSONAS;
		List<Persona> listPersonas = new ArrayList<>();
		Map<String, Object> response = new HashMap<>();
		try {
			int codigoFamilia = (int) (Math.random() * 9999999 + 1);
			for (PersonaDto p : personas) {
				Persona personaRequest = new Persona();
				personaRequest.setNombres(p.getNombres());
				personaRequest.setApellidos(p.getApellidos());
				personaRequest.setDocumento(p.getDocumento());
				personaRequest.setParentesco(p.getParentesco());
				personaRequest.setCodigoFamilia(codigoFamilia);
				listPersonas.add(service.save(personaRequest));
			}
		} catch (DataAccessException e) {
			response.put("Mensaje", "Error al realizar el insert en la DB");
			response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			responseEntity = new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

			logAuditoria(new Gson().toJson(personas), new Gson().toJson(responseEntity), endpoint,
					Constants.ACCION_CREAR);
			return responseEntity;
		}

		response.put("Mensaje", "La familia a sido creada con exito!");
		response.put("Familia", listPersonas);
		responseEntity = new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
		logAuditoria(new Gson().toJson(personas), new Gson().toJson(responseEntity), endpoint, Constants.ACCION_CREAR);
		return responseEntity;
	}

	@PutMapping(Constants.METHOD_PERSONAS + "/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> update(@RequestBody PersonaDto p, @PathVariable Long id) {
		ResponseEntity<?> responseEntity = null;
		String endpoint = Constants.PATH + Constants.WS + Constants.METHOD_PERSONAS + "/" + id;

		Map<String, Object> response = new HashMap<>();
		Persona personaActual = service.fineById(id);
		if (personaActual == null) {
			response.put("Mensaje", "Error: no se pudo editar la persona ID: ".concat(id.toString()));
			responseEntity = new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			logAuditoria(new Gson().toJson(p), new Gson().toJson(responseEntity), endpoint,
					Constants.ACCION_ACTUALIZAR);
			return responseEntity;
		}

		try {
			personaActual.setNombres(p.getNombres());
			personaActual.setApellidos(p.getApellidos());
			personaActual.setParentesco(p.getParentesco());
			service.save(personaActual);
		} catch (DataAccessException e) {
			response.put("Mensaje", "Error al actualizar la persona en la DB");
			response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			responseEntity = new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

			logAuditoria(new Gson().toJson(p), new Gson().toJson(responseEntity), endpoint, Constants.ACCION_CREAR);
			return responseEntity;
		}

		response.put("Mensaje", "La persona a sido actualizada con exito!");
		response.put("Persona", personaActual);
		responseEntity = new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
		logAuditoria(new Gson().toJson(p), new Gson().toJson(responseEntity), endpoint, Constants.ACCION_CREAR);
		return responseEntity;
	}

	@DeleteMapping(Constants.METHOD_PERSONAS + "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<?> delete(@PathVariable Long id) {
		ResponseEntity<?> responseEntity = null;
		String endpoint = Constants.PATH + Constants.WS + Constants.METHOD_PERSONAS + "/" + id;
		Map<String, Object> response = new HashMap<>();
		try {
			service.delete(id);
		} catch (DataAccessException e) {
			response.put("Mensaje", "Error al eliminar la perdona en la DB");
			response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			responseEntity = new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			logAuditoria("", new Gson().toJson(responseEntity), endpoint, Constants.ACCION_ELIMINAR);
			return responseEntity;
		}

		response.put("Mensaje", "La persona fue eliminado con exito!");
		responseEntity = new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		logAuditoria("", new Gson().toJson(responseEntity), endpoint, Constants.ACCION_ELIMINAR);
		return responseEntity;
	}

	/**
	 * Este metodo se encarga de crear un nuevo hilo para insertar el log de la
	 * trazabilidad de cada metodo en la tabla auditoria
	 *
	 * @param request  Request de entrada en cada metodo.
	 * @param response Response para cada metodo .
	 * @param endPoint Indica la url que se va a ejecutar.
	 * @param accion   Lindica la accion a ejecutar..
	 * 
	 * @return
	 */
	private void logAuditoria(String request, String response, String endPoint, String accion) {
		Runnable nuevoHilo = () -> {
			Auditoria auditoria = new Auditoria();
			auditoria.setRequest(request);
			auditoria.setResponse(response);
			auditoria.setEndpoint(endPoint);
			auditoria.setAccion(accion);
			auditoriaService.save(auditoria);
		};

		new Thread(nuevoHilo).start();

	}
}
