import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ContactService {
    private Map<String, Contact> contacts;

    // Secondary hash-based index: lowercased last name -> matching contact IDs.
    // Trades O(n) extra space for O(1) average-time exact last-name lookup.
    private Map<String, Set<String>> lastNameIndex;

    // Trie over (lowercased) first and last names for O(k) prefix search/autocomplete,
    // where k is the length of the prefix.
    private TrieNode nameTrie;

    /**
     * Constructor initializes the contact storage and the secondary indexes
     */
    public ContactService() {
        this.contacts = new HashMap<>();
        this.lastNameIndex = new HashMap<>();
        this.nameTrie = new TrieNode();
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
        addToLastNameIndex(contact.getLastName(), contact.getContactId());
        insertIntoTrie(contact.getFirstName(), contact.getContactId());
        insertIntoTrie(contact.getLastName(), contact.getContactId());
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

        Contact contact = contacts.get(contactId);
        if (contact == null) {
            throw new IllegalArgumentException("Contact ID does not exist");
        }

        contacts.remove(contactId);
        removeFromLastNameIndex(contact.getLastName(), contactId);
        removeFromTrie(contact.getFirstName(), contactId);
        removeFromTrie(contact.getLastName(), contactId);
    }

    /**
     * Updates the first name of a contact
     * @param contactId The ID of the contact to update
     * @param firstName The new first name
     * @throws IllegalArgumentException if contact ID does not exist
     */
    public void updateFirstName(String contactId, String firstName) {
        Contact contact = getContact(contactId);
        String oldFirstName = contact.getFirstName();
        contact.setFirstName(firstName);

        removeFromTrie(oldFirstName, contactId);
        insertIntoTrie(contact.getFirstName(), contactId);
    }

    /**
     * Updates the last name of a contact
     * @param contactId The ID of the contact to update
     * @param lastName The new last name
     * @throws IllegalArgumentException if contact ID does not exist
     */
    public void updateLastName(String contactId, String lastName) {
        Contact contact = getContact(contactId);
        String oldLastName = contact.getLastName();
        contact.setLastName(lastName);

        removeFromLastNameIndex(oldLastName, contactId);
        addToLastNameIndex(contact.getLastName(), contactId);

        removeFromTrie(oldLastName, contactId);
        insertIntoTrie(contact.getLastName(), contactId);
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

    /**
     * Looks up all contacts with an exact (case-insensitive) last name using the
     * secondary hash index.
     * @param lastName The last name to look up
     * @return List of contacts with that last name, empty if none match
     * @throws IllegalArgumentException if lastName is null
     */
    public List<Contact> getContactsByLastName(String lastName) {
        if (lastName == null) {
            throw new IllegalArgumentException("Last name cannot be null");
        }

        List<Contact> results = new ArrayList<>();
        Set<String> ids = lastNameIndex.get(lastName.toLowerCase());
        if (ids != null) {
            for (String id : ids) {
                results.add(contacts.get(id));
            }
        }
        return results;
    }

    /**
     * Finds every contact whose first or last name starts with the given prefix,
     * using the name trie. Cost is O(k) to walk the prefix plus O(m) to collect
     * the m matches found beneath it.
     * @param prefix The prefix to search for
     * @return List of matching contacts, empty if none match
     * @throws IllegalArgumentException if prefix is null
     */
    public List<Contact> searchByPrefix(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("Prefix cannot be null");
        }

        TrieNode node = nameTrie;
        for (char c : prefix.toLowerCase().toCharArray()) {
            node = node.children.get(c);
            if (node == null) {
                return new ArrayList<>();
            }
        }

        Set<String> matchingIds = new HashSet<>();
        collectContactIds(node, matchingIds);

        List<Contact> results = new ArrayList<>();
        for (String id : matchingIds) {
            results.add(contacts.get(id));
        }
        return results;
    }

    /**
     * Returns every contact sorted alphabetically by last name, then first name,
     * using a merge sort in O(n log n) time.
     * @return Sorted list of contacts
     */
    public List<Contact> getContactsSortedByName() {
        List<Contact> sorted = new ArrayList<>(contacts.values());
        Comparator<Contact> byLastThenFirst = Comparator
                .comparing(Contact::getLastName)
                .thenComparing(Contact::getFirstName);
        mergeSort(sorted, byLastThenFirst);
        return sorted;
    }

    private void addToLastNameIndex(String lastName, String contactId) {
        lastNameIndex.computeIfAbsent(lastName.toLowerCase(), k -> new HashSet<>()).add(contactId);
    }

    private void removeFromLastNameIndex(String lastName, String contactId) {
        String key = lastName.toLowerCase();
        Set<String> ids = lastNameIndex.get(key);
        if (ids != null) {
            ids.remove(contactId);
            if (ids.isEmpty()) {
                lastNameIndex.remove(key);
            }
        }
    }

    private void insertIntoTrie(String name, String contactId) {
        TrieNode node = nameTrie;
        for (char c : name.toLowerCase().toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new TrieNode());
        }
        node.contactIds.add(contactId);
    }

    private void removeFromTrie(String name, String contactId) {
        TrieNode node = nameTrie;
        for (char c : name.toLowerCase().toCharArray()) {
            node = node.children.get(c);
            if (node == null) {
                return;
            }
        }
        node.contactIds.remove(contactId);
    }

    private void collectContactIds(TrieNode node, Set<String> result) {
        result.addAll(node.contactIds);
        for (TrieNode child : node.children.values()) {
            collectContactIds(child, result);
        }
    }

    private static void mergeSort(List<Contact> list, Comparator<Contact> comparator) {
        if (list.size() <= 1) {
            return;
        }

        int mid = list.size() / 2;
        List<Contact> left = new ArrayList<>(list.subList(0, mid));
        List<Contact> right = new ArrayList<>(list.subList(mid, list.size()));

        mergeSort(left, comparator);
        mergeSort(right, comparator);
        merge(list, left, right, comparator);
    }

    private static void merge(List<Contact> list, List<Contact> left, List<Contact> right,
            Comparator<Contact> comparator) {
        int i = 0, j = 0, k = 0;
        while (i < left.size() && j < right.size()) {
            if (comparator.compare(left.get(i), right.get(j)) <= 0) {
                list.set(k++, left.get(i++));
            } else {
                list.set(k++, right.get(j++));
            }
        }
        while (i < left.size()) {
            list.set(k++, left.get(i++));
        }
        while (j < right.size()) {
            list.set(k++, right.get(j++));
        }
    }
}
