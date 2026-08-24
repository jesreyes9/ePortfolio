import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;


public class ContactTest {
    
    private Contact contact;
    
    @BeforeEach
    void setUp() {
        contact = new Contact("1234567890", "John", "Doe", "1234567890", "123 Main St");
    }
    
    // Test successful contact creation
    @Test
    void testContactCreation() {
        assertNotNull(contact);
        assertEquals("1234567890", contact.getContactId());
        assertEquals("John", contact.getFirstName());
        assertEquals("Doe", contact.getLastName());
        assertEquals("1234567890", contact.getPhone());
        assertEquals("123 Main St", contact.getAddress());
    }
    
    // Test contactId validation - null
    @Test
    void testContactIdNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact(null, "John", "Doe", "1234567890", "123 Main St");
        });
    }
    
    // Test contactId validation - too long
    @Test
    void testContactIdTooLong() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact("12345678901", "John", "Doe", "1234567890", "123 Main St");
        });
    }
    
    // Test contactId exactly 10 characters (boundary test)
    @Test
    void testContactIdExactly10Characters() {
        Contact testContact = new Contact("1234567890", "John", "Doe", "1234567890", "123 Main St");
        assertEquals("1234567890", testContact.getContactId());
    }
    
    // Test firstName validation - null
    @Test
    void testFirstNameNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact("123", null, "Doe", "1234567890", "123 Main St");
        });
    }
    
    // Test firstName validation - too long
    @Test
    void testFirstNameTooLong() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact("123", "JohnJohnJohn", "Doe", "1234567890", "123 Main St");
        });
    }
    
    // Test firstName exactly 10 characters (boundary test)
    @Test
    void testFirstNameExactly10Characters() {
        Contact testContact = new Contact("123", "JohnJohnJo", "Doe", "1234567890", "123 Main St");
        assertEquals("JohnJohnJo", testContact.getFirstName());
    }
    
    // Test lastName validation - null
    @Test
    void testLastNameNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact("123", "John", null, "1234567890", "123 Main St");
        });
    }
    
    // Test lastName validation - too long
    @Test
    void testLastNameTooLong() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact("123", "John", "DoeDoeDoeD", "1234567890", "123 Main St");
        });
    }
    
    // Test lastName exactly 10 characters (boundary test)
    @Test
    void testLastNameExactly10Characters() {
        Contact testContact = new Contact("123", "John", "DoeDoeDoeD", "1234567890", "123 Main St");
        assertEquals("DoeDoeDoeD", testContact.getLastName());
    }
    
    // Test phone validation - null
    @Test
    void testPhoneNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact("123", "John", "Doe", null, "123 Main St");
        });
    }
    
    // Test phone validation - not exactly 10 digits
    @Test
    void testPhoneTooShort() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact("123", "John", "Doe", "123456789", "123 Main St");
        });
    }
    
    @Test
    void testPhoneTooLong() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact("123", "John", "Doe", "12345678901", "123 Main St");
        });
    }
    
    // Test phone validation - non-numeric characters
    @Test
    void testPhoneNonNumeric() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact("123", "John", "Doe", "123456789a", "123 Main St");
        });
    }
    
    // Test address validation - null
    @Test
    void testAddressNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact("123", "John", "Doe", "1234567890", null);
        });
    }
    
    // Test address validation - too long
    @Test
    void testAddressTooLong() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact("123", "John", "Doe", "1234567890", "1234567890123456789012345678901");
        });
    }
    
    // Test address exactly 30 characters (boundary test)
    @Test
    void testAddressExactly30Characters() {
        Contact testContact = new Contact("123", "John", "Doe", "1234567890", "123456789012345678901234567890");
        assertEquals("123456789012345678901234567890", testContact.getAddress());
    }
    
    // Test setters
    @Test
    void testSetFirstName() {
        contact.setFirstName("Jane");
        assertEquals("Jane", contact.getFirstName());
    }
    
    @Test
    void testSetFirstNameNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            contact.setFirstName(null);
        });
    }
    
    @Test
    void testSetFirstNameTooLong() {
        assertThrows(IllegalArgumentException.class, () -> {
            contact.setFirstName("JaneJaneJane");
        });
    }
    
    @Test
    void testSetLastName() {
        contact.setLastName("Smith");
        assertEquals("Smith", contact.getLastName());
    }
    
    @Test
    void testSetLastNameNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            contact.setLastName(null);
        });
    }
    
    @Test
    void testSetLastNameTooLong() {
        assertThrows(IllegalArgumentException.class, () -> {
            contact.setLastName("SmithSmithS");
        });
    }
    
    @Test
    void testSetPhone() {
        contact.setPhone("9876543210");
        assertEquals("9876543210", contact.getPhone());
    }
    
    @Test
    void testSetPhoneNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            contact.setPhone(null);
        });
    }
    
    @Test
    void testSetPhoneInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            contact.setPhone("123456789");
        });
    }
    
    @Test
    void testSetAddress() {
        contact.setAddress("456 Oak Ave");
        assertEquals("456 Oak Ave", contact.getAddress());
    }
    
    @Test
    void testSetAddressNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            contact.setAddress(null);
        });
    }
    
    @Test
    void testSetAddressTooLong() {
        assertThrows(IllegalArgumentException.class, () -> {
            contact.setAddress("12345678901234567890123456789012345");
        });
    }
}