package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use
 * the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations.
 * You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */

@RestController
@Component
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @GetMapping("/messages")
    public @ResponseBody ResponseEntity<List<Message>> getAllMessages() {

        List<Message> listMessage = messageService.getAllMessages();
        return ResponseEntity.status(200).body(listMessage);
    }

    @GetMapping("/messages/{message_id}")
    public @ResponseBody ResponseEntity<Message> getMessagesFromID(@PathVariable int message_id) {

        Message message = messageService.getMessageById(message_id);

        return ResponseEntity.status(200)
                .body(message);

    }

    @GetMapping("/accounts/{account_id}/messages")
    public @ResponseBody ResponseEntity<List<Message>> getMessagesFromAccount(@PathVariable int account_id) {
        // get message from ID from message service
        List<Message> listMessage = messageService.getMessagesByAccount(account_id);
        return ResponseEntity.status(200).body(listMessage);
    }

    @PostMapping(value = "/register")
    public @ResponseBody ResponseEntity<Account> registerAccount(@RequestBody Account newAccount) {

        if (newAccount.getUsername().length() > 0 && newAccount.getPassword().length() > 4) {
            if (accountService.isThereMatchingAccountUsername(newAccount.getUsername())) {
                return ResponseEntity.status(409).body(null);
            } else {
                return ResponseEntity.status(200).body(accountService.registerAccount(newAccount));
            }

        } else
            return ResponseEntity.status(400).body(null);
    }

    @PostMapping(value = "/login")
    public @ResponseBody ResponseEntity<Account> loginAccount(@RequestBody Account newAccount) {

        Account loginAccount = accountService.loginAccount(newAccount);
        if (loginAccount != null) {
            return ResponseEntity.status(200).body(loginAccount);
        } else
            return ResponseEntity.status(401).body(null);

    }

    @PostMapping(value = "/messages")
    public @ResponseBody ResponseEntity<Message> postMessage(@RequestBody Message newMessage) {

        Message newMes = messageService.postMessage(newMessage);

        if (newMes != null) {
            return ResponseEntity.status(200).body(newMes);
        } else
            return ResponseEntity.status(400).body(null);
    }

    
    
    @DeleteMapping("messages/{message_id}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable("message_id") Integer messageId){
        Integer response = messageService.deleteMessageById(messageId);
        return ResponseEntity.status(200).body(response);
    }
    
    // PATCH message
    @PatchMapping(value = "/messages/{message_id}")
    public @ResponseBody ResponseEntity<Integer> patchMessage(@PathVariable int message_id,
            @RequestBody Message updatedMessage) {

        int body = messageService.updateMessage(message_id, updatedMessage.getMessageText());
        if (body > 0) {
            return ResponseEntity.status(200).body(body);
        } else
            return ResponseEntity.status(400).body(null);
    }
}