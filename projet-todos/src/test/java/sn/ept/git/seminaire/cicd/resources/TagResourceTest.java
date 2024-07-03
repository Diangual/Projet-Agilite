package sn.ept.git.seminaire.cicd.resources;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sn.ept.git.seminaire.cicd.data.TagDTOTestData;
import sn.ept.git.seminaire.cicd.models.TagDTO;
import sn.ept.git.seminaire.cicd.services.ITagService;
import sn.ept.git.seminaire.cicd.utils.UrlMapping;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TagResourceTest {

    @Mock
    private ITagService service;

    @InjectMocks
    private TagResource tagResource;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(tagResource).build();
    }

    @Test
    public void testFindAll() throws Exception {
        when(service.findAll()).thenReturn(TagDTOTestData.getList());
        mockMvc.perform(get(UrlMapping.Tag.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1));
    }

    @Test
    public void testFindById() throws Exception {
        TagDTO expected = TagDTOTestData.getSingle();
        when(service.findById(expected.getId())).thenReturn(Optional.of(expected));
        mockMvc.perform(get(UrlMapping.Tag.FIND_BY_ID, expected.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expected.getId())))
                .andExpect(jsonPath("$.name", is(expected.getName())))
                .andExpect(jsonPath("$.description", is(expected.getDescription())));
    }

    @Test
    public void testCreate() throws Exception {
        TagDTO expected = TagDTOTestData.getNew();
        when(service.save(expected)).thenReturn(expected);
        mockMvc.perform(post(UrlMapping.Tag.ADD)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(expected)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(expected.getId())))
                .andExpect(jsonPath("$.name", is(expected.getName())))
                .andExpect(jsonPath("$.description", is(expected.getDescription())));
    }

    @Test
    public void testDelete() throws Exception {
        when(service.deleteById("id")).thenReturn(Optional.empty());
        mockMvc.perform(delete(UrlMapping.Tag.DELETE + "/id"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdate() throws Exception {
        TagDTO expected = TagDTOTestData.getUpdated();
        when(service.update("id", expected)).thenReturn(Optional.of(expected));
        mockMvc.perform(put(UrlMapping.Tag.UPDATE + "/id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(expected)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", is(expected.getId())))
                .andExpect(jsonPath("$.name", is(expected.getName())))
                .andExpect(jsonPath("$.description", is(expected.getDescription())));
    }
}






