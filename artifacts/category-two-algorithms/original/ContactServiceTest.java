import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;


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
}
