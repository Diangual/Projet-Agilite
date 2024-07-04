package sn.ept.git.seminaire.cicd.cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import sn.ept.git.seminaire.cicd.models.TagDTO;
import sn.ept.git.seminaire.cicd.resources.TagResource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TagSteps {
    private final TagResource tagResource;
    private ResponseEntity<Page<TagDTO>> tagsResponse;
    private ResponseEntity<TagDTO> tagResponse;

    public TagSteps(TagResource tagResource) {
        this.tagResource = tagResource;
    }

    @Given("there are {int} tags in the system")
    public void thereAreTagsInTheSystem(int numTags) {
        for (int i = 1; i <= numTags; i++) {
            TagDTO tag = new TagDTO();
            tag.setId("" + i);
            tag.setName("Tag " + i);
            tagResource.create(tag); // Ajouter chaque tag dans le système
        }
    }

    @When("I request to retrieve the tags with page size {int} and page number {int}")
    public void iRequestToRetrieveTheTagsWithPageSizeAndPageNumber(int pageSize, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        tagsResponse = tagResource.findAll(pageable);
    }

    @Then("I should receive a page of {int} tags")
    public void iShouldReceiveAPageOfTags(int expectedPageSize) {
        assertThat(tagsResponse.getBody().getContent().size()).isEqualTo(expectedPageSize);
    }

    @Then("the total number of tags should be {int}")
    public void theTotalNumberOfTagsShouldBe(int expectedTotalTags) {
        assertThat(tagsResponse.getBody().getTotalElements()).isEqualTo(expectedTotalTags);
    }

    @Given("there is a tag with ID {string} and name {string}")
    public void thereIsATagWithIdAndName(String id, String name) {
        TagDTO tag = new TagDTO();
        tag.setId(id);
        tag.setName(name);
        tagResource.create(tag);
    }

    @Given("there is no tag with ID {string}")
    public void thereIsNoTagWithId(String id) {
        assertThatThrownBy(() -> tagResource.findById(id))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND);
    }

    @When("I request to retrieve the tag with ID {string}")
    public void iRequestToRetrieveTheTag(String id) {
        tagResponse = tagResource.findById(id);
    }

    @Then("I should receive the tag with ID {string} and name {string}")
    public void iShouldReceiveTheTagWithIdAndName(String id, String name) {
        assertThat(tagResponse).isNotNull();
        assertThat(tagResponse.getBody().getId()).isEqualTo(id);
        assertThat(tagResponse.getBody().getName()).isEqualTo(name);
    }

    @Then("I should receive a {int} Not Found response")
    public void iShouldReceiveANotFoundResponse(int expectedStatus) {
        assertThat(tagResponse.getStatusCode()).isEqualTo(HttpStatus.valueOf(expectedStatus));
    }

    @Given("I have a tag with name {string}")
    public void iHaveATagWithName(String name) {
        TagDTO newTag = new TagDTO();
        newTag.setName(name);
        tagResource.create(newTag);
    }

    @When("I request to create the new tag")
    public void iRequestToCreateTheNewTag() {
        TagDTO newTag = new TagDTO();
        newTag.setName("New Tag");
        tagResponse = tagResource.create(newTag);
    }

    @Then("I should receive the created tag")
    public void iShouldReceiveTheCreatedTag() {
        assertThat(tagResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(tagResponse.getBody()).isNotNull();
    }

    @Then("the created tag should have a non-null ID")
    public void theCreatedTagShouldHaveANonNullId() {
        assertThat(tagResponse.getBody().getId()).isNotNull();
    }

    @Then("the created tag should have the name {string}")
    public void theCreatedTagShouldHaveTheName(String expectedName) {
        assertThat(tagResponse.getBody().getName()).isEqualTo(expectedName);
    }

    @When("I request to update the tag with ID {string} with the name {string}")
    public void iRequestToUpdateTheTagWithIdWithTheName(String id, String newName) {
        TagDTO updatedTag = new TagDTO();
        updatedTag.setId(id);
        updatedTag.setName(newName);
        tagResponse = tagResource.update(updatedTag);
    }

    @Then("I should receive the updated tag")
    public void iShouldReceiveTheUpdatedTag() {
        assertThat(tagResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(tagResponse.getBody()).isNotNull();
    }

    @Then("the updated tag should have the ID {string} and the name {string}")
    public void theUpdatedTagShouldHaveTheIdAndTheName(String expectedId, String expectedName) {
        assertThat(tagResponse.getBody().getId()).isEqualTo(expectedId);
        assertThat(tagResponse.getBody().getName()).isEqualTo(expectedName);
    }

    @When("I request to delete the tag with ID {string}")
    public void iRequestToDeleteTheTagWithId(String id) {
        ResponseEntity<Void> deleteResponse = tagResource.delete(id);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Then("I should receive a {int} No Content response")
    public void iShouldReceiveANoContentResponse(int expectedStatus) {
        assertThat(tagResponse.getStatusCode()).isEqualTo(HttpStatus.valueOf(expectedStatus));
    }

    @Then("the tag with ID {string} should no longer exist in the system")
    public void theTagWithIdShouldNoLongerExistInTheSystem(String id) {
        assertThatThrownBy(() -> tagResource.findById(id))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND);
    }
}
