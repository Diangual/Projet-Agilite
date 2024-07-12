package sn.ept.git.seminaire.cicd.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import sn.ept.git.seminaire.cicd.models.TagDTO;
import sn.ept.git.seminaire.cicd.exceptions.ItemExistsException;
import sn.ept.git.seminaire.cicd.exceptions.ItemNotFoundException;
import sn.ept.git.seminaire.cicd.mappers.TagMapper;
import sn.ept.git.seminaire.cicd.entities.Tag;
import sn.ept.git.seminaire.cicd.repositories.TagRepository;
import sn.ept.git.seminaire.cicd.services.impl.TagServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TagMapper tagMapper;

    @InjectMocks
    private TagServiceImpl tagService;

    private Tag tag;
    private TagDTO tagDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        tag = new Tag();
        tag.setId("1");
        tag.setName("Polytechnique");
        tag.setDescription("Ecole Polytechnique de Thiès");

        tagDTO = new TagDTO();
        tagDTO.setId("1");
        tagDTO.setName("Polytechnique");
        tagDTO.setDescription("Ecole Polytechnique de Thiès");
    }

    @Test
    public void save_savesNewTag() {
        // Arrange
        when(tagRepository.findByName(tagDTO.getName())).thenReturn(Optional.empty());
        when(tagMapper.toEntity(any(TagDTO.class))).thenReturn(tag);
        when(tagRepository.saveAndFlush(any(Tag.class))).thenReturn(tag);
        when(tagMapper.toDTO(any(Tag.class))).thenReturn(tagDTO);

        // Act
        TagDTO savedTag = tagService.save(tagDTO);

        // Assert
        assertNotNull(savedTag);
        assertEquals(tagDTO.getName(), savedTag.getName());
        verify(tagRepository).findByName(tagDTO.getName());
        verify(tagRepository).saveAndFlush(tag);
    }

    @Test
    public void save_throwsExceptionWhenTagExists() {
        // Arrange
        when(tagRepository.findByName(tagDTO.getName())).thenReturn(Optional.of(tag));

        // Act & Assert
        assertThrows(ItemExistsException.class, () -> tagService.save(tagDTO));
        verify(tagRepository).findByName(tagDTO.getName());
    }

    @Test
    public void delete_deletesExistingTag() {
        // Arrange
        when(tagRepository.findById("1")).thenReturn(Optional.of(tag));

        // Act
        tagService.delete("1");

        // Assert
        verify(tagRepository).findById("1");
        verify(tagRepository).deleteById("1");
    }

    @Test
    public void delete_throwsExceptionWhenTagNotFound() {
        // Arrange
        when(tagRepository.findById("1")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ItemNotFoundException.class, () -> tagService.delete("1"));
        verify(tagRepository).findById("1");
    }

    @Test
    public void findById_retrievesTagById() {
        // Arrange
        when(tagRepository.findById("1")).thenReturn(Optional.of(tag));
        when(tagMapper.toDTO(any(Tag.class))).thenReturn(tagDTO);

        // Act
        Optional<TagDTO> foundTag = tagService.findById("1");

        // Assert
        assertTrue(foundTag.isPresent());
        assertEquals(tagDTO.getName(), foundTag.get().getName());
        verify(tagRepository).findById("1");
    }

    @Test
    public void findById_returnsEmptyWhenTagNotFound() {
        // Arrange
        when(tagRepository.findById("1")).thenReturn(Optional.empty());

        // Act
        Optional<TagDTO> foundTag = tagService.findById("1");

        // Assert
        assertFalse(foundTag.isPresent());
        verify(tagRepository).findById("1");
    }

    @Test
    public void findAll_returnsAllTags() {
        // Arrange
        when(tagRepository.findAll()).thenReturn(List.of(tag));
        when(tagMapper.toDTO(any(Tag.class))).thenReturn(tagDTO);

        // Act
        List<TagDTO> tags = tagService.findAll();

        // Assert
        assertNotNull(tags);
        assertEquals(1, tags.size());
        assertEquals(tagDTO.getName(), tags.get(0).getName());
        verify(tagRepository).findAll();
    }

    @Test
    public void findAll_returnsEmptyListWhenNoTags() {
        // Arrange
        when(tagRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<TagDTO> tags = tagService.findAll();

        // Assert
        assertNotNull(tags);
        assertTrue(tags.isEmpty());
        verify(tagRepository).findAll();
    }

    @Test
    public void findAll_withPageable_returnsPagedTags() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Tag> tagPage = new PageImpl<>(List.of(tag), pageable, 1);
        when(tagRepository.findAll(pageable)).thenReturn(tagPage);
        when(tagMapper.toDTO(any(Tag.class))).thenReturn(tagDTO);

        // Act
        Page<TagDTO> tags = tagService.findAll(pageable);

        // Assert
        assertNotNull(tags);
        assertEquals(1, tags.getTotalElements());
        assertEquals(tagDTO.getName(), tags.getContent().get(0).getName());
        verify(tagRepository).findAll(pageable);
    }

    @Test
    public void update_updatesExistingTag() {
        // Arrange
        when(tagRepository.findById("1")).thenReturn(Optional.of(tag));
        when(tagRepository.findByNameWithIdNotEquals(tagDTO.getName(), "1")).thenReturn(Optional.empty());
        when(tagRepository.saveAndFlush(any(Tag.class))).thenReturn(tag);
        when(tagMapper.toDTO(any(Tag.class))).thenReturn(tagDTO);

        // Act
        TagDTO updatedTag = tagService.update("1", tagDTO);

        // Assert
        assertNotNull(updatedTag);
        assertEquals(tagDTO.getName(), updatedTag.getName());
        verify(tagRepository).findById("1");
        verify(tagRepository).findByNameWithIdNotEquals(tagDTO.getName(), "1");
        verify(tagRepository).saveAndFlush(any(Tag.class));
    }

    @Test
    public void update_throwsExceptionWhenTagNotFound() {
        // Arrange
        when(tagRepository.findById("1")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ItemNotFoundException.class, () -> tagService.update("1", tagDTO));
        verify(tagRepository).findById("1");
    }

    @Test
    public void update_throwsExceptionWhenTagWithSameNameExists() {
        // Arrange
        when(tagRepository.findById("1")).thenReturn(Optional.of(tag));
        when(tagRepository.findByNameWithIdNotEquals(tagDTO.getName(), "1")).thenReturn(Optional.of(tag));

        // Act & Assert
        assertThrows(ItemExistsException.class, () -> tagService.update("1", tagDTO));
        verify(tagRepository).findById("1");
        verify(tagRepository).findByNameWithIdNotEquals(tagDTO.getName(), "1");
    }

    @Test
    public void deleteAll_deletesAllTags() {
        // Act
        tagService.deleteAll();

        // Assert
        verify(tagRepository).deleteAll();
    }
}
