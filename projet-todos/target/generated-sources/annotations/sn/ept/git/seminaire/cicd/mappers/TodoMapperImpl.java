package sn.ept.git.seminaire.cicd.mappers;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sn.ept.git.seminaire.cicd.entities.Tag;
import sn.ept.git.seminaire.cicd.entities.Todo;
import sn.ept.git.seminaire.cicd.models.TagDTO;
import sn.ept.git.seminaire.cicd.models.TodoDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-04T18:04:43+0000",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.39.0.v20240620-1855, environment: Java 17.0.11 (Eclipse Adoptium)"
)
@Component
public class TodoMapperImpl implements TodoMapper {

    @Override
    public Todo toEntity(TodoDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Todo.TodoBuilder<?, ?> todo = Todo.builder();

        todo.createdDate( dto.getCreatedDate() );
        todo.id( dto.getId() );
        todo.lastModifiedDate( dto.getLastModifiedDate() );
        todo.version( dto.getVersion() );
        todo.completed( dto.isCompleted() );
        todo.description( dto.getDescription() );
        todo.tags( tagDTOSetToTagSet( dto.getTags() ) );
        todo.title( dto.getTitle() );

        return todo.build();
    }

    @Override
    public TodoDTO toDTO(Todo todo) {
        if ( todo == null ) {
            return null;
        }

        TodoDTO.TodoDTOBuilder<?, ?> todoDTO = TodoDTO.builder();

        todoDTO.createdDate( todo.getCreatedDate() );
        todoDTO.id( todo.getId() );
        todoDTO.lastModifiedDate( todo.getLastModifiedDate() );
        todoDTO.version( todo.getVersion() );
        todoDTO.completed( todo.isCompleted() );
        todoDTO.description( todo.getDescription() );
        todoDTO.tags( tagSetToTagDTOSet( todo.getTags() ) );
        todoDTO.title( todo.getTitle() );

        return todoDTO.build();
    }

    protected Tag tagDTOToTag(TagDTO tagDTO) {
        if ( tagDTO == null ) {
            return null;
        }

        Tag.TagBuilder<?, ?> tag = Tag.builder();

        tag.createdDate( tagDTO.getCreatedDate() );
        tag.id( tagDTO.getId() );
        tag.lastModifiedDate( tagDTO.getLastModifiedDate() );
        tag.version( tagDTO.getVersion() );
        tag.description( tagDTO.getDescription() );
        tag.name( tagDTO.getName() );

        return tag.build();
    }

    protected Set<Tag> tagDTOSetToTagSet(Set<TagDTO> set) {
        if ( set == null ) {
            return null;
        }

        Set<Tag> set1 = new LinkedHashSet<Tag>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( TagDTO tagDTO : set ) {
            set1.add( tagDTOToTag( tagDTO ) );
        }

        return set1;
    }

    protected TagDTO tagToTagDTO(Tag tag) {
        if ( tag == null ) {
            return null;
        }

        TagDTO.TagDTOBuilder<?, ?> tagDTO = TagDTO.builder();

        tagDTO.createdDate( tag.getCreatedDate() );
        tagDTO.id( tag.getId() );
        tagDTO.lastModifiedDate( tag.getLastModifiedDate() );
        tagDTO.version( tag.getVersion() );
        tagDTO.description( tag.getDescription() );
        tagDTO.name( tag.getName() );

        return tagDTO.build();
    }

    protected Set<TagDTO> tagSetToTagDTOSet(Set<Tag> set) {
        if ( set == null ) {
            return null;
        }

        Set<TagDTO> set1 = new LinkedHashSet<TagDTO>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Tag tag : set ) {
            set1.add( tagToTagDTO( tag ) );
        }

        return set1;
    }
}
