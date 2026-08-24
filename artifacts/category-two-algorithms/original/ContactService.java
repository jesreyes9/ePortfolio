import java.util.HashMap;
import java.util.Map;

public class ContactService {
    private Map<String, Contact> contacts;

    /**
     * Constructor initializes the contact storage
     */
    public ContactService() {
        this.contacts = new HashMap<>();
    }

    /**
     * Adds a contact with unique ID
     * @param contact The contact to add
     * @throws IllegalArgumentException if contact ID already exists
     */
    public void addContact(Contact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Contact cannot be null");
        }

        if (contacts.containsKey(contact.getContactId())) {
            throw new IllegalArgumentException("Contact ID already exists");
        }

        contacts.put(contact.getContactId(), contact);
    }

    /**
     * Deletes a contact by contactId
     * @param contactId The ID of the contact to delete
     * @throws IllegalArgumentException if contact ID does not exist
     */
    public void deleteContact(String contactId) {
        if (contactId == null) {
            throw new IllegalArgumentException("Contact ID cannot be null");
        }

        if (!contacts.containsKey(contactId)) {
            throw new IllegalArgumentException("Contact ID does not exist");
        }

        contacts.remove(contactId);
    }

    /**
     * Updates the first name of a contact
     * @param contactId The ID of the contact to update
     * @param firstName The new first name
     * @throws IllegalArgumentException if contact ID does not exist
     */
    public void updateFirstName(String contactId, String firstName) {
        Contact contact = getContact(contactId);
        contact.setFirstName(firstName);
    }

    /**
     * Updates the last name of a contact
     * @param contactId The ID of the contact to update
     * @param lastName The new last name
     * @throws IllegalArgumentException if contact ID does not exist
     */
    public void updateLastName(String contactId, String lastName) {
        Contact contact = getContact(contactId);
        contact.setLastName(lastName);
    }

    /**
     * Updates the phone number of a contact
     * @param contactId The ID of the contact to update
     * @param phone The new phone number
     * @throws IllegalArgumentException if contact ID does not exist
     */
    public void updatePhoneNumber(String contactId, String phone) {
        Contact contact = getContact(contactId);
        contact.setPhone(phone);
    }

    /**
     * Updates the address of a contact
     * @param contactId The ID of the contact to update
     * @param address The new address
     * @throws IllegalArgumentException if contact ID does not exist
     */
    public void updateAddress(String contactId, String address) {
        Contact contact = getContact(contactId);
        contact.setAddress(address);
    }

    /**
     * Retrieves a contact by ID
     * @param contactId The ID of the contact to retrieve
     * @return The contact object
     * @throws IllegalArgumentException if contact ID does not exist
     */
    public Contact getContact(String contactId) {
        if (contactId == null) {
            throw new IllegalArgumentException("Contact ID cannot be null");
        }

        Contact contact = contacts.get(contactId);
        if (contact == null) {
            throw new IllegalArgumentException("Contact ID does not exist");
        }

        return contact;
    }

    /**
     * Gets the number of contacts in the service
     * @return The number of contacts
     */
    public int getContactCount() {
        return contacts.size();
    }

    /**
     * Checks if a contact exists
     * @param contactId The ID to check
     * @return true if contact exists, false otherwise
     */
    public boolean contactExists(String contactId) {
        return contacts.containsKey(contactId);
    }
}
