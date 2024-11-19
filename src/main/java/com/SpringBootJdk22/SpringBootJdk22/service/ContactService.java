package com.SpringBootJdk22.SpringBootJdk22.service;

import com.SpringBootJdk22.SpringBootJdk22.model.Contact;
import com.SpringBootJdk22.SpringBootJdk22.repository.ContactRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    public Contact saveContact(Contact contact) {
        return contactRepository.save(contact);
    }
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }
    public Optional<Contact> getContactById(Long id) {
        return contactRepository.findById(id);
    }
}
