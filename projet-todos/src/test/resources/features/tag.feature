
Feature: Tag Management

  Scenario: Retrieve all tags (paginable)
    Given there are 20 tags in the system
    When I request to retrieve the tags with page size 10 and page number 2
    Then I should receive a page of 10 tags
    And the total number of tags should be 20

  Scenario: Retrieve a tag by ID
    Given there is a tag with ID "123" and name "Tag 1"
    When I request to retrieve the tag with ID "123"
    Then I should receive the tag with ID "123" and name "Tag 1"

  Scenario: Retrieve a non-existent tag
    Given there is no tag with ID "456"
    When I request to retrieve the tag with ID "456"
    Then I should receive a 404 Not Found response

  Scenario: Create a new tag
    Given I have a tag with name "New Tag"
    When I request to create the new tag
    Then I should receive the created tag
    And the created tag should have a non-null ID
    And the created tag should have the name "New Tag"

  Scenario: Update an existing tag
    Given there is a tag with ID "456" and name "Ancien Nom du Tag"
    When I request to update the tag with ID "456" with the name "Nouveau Nom du Tag"
    Then I should receive the updated tag
    And the updated tag should have the ID "456" and the name "Nouveau Nom du Tag"

  Scenario: Update a non-existent tag
    Given there is no tag with ID "789"
    When I request to update the tag with ID "789" with the name "Nouveau Nom du Tag"
    Then I should receive a 404 Not Found response

  Scenario: Delete an existing tag
    Given there is a tag with ID "789" and name "Tag à supprimer"
    When I request to delete the tag with ID "789"
    Then I should receive a 204 No Content response
    And the tag with ID "789" should no longer exist in the system

  Scenario: Delete a non-existent tag
    Given there is no tag with ID "456"
    When I request to delete the tag with ID "456"
    Then I should receive a 404 Not Found response