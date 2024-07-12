package sn.ept.git.seminaire.cicd.entities;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SetAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Todo.class)
@Generated("org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public abstract class Todo_ extends sn.ept.git.seminaire.cicd.entities.BaseEntity_ {

	
	/**
	 * @see sn.ept.git.seminaire.cicd.entities.Todo#description
	 **/
	public static volatile SingularAttribute<Todo, String> description;
	
	/**
	 * @see sn.ept.git.seminaire.cicd.entities.Todo#completed
	 **/
	public static volatile SingularAttribute<Todo, Boolean> completed;
	
	/**
	 * @see sn.ept.git.seminaire.cicd.entities.Todo#title
	 **/
	public static volatile SingularAttribute<Todo, String> title;
	
	/**
	 * @see sn.ept.git.seminaire.cicd.entities.Todo
	 **/
	public static volatile EntityType<Todo> class_;
	
	/**
	 * @see sn.ept.git.seminaire.cicd.entities.Todo#tags
	 **/
	public static volatile SetAttribute<Todo, Tag> tags;

	public static final String DESCRIPTION = "description";
	public static final String COMPLETED = "completed";
	public static final String TITLE = "title";
	public static final String TAGS = "tags";

}

