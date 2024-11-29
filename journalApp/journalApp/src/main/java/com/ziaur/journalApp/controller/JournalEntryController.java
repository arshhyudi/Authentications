package com.ziaur.journalApp.controller;

import com.ziaur.journalApp.entity.JournalEntry;
import com.ziaur.journalApp.entity.User;
import com.ziaur.journalApp.service.JournalEntryService;
import com.ziaur.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class  JournalEntryController {
    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("{userName}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String userName) {
        User user = userService.findByUsername(userName);
        List<JournalEntry> all = user.getJournalEntries();
       if(all != null && !all.isEmpty()){
           return new ResponseEntity<>(all, HttpStatus.OK);
       }
       return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping("{userName}")
    public ResponseEntity<JournalEntry> createJournalEntry(@RequestBody JournalEntry myJournalEntry, @PathVariable String userName) {
        User user = userService.findByUsername(userName);
        try {
            myJournalEntry.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(myJournalEntry, userName);
            return new ResponseEntity<>(myJournalEntry, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myId){
       Optional<JournalEntry> journalEntry = journalEntryService.getJournalEntryById(myId);
       if(journalEntry.isPresent()){
           return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
       }
       return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PutMapping("/id/{userName}{myId}")
    public ResponseEntity<?> updateJournalEntry(@PathVariable ObjectId myId,
                                                @RequestBody JournalEntry myJournalEntry,
                                                @PathVariable String userName) {
        JournalEntry oldJournalEntry = journalEntryService.getJournalEntryById(myId).orElse(null);
        if(oldJournalEntry != null){
            oldJournalEntry.setTitle(myJournalEntry.getTitle() != null && !oldJournalEntry.getTitle().equals("") ? myJournalEntry.getTitle() : oldJournalEntry.getTitle());
            oldJournalEntry.setContent(myJournalEntry.getContent() != null && !oldJournalEntry.getContent().equals("") ? myJournalEntry.getContent() : oldJournalEntry.getContent());
            journalEntryService.saveEntry(oldJournalEntry);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/id/{userName}/{myId}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myId, @PathVariable String userName) {
        journalEntryService.deleteEntryById(myId, userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
