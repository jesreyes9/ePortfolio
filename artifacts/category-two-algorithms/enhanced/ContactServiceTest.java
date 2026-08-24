import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;


public class ContactServiceTest {
    
    private ContactService contactService;
    private Contact contact1;
    private Contact contact2;
    
    @BeforeEach
    void setUp() {
        contactService = new ContactService();
        contact1 = new Contact("1234567890", "John", "Doe", "1234567890", "123 Main St");
        contact2 = new Contact("0987654321", "Jane", "Smith", "0987654321", "456 Oak Ave");
    }
    
    // Test adding contacts with unique IDs
    @Test
    void testAddContact() {
        contactService.addContact(contact1);
        assertEquals(1, contactService.getContactCount());
        assertTrue(contactService.contactExists("1234567890"));
        assertEquals(contact1, contactService.getContact("1234567890"));
    }
    
    @Test
    void testAddMultipleContacts() {
        contactService.addContact(contact1);
        contactService.addContact(contact2);
        assertEquals(2, contactService.getContactCount());
        assertTrue(contactService.contactExists("1234567890"));
        assertTrue(contactService.contactExists("0987654321"));
    }
    
    // Test adding contact with duplicate ID
    @Test
    void testAddContactDuplicateId() {
        contactService.addContact(contact1);
        Contact duplicateContact = new Contact("1234567890", "Jane", "Smith", "0987654321", "456 Oak Ave");
        
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.addContact(duplicateContact);
        });
        
        assertEquals(1, contactService.getContactCount());
    }
    
    // Test adding null contact
    @Test
    void testAddNullContact() {
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.addContact(null);
        });
    }
    
    // Test deleting contacts by contactId
    @Test
    void testDeleteContact() {
        contactService.addContact(contact1);
        contactService.addContact(contact2);
        assertEquals(2, contactService.getContactCount());
        
        contactService.deleteContact("1234567890");
        assertEquals(1, contactService.getContactCount());
        assertFalse(contactService.contactExists("1234567890"));
        assertTrue(contactService.contactExists("0987654321"));
    }
    
    // Test deleting non-existent contact
    @Test
    void testDeleteNonExistentContact() {
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.deleteContact("nonexistent");
        });
    }
    
    // Test deleting contact with null ID
    @Test
    void testDeleteContactNullId() {
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.deleteContact(null);
        });
    }
    
    // Test updating firstName
    @Test
    void testUpdateFirstName() {
        contactService.addContact(contact1);
        contactService.updateFirstName("1234567890", "Johnny");
        
        Contact updatedContact = contactService.getContact("1234567890");
        assertEquals("Johnny", updatedContact.getFirstName());
        assertEquals("Doe", updatedContact.getLastName()); // Other fields unchanged
    }
    
    @Test
    void testUpdateFirstNameNonExistentContact() {
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.updateFirstName("nonexistent", "John");
        });
    }
    
    @Test
    void testUpdateFirstNameInvalidData() {
        contactService.addContact(contact1);
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.updateFirstName("1234567890", null);
        });
    }
    
    // Test updating lastName
    @Test
    void testUpdateLastName() {
        contactService.addContact(contact1);
        contactService.updateLastName("1234567890", "Johnson");
        
        Contact updatedContact = contactService.getContact("1234567890");
        assertEquals("Johnson", updatedContact.getLastName());
        assertEquals("John", updatedContact.getFirstName()); // Other fields unchanged
    }
    
    @Test
    void testUpdateLastNameNonExistentContact() {
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.updateLastName("nonexistent", "Johnson");
        });
    }
    
    @Test
    void testUpdateLastNameInvalidData() {
        contactService.addContact(contact1);
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.updateLastName("1234567890", null);
        });
    }
    
    // Test updating phone number
    @Test
    void testUpdatePhoneNumber() {
        contactService.addContact(contact1);
        contactService.updatePhoneNumber("1234567890", "9876543210");
        
        Contact updatedContact = contactService.getContact("1234567890");
        assertEquals("9876543210", updatedContact.getPhone());
        assertEquals("John", updatedContact.getFirstName()); // Other fields unchanged
    }
    
    @Test
    void testUpdatePhoneNumberNonExistentContact() {
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.updatePhoneNumber("nonexistent", "9876543210");
        });
    }
    
    @Test
    void testUpdatePhoneNumberInvalidData() {
        contactService.addContact(contact1);
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.updatePhoneNumber("1234567890", "123456789"); // Invalid phone
        });
    }
    
    // Test updating address
    @Test
    void testUpdateAddress() {
        contactService.addContact(contact1);
        contactService.updateAddress("1234567890", "789 Pine St");
        
        Contact updatedContact = contactService.getContact("1234567890");
        assertEquals("789 Pine St", updatedContact.getAddress());
        assertEquals("John", updatedContact.getFirstName()); // Other fields unchanged
    }
    
    @Test
    void testUpdateAddressNonExistentContact() {
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.updateAddress("nonexistent", "789 Pine St");
        });
    }
    
    @Test
    void testUpdateAddressInvalidData() {
        contactService.addContact(contact1);
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.updateAddress("1234567890", null);
        });
    }
    
    // Test getContact
    @Test
    void testGetContact() {
        contactService.addContact(contact1);
        Contact retrievedContact = contactService.getContact("1234567890");
        assertEquals(contact1, retrievedContact);
    }
    
    @Test
    void testGetNonExistentContact() {
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.getContact("nonexistent");
        });
    }
    
    @Test
    void testGetContactNullId() {
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.getContact(null);
        });
    }
    
    // Test contactExists
    @Test
    void testContactExists() {
        assertFalse(contactService.contactExists("1234567890"));
        contactService.addContact(contact1);
        assertTrue(contactService.contactExists("1234567890"));
        contactService.deleteContact("1234567890");
        assertFalse(contactService.contactExists("1234567890"));
    }
    
    // Test getContactCount
    @Test
    void testGetContactCount() {
        assertEquals(0, contactService.getContactCount());
        contactService.addContact(contact1);
        assertEquals(1, contactService.getContactCount());
        contactService.addContact(contact2);
        assertEquals(2, contactService.getContactCount());
        contactService.deleteContact("1234567890");
        assertEquals(1, contactService.getContactCount());
    }

    // ----- Secondary last-name index -----

    @Test
    void testGetContactsByLastName() {
        contactService.addContact(contact1);
        contactService.addContact(contact2);

        List<Contact> results = contactService.getContactsByLastName("Doe");
        assertEquals(1, results.size());
        assertEquals(contact1, results.get(0));
    }

    @Test
    void testGetContactsByLastNameCaseInsensitive() {
        contactService.addContact(contact1);
        List<Contact> results = contactService.getContactsByLastName("doe");
        assertEquals(1, results.size());
        assertEquals(contact1, results.get(0));
    }

    @Test
    void testGetContactsByLastNameMultipleMatches() {
        contactService.addContact(contact1);
        Contact contact3 = new Contact("1111111111", "Jim", "Doe", "1112223333", "1 Elm St");
        contactService.addContact(contact3);

        List<Contact> results = contactService.getContactsByLastName("Doe");
        assertEquals(2, results.size());
        assertTrue(results.contains(contact1));
        assertTrue(results.contains(contact3));
    }

    @Test
    void testGetContactsByLastNameNoMatch() {
        contactService.addContact(contact1);
        assertTrue(contactService.getContactsByLastName("Nobody").isEmpty());
    }

    @Test
    void testGetContactsByLastNameNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.getContactsByLastName(null);
        });
    }

    @Test
    void testLastNameIndexUpdatesAfterDelete() {
        contactService.addContact(contact1);
        contactService.deleteContact("1234567890");
        assertTrue(contactService.getContactsByLastName("Doe").isEmpty());
    }

    @Test
    void testLastNameIndexUpdatesAfterUpdateLastName() {
        contactService.addContact(contact1);
        contactService.updateLastName("1234567890", "Smith");

        assertTrue(contactService.getContactsByLastName("Doe").isEmpty());
        List<Contact> results = contactService.getContactsByLastName("Smith");
        assertEquals(1, results.size());
        assertEquals("1234567890", results.get(0).getContactId());
    }

    // ----- Trie-based prefix search -----

    @Test
    void testSearchByPrefixMatchesFirstName() {
        contactService.addContact(contact1);
        contactService.addContact(contact2);

        List<Contact> results = contactService.searchByPrefix("Jo");
        assertEquals(1, results.size());
        assertEquals(contact1, results.get(0));
    }

    @Test
    void testSearchByPrefixMatchesLastName() {
        contactService.addContact(contact1);
        contactService.addContact(contact2);

        List<Contact> results = contactService.searchByPrefix("Sm");
        assertEquals(1, results.size());
        assertEquals(contact2, results.get(0));
    }

    @Test
    void testSearchByPrefixCaseInsensitive() {
        contactService.addContact(contact1);
        List<Contact> results = contactService.searchByPrefix("jo");
        assertEquals(1, results.size());
        assertEquals(contact1, results.get(0));
    }

    @Test
    void testSearchByPrefixMultipleMatches() {
        contactService.addContact(contact1); // John Doe
        Contact contact3 = new Contact("1111111111", "Johnny", "Appleseed", "1112223333", "1 Elm St");
        contactService.addContact(contact3);

        List<Contact> results = contactService.searchByPrefix("John");
        assertEquals(2, results.size());
        assertTrue(results.contains(contact1));
        assertTrue(results.contains(contact3));
    }

    @Test
    void testSearchByPrefixNoMatch() {
        contactService.addContact(contact1);
        assertTrue(contactService.searchByPrefix("xyz").isEmpty());
    }

    @Test
    void testSearchByPrefixEmptyStringReturnsAll() {
        contactService.addContact(contact1);
        contactService.addContact(contact2);
        assertEquals(2, contactService.searchByPrefix("").size());
    }

    @Test
    void testSearchByPrefixNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.searchByPrefix(null);
        });
    }

    @Test
    void testTrieUpdatesAfterDelete() {
        contactService.addContact(contact1);
        contactService.deleteContact("1234567890");
        assertTrue(contactService.searchByPrefix("Jo").isEmpty());
    }

    @Test
    void testTrieUpdatesAfterUpdateFirstName() {
        contactService.addContact(contact1);
        contactService.updateFirstName("1234567890", "Zack");

        assertTrue(contactService.searchByPrefix("John").isEmpty());
        List<Contact> results = contactService.searchByPrefix("Za");
        assertEquals(1, results.size());
        assertEquals("1234567890", results.get(0).getContactId());
    }

    // ----- Merge sort by last name then first name -----

    @Test
    void testGetContactsSortedByName() {
        Contact aaron = new Contact("1111111111", "Aaron", "Adams", "1112223333", "1 Elm St");
        Contact brian = new Contact("2222222222", "Brian", "Doe", "2223334444", "2 Elm St");
        contactService.addContact(contact1); // John Doe
        contactService.addContact(contact2); // Jane Smith
        contactService.addContact(aaron);
        contactService.addContact(brian);

        List<Contact> sorted = contactService.getContactsSortedByName();

        assertEquals(4, sorted.size());
        assertEquals("Adams", sorted.get(0).getLastName());
        assertEquals("Doe", sorted.get(1).getLastName());
        assertEquals("Brian", sorted.get(1).getFirstName()); // Doe, Brian before Doe, John
        assertEquals("Doe", sorted.get(2).getLastName());
        assertEquals("John", sorted.get(2).getFirstName());
        assertEquals("Smith", sorted.get(3).getLastName());
    }

    @Test
    void testGetContactsSortedByNameEmpty() {
        assertTrue(contactService.getContactsSortedByName().isEmpty());
    }

    @Test
    void testGetContactsSortedByNameSingleContact() {
        contactService.addContact(contact1);
        List<Contact> sorted = contactService.getContactsSortedByName();
        assertEquals(1, sorted.size());
        assertEquals(contact1, sorted.get(0));
    }
}