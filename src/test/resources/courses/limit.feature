Feature: Limit of enrollments per course

  As a course administrator
  I want to set a limit on the number of enrollments per course
  So that I can manage course capacity effectively

  Scenario: Limit enrollments to a specific number
    Given a course "Introduction to Testing" with a limit of 3 enrollments
    Given 3 applicants attempt to enroll in the course
    When one more applicant attempts to enroll
    Then the applicamt should be rejected with a message "Enrollment limit reached: 3"