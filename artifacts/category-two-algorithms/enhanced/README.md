# CS-320-13955 Software Test, Automation, and QA

## Key Reflection Questions

### How can I ensure that my code, program, or software is functional and secure?

My approach to ensuring functionality and security centers on comprehensive testing and defensive programming practices. In this project, I achieved 95% test coverage by implementing boundary value testing, such as testing names with exactly 10 characters ("TenCharNam") versus those exceeding the limit ("ElevenCharacterName"). I used parameterized testing with JUnit to efficiently test multiple invalid scenarios, like phone numbers that were too short ("123456789"), too long ("12345678901"), or contained invalid characters ("notanumber").

Security comes through rigorous input validation. Every setter and constructor in my Contact class validates data before accepting it, throwing IllegalArgumentExceptions for null values, improper lengths, and invalid formats. I learned that testing edge cases like null inputs, boundary values, and malformed data is crucial for catching vulnerabilities early. The assertThrows method became invaluable for verifying that my validation logic properly rejected invalid inputs.

Moving forward, I plan to maintain comprehensive test suites, participate in code reviews, and keep documentation current. I've developed a skeptical mindset where I actively try to break my own code rather than assuming it will work, which helps identify potential security issues before they reach production.

### How do I interpret user needs and incorporate them into a program?

Throughout this project, I translated business requirements into technical specifications by carefully analyzing constraints and use cases. The requirement for "unique contact IDs" led me to implement HashMap-based storage with duplicate detection. When the specification called for "exactly 10 digits" for phone numbers, I created regex validation (`\\d{10}`) rather than just checking length.

I learned to think beyond the happy path scenarios. While users expect to successfully create and update contacts, they also need meaningful error messages when something goes wrong. My exception handling provides clear feedback like "Contact ID cannot be null and must be 10 characters or less" rather than generic error messages.

The CRUD operations directly map to user stories: users need to add new contacts, find existing ones, update information when it changes, and remove outdated entries. I designed the ContactService class to make these operations intuitive while maintaining data integrity through validation.

### How do I approach designing software?

My design approach emphasizes separation of concerns and defensive programming. I separated the Contact data model from the ContactService business logic, making the system more maintainable and testable. The Contact class focuses solely on data validation and storage, while ContactService handles collection management and business rules.

I used the Arrange-Act-Assert pattern consistently in my tests, which kept them organized and readable. For example, in `testValidContactCreation`, I arranged test data (contactId = "C001", firstName = "Jane"), acted by creating the contact object, and asserted the results matched expectations.

The design also considers real-world constraints. Making contactId final prevents accidental modification after creation, which maintains referential integrity in the service layer. Using HashMap for storage provides O(1) lookup performance for contact retrieval operations.

Testing drove many design decisions. When I discovered timezone handling issues during date validation testing, it prompted me to reconsider how the system would behave across different environments. This experience taught me that good design isn't just about meeting current requirements—it's about building systems that are robust, maintainable, and extensible.


