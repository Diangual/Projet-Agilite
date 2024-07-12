package sn.ept.git.seminaire.cicd.entities;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SetAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Tag.class)
@Generated("org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public abstract class Tag_ extends sn.ept.git.seminaire.cicd.entities.BaseEntity_ {

	
	/**
	 * @see sn.ept.git.seminaire.cicd.entities.Tag#name
	 **/
	public static volatile SingularAttribute<Tag, String> name;
	
	/**
	 * @see sn.ept.git.seminaire.cicd.entities.Tag#description
	 **/
	public static volatile SingularAttribute<Tag, String> description;
	
	/**
	 * @see sn.ept.git.seminaire.cicd.entities.Tag#todos
	 **/
	public static volatile SetAttribute<Tag, Todo> todos;
	
	/**
	 * @see sn.ept.git.seminaire.cicd.entities.Tag
	 **/
	public static volatile EntityType<Tag> class_;

	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String TODOS = "todos";

}

