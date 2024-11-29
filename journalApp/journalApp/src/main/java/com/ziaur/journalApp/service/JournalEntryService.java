package com.ziaur.journalApp.service;

import com.ziaur.journalApp.entity.JournalEntry;
import com.ziaur.journalApp.entity.User;
import com.ziaur.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {
    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName) {
        try {
            User user = userService.findByUsername(userName);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
            userService.saveEntry(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Error saving journal entry");
        }
    }
    public void saveEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }
    public List<JournalEntry> getAllJournalEntries() {
        return journalEntryRepository.findAll();
    }
    public Optional<JournalEntry> getJournalEntryById(@PathVariable ObjectId id) {
        return journalEntryRepository.findById(id);
    }
    public void deleteEntryById(ObjectId id, String userName) {
        User user = userService.findByUsername(userName);
        user.getJournalEntries().removeIf(x -> x.getId().equals(id));
        userService.saveEntry(user);
        journalEntryRepository.deleteById(id);
    }
    public void updateEntryById(@PathVariable ObjectId id, JournalEntry journalEntry) {
        journalEntry.setId(id);
    }
}


//controller ---> service ----> repository