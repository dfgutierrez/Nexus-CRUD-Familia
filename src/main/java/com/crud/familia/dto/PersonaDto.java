package com.crud.familia.dto;



public class PersonaDto {

	private String nombres;


	private String apellidos;


	private String documento;


	private String parentesco;


	public PersonaDto() {
	}

	public PersonaDto( String nombres, String apellidos, String documento, String parentesco) {

		this.nombres = nombres;
		this.apellidos = apellidos;
		this.documento = documento;
		this.parentesco = parentesco;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getParentesco() {
		return parentesco;
	}

	public void setParentesco(String parentesco) {
		this.parentesco = parentesco;
	}


}
