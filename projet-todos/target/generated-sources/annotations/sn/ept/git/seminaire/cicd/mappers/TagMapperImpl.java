package sn.ept.git.seminaire.cicd.mappers;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sn.ept.git.seminaire.cicd.entities.Tag;
import sn.ept.git.seminaire.cicd.models.TagDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-02T22:59:45+0000",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.39.0.v20240620-1855, environment: Java 17.0.11 (Eclipse Adoptium)"
)
@Component
public class TagMapperImpl implements TagMapper {

    @Override
    public Tag toEntity(TagDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Tag.TagBuilder<?, ?> tag = Tag.builder();

        tag.createdDate( dto.getCreatedDate() );
        tag.id( dto.getId() );
        tag.lastModifiedDate( dto.getLastModifiedDate() );
        tag.version( dto.getVersion() );
        tag.description( dto.getDescription() );
        tag.name( dto.getName() );

        return tag.build();
    }

    @Override
    public TagDTO toDTO(Tag tag) {
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
}
